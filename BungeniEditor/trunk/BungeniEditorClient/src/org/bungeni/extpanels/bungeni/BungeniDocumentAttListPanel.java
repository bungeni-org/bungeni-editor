/*
 *  Copyright (C) 2012 PC
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.bungeni.editor.panels.impl.IMainContainerPanel;
import org.bungeni.editor.panels.impl.ITabbedPanel;

import org.bungeni.extpanels.bungeni.BungeniDocument.Attachment;
import org.bungeni.translators.utility.runtime.TempFileManager;
import org.bungeni.utils.BungeniDialog;
import org.bungeni.utils.CommonEditorInterfaceFunctions;
import org.jsoup.Jsoup;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocumentAttListPanel extends javax.swing.JPanel {

    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniDocumentAttListPanel.class.getName());
    private BungeniDialog parentDialog = null;
    private BungeniDocument doc = null;
    private Attachment selectedAttachment = null;
    private final DefaultHttpClient client;

    /** Creates new form BungeniDocumentAttListPanel */
    public BungeniDocumentAttListPanel(DefaultHttpClient client, BungeniDialog dlg, BungeniDocument doc) {
        this.parentDialog = dlg;
        this.doc = doc;
        this.client = client;
        initComponents();
    }

    public void init() {
        this.infoStatus.setText(doc.getStatus());
        this.infoTitle.setText(doc.getTitle());
        this.txtDescription.setText(doc.getDescription());

        this.cboListAttachments.setModel(
                new DefaultComboBoxModel(doc.getAttachments().toArray()));
        this.cboTransitions.setModel(
                new DefaultComboBoxModel(doc.getTransitions().toArray()));

        this.btnImportAttachment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedAttachment = (Attachment) cboListAttachments.getSelectedValue();
                parentDialog.dispose();
            }
        });
        this.btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parentDialog.dispose();
            }
        });

    }

    public Attachment getSelectedAttachment() {
        return this.selectedAttachment;
    }

    private void importAttachment() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String sAttURL = selectedAttachment.url;
                HttpGet hget = new HttpGet(sAttURL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = "";
                try {
                    responseBody = client.execute(hget, responseHandler);
                } catch (IOException ex) {
                    log.error("Error getting response body", ex);
                }
                //parse the attachment body
                final org.jsoup.nodes.Document attsoupdoc = Jsoup.parse(responseBody);
                final BungeniAttachment attDoc = new BungeniAttachment(sAttURL, attsoupdoc);
                HashMap objMap = new HashMap() {

                    {
                        put("MAIN_DOC", doc);
                        put("ATT_DOC", attDoc);
                    }
                };
                IMainContainerPanel panel = CommonEditorInterfaceFunctions.getMainPanel();
                for (ITabbedPanel ipanel : panel.getTabbedPanels()) {
                    ipanel.setCustomObjectMap(objMap);
                }
                File fattFile = importAttachmentFile(doc, attDoc);
                
            }
        });
    }

    private File importAttachmentFile(BungeniDocument doc, BungeniAttachment attDoc) {
        //download the file --
        File fdownTemp = null;
        String binaryFileUrl = attDoc.getDownloadURL();
        String binaryFileName = attDoc.getAttachmentName();
        HttpGet fileGet = new HttpGet(binaryFileUrl);
        InputStream input = null;
        OutputStream output = null;
        try {
            HttpResponse downloadResponse = client.execute(fileGet);
            byte[] buffer = new byte[1024];
            String filenameRandom = RandomStringUtils.randomAlphanumeric(14);
            input = downloadResponse.getEntity().getContent();
            fdownTemp = TempFileManager.createTempFile(filenameRandom, ".odt");
            output = new FileOutputStream(fdownTemp);
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            log.error("Error while attempting to download attachmebt");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                log.error("Error while closing streams");
            }
            return fdownTemp;
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

        btnTransit.setText("Transit");

        cboListAttachments.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(cboListAttachments);

        btnImportAttachment.setText("Import Attachment");
        btnImportAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportAttachmentActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");

        lblDocInfo.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblDocInfo.setText("Document Information");

        infoTitle.setText("Kenya Information and Communications Bill  - [received by clerk]");

        lblTitle.setText("Title:");

        lblStatus.setText("Status: ");

        infoStatus.setText("Kenya Information and Communications Bill  - [received by clerk]");

        lblDesc.setText("Desc:");

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane2.setViewportView(txtDescription);

        jLabel1.setText("Attachments");

        lblTransit.setLabelFor(cboTransitions);
        lblTransit.setText("Possible Transitions : ");

        cboTransitions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTransitions.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDocInfo)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus)
                            .addComponent(lblTitle)
                            .addComponent(lblDesc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(infoTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(infoStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(btnImportAttachment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTransit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboTransitions, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(253, 253, 253)))
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

    private void btnImportAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportAttachmentActionPerformed
        // TODO add your handling code here:
        //import the attachemnt here
        importAttachment();
    }//GEN-LAST:event_btnImportAttachmentActionPerformed
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
