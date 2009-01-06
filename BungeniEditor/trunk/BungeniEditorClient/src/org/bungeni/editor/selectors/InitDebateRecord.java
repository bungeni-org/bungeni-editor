/*
 * InitDebateRecord.java
 *
 * Created on August 27, 2007, 11:39 AM
 */

package org.bungeni.editor.selectors;

import com.sun.star.text.XTextRange;
import java.awt.Component;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.BungeniEditorProperties;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.fragments.FragmentsFactory;
import org.bungeni.editor.macro.ExternalMacro;
import org.bungeni.editor.macro.ExternalMacroFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocFieldSet;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooDocMetadataFieldSet;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author  Administrator
 * This class creates the initial MastHead section
 * - creates text section with specific name
 * - allows setting various variables within that section
 * - slaps the text into the created text section
 */
public class InitDebateRecord extends selectorTemplatePanel implements IBungeniForm {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InitDebateRecord.class.getName());
   private String m_strLogoPath; 
   private String m_strLogoFileName;
   
    /** Creates new form InitDebateRecord */
    public InitDebateRecord() {
        //initComponents();
        super();
    }
    
    public InitDebateRecord(OOComponentHelper ooDocument, 
            JDialog parentDlg, 
            toolbarAction theAction) {
        super(ooDocument, parentDlg, theAction);
        init();       // initdebate_timeofhansard.setFormatterFactory(new DefaultFormatterFactory(dfTimeOfHansard));
        setControlModes();
        setControlData();
        log.debug("calling constructor : initDebateRecord, mode = " + getDialogMode());
    }
    
        //invoked only for selection mode
      public InitDebateRecord(OOComponentHelper ooDocument, 
            JDialog parentDlg, 
            toolbarAction theAction, toolbarSubAction subAction) {
         super(ooDocument, parentDlg, theAction, subAction);
         init();
         setControlModes();
         setControlData();
         //selectionControlModes();
         log.debug("calling constructor : initDebateRecord, mode = " + getDialogMode());
      }
      public JPanel getPanel(){
          return this;
      }
 
      String[] controls_ignore_list = {"btn_apply", "btn_cancel" };
     
      
    public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
        super.initObject( ooDoc, dlg, act, subAct);
            init();
         setControlModes();
         setControlData();
    }
    
    //override parent, call the parent api using super.
      /*
      protected void getEnabledControlList_TextInsertion() {
          super.getEnabledControlList_TextInsertion();  
         //add elements from ignore list
         for (int i=0; i < this.controls_ignore_list.length ; i++ ) {
             getEnabledControls().add(controls_ignore_list[i]);
         }
      }
      */
  
      protected void getEnabledControlList_TextSelection(){
          super.getEnabledControlList_TextSelection();
          for (int i=0; i <controls_ignore_list.length; i++ ) {
              getEnabledControls().add(controls_ignore_list[i]);
          }
      }

      
      public void init(){
        super.init();
        initComponents();
        //default m_strLogoPath
        String logoPath = BungeniEditorProperties.getEditorProperty("logoPath");
        log.debug("logo path = " + logoPath);
        String strPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH();
        m_strLogoPath = strPath + File.separator + logoPath + File.separator + "default_logo.jpg";
        log.debug("InitDebateRecord:" + m_strLogoPath);
        dt_initdebate_timeofhansard.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
        dt_initdebate_timeofhansard.setEditor(new JSpinner.DateEditor(dt_initdebate_timeofhansard, "HH:mm"));
        ((JSpinner.DefaultEditor)dt_initdebate_timeofhansard.getEditor()).getTextField().setEditable(false);
        this.dt_initdebate_hansarddate.setInputVerifier(new DateVerifier());
        buildComponentsArray();

      }
      
      protected HashMap<SelectorDialogModes, String> COMMAND_CHAIN = new HashMap<SelectorDialogModes, String>();
      
      protected void createContext(){
          super.createContext();
          formContext.setBungeniForm(this);
      }
     
    public String getClassName(){
        return InitDebateRecord.class.getName();
    }
            
    protected void setControlData() {
        try {
        //only in edit mode, only if the metadata properties exist
        if (theMode == SelectorDialogModes.TEXT_EDIT) {
            if (ooDocument.propertyExists("Bungeni_DebateOfficialDate") &&
                    ooDocument.propertyExists("Bungeni_DebateOfficialTime")) {
                ooDocMetadata meta = new ooDocMetadata(ooDocument);
                String strDate = meta.GetProperty("Bungeni_DebateOfficialDate");
                String strTime = meta.GetProperty("Bungeni_DebateOfficialTime");
                SimpleDateFormat formatter = new SimpleDateFormat ("MMMM dd yyyy");
                this.dt_initdebate_hansarddate.setDate(formatter.parse(strDate));
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                this.dt_initdebate_timeofhansard.setValue(timeFormat.parse(strTime));
                }
            }
        } catch (ParseException ex) {
            log.error("SetControlData: "+ ex.getMessage());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        dt_initdebate_hansarddate = new org.jdesktop.swingx.JXDatePicker();
        lbl_initdebate_hansarddate = new javax.swing.JLabel();
        lbl_initdebate_selectlogo = new javax.swing.JLabel();
        btn_initdebate_selectlogo = new javax.swing.JButton();
        lbl_initdebate_timeofhansard = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        dt_initdebate_timeofhansard = new javax.swing.JSpinner();
        txt_initdebate_selectlogo = new javax.swing.JTextField();

        dt_initdebate_hansarddate.setName("dt_initdebate_hansarddate");

        lbl_initdebate_hansarddate.setText("Hansard Date");
        lbl_initdebate_hansarddate.setName("lbl_initdebate_hansarddate");

        lbl_initdebate_selectlogo.setText("Masthead Logo");
        lbl_initdebate_selectlogo.setName("lbl_initdebate_selectlogo");

        btn_initdebate_selectlogo.setText("Select Logo...");
        btn_initdebate_selectlogo.setName("btn_initdebate_selectlogo");
        btn_initdebate_selectlogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_initdebate_selectlogoActionPerformed(evt);
            }
        });

        lbl_initdebate_timeofhansard.setText("Hansard Time");
        lbl_initdebate_timeofhansard.setName("lbl_initdebate_timeofhansard");

        btnApply.setText("Apply ");
        btnApply.setName("btn_apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.setName("btn_cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        dt_initdebate_timeofhansard.setName("dt_initdebate_timeofhansard");

        txt_initdebate_selectlogo.setEditable(false);
        txt_initdebate_selectlogo.setName("txt_initdebate_selectlogo");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(btnApply, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(txt_initdebate_selectlogo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(lbl_initdebate_hansarddate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, btn_initdebate_selectlogo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lbl_initdebate_selectlogo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, lbl_initdebate_timeofhansard, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                        .add(126, 126, 126))
                    .add(layout.createSequentialGroup()
                        .add(dt_initdebate_timeofhansard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(dt_initdebate_hansarddate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .add(lbl_initdebate_hansarddate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dt_initdebate_hansarddate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_initdebate_timeofhansard)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dt_initdebate_timeofhansard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_initdebate_selectlogo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btn_initdebate_selectlogo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txt_initdebate_selectlogo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

   
    private void enableButtons(boolean state) {
        btnApply.setEnabled(state);
        btnCancel.setEnabled(state);
    }
    
    private boolean actionTypeCheck (toolbarAction action ) {
    if (action.action_type().equals("section") ) {
        if (action.action_numbering_convention().equals("single")) 
           if (ooDocument.hasSection(action.action_naming_convention())) {
                MessageBox.OK(parent, "This document already has a mast head, you cannot add a second one!");
                enableButtons(true);
                return false;
                }
        }
    return true;
    }
   
  
    
    /*
     *This is a funciton overriden from the base class
     *validateFieldValue is invoked by check fields
     *user must write validation functions for fields and return true / false 
     *after setting an error message
     *
     */
    protected boolean validateFieldValue(Component field, Object fieldValue ) {
        String formFieldName = field.getName();
        log.debug("validating field = "+ field.getName());
        boolean bFailure=false;
        //routes to appropriate field validator...
        if (formFieldName.equals("dt_initdebate_hansarddate")) {
            bFailure = validateHansardDate("dt_initdebate_hansarddate", fieldValue);
        } else if (formFieldName.equals("dt_initdebate_timeofhansard")) {
            bFailure = validateHansardTime("dt_initdebate_timeofhansard", fieldValue);
        } else if (formFieldName.equals("txt_initdebate_selectlogo")) {
            bFailure = validateLogo("txt_initdebate_selectlogo", fieldValue);
        }  else {
            log.debug("no validator found for field: "+ field.getName()+ " , returning true");
            return true;
        }
        return bFailure;
    }
    
   //needs to be in current class
    private boolean validateHansardDate(String controlName, Object fieldValue) {
        boolean bState = false;
        try {
            Date hansardDate = (Date) fieldValue;
            SimpleDateFormat formatter = new SimpleDateFormat ("MMMM dd yyyy");
            String strDebateDate = formatter.format(hansardDate);
            theControlDataMap.put(controlName, strDebateDate);
            bState = true;
        } catch (Exception ex) {
            log.error("validateHansardDate: error : " + ex.getMessage());
            checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
        } finally {
        return bState;
        }
    }
    
    private boolean validateHansardTime(String controlName, Object fieldValue) {
        boolean bState = false;
        try {
            Date hansardTime = (Date) fieldValue;
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String strTimeOfHansard = df.format(hansardTime);
            theControlDataMap.put(controlName, strTimeOfHansard);
            bState = true;
        } catch (Exception ex) {
           log.error("validateHansardTime: error : " + ex.getMessage());
            checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
        } finally {
             return bState;
        }
    }
    
    private boolean validateLogo(String controlName, Object fieldValue ) {
        boolean bState= false;
        try{
           String logoFieldValue = (String) fieldValue;
           if (logoFieldValue.length() == 0 ) {
               checkFieldsMessages.add("You must select a logo !");
               bState = false;
               return bState;
           }
           
          theControlDataMap.put(controlName, m_strLogoPath);
           bState = true;        
        } catch (Exception ex) {
           log.error("validateLogo: error : " + ex.getMessage());
            checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
        } finally {
            return bState;
        }
    }
    
    
    //move to base...
   
    //over ride btnApply....by calling  a base class function to override functionality...
    

   

    /*
     *
     *
     *Insert mode processing
     *
     *
     */
    public boolean preFullInsert(){
            log.debug("in preFullInsert...");
            
            /** gathering values ***/
            
            long sectionBackColor = 0xffffff;
            float sectionLeftMargin = (float).2;
           
            String parentSection = getParentSection();
            log.debug("preFullInsert() : getParentSection()" + parentSection );
            String newSectionName = getNewSectionName();
            log.debug("preFullInsert() : getNewSectionName()" + newSectionName );
            
            /** seeding pre insert map that will be used by the commands chain **/
            formContext.addFieldSet("section_back_color");
            formContext.addFieldSet("section_left_margin");
            formContext.addFieldSet("container_section");
            formContext.addFieldSet("current_section");
            formContext.addFieldSet("new_section");
            formContext.addFieldSet("selected_logo");
            formContext.addFieldSet("document_fragment");
            formContext.addFieldSet("image_import_section");
            formContext.addFieldSet("document_import_section");
            
            formContext.getFieldSets("section_back_color").add(Long.toHexString(sectionBackColor));
            //thePreInsertMap.put("section_back_color", Long.toHexString(sectionBackColor));
            formContext.getFieldSets("section_left_margin").add(Float.toString(sectionLeftMargin));
            //thePreInsertMap.put("section_left_margin", Float.toString(sectionLeftMargin));
            formContext.getFieldSets("container_section").add(parentSection);
            //thePreInsertMap.put("container_section", parentSection);
            formContext.getFieldSets("current_section").add(newSectionName);
            formContext.getFieldSets("document_import_section").add(newSectionName);
            //thePreInsertMap.put("current_section", newSectionName);
            formContext.getFieldSets("new_section").add(newSectionName);
            //thePreInsertMap.put("new_section", newSectionName);
            formContext.getFieldSets("selected_logo").add((String) theControlDataMap.get("txt_initdebate_selectlogo"));
            //thePreInsertMap.put("selected_logo", (String) theControlDataMap.get("txt_initdebate_selectlogo"));
            formContext.getFieldSets("document_fragment").add((String) FragmentsFactory.getFragment("hansard_masthead"));
            //thePreInsertMap.put("document_fragment", (String) FragmentsFactory.getFragment("hansard_masthead"));
            formContext.getFieldSets("image_import_section").add(newSectionName);
            
            formContext.addFieldSet("document_field_set");
            formContext.getFieldSets("document_field_set").add(
                    new ooDocFieldSet(new String("debaterecord_official_date"),
                                            (String) theControlDataMap.get("dt_initdebate_hansarddate"),
                                             new String("int:masthead_datetime"))
                    );
            formContext.getFieldSets("document_field_set").add(
                     new ooDocFieldSet(new String("debaterecord_official_time"),
                                            (String) theControlDataMap.get("dt_initdebate_timeofhansard"),
                                             new String("int:masthead_datetime"))                    
                   );

            formContext.getMetadataFieldSets().add(new ooDocMetadataFieldSet("BungeniDebateOfficialDate", (String) theControlDataMap.get("dt_initdebate_hansarddate")));
            formContext.getMetadataFieldSets().add(new ooDocMetadataFieldSet("BungeniDebateOfficialTime", (String) theControlDataMap.get("dt_initdebate_timeofhansard")));
            
            return true;
    }
    /*
    protected boolean preFullInsert_old() {
        //add section inside the root section
            log.debug("in preFullInsert...");
            long sectionBackColor = 0xffffff;
            float sectionLeftMargin = (float).2;
           
            String parentSection = getParentSection();
            log.debug("preFullInsert() : getParentSection()" + parentSection );
            String newSectionName = getNewSectionName();
            log.debug("preFullInsert() : getNewSectionName()" + newSectionName );
            
            thePreInsertMap.clear();
            thePreInsertMap.put("container_section", parentSection);
            thePreInsertMap.put("current_section", newSectionName);
            thePreInsertMap.put("new_section", newSectionName);
           
           // m_containerSection = parentSection;
           // m_newSectionName = newSectionName;
            boolean bState = this.action_addSectionIntoSectionwithStyling(ooDocument,
                                                                        parentSection,
                                                                        newSectionName,
                                                                        sectionBackColor, 
                                                                        sectionLeftMargin);
            
            if (bState == false ) {
                return false;
            } 
            setSectionMetadataForAction(newSectionName, theAction);
    
            return true;
       }
    */
    public boolean processFullInsert() {
        /**call catalogcommand of base class**/
        
        boolean bReturn = processCatalogCommand();
        return bReturn;
    }
    /*
    protected boolean processFullInsert_old(){
            //set metadata for section
        //embed logo image
        
            boolean bAddImage = action_addImageIntoSection(ooDocument, 
                    (String) thePreInsertMap.get("current_section"), 
                    (String) theControlDataMap.get("txt_initdebate_selectlogo") );
            if (bAddImage == false) {
                //displayFieldErrors();
                checkFieldsMessages.add("The image could not be inserted properly");
                return false;
            } 
            
            boolean bAddDocintoSection = action_addDocIntoSection(ooDocument, 
                    (String) thePreInsertMap.get("current_section"),
                    FragmentsFactory.getFragment("hansard_masthead"));
            if (bAddDocintoSection == false) {
                checkFieldsMessages.add("The section could not be initialized correctly");
                //displayFieldErrors();
                return false;
            } 
        
            boolean bSetFieldValue =  action_setInputFieldValue(ooDocument,
                                    new String("debaterecord_official_date"),
                                    (String) theControlDataMap.get("dt_initdebate_hansarddate"), 
                                    new String("int:masthead_datetime"));
            
            if (bSetFieldValue == false) {
                checkFieldsMessages.add("There was an error while setting the debate date");
                //displayFieldErrors();
                return false;
            } 
            
            bSetFieldValue =  action_setInputFieldValue(ooDocument,
                                    new String("debaterecord_official_time"),
                                    (String) theControlDataMap.get("dt_initdebate_timeofhansard"), 
                                    new String("int:masthead_datetime"));
            
            if (bSetFieldValue == false) {
                //displayFieldErrors();
                checkFieldsMessages.add("There was an error while setting the official time of the debate");
                return false;
            } 
 
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialDate", (String) theControlDataMap.get("dt_initdebate_hansarddate"));
            meta.AddProperty("Bungeni_DebateOfficialTime", (String) theControlDataMap.get("dt_initdebate_timeofhansard"));
            
            return true;
    }
    */
   
    public boolean postFullInsert() {
        //enable disable specific controls if required
        parent.dispose();
        return true;
    }
 
    
    
    
    /*
    *
    *
    *Edit mode processing
    *
    *
    */
   public boolean preFullEdit(){
     boolean bpreFullEdit= false;
     try {
     String containerSection = theAction.getSelectedSectionToActUpon();
     String datetimeContainerSection = "int:masthead_datetime";
     if (ooDocument.hasSection(containerSection) && ooDocument.hasSection(datetimeContainerSection)) {
           formContext.addFieldSet("document_field_set");
           formContext.getFieldSets("document_field_set").add(new ooDocFieldSet(new String("debaterecord_official_date"),
                                            (String) theControlDataMap.get("dt_initdebate_hansarddate"),
                                            datetimeContainerSection));
           formContext.getFieldSets("document_field_set").add(new ooDocFieldSet(new String("debaterecord_official_time"),
                                            (String) theControlDataMap.get("dt_initdebate_timeofhansard"),
                                            datetimeContainerSection));
           formContext.addFieldSet("container_section");
           formContext.getFieldSets("container_section").add(containerSection);
           //thePreInsertMap.put("container_section", containerSection);

           formContext.getMetadataFieldSets().add(new ooDocMetadataFieldSet("Bungeni_DebateOfficialDate",
                   (String) theControlDataMap.get("dt_initdebate_hansarddate")));

           formContext.getMetadataFieldSets().add(new ooDocMetadataFieldSet("Bungeni_DebateOfficialTime", 
                   (String) theControlDataMap.get("dt_initdebate_timeofhansard")));

           bpreFullEdit = true;
     
     } else {
            checkFieldsMessages.add("There is no masthead section available for editing in this document!");
            bpreFullEdit = false;
        } 
     }  catch (Exception ex) {
         log.error("preFullEdit: error :" + ex.getMessage());
         checkFieldsMessages.add("There was an error initializing section editing!");
         bpreFullEdit = false;
         
     } finally {
         return bpreFullEdit;
     }
     
   }
    
  public boolean processFullEdit(){
    processCatalogCommand();
    return true;
  }
  /*
    protected boolean processFullEdit_Old() {
        boolean bprocessFull = false;
        try {
            boolean bSetDate = this.action_setInputFieldValue(ooDocument, 
                                                               "debaterecord_official_date",
                                                                (String) theControlDataMap.get("dt_initdebate_hansarddate"),
                                                                (String) thePreInsertMap.get("datetime_container_section"));
            if (!bSetDate) {
                checkFieldsMessages.add("Date could not be marked up in document");
                bprocessFull = false;
                return bprocessFull;
            }                                                    
          bSetDate = this.action_setInputFieldValue(ooDocument, 
                                                               "debaterecord_official_time",
                                                                (String) theControlDataMap.get("dt_initdebate_timeofhansard"),
                                                                (String) thePreInsertMap.get("datetime_container_section"));
           if (!bSetDate) {
                checkFieldsMessages.add("Time could not be marked up in document");
                bprocessFull = false;
                return bprocessFull;
           }
          
          bprocessFull = true;
        } catch (Exception ex) {
            log.error("proccessFullEdit: error: "+ ex.getMessage());
            bprocessFull = false;
        } finally {
            return bprocessFull;
        }
    }
    */
  
    public boolean postFullEdit(){
      boolean bFullEdit = false;      
      try {
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialDate", (String) theControlDataMap.get("dt_initdebate_hansarddate"));
            meta.AddProperty("Bungeni_DebateOfficialTime", (String) theControlDataMap.get("dt_initdebate_timeofhansard"));
            bFullEdit = true;
        
      } catch (Exception ex) {
            log.error("postFullEdit: error :" + ex.getMessage());
            checkFieldsMessages.add("There was an error setting document level metadata");
            bFullEdit = false;
            return bFullEdit;
      } finally {
          return bFullEdit;
      }
    }
    
    /*
     *
     *Selection mode implmenetation
     *
     *
     */
    private boolean m_wasSystemSectionProtected = false;
    public boolean preSelectInsert() {
        //unprotect section
        String systemContainer = theSubAction.system_container();
        if (ooDocument.hasSection(systemContainer)) {
            if (ooDocument.isSectionProtected(systemContainer)) {
                ooDocument.protectSection(systemContainer, false);
                m_wasSystemSectionProtected = true;
            }
        }
        return true;
    }

    public boolean processSelectInsert() {
        
        if (enabledControls.contains(new String("dt_initdebate_hansarddate"))) {
            //get the value and set it into the document
            String dateOfHansard = (String) theControlDataMap.get("dt_initdebate_hansarddate");
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialDate", dateOfHansard);
            XTextRange selRange = (XTextRange) ooDocument.getSingleSelectionRange().get("XTextRange");
            String strSelection = selRange.getString();            
            boolean bDateAdd = action_replaceTextWithField(ooDocument, "debaterecord_official_date", strSelection);
            if (!bDateAdd) {
                checkFieldsMessages.add("There was an error while marking the handard date");
            }
            return bDateAdd;
        }
        
        if (enabledControls.contains(new String("dt_initdebate_timeofhansard"))) {
            //get the current time value into the document
            String timeOfHansard = (String) theControlDataMap.get("dt_initdebate_timeofhansard");
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialTime", timeOfHansard);
            XTextRange selRange = (XTextRange) ooDocument.getSingleSelectionRange().get("XTextRange");
            String strSelection = selRange.getString();
            boolean bTimeAdd = action_replaceTextWithField(ooDocument, "debaterecord_official_time", strSelection);
            if (!bTimeAdd) {
                checkFieldsMessages.add("There was an error while marking the handard time");
            }
            return bTimeAdd;
            
        }
        return true;
    }

    public boolean postSelectInsert() {
        return true;
    }
    
    
       private boolean action_replaceTextWithField(OOComponentHelper ooDoc, String hintName, String hintPlaceholderValue) {
        boolean bState = false; 
        try {
            ExternalMacro ReplaceTextWithField = ExternalMacroFactory.getMacroDefinition("ReplaceTextWithField");
            ReplaceTextWithField.addParameter(ooDoc.getComponent());
            ReplaceTextWithField.addParameter(hintName);
            ReplaceTextWithField.addParameter(hintPlaceholderValue);
            ooDoc.executeMacro(ReplaceTextWithField.toString(), ReplaceTextWithField.getParams());
             bState= true;
        } catch (Exception ex) {
            log.error("action_replaceTextWithField: error : " + ex.getMessage());
            //checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
         }   finally {
             return bState;
         }
    }
       private boolean action_addSectionIntoSectionwithStyling(OOComponentHelper ooDoc, String parentSection, String newSectionName, long sectionBackColor, float sectionLeftMargin) {
        boolean bState = false; 
        try {
            ExternalMacro AddSectionInsideSection = ExternalMacroFactory.getMacroDefinition("AddSectionInsideSectionWithStyle");
            AddSectionInsideSection.addParameter(ooDoc.getComponent());
            AddSectionInsideSection.addParameter(parentSection);
            AddSectionInsideSection.addParameter(newSectionName);
            AddSectionInsideSection.addParameter(sectionBackColor);
            AddSectionInsideSection.addParameter(sectionLeftMargin);
            ooDoc.executeMacro(AddSectionInsideSection.toString(), AddSectionInsideSection.getParams());
             bState= true;
        } catch (Exception ex) {
            log.error("action_addSectionIntoSectionwithStyling: error : " + ex.getMessage());
            //checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
         }   finally {
             return bState;
         }
    }
    
    private boolean action_addImageIntoSection(OOComponentHelper ooDoc, String intoSection, String logoPath) {
        boolean bState = false; 
        try {
            log.debug("executing addImageIntoSection : intoSection = "+ intoSection + " , logoPath = "+logoPath);
             ExternalMacro addImageIntoSection = ExternalMacroFactory.getMacroDefinition("AddImageIntoSection");
             addImageIntoSection.addParameter(ooDoc.getComponent());
             addImageIntoSection.addParameter(intoSection);
             addImageIntoSection.addParameter(logoPath);
             ooDoc.executeMacro(addImageIntoSection.toString(), addImageIntoSection.getParams());
             bState= true;
        } catch (Exception ex) {
            log.error("action_addImageIntoSection: error : " + ex.getMessage());
            //checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
         }   finally {
             return bState;
         }
    }
    
    
    private boolean action_addDocIntoSection(OOComponentHelper ooDoc, String intoSection, String fragmentName) {
        boolean bState = false; 
       try {
            ExternalMacro insertDocIntoSection = ExternalMacroFactory.getMacroDefinition("InsertDocumentIntoSection");
            insertDocIntoSection.addParameter(ooDoc.getComponent());
            insertDocIntoSection.addParameter(intoSection)  ;
            insertDocIntoSection.addParameter(fragmentName);
            ooDoc.executeMacro(insertDocIntoSection.toString(), insertDocIntoSection.getParams());
            bState= true;
        } catch (Exception ex) {
            log.error("action_addImageIntoSection: error : " + ex.getMessage());
            //checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
         }   finally {
             return bState;
         }
    }
    
    private boolean action_setInputFieldValue(OOComponentHelper ooDoc, String hintName, String strDebateDate, String unprotectSection) {
        boolean bState = false; 
       try {
           ExternalMacro setFieldValue = ExternalMacroFactory.getMacroDefinition("SetReferenceInputFieldValue");
            setFieldValue.addParameter(ooDoc.getComponent());
            setFieldValue.addParameter(hintName);
            setFieldValue.addParameter(strDebateDate);
            setFieldValue.addParameter(unprotectSection);
            ooDoc.executeMacro( setFieldValue.toString(),  setFieldValue.getParams());
            bState= true;
        } catch (Exception ex) {
            log.error("action_addImageIntoSection: error : " + ex.getMessage());
            //checkFieldsMessages.add(ex.getLocalizedMessage());
            bState=false;
         }   finally {
             return bState;
         }
    }
 
    
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
        //get field values :
        //apply functions work up the hierarchy
        this.FORM_APPLY_NO_ERROR = false;
        super.formApply();
        if (FORM_APPLY_NO_ERROR)
            parent.dispose();

      
    }//GEN-LAST:event_btnApplyActionPerformed
    
    private void processSelectionAction() {
        
    }
    
    private boolean routeSectionAction () {
        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
             return sectionInsertionAction();
        } else if (theMode == SelectorDialogModes.TEXT_EDIT) {
             return sectionEditAction();
        } else 
            return false;
    }
    
    private boolean sectionEditAction() {
        //if section exists
        //update fields
        String containerSection = theAction.action_naming_convention();
        if (ooDocument.hasSection(containerSection) && ooDocument.hasSection("int:masthead_datetime")) {
           //now edit the fields and set the new values
            String strDebateDate = "", strTimeOfHansard = "";   
            Date dtDebate = dt_initdebate_hansarddate.getDate();
         
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            strTimeOfHansard =  df.format((Date)dt_initdebate_timeofhansard.getValue()); //.getText();

            SimpleDateFormat formatter = new SimpleDateFormat ("MMMM dd yyyy");
            strDebateDate = formatter.format(dtDebate);
            
            ExternalMacro setFieldValue = ExternalMacroFactory.getMacroDefinition("SetReferenceInputFieldValue");
            setFieldValue.addParameter(new String("debaterecord_official_date"));
            setFieldValue.addParameter(strDebateDate);
            setFieldValue.addParameter(new String("int:masthead_datetime"));
            ooDocument.executeMacro( setFieldValue.toString(),  setFieldValue.getParams());
            setFieldValue.clearParams();
            setFieldValue.addParameter(new String("debaterecord_official_time"));
            setFieldValue.addParameter(strTimeOfHansard);
            setFieldValue.addParameter(new String("int:masthead_datetime"));
            ooDocument.executeMacro( setFieldValue.toString(),  setFieldValue.getParams());   
            //set date and time of hansard to document
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialDate", strDebateDate);
            meta.AddProperty("Bungeni_DebateOfficialTime", strTimeOfHansard);
            return true;
            
        } else {
            MessageBox.OK(parent, "There is no masthead section available for editing in this document!");
            return false;
        }
    }
    private boolean sectionInsertionAction() {
        String strDebateDate = "", strTimeOfHansard = "", strLogoPath = "";   
        Date dtDebate = dt_initdebate_hansarddate.getDate();
        SimpleDateFormat df= new SimpleDateFormat("HH:mm");
        strTimeOfHansard =  df.format((Date)dt_initdebate_timeofhansard.getValue());
        SimpleDateFormat formatter = new SimpleDateFormat ("MMMM dd yyyy");
        strDebateDate = formatter.format(dtDebate);
        strLogoPath = m_strLogoPath;
        
        long sectionBackColor = 0xffffff;
        float sectionLeftMargin = (float).2;
        log.debug("section left margin : "+ sectionLeftMargin);
        //get the parent section name of this action
        //query action parents to find the parent of this action
       
        //String parentSectionName = 
        
        ExternalMacro AddSectionInsideSection = ExternalMacroFactory.getMacroDefinition("AddSectionInsideSectionWithStyle");
        AddSectionInsideSection.addParameter(ooDocument.getComponent());
        AddSectionInsideSection.addParameter(BungeniEditorPropertiesHelper.getDocumentRoot());
        AddSectionInsideSection.addParameter(theAction.action_naming_convention());
        AddSectionInsideSection.addParameter(sectionBackColor);
        AddSectionInsideSection.addParameter(sectionLeftMargin);
        
        ooDocument.executeMacro(AddSectionInsideSection.toString(), AddSectionInsideSection.getParams());
        
        this.setSectionMetadataForAction(theAction.action_naming_convention(), theAction);
        //embed logo image
             ExternalMacro addImageIntoSection = ExternalMacroFactory.getMacroDefinition("AddImageIntoSection");
             addImageIntoSection.addParameter(theAction.action_naming_convention());
             addImageIntoSection.addParameter(m_strLogoPath);
             ooDocument.executeMacro(addImageIntoSection.toString(), addImageIntoSection.getParams());
            
        //loading the related document
            ExternalMacro insertDocIntoSection = ExternalMacroFactory.getMacroDefinition("InsertDocumentIntoSection");
            insertDocIntoSection.addParameter(ooDocument.getComponent());
            insertDocIntoSection.addParameter(theAction.action_naming_convention())  ;
            insertDocIntoSection.addParameter(FragmentsFactory.getFragment("hansard_masthead"));
            ooDocument.executeMacro(insertDocIntoSection.toString(), insertDocIntoSection.getParams());
            
            ExternalMacro setFieldValue = ExternalMacroFactory.getMacroDefinition("SetReferenceInputFieldValue");
            setFieldValue.addParameter(new String("debaterecord_official_date"));
            setFieldValue.addParameter(strDebateDate);
            setFieldValue.addParameter(new String("int:masthead_datetime"));
            ooDocument.executeMacro( setFieldValue.toString(),  setFieldValue.getParams());
            
            
            setFieldValue.clearParams();
            setFieldValue.addParameter(new String("debaterecord_official_time"));
            setFieldValue.addParameter(strTimeOfHansard);
            setFieldValue.addParameter(new String("int:masthead_datetime"));
            ooDocument.executeMacro( setFieldValue.toString(),  setFieldValue.getParams());   
        
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty("Bungeni_DebateOfficialDate", strDebateDate);
            meta.AddProperty("Bungeni_DebateOfficialTime", strTimeOfHansard);
            
            enableButtons(true);
            //MessageBox.OK(parent, "Prayers section was successfully added");
            return true;
    }
    
    private void markupAction() {
    }

    private void btn_initdebate_selectlogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_initdebate_selectlogoActionPerformed
// TODO add your handling code here:
        String logoPath = "";
        logoPath = BungeniEditorProperties.getEditorProperty("logoPath");
        log.debug("logo path = " + logoPath);
        String strPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH();
        logoPath = strPath + File.separator + logoPath.replace('/', File.separatorChar);
        log.debug("logo path new = "+ logoPath);
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File fLogoPath = new File(logoPath);
        chooser.setCurrentDirectory(fLogoPath);
        int nReturnVal = chooser.showOpenDialog(this);
        if (nReturnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            m_strLogoFileName = file.getName();
            m_strLogoPath = file.getAbsolutePath();
            txt_initdebate_selectlogo.setText(m_strLogoFileName);
            //This is where a real application would open the file.
            log.debug("Opening: " + file.getName() + "." + "\n");
        } else {
            log.debug("Open command cancelled by user." + "\n");
        }
    }//GEN-LAST:event_btn_initdebate_selectlogoActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btn_initdebate_selectlogo;
    private org.jdesktop.swingx.JXDatePicker dt_initdebate_hansarddate;
    private javax.swing.JSpinner dt_initdebate_timeofhansard;
    private javax.swing.JLabel lbl_initdebate_hansarddate;
    private javax.swing.JLabel lbl_initdebate_selectlogo;
    private javax.swing.JLabel lbl_initdebate_timeofhansard;
    private javax.swing.JTextField txt_initdebate_selectlogo;
    // End of variables declaration//GEN-END:variables
  
   public class DateVerifier extends InputVerifier {
     public boolean verify(JComponent input) {
         if (input instanceof JFormattedTextField) {
             JFormattedTextField ftf = (JFormattedTextField)input;
             JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
             if (formatter != null) {
                 String text = ftf.getText();
                 try {
                      formatter.stringToValue(text);
                      return true;
                  } catch (ParseException pe) {
                      return false;
                  }
              }
          }
          return true;
      }
      public boolean shouldYieldFocus(JComponent input) {
          return verify(input);
      }
  }
}
