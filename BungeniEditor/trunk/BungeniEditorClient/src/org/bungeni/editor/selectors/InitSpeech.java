/*
 * InitQuestionBlock.java
 *
 * Created on August 31, 2007, 4:01 PM
 */

package org.bungeni.editor.selectors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.JDialog;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.fragments.FragmentsFactory;
import org.bungeni.editor.macro.ExternalMacro;
import org.bungeni.editor.macro.ExternalMacroFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniUUID;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author  Administrator
 */
public class InitSpeech extends selectorTemplatePanel {

    registryQueryDialog rqs;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InitSpeech.class.getName());
    private String sourceSectionName = "";
    HashMap<String, String> selectionData = new HashMap<String,String>();
    String[] serializedFields = {"SpeechBy", "SpeechByURI" };
    private boolean okayToLaunch = true;
    /** Creates new form InitQuestionBlock */
    public InitSpeech() {
        initComponents();
    }
    public InitSpeech(OOComponentHelper ooDocument, JDialog parentDlg, toolbarAction theAction) {
        super(ooDocument, parentDlg, theAction);
        initComponents();
        initNames();
        initSerializationMap();
        
        setControlModes();
        setControlData();
   
      
    }
   
    private void initNames() {
        this.txt_SpeechBy.setName("SpeechBy");
        this.txt_URIofPerson.setName("SpeechByURI");
    }
    
    private void initSerializationMap(){
        String dummyValue = "";
        for (int i=0; i < serializedFields.length; i++ )
         theSerializationMap.put(serializedFields[i], dummyValue);
    }
    
    /*
     *  1 => sets values from form to map
     *  2 => sets values from map to form
     */
    enum SerializationDirection { FormToMap, MapToForm };
    private void serializeFields(SerializationDirection direction){
       Set<String> fieldNames =  theSerializationMap.keySet();
       Iterator<String> fieldIterator = fieldNames.iterator();
       java.awt.Container parentContainer = this;
 
        if (direction == SerializationDirection.FormToMap) {
           while (fieldIterator.hasNext()) {
               String field = fieldIterator.next();
               String value = getComponentValue(parentContainer, field);
               theSerializationMap.put(field, value);
           }
        } else 
        if (direction == SerializationDirection.MapToForm ) {
           while (fieldIterator.hasNext()) {
                String field = fieldIterator.next();
                String value = theSerializationMap.get(field);
                setComponentValue(parentContainer, field, value);
           }
        }
       log.debug("serializationMap = " + org.bungeni.utils.BungeniLoggingUtils.dumpMap(theSerializationMap));
 
    }
    
    public String getComponentValue (java.awt.Container container, String name) {
        log.debug("getComponentValue = " + name);
        java.awt.Component c = getComponentByName(container, name);
        log.debug("getComponentValue, class name = " + c.getClass().getName());
        if (c.getClass().getName().equals("javax.swing.JTextField")) {
            log.debug("returning value for = " + c.getName());
            return ((javax.swing.JTextField)c).getText();
        } else  
        if (c.getClass().equals("javax.swing.JTextArea")) {
            log.debug("returning value for = " + c.getName());
            return ((javax.swing.JTextArea)c).getText();
        } else
            return null;
    }
    
    public boolean setComponentValue (java.awt.Container container, String name, String value ) {
        boolean bSetValue = false;
        java.awt.Component c = getComponentByName(container, name);
         if (c.getClass().equals("javax.swing.JTextField")) {
           ((javax.swing.JTextField)c).setText(value);
           return true;
        } else  
        if (c.getClass().equals("javax.swing.JTextArea")) {
           ((javax.swing.JTextArea)c).setText(value);
           return true;
        } else
            return bSetValue;
    }
    
    public java.awt.Component getComponentByName (java.awt.Container container, String name) {
        for (int i=0; i < container.getComponentCount() ; i++){
            //if componennt has children recurse to children
            java.awt.Component child = container.getComponent(i);
            log.debug("getComponentByName, loop = " + child.getName());
            if (name.equals(child.getName())) {
                return child;
            }
            
            if (child instanceof java.awt.Container) {
                java.awt.Component c = getComponentByName((java.awt.Container)child, name);
                if (!(c == null ))
                    return c;
            } 
        }
        return null;
    }
    
    public void setControlModes() {
        System.out.println("Current mode = " + theMode);
        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
             txt_URIofPerson.setEditable(false);
            txtMessageArea.setText("You are attempting to insert a new Speech, " +
                    "please select the person making the speech, and edit the name if neccessary");               
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
            txt_SpeechBy.setEditable(false);
            txt_URIofPerson.setEditable(false);
            txtMessageArea.setText("You are attempting to markup some existing text" +
                    " as a Speech, " +
                    "please select the Speech you would like to markup , and press apply" +
                    "to markup the selected text with the correct speech metadata");
        } else if (theMode == SelectorDialogModes.TEXT_EDIT) {
           txt_URIofPerson.setEditable(false);
           txtMessageArea.setText("You are attempting to edit metadata and content for an existing block of text.");
        }
    }
    
    public boolean okToLaunch() {
        return okayToLaunch;
    }
     public void setControlData() {
        try {
        //only in edit mode, only if the metadata properties exist
        if (theMode == SelectorDialogModes.TEXT_EDIT) {
            
            if (goEditMode() == false)  
            {
                this.okayToLaunch = false;
            }
        
           
        } else if (theMode == SelectorDialogModes.TEXT_INSERTION) {
           
          
           
        } else if (theMode == SelectorDialogModes.TEXT_SELECTED_INSERT) {
            
        }
        
        } catch (Exception ex) {
            log.error("SetControlData: "+ ex.getMessage());
        }
    }
     
     public boolean goEditMode() {
                 //get data from metadata in speech section
            String currentSectionName = "";
            currentSectionName = ooDocument.currentSectionName();
            ///do stuff for speech sections retrieve from section metadata////
            ///we probably need a associative metadata attribute factory that
            ///retrieves valid metadata elements for specific seciton types.
            ///how do you identify section types ? probably by naming convention....
            if (currentSectionName.lastIndexOf(theAction.action_naming_convention()) != -1) {
                
                //this section stores MP specific metadata
                //get attribute names for mp specific metadata
                //Bungeni_SpeechMemberName
                //Bungeni_SpeechMemberURI
                //the basic macro for adding attributes takes two arrays as a parameter
                //one fr attribute names , one for attribute values
                log.debug("in goEditMode()");
                HashMap<String,String> attribMap = ooDocument.getSectionMetadataAttributes(currentSectionName);
                this.sourceSectionName = currentSectionName;
                
                if (attribMap.size() > 0 ) {
                  log.debug("attribMap was > 0");
                  log.debug("attribMap keys = " + org.bungeni.utils.BungeniLoggingUtils.dumpMap(attribMap));
                  this.txt_SpeechBy.setText(attribMap.get("Bungeni_SpeechBy"));
                  this.txt_URIofPerson.setText(attribMap.get("Bungeni_SpeechByURI"));
                  return true;
                } else {
                    log.debug("attribMap is  0!");
                    return false;
                }
            } else {
                MessageBox.OK(this.parent, "The Current section, "+currentSectionName + ", does not have any Speech related metadata !");
                return false;
            }
     }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        btn_SpeechBy = new javax.swing.JButton();
        txt_SpeechBy = new javax.swing.JTextField();
        lbl_SpeechBy = new javax.swing.JLabel();
        txt_URIofPerson = new javax.swing.JTextField();
        lbl_URIofPerson = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        separatorLine1 = new javax.swing.JSeparator();
        scrollMessageArea = new javax.swing.JScrollPane();
        txtMessageArea = new javax.swing.JTextArea();

        setPreferredSize(new java.awt.Dimension(299, 280));
        btn_SpeechBy.setText("Select a Person...");
        btn_SpeechBy.setActionCommand("Select a Question");
        btn_SpeechBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SpeechByActionPerformed(evt);
            }
        });

        lbl_SpeechBy.setText("Speech By");

        lbl_URIofPerson.setText("URI of Selected Person");

        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText("Close");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtMessageArea.setBackground(new java.awt.Color(204, 204, 204));
        txtMessageArea.setColumns(20);
        txtMessageArea.setEditable(false);
        txtMessageArea.setFont(new java.awt.Font("Tahoma", 0, 11));
        txtMessageArea.setLineWrap(true);
        txtMessageArea.setRows(5);
        txtMessageArea.setWrapStyleWord(true);
        scrollMessageArea.setViewportView(txtMessageArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(separatorLine1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(lbl_SpeechBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 47, Short.MAX_VALUE)
                        .add(btn_SpeechBy))
                    .add(txt_SpeechBy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(lbl_URIofPerson, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txt_URIofPerson, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(btnApply, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 50, Short.MAX_VALUE)
                        .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollMessageArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(separatorLine1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btn_SpeechBy)
                    .add(lbl_SpeechBy))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txt_SpeechBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbl_URIofPerson)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txt_URIofPerson, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(btnCancel))
                .add(211, 211, 211))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
        parent.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void returnError(boolean state) {
        btnApply.setEnabled(state);
        btnCancel.setEnabled(state);
        btn_SpeechBy.setEnabled(state);
    }
    /*
     *get Value from seriailization map
     *
     */
    private String getValue (String name) {
        return theSerializationMap.get(name);
    }
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt)  {//GEN-FIRST:event_btnApplyActionPerformed
// TODO add your handling code here:
        returnError(false);
        if (this.theMode == SelectorDialogModes.TEXT_EDIT) {
                  //only name can be edited nothing else....
            String sectionName = ooDocument.currentSectionName();
            //unprotect any child sections if neccessary, and reprotect them at the end
            //1 change the metadata in the parent section
            //2 change he display text in the inner section
            String childSection = ooDocument.getMatchingChildSection(sectionName, "meta-mp-");
            boolean wasProtected = false;
            if (ooDocument.isSectionProtected(childSection)) {
                wasProtected = true;
            }
            log.debug("in Text_Edit before serializing fields");
            //send field values to map
            this.serializeFields(SerializationDirection.FormToMap);
            
            ExternalMacro ReplaceLinkInSectionByName = ExternalMacroFactory.getMacroDefinition("ReplaceLinkInSectionByName");
            ReplaceLinkInSectionByName.addParameter(ooDocument.getComponent());
            ReplaceLinkInSectionByName.addParameter(childSection);
            ReplaceLinkInSectionByName.addParameter(new String("member_url"));
            ReplaceLinkInSectionByName.addParameter(getValue("SpeechBy"));
            ReplaceLinkInSectionByName.addParameter( "Name: "+getValue("SpeechBy")+ ";URI: "+ getValue("SpeechByURI"));
            ReplaceLinkInSectionByName.addParameter(wasProtected);
            
            ooDocument.executeMacro(ReplaceLinkInSectionByName.toString(), ReplaceLinkInSectionByName.getParams());

            
            /////now set the section metadata///
            HashMap<String,String> speechMeta = new HashMap<String,String>();
            speechMeta.put("Bungeni_SpeechBy", getValue("SpeechBy"));
            speechMeta.put("Bungeni_SpeechByURI", getValue("SpeechByURI"));
            speechMeta.put("BungeniSectionType", theAction.action_section_type());
            /*
            String[] attrNames = new String[3];
            String[] attrValues = new String[3];
            attrNames[0] = "Bungeni_SpeechBy";
            attrNames[1] = "Bungeni_SpeechByURI";
            attrNames[2] = "Bungeni_SectionType";
            
            attrValues[0] = getValue("SpeechBy");
            attrValues[1] = getValue("SpeechByURI");
            attrValues[2] = "SpeechContainer";
            */
            /*
            ExternalMacro SetSectionMetadata = ExternalMacroFactory.getMacroDefinition("SetSectionMetadata");
            SetSectionMetadata.addParameter(ooDocument.getComponent());
            SetSectionMetadata.addParameter(sectionName );
            SetSectionMetadata.addParameter(attrNames);
            SetSectionMetadata.addParameter(attrValues);
            ooDocument.executeMacro(SetSectionMetadata.toString(), SetSectionMetadata.getParams());
            */
            ooDocument.setSectionMetadataAttributes(sectionName, speechMeta);
           
           
            MessageBox.OK(parent, "Metadata for the section was updated");
            returnError(true);
            parent.dispose();
           
                
      
        } else 
        if (this.theMode == SelectorDialogModes.TEXT_INSERTION) {
          if (selectionData.size() == 0) {
            MessageBox.OK(parent, "Please select a Person!");
            returnError(true);
            return;
            }
         String strCurrentSectionActionCommand = theAction.getSelectedSectionActionCommand();
         String strCurrentSection = theAction.getSelectedSectionToActUpon();
         
          //the below is not valid anymore since we are prompting the user for the section ///
         /*
          ExternalMacro cursorInSection = ExternalMacroFactory.getMacroDefinition("CursorInSection");
            Object retValue = ooDocument.executeMacro(cursorInSection.toString(), cursorInSection.getParams());
            String strCurrentSection = (String)retValue;
           */
         
            if (strCurrentSection.indexOf("-") != -1 ) {
                MessageBox.OK(parent, "A speech can be created only as part of a Main section of text ! \n" +
                        "Please try and add the speech inside a primary container, for e.g. question<no> ");
                returnError(true);
                return;
            }
           
            if (strCurrentSection.trim().length() == 0 ) {
                MessageBox.OK(parent, "You can create a speech only within a Main text section !");
                returnError(true);
                return;
            }
            /*
            if (!strCurrentSection.startsWith("question") ) {
                MessageBox.OK(parent, "You can create speech only within a question");
                returnError(true);
                return;
            }
            */
        String newSectionName = strCurrentSection + "-"+ theAction.action_naming_convention() +"1" ;
        int nCounter = 1;
         while (ooDocument.getTextSections().hasByName(newSectionName) ) {
                nCounter++;
                newSectionName = strCurrentSection+"-"+theAction.action_naming_convention()+nCounter;
            }
        
        
        
        String URI = selectionData.get("URI");
        String PersonName = txt_SpeechBy.getText();
        
        //build attribute metadata for section
        Vector<String> AttrNames = new Vector<String>();
        AttrNames.addElement(new String("Bungeni_MemberName"));
        AttrNames.addElement(new String("Bungeni_MemberURI"));
        String[] strAttrNames = AttrNames.toArray(new String[AttrNames.size()]);
        //String newSectionName = strCurrentSection+"-speech"+speechCounter;
     ExternalMacro createSectionMacro;
     long sectionBackColor = 0xffffff;
     float sectionLeftMargin = (float).6;            
   
     if (theAction.getSelectedSectionActionCommand().equals("INSIDE_SECTION")) {
        createSectionMacro = ExternalMacroFactory.getMacroDefinition("AddSectionInsideSectionWithStyle");
        createSectionMacro.addParameter(ooDocument.getComponent());
        createSectionMacro.addParameter(strCurrentSection);
        createSectionMacro.addParameter(newSectionName);
        createSectionMacro.addParameter(sectionBackColor);
        createSectionMacro.addParameter(sectionLeftMargin);      
     
    } else  /*** if (theAction.getSelectedSectionActionCommand().equals("AFTER_SECTION")) ***/ {
        createSectionMacro = ExternalMacroFactory.getMacroDefinition("InsertSectionAfterSectionWithStyle");
        createSectionMacro.addParameter(ooDocument.getComponent());
        createSectionMacro.addParameter(theAction.action_naming_convention());
        createSectionMacro.addParameter(strCurrentSection);
        createSectionMacro.addParameter(sectionBackColor);
        createSectionMacro.addParameter(sectionLeftMargin);      
       
    }
        /*
        long sectionBackColor = 0xffffff;
        float sectionLeftMargin = (float).6;            
       ExternalMacro AddSectionInsideSection = ExternalMacroFactory.getMacroDefinition("AddSectionInsideSectionWithStyle");
        AddSectionInsideSection.addParameter(ooDocument.getComponent());
        AddSectionInsideSection.addParameter(strCurrentSection);
        AddSectionInsideSection.addParameter(newSectionName);
        AddSectionInsideSection.addParameter(sectionBackColor);
        AddSectionInsideSection.addParameter(sectionLeftMargin);      
        ooDocument.executeMacro(AddSectionInsideSection.toString(), AddSectionInsideSection.getParams());
        */
        ooDocument.executeMacro(createSectionMacro.toString(), createSectionMacro.getParams());
        
        ExternalMacro insertDocIntoSection = ExternalMacroFactory.getMacroDefinition("InsertDocumentIntoSection");
        insertDocIntoSection.addParameter(ooDocument.getComponent());
        insertDocIntoSection.addParameter(newSectionName)   ;
        insertDocIntoSection.addParameter(FragmentsFactory.getFragment("hansard_speech"));
        ooDocument.executeMacro(insertDocIntoSection.toString(), insertDocIntoSection.getParams());
        
        //search replace title into question title marker
        String[] speechBookmarkRanges= {"begin-speech_by", "end-speech_by" };

        ExternalMacro SearchAndReplaceWithAttrs = ExternalMacroFactory.getMacroDefinition("SearchAndReplace2");
        SearchAndReplaceWithAttrs.addParameter(ooDocument.getComponent());
        SearchAndReplaceWithAttrs.addParameter("[[SPEECH_BY]]");
        SearchAndReplaceWithAttrs.addParameter(PersonName);
        SearchAndReplaceWithAttrs.addParameter(speechBookmarkRanges);
        SearchAndReplaceWithAttrs.addParameter("Name: "+PersonName+";URI: "+URI);
        SearchAndReplaceWithAttrs.addParameter("member_url");
        ooDocument.executeMacro(SearchAndReplaceWithAttrs.toString(), SearchAndReplaceWithAttrs.getParams());
        //above also imports a section called speech_by
        //now rename the newly imported section
        //newly imported section is called "speech_by", change the name to meta-mp-uuid identified name
        String renamedSectionName = "meta-mp-"+BungeniUUID.getStringUUID();

        if (ooDocument.renameSection("speech_by", renamedSectionName)) {

                HashMap<String,String> speechMeta = new HashMap<String,String>();
                speechMeta.put("Bungeni_SpeechBy", PersonName);
                speechMeta.put("Bungeni_SpeechByURI", URI);
                speechMeta.put("BungeniSectionType", theAction.action_section_type());
                /*commented....
                String[] attrNames = new String[2];
                String[] attrValues = new String[2];
                attrNames[0] = "Bungeni_SpeechBy";
                attrNames[1] = "Bungeni_SpeechByURI" ;

                attrValues[0] = PersonName;
                attrValues[1] = URI;
                */
                /*
                 *Set metadata into section
                 */
                
                /*commented...*
                ExternalMacro SetSectionMetadata = ExternalMacroFactory.getMacroDefinition("SetSectionMetadata");
                SetSectionMetadata.addParameter(ooDocument.getComponent());
                SetSectionMetadata.addParameter(newSectionName );
                SetSectionMetadata.addParameter(attrNames);
                SetSectionMetadata.addParameter(attrValues);
                */
                //ooDocument.executeMacro(SetSectionMetadata.toString(), SetSectionMetadata.getParams());
                
                ooDocument.protectSection(newSectionName, false);
                ooDocument.setSectionMetadataAttributes(newSectionName, speechMeta);
                ooDocument.protectSection(newSectionName, true);
        }

        returnError(true);
        //MessageBox.OK(parent, "Added new Speech element to document, \n please type in the text of the speech.");
        parent.dispose();
        }
        
    // End of variables declaration                      
        
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btn_SpeechByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SpeechByActionPerformed
// TODO add your handling code here:
        rqs = new registryQueryDialog("Select A Person", "Select * from persons", parent);
        rqs.show();
        log.debug("Moved on before closing child dialog");
        selectionData = rqs.getData();
        if (selectionData.size() > 0 ) {
            txt_SpeechBy.setText(selectionData.get("FIRST_NAME") + " " + selectionData.get("LAST_NAME"));
            txt_URIofPerson.setText(selectionData.get("URI"));
        } else {
            log.debug("selected keyset empty");
        }
        
    }//GEN-LAST:event_btn_SpeechByActionPerformed

    private void fillDocument(){
        
    }
    public void setDialogMode(SelectorDialogModes mode) {
        theMode = mode;
        setControlModes();
    }

    public SelectorDialogModes getDialogMode() {
        return theMode;
    }

    public void setOOComponentHelper(OOComponentHelper ooComp) {
    }

    public void setToolbarAction(toolbarAction action) {
    }

    public void setParentDialog(JDialog dlg) {
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btn_SpeechBy;
    private javax.swing.JLabel lbl_SpeechBy;
    private javax.swing.JLabel lbl_URIofPerson;
    private javax.swing.JScrollPane scrollMessageArea;
    private javax.swing.JSeparator separatorLine1;
    private javax.swing.JTextArea txtMessageArea;
    private javax.swing.JTextField txt_SpeechBy;
    private javax.swing.JTextField txt_URIofPerson;
    // End of variables declaration//GEN-END:variables
    
}
