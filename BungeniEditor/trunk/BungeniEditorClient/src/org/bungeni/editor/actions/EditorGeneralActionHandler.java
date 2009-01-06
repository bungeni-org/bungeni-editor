/*
 * EditorGeneralActionHandler.java
 *
 * Created on January 21, 2008, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import java.util.ArrayList;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.bungeni.commands.CommonActions;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.error.ErrorMessages;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public class EditorGeneralActionHandler implements IEditorActionEvent {
   private static org.apache.log4j.Logger log = Logger.getLogger(EditorGeneralActionHandler.class.getName());
   private OOComponentHelper ooDocument;
   private JFrame parentFrame;
   private BungeniClientDB dbSettings;
   private String[] generalAction;
   private ErrorMessages errorMsgObj = new ErrorMessages();
    /** Creates a new instance of EditorGeneralActionHandler */
    public EditorGeneralActionHandler() {
        dbSettings = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, JFrame parentFrame) {
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, JFrame parentFrame) {
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {
           this.ooDocument = ooDocument;
           this.parentFrame = parentFrame;
           this.generalAction = action.toArray(new String[action.size()]);
           if (generalAction[1].equals("createRootSection")) {
               doCreateRootSection();
           }
    }
    
    private void doCreateRootSection(){
        String currentDocType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String rootSectionName = BungeniEditorProperties.getEditorProperty("root:"+currentDocType);
        CommonActions.action_createRootSection(ooDocument, rootSectionName);
    }
}
