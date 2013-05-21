/*
 * Copyright (C) 2013 Africa i-Parliaments
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
package org.bungeni.ext.integration.bungeniportal;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.http.message.BasicNameValuePair;
import org.bungeni.ext.integration.bungeniportal.BungeniAppConnector.WebResponse;
import org.bungeni.extutils.DisabledGlassPane;
import org.bungeni.extutils.NotifyBox;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniDialog;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniTransitionConfirmationPanel extends javax.swing.JPanel {

        private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniTransitionConfirmationPanel.class.getName());

    
       private DisabledGlassPane glassPane = new DisabledGlassPane();
 
          ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/bungeni/extpanels/bungeni/Bundle");

    
    /**
     * Creates new form BungeniTransitionConfirmationPanel
     */
    private BungeniTransitionConfirmationPanel() {
        initComponents();
    }

    private BungeniDialog parentDialog = null;
    private Transition transition = null;
    private String status = null;
    private String title = null;
    private OOComponentHelper ooDocument = null;
    private String sAttURL = null;
    private boolean transitionSuccessful = false;
    
    public BungeniTransitionConfirmationPanel(BungeniDialog dlg, OOComponentHelper ooDoc, Transition transition, String strStatus, String strTitle){
        this();
        this.status = strStatus;
        this.title = strTitle;
        this.parentDialog = dlg;
        this.transition = transition;
        this.ooDocument = ooDoc;
    }
    
    public void init(){
        //status
        this.txtFromStatus.setText(this.status);
        this.txtToStatus.setText(this.transition.title);
        // set date
        Date now = new Date();
        this.dtDate.setDate(now);
        
        // set time
        spnTime.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnTime, "HH:mm:ss");
        spnTime.setEditor(timeEditor);
        spnTime.setValue(now);
        // this is the url of the attachment
        this.sAttURL = ooDocument.getPropertyValue("PortalAttSource");
    }
    
    private void disablePanel(){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(BUNDLE.getString("RETRIEVE_DOCS_MESSAGE"));
    }
    
    class TransitionExec extends SwingWorker<WebResponse, Boolean>{

        String transitionDate;
        String transitionTime;
        
        public TransitionExec(Date transitionDate, Date transitionTime){
            this.transitionDate = BungeniServiceAccess.getInstance().getTransitionDate(
                    transitionDate
                    );
            this.transitionTime = BungeniServiceAccess.getInstance().getTransitionTime(
                    transitionTime
                    );
        }
        
        @Override
        protected WebResponse doInBackground() throws Exception {
            // get the form information for posting 
            // this is because we dont know the kind of fields form lib generates
            // form lib uses the UNTRANSLATED transition title to generate hashes for 
            // submission buttons, we cannot know/guess that, so we make a rountrip
            //tothe server to get the form and parse the field names
            List<BasicNameValuePair> formFields = 
                    BungeniServiceAccess.getInstance().
                        getWfTransitionInputTypeSubmitInfo(
                            sAttURL
                        );
            
            //get the post parameters
            List<BasicNameValuePair> postParams = BungeniServiceAccess.getInstance().
                    attachmentWorkflowTransitPostQuery(
                        transition, 
                        this.transitionDate, 
                        this.transitionTime,
                        formFields
                    );
            // post
            WebResponse wr = BungeniServiceAccess.getInstance().doTransition(
                    sAttURL, 
                    postParams 
                    );
            return wr;
        }

        @Override
        protected void done()
        {
            try {
                WebResponse wr = get();
                if (wr != null ) {
                    if (wr.getStatusCode() == 200 ) {
                        glassPane.deactivate();
                        transitionSuccessful = true;
                        //NotifyBox.info("Transited !" );
                        parentDialog.dispose();
                    }
                } else {
                    NotifyBox.error("Could not Transit !" );
                }
            } catch (InterruptedException ex) {
               log.error("Error while parsing document ", ex);
            } catch (ExecutionException ex) {
               log.error("Error while parsing document ", ex);
            } finally {
                glassPane.deactivate();
            }
        }
 }

    public boolean getTransitionSuccessful(){
        return this.transitionSuccessful;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblToStatus = new javax.swing.JLabel();
        txtToStatus = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtComment = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        dtDate = new org.jdesktop.swingx.JXDatePicker();
        spnTime = new javax.swing.JSpinner();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblFromStatus = new javax.swing.JLabel();
        txtFromStatus = new javax.swing.JTextField();
        btnTransit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        lblToStatus.setText("To State");

        txtToStatus.setEnabled(false);

        txtComment.setColumns(20);
        txtComment.setRows(5);
        jScrollPane1.setViewportView(txtComment);

        jLabel1.setText("Comment on Workflow Change");

        dtDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        spnTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR));

        lblDate.setText("Date");

        lblTime.setText("Time");

        lblFromStatus.setText("From State");

        txtFromStatus.setEnabled(false);

        btnTransit.setText("Transit");
        btnTransit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransitActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFromStatus)
                    .addComponent(lblToStatus)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFromStatus)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .addComponent(txtToStatus))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTime)
                            .addComponent(spnTime, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(btnTransit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFromStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFromStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblToStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtToStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate)
                    .addComponent(lblTime))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTransit)
                    .addComponent(btnCancel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTransitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransitActionPerformed
        // TODO add your handling code here:
        //validate 
        String sComment = this.txtComment.getText();
        if (sComment != null ) {
            if (sComment.trim().length() ==  0 ) {
                NotifyBox.error("Please enter a comment !");
                return;
            }
        } else {
            NotifyBox.error("Please enter a comment !");
        }
        disablePanel();
        TransitionExec exec = new TransitionExec(
                dtDate.getDate(), 
                (Date)spnTime.getValue()
                );
        exec.execute();
    }//GEN-LAST:event_btnTransitActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.parentDialog.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnTransit;
    private org.jdesktop.swingx.JXDatePicker dtDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblFromStatus;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblToStatus;
    private javax.swing.JSpinner spnTime;
    private javax.swing.JTextArea txtComment;
    private javax.swing.JTextField txtFromStatus;
    private javax.swing.JTextField txtToStatus;
    // End of variables declaration//GEN-END:variables
}
