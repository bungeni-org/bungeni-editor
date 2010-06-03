package org.bungeni.trackchanges.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.bungeni.odfdocument.docinfo.BungeniChangeDocumentsInfo;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.bungeni.trackchanges.utils.ReviewDocuments;

/**
 *
 * @author Ashok Hariharan
 */
public class panelChangesBase extends JPanel implements IChangesPanel  {
     String m_panelName;
    BungeniChangeDocumentsInfo changesInfo = new BungeniChangeDocumentsInfo();
     ResourceBundle bundleBase = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");
     String                 PANEL_FILTER_REVIEW_STAGE = "";
     String                 PANEL_REVIEW_STAGE = "";
     JFrame parentFrame ;


   public panelChangesBase(){

   }

   public panelChangesBase(JFrame parentFrm, String panelName){
       this.parentFrame = parentFrm;
       this.m_panelName = panelName;
   }


    public void updatePanel(HashMap<String,Object> infomap) {
        //do nothing
    }

    public IChangesContainer getContainerInterface(){
        IChangesContainer changeContainer = (IChangesContainer)((JTabbedPane)getParent()).getParent();
        return changeContainer;
    }

    public synchronized BungeniChangeDocumentsInfo getChangesInfo() {
        return changesInfo;
    }

    public String getPanelName() {
        return m_panelName;
    }

    public void setPanelName(String panelName) {
        this.m_panelName = panelName;
    }

    /**
     * Common list model class used by derived classes
     */
    class DocOwnerListModel extends AbstractListModel {

         public int getSize() {
          return changesInfo.getSize();
        }

        public Object getElementAt(int arg0) {
           return getAuthorAtIndex(arg0);
        }

        public BungeniDocAuthor getAuthorAtIndex(int iIndex) {
            BungeniOdfDocumentHelper docHelper =  changesInfo.getDocuments().get(iIndex);
            return new BungeniDocAuthor(docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor"), "");
        }

    }


    protected void loadFilesFromFolder() {
        String currentBillFolder =
            CommonFunctions.getWorkspaceForBill((String) AppProperties.getProperty("CurrentBillID"));

        if (currentBillFolder.length() > 0) {
            File fFolder = new File(currentBillFolder);

            // find files in changes folder
            if (fFolder.isDirectory()) {
                File[] files = fFolder.listFiles(new FilenameFilter() {
                    Pattern pat = Pattern.compile(
                                      ReviewDocuments.getReviewStage(
                                          PANEL_FILTER_REVIEW_STAGE).getDocumentFilterPattern());    // ("clerk_u[0-9][0-9][0-9][0-9]([a-z0-9_-]*?).odt");
                    public boolean accept(File dir, String name) {
                        if (pat.matcher(name).matches()) {
                            return true;
                        }

                        return false;
                    }
                });

                changesInfo.reload(files);
            }
        }
    }
}
