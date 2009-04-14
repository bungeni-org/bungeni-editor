/*
 * MetadataEditorContainer.java
 *
 * Created on November 4, 2008, 1:43 PM
 */

package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.IQueryResultsIterator;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.extutils.MessageBox;
import org.bungeni.editor.metadata.EditorDocMetadataDialogFactory;
import org.bungeni.editor.metadata.IEditorDocMetadataDialog;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.BungeniFrame;
import org.bungeni.extutils.BungeniRuntimeProperties;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *This JPanel class is the main container for the metadata tabs for a document type.
 * metadata tabs are loaded based on the metadata_model_editors configured for a particular
 * document type
 * @author  Ashok Hariharan
 */
public class MetadataEditorContainer extends JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetadataEditorContainer.class.getName());
    private String EDIT_MESSAGE = "Please edit the metadata for the currently open document using the below form";
    OOComponentHelper ooDocument  = null;
    JFrame parentFrame = null;
     SelectorDialogModes dlgMode = null;
     /**
      * ArrayList to store all the available tabs for the document type.
      */
     ArrayList<IEditorDocMetadataDialog> metaTabs = new ArrayList<IEditorDocMetadataDialog>(0);

    BungeniFileSavePathFormat m_spf = null;
    
    public MetadataEditorContainer(){
        super();
        initComponents();
    }
    
    public MetadataEditorContainer(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlg){
        super();
        initComponents();

        ooDocument = ooDoc;
        parentFrame = parentFrm;
        dlgMode = dlg;
        if (dlgMode.equals(SelectorDialogModes.TEXT_EDIT)) {
            txtMsgArea.setText(EDIT_MESSAGE);
        }

    }
    


    /**
     * Loads the availabled metadata editors for this document type
     * Retrieves the applicable URI types for this document type
     */
    public void initialize() {
        //get the available tabs for this document type
        this.metaTabs= EditorDocMetadataDialogFactory.getInstances(BungeniEditorPropertiesHelper.getCurrentDocType());
        for (IEditorDocMetadataDialog mTab : this.metaTabs) {
            mTab.initVariables(ooDocument, parentFrame, dlgMode);
            mTab.initialize();
        }

        //get work, exp, manifestation formats :
        BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        db.Connect();
        QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_DOCUMENT_TYPE_BY_NAME(BungeniEditorPropertiesHelper.getCurrentDocType()));
        db.EndConnect();
        metadataModelInfoIterator modelIterator = new metadataModelInfoIterator();
        qr.resultsIterator(modelIterator );

        m_spf = new BungeniFileSavePathFormat(modelIterator.WORK_URI, modelIterator.EXP_URI, modelIterator.MANIFESTATION_FORMAT);
        //now load the newly created tabs
        for (IEditorDocMetadataDialog thisTab : this.metaTabs) {
            metadataTabContainer.add(thisTab.getPanelComponent(), thisTab.getTabTitle());
        }
    }


        /**
     * Results iterator for retrieving metadata model info
     */
    class metadataModelInfoIterator implements IQueryResultsIterator {

        public String WORK_URI, EXP_URI, MANIFESTATION_FORMAT ;

        public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                       WORK_URI =  mQR.getField(rowData, "WORK_URI");
                       EXP_URI = mQR.getField(rowData, "EXP_URI");
                       MANIFESTATION_FORMAT = mQR.getField(rowData, "FILE_NAME_SCHEME");
                       return true;
        }
    }


    public Component getPanelComponent() {
        return this;
    }
        

    ArrayList<String> formErrors = new ArrayList<String>(0);

    /**
     * validateSelectedMetadata
     * @param m_spf
     * @return
     */
    private boolean validateSelectedMetadata(BungeniFileSavePathFormat m_spf) {
        boolean bState = false;
        try {
            formErrors.clear();
            //iterate through the tabs and apply them individually
            for (IEditorDocMetadataDialog mTab : this.metaTabs) {
                formErrors.addAll(mTab.validateSelectedMetadata(m_spf));
            }

        } catch (Exception ex) {

        } finally {
            return (formErrors.size() > 0)? false: true;
        }
    }

    
   
    
/**
 * Apply the metadata in all the available tabs into the document
 * @param spf
 * @return
 */
private boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        //iterate through the tabs and apply them individually
        for (IEditorDocMetadataDialog mTab : this.metaTabs) {
            mTab.applySelectedMetadata(spf);
        }

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

/**
 * Saves the document to disk
 * @param spf
 * @return
 */
private boolean saveDocumentToDisk(BungeniFileSavePathFormat spf){
        boolean bState = false;
        String savedFile = "";
        //1 check if file is already open and saved 
            //if open check if there is a new path for the document different from the current path
            //warn the user provide an option to save to the old path
        //2 if file is not saved - save normally - check if there is a file existing in the path and warn if exists
        try {
        //this is the relative base path where hte files are stored
        log.debug("saveDocumentToDisk: begin");
        String defaultSavePath = BungeniEditorProperties.getEditorProperty("defaultSavePath");
        defaultSavePath = defaultSavePath.replace('/', File.separatorChar);
        log.debug("saveDocumentToDisk: defaultSavePath : " + defaultSavePath);

        //parse URI and save path components
        m_spf.parseComponents();
        //get the absolute path
        String exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + defaultSavePath + m_spf.getExpressionPath() ; 
        
        log.debug("saveDocumentToDisk : exportPath = " + exportPath);
        //get the full path to the file
        String fileFullPath = "";
        fileFullPath = exportPath + File.separator + spf.getManifestationName() + ".odt";
       // MessageBox.OK(fileFullPath);
        log.debug("saveDocumentToDisk : fileFullPath = " + fileFullPath);
        
        File fFile = new File(fileFullPath);
        savedFile = fFile.toURI().toString();
        HashMap<String,Object> saveParams = new HashMap<String,Object>();
        
        //1
        if (ooDocument.isDocumentOnDisk()) {
            log.debug("saveDocumentToDisk : file is on disk");
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
                        MessageBox.OK(parentFrame, "A file with these attributes already exists, \n please edit the existing file or amend the attributes to save as a different file. \n Usually this means you are trying to save a document with an 'Official Date' that already exists");
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
            log.debug("saveDocumentToDisk : file is NOT on disk");

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
            log.error("saveDocumentToDisk :" + CommonExceptionUtils.getStackTrace(ex));
            bState = false;
        } finally {
        
        if (bState) {
            this.txtMsgArea.setText(EDIT_MESSAGE);
            BungeniRuntimeProperties.setProperty("SAVED_FILE", savedFile);
            //MessageBox.OK(parentFrame, "Document was Saved!");
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
        txtMsgArea = new javax.swing.JTextArea();
        metadataTabContainer = new javax.swing.JTabbedPane();

        btnSave.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        btnSave.setText(bundle.getString("MetadataEditorContainer.btnSave.text")); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText(bundle.getString("MetadataEditorContainer.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtMsgArea.setBackground(java.awt.Color.lightGray);
        txtMsgArea.setColumns(20);
        txtMsgArea.setEditable(false);
        txtMsgArea.setLineWrap(true);
        txtMsgArea.setRows(5);
        txtMsgArea.setText(bundle.getString("MetadataEditorContainer.txtMsgArea.text")); // NOI18N
        txtMsgArea.setWrapStyleWord(true);
        txtMsgArea.setBorder(null);
        jScrollPane1.setViewportView(txtMsgArea);

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
    if (validateSelectedMetadata(m_spf)) {
        if (applySelectedMetadata(m_spf)) {
            if (saveDocumentToDisk(m_spf)) {
                parentFrame.dispose();
            }
        }
    } else {
        StringBuffer bf = new StringBuffer();
        for (String msg : formErrors) {
                bf.append(msg + "\n");
        }
        MessageBox.OK(this.parentFrame, bf.toString());
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
    private javax.swing.JTabbedPane metadataTabContainer;
    private javax.swing.JTextArea txtMsgArea;
    // End of variables declaration//GEN-END:variables

  
 
    public Dimension getFrameSize() {
        return new Dimension(420, 432 + 15);
    }

    /**
     * Static api to launch metadata editor for a document 
     * @param oohc OpenOffice document handle
     * @param dlgMode mode with which to open metadata dialog edit / insert
     * @return Handle to metadata editor window frame
     */
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
