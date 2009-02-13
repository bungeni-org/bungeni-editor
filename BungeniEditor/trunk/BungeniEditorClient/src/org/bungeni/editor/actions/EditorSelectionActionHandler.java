/*
 * EditorSelectionActionHandler.java
 *
 * Created on December 2, 2007, 10:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.editor.actions.routers.routerFactory;
import org.bungeni.editor.selectors.DialogSelectorFactory;
import org.bungeni.editor.selectors.IDialogSelector;
import org.bungeni.error.BungeniError;
import org.bungeni.error.BungeniMessage;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.error.ErrorMessages;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.CommonPropertyFunctions;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author Administrator
 */
public class EditorSelectionActionHandler implements IEditorActionEvent {
   private static org.apache.log4j.Logger log = Logger.getLogger(EditorSelectionActionHandler.class.getName());
   private OOComponentHelper ooDocument;
   private toolbarSubAction m_subAction;   
   private JFrame parentFrame;
   private toolbarAction m_parentAction;
   private BungeniClientDB dbSettings;
   private ErrorMessages errorMsgObj = new ErrorMessages();
    /** Creates a new instance of EditorSelectionActionHandler */
    public EditorSelectionActionHandler() {
        dbSettings = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, JFrame c) {
    }

    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, JFrame c) {
        //the modes available are either text_select_insert or edit
        this.ooDocument = ooDocument;
        this.parentFrame = c;
        this.m_subAction = action;
        this.m_parentAction = get_parentAction();
        this.m_parentAction.setSelectorDialogMode(action.getSelectorDialogMode());
        
        int nValid = -1;
        //BungeniMessage theMessage ;
        //all error returns < 0 indicate failure and stoppagte
        //all error returns > 0 indicate that processing can go ahead
        //theMessage = _validateAction();
        BungeniValidatorState validObj = _validateAction();
        if (validObj.state)  { //state alright, route action  
            BungeniValidatorState routeObj = _routeAction();
        } else {
            //state false....
            //show error message
            String msg = validObj.msg.getMessageString();
            MessageBox.OK(null, msg);
        }
            /*
        int nRouteAction = -1;
        switch (theMessage.getStep()) {
            case BungeniError.DOCUMENT_LEVEL_ACTION_PROCEED:
            case BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED:
            case BungeniError.TEXT_SELECTED_SYSTEM_ACTION_PROCEED:
               nRouteAction = _routeAction(theMessage);
               break;
            default:
                String message = theMessage.getMessageString();
               MessageBox.OK(c, message);
               //log.debug("There was an error : BungeniError : step: " + theMessage.getStep()+" , message = " + theMessage.getMessage());
               break;
        } */
       
    }
    
     private BungeniValidatorState _routeAction(){
         log.debug("_routeAction : calling _routeAction()");
         org.bungeni.editor.actions.routers.IBungeniActionRouter routerObject = null;
         routerObject = routerFactory.getRouterClass(m_subAction);
         BungeniValidatorState validState = routerObject.route(m_parentAction, m_subAction, parentFrame, ooDocument);
         return validState; 
     }
     
     private int _routeAction(BungeniMessage lastMessage){
        int nRouteAction = BungeniError.METHOD_NOT_IMPLEMENTED;
         switch (m_subAction.getSelectorDialogMode()) {
             case DOCUMENT_LEVEL_ACTION:
                 nRouteAction = routeAction_DocumentLevelAction(lastMessage);
                 break;
                 /*
             case TEXT_SELECTED_EDIT:
                 nRouteAction = routeAction_TextSelectedEditAction(nValidationErrorCode);
                 break;
                  */
             case TEXT_SELECTED_INSERT:
                 nRouteAction = routeAction_TextSelectedInsertAction(lastMessage);
                 break;
                 
             case TEXT_SELECTED_SYSTEM_ACTION:
                 nRouteAction = routeAction_TextSelectedSystemAction(lastMessage);
                 break;
                 
         }
        
        return nRouteAction;
    }
    
     private int routeAction_TextSelectedInsertAction( BungeniMessage lastMessage) {
        int nActionDocument = -1;
        if (m_subAction.sub_action_name().equals("section_creation")) {
            nActionDocument = routeAction_TextSelectedInsertAction_CreateSection(lastMessage);
            return nActionDocument;
        } else
        if (m_subAction.sub_action_name().equals("debatedate_entry")) {
            nActionDocument = routeAction_TextSelectedInsertAction_DebateDateEntry(lastMessage);
            return nActionDocument;
        } else 
        if (m_subAction.sub_action_name().equals("debatetime_entry")) {
            nActionDocument = routeAction_TextSelectedInsertAction_DebateTimeEntry(lastMessage);
            return nActionDocument;
        } else 
        if (m_subAction.sub_action_name().equals("markup_logo")) {
            nActionDocument = routeAction_TextSelectedInsertAction_MarkupLogo(lastMessage);
            return nActionDocument;
        } else
        
        {
            log.debug("validateAction_DocumentLevelAction() : method not implemented");
            return BungeniError.METHOD_NOT_IMPLEMENTED;
        }
     }
     
     private int routeAction_TextSelectedInsertAction_CreateSection(BungeniMessage lastMessage) {
         String newSectionName = "";
         newSectionName = get_newSectionNameForAction(m_parentAction);
         if (newSectionName.length() == 0 ) {
             
         } else {
            boolean bAction = action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set section type metadata
                ooDocument.setSectionMetadataAttributes(newSectionName, get_newSectionMetadata(m_parentAction));
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
            }
         }      
         return 0;
     }
    
    private int routeAction_TextSelectedInsertAction_DebateDateEntry(BungeniMessage lastMessage) {
        displayFilteredDialog();
        return 0;
    }
  
    private int routeAction_TextSelectedInsertAction_DebateTimeEntry(BungeniMessage lastMessage) {
        displayFilteredDialog();
        return 0;
    }
    private int routeAction_TextSelectedInsertAction_MarkupLogo(BungeniMessage lastMessage) {
        //this.m_subAction. 
        String newSectionName = "";
         newSectionName = get_newSectionNameForAction(m_parentAction);
         if (newSectionName.length() == 0 ) {
             
         } else {
            boolean bAction = action_createSystemContainerFromSelection(ooDocument, newSectionName);
            if (bAction ) {
                //set section type metadata
                ooDocument.setSectionMetadataAttributes(newSectionName, get_newSectionMetadata(m_parentAction));
            } else {
                log.error("routeAction_TextSelectedInsertAction_CreateSection: error while creating section ");
            }
         }      
         return 0;
    }

   
    private int routeAction_TextSelectedSystemAction_DebateDateEntry(BungeniMessage lastMessage) {
        return 0;
    }
    
    private int routeAction_TextSelectedSystemAction_DebateTimeEntry(BungeniMessage lastMessage) {
        return 0;
    }

    
     private int routeAction_TextSelectedEditAction( int nValidationErrorCode) {
         return 0;
     }
    
     
     private int routeAction_TextSelectedSystemAction(BungeniMessage lastMessage) {
        int nActionDocument = -1;
    
        if (m_subAction.sub_action_name().equals("debatedate_entry")) {
            nActionDocument = routeAction_TextSelectedSystemAction_DebateDateEntry(lastMessage);
            return nActionDocument;
        } else 
        if (m_subAction.sub_action_name().equals("debatedate_entry")) {
            nActionDocument = routeAction_TextSelectedSystemAction_DebateTimeEntry(lastMessage);
            return nActionDocument;
        } else  {
            log.debug("routeAction_TextSelectedSystemAction() : method not implemented");
            return BungeniError.METHOD_NOT_IMPLEMENTED;
        }
     }

     
     /*
      *
      *Route Action == Document Level Action
      *
      *
      */
     private int routeAction_DocumentLevelAction( BungeniMessage lastMessage) {
          //actions operate on the whole document
        int nActionDocument = -1;
        if (m_subAction.sub_action_name().equals("init_document")) {
            nActionDocument = routeAction_DocumentLevelAction_InitDocument(lastMessage);
            return nActionDocument;
        } else  {
            log.debug("validateAction_DocumentLevelAction() : method not implemented");
            return BungeniError.METHOD_NOT_IMPLEMENTED;
        }
     
     }
     /*
      *
      *Route Action : Document Level Action
      *Routing functions
      */
     private int routeAction_DocumentLevelAction_InitDocument(BungeniMessage lastMessage){
         int nRouteActionReturnValue = BungeniError.METHOD_NOT_IMPLEMENTED;
         int nRetValue = 0;
         String strSection = CommonPropertyFunctions.getDocumentRootSection();

         switch (lastMessage.getMessage()) {
             case BungeniError.DOCUMENT_ROOT_EXISTS:
                //document has root section 
                //1. prompt to erase root section, if yes delete root section
                //2. create fresh root section
                 nRetValue = MessageBox.Confirm(parentFrame, "The document already has a root section.\n " +
                         "Are you sure you want to delete the current root section and recreate it ", "Please Confirm");
                 if (nRetValue == JOptionPane.YES_OPTION) {
                     boolean bReturn = action_removeSectionWithoutContents(ooDocument,  strSection);
                     if (bReturn ) {
                     bReturn = action_createRootSection(ooDocument, strSection); 
                     }
                 } else {
                     return BungeniError.ACTION_CANCELLED;
                 }
                 break;
             case BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST:
                 //1. prompt warning that this will create a root section
                 //2. create root section
                 nRetValue = MessageBox.Confirm(parentFrame, "The document will be initialized now.\n " +
                         "The contents of the document will be enclosed in a container section, Proceed ? ", "Please Confirm");
                 if (nRetValue == JOptionPane.YES_OPTION) {
                   boolean  bReturn = action_createRootSection(ooDocument, strSection); 
                 } else {
                     return BungeniError.ACTION_CANCELLED;
                 }
                 break;
         }
         return nRouteActionReturnValue;
     }
     
 
    
    private boolean action_removeSectionWithoutContents(OOComponentHelper ooDoc, String sectionName ) {
        boolean bResult = false;
        try {
        XTextSection theSection = ooDocument.getSection(sectionName);
        XText docText = ooDocument.getTextDocument().getText();
        docText.removeTextContent(theSection);
        bResult = true;
        } catch (NoSuchElementException ex) {
            log.error("in removeSectionWIthoutContents : "+ex.getLocalizedMessage(), ex);
            bResult = false;
        } finally {
            return bResult;
        }
    }
    
    
  
    /*
     *
     *
     *
If documentHasNoRootSection
	error root section
	return

If documentHasRootSection
	if NoSelectedText
	 	error selected text
	 	return
	 if selectedText
	 	markupSelectedText()
	 	

if markupSelectedText():
	if action requires SystemSection:
		lookup full hierarchy for markupAction
		if (actionHierarchy == sectionHierarchy) 
			do markupAction()
			exit
		if (actionHierarchy <> sectionHierarchy)
			error hierarchy
			disply valid hierarchy 
			exit
	if action does not require SystemSection:
		lookup full hierarchy for markupAction
		if (actionHierarchy == sectionHierarchy) 
			do markupAction()
			exit
		if (actionHierarchy <> sectionHierarchy)
			error hierarchy
			disply valid hierarchy 
			exit
			 	
     *			 	
     *
     *
     */
    
     private BungeniValidatorState _validateAction() {
         //get subAction validator class
         org.bungeni.editor.actions.validators.IBungeniActionValidator validatorObject = null;
         validatorObject = org.bungeni.editor.actions.validators.validatorFactory.getValidatorClass(m_subAction);
         BungeniValidatorState validState = validatorObject.check(this.m_parentAction, this.m_subAction, this.ooDocument);
         return validState;
     }
     private BungeniMessage _validateAction_old(){
        int nValidateAction = BungeniError.METHOD_NOT_IMPLEMENTED;
        BungeniMessage msg = new BungeniMessage();
         switch (m_subAction.getSelectorDialogMode()) {
             case DOCUMENT_LEVEL_ACTION:
                 msg = validateAction_DocumentLevelAction();
                 break;
                 /*
             case TEXT_SELECTED_EDIT:
                 nValidateAction = validateAction_TextSelectedEditAction();
                 break;
                  */
             case TEXT_SELECTED_INSERT:
                 msg = validateAction_TextSelectedInsertAction();
                 break;
                 
             case TEXT_SELECTED_SYSTEM_ACTION:
                 msg = validateAction_TextSelectedSystemAction();
                 break;
                 
         }
         
     return msg
             ;
     }
     
    
    private void displayFilteredDialog(){
              JDialog dlg;
             dlg= new JDialog();
             dlg.setTitle(m_subAction.action_display_text());
             dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
             IDialogSelector panel = DialogSelectorFactory.getDialogClass(m_subAction.dialog_class());
             panel.initObject(ooDocument, dlg, m_parentAction, m_subAction);
             //panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
             //panel.setBackground(new Color(255, 255, 153));
             //initDebaterecord.setTitle("Selection Mode");
             dlg.getContentPane().add(panel.getPanel());
             dlg.pack();
             dlg.setLocationRelativeTo(null);
             dlg.setVisible(true);
             dlg.setAlwaysOnTop(true);   
    }
    /*
    private void displayFilteredDialog() {
             try {
             log.debug("displayFilteredDialog: subAction name = "+ this.m_subAction.sub_action_name());
             // toolbarAction parentAction = getParentAction();
             
             JDialog initDebaterecord;
             initDebaterecord = new JDialog();
             initDebaterecord.setTitle("Enter Settings for Document");
             initDebaterecord.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             //initDebaterecord.setPreferredSize(new Dimension(420, 300));
            
             InitDebateRecord panel = new InitDebateRecord(ooDocument, 
                     initDebaterecord, m_parentAction, m_subAction);
             //panel.setDialogMode(SelectorDialogModes.TEXT_INSERTION);
             //panel.setBackground(new Color(255, 255, 153));
             //initDebaterecord.setTitle("Selection Mode");
             initDebaterecord.getContentPane().add(panel);
             initDebaterecord.pack();
             initDebaterecord.setLocationRelativeTo(null);
             initDebaterecord.setVisible(true);
             initDebaterecord.setAlwaysOnTop(true);   
             } catch (Exception ex) {
                 log.error("displayFilteredDialog : " + ex.getMessage());
                 log.error("displayFilteredDialog: stack trace :  \n" + org.bungeni.ooo.utils.CommonExceptionUtils.getStackTrace(ex));
             }
    }
    */
    
    private toolbarAction get_parentAction(){
        Vector<Vector<String>> resultRows = new Vector<Vector<String>>();
        toolbarAction action = null;
        try {
            String currentDocumentType = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
            dbSettings.Connect();
            QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_FETCH_ACTION_BY_NAME(currentDocumentType, this.m_subAction.parent_action_name()));
            dbSettings.EndConnect();
            if (qr == null ) {
                throw new Exception ("QueryResults returned null");
            }
            if (qr.hasResults()) {
                 HashMap columns = qr.columnNameMap();
                 //child actions are present
                 //call the result nodes recursively...
                 resultRows = qr.theResults();
                 //should alwayrs return a single result
                 Vector<java.lang.String> tableRow = new Vector<java.lang.String>();
                 tableRow = resultRows.elementAt(0);
                 action = new toolbarAction(tableRow, columns );
            }
        } catch (Exception ex) {
            log.error("getParentSection: "+ ex.getMessage());
        } finally {
            return action;
        }
    }  
   
    
    private BungeniMessage validateAction_DocumentLevelAction() {
        //actions operate on the whole document
        int nActionDocument = -1;
        if (m_subAction.sub_action_name().equals("init_document")) {
            BungeniMessage msg = validateAction_DocumentLevelAction_InitDocument();
            return msg;
        } else  {
            log.debug("validateAction_DocumentLevelAction() : method not implemented");
            return new BungeniMessage(BungeniError.DOCUMENT_LEVEL_ACTION_FAIL, BungeniError.METHOD_NOT_IMPLEMENTED); 
        }
    }
   
    private int validateAction_TextSelectedEditAction() {
        
        return 0;
    }

    private BungeniMessage validateAction_TextSelectedInsertAction() {
        BungeniMessage validMessage = new BungeniMessage();
        //check if text was selected
        if ((ooDocument.isTextSelected() == false) || (ooDocument.isTextGraphicObjectSelected() == false)) {
            //fail if no text was selected
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.NO_TEXT_SELECTED);
        }
        
        if (m_subAction.sub_action_name().equals("section_creation")) {
            validMessage = validateAction_TextSelectedInsertAction_CreateSection();
            return validMessage;
        } else  
        if (m_subAction.sub_action_name().equals("debatedate_entry")) {
            validMessage = validateAction_TextSelectedInsertAction_DebateDateEntry();
            return validMessage;
        } else 
        if (m_subAction.sub_action_name().equals("debatetime_entry")) {
            validMessage = validateAction_TextSelectedInsertAction_DebateTimeEntry();
            return validMessage;
        } else
        if (m_subAction.sub_action_name().equals("markup_logo")) {
            validMessage = validateAction_TextSelectedInsertAction_MarkupLogo();
            return validMessage;
        }
        
        
        else  {
            log.debug("validateAction_DocumentLevelAction() : method not implemented");
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.METHOD_NOT_IMPLEMENTED);
        }
        
    }

    /*
     *
     *This is a generic check, because a system action is always a generic action with generic validation
     *
     *
     */
    private BungeniMessage validateAction_TextSelectedSystemAction() {
        int nRetValue  = -1;
        BungeniMessage theMessage = new BungeniMessage();
        //1st check ... look for root
        nRetValue = check_rootContainerExists();
        if (nRetValue == BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST) {
                return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST);
        }
        //2nd check ... if text was selected
        if (ooDocument.isTextSelected() == false) {
            //fail if no text was selected
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.NO_TEXT_SELECTED);
        }
        //3rd check ... if system container can be created here
        if (m_subAction.sub_action_name().equals("debatedate_entry")) {
          theMessage = validateAction_TextSelectedSystemAction_DebateDateEntry();  
          return theMessage;
        } else 
        if (m_subAction.sub_action_name().equals("debatetime_entry")) {
          theMessage = validateAction_TextSelectedSystemAction_DebateTimeEntry();  
          return theMessage;
        } 
        /*
        nRetValue = check_canSystemContainerBeCreated();
        if (nRetValue == BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION) {
            return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION);
        }
        */
        
        return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_PROCEED, nRetValue);
    
    }

    private BungeniMessage validateAction_TextSelectedSystemAction_DebateDateEntry(){
        int nRetValue = -1;
        nRetValue = this.check_systemContainerPositionCheck();
        switch (nRetValue) {
            case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
            case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, nRetValue);
            default:
                break;
        }
       nRetValue = this.check_canSystemContainerBeCreated();
       if (nRetValue == BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION) {
           return  new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, nRetValue);
       } 
      return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_PROCEED, nRetValue);
    }

    private BungeniMessage validateAction_TextSelectedSystemAction_DebateTimeEntry(){
        int nRetValue = -1;
        nRetValue = check_systemContainerPositionCheck();
        switch (nRetValue) {
            case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
            case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, nRetValue);
            default:
                break;
        }
       nRetValue = check_canSystemContainerBeCreated();
       if (nRetValue == BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION) {
           return  new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_FAIL, nRetValue);
       } 
      return new BungeniMessage(BungeniError.TEXT_SELECTED_SYSTEM_ACTION_PROCEED, nRetValue);
    }
    
    private BungeniMessage validateAction_DocumentLevelAction_InitDocument() {
        int nRootCheck =  check_rootContainerExists();
        //whether it exists or not...return the proceed message
        if (nRootCheck == BungeniError.DOCUMENT_ROOT_EXISTS ) {
                return new BungeniMessage(BungeniError.DOCUMENT_LEVEL_ACTION_PROCEED, nRootCheck);
        } else if (nRootCheck == BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST) {
                return new BungeniMessage(BungeniError.DOCUMENT_LEVEL_ACTION_PROCEED, nRootCheck);
        }
        return new BungeniMessage(BungeniError.DOCUMENT_LEVEL_ACTION_FAIL, BungeniError.GENERAL_ERROR);
    }
    
    private BungeniMessage validateAction_TextSelectedInsertAction_CreateSection(){
        int nRetValue = -1;
      
        //1st tier, root container check
        nRetValue = check_rootContainerExists();
        if (nRetValue == BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST) {
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST);
        }
        //2nd tier validation ...check up the hierarchy
        //check if there is a current section, and if the section can be created in the current section
        String currentSectionname = ooDocument.currentSectionName();
        nRetValue = check_containment(currentSectionname);
        if (nRetValue == BungeniError.INVALID_SECTION_CONTAINER) {
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.INVALID_SECTION_CONTAINER);
        }
       
        //3rd tier validation
        //check if section already exists (only for single type section)
        nRetValue = check_actionSectionExists();
        if (nRetValue == BungeniError.SECTION_EXISTS) {
            return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.SECTION_EXISTS);
        }
        
        return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED, nRetValue);    
    }
    
   private BungeniMessage validateAction_TextSelectedInsertAction_DebateDateEntry(){
     int nRetValue = -1;
     /*
     if (m_subAction.system_container().length() > 0 ) { 
         nRetValue = check_systemContainerExists();
         if (nRetValue == BungeniError.SYSTEM_CONTAINER_NOT_PRESENT){
             return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, nRetValue);
         }
         nRetValue = this.check_generic_systemContainerCheck();
             switch (nRetValue) {
                 case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
                 case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                 case BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION:
                     return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, nRetValue);
                 default:
                     return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED, nRetValue);
              }
     } else { */
            String currentSection = ooDocument.currentSectionName();
            nRetValue = this.check_containment(currentSection);
            if (nRetValue == BungeniError.VALID_SECTION_CONTAINER)
                return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED, BungeniError.VALID_SECTION_CONTAINER);
            else 
                return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, BungeniError.INVALID_SECTION_CONTAINER);
     /* } */
 }
     
     
     
   
    private BungeniMessage validateAction_TextSelectedInsertAction_DebateTimeEntry(){
      int nRetValue = -1;
      /*
        nRetValue = check_systemContainerPositionCheck();
        switch (nRetValue) {
            case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
            case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, nRetValue);
            default:
                break;
        }
       */
       nRetValue = check_canSystemContainerBeCreated();
       if (nRetValue == BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION) {
           return  new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, nRetValue);
       } 
      return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED, nRetValue);
    }
    
    
    private BungeniMessage validateAction_TextSelectedInsertAction_MarkupLogo(){
      int nRetValue = -1;
      /*
        nRetValue = check_systemContainerPositionCheck();
        switch (nRetValue) {
            case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
            case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_FAIL, nRetValue);
            default:
                break;
        }
       */
       nRetValue = BungeniError.MARKUP_LOGO_PROCEED;
      return new BungeniMessage(BungeniError.TEXT_SELECTED_INSERT_ACTION_PROCEED, nRetValue);
    }
    
    private int check_actionSectionExists() {
        if (m_parentAction.action_numbering_convention().equals("single")) {
            if (ooDocument.hasSection(m_parentAction.action_naming_convention())) {
                return BungeniError.SECTION_EXISTS;
            } else {
                return BungeniError.SECTION_DOES_NOT_EXIST;
            }
        } else { //if section style is not single then multiple instances of the section can be created.
            return BungeniError.SECTION_DOES_NOT_EXIST;
        }
    }
    
    private int check_containment(String currentSectionname){
         int nRetValue = -1;
         if (currentSectionname.equals(CommonPropertyFunctions.getDocumentRootSection())) {
                //current section is the root section
                //can the section be added inside the root section ?
                nRetValue = check_containment_RootSection(currentSectionname);
            } else {
                //current section is not the root section
                //can the section be added inside this section
                nRetValue = check_containment_Section(currentSectionname);
            }
        return nRetValue;
    }
    
    private int check_containment_RootSection(String currentSectionname) {
        //check if the current section can have the root section as a parent
        String strQuery = "Select count(*) as THE_COUNT from ACTION_PARENT where ACTION_NAME='"+m_parentAction.action_name()+"'";
        dbSettings.Connect();
        QueryResults qr = dbSettings.QueryResults(strQuery);
        dbSettings.EndConnect();
        if (qr.hasResults()) {
            String[] theCount = qr.getSingleColumnResult("THE_COUNT");
            if (theCount[0].equals("0")) {
                // the section can have the root section as its container
                return BungeniError.VALID_SECTION_CONTAINER;
            } else {
                return BungeniError.INVALID_SECTION_CONTAINER;
            }
        } else {
            return BungeniError.INVALID_SECTION_CONTAINER;
        }
    }
    
    private int check_containment_Section(String currentContainerSection) {
        
        int error = BungeniError.GENERAL_ERROR;
        try {
        //get valid parent actions
        String strActionName = m_parentAction.action_name();
        dbSettings.Connect();
        String fetchParentQuery = SettingsQueryFactory.Q_FETCH_PARENT_ACTIONS(strActionName);
        log.debug("checkContainmentSection = " + fetchParentQuery);
        QueryResults qr = dbSettings.QueryResults(fetchParentQuery);
        dbSettings.EndConnect();
        String[] actionSectionTypes = qr.getSingleColumnResult("ACTION_SECTION_TYPE");
        //there can be multiple parents... so we iterate through the array if one of them is a valid parent
        
        HashMap<String,String> sectionMetadata = ooDocument.getSectionMetadataAttributes(currentContainerSection);
        //if (sectionMetadata.get)
        String strDocSectionType = "";
        if (sectionMetadata.containsKey("BungeniSectionType")) {
                strDocSectionType = sectionMetadata.get("BungeniSectionType").trim();
                //check the doc section type against the array of valid action section types
                for (String sectionType: actionSectionTypes) {
                     if (strDocSectionType.equals(sectionType.trim())) {
                            error =  BungeniError.VALID_SECTION_CONTAINER;
                            break;
                     }  else {
                             error = BungeniError.INVALID_SECTION_CONTAINER;
                     }
                 }
          } else {
            error =  BungeniError.INVALID_SECTION_CONTAINER;
        }
        } catch (Exception ex) {
            log.error("check_containmentSection : " + ex.getMessage());
            log.error("check_containmentSection : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return error;
        }
    }
    
    private int check_rootContainerExists(){
        String rootSectionname = CommonPropertyFunctions.getDocumentRootSection();
        if (ooDocument.hasSection(rootSectionname)){
            return BungeniError.DOCUMENT_ROOT_EXISTS;
        } else {
            return BungeniError.DOCUMENT_ROOT_DOES_NOT_EXIST;
        }
    }
    
    private int check_canSystemContainerBeCreated(){
        //this is the actual container section type
        String containerSectionType = m_parentAction.action_section_type();
        //this is the current section where the cursor is, we want to compare actual container
        //section type with required.
        String currentSection = ooDocument.currentSectionName();
        HashMap<String,String> metaMap = ooDocument.getSectionMetadataAttributes(currentSection);
        if (metaMap.containsKey("BungeniSectionType")) {
            String currentSectionType = metaMap.get("BungeniSectionType");
            if (currentSectionType.equals(containerSectionType)){
                return BungeniError.VALID_CONTAINER_FOR_SYSTEM_ACTION;
            } else {
                return BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION;
            }
        } else 
            return BungeniError.INVALID_CONTAINER_FOR_SYSTEM_ACTION;
    }
    
    private int check_systemContainerPositionCheck(){
     String systemContainer = m_subAction.system_container();
        if (ooDocument.hasSection(systemContainer)) {
            String currentSection = ooDocument.currentSectionName();
            if (currentSection.equals(systemContainer)) {
                return BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS;
            } else {
                return BungeniError.SYSTEM_CONTAINER_WRONG_POSITION;
            }
        } else {
            return BungeniError.SYSTEM_CONTAINER_CHECK_OK;
        }     
    }
    
    
    private int check_systemContainerExists() {
        String systemContainer = m_subAction.system_container();
        if (ooDocument.hasSection(systemContainer)) {
            return BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS;
        } else {
            return BungeniError.SYSTEM_CONTAINER_NOT_PRESENT;
        } 
            
    }
    private int check_generic_systemContainerCheck(){
       int nRetValue = 0;
        
       nRetValue = check_systemContainerPositionCheck();
        switch (nRetValue) {
            case BungeniError.SYSTEM_CONTAINER_ALREADY_EXISTS:
            case BungeniError.SYSTEM_CONTAINER_WRONG_POSITION:
                return nRetValue;
            default:
                break;
        }
      
        nRetValue = check_canSystemContainerBeCreated();
        
        return nRetValue;
    }
      
       private HashMap<String,String> get_newSectionMetadata(toolbarAction pAction) {
         HashMap<String,String> metaMap = new HashMap<String,String>();
         metaMap.put("BungeniSectionType", pAction.action_section_type());
         return metaMap;
     }
     private String get_newSectionNameForAction(toolbarAction pAction) {
         String newSectionName = "";
         if (pAction.action_numbering_convention().equals("single")) {
             newSectionName = pAction.action_naming_convention();
         } else if (pAction.action_numbering_convention().equals("serial")) {
             String sectionPrefix = pAction.action_naming_convention();
             for (int i=1 ; ; i++) {
                if (ooDocument.hasSection(sectionPrefix+i)) {
                    continue;
                } else {
                    newSectionName = sectionPrefix+i;
                    break;
                }
             }
           
         } else {
             log.error("get_newSectionNameForAction: invalid action naming convention: "+ m_parentAction.action_naming_convention());
         }
         return newSectionName;
    }
    
    private int action_initDocument(){
        return 0;
    }

    
    private boolean action_createRootSection(OOComponentHelper ooDoc, String sectionName) {
        boolean bResult = false;
        try {
            XText docText = ooDocument.getTextDocument().getText();
            XTextCursor docCursor = docText.createTextCursor();
            docCursor.gotoStart(false);
            docCursor.gotoEnd(true);
            XTextContent theContent = ooDocument.createTextSection(sectionName, (short)1);
            docText.insertTextContent(docCursor, theContent, true);
            bResult = true;
        } catch (IllegalArgumentException ex) {
            log.error("in action_createRootSection :" + ex.getMessage());
            log.error("in action_createRootSection :" + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return bResult;
        }
    }
    
    private boolean action_createSystemContainerFromSelection(OOComponentHelper ooDoc, String systemContainerName){
        boolean bResult = false; 
        try {
        XTextViewCursor xCursor = ooDocument.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDocument.createTextSection(systemContainerName, (short)1);
        xText.insertTextContent(xCursor, xSectionContent , true); 
        bResult = true;
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            bResult = false;
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }  finally {
            return bResult;
        }
    }

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, JFrame parentFrame) {
    }


    

 


   }
