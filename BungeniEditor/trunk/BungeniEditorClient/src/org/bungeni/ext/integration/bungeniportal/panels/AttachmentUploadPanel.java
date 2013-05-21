/*
 * Copyright (C) 2013 PC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.ext.integration.bungeniportal.panels;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.http.entity.mime.content.ContentBody;
import org.bungeni.ext.integration.bungeniportal.BungeniAppConnector;
import org.bungeni.ext.integration.bungeniportal.BungeniAppConnector.WebResponse;
import org.bungeni.ext.integration.bungeniportal.docimpl.BungeniAttachment;
import org.bungeni.ext.integration.bungeniportal.BungeniServiceAccess;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.extutils.DisabledGlassPane;
import org.bungeni.extutils.NotifyBox;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniDialog;

/**
 *
 * @author Ashok Hariharan
 */
public class AttachmentUploadPanel extends javax.swing.JPanel {

    private String attachmentDocURL ;
    private String fileToUpload ; 
    private BungeniDialog parentDialog ;
    private OOComponentHelper ooDocument;
    private boolean proceed = false;
    
    
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(AttachmentUploadPanel.class.getName());


    private DisabledGlassPane glassPane = new DisabledGlassPane();

    ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle(
           "org/bungeni/extpanels/bungeni/Bundle"
           );
    
    /**
     * Creates new form AttachmentUploadPanel
     */
    public AttachmentUploadPanel(BungeniDialog dlg, OOComponentHelper ooDocument, String docURL, String pathToFile) {
        initComponents();
        this.parentDialog = dlg;
        this.attachmentDocURL = docURL;
        this.ooDocument = ooDocument;
        this.fileToUpload = pathToFile;
    }

    
    public void init(){
        CommonUIFunctions.disablePanel(this.glassPane, this.parentDialog, "Loading Attachment Info" );
        LoadAttInfoExec loadAtt = new LoadAttInfoExec(this.attachmentDocURL);
        loadAtt.execute();
    }
    
    class LoadAttInfoExec extends SwingWorker<BungeniAttachment, BungeniAttachment>{
     
       String attachmentURL ; 
       BungeniAttachment attachment ; 
       
       public LoadAttInfoExec(String attURL) {
           this.attachmentURL = attURL;
       }
       
        @Override
        protected BungeniAttachment doInBackground() throws Exception {
            BungeniAttachment att = BungeniServiceAccess.getInstance().getAttachmentFromURL(attachmentURL);
            return att;
        }
          
       @Override
       protected void done(){
           try {
               BungeniAttachment att = get();
               if (att != null ){
                    txtTitle.setText(att.title);
                    txtDescription.setText(att.description);
                    lblFileName.setText(att.fileName);
                    glassPane.deactivate();
               }
           } catch (InterruptedException ex) {
               log.error("Error parsing attachment", ex);
           } catch (ExecutionException ex) {
               log.error("Error parsing attachment", ex);
           }
       }

   }
    
   class UploadExec extends SwingWorker<BungeniAppConnector.WebResponse, Boolean>{

        String odfdocPath;
        String docTitle;
        String docDesc ; 
        boolean uploadSuccess = false;
        
        public UploadExec(String filePath, String title, String description){
            this.odfdocPath = filePath;
            this.docDesc = description;
            this.docTitle = title;
        }
       
        @Override
        protected WebResponse doInBackground() throws Exception {
            String sAttURL = ooDocument.getPropertyValue("PortalAttSource");
            HashMap<String, ContentBody> formFields = 
                BungeniServiceAccess.getInstance().getAttachmentEditSubmitInfo(sAttURL);
            formFields = 
                BungeniServiceAccess.getInstance().attachmentEditSubmitPostQuery(
                    formFields, 
                    docTitle, 
                    docDesc, 
                    odfdocPath
                );
            WebResponse wr = BungeniServiceAccess.getInstance().doEdit(sAttURL, formFields);
            return wr;
        }
        
        
       @Override
       protected void done(){
            try {
                BungeniAppConnector.WebResponse wr  = get();
                if (wr.getStatusCode() == 200 ) {
                    this.uploadSuccess = true;
                    proceed = true;
                    parentDialog.dispose();
                }
                glassPane.deactivate();
            } catch (InterruptedException ex) {
                log.error("Versioning was interrupted", ex);
            } catch (ExecutionException ex) {
                log.error("Versioning was interrupted", ex);
            }
            
        }
   }

    private boolean errorMessageOnEmpty(String value, String message) {
        if (value != null ) {
            if (value.trim().length() ==  0 ) {
                NotifyBox.error(message);
                return false;
            }
        } else {
            NotifyBox.error(message);
            return false;
        }
        return true;
    }
        
    public boolean proceed(){
        return this.proceed;
    }
    
    private void disablePanel(){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(BUNDLE.getString("RETRIEVE_DOCS_MESSAGE"));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFileName = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        lblDescription = new javax.swing.JLabel();
        filenameLabel = new javax.swing.JLabel();

        lblFileName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnUpload.setText("Upload Document to Bungeni");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        lblTitle.setText("Title");

        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        lblDescription.setText("Description");

        filenameLabel.setText("File Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnUpload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addComponent(lblFileName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTitle)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                            .addComponent(lblDescription)
                            .addComponent(filenameLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(filenameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnUpload)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        String sTitle = this.txtTitle.getText();
        if (!errorMessageOnEmpty(sTitle, "Please enter a Title !")) {
            return;
        }
        String sDesc = this.txtDescription.getText();
        if (!errorMessageOnEmpty(sDesc, "Please enter a Description !")) {
            return;
        }     
        
        disablePanel();
        UploadExec exec = new UploadExec(this.ooDocument.getDocumentURL(), sTitle, sDesc);
        exec.execute();
        
        /**
        BungeniTransitionConfirmationPanel.TransitionExec exec = new BungeniTransitionConfirmationPanel.TransitionExec(
                dtDate.getDate(), 
                (Date)spnTime.getValue()
                );
        exec.execute();
        * **/

    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.parentDialog.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnUpload;
    private javax.swing.JLabel filenameLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
