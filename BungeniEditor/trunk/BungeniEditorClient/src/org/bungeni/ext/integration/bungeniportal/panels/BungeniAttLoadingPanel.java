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
package org.bungeni.ext.integration.bungeniportal.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.bungeni.ext.integration.bungeniportal.BungeniAppConnector;
import org.bungeni.ext.integration.bungeniportal.docimpl.BungeniDocument;
import org.bungeni.ext.integration.bungeniportal.BungeniServiceAccess;
import org.bungeni.extutils.DisabledGlassPane;
import org.bungeni.utils.BungeniDialog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniAttLoadingPanel extends javax.swing.JPanel {

    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniAttLoadingPanel.class.getName());
    
    private BungeniDialog parentDialog;
    private BungeniDocument doc;
    private DisabledGlassPane glassPane = new DisabledGlassPane();
    private    File fodfDocument ; 

    ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/bungeni/extpanels/bungeni/Bundle");

    /**
     * Creates new form BungeniAttLoadingPanel
     */
    private BungeniAttLoadingPanel() {
        initComponents();
    }

    public BungeniAttLoadingPanel(BungeniDialog parentDialog, BungeniDocument doc){
        this();
        this.parentDialog = parentDialog;
        this.doc = doc;
    }
    
    public void init(){
        //disablePanel();
        //LoadAttachment ldexec = new LoadAttachment(this.doc);
        //ldexec.execute();
        this.setupFields();
        this.lblInfo.setText(
                "<html><body style='width:350px;'>" + BUNDLE.getString("LOADING_PANEL_HELP_TEXT")
                );
        this.parentDialog.getRootPane().setDefaultButton(this.btnLoadEditAttachment);
    }
    
    public File getOdfDocument(){
        return this.fodfDocument;
    }
    private void disablePanel(){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(BUNDLE.getString("RETRIEVE_DOCS_MESSAGE"));
    }
    
    class LoadAttachment extends SwingWorker<Boolean, BungeniDocument>
    {
        BungeniDocument loadedDocument;
        
        public LoadAttachment(BungeniDocument inputDoc) {
            this.loadedDocument = inputDoc;
        }
        
        @Override
        protected Boolean doInBackground() throws Exception
        {
            BungeniAppConnector.WebResponse wr = 
                    BungeniServiceAccess.getInstance().
                        getAppConnector().getUrl(
                            loadedDocument.getSelectedAttachment().url,
                            false
                    );
            if (wr.getStatusCode() == 200) {
                String responseBody = wr.getResponseBody();
                if (null != responseBody) {
                    Document attDoc  = Jsoup.parse(responseBody);
                    loadedDocument.getSelectedAttachment().parseAttachment(attDoc);
                    System.out.println(loadedDocument.getSelectedAttachment().transitions.size());
                    setupFields();
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }

        @Override
        protected void done()
        {
            try {
                Boolean bState = get();
                if (bState != false ) {
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

    public void setupFields(){
         this.txtDescription.setText(this.doc.getSelectedAttachment().description);
         this.txtFileName.setText(this.doc.getSelectedAttachment().fileName);
         this.txtStatus.setText(this.doc.getSelectedAttachment().status);
         this.txtStatusDate.setText(this.doc.getSelectedAttachment().statusDate);
         this.txtTitle.setText(this.doc.getSelectedAttachment().title);
         
         this.btnLoadEditAttachment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                disablePanel();
                EditAttachment eaExec = new EditAttachment(doc);
                eaExec.execute();
            }
        });
        this.btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parentDialog.dispose();
            }
        });


    }

    
    public BungeniDocument getBungeniDocument(){
        return this.doc;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        txtStatus = new javax.swing.JTextField();
        txtStatusDate = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        lblStatusDate = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        lblDescription = new javax.swing.JLabel();
        btnLoadEditAttachment = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblInfo = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/ext/integration/bungeniportal/panels/Bundle"); // NOI18N
        lblTitle.setText(bundle.getString("BungeniAttLoadingPanel.lblTitle.text")); // NOI18N

        txtTitle.setEditable(false);

        lblFileName.setText(bundle.getString("BungeniAttLoadingPanel.lblFileName.text")); // NOI18N

        txtFileName.setEditable(false);

        txtStatus.setEditable(false);

        txtStatusDate.setEditable(false);

        lblStatus.setText(bundle.getString("BungeniAttLoadingPanel.lblStatus.text")); // NOI18N

        lblStatusDate.setText(bundle.getString("BungeniAttLoadingPanel.lblStatusDate.text")); // NOI18N

        txtDescription.setEditable(false);
        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        lblDescription.setText(bundle.getString("BungeniAttLoadingPanel.lblDescription.text")); // NOI18N

        btnLoadEditAttachment.setText(bundle.getString("BungeniAttLoadingPanel.btnLoadEditAttachment.text")); // NOI18N

        btnCancel.setText(bundle.getString("BungeniAttLoadingPanel.btnCancel.text")); // NOI18N

        lblInfo.setText(bundle.getString("BungeniAttLoadingPanel.lblInfo.text_1")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addComponent(txtFileName, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTitle)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStatusDate)
                                    .addComponent(lblStatusDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFileName)
                                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnLoadEditAttachment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(0, 93, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(lblStatusDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStatusDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDescription)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel)
                    .addComponent(btnLoadEditAttachment)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLoadEditAttachment;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusDate;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtFileName;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtStatusDate;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables

    class EditAttachment extends SwingWorker<File, BungeniDocument>
    {
        BungeniDocument loadedDocument;
        
        
        public EditAttachment(BungeniDocument inputDoc) {
            this.loadedDocument = inputDoc;
            //NotifyBox.info("Loading Document in Editor");
        }
        
        @Override
        protected File doInBackground() throws Exception
        {
            File fodt = null;
            File ftempAtt = BungeniServiceAccess.getInstance().getAppConnector().
                    getDownloadUrl(
                        this.loadedDocument.getSelectedAttachment().downloadUrl,
                        false
                    );
            
            if (ftempAtt != null) {
                 // initialize the file
                 fodt = BungeniServiceAccess.getInstance().checkOdfDocument(ftempAtt, this.loadedDocument);
            }
            return fodt;
        }

        @Override
        protected void done()
        {
            try {
                File fodt = get();
                if (fodt != null ) {
                    fodfDocument = fodt;
                    glassPane.deactivate();
                    //MessageBox.OK(parentDialog, BUNDLE.getString("DOC_OPEN_FOR_EDIT") );
                    parentDialog.dispose();
                }
            } catch (InterruptedException ex) {
               log.error("Error while parsing document ", ex);
            } catch (ExecutionException ex) {
               log.error("Error while parsing document ", ex);
            }
        }
        
    
 }





}
