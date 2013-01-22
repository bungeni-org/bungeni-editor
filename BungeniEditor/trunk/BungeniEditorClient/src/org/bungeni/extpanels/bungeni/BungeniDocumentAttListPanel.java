/*
 *  Copyright (C) 2012 Africa i-Parliaments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/*
 * BungeniDocumentAttListPanel.java
 *
 * Created on Oct 12, 2012, 11:23:07 AM
 */
package org.bungeni.extpanels.bungeni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.bungeni.extpanels.bungeni.BungeniDocument.Attachment;
import org.bungeni.extpanels.bungeni.BungeniListDocuments.BungeniListDocument;
import org.bungeni.extutils.DisabledGlassPane;
import org.bungeni.extutils.MessageBox;
import org.bungeni.utils.BungeniDialog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocumentAttListPanel extends javax.swing.JPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniDocumentAttListPanel.class.getName());
    private BungeniDialog parentDialog = null;
    private String documentURL = null;
    private BungeniListDocument listDoc = null;
    private BungeniDocument doc = null;
    private Attachment selectedAttachment = null;
    private DisabledGlassPane glassPane = new DisabledGlassPane();
   
    /** Creates new form BungeniDocumentAttListPanel */
    public BungeniDocumentAttListPanel(BungeniDialog dlg, BungeniListDocument listDocument, String docURL) {
        this.parentDialog = dlg;
        this.listDoc= listDocument;
        this.documentURL = docURL;
        initComponents();
    }

    public void init(){
        this.infoTitle.setText(this.listDoc.title);
        this.infoStatus.setText(this.listDoc.status);
        disablePanel();
        LoadDocument ldexec = new LoadDocument(this.documentURL);
        ldexec.execute();
    }
    
    private void disablePanel(){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate("Retrieving Document Information from Bungeni ....");
    }
    
    class LoadDocument extends SwingWorker<BungeniDocument, BungeniDocument>
    {
        String urlDocument ;
        BungeniDocument loadedDocument ; 
        
        public LoadDocument(String urlDocument) {
            this.urlDocument = urlDocument;
        }
        
        @Override
        protected BungeniDocument doInBackground() throws Exception
        {
            BungeniAppConnector.WebResponse wr = 
                    BungeniServiceAccess.getInstance().
                        getAppConnector().getUrl(
                            urlDocument,
                            true
                    );
            if (wr.getStatusCode() == 200) {
                String responseBody = wr.getResponseBody();
                if (null != responseBody) {
                    Document doc  = Jsoup.parse(responseBody);
                    BungeniDocument aDocument = new BungeniDocument(urlDocument, doc);
                    return aDocument;
                }
            }
            return null;
        }

        @Override
        protected void done()
        {
            try {
                loadedDocument = get();
                if (loadedDocument != null ) {
                    doc = loadedDocument;
                    setupFields();
                    glassPane.deactivate();
                }
            } catch (InterruptedException ex) {
               log.error("Error while parsing document ", ex);
            } catch (ExecutionException ex) {
               log.error("Error while parsing document ", ex);
            }
        }
}
    
    
    private void setupFields() {
        this.infoStatus.setText(doc.getStatus());
        this.infoTitle.setText(doc.getTitle());
        this.txtDescription.setText(doc.getDescription());
        
        DefaultListModel attModel = new DefaultListModel();
        for (Attachment att : doc.getAttachments()){
            attModel.addElement(att);
        }
        
        this.cboListAttachments.setModel(
                attModel
                );
        if (cboListAttachments.getModel().getSize() == 0) {
            btnImportAttachment.setEnabled(false);
            MessageBox.OK(parentDialog, "There are no attachments to import !");
        } else {
            btnImportAttachment.setEnabled(true);
        }

        this.cboTransitions.setModel(
                new DefaultComboBoxModel(doc.getTransitions().toArray()));

        this.btnImportAttachment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedAttachment = (Attachment) cboListAttachments.getSelectedValue();
                if (null == selectedAttachment) {
                    MessageBox.OK("You need to select an attachment to import it !");
                } else {
                    selectedAttachment.isSelected = true;
                    parentDialog.dispose();
                }
            }
        });
        this.btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parentDialog.dispose();
            }
        });

    }

    public BungeniDocument getDocument(){
        return this.doc;
    }
    
    public Attachment getSelectedAttachment() {
        return this.selectedAttachment;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTransit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cboListAttachments = new javax.swing.JList();
        btnImportAttachment = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblDocInfo = new javax.swing.JLabel();
        infoTitle = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        infoStatus = new javax.swing.JLabel();
        lblDesc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        lblTransit = new javax.swing.JLabel();
        cboTransitions = new javax.swing.JComboBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/extpanels/bungeni/Bundle"); // NOI18N
        btnTransit.setText(bundle.getString("BungeniDocumentAttListPanel.btnTransit.text")); // NOI18N

        cboListAttachments.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(cboListAttachments);

        btnImportAttachment.setText(bundle.getString("BungeniDocumentAttListPanel.btnImportAttachment.text")); // NOI18N

        btnCancel.setText(bundle.getString("BungeniDocumentAttListPanel.btnCancel.text")); // NOI18N

        lblDocInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDocInfo.setText(bundle.getString("BungeniDocumentAttListPanel.lblDocInfo.text")); // NOI18N

        infoTitle.setText(bundle.getString("BungeniDocumentAttListPanel.infoTitle.text")); // NOI18N

        lblTitle.setText(bundle.getString("BungeniDocumentAttListPanel.lblTitle.text")); // NOI18N

        lblStatus.setText(bundle.getString("BungeniDocumentAttListPanel.lblStatus.text")); // NOI18N

        infoStatus.setText(bundle.getString("BungeniDocumentAttListPanel.infoStatus.text")); // NOI18N

        lblDesc.setText(bundle.getString("BungeniDocumentAttListPanel.lblDesc.text")); // NOI18N

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane2.setViewportView(txtDescription);

        jLabel1.setText(bundle.getString("BungeniDocumentAttListPanel.jLabel1.text")); // NOI18N

        lblTransit.setLabelFor(cboTransitions);
        lblTransit.setText(bundle.getString("BungeniDocumentAttListPanel.lblTransit.text")); // NOI18N

        cboTransitions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTransitions.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus)
                            .addComponent(lblTitle)
                            .addComponent(lblDesc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(infoTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(infoStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDocInfo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(btnImportAttachment)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTransit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboTransitions, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(245, 245, 245)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDocInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoTitle)
                    .addComponent(lblTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoStatus)
                    .addComponent(lblStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDesc)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransit)
                    .addComponent(cboTransitions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImportAttachment)
                    .addComponent(btnCancel)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnImportAttachment;
    private javax.swing.JButton btnTransit;
    private javax.swing.JList cboListAttachments;
    private javax.swing.JComboBox cboTransitions;
    private javax.swing.JLabel infoStatus;
    private javax.swing.JLabel infoTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblDocInfo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTransit;
    private javax.swing.JTextArea txtDescription;
    // End of variables declaration//GEN-END:variables
}
