/*
 * Copyright (C) 2012 Africa i-Parliaments
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.ext.integration.bungeniportal.BungeniServiceAccess;
import org.bungeni.ext.integration.bungeniportal.LoginInfo;
import org.bungeni.extutils.DisabledGlassPane;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.NotifyBox;

/**
 * Login screen for Bungeni
 *
 * @author Ashok HAriharan
 */
public class BungeniLoginPanel extends javax.swing.JPanel {

     private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniLoginPanel.class.getName());
   
    
    JDialog parentDialog;
    BungeniServiceAccess bungeniService;
    private LoginInfo loginInfo;
    private boolean bConnection = false;
    private DisabledGlassPane glassPane = new DisabledGlassPane();
    ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/bungeni/ext/integration/bungeniportal/Bundle");

    /**
     * Creates new form BungeniLogin
     */
    public BungeniLoginPanel(JDialog parentDialog, LoginInfo loginInfo) {
        initComponents();
        this.loginInfo = loginInfo;
        this.parentDialog = parentDialog;
        bungeniService = BungeniServiceAccess.getInstance();
        init();
    }

    private void init() {
        this.txtLoginBase.setText(loginInfo.loginBase);
        this.txtServer.setText(loginInfo.server);
        this.txtServerPort.setText(loginInfo.port);
        this.parentDialog.getRootPane().setDefaultButton(this.btnLogin);
    }

    private void closeDialog() {
        this.parentDialog.dispose();
    }

    public boolean loginSuccessful() {
        return bConnection;
    }

    private void disablePanel() {
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(BUNDLE.getString("LOGGING_IN"));
    }

    
    class LoginStatus {
        String message ; 
        Boolean state ; 
        
        public LoginStatus(Boolean b, String s) {
            this.message = s;
            this.state = b;
        }
    }
    
    class LoginWorker extends SwingWorker<LoginStatus, Boolean> {


        public LoginWorker() {
        }

        @Override
        protected LoginStatus doInBackground() throws Exception {
            DefaultHttpClient client = null;
            bConnection = false;
            String failureMessage;
            try {
                bConnection = true;
                client = bungeniService.login(
                        txtServer.getText(),
                        txtServerPort.getText(),
                        txtLoginBase.getText(),
                        txtUser.getText(),
                        txtPassword.getText(),
                        loginInfo.oauthCredentials
                        );
                failureMessage = "login successful";
            } catch (UnsupportedEncodingException ex) {
                bConnection = false;
                failureMessage = ex.getMessage();
            } catch (IOException ex) {
                bConnection = false;
                failureMessage = ex.getMessage();
            } catch (Exception ex) {
                bConnection = false;
                failureMessage = ex.getMessage();
            }
            return new LoginStatus(bConnection, failureMessage);
        }

        @Override
        protected void done() {
            try {
                LoginStatus loginState = get();
                glassPane.deactivate();
                if (loginState.state == false) {
                     NotifyBox.error("Login to Bungeni Failed ! : \n" + loginState.message);
                } else {
                     closeDialog();
                }
            } catch (InterruptedException ex) {
                log.error("Error while login ", ex);
            } catch (ExecutionException ex) {
                log.error("Error while login  ", ex);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUser = new javax.swing.JTextField();
        lblUser = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        lblLoginLabel = new javax.swing.JLabel();
        lblPassword1 = new javax.swing.JLabel();
        txtLoginBase = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        lblServerName = new javax.swing.JLabel();
        txtServer = new javax.swing.JTextField();
        txtServerPort = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();

        txtUser.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/ext/integration/bungeniportal/panels/Bundle"); // NOI18N
        lblUser.setText(bundle.getString("BungeniLoginPanel.lblUser.text")); // NOI18N

        lblPassword.setText(bundle.getString("BungeniLoginPanel.lblPassword.text")); // NOI18N

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lblLoginLabel.setText(bundle.getString("BungeniLoginPanel.lblLoginLabel.text")); // NOI18N

        lblPassword1.setText(bundle.getString("BungeniLoginPanel.lblPassword1.text")); // NOI18N

        txtLoginBase.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        btnLogin.setText(bundle.getString("BungeniLoginPanel.btnLogin.text")); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblServerName.setText(bundle.getString("BungeniLoginPanel.lblServerName.text")); // NOI18N

        txtServer.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        txtServerPort.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lblPort.setText(bundle.getString("BungeniLoginPanel.lblPort.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblServerName)
                    .addComponent(txtServer, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPort)
                    .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUser)
                            .addComponent(lblUser)
                            .addComponent(lblPassword)
                            .addComponent(txtPassword)
                            .addComponent(lblLoginLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(lblPassword1)
                            .addComponent(txtLoginBase)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblServerName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLoginBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        disablePanel();
        LoginWorker lwExec = new LoginWorker();
        lwExec.execute();
 
    }//GEN-LAST:event_btnLoginActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblLoginLabel;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPassword1;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblServerName;
    private javax.swing.JLabel lblUser;
    private javax.swing.JTextField txtLoginBase;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtServer;
    private javax.swing.JTextField txtServerPort;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
