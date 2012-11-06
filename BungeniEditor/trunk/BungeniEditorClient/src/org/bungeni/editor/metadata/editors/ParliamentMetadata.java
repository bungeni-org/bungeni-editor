/*
 * DebateRecordMetadata.java
 *
 * Created on November 4, 2008, 1:43 PM
 */
package org.bungeni.editor.metadata.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.bungeni.editor.metadata.ParliamentMetadataModel;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.extutils.CommonStringFunctions;

/**
 *
 * @author  Ashok Hariharan
 */
public class ParliamentMetadata extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ParliamentMetadata.class.getName());
    ParliamentMetadataModel docMetaModel = new ParliamentMetadataModel();

    public ParliamentMetadata() {
        super();
        initComponents();
    }

    @Override
    public void initialize() {
        super.initialize();
        loadParliamentInfo();
        this.docMetaModel.setup();
        if (theMode == SelectorDialogModes.TEXT_INSERTION) {
        } else if (theMode == SelectorDialogModes.TEXT_EDIT) {
            docMetaModel.loadModel(ooDocument);
        }
        try {
            String sParlId = docMetaModel.getItem("BungeniParliamentID");
            String sParlSitting = docMetaModel.getItem("BungeniParliamentSitting");
            String sParlSession = docMetaModel.getItem("BungeniParliamentSession");
            if (!CommonStringFunctions.emptyOrNull(sParlId)) {
                this.txtParliamentID.setText(sParlId);
            }
            if (!CommonStringFunctions.emptyOrNull(sParlSession)) {
                this.txtParliamentSession.setText(sParlSession);
            }
            if (!CommonStringFunctions.emptyOrNull(sParlSitting)) {
                this.txtParliamentSitting.setText(sParlSitting);
            }

        } catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
        }
    }

    public Component getPanelComponent() {
        return this;
    }


    /**
     * Loads the metadata information about the parliament
     */
    
    // !+BUNGENI_CONNECTOR(reagan,06-01-2012)
    // Changed the Initialization of the BungeniConnector Object
    // to ensure that metadata is accessed using the REST API
    // rather than directly from the datasource
    private void loadParliamentInfo() {
        BungeniConnector client = null ;
        try {
            client = CommonConnectorFunctions.getDSClient();
            List<MetadataInfo> metadata = client.getMetadataInfo();
            if (metadata != null) {
                for (int i = 0; i < metadata.size(); i++) {
                    if (metadata.get(i).getName().equalsIgnoreCase("ParliamentID")) {
                        docMetaModel.setBungeniParliamentID(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("ParliamentSession")) {
                        docMetaModel.setBungeniParliamentSession(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("ParliamentSitting")) {
                        docMetaModel.setBungeniParliamentSitting(metadata.get(i).getValue());
                    }
                    System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                }
            }
            client.closeConnector();
        } catch (IOException ex) {
            log.error("THe connector client could not be initialized" , ex);
        }
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        try {
            String sParliamentID = this.txtParliamentID.getText();
            String sParliamentSitting = this.txtParliamentSitting.getText();
            String sParliamentSession = this.txtParliamentSession.getText();

            // docMetaModel.updateItem("BungeniParliamentID")
            docMetaModel.updateItem("BungeniParliamentID", sParliamentID);
            docMetaModel.updateItem("BungeniParliamentSitting", sParliamentSitting);
            docMetaModel.updateItem("BungeniParliamentSession", sParliamentSession);
            //  spf.setSaveComponent("FileName", spf.getFileName());
            docMetaModel.saveModel(ooDocument);
            bState = true;
        } catch (Exception ex) {
            log.error("applySelectedMetadata : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
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

        txtParliamentID = new javax.swing.JTextField();
        lblParliamentID = new javax.swing.JLabel();
        txtParliamentSession = new javax.swing.JTextField();
        lblParliamentSession = new javax.swing.JLabel();
        txtParliamentSitting = new javax.swing.JTextField();
        lblParliamentSitting = new javax.swing.JLabel();

        txtParliamentID.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtParliamentID.setName("fld.BungeniParliamentID"); // NOI18N

        lblParliamentID.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblParliamentID.setText(bundle.getString("ParliamentMetadata.lblParliamentID.text")); // NOI18N
        lblParliamentID.setName("lbl.BungeniParliamentID"); // NOI18N

        txtParliamentSession.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtParliamentSession.setName("fld.BungeniParliamentSession"); // NOI18N

        lblParliamentSession.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblParliamentSession.setText(bundle.getString("ParliamentMetadata.lblParliamentSession.text")); // NOI18N
        lblParliamentSession.setName("lbl.BungeniParliamentSession"); // NOI18N

        txtParliamentSitting.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        txtParliamentSitting.setName("BungeniParliamentSitting"); // NOI18N

        lblParliamentSitting.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblParliamentSitting.setText(bundle.getString("ParliamentMetadata.lblParliamentSitting.text")); // NOI18N
        lblParliamentSitting.setName("lbl.BungeniParliamentSitting"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtParliamentSitting, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(txtParliamentSession, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(txtParliamentID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(lblParliamentID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblParliamentSession, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblParliamentSitting, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblParliamentID)
                .addGap(2, 2, 2)
                .addComponent(txtParliamentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblParliamentSession, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParliamentSession, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblParliamentSitting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtParliamentSitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblParliamentID;
    private javax.swing.JLabel lblParliamentSession;
    private javax.swing.JLabel lblParliamentSitting;
    private javax.swing.JTextField txtParliamentID;
    private javax.swing.JTextField txtParliamentSession;
    private javax.swing.JTextField txtParliamentSitting;
    // End of variables declaration//GEN-END:variables

    @Override
    public Dimension getFrameSize() {
        int DIM_X = 347;
        int DIM_Y = 294;
        return new Dimension(DIM_X, DIM_Y + 10);
    }

    @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
        addFieldsToValidate(new TreeMap<String, Component>() {

            {
                put(lblParliamentID.getText().replace("*", ""), txtParliamentID);
                put(lblParliamentSitting.getText().replace("*", ""), txtParliamentSitting);
                put(lblParliamentSession.getText().replace("*", ""), txtParliamentSession);
            }
        });
        return super.validateSelectedMetadata(spf);
    }
}
