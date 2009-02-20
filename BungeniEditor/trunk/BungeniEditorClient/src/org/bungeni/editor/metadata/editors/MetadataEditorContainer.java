/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.utils.MessageBox;
import org.bungeni.editor.metadata.EditorDocMetadataDialogFactory;
import org.bungeni.editor.metadata.IEditorDocMetadataDialog;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniFrame;
import org.bungeni.utils.FrameLauncher;

/**
 *
 * @author  undesa
 */
public class MetadataEditorContainer extends JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetadataEditorContainer.class.getName());
     
    
   OOComponentHelper ooDocument  = null;
    JFrame parentFrame = null;
     SelectorDialogModes dlgMode = null;
    IEditorDocMetadataDialog generalDlg = null;
    IEditorDocMetadataDialog metaDlg = null;
    BungeniFileSavePathFormat m_spf = null;
    
    public MetadataEditorContainer(){
        super();
        initComponents();
    }
    
    public MetadataEditorContainer(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlg){
        super();
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        dlgMode = dlg;
        initComponents();
    }
    
    
    
    public void initialize() {
        //add the general metadata panel
        generalDlg = EditorDocMetadataDialogFactory.getInstance("document");
        generalDlg.initVariables(ooDocument, parentFrame, dlgMode);
        generalDlg.initialize();
       
        //add the panel for the document type
        metaDlg = EditorDocMetadataDialogFactory.getInstance(BungeniEditorPropertiesHelper.getCurrentDocType());
        //load the work and expression formats for the current doc type
        m_spf = new BungeniFileSavePathFormat(EditorDocMetadataDialogFactory.WORK_URI, EditorDocMetadataDialogFactory.EXP_URI, EditorDocMetadataDialogFactory.MANIFESTATION_FORMAT);

        if (!metaDlg.getClass().getName().equals(generalDlg.getClass().getName())) {
            metaDlg.initVariables(ooDocument, parentFrame, dlgMode);
            metaDlg.initialize();
        } else 
            metaDlg = null;
        
        metadataTabContainer.add(generalDlg.getPanelComponent(), generalDlg.getTabTitle() );    
        if (metaDlg != null)
        metadataTabContainer.add(metaDlg.getPanelComponent(), metaDlg.getTabTitle());
    }

    public Component getPanelComponent() {
        return this;
    }
        
 
    
   
    

private boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {   
        generalDlg.applySelectedMetadata(spf);
        if (metaDlg != null)
            metaDlg.applySelectedMetadata(spf);
        bState = true;
    } catch (Exception ex) {
        log.error("applySelectedMetadata : " + ex.getMessage());
        bState = false;
    } finally {
        return bState;
    }
}    


private final static String STORE_TO_URL = "StoreToURL";
private final static String STORE_AS_URL = "StoreAsURL";


private boolean saveDocumentToDisk(BungeniFileSavePathFormat spf){
        boolean bState = false; 
        //1 check if file is already open and saved 
            //if open check if there is a new path for the document different from the current path
            //warn the user provide an option to save to the old path
        //2 if file is not saved - save normally - check if there is a file existing in the path and warn if exists
        try {
        //this is the relative base path where hte files are stored
        String defaultSavePath = BungeniEditorProperties.getEditorProperty("defaultSavePath");
        defaultSavePath = defaultSavePath.replace('/', File.separatorChar);
        //parse URI and save path components
        m_spf.parseComponents();
        //get the absolute path
        String exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + defaultSavePath + m_spf.getExpressionPath() ; 
        //get the full path to the file
        String fileFullPath = "";
        fileFullPath = exportPath + File.separator + spf.getManifestationName() + ".odt";
       // MessageBox.OK(fileFullPath);
        
        File fFile = new File(fileFullPath);

        HashMap<String,Object> saveParams = new HashMap<String,Object>();
        
        //1
        if (ooDocument.isDocumentOnDisk()) {
            //document already exists... we just need to save it
            //check generated URL 
            URL genURL = fFile.toURI().toURL();
            //get current open url
            URL curURL = new URL(ooDocument.getDocumentURL());
            if (curURL.toString().equals(genURL.toString())) {
                //the generated and current urls are euals... we can simply sav
                //storeToURL
                 saveParams.put(STORE_TO_URL, fFile.toURI().toString());

            } else {
                int nConfirm = MessageBox.Confirm(parentFrame, "The document will be saved to a new location, as the URI of the document has changed \n Click Yes to save to a new location, Click No to continue saving the document at the current location", "Warning");
                if (nConfirm == JOptionPane.YES_OPTION) {
                 //storeAsURL to new path
                  if (fFile.exists()) {
                        //error message and abort
                        MessageBox.OK(parentFrame, "A file with these attributes already exists, \n please edit the existing file or amend the attributes to save as a different file");
                        bState = false;
                        return false;
                  } else {    
                    File fDir = new File(exportPath);
                    if (!fDir.exists()) {
                        //if path does not exist, create it
                        fDir.mkdirs();
                    }
                    saveParams.put(STORE_AS_URL, genURL.toURI().toString());
                  }
                } else {
                    saveParams.put(STORE_TO_URL, curURL.toURI().toString());
                    //storeTOURL to old path
                }
             }
        } else {
            //check if there is an existing file at the generated path
            if (fFile.exists()) {
                //error message and abort
                MessageBox.OK(parentFrame, "A file with these attributes already exists, \n please edit the existing file or amend the attributes to save as a different file");
                bState = false;
                return false;
            } else {
                //save to new path
                 File fDir = new File(exportPath);
                if (!fDir.exists()) {
                    //if path does not exist, create it
                    fDir.mkdirs();
                    }
                    saveParams.put(STORE_AS_URL, fFile.toURI().toString());
            }
        }
        
        IBungeniDocTransform idocTrans = BungeniTransformationTargetFactory.getDocTransform("ODT");
        idocTrans.setParams(saveParams);
        bState= idocTrans.transform(ooDocument);
       
        } catch (Exception ex) {
            log.error("saveDocumentToDisk : " + ex.getMessage());
            bState = false;
        } finally {
        
        if (bState) {
            MessageBox.OK(parentFrame, "Document was Saved!");
            return true;
        } else {
            MessageBox.OK(parentFrame, "The Document could not be saved!");
            return false;
        }
        }
}
    




    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        metadataTabContainer = new javax.swing.JTabbedPane();

        setBackground(java.awt.Color.lightGray);

        btnSave.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jTextArea1.setBackground(java.awt.Color.lightGray);
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("This is a new document, Please select and enter required metadata to initialize the document");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(metadataTabContainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metadataTabContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
// TODO add your handling code here:
   //APPLY SELECTED METADATA... 
  //  BungeniFileSavePathFormat spf = new BungeniFileSavePathFormat();
    if (applySelectedMetadata(m_spf)) {
        if (saveDocumentToDisk(m_spf)) {
            parentFrame.dispose();
        }
    }
}//GEN-LAST:event_btnSaveActionPerformed

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
    parentFrame.dispose();
}//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTabbedPane metadataTabContainer;
    // End of variables declaration//GEN-END:variables

  
 
    public Dimension getFrameSize() {
        /*
        Dimension gSize = this.generalDlg.getFrameSize();
        if (metaDlg != null ) {
            Dimension mSize = metaDlg.getFrameSize();
            int retWidth = gSize.width; int retHeight = gSize.height;
            if (mSize.height >= gSize.height)
                retHeight = mSize.height;
            if (mSize.width >= gSize.width) 
                retWidth = mSize.width;
            return new Dimension(retWidth, retHeight);
        } else {
        return gSize;
        }*/
        return new Dimension(420, 432 + 15);
    }


    public static JFrame launchMetadataEditor(OOComponentHelper oohc, SelectorDialogModes dlgMode){
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniFrame frm = new BungeniFrame(docType + " Metadata");
        MetadataEditorContainer meta = new MetadataEditorContainer(oohc, frm, dlgMode);
        meta.initialize();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setSize(meta.getFrameSize());
        frm.add(meta.getPanelComponent());
        frm.setVisible(true);
        FrameLauncher.CenterFrame(frm);
        frm.setAlwaysOnTop(true);
        return frm;
    }

   


}
