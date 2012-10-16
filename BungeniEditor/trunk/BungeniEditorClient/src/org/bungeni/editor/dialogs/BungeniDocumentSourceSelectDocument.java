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
 * BungeniDocumentSourceSelectDocument.java
 *
 * Created on Oct 5, 2012, 5:27:30 PM
 */

package org.bungeni.editor.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.editor.dialogs.BungeniDocumentSource.BungeniDocuments;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocumentSourceSelectDocument extends javax.swing.JPanel {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniDocumentSourceSelectDocument.class.getName());



    private BungeniDocumentSource docSource = null;
    private BungeniDocuments selectedDocument = null;
    private JDialog parentDialog = null;
    /** Creates new form BungeniDocumentSourceSelectDocument */
    public BungeniDocumentSourceSelectDocument(JDialog parent) {
        initComponents();
        this.parentDialog = parent;
    }

    public void init(){

        this.docSource = new BungeniDocumentSource(
                "10.0.2.2",
                "8081",
                "login",
                "clerk.p1_01",
                "member"
                );
        this.cboSelectDocuments.setModel(
                new DefaultComboBoxModel(docSource.getBungeniDocuments().toArray())
                );
        this.btnSelectDocuments.addActionListener(new btnSelectDocumentsListener());
        this.btnCancel.addActionListener(new btnCancelListener());
    }

    class btnCancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            parentDialog.dispose();
        }
    }
    class btnSelectDocumentsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
                BungeniDocuments selectedDoc = (BungeniDocuments) cboSelectDocuments.getSelectedItem();
                selectedDocument = selectedDoc;
                parentDialog.dispose();
                //openDocumentFromBungeni(docsource);
        }

    }

    public BungeniDocuments getSelectedDocument() {
        return this.selectedDocument;
    }


    class OpenDocumentWorker extends SwingWorker<String,Object> {

        BungeniDocuments bungeniDoc ;

        public OpenDocumentWorker(BungeniDocuments adoc){
            bungeniDoc = adoc;
        }

        @Override
        protected String doInBackground() throws Exception {
            String responseBody = "";
            if (docSource != null ) {
                DefaultHttpClient client = docSource.login();
                final HttpGet geturl = new HttpGet(bungeniDoc.url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseBody = client.execute(geturl, responseHandler);
            }
            return responseBody;
        }

        @Override
        public void done(){
            String sBody = "";
            try {
               sBody = get();
            } catch (InterruptedException ex) {
                log.error("Thread was interrupted", ex);
            } catch (ExecutionException ex) {
                log.error("Thread was interrupted", ex);
            }
            JOptionPane.showMessageDialog(null, sBody);
        }
    }

    private void openDocumentFromBungeni(BungeniDocuments docsource) throws IOException {
                DefaultHttpClient client = docSource.login();
                final HttpGet geturl = new HttpGet(docsource.url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = client.execute(geturl, responseHandler);
                docSource.client.getConnectionManager().shutdown();
                docSource.client = null;
                JOptionPane.showConfirmDialog(null, responseBody.length());

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSelectDocuments = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cboSelectDocuments = new javax.swing.JComboBox();

        btnSelectDocuments.setText("Select Document");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cboSelectDocuments.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboSelectDocuments, 0, 362, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnSelectDocuments)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboSelectDocuments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelectDocuments)
                    .addComponent(btnCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelectDocuments;
    private javax.swing.JComboBox cboSelectDocuments;
    // End of variables declaration//GEN-END:variables

}
