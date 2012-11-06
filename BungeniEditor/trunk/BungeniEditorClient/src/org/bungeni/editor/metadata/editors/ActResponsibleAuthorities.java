/*
 * Copyright (C) 2012 bzuadmin
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
package org.bungeni.editor.metadata.editors;

import org.bungeni.utils.CommonConnectorFunctions;
import org.bungeni.editor.config.BungeniEditorProperties;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.ActResponsibleAuthoritiesModel;
import org.bungeni.editor.metadata.DateHijri;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author bzuadmin
 */
public class ActResponsibleAuthorities extends BaseEditorDocMetadataDialog {
    
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
     ActResponsibleAuthoritiesModel docMetaModel = new ActResponsibleAuthoritiesModel();
     private final SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
        

    /**
     * Creates new customizer ActResponsibleAuthorities
     */
    public ActResponsibleAuthorities() {
        super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.docMetaModel.setup();
        loadActInfo();
        
           if (theMode == SelectorDialogModes.TEXT_EDIT) {
            try {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
                
                String sBungeniActRelease = docMetaModel.getItem("BungeniActRelease");
                String sBungeniActReleaseDate = docMetaModel.getItem("BungeniActReleaseDate");
                String sBungeniActDeveloped = docMetaModel.getItem("BungeniActDeveloped");
                String sBungeniActDevelopedDate = docMetaModel.getItem("BungeniActDevelopedDate");
                String sBungeniActApproved = docMetaModel.getItem("BungeniActApproved");
                String sBungeniActAprovedDate = docMetaModel.getItem("BungeniActApprovedDate");
                String sBungeniActImplementation = docMetaModel.getItem("BungeniActImplementation");
                String sBungeniSecondaryActPromulgating = docMetaModel.getItem("BungeniSecondaryActPromulgating");
                String sBungeniActImplementationDate = docMetaModel.getItem("BungeniActImplementationDate");
                String sBungeniSecondaryActPromulgatingDate = docMetaModel.getItem("BungeniSecondaryActPromulgatingDate");
                
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActRelease)) 
                    this.cboActRelease.setSelectedItem(sBungeniActRelease);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActReleaseDate))
                    this.dt_ActRelease_date.setDate(dformatter.parse(sBungeniActReleaseDate));
                 
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDeveloped))
                    this.txtActDeveloped.setText(sBungeniActDeveloped);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDevelopedDate)){
                    this.dt_ActDeveloped_date.setDate(dformatter.parse(sBungeniActDevelopedDate));
                }
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActApproved))
                    this.txtActDeveloped.setText(sBungeniActApproved);
                  
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActAprovedDate)){
                    this.dt_ActApproved_date.setDate(dformatter.parse(sBungeniActAprovedDate));
                 } 
                
            } catch (ParseException ex) {
                log.error("initalize()  =  "  + ex.getMessage());
            }
         
        }
    }
     
      protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField) c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

       private void loadActInfo() {
            BungeniConnector client = null ;
            try {
                client = CommonConnectorFunctions.getDSClient();
                List<MetadataInfo> metadata = client.getMetadataInfo();
                if (metadata != null) {                     
                    for (int i = 0; i < metadata.size(); i++) {
                        if (metadata.get(i).getName().equalsIgnoreCase("BungeniActRelease")) {
                            docMetaModel.setBungeniActRelease(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActReleaseDate")) {
                            docMetaModel.setBungeniActReleaseDate(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApproved")) {
                            docMetaModel.setBungeniActApproved(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApprovedDate")) {
                            docMetaModel.setBungeniActApprovedDate(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDeveloped")) {
                            docMetaModel.setBungeniActDeveloped(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDevelopedDate")) {
                            docMetaModel.setBungeniActDevelopedDate(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActImplementation")) {
                            docMetaModel.setBungeniActImplementation(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActImplementationDate")) {
                            docMetaModel.setBungeniActImplementationDate(metadata.get(i).getValue());
                        } 
                        System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                    }
                }
                client.closeConnector();
            }catch (IOException ex) {
                log.error("THe connector client could not be initialized" , ex);
            }
    }

       private ComboBoxModel setActReleaseOrganizationModel()
       {
            DefaultComboBoxModel actOrganizationsModel = null ;
            String[] actOrganizations = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get tactScopeshe acts from the registry H2 db
                List<ActOrganization> actOrganizationsList = client.getActOrganizations();
                actOrganizations = new String[actOrganizationsList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actOrganizationsList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    ActOrganization currActOrganization = actOrganizationsList.get(i);
                    actOrganizations[i] = currActOrganization.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actOrganizationsModel = new DefaultComboBoxModel(actOrganizations) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actOrganizationsModel ;
    }
         
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblActApprovedDate = new javax.swing.JLabel();
        txtActApproved = new javax.swing.JTextField();
        txtActDeveloped = new javax.swing.JTextField();
        lblActDevelopedDate = new javax.swing.JLabel();
        lblActReleased = new javax.swing.JLabel();
        lblActApproved = new javax.swing.JLabel();
        dt_ActRelease_date = new org.jdesktop.swingx.JXDatePicker();
        lblActReleaseDate = new javax.swing.JLabel();
        lblActDeveloped = new javax.swing.JLabel();
        dt_ActDeveloped_date = new org.jdesktop.swingx.JXDatePicker();
        dt_ActApproved_date = new org.jdesktop.swingx.JXDatePicker();
        cboActRelease = new javax.swing.JComboBox();
        dt_ActRelease_dateHijri = new org.jdesktop.swingx.JXDatePicker();
        lblHijriDate = new javax.swing.JLabel();

        lblActApprovedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblActApprovedDate.setText(bundle.getString("ActResponsibleAuthorities.lblActApprovedDate.text")); // NOI18N

        txtActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActDevelopedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDevelopedDate.setText(bundle.getString("ActResponsibleAuthorities.lblActDevelopedDate.text")); // NOI18N

        lblActReleased.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleased.setText(bundle.getString("ActResponsibleAuthorities.lblActReleased.text")); // NOI18N

        lblActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApproved.setText(bundle.getString("ActResponsibleAuthorities.lblActApproved.text")); // NOI18N

        dt_ActRelease_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        dt_ActRelease_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dt_ActRelease_dateActionPerformed(evt);
            }
        });

        lblActReleaseDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleaseDate.setText(bundle.getString("ActResponsibleAuthorities.lblActReleasedDate.text")); // NOI18N

        lblActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDeveloped.setText(bundle.getString("ActResponsibleAuthorities.lblActDeveloped.text")); // NOI18N

        dt_ActDeveloped_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        dt_ActApproved_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        cboActRelease.setModel(setActReleaseOrganizationModel());
        cboActRelease.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboActRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActReleaseActionPerformed(evt);
            }
        });

        dt_ActRelease_dateHijri.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblHijriDate.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboActRelease, 0, 173, Short.MAX_VALUE)
                            .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActReleaseDate))
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtActDeveloped)
                            .addComponent(txtActApproved)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblActDeveloped)
                                    .addComponent(lblActApproved))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_ActRelease_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHijriDate)
                            .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblActDevelopedDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblActApprovedDate))
                        .addGap(29, 29, 29))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActReleaseDate))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboActRelease, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActDeveloped)
                        .addGap(6, 6, 6)
                        .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(lblActApproved)
                        .addGap(6, 6, 6)
                        .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHijriDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActRelease_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblActDevelopedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActApprovedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboActReleaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActReleaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboActReleaseActionPerformed

    private void dt_ActRelease_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dt_ActRelease_dateActionPerformed
        
        Date gReleaseDate = dt_ActRelease_date.getDate();
        String sHijriDate = DateHijri.writeIslamicDate(gReleaseDate);     
        try {
            dt_ActRelease_dateHijri.setDate(dformatter.parse(sHijriDate));
            lblHijriDate.setText(sHijriDate);
        } catch (ParseException ex) {
            log.error(ex);
        }
    }//GEN-LAST:event_dt_ActRelease_dateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboActRelease;
    private org.jdesktop.swingx.JXDatePicker dt_ActApproved_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActDeveloped_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActRelease_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActRelease_dateHijri;
    private javax.swing.JLabel lblActApproved;
    private javax.swing.JLabel lblActApprovedDate;
    private javax.swing.JLabel lblActDeveloped;
    private javax.swing.JLabel lblActDevelopedDate;
    private javax.swing.JLabel lblActReleaseDate;
    private javax.swing.JLabel lblActReleased;
    private javax.swing.JLabel lblHijriDate;
    private javax.swing.JTextField txtActApproved;
    private javax.swing.JTextField txtActDeveloped;
    // End of variables declaration//GEN-END:variables

 @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public Dimension getFrameSize() {
         int DIM_X = 550 ; int DIM_Y = 500 ;
        return new Dimension(DIM_X, DIM_Y);
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf)
    {
             boolean bState = false;
        try {
          
            //get the offical date
            
            String selBungeniActRelease = (String)this.cboActRelease.getSelectedItem();
            String sBungeniActReleaseDate = dformatter.format(dt_ActRelease_date.getDate());
            String sBungeniActDeveloped =  this.txtActDeveloped.getText();
            String sBungeniActDevelopedDate = null;
            if(dt_ActDeveloped_date.getDate() != null)
                sBungeniActDevelopedDate = dformatter.format(dt_ActDeveloped_date.getDate());
            
            String sBungeniActApproved =  this.txtActApproved.getText();
            String sBungeniActAprovedDate = null;
            if(dt_ActApproved_date.getDate()!= null)
                sBungeniActAprovedDate = dformatter.format(dt_ActApproved_date.getDate());
            
           
            docMetaModel.updateItem("BungeniActRelease", selBungeniActRelease);
            docMetaModel.updateItem("BungeniActReleaseDate", sBungeniActReleaseDate);
            docMetaModel.updateItem("BungeniActDeveloped",sBungeniActDeveloped);
            docMetaModel.updateItem("BungeniActDevelopedDate",sBungeniActDevelopedDate);
            docMetaModel.updateItem("BungeniActApproved",sBungeniActApproved);
            docMetaModel.updateItem("BungeniActAprovedDate",sBungeniActAprovedDate);

            //docMetaModel.updateItem("__BungeniDocMeta", "true");
            docMetaModel.saveModel(ooDocument);
            bState = true;
        } catch (Exception ex) {
            log.error("applySelectedMetadata : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }
    }
       
   
      @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblActReleased.getText().replace("*",""), cboActRelease);
                put(lblActReleaseDate.getText().replace("*",""), dt_ActRelease_date);
                }
            });
        return super.validateSelectedMetadata(spf);
    }
}
