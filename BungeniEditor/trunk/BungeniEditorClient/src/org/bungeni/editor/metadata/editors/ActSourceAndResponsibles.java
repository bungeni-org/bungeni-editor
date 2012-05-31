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

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.metadata.ActSourceAndResponsiblesModel;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author bzuadmin
 */
public class ActSourceAndResponsibles extends BaseEditorDocMetadataDialog{
    
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
   ActSourceAndResponsiblesModel docMetaModel = new ActSourceAndResponsiblesModel();

    /**
     * Creates new customizer ActSourceAndResponsibles
     */
    public ActSourceAndResponsibles() {
         super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
    }

     @Override
    public void initialize() {
        super.initialize();
        this.docMetaModel.setup();
      
        
           if (theMode == SelectorDialogModes.TEXT_EDIT) {
            try {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
  
                String sPublicationName = docMetaModel.getItem("BungeniPublicationName");
                String sPublicationDate = docMetaModel.getItem("BungeniPublicationDate");
                String sPublicationArea = docMetaModel.getItem("BungeniPublicationArea");
                String sSourceType = docMetaModel.getItem("BungeniSourceType");
                String sSourceNo = docMetaModel.getItem("BungeniSourceNo");
                String sPageNo = docMetaModel.getItem("BungeniPageNo");
                
                String sSourceSide = docMetaModel.getItem("BungeniActRelease");
                String sBungeniActReleaseDate = docMetaModel.getItem("BungeniActReleaseDate");
                String sBungeniActDeveloped = docMetaModel.getItem("BungeniActDeveloped");
                String sBungeniActDevelopedDate = docMetaModel.getItem("BungeniActDevelopedDate");
                String sBungeniActApproved = docMetaModel.getItem("BungeniActApproved");
                String sBungeniActAprovedDate = docMetaModel.getItem("BungeniActAprovedDate");
                String sBungeniActImplementation = docMetaModel.getItem("BungeniActImplementation");
                String sBungeniSecondaryActPromulgating = docMetaModel.getItem("BungeniSecondaryActPromulgating");
                String sBungeniActImplementationDate = docMetaModel.getItem("BungeniActImplementationDate");
                String sBungeniSecondaryActPromulgatingDate = docMetaModel.getItem("BungeniSecondaryActPromulgatingDate");
                
                SimpleDateFormat dateFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                
               
                //effective date
               
                if (!CommonStringFunctions.emptyOrNull(sPublicationDate)) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    this.dt_publication_date.setDate(timeFormat.parse(sPublicationDate));
                }
                if (!CommonStringFunctions.emptyOrNull(sPublicationName)) {
                     this.cboPublicationName.setSelectedItem(sPublicationName);
                }
                 if (!CommonStringFunctions.emptyOrNull(sPublicationArea)) {
                    this.txtPublicationArea.setText(sPublicationArea);
                }
               
                if (!CommonStringFunctions.emptyOrNull(sSourceType)) {
                    this.cboSourceType.setSelectedItem(sSourceType);
                }
                if (!CommonStringFunctions.emptyOrNull(sSourceNo)) {
                    this.txtSourceNo.setText(sSourceNo);
                }
                if (!CommonStringFunctions.emptyOrNull(sPageNo)) {
                    this.txtPageNo.setText(sPageNo);
                }
                
                if (!CommonStringFunctions.emptyOrNull(sSourceSide)) 
                    this.txtSourceSide.setText(sSourceSide);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActReleaseDate))
                    this.dt_ActRelease_date.setDate(dateFormat.parse(sBungeniActReleaseDate));
                 
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDeveloped))
                    this.txtActDeveloped.setText(sBungeniActDeveloped);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDevelopedDate)){
                    this.dt_ActDeveloped_date.setDate(dateFormat.parse(sBungeniActDevelopedDate));
                }
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActApproved))
                    this.txtActDeveloped.setText(sBungeniActApproved);
                  
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActAprovedDate)){
                    this.dt_ActApproved_date.setDate(dateFormat.parse(sBungeniActAprovedDate));
                 } 
               
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActImplementation))
                    this.txtActDeveloped.setText(sBungeniActImplementation);
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActImplementationDate))
                    this.dt_ActImplementation_date.setDate(dateFormat.parse(sBungeniActImplementationDate));
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniSecondaryActPromulgating))
                    this.txtActDeveloped.setText(sBungeniSecondaryActPromulgating);
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniSecondaryActPromulgatingDate))
                    this.dt_SecondaryActPromulgating_date.setDate(dateFormat.parse(sBungeniSecondaryActPromulgatingDate));
            
                
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dt_publication_date = new org.jdesktop.swingx.JXDatePicker();
        txtPublicationArea = new javax.swing.JTextField();
        txtSourceNo = new javax.swing.JTextField();
        cboPublicationName = new javax.swing.JComboBox();
        lblPublicationDate = new javax.swing.JLabel();
        lblPublicationName = new javax.swing.JLabel();
        lblPublicationArea = new javax.swing.JLabel();
        lblSourceNo = new javax.swing.JLabel();
        lblSourceType = new javax.swing.JLabel();
        txtPageNo = new javax.swing.JTextField();
        lblPageNo = new javax.swing.JLabel();
        txtSourceSide = new javax.swing.JTextField();
        lblActApprovedDate = new javax.swing.JLabel();
        dt_ActDeveloped_date = new org.jdesktop.swingx.JXDatePicker();
        dt_ActApproved_date = new org.jdesktop.swingx.JXDatePicker();
        lblActDevelopedDate = new javax.swing.JLabel();
        txtActImplementation = new javax.swing.JTextField();
        txtSecondaryActPromulgating = new javax.swing.JTextField();
        lblActReleased = new javax.swing.JLabel();
        dt_ActRelease_date = new org.jdesktop.swingx.JXDatePicker();
        lblActReleaseDate = new javax.swing.JLabel();
        dt_ActImplementation_date = new org.jdesktop.swingx.JXDatePicker();
        lblActImplementationDate = new javax.swing.JLabel();
        dt_SecondaryActPromulgating_date = new org.jdesktop.swingx.JXDatePicker();
        txtActApproved = new javax.swing.JTextField();
        lblSecondaryActPromulgatingDate = new javax.swing.JLabel();
        lblActApproved = new javax.swing.JLabel();
        lblActDeveloped = new javax.swing.JLabel();
        txtActDeveloped = new javax.swing.JTextField();
        lblActImplementation = new javax.swing.JLabel();
        lblSecondaryActPromulgating = new javax.swing.JLabel();
        cboSourceType = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(500, 615));

        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPublicationArea.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPublicationArea.setMinimumSize(new java.awt.Dimension(107, 21));

        txtSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtSourceNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtSourceNo.setMinimumSize(new java.awt.Dimension(107, 21));

        cboPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboPublicationName.setModel(setPublicationNamesModel());
        cboPublicationName.setMaximumSize(new java.awt.Dimension(107, 21));
        cboPublicationName.setMinimumSize(new java.awt.Dimension(107, 21));

        lblPublicationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblPublicationDate.setText(bundle.getString("ActSourceAndResponsibles.lblPublicationDate.text")); // NOI18N

        lblPublicationName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationName.setText(bundle.getString("ActSourceAndResponsibles.lblPublicationName.text")); // NOI18N

        lblPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationArea.setText(bundle.getString("ActSourceAndResponsibles.lblPublicationArea.text")); // NOI18N

        lblSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceNo.setText(bundle.getString("ActSourceAndResponsibles.lblSourceNo.text")); // NOI18N

        lblSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceType.setText(bundle.getString("ActSourceAndResponsibles.lblSourceType.text")); // NOI18N

        txtPageNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPageNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPageNo.setMinimumSize(new java.awt.Dimension(107, 21));

        lblPageNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPageNo.setText(bundle.getString("ActSourceAndResponsibles.lblPageNo.text")); // NOI18N

        txtSourceSide.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActApprovedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApprovedDate.setText(bundle.getString("ActSourceAndResponsibles.lblActApprovedDate.text")); // NOI18N

        dt_ActDeveloped_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        dt_ActApproved_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActDevelopedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDevelopedDate.setText(bundle.getString("ActSourceAndResponsibles.lblActDevelopedDate.text")); // NOI18N

        txtActImplementation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtSecondaryActPromulgating.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActReleased.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleased.setText(bundle.getString("ActSourceAndResponsibles.lblActReleased.text")); // NOI18N

        dt_ActRelease_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActReleaseDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleaseDate.setText(bundle.getString("ActSourceAndResponsibles.lblActReleasedDate.text")); // NOI18N

        dt_ActImplementation_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActImplementationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActImplementationDate.setText(bundle.getString("ActSourceAndResponsibles.lblActImplementationDate.text")); // NOI18N

        dt_SecondaryActPromulgating_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblSecondaryActPromulgatingDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSecondaryActPromulgatingDate.setText(bundle.getString("ActSourceAndResponsibles.lblSecondaryActPromulgatingDate.text")); // NOI18N

        lblActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApproved.setText(bundle.getString("ActSourceAndResponsibles.lblActApproved.text")); // NOI18N

        lblActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDeveloped.setText(bundle.getString("ActSourceAndResponsibles.lblActDeveloped.text")); // NOI18N

        txtActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActImplementation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActImplementation.setText(bundle.getString("ActSourceAndResponsibles.lblActImplementation.text")); // NOI18N

        lblSecondaryActPromulgating.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSecondaryActPromulgating.setText(bundle.getString("ActSourceAndResponsibles.lblSecondaryActPromulgating.text")); // NOI18N

        cboSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboSourceType.setModel(setSourceTypesNamesModel());
        cboSourceType.setMaximumSize(new java.awt.Dimension(107, 21));
        cboSourceType.setMinimumSize(new java.awt.Dimension(107, 21));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSourceNo)
                    .addComponent(lblPublicationName)
                    .addComponent(lblSecondaryActPromulgating)
                    .addComponent(txtSecondaryActPromulgating, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActImplementation)
                    .addComponent(txtActImplementation, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActDeveloped)
                    .addComponent(lblActApproved)
                    .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPublicationName, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSourceSide, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPageNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPublicationArea)
                    .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPublicationDate)
                    .addComponent(dt_publication_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActReleaseDate)
                    .addComponent(lblSecondaryActPromulgatingDate)
                    .addComponent(lblActImplementationDate)
                    .addComponent(dt_ActImplementation_date, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_SecondaryActPromulgating_date, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblActDevelopedDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblActApprovedDate)
                    .addComponent(lblSourceType)
                    .addComponent(cboSourceType, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPublicationName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboPublicationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSourceNo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPageNo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSourceSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(lblActDeveloped)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActApproved)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(lblActImplementation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtActImplementation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSecondaryActPromulgating)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSecondaryActPromulgating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPublicationDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSourceType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPublicationArea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblActReleaseDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblActDevelopedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActApprovedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblActImplementationDate)
                        .addGap(5, 5, 5)
                        .addComponent(dt_ActImplementation_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSecondaryActPromulgatingDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_SecondaryActPromulgating_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(162, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboPublicationName;
    private javax.swing.JComboBox cboSourceType;
    private org.jdesktop.swingx.JXDatePicker dt_ActApproved_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActDeveloped_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActImplementation_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActRelease_date;
    private org.jdesktop.swingx.JXDatePicker dt_SecondaryActPromulgating_date;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private javax.swing.JLabel lblActApproved;
    private javax.swing.JLabel lblActApprovedDate;
    private javax.swing.JLabel lblActDeveloped;
    private javax.swing.JLabel lblActDevelopedDate;
    private javax.swing.JLabel lblActImplementation;
    private javax.swing.JLabel lblActImplementationDate;
    private javax.swing.JLabel lblActReleaseDate;
    private javax.swing.JLabel lblActReleased;
    private javax.swing.JLabel lblPageNo;
    private javax.swing.JLabel lblPublicationArea;
    private javax.swing.JLabel lblPublicationDate;
    private javax.swing.JLabel lblPublicationName;
    private javax.swing.JLabel lblSecondaryActPromulgating;
    private javax.swing.JLabel lblSecondaryActPromulgatingDate;
    private javax.swing.JLabel lblSourceNo;
    private javax.swing.JLabel lblSourceType;
    private javax.swing.JTextField txtActApproved;
    private javax.swing.JTextField txtActDeveloped;
    private javax.swing.JTextField txtActImplementation;
    private javax.swing.JTextField txtPageNo;
    private javax.swing.JTextField txtPublicationArea;
    private javax.swing.JTextField txtSecondaryActPromulgating;
    private javax.swing.JTextField txtSourceNo;
    private javax.swing.JTextField txtSourceSide;
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
          
            //get the official time
    //        SimpleDateFormat tformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
    //        Object timeValue = this.dt_official_time.getValue();
    //        Date hansardTime = (Date) timeValue;
    //        final String strTimeOfHansard = tformatter.format(hansardTime);
            //get the offical date
            SimpleDateFormat dformatter = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                //get the current date
            Calendar cal = Calendar.getInstance();
            String strCurrentDate = dformatter.format(cal.getTime());
                //get the publication date
            String strPubDate = dformatter.format( this.dt_publication_date.getDate());
            String strPubName = (String) this.cboPublicationName.getSelectedItem();
            String strPubArea = this.txtPublicationArea.getText();
            String strSrcNo = this.txtSourceNo.getText();
            String strPageNo = this.txtPageNo.getText();
            String selSrcType = (String) this.cboPublicationName.getSelectedItem();

            
            String strSrcSide = this.txtSourceSide.getText();
            String strReleaseDate = dformatter.format(dt_ActRelease_date.getDate());
            String sBungeniActDeveloped =  this.txtActDeveloped.getText();
            String sBungeniActDevelopedDate = dformatter.format(this.dt_ActDeveloped_date.getDate());
            String sBungeniActApproved =  this.txtActApproved.getText();
            String sBungeniActAprovedDate = dformatter.format(this.dt_ActApproved_date.getDate());
            String sBungeniActImplementation =  this.txtActImplementation.getText();
            String sBungeniActImplementationDate =  dformatter.format(dt_ActImplementation_date.getDate());
            String sBungeniSecondaryActPromulgating =  this.txtSecondaryActPromulgating.getText();
            String sBungeniSecondaryActPromulgatingDate =  dformatter.format(dt_SecondaryActPromulgating_date.getDate());

            docMetaModel.updateItem("BungeniWorkDate", strPubDate);
            docMetaModel.updateItem("BungeniDocAuthor", "Ashok");
            
            // General Metadata
            docMetaModel.updateItem("BungeniPublicationDate", strPubDate);
            docMetaModel.updateItem("BungeniPublicationName", strPubName);
            docMetaModel.updateItem("BungeniPublicationArea", strPubArea);
            docMetaModel.updateItem("BungeniSourceType", selSrcType);
            docMetaModel.updateItem("BungeniSourceNo", strSrcNo);
            docMetaModel.updateItem("BungeniPageNo", strPageNo);
                       
            docMetaModel.updateItem("BungeniActSourceSide", strSrcSide);
            docMetaModel.updateItem("BungeniActReleaseDate", strReleaseDate);
            docMetaModel.updateItem("BungeniActDeveloped",sBungeniActDeveloped);
            docMetaModel.updateItem("BungeniActDevelopedDate",sBungeniActDevelopedDate);
            docMetaModel.updateItem("BungeniActApproved",sBungeniActApproved);
            docMetaModel.updateItem("BungeniActAprovedDate",sBungeniActAprovedDate);
            docMetaModel.updateItem("BungeniActImplementation",sBungeniActImplementation);
            docMetaModel.updateItem("BungeniActImplementationDate",sBungeniActImplementationDate);
            docMetaModel.updateItem("BungeniSecondaryActPromulgating",sBungeniSecondaryActPromulgating);
            docMetaModel.updateItem("BungeniSecondaryActPromulgatingDate",sBungeniSecondaryActPromulgatingDate);

            //other metadata
            docMetaModel.updateItem("BungeniWorkAuthor", "user.Ashok");
            docMetaModel.updateItem("BungeniWorkAuthorURI", "user.Ashok");
            docMetaModel.updateItem("BungeniWorkDateName","workDate");
            //expression
            docMetaModel.updateItem("BungeniExpAuthor", "user.Ashok");
            docMetaModel.updateItem("BungeniExpAuthorURI", "user.Ashok");
            docMetaModel.updateItem("BungeniExpDateName","expDate");
            //manifestation
            docMetaModel.updateItem("BungeniManAuthor", "user.Ashok");
            docMetaModel.updateItem("BungeniManAuthorURI", "user.Ashok");
            docMetaModel.updateItem("BungeniManDateName","manDate");

            spf.setSaveComponent("DocumentType", BungeniEditorPropertiesHelper.getCurrentDocType());
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());
            Date dtHansardDate = dt_publication_date.getDate();
            GregorianCalendar debateCal = new GregorianCalendar(); 
            debateCal.setTime(dtHansardDate);
       
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));
            spf.parseComponents();

            docMetaModel.updateItem("BungeniWorkURI", spf.getWorkURI());
            docMetaModel.updateItem("BungeniExpURI", spf.getExpressionURI());
            docMetaModel.updateItem("BungeniManURI", spf.getExpressionURI() + ".xml");

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
       
    private ComboBoxModel setSourceTypesNamesModel()
    {
         DefaultComboBoxModel sourceTypesNamesModel = null ;
        String [] sourceTypesNames = null; // stores all the bill Names

        // initialise the Bungeni Connector Client
        BungeniConnector client = null;
        
        try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();

                // get the acts from the registry H2 db
                List<SourceType> SourceTypesList = client.getSourceTypes();
                sourceTypesNames = new String[SourceTypesList.size()];

                // loop through extracting the acts
                for (int i = 0 ; i < SourceTypesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    SourceType currSourceType = SourceTypesList.get(i);
                    sourceTypesNames[i] = currSourceType.getNameByLang(Locale.getDefault().getLanguage());
                }
                // create the default acts Names model
                sourceTypesNamesModel = new DefaultComboBoxModel(sourceTypesNames) ;
            } catch (IOException ex) {
                log.error(ex) ;
            }

        return sourceTypesNamesModel;
    }
    
     private ComboBoxModel setPublicationNamesModel()
    {
         DefaultComboBoxModel publicationNamesModel = null ;
        String [] publicationNames = null; // stores all the bill Names

        // initialise the Bungeni Connector Client
        BungeniConnector client = null;
        
        try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();

                // get the acts from the registry H2 db
                List<PublicationName> PublicationNamesList = client.getPublicationNames();
                publicationNames = new String[PublicationNamesList.size()];

                // loop through extracting the acts
                for (int i = 0 ; i < PublicationNamesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    PublicationName currPublicationName = PublicationNamesList.get(i);
                    publicationNames[i] = currPublicationName.getNameByLang(Locale.getDefault().getLanguage());
                }
                // create the default acts Names model
                publicationNamesModel = new DefaultComboBoxModel(publicationNames) ;
            } catch (IOException ex) {
                log.error(ex) ;
            }

        return publicationNamesModel;
    }
      
      @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblPublicationDate.getText().replace("*",""), dt_publication_date);
                put(lblSourceType.getText().replace("*",""), cboPublicationName);
                put(lblActDevelopedDate.getText().replace("*",""), dt_ActDeveloped_date);
                put(lblActApprovedDate.getText().replace("*",""), dt_ActApproved_date);
                put(lblActImplementationDate.getText().replace("*",""), dt_ActImplementation_date);
                put(lblActReleaseDate.getText().replace("*",""), dt_ActRelease_date);
                put(lblSecondaryActPromulgatingDate.getText().replace("*",""), dt_SecondaryActPromulgating_date);
                }
            });
        return super.validateSelectedMetadata(spf);
    }
}
