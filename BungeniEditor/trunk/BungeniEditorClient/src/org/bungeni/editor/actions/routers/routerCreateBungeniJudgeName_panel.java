/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.bungeni.error.BungeniValidatorState;

/**
 *
 * @author undesa
 */
public class routerCreateBungeniJudgeName_panel extends routerCreateTabularMetadataReference_panel {
    
    /**
     * This abstract method is the method that should be used to insert the tabular reference 
     * It provides table model / vector columns and rows for the data 
     * It is upto the implementing class to interpret the tabular data
     * @param model
     * @param vCols
     * @param vRows
     */
    @Override
    public void makeAndInsertReference(DefaultTableModel model, Vector vCols, Vector vRows) {
        //the subaction value has the reference name.
        //column 3 has the uri (first name, last name , uri)
        //we build the metadata reference using the subaction value and column 3
        String uriString = (String) vRows.elementAt(2);
        String fullName = (String) vRows.elementAt(0) + " " + vRows.elementAt(1);
        String metaPrefix = theSubAction.action_value();
        String metaName = metaPrefix + ":" + uriString;
        String fullRefString = metaName + ";" + fullName ;
        String documentRefString = fullRefString + " ;#1";
        int i = 1;
        while (ooDocument.getReferenceMarks().hasByName(documentRefString) ) {
            documentRefString = fullRefString + " ;#" + ++i;
        }
        theSubAction.setActionValue(documentRefString);
        routerCreateReference rcf = new routerCreateReference();
        BungeniValidatorState bvState = rcf.route_TextSelectedInsert(theAction, theSubAction, parentFrame, ooDocument);
        containerFrame.dispose();
    }

}
