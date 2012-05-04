
package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.transforms.impl.BungeniTransformationTargetFactory;
import org.bungeni.ooo.transforms.impl.IBungeniDocTransform;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.editor.metadata.EditorDocMetadataDialogFactory;
import org.bungeni.editor.metadata.IEditorDocMetadataDialog;
import org.bungeni.editor.noa.BungeniNoaTabbedPane;
import org.bungeni.extutils.*;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *This JPanel class is the main container for the metadata tabs for a document type.
 * metadata tabs are loaded based on the metadata_model_editors configured for a particular
 * document type
 * @author  Ashok Hariharan
 */
public class MetadataEditorContainer extends JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetadataEditorContainer.class.getName());
    private String EDIT_MESSAGE = bundle.getString("edit_metadata_with_form");
    OOComponentHelper ooDocument  = null;
    JFrame parentFrame = null;
     SelectorDialogModes dlgMode = null;
     /**
      * ArrayList to store all the available tabs for the document type.
      */
     ArrayList<IEditorDocMetadataDialog> metaTabs = new ArrayList<IEditorDocMetadataDialog>(0);

    BungeniFileSavePathFormat m_spf = null;

    NextTabAction nextAction = new NextTabAction(bundle.getString("btn_next"));
    PrevTabAction prevAction = new PrevTabAction(bundle.getString("btn_prev"));
    ApplyButtonSaveAction saveAction = new ApplyButtonSaveAction(bundle.getString("msg_save"));

    public MetadataEditorContainer(){
        super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
    }
    
    public MetadataEditorContainer(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlg){
        super();
        initComponents();

        ooDocument = ooDoc;
        parentFrame = parentFrm;
        
        CommonUIFunctions.compOrientation(parentFrm);
        CommonUIFunctions.compOrientation(this);
        
        dlgMode = dlg;
        if (dlgMode.equals(SelectorDialogModes.TEXT_EDIT)) {
            txtMsgArea.setText(EDIT_MESSAGE);
        }
        
        this.btnSave.setAction(saveAction);
        this.btnNavigate.setAction(nextAction);
        this.metadataTabContainer.addChangeListener(new MetaTabsChangeListener());

    }

    class NextTabAction extends AbstractAction {
        public NextTabAction(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
            int nIndex = metadataTabContainer.getSelectedIndex();
            int nNoOfTabs = metadataTabContainer.getTabCount();
            if (!(nIndex == (nNoOfTabs - 1))) {
                   metadataTabContainer.setSelectedIndex(nIndex + 1) ;
            }
        }

    }

    class PrevTabAction extends AbstractAction {
        public PrevTabAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent e) {
            int nIndex = metadataTabContainer.getSelectedIndex();
            if (nIndex  > 0) {
                   metadataTabContainer.setSelectedIndex(nIndex - 1) ;
            }
        }

    }

    class MetaTabsChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            int iIndex = metadataTabContainer.getSelectedIndex();
            if (iIndex == 0 ) {
                btnNavigate.setAction(nextAction);
            } else {
                btnNavigate.setAction(prevAction);
            }
        }

    }

    /**
     * Loads the availabled metadata editors for this document type
     * Retrieves the applicable URI types for this document type
     */
    public void initialize() {
        // if(Locale.getDefault().getLanguage().equals("ar") && Locale.getDefault().getCountry().equals("PS") )
        //    CommonEditorFunctions.compOrientation(this);
        //get the available tabs for this document type
        log.info("calling initialize .....");
        String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();
        log.info("initialize : current doc type = " + currentDocType);
        this.metaTabs= EditorDocMetadataDialogFactory.getInstances(currentDocType);
        for (IEditorDocMetadataDialog mTab : this.metaTabs) {
            if (mTab == null ) log.error("initialize : returned metadata tab is null");
            mTab.initVariables(ooDocument, parentFrame, dlgMode);
            log.info("initialize : after calling initVariables");
            mTab.initialize();
        }

        Element doctypeElem = null;
        try {
            //get work, exp, manifestation formats :
           doctypeElem = DocTypesReader.getInstance().getDocTypeByName(BungeniEditorPropertiesHelper.getCurrentDocType());
        } catch (JDOMException ex) {
           log.error("Error getting doctype config", ex);
        }
        if (null != doctypeElem) {
            m_spf = new BungeniFileSavePathFormat(
                    DocTypesReader.getInstance().getWorkUriForDocType(doctypeElem),
                    DocTypesReader.getInstance().getExpUriForDocType(doctypeElem),
                    DocTypesReader.getInstance().getFileNameSchemeForDocType(doctypeElem)
                    );
            //now load the newly created tabs
            for (IEditorDocMetadataDialog thisTab : this.metaTabs) {
                metadataTabContainer.add(thisTab.getPanelComponent(), thisTab.getTabTitle());
            }
        }

        CommonUIFunctions.compOrientation(this);
    }


        /**
     * Results iterator for retrieving metadata model info
     */
    /***
    class metadataModelInfoIterator implements IQueryResultsIterator {

        public String WORK_URI, EXP_URI, MANIFESTATION_FORMAT ;

        public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                       WORK_URI =  mQR.getField(rowData, "WORK_URI");
                       EXP_URI = mQR.getField(rowData, "EXP_URI");
                       MANIFESTATION_FORMAT = mQR.getField(rowData, "FILE_NAME_SCHEME");
                       return true;
        }
    }
***/

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
                ArrayList<String> errors = mTab.validateSelectedMetadata(m_spf);
                if (errors.size() > 0 )  {
                    formErrors.add(mTab.getTabTitle() + " Tab :-");
                    formErrors.addAll(errors);
                    formErrors.add("");
                }
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

            //get the absolute path
            String exportPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + defaultSavePath + m_spf.getExpressionFilePath() ;

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
                    int nConfirm = MessageBox.Confirm(parentFrame, bundle.getString("prompt_doc_save_new_location"), bundle.getString("msg_warning"));
                    if (nConfirm == JOptionPane.YES_OPTION) {
                    //storeAsURL to new path
                    if (fFile.exists()) {
                            //error message and abort
                            MessageBox.OK(parentFrame, bundle.getString("file_exists"));
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
                    MessageBox.OK(parentFrame, bundle.getString("file_exists2"));
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
        }
        if (bState) {
            this.txtMsgArea.setText(EDIT_MESSAGE);
            BungeniRuntimeProperties.setProperty("SAVED_FILE", savedFile);
           /// Update the title of the tab in the noatabbedpane
           String newFrameTitle = OOComponentHelper.getFrameTitle(ooDocument.getTextDocument());
           int nSelected = BungeniNoaTabbedPane.getInstance().getTabbedPane().getSelectedIndex();
           BungeniNoaTabbedPane.getInstance().getTabbedPane().setTitleAt(nSelected, newFrameTitle);
           /// Update - End
           return true;
        } else {
            MessageBox.OK(parentFrame, bundle.getString("doc_not_saved"));
            return false;
        }
}
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle");
    


    class ApplyButtonSaveAction extends AbstractAction {

        public ApplyButtonSaveAction(String text ) {
            super(text);
        }
        
        public void actionPerformed(ActionEvent e) {

            if (validateSelectedMetadata(m_spf)) {
                if (applySelectedMetadata(m_spf)) {
                    applyMetadataSetFlag();
                    if (saveDocumentToDisk(m_spf)) {
                         parentFrame.dispose();
                    }
                }
            } else {
                StringBuilder bf = new StringBuilder();
                for (String msg : formErrors) {
                    bf.append(msg).append("\n");
                }
                MessageBox.OK(parentFrame, bf.toString(), bundle.getString("incomplete_fields"), JOptionPane.ERROR_MESSAGE);
            }

        }

        private void applyMetadataSetFlag() {
            ooDocMetadata meta = new ooDocMetadata(ooDocument);
            meta.AddProperty(BaseEditorDocMetaModel.__METADATA_SET_FLAG__, "true");
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
        btnNavigate = new javax.swing.JButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnSave.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        btnSave.setText(bundle.getString("MetadataEditorContainer.btnSave.text")); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
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

        btnNavigate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        btnNavigate.setText(bundle.getString("MetadataEditorContainer.btnNavigate.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(metadataTabContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(btnNavigate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metadataTabContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave)
                    .addComponent(btnNavigate))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
    parentFrame.dispose();
}//GEN-LAST:event_btnCancelActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_btnSaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNavigate;
    private javax.swing.JButton btnSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane metadataTabContainer;
    private javax.swing.JTextArea txtMsgArea;
    // End of variables declaration//GEN-END:variables

  
 
    public Dimension getFrameSize() {
        return new Dimension(470, 550);
    }

    /**
     * Static api to launch metadata editor for a document 
     * @param oohc OpenOffice document handle
     * @param dlgMode mode with which to open metadata dialog edit / insert
     * @return Handle to metadata editor window frame
     */
    public static JFrame launchMetadataEditor(OOComponentHelper oohc, SelectorDialogModes dlgMode){
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniFrame frm = new BungeniFrame("frameTitle");
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
