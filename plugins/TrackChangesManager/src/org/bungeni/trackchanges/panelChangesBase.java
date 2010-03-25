package org.bungeni.trackchanges;

import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.bungeni.odfdocument.docinfo.BungeniChangeDocumentsInfo;
import org.bungeni.odfdocument.docinfo.BungeniDocAuthor;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class panelChangesBase extends JPanel implements IChangesPanel  {
     BungeniChangeDocumentsInfo changesInfo = new BungeniChangeDocumentsInfo();
     ResourceBundle bundleBase = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");
     String                 PANEL_FILTER_REVIEW_STAGE = "";
     String                 PANEL_REVIEW_STAGE = "";
     JFrame parentFrame ;


   public panelChangesBase(){

   }

   public panelChangesBase(JFrame parentFrm){
       this.parentFrame = parentFrm;
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
}
