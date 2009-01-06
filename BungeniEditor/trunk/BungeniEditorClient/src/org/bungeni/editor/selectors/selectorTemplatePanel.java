/*
 * selectorTemplatePanel.java
 *
 * Created on September 19, 2007, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 *   

 *
 *
 *
 *
 *
 *

 *
 *
 *
 *Mandatory methods to override in derived class

public String getClassName(){
	return this.getClass().getName();
}

protected void createContext(){

     super.createContext();

     formContext.setBungeniForm(this);

 }
   
   
 public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {

    super.initObject( ooDoc, dlg, act, subAct);

    init();

    setControlModes();

    // setControlData();

}

 *
 *
 *
 *
 *
 */


   
    
package org.bungeni.editor.selectors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.bungeni.commands.chains.BungeniCatalogCommand;
import org.bungeni.commands.chains.BungeniCommandsCatalogLoader;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.BungeniRegistryFactory;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.utils.CommonPropertyFunctions;
import org.bungeni.utils.MessageBox;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Administrator
 */
public class selectorTemplatePanel extends javax.swing.JPanel 
                implements IDialogSelector {
   
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(selectorTemplatePanel.class.getName());
 
    protected OOComponentHelper ooDocument;
    protected JDialog parent;
    protected toolbarAction theAction =null ;
    protected toolbarSubAction theSubAction = null;
    protected boolean FORM_APPLY_NO_ERROR = false;
    protected SelectorDialogModes theMode;
    protected BungeniClientDB dbInstance=null;
    protected BungeniClientDB dbSettings = null;
    protected HashMap<String,String> theSerializationMap = new HashMap<String,String>();
    /*List of metadata in the document*/
    protected HashMap<String,String> theMetadataMap = new HashMap<String,String>();
    /*Map that stores variables to be passed to command chain handler*/
    protected HashMap<String,Object> thePreInsertMap = new HashMap<String, Object>();  
    /*Stores the command chain for the form*/
    protected HashMap<SelectorDialogModes,BungeniCatalogCommand> theCatalogCommands = new HashMap<SelectorDialogModes,BungeniCatalogCommand>();
    /*Title of the window*/
    protected String windowTitle;
    /*Form context object used by the command chain*/
    protected BungeniFormContext formContext;    
 
    /*List of all named controls in the document*/
    protected HashMap<String,Component> theControlMap = new HashMap<String,Component>();
    /*List of enabled controls in the form - a subset of theControlMap*/
    protected ArrayList<String> enabledControls = new ArrayList<String>(); 
    /*Controls and the corresponding data in the controls */
    protected HashMap<String, Object> theControlDataMap = new HashMap<String, Object>();
    /*Map of controls to be disabled or to set as readonly - used to generate the enabled controls map*/
    protected HashMap<String, controlState> controlStateMap = new HashMap<String, controlState>();

    class dlgBackgrounds {
        Color background;
        String windowTitle = "";
        dlgBackgrounds(SelectorDialogModes mode) {
            if (mode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
                  background = new Color(255, 255, 153);
                  windowTitle = "Selection Mode Insert";
              } else 
            if (mode == SelectorDialogModes.TEXT_SELECTED_EDIT) {
                  background = new Color(255, 255, 153);
                  windowTitle = "Selection Mode Edit";
              } else 
            if (mode == SelectorDialogModes.TEXT_INSERTION){
                  background = new Color(204, 255, 153);
                  windowTitle = "Insertion Mode";
            } else 
            if (mode == SelectorDialogModes.TEXT_EDIT){
                  background = new Color(150, 255, 153);
                  windowTitle = "Edit Mode";
            } else {
                background = new Color (100, 255, 153);
                windowTitle = "Mode not Selected";
            }
        }
        
        Color getBackground() {
            return background;
        }
        String getTitle() {
            return windowTitle;
        }
        }
    
    
    /** Creates a new instance of selectorTemplatePanel */
    public selectorTemplatePanel() {
    }
    
    public selectorTemplatePanel (OOComponentHelper ooDocument, 
            JDialog parentDlg, 
            toolbarAction theAction) {
            initVariables(ooDocument, parentDlg, theAction);
    }

    public selectorTemplatePanel(OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction, toolbarSubAction theSubAction) {
            initVariables(ooDocument, parentDlg, theAction, theSubAction);
    }
    
    protected void initVariables (OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction, toolbarSubAction theSubAction) {
       initVariables(ooDocument, parentDlg, theAction);
       this.theSubAction = theSubAction;
    }
    
    protected void initVariables (OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction) {
        this.ooDocument = ooDocument;
        this.parent = parentDlg;
        this.theAction = theAction;
        this.theMode = theAction.getSelectorDialogMode();
        dlgBackgrounds bg = new dlgBackgrounds(theMode);
        this.setBackground(bg.getBackground());
        this.windowTitle = bg.getTitle(); 
        HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
        this.dbInstance = new BungeniClientDB(registryMap);
        this.dbSettings = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        this.theSubAction = null;
    }
    
    protected void init(){
        createContext();
        createCommandChain();
        
    }
    
    //this is overriden from the derived class and invoked form the derived as super.createContext();
    protected void createContext(){
        formContext = new BungeniFormContext();
        formContext.setTheAction(theAction);
        formContext.setTheSubAction(theSubAction);
        formContext.setOoDocument(ooDocument);
        formContext.setPreInsertMap(thePreInsertMap);
    }
    
    public void setDialogMode(SelectorDialogModes mode) {
        this.theMode = mode;
    }

    public SelectorDialogModes getDialogMode() {
        return this.theMode;
    }

    public String getWindowTitle() {
        return windowTitle;
    }
    public void setOOComponentHelper(OOComponentHelper ooComp) {
        this.ooDocument=ooComp;
    }

    public void setToolbarAction(toolbarAction action) {
        this.theAction = action;
    }

    public void setParentDialog(JDialog dlg) {
        this.parent = dlg;
    }
    
    public void setSectionMetadataForAction(String sectionName, toolbarAction action) {
      
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        sectionMeta.put("BungeniSectionType", theAction.action_section_type());
        ooDocument.setSectionMetadataAttributes(sectionName, sectionMeta);
      
    }
    
    public void setSectionMetadataSectionType (String sectionName, String sectionType) {
      
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        sectionMeta.put("BungeniSectionType",sectionType);
        ooDocument.setSectionMetadataAttributes(sectionName, sectionMeta);
        
    }
       
    public HashMap<String,String> getSectionMetadataForAction(String sectionName) {
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        return ooDocument.getSectionMetadataAttributes(sectionName);
    }    
 
    public void buildComponentsArray() {
        getComponentsWithNames(this);
    }
    
   private void getComponentsWithNames(Container container) {
        
        for (Component component: container.getComponents()){
           String strName = null;
                       
           strName = component.getName();
           if (strName != null) {
               theControlMap.put(strName.trim(), component);
           }
     
           if (component instanceof JRootPane) {    
              JRootPane nestedJRootPane = (JRootPane)component;
              getComponentsWithNames(nestedJRootPane.getContentPane());
            }

           if (component instanceof JPanel) {
              // JPanel found. Recursing into this panel.
              JPanel nestedJPanel = (JPanel)component;
              getComponentsWithNames(nestedJPanel);
            }
           
           if (component instanceof JScrollPane) {
               JScrollPane nestedJScroller = (JScrollPane) component;
               getComponentsWithNames(nestedJScroller.getViewport());
           }
   
        }
        
    }
   
   //selection mode api
   
     //move to base class implementation
    protected ArrayList<String> checkFieldsMessages = new ArrayList<String>(0);
    
    protected boolean checkFields() {
        boolean fieldCheck = true;
        Iterator<String> enabledFields = enabledControls.iterator();
        while (enabledFields.hasNext()) {
            String fieldName = enabledFields.next();
            boolean tmpCheck = checkField(fieldName);
            //if any of the fields fails the checks, record it as a global failure
            if (tmpCheck == false) fieldCheck = false;
        }
        //validations passed with flying colors
        if (fieldCheck) this.FORM_APPLY_NO_ERROR = true;
        return fieldCheck;
    }
    
    protected void displayFieldErrors () {
        MessageBox.OK(parent, checkFieldsMessages.toArray());
        checkFieldsMessages.clear();
    }
    //move to base class implementation
    private boolean checkField (String fieldName) {
        Component componentField = theControlMap.get(fieldName);
        if (isValidationRequired(componentField)) {
            Object fieldValue =  getFieldValue(componentField);
            if (fieldValue == null ) {
                checkFieldsMessages.add("Field :"+ fieldName+ " cannot be blank!");
                return false;
            }
            boolean validationCheck = validateFieldValue(componentField, fieldValue);
            return validationCheck;
        } else 
            return true;
    }

   ///move to base class
    private Object getFieldValue (Component field) {
          if (field.getClass() == org.jdesktop.swingx.JXDatePicker.class ){
           JXDatePicker dateField = (JXDatePicker)field;
           Date fieldValue = dateField.getDate();
           return fieldValue;
        } else if (field.getClass() == javax.swing.JTextField.class) {
            JTextField textField = (JTextField) field;
            String fieldValue = textField.getText();
            return fieldValue ;
        } else if (field.getClass() == javax.swing.JList.class) {
            JList listField = (JList) field;
            Object fieldValue = listField.getSelectedValue();
            return fieldValue;
        } else if (field.getClass() == javax.swing.JComboBox.class)  {
            JComboBox comboField = (JComboBox) field;
            Object fieldValue = comboField.getSelectedItem();
            return fieldValue;
        } else if (field.getClass() == javax.swing.JTextArea.class) {
            JTextArea textareaField = (JTextArea) field;
            Object fieldValue = textareaField.getText();
            return fieldValue;
        } else if (field.getClass() == javax.swing.JSpinner.class ) {
            JSpinner spinnerField = (JSpinner) field;
            Object fieldValue = spinnerField.getValue();
            return fieldValue;
        } else if (field.getClass() == javax.swing.JTable.class) {
           //table validation  handle manually in overriding class
            Object fieldValue = "javax.swing.JTable.class";
           return fieldValue;
        } 
        else {
            log.debug("getFieldValue: "+ field.getClass().getName()+ " field type not supported!");
            return null;
        }
        
    }
     
    protected boolean validateFieldValue(Component field, Object fieldValue) {
        return true;
    }
    
    protected ArrayList<String> getEnabledControls() {
          return this.enabledControls;
      }
 //move to base class
    private static Class[] validateControlTypes = {
        org.jdesktop.swingx.JXDatePicker.class,
        javax.swing.JTextField.class,
        javax.swing.JList.class,
        javax.swing.JComboBox.class,
        javax.swing.JTextArea.class,
        javax.swing.JSpinner.class,
        javax.swing.JTable.class
    };

   
    private boolean isValidationRequired(Component c) {
        //add additional field types if required...
        for (int i=0; i < validateControlTypes.length; i++ ) {
            if (validateControlTypes[i] == c.getClass() )
                return true;
        }
        return false;
    }
    
    public void makeFieldReadOnly(Component field){
            if (field.getClass() == org.jdesktop.swingx.JXDatePicker.class ){
               JXDatePicker dateField = (JXDatePicker)field;
               dateField.setEditable(false);
            } else if (field.getClass() == javax.swing.JTextField.class) {
                JTextField textField = (JTextField) field;
                textField.setEditable(false);
            } else if (field.getClass() == javax.swing.JComboBox.class)  {
                JComboBox comboField = (JComboBox) field;
                comboField.setEditable(false);
           } else if (field.getClass() == javax.swing.JTextArea.class) {
                JTextArea textareaField = (JTextArea) field;
                textareaField.setEditable(false);
           } else if (field.getClass() == javax.swing.JSpinner.class ) {
                JSpinner spinnerField = (JSpinner) field;
                spinnerField.getEditor().getComponent(0).setEnabled(false);
           } else if (field.getClass() == javax.swing.JTable.class) {
                javax.swing.JTable tableField = (javax.swing.JTable) field;
                tableField.removeEditor();
                tableField.setEnabled(false);   
           } else {
                field.setEnabled(false);
           }
    }

 public void setControlModes() {
        //set selection mode control modes
         getEnabledControlList();
         if (theControlMap.keySet().size() > 0 ) {
             Iterator<String> iterCtlNames = theControlMap.keySet().iterator();
             while (iterCtlNames.hasNext()) {
                 String controlname =   iterCtlNames.next();
                 if (!enabledControls.contains(controlname)) {
                     //disable all these controls
                     if (theControlMap.containsKey(controlname)) {
                         controlState ctlState = controlStateMap.get(controlname);
                         switch (ctlState.controlMode) {
                             case hidden:
                                theControlMap.get(controlname).setVisible(false);     
                                break;
                             case readonly:
                                 Component field = theControlMap.get(controlname);
                                makeFieldReadOnly(field);
                                break; 
                         }
                     }
                 }
             }
         } else {
             log.debug("selectionControlModes: no controls with names were found");
         }
      }    

 protected void getEnabledControlList() {
        switch (getDialogMode() ) {
            case TEXT_INSERTION:
                getEnabledControlList_TextInsertion();
                break;
            case TEXT_EDIT :
                getEnabledControlList_TextEdit();
                break;
            case TEXT_SELECTED_EDIT:
            case TEXT_SELECTED_INSERT:
                getEnabledControlList_TextSelection();
                break;
                
        }
             //add elements from ignore list
         /*
         for (int i=0; i < this.controls_ignore_list.length ; i++ ) {
             enabledControls.add(controls_ignore_list[i]);
         }
          */
      } 
   
  protected void createCommandChain(){
    dbSettings.Connect();
    String formName = getClassName();
    String cmdsByForm = SettingsQueryFactory.Q_FETCH_COMMANDS_BY_FORM(formName);
    log.info("createCommandChain: " + cmdsByForm);
    QueryResults qr = dbSettings.QueryResults(cmdsByForm);
    dbSettings.EndConnect();
    if (qr.hasResults()) {
         Vector<Vector<String>> resultRows  = new Vector<Vector<String>>();
         resultRows = qr.theResults(); 
         for (Vector<String> resultRow : resultRows) {
            BungeniCatalogCommand catalogCommand = new BungeniCatalogCommand();
            catalogCommand.setFormName(formName);
            catalogCommand.setCatalogSource(resultRow.elementAt(qr.getColumnIndex("CATALOG_SOURCE") - 1));
            catalogCommand.setCommandCatalog(resultRow.elementAt(qr.getColumnIndex("COMMAND_CATALOG") - 1 ));
            catalogCommand.setFormMode(resultRow.elementAt(qr.getColumnIndex("FORM_MODE") - 1));
            catalogCommand.setCommandChain(resultRow.elementAt(qr.getColumnIndex("COMMAND_CHAIN") - 1));
            theCatalogCommands.put(catalogCommand.getFormMode(), catalogCommand);
//resultRow.
         }   
    }
           
  }
  
  /*
   *Always override in derived class
   *
   */
  public String getClassName(){
        return selectorTemplatePanel.class.getName();
  }
  
  
  
  static enum enum_controlMode {hidden, readonly};
  class controlState {
        String controlName;
        enum_controlMode controlMode;
        controlState (String name, String mode) {
            controlName = name;
            setControlMode(mode);
        }
        
        void setControlMode(String mode) {
            try {
            this.controlMode = enum_controlMode.valueOf(mode);
            } catch (Exception ex) {
                log.error("enum_controlmode, setControlMode = " + ex.getMessage());
            }
        }
  };
    
    
  private class controlsForActionMode {
      HashMap<String, controlState> inactiveControlsForMode = new HashMap<String, controlState>(0);
       
      controlsForActionMode() {
      }
  
      HashMap<String, controlState> getInactiveControlsForMode(){
          String currentMode = getDialogMode().toString();
          String sQuery ="";
          if (theSubAction == null )
            sQuery = SettingsQueryFactory.Q_HIDDEN_FIELDS_FOR_ACTION_MODE(theAction.action_name(), currentMode);
          else
            sQuery = SettingsQueryFactory.Q_HIDDEN_FIELDS_FOR_ACTION_MODE(theAction.action_name(), theSubAction.sub_action_name(), currentMode);    
          
          dbSettings.Connect();
          QueryResults qr = dbSettings.QueryResults(sQuery);
          log.info("getInactiveControlsForMode : " + sQuery);
           dbSettings.EndConnect();
          if (qr.hasResults() ) {
            Vector<Vector<String>> theResults = qr.theResults();
             for (int i = 0; i< theResults.size(); i++ ) {
                Vector<String> tableRow = theResults.elementAt(i);
                String fieldName = tableRow.elementAt(qr.getColumnIndex("MODE_HIDDEN_FIELD")-1);
                String fieldMode = tableRow.elementAt(qr.getColumnIndex("CONTROL_MODE")-1);
                controlState ctlState = new controlState(fieldName, fieldMode);
                inactiveControlsForMode.put(fieldName, ctlState);
             }
                
          }  
          return inactiveControlsForMode ;
      }
  }
  
  /*
      ArrayList<String> getInactiveControlsForMode(){
          String currentMode = getDialogMode().toString();
          String sQuery ="";

          if (theSubAction == null )
            sQuery = SettingsQueryFactory.Q_HIDDEN_FIELDS_FOR_ACTION_MODE(theAction.action_name(), currentMode);
          else
            sQuery = SettingsQueryFactory.Q_HIDDEN_FIELDS_FOR_ACTION_MODE(theAction.action_name(), theSubAction.sub_action_name(), currentMode);    
          
          dbSettings.Connect();
           QueryResults qr = dbSettings.QueryResults(sQuery);
          log.info("getInactiveControlsForMode : " + sQuery);
           dbSettings.EndConnect();
          String[] hiddenFields = null;
            try {
            hiddenFields = qr.getSingleColumnResult("MODE_HIDDEN_FIELD");
            } catch (NullPointerException ex) {
                log.info("getInactiveControlsForMode: " + ex.getMessage());
                log.info("getInactiveControlsForMode: " + CommonExceptionUtils.getStackTrace(ex));
                hiddenFields = null;
            }
            if (hiddenFields != null ) {
                inactiveControlsForMode = new ArrayList<String>(Arrays.asList(hiddenFields));
            } 
          
          if (inactiveControlsForMode.size() > 0 ) {
              for (String inact: inactiveControlsForMode) {
                  log.info("inactive controls for this mode ("+getDialogMode().toString()+") " + inact);
              }
          }
          return inactiveControlsForMode ;
      } */

  private HashMap<String, controlState> getInactiveControlsForMode(){
    controlsForActionMode actionMode = new controlsForActionMode();
    this.controlStateMap = actionMode.getInactiveControlsForMode();
    return this.controlStateMap;
  }
  
  
    protected void getEnabledControlListCommon() {
        //we filter the list of controls
        //contains the list of hidden and readonly fields
            HashMap<String, controlState> arrHiddenFields = getInactiveControlsForMode();
            //default is to enable all controls in text insertion
            Iterator<String> controlNames = theControlMap.keySet().iterator();
            while(controlNames.hasNext()) {
                String controlName = controlNames.next();
                if (arrHiddenFields.size() > 0 ) {
                    if (!arrHiddenFields.containsKey(controlName)) {
                         enabledControls.add(controlName); 
                    }
                } else {
                    enabledControls.add(controlName);
                }
            }
    }
    
    protected void getEnabledControlList_TextInsertion() {
        getEnabledControlListCommon();
    }
    
    protected void getEnabledControlList_TextEdit(){
        getEnabledControlListCommon();
    }
    
    protected void getEnabledControlList_TextSelection() {
        getEnabledControlListCommon();
     }
    
    
    //full insert events
    
    /*
     *Event that traps full insert events....
     *You dont have to override this, as this takes care of routing
     *..unless of course you want to override the routing
     *over-ride the pre/process/post insert functions in the dialog classes
     */
     protected void applyFullInsert() {
       log.debug("in applyFullInsert()");
       if (preValidation() == false) {
           if (!checkFieldsMessages.isEmpty()) {
               displayFieldErrors();
               return;
           }
       }
       if (checkFields() == false) {
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }
        }
       if (preFullInsert() == false) {
            //activities needed to be done before a full insert
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   
       if (processFullInsert() == false) {
            //activities needed to be done during a full insert 
           if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }   
       }   
       if (postFullInsert() == false) {
            //activities to be done after a full insert  
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   else {
           parent.dispose();
       }
    }
     
     
   public boolean preValidation(){
        switch (getDialogMode()) {
                case TEXT_SELECTED_EDIT :
                    return preValidationSelectEdit();
                case TEXT_SELECTED_INSERT :
                    return preValidationSelectInsert();
                case TEXT_EDIT:
                    return  preValidationFullEdit();
                case TEXT_INSERTION:
                   // log.debug("formApply: calling apllyFullInsert()");
                    return preValidationFullInsert();
                default:
                    return true;
        }  
   }
    /*
     *All these functions are overriden in the dialog classes
     */ 
   
    public boolean preValidationSelectEdit(){
        return true;
    }
    
    public boolean preValidationSelectInsert(){
       return true;
    }
    
    public boolean preValidationFullEdit(){
      return true;
    }
    
    public boolean preValidationFullInsert(){
        return true;
    }
            
   
    public boolean preFullInsert(){
        return true;
    }
    
    public boolean postFullInsert(){
        return true;
    }
    
    public boolean processFullInsert(){
        return true;
    }
  
    protected boolean processCatalogCommand() {
        boolean bReturn = false;
        try {
             if (theSubAction == null ) {
                 //get the current catalog command object for the mode.
                BungeniCatalogCommand cmd = theCatalogCommands.get(getDialogMode());
                log.info("processCatalogCommand, cmd is : "+ ((cmd == null)?"null":"not null"));
                ///load the required catalog for the mode, getCatalogSource refers to the full path fo the command catalog
                log.debug("processCatalogCommand : loading catalog");
                BungeniCommandsCatalogLoader loader = new BungeniCommandsCatalogLoader(cmd);
                Catalog selectedCatalog;
                //selectedCatalog = loader.getCatalog(cmd.getCommandCatalog());

                selectedCatalog = loader.getCatalog();
                log.debug("processCatalogCommand : getting commandChain from catalog =  " + cmd.getCommandChain());
                Command selectedCatalogCommand  = selectedCatalog.getCommand(cmd.getCommandChain());
                log.debug("processCatalogCommand : executing command ");
                selectedCatalogCommand.execute(formContext);
            } else {
                //theSubAciton is not null use the 
                 if (theSubAction.action_command_chain().length() > 0 )  {
                     BungeniCatalogCommand cmd = theCatalogCommands.get(getDialogMode());
                     BungeniCommandsCatalogLoader loader = new BungeniCommandsCatalogLoader(cmd /*.getCatalogSource()*/);
                     Catalog selectedCatalog;
                     selectedCatalog = loader.getCatalog(/*cmd.getCommandCatalog()*/);
                     //now load the command chain from the sub_action rather than from the catalogcommand object
                     String commandChain = theSubAction.action_command_chain();
                     Command selectedCatalogCommand  = selectedCatalog.getCommand(commandChain);
                     selectedCatalogCommand.execute(formContext);
                 }
             }
        bReturn = true;
        } catch (Exception ex) {
            log.error("exception in  processCatalogCommand: "+ ex.getMessage());
            log.error("exception in  processCatalogCommand: " + CommonExceptionUtils.getStackTrace(ex));
            bReturn = false;
        } finally {
            return bReturn;
        }
    }

    protected void applySelectEdit() {
        log.debug("applySelectEdit: not implemented yet");
     }
     
   
         public String getNewSectionName() {
        String newSectionName ="";
        if (theAction.action_type().equals("section")) {
            if (theAction.action_numbering_convention().equals("single")) {
                return theAction.action_naming_convention();
            } else {
                String sectionPrefix = theAction.action_naming_convention();
                log.debug("getNewSectionName: sectionPrefix = "+ sectionPrefix);
                for (int i=1; ; i++) {
                    newSectionName = sectionPrefix+i;
                    if (ooDocument == null ) {
                        System.out.println("ooDocument is null in new section name");
                    }
                    if (ooDocument.hasSection(newSectionName))
                        continue;
                    else
                        break;
                }
            }
            return newSectionName;
        } else {
            log.debug("getNewSectionName: the action type is not a section.");
            return null;
        }
    }
    
    public String getActionSectionName() {
        //get the action naming convention
        String numberingConvention = theAction.action_numbering_convention();
        if (numberingConvention.equals("none") || numberingConvention.equals("single")) {
            return theAction.action_naming_convention();
        } else if (numberingConvention.equals("serial")) {
            //get highest section name possible
            int iStart = 1;
            for (; ooDocument.hasSection(theAction.action_naming_convention()+iStart); iStart++ );
            return theAction.action_naming_convention()+iStart; 
        } else
            return theAction.action_naming_convention();
    }
    public String getParentSection(){
      String parentSection = "";
      dbSettings.Connect();
      QueryResults qr = dbSettings.QueryResults(SettingsQueryFactory.Q_CHECK_IF_ACTION_HAS_PARENT(theAction.action_naming_convention()));
      dbSettings.EndConnect();
      String[] results = qr.getSingleColumnResult("THE_COUNT");
      if (results[0].equals("0")) {
          //get the main root as the partn
          parentSection = CommonPropertyFunctions.getDocumentRootSection();
        } else {
          //this needs to be patched to deal with non root parents..
          parentSection = CommonPropertyFunctions.getDocumentRootSection();
        }
     return parentSection;
    }
    
    protected void formApply() {
      switch (getDialogMode()) {
        case TEXT_SELECTED_EDIT :
            applySelectEdit();
            break;
        case TEXT_SELECTED_INSERT :
            applySelectInsert();
            break;
        case TEXT_EDIT:
            applyFullEdit();
            break;
        case TEXT_INSERTION:
            log.debug("formApply: calling apllyFullInsert()");
            applyFullInsert();
            break;
        default:
            break;
    }
    }
    
    /*
     *Event that traps full insert events....
     *You dont have to override this, as this takes care of routing
     *..unless of course you want to override the routing
     *over-ride the pre/process/post insert functions in the dialog classes
     */
     protected void applyFullEdit() {
       if (checkFields() == false) {
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }
        }
       if (preFullEdit() == false) {
            //activities needed to be done before a full insert
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   
       if (processFullEdit() == false) {
            //activities needed to be done during a full insert 
           if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }   
       }   
       if (postFullEdit() == false) {
            //activities to be done after a full insert  
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   else {
           parent.dispose();
       }
    }
     
     
    /*
     *All these functions are overriden in the dialog classes
     */ 
    public boolean preFullEdit(){
        return true;
    }
    
    public boolean postFullEdit(){
        return true;
    }
    
    public boolean processFullEdit(){
        return true;
    }

/*
 *Event loop for select / markup mode.... 
 *
 */
  protected void applySelectInsert() {
       log.debug("in applyFullInsert()");
       if (checkFields() == false) {
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }
        }
       if (preSelectInsert() == false) {
            //activities needed to be done before a full insert
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   
    
       if (processSelectInsert() == false) {
            //activities needed to be done before a full insert
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   
 
       if (postSelectInsert() == false) {
            //activities needed to be done before a full insert
            if (!checkFieldsMessages.isEmpty()) {
                displayFieldErrors();   
                return;
            }  
       }   
        
     
     }    

  /****
   *
   *All the below functions are overriden
   *
   *****/
    public boolean preSelectInsert() {
        return true;
    }

    public boolean processSelectInsert() {
        return true;
    }

    public boolean postSelectInsert() {
        return true;
    }

    public void setToolbarSubAction(toolbarSubAction subAction) {
        theSubAction = subAction;
    }

    public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
        initVariables(ooDoc, dlg, act, subAct);
    }

    public JPanel getPanel() {
        return this;
    }
    
    
    
}


