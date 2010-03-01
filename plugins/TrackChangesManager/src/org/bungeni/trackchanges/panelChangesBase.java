package org.bungeni.trackchanges;

import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.bungeni.odfdocument.docinfo.BungeniChangeDocumentsInfo;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class panelChangesBase extends JPanel implements IChangesPanel  {
     BungeniChangeDocumentsInfo changesInfo = new BungeniChangeDocumentsInfo();
     ResourceBundle bundleBase = java.util.ResourceBundle.getBundle("org/bungeni/trackchanges/Bundle");

     JFrame parentFrame ;


   public panelChangesBase(){

   }

   public panelChangesBase(JFrame parentFrm){
       this.parentFrame = parentFrm;
   }


    public void updatePanel() {
        //do nothing
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

        public String getAuthorAtIndex(int iIndex) {
            BungeniOdfDocumentHelper docHelper =  changesInfo.getDocuments().get(iIndex);
            return docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
        }

    }
}
