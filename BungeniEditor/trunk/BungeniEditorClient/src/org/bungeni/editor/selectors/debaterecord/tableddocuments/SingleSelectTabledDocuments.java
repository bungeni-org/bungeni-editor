/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors.debaterecord.tableddocuments;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author undesa
 */
public class SingleSelectTabledDocuments extends TabledDocuments {
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SingleSelectTabledDocuments.class.getName());

    public SingleSelectTabledDocuments() {
            super();
        }

    @Override
        protected void initTable(){
            super.initTable();
            this.tbl_tabledDocs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    
    private void markupLink(){
        try {
        //it will always return a single selection
        HashMap<String, ArrayList<String>> arrTableSelection = this.getTableSelection();
        OOComponentHelper ooDocument = getContainerPanel().getOoDocument();
        //oStart = docComponent.currentcontroller.getViewCursor().getStart()
        XTextRange xStartRange = ooDocument.getViewCursor().getStart();
        //oText = docComponent.currentController.getviewcursor().getText()
        XText xCursorText = ooDocument.getViewCursor().getText();
        //
        XTextCursor startCur=xCursorText.createTextCursorByRange(xStartRange);
        ArrayList<String> tblDocTitles = arrTableSelection.get("tabled_document_titles");
        ArrayList<String> tblDocURIs = arrTableSelection.get("tabled_document_uris");
       //For i=LBound(selectItemsArray) to UBound(selectItemsArray)
        for (int i=0; i < tblDocTitles.size(); i++) {
      
                //oCur.HyperLinkURL="http://akomantoso.org/resolver/"+ listItemURIs(i)
                XPropertySet xCurProps = ooQueryInterface.XPropertySet(startCur);
                xCurProps.setPropertyValue("HyperLinkURL", __ODF_URI_PREFIX__ + tblDocURIs.get(i));
                xCursorText.insertString(startCur, tblDocTitles.get(i), false);
               // if (!(i == tblDocTitles.size() -1 ))
                xCursorText.insertControlCharacter(ooDocument.getViewCursor(), com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
        }
        
            } catch (IllegalArgumentException ex) {
                log.error("markupLink : " + ex.getMessage(),ex);
            } catch (UnknownPropertyException ex) {
                log.error("markupLink : " + ex.getMessage(),ex);
            } catch (PropertyVetoException ex) {
                log.error("markupLink : " + ex.getMessage(),ex);
            }  catch (WrappedTargetException ex) {
                log.error("markupLink : " + ex.getMessage(),ex);
            }
        
    }
   

    @Override
    public boolean processSelectInsert() {
        markupLink();
        return true;
    }

}
