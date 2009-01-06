/*
 * InitQuestionBlock.java
 *
 * Created on August 31, 2007, 4:01 PM
 */

package org.bungeni.editor.selectors;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JDialog;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.GeneralQueryFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.editor.fragments.FragmentsFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.MessageBox;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 *
 * @author  Administrator
 */
public class InitQuestionBlock extends selectorTemplatePanel implements IBungeniForm  {
  
    registryQueryDialog rqs;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InitQuestionBlock.class.getName());
 
    HashMap<String, String> selectionData = new HashMap<String,String>();
    String txtURI = "";
    
    String[] fldsSerialized = {"txtQuestionTitle", "txtQuestionName", "txtPersonURI", "txtAddressedTo", "txtQuestionText" };
    
    private String sourceSectionName;
    /** Creates new form InitQuestionBlock */
    public InitQuestionBlock() {
      //  initComponents();
        super();
    }
    public InitQuestionBlock(OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction) {
        super(ooDocument, parentDlg, theAction);
        initComponents();
        //above is required code....
        init();

    }
   
  public void initObject(OOComponentHelper ooDoc, JDialog dlg, toolbarAction act, toolbarSubAction subAct) {
    super.initObject( ooDoc, dlg, act, subAct);
    init();
    //setControlModes();
}

  
  public void init(){
      super.init();
      initComponents();
      buildComponentsArray();
      setControlModes();
      setControlData();
  }

  public void createContext(){
      super.createContext();
      formContext.setBungeniForm(this);
  }

public String getClassName(){
    return this.getClass().getName();
}

  
    private void xinit() {
        txtQuestionText.setContentType("text/html");
        setControlModes();
        setControlData();
        
    }
    
    private void initSerializationMap() {
        for (int i=0; i < fldsSerialized.length; i++) {
            theSerializationMap.put(fldsSerialized[i], "");
        }
    }
    
   /*
    
    
    public void setControlModes() {

        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
            txtAddressedTo.setEditable(false);
            txtPersonName.setEditable(true);
            txtQuestionText.setEditable(false);
            txtQuestionTitle.setEditable(false);
            txtPersonURI.setVisible(false); lblPersonURI.setVisible(false);
            txtQuestionText.setVisible(true);
            lblQuestionText.setVisible(true);
 
            txtMessageArea.setText("You are attempting to insert a new Question, " +
                    "please select a question, and edit the name if neccessary, the " +
                    "text of the question and the metadata will be inserted into the " +
                    "document");
        } else if (theMode == SelectorDialogModes.TEXT_EDIT) {
            log.debug("InitQuestionBlock: In selectorDialogMode TEXT_EIDT ");
            txtAddressedTo.setEditable(false);
            txtPersonName.setEditable(true);
            txtQuestionText.setEditable(false);
            txtQuestionTitle.setEditable(false);
            txtPersonURI.setEditable(false);
            txtPersonURI.setVisible(true); 
            lblPersonURI.setVisible(true);
            txtQuestionText.setVisible(false);
            lblQuestionText.setVisible(false);
            scrollQuestionText.setVisible(false);
            this.btnSelectQuestion.setVisible(false);
            txtMessageArea.setText("You are attempting to Edit metadata for a question");
            
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
            txtAddressedTo.setEditable(false);
            lblNameOfPersonFrom.setVisible(false);
            txtPersonName.setVisible(false); //setEditable(false);
            txtQuestionText.setEditable(false);
            txtQuestionTitle.setEditable(false);    
            txtPersonURI.setVisible(false); lblPersonURI.setVisible(false);
            txtQuestionText.setVisible(true);
            lblQuestionText.setVisible(true);
            
            txtMessageArea.setText("You are in Select mode. Your hightlighted block of text will be marked up as a Question using this interface ");
                   
        }
    }
    */
    public void setControlData(){
           try {
        //only in edit mode, only if the metadata properties exist
        if (theMode == SelectorDialogModes.TEXT_EDIT) {
              String currentSectionName = "";
           currentSectionName = this.theAction.getSelectedSectionToActUpon();
            //currentSectionName = ooDocument.currentSectionName();
            ///do stuff for speech sections retrieve from section metadata////
            ///we probably need a associative metadata attribute factory that
            ///retrieves valid metadata elements for specific seciton types.
            ///how do you identify section types ? probably by naming convention....
            //if (currentSectionName.startsWith(theAction.action_naming_convention())) {
                //this section stores MP specific metadata
                //get attribute names for mp specific metadata
                //Bungeni_SpeechMemberName
                //Bungeni_SpeechMemberURI
                //the basic macro for adding attributes takes two arrays as a parameter
                //one fr attribute names , one for attribute values
                HashMap<String,String> attribMap = ooDocument.getSectionMetadataAttributes(currentSectionName);
                this.sourceSectionName = currentSectionName;
                if (attribMap.size() > 0 ) {
                  
                    this.txtAddressedTo.setText(attribMap.get("Bungeni_QuestionAddressedTo"));
                    this.txtQuestionTitle.setText(attribMap.get("Bungeni_QuestionTitle"));
                    this.txtPersonName.setText(attribMap.get("Bungeni_QuestionMemberFrom"));
                    this.txtPersonURI.setText(attribMap.get("Bungeni_QuestionMemberFromURI"));
                } else {
                    MessageBox.OK(parent, "No metadata has been set for this section!");
                    parent.dispose();
                }
                return;
        }
        } catch (Exception ex) {
            log.error("SetControlData: "+ ex.getMessage());
        }
    }
    
    private boolean goEditMode() {
          String currentSectionName = "";
           currentSectionName = this.theAction.getSelectedSectionToActUpon();
            //currentSectionName = ooDocument.currentSectionName();
            ///do stuff for speech sections retrieve from section metadata////
            ///we probably need a associative metadata attribute factory that
            ///retrieves valid metadata elements for specific seciton types.
            ///how do you identify section types ? probably by naming convention....
            //if (currentSectionName.startsWith(theAction.action_naming_convention())) {
                //this section stores MP specific metadata
                //get attribute names for mp specific metadata
                //Bungeni_SpeechMemberName
                //Bungeni_SpeechMemberURI
                //the basic macro for adding attributes takes two arrays as a parameter
                //one fr attribute names , one for attribute values
                HashMap<String,String> attribMap = ooDocument.getSectionMetadataAttributes(currentSectionName);
                this.sourceSectionName = currentSectionName;
                if (attribMap.size() > 0 ) {
                  
                    this.txtAddressedTo.setText(attribMap.get("Bungeni_QuestionAddressedTo"));
                    this.txtQuestionTitle.setText(attribMap.get("Bungeni_QuestionTitle"));
                    this.txtPersonName.setText(attribMap.get("Bungeni_QuestionMemberFrom"));
                    this.txtPersonURI.setText(attribMap.get("Bungeni_QuestionMemberFromURI"));
                    parent.dispose();
                  return true;
                } else {
                    MessageBox.OK(parent, "No metadata has been set for this section!");
                    parent.dispose();
                    return false;
                }
            //} else {
            //    MessageBox.OK(this.parent, "The Current section, "+currentSectionName + ", does not have any Speech related metadata !");
            //    parent.dispose();
            //    return false;
            //}
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblQuestionText = new javax.swing.JLabel();
        btnSelectQuestion = new javax.swing.JButton();
        txtQuestionTitle = new javax.swing.JTextField();
        lblQuestionTitle = new javax.swing.JLabel();
        txtPersonName = new javax.swing.JTextField();
        lblNameOfPersonFrom = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtAddressedTo = new javax.swing.JTextField();
        lblQuestionAddressedTo = new javax.swing.JLabel();
        separatorLine1 = new javax.swing.JSeparator();
        scrollMessageArea = new javax.swing.JScrollPane();
        txtMessageArea = new javax.swing.JTextArea();
        lblPersonURI = new javax.swing.JLabel();
        txtPersonURI = new javax.swing.JTextField();
        scrollQuestionText = new javax.swing.JScrollPane();
        txtQuestionText = new javax.swing.JTextPane();

        lblQuestionText.setText("Question Text");
        lblQuestionText.setName("lbl_question_text");

        btnSelectQuestion.setText("Select a Question...");
        btnSelectQuestion.setActionCommand("Select a Question");
        btnSelectQuestion.setName("btn_select_question");
        btnSelectQuestion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectQuestionActionPerformed(evt);
            }
        });

        txtQuestionTitle.setName("txt_question_title");

        lblQuestionTitle.setText("Question Title ");
        lblQuestionTitle.setName("lbl_question_title");

        txtPersonName.setName("txt_person_name");

        lblNameOfPersonFrom.setText("Edit name of Person asking Question");
        lblNameOfPersonFrom.setName("lbl_person_name");

        btnApply.setText("Apply");
        btnApply.setName("btn_apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Close");
        btnCancel.setName("btn_cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtAddressedTo.setName("txt_question_to");

        lblQuestionAddressedTo.setText("Question Addressed To :");
        lblQuestionAddressedTo.setName("lbl_question_to");

        txtMessageArea.setBackground(new java.awt.Color(204, 204, 204));
        txtMessageArea.setColumns(20);
        txtMessageArea.setEditable(false);
        txtMessageArea.setFont(new java.awt.Font("Tahoma", 0, 11));
        txtMessageArea.setLineWrap(true);
        txtMessageArea.setRows(5);
        txtMessageArea.setWrapStyleWord(true);
        scrollMessageArea.setViewportView(txtMessageArea);

        lblPersonURI.setText("URI of Person");
        lblPersonURI.setName("lbl_person_uri");

        txtPersonURI.setName("txt_person_uri");

        scrollQuestionText.setName("scroll_question_text");
        txtQuestionText.setName("txt_question_text");
        scrollQuestionText.setViewportView(txtQuestionText);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scrollQuestionText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, separatorLine1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(btnSelectQuestion)
                    .add(lblQuestionTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtQuestionTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(lblNameOfPersonFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPersonName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(btnApply, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 43, Short.MAX_VALUE)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(lblPersonURI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPersonURI, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(lblQuestionAddressedTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 265, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtAddressedTo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(lblQuestionText))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(separatorLine1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnSelectQuestion)
                .add(14, 14, 14)
                .add(lblQuestionTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtQuestionTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblNameOfPersonFrom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtPersonName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblPersonURI)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtPersonURI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblQuestionAddressedTo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtAddressedTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblQuestionText)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrollQuestionText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void returnError (boolean state) {
        btnApply.setEnabled(state);
        btnCancel.setEnabled(state);
        btnSelectQuestion.setEnabled(state);
        return;
    }
    
    public boolean preFullInsert(){
                /*
                 *
           <command   id="drfiQB01.addSectionIntoSectionWithStyling"
                       className="org.bungeni.commands.addSectionIntoSectionWithStyling"/>
            <command   id="drfiQB02.setSectionMetadata"
                       className="org.bungeni.commands.setSectionMetadata"/>
            <command   id="drfiQB03.addDocumentIntoSection"
                       className="org.bungeni.commands.addDocumentIntoSection"/>
            <command   id="drfiQB04.searchAndReplace"
                       className="org.bungeni.commands.searchAndReplace"/>
            <command   id="drfiQB05.addSectionIntoSectionWithStyling2"
                       className="org.bungeni.commands.addSectionIntoSectionWithStyling"/>
            <command   id="drfiQB06.setSectionMetadata"
                       className="org.bungeni.commands.setSectionMetadata"/>
            <command   id="drfiQB07.addDocumentIntoSection"
                       className="org.bungeni.commands.addDocumentIntoSection"/>
            <command   id="drfiQB.searchAndReplace2"
                       className="org.bungeni.commands.searchAndReplace2"/>
            <command   id="drfiQB.renameSection"
                       className="org.bungeni.commands.renameSection"/>
            <command   id="drfiQB.insertHtmlDocumentIntoSection"
                       className="org.bungeni.commands.insertHtmlDocumentIntoSection"/>                 
                 */
            
            log.debug("adding section inside section");
            long sectionBackColor = 0xffffff;
            float sectionLeftMargin = (float).2;
            
            String AddressedTo = txtAddressedTo.getText();
            String PersonName = txtPersonName.getText();
            String QuestionText = txtQuestionText.getText();
            String QuestionTitle = txtQuestionTitle.getText();
            String URI = selectionData.get("QUESTION_FROM");
            String QuestionId = selectionData.get("ID");
            String importHtmlFile = "";
            HashMap<String,String> mainQuestionmeta = new HashMap<String,String>();
            mainQuestionmeta.put("Bungeni_QuestionID", QuestionId);
            mainQuestionmeta.put("Bungeni_QuestionTitle", QuestionTitle);
            mainQuestionmeta.put("Bungeni_QuestionMemberFrom", PersonName);
            mainQuestionmeta.put("Bungeni_QuestionMemberFromURI", URI);
            mainQuestionmeta.put("Bungeni_QuestionAddressedTo", AddressedTo);
            mainQuestionmeta.put("BungeniSectionType", theAction.action_section_type());
            
            UUIDGenerator gen = UUIDGenerator.getInstance();
            UUID uuid = gen.generateTimeBasedUUID();
            String tmpFileName = uuid.toString().replaceAll("-", "")+".html";
            String pathToFile = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator+ "tmp" + File.separator;
            try {
                BufferedWriter out;
                importHtmlFile = pathToFile + tmpFileName;
                out = new BufferedWriter(new FileWriter(new File(importHtmlFile)));
                out.write(QuestionText);
                out.close();
            } catch (IOException ioEx) {
                log.error("preFullInsert: error writing to output file : " + ioEx.getMessage() );
            }
            
            
           /*<command   id="drfiQB01.addSectionIntoSectionWithStyling"
                       className="org.bungeni.commands.addSectionIntoSectionWithStyling"/> */
    
            String strActionSectionName = getActionSectionName();
            formContext.addFieldSet("section_back_color");
            formContext.getFieldSets("section_back_color").add(Long.toHexString(sectionBackColor));
         
            formContext.addFieldSet("section_left_margin");
            formContext.getFieldSets("section_left_margin").add(Float.toString(sectionLeftMargin));
            
            formContext.addFieldSet("container_section");
            formContext.getFieldSets("container_section").add(ooDocument.currentSectionName());
            
            formContext.addFieldSet("current_section");
            formContext.getFieldSets("current_section").add(strActionSectionName);

            /*
             *       <command   id="drfiQB02.setSectionMetadata"
                       className="org.bungeni.commands.setSectionMetadata"/>
                       */
            formContext.addFieldSet("new_section");
            formContext.getFieldSets("new_section").add(strActionSectionName);
            formContext.addFieldSet("section_metadata_map");
            formContext.getFieldSets("section_metadata_map").add(mainQuestionmeta);

            /*<command   id="drfiQB03.addDocumentIntoSection"
                       className="org.bungeni.commands.addDocumentIntoSection"/>*/
            formContext.addFieldSet("document_fragment");
            formContext.getFieldSets("document_fragment").add(FragmentsFactory.getFragment("hansard_question"));
            formContext.addFieldSet("document_import_section");
            formContext.getFieldSets("document_import_section").add(strActionSectionName);
            
              /** <command   id="drfiQB04.searchAndReplace"
                       className="org.bungeni.commands.searchAndReplace"/>**/
            formContext.addFieldSet("search_for");
            formContext.getFieldSets("search_for").add("[[QUESTION_TITLE]]");
            formContext.addFieldSet("replacement_text");
            formContext.getFieldSets("replacement_text").add(QuestionTitle );

            //generate new section name
            String newSectionName = strActionSectionName + "-que1" ;
            int nCounter = 1;
            while (ooDocument.getTextSections().hasByName(newSectionName) ) {
                nCounter++;
                newSectionName = strActionSectionName + "-que"+nCounter;
            }
            /* 
             <command   id="drfiQB05.addSectionIntoSectionWithStyling2"
                       className="org.bungeni.commands.addSectionIntoSectionWithStyling"/>            
            ***/
            formContext.getFieldSets("section_back_color").add(Long.toHexString(0xffffff));
            formContext.getFieldSets("section_left_margin").add(Float.toString((float).4));
            formContext.getFieldSets("container_section").add(strActionSectionName);
            formContext.getFieldSets("current_section").add(newSectionName);
            /*** <command   id="drfiQB06.setSectionMetadata"
                       className="org.bungeni.commands.setSectionMetadata"/> ***/
            formContext.getFieldSets("new_section").add(newSectionName);
            HashMap<String,String> subQuestionMeta = new HashMap<String,String>();
            subQuestionMeta.put("BungeniSectionType", "Question");
            formContext.getFieldSets("section_metadata_map").add(subQuestionMeta);

            /*** <command   id="drfiQB07.addDocumentIntoSection"
                       className="org.bungeni.commands.addDocumentIntoSection"/> ***/
            formContext.getFieldSets("document_import_section").add(newSectionName);
            formContext.getFieldSets("document_fragment").add(FragmentsFactory.getFragment("hansard_question_text"));

            /** <command   id="drfiQB.searchAndReplace2"
                       className="org.bungeni.commands.searchAndReplace2"/> **/
            
            formContext.getFieldSets("search_for").add("[[QUESTION_FROM]]");
            formContext.getFieldSets("replacement_text").add(PersonName);
            formContext.addFieldSet("bookmark_range");
            String[] bookmarkRanges = {"begin-question_from", "end-question_from"};
            formContext.getFieldSets("bookmark_range").add(bookmarkRanges);
            formContext.addFieldSet("url_name");
            formContext.addFieldSet("url_hyperlink");
            formContext.getFieldSets("url_hyperlink").add("Name: "+PersonName+ ";URI: "+selectionData.get("QUESTION_FROM"));
            formContext.getFieldSets("url_name").add("member_url");

            /** <command   id="drfiQB.renameSection"
                       className="org.bungeni.commands.renameSection"/> **/
            formContext.addFieldSet("from_section");
            formContext.getFieldSets("from_section").add("mp_name");
            formContext.addFieldSet("to_section");
            formContext.getFieldSets("to_section").add("meta-mp-"+uuid.toString());

            /** <command   id="drfiQB.insertHtmlDocumentIntoSection"
                       className="org.bungeni.commands.insertHtmlDocumentIntoSection"/> **/                 
            formContext.addFieldSet("import_html_section");
            formContext.addFieldSet("import_html_file");
            formContext.addFieldSet("import_html_style");
            formContext.getFieldSets("import_html_section").add(newSectionName);
            formContext.getFieldSets("import_html_file").add(importHtmlFile);
            formContext.getFieldSets("import_html_style").add("question-text");
            
        return true;
    }
    
    public boolean processFullInsert() {
        boolean bReturn = processCatalogCommand();
        return bReturn;
    }
   
    public boolean postFullInsert(){
        return true;
    }
    
    public boolean preValidationFullInsert(){
        //http://www.fastcompany.com/node/798964/print
        //check if question exists in document.
        return true;
    }
    
    public boolean preFullEdit(){
        
            /*** variable setting for function ***/
        
            //only name can be edited nothing else....
            String sectionName = theAction.getSelectedSectionToActUpon();
            //unprotect any child sections if neccessary, and reprotect them at the end
            //1 change the metadata in the parent section
            //2 change he display text in the inner section
            String childSection = ooDocument.getFirstMatchingDescendantSection(sectionName, "Question", "meta-mp-");
            boolean wasProtected = false;
            if (ooDocument.isSectionProtected(childSection))
                wasProtected = true;
            
            String PersonName = txtPersonName.getText();
            HashMap<String, String> questionMeta = new HashMap<String,String>();
            questionMeta.put("Bungeni_QuestionMemberFrom", PersonName);
            
            /*** end variable setting for function ****/
     
            /*<command   id="drfeQB.replaceLinkInSectionByName"
                       className="org.bungeni.commands.replaceLinkInSectionByName"/> */
 
            /*** add field sets ****/
            
            formContext.addFieldSet("inside_section");
            formContext.addFieldSet("hyperlink_name");
            formContext.addFieldSet("hyperlink_text");
            formContext.addFieldSet("hyperlink_url");
            formContext.addFieldSet("is_protected");
            
            /**** end add field sets ****/
            
            /*** setting field sets ***/
     
            formContext.getFieldSets("inside_section").add(childSection);
            formContext.getFieldSets("hyperlink_name").add("member_url");
            formContext.getFieldSets("hyperlink_text").add(PersonName);
            formContext.getFieldSets("hyperlink_url").add("Name: "+PersonName+ ";URI: "+this.txtPersonURI.getText());
            formContext.getFieldSets("is_protected").add(wasProtected);
  
            /*** end setting field sets ***/
     
            /*<command   id="drfeQB.setSectionMetadata"
                       className="org.bungeni.commands.setSectionMetadata"/>*/
 
            /*** add field sets ****/
            
            formContext.addFieldSet("new_section");
            formContext.addFieldSet("section_metadata_map");
            
            /**** end add field sets ****/
    
            /*** setting field sets ***/
            formContext.getFieldSets("new_section").add(sectionName);
            formContext.getFieldSets("section_metadata_map").add(questionMeta);
            
             /*** end setting field sets ***/
        return true;
    }
    
    
    public boolean processFullEdit() {
        boolean bReturn = processCatalogCommand();
        return bReturn;
    }
    
   public boolean validateFieldValue(Component field, Object fieldValue ) {
        String formFieldName = field.getName();
        //by default always succeed
        boolean bFailure=true;
      //table validations need to be handled directly.
        if (formFieldName.equals("txt_question_title")) {
            bFailure = validateSelectedQuestion(field);
        }
     return bFailure;
     }
    
   private boolean validateSelectedQuestion(Component field) {
      if (selectionData.size() == 0 ) {
             checkFieldsMessages.add("Please select a question first!");
            return false;            
        }
        return true;
   }
   
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt)  {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
        this.FORM_APPLY_NO_ERROR=false;
        super.formApply();
        if (this.FORM_APPLY_NO_ERROR) 
            parent.dispose();
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnSelectQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectQuestionActionPerformed
// TODO add your handling code here:
        rqs = new registryQueryDialog("Select A Question", "Select * from questions", parent);
        rqs.show();
        log.debug("Moved on before closing child dialog");
        selectionData = rqs.getData();
        if (selectionData.size() > 0 ) {
            Set keyset = selectionData.keySet();
            log.debug("selected keyset size = " + keyset.size());
            txtQuestionTitle.setText(selectionData.get("QUESTION_TITLE"));
            txtAddressedTo.setText(selectionData.get("QUESTION_TO"));
            //resolve person name URI to registry entry
            dbInstance.Connect();
            QueryResults rs = dbInstance.QueryResults(GeneralQueryFactory.Q_FETCH_PERSON_BY_URI(selectionData.get("QUESTION_FROM")));
            dbInstance.EndConnect();
            String fullName = "";
            if (rs.hasResults()) {
                
                String[] firstName = rs.getSingleColumnResult("FIRST_NAME");
                String[] lastName = rs.getSingleColumnResult("LAST_NAME");
                if (firstName != null )
                    fullName = firstName[0];
                if (lastName != null ) 
                    fullName += " " + lastName[0];
                
            }
            txtPersonName.setText(fullName);
            
            //
            txtQuestionText.setText(selectionData.get("QUESTON_TEXT"));
            //fillDocument();
        } else {
            log.debug("selected keyset empty");
        }
    }//GEN-LAST:event_btnSelectQuestionActionPerformed

    private void fillDocument(){
           //check if section exists
           //if already exists, bail out with error message
           //else
           //create section with appropriate name
           //set section metadata
           //fill up respetive information on the document.
           String newSectionName = "";
           //must check for action type too, but for testing purposes ignored...
           newSectionName = theAction.action_naming_convention()+"-"+selectionData.get("ID");
           if (ooDocument.getTextSections().hasByName(newSectionName)) {
               MessageBox.OK("There is Question : " + selectionData.get("ID")+" has already been imported into the document!" );
               return;
           }
           //now create section
          ooDocument.addViewSection(newSectionName); 
        
    }
   

   
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectQuestion;
    private javax.swing.JLabel lblNameOfPersonFrom;
    private javax.swing.JLabel lblPersonURI;
    private javax.swing.JLabel lblQuestionAddressedTo;
    private javax.swing.JLabel lblQuestionText;
    private javax.swing.JLabel lblQuestionTitle;
    private javax.swing.JScrollPane scrollMessageArea;
    private javax.swing.JScrollPane scrollQuestionText;
    private javax.swing.JSeparator separatorLine1;
    private javax.swing.JTextField txtAddressedTo;
    private javax.swing.JTextArea txtMessageArea;
    private javax.swing.JTextField txtPersonName;
    private javax.swing.JTextField txtPersonURI;
    private javax.swing.JTextPane txtQuestionText;
    private javax.swing.JTextField txtQuestionTitle;
    // End of variables declaration//GEN-END:variables
    
}
