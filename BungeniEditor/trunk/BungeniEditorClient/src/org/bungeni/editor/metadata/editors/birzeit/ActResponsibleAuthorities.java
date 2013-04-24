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
package org.bungeni.editor.metadata.editors.birzeit;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.metadata.ActResponsibleAuthoritiesModel;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.birzeit.DateHijri;
import org.bungeni.editor.metadata.birzeit.PromulagtedLeg;
import org.bungeni.editor.metadata.editors.GeneralMetadata;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author bzuadmin
 */
public class ActResponsibleAuthorities extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
    private static ActResponsibleAuthoritiesModel docMetaModel = new ActResponsibleAuthoritiesModel();
    private final SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
   // private final SimpleDateFormat dbformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("dataBaseDateFormat"));
    private ArrayList<PromulagtedLeg> ReleaseOrganizationsList = new ArrayList<PromulagtedLeg>();
    private String dbName = "Muqtafi_test";
    private Connection con = ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;
    /**
     * Creates new customizer ActResponsibleAuthorities
     */
    public ActResponsibleAuthorities() {
        super();
          try {
            conStmt = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(ActSource.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                String sBungeniActReleaseDateHijri = docMetaModel.getItem("BungeniActReleaseDateHijri");

                String sBungeniActDeveloped = docMetaModel.getItem("BungeniActDeveloped");
                String sBungeniActDevelopedDate = docMetaModel.getItem("BungeniActDevelopedDate");
                String sBungeniActApproved = docMetaModel.getItem("BungeniActApproved");
                String sBungeniActAprovedDate = docMetaModel.getItem("BungeniActApprovedDate");


                if (!CommonStringFunctions.emptyOrNull(sBungeniActRelease)) {
                    this.cboActRelease.setSelectedItem(sBungeniActRelease);
                }

                if (!CommonStringFunctions.emptyOrNull(sBungeniActReleaseDate)) {
                    this.dt_ActRelease_date.setDate(dformatter.parse(sBungeniActReleaseDate));
                }


                if (!CommonStringFunctions.emptyOrNull(sBungeniActReleaseDateHijri)) {
                    this.dt_ActRelease_dateHijri.setDate(dformatter.parse(sBungeniActReleaseDateHijri));
                }

                if (!CommonStringFunctions.emptyOrNull(sBungeniActDeveloped)) {
                    this.txtActDeveloped.setText(sBungeniActDeveloped);
                }

                if (!CommonStringFunctions.emptyOrNull(sBungeniActDevelopedDate)) {
                    this.dt_ActDeveloped_date.setDate(dformatter.parse(sBungeniActDevelopedDate));
                }

                if (!CommonStringFunctions.emptyOrNull(sBungeniActApproved)) {
                    this.txtActApproved.setText(sBungeniActApproved);
                }

                if (!CommonStringFunctions.emptyOrNull(sBungeniActAprovedDate)) {
                    this.dt_ActApproved_date.setDate(dformatter.parse(sBungeniActAprovedDate));
                }

            } catch (ParseException ex) {
                log.error("initalize()  =  " + ex.getMessage());
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

     public static ActResponsibleAuthoritiesModel getDocMetaModel(){
        return docMetaModel;
    }
     
    private void loadActInfo() {
        BungeniConnector client = null;
        try {
            client = CommonConnectorFunctions.getDSClient();
            List<MetadataInfo> metadata = client.getMetadataInfo();
            if (metadata != null) {
                for (int i = 0; i < metadata.size(); i++) {
                    if (metadata.get(i).getName().equalsIgnoreCase("BungeniActRelease")) {
                        docMetaModel.setBungeniActRelease(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActReleaseDate")) {
                        docMetaModel.setBungeniActReleaseDate(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActReleaseDateHijri")) {
                        docMetaModel.setBungeniActReleaseDateHijri(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApproved")) {
                        docMetaModel.setBungeniActApproved(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApprovedDate")) {
                        docMetaModel.setBungeniActApprovedDate(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDeveloped")) {
                        docMetaModel.setBungeniActDeveloped(metadata.get(i).getValue());
                    } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDevelopedDate")) {
                        docMetaModel.setBungeniActDevelopedDate(metadata.get(i).getValue());
                    }
                    System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                }
            }
            client.closeConnector();
        } catch (IOException ex) {
            log.error("THe connector client could not be initialized", ex);
        }
    }

    private ComboBoxModel setActReleaseOrganizationModel() {

        DefaultComboBoxModel releaseOrganizationsModel = null;

        try {
            String sqlStm = "SELECT [LG_Body_ID], [LG_Body_Name], [LG_Body_Name_E] FROM [LG_Body] WHERE LG_Body_LG_BodyType_ID=3";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                PromulagtedLeg plObj = new PromulagtedLeg(rs.getString(1), rs.getString(2), rs.getString(3));
                ReleaseOrganizationsList.add(plObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] releaseOrganizations = new String[ReleaseOrganizationsList.size()];
        for (int i = 0; i < ReleaseOrganizationsList.size(); i++) {
            releaseOrganizations[i] = ReleaseOrganizationsList.get(i).toString();  
            if(releaseOrganizations[i].length() > 35 ){
                releaseOrganizations[i] = ReleaseOrganizationsList.get(i).toString().substring(0, 35) + " ...";
            }
        }
        // create the default acts Names mode
        releaseOrganizationsModel = new DefaultComboBoxModel(releaseOrganizations);


        return releaseOrganizationsModel;
//        DefaultComboBoxModel actOrganizationsModel = null;
//        String[] actOrganizations = null; // stores all the bill Names
//        // initialise the Bungeni Connector Client
//        BungeniConnector client = null;
//        try {
//            // initialize the data store client
//            client = CommonConnectorFunctions.getDSClient();
//            // get tactScopeshe acts from the registry H2 db
//            List<ActOrganization> actOrganizationsList = client.getActOrganizations();
//            actOrganizations = new String[actOrganizationsList.size()];
//
//            // loop through extracting the acts
//            for (int i = 0; i < actOrganizationsList.size(); i++) {
//                // get the current act & extract the act Name
//                ActOrganization currActOrganization = actOrganizationsList.get(i);
//                actOrganizations[i] = currActOrganization.getNameByLang(Locale.getDefault().getLanguage());
//            }
//
//            // create the default acts Names model
//            actOrganizationsModel = new DefaultComboBoxModel(actOrganizations);
//
//        } catch (IOException ex) {
//            log.error(ex);
//        }
//        return actOrganizationsModel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblActApproved = new javax.swing.JLabel();
        dt_ActRelease_date = new org.jdesktop.swingx.JXDatePicker();
        lblActDevelopedDate = new javax.swing.JLabel();
        lblActReleased = new javax.swing.JLabel();
        dt_ActDeveloped_date = new org.jdesktop.swingx.JXDatePicker();
        dt_ActApproved_date = new org.jdesktop.swingx.JXDatePicker();
        lblActReleaseDate = new javax.swing.JLabel();
        lblActDeveloped = new javax.swing.JLabel();
        lblActApprovedDate = new javax.swing.JLabel();
        txtActDeveloped = new javax.swing.JTextField();
        txtActApproved = new javax.swing.JTextField();
        lblActReleaseDateHijri = new javax.swing.JLabel();
        dt_ActRelease_dateHijri = new org.jdesktop.swingx.JXDatePicker();
        cboActRelease = new javax.swing.JComboBox();

        lblActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblActApproved.setText(bundle.getString("ActResponsibleAuthorities.lblActApproved.text")); // NOI18N

        dt_ActRelease_date.setFormats("yyyy-MM-dd");
        dt_ActRelease_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        dt_ActRelease_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dt_ActRelease_dateActionPerformed(evt);
            }
        });
        dt_ActRelease_date.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                dt_ActRelease_dateInputMethodTextChanged(evt);
            }
        });

        lblActDevelopedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDevelopedDate.setText(bundle.getString("ActResponsibleAuthorities.lblActDevelopedDate.text")); // NOI18N

        lblActReleased.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleased.setText(bundle.getString("ActResponsibleAuthorities.lblActReleased.text")); // NOI18N

        dt_ActDeveloped_date.setFormats("yyyy-MM-dd");
        dt_ActDeveloped_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        dt_ActApproved_date.setFormats("yyyy-MM-dd");
        dt_ActApproved_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActReleaseDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleaseDate.setText(bundle.getString("ActResponsibleAuthorities.lblActReleasedDate.text")); // NOI18N

        lblActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDeveloped.setText(bundle.getString("ActResponsibleAuthorities.lblActDeveloped.text")); // NOI18N

        lblActApprovedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApprovedDate.setText(bundle.getString("ActResponsibleAuthorities.lblActApprovedDate.text")); // NOI18N

        txtActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActReleaseDateHijri.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActReleaseDateHijri.setText(bundle.getString("ActResponsibleAuthorities.lblActReleasedDateHijri.text")); // NOI18N

        dt_ActRelease_dateHijri.setFormats("yyyy-MM-dd");
        dt_ActRelease_dateHijri.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        cboActRelease.setModel(setActReleaseOrganizationModel());
        cboActRelease.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtActDeveloped, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtActApproved, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboActRelease, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblActDeveloped, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblActApproved, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGap(0, 124, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dt_ActRelease_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblActDevelopedDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblActApprovedDate)
                    .addComponent(lblActReleaseDateHijri)
                    .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblActReleaseDate))
                .addGap(47, 47, 47))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActReleaseDate)
                        .addGap(8, 8, 8)
                        .addComponent(dt_ActRelease_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblActReleaseDateHijri)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActRelease_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblActDevelopedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActApprovedDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblActReleased, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(cboActRelease, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblActDeveloped)
                        .addGap(6, 6, 6)
                        .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(lblActApproved)
                        .addGap(6, 6, 6)
                        .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dt_ActRelease_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dt_ActRelease_dateActionPerformed

        Date gReleaseDate = dt_ActRelease_date.getDate();
        String sHijriDate = DateHijri.writeIslamicDate(gReleaseDate);
        try {
            dt_ActRelease_dateHijri.setDate(dformatter.parse(sHijriDate));
        } catch (ParseException ex) {
            log.error(ex);
        }

    }//GEN-LAST:event_dt_ActRelease_dateActionPerformed

    private void dt_ActRelease_dateInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_dt_ActRelease_dateInputMethodTextChanged
        // TODO add your handling code here:
         Date gReleaseDate = dt_ActRelease_date.getDate();
        String sHijriDate = DateHijri.writeIslamicDate(gReleaseDate);
        try {
            dt_ActRelease_dateHijri.setDate(dformatter.parse(sHijriDate));
        } catch (ParseException ex) {
            log.error(ex);
        }
    }//GEN-LAST:event_dt_ActRelease_dateInputMethodTextChanged

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
    private javax.swing.JLabel lblActReleaseDateHijri;
    private javax.swing.JLabel lblActReleased;
    private javax.swing.JTextField txtActApproved;
    private javax.swing.JTextField txtActDeveloped;
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getPanelComponent() {
        return this;
    }

    @Override
    public Dimension getFrameSize() {
        int DIM_X = 550;
        int DIM_Y = 500;
        return new Dimension(DIM_X, DIM_Y);
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        try {

            //get the offical date

            PromulagtedLeg promulagtedLeg = ReleaseOrganizationsList.get(this.cboActRelease.getSelectedIndex());
            
            String sBungeniActReleaseDate = dformatter.format(dt_ActRelease_date.getDate());
            String sBungeniActReleaseDateHijri = dformatter.format(dt_ActRelease_dateHijri.getDate());
            String sBungeniActDeveloped = this.txtActDeveloped.getText();
            String sBungeniActDevelopedDate = null;
            if (dt_ActDeveloped_date.getDate() != null) {
                sBungeniActDevelopedDate = dformatter.format(dt_ActDeveloped_date.getDate());
            }

            String sBungeniActApproved = this.txtActApproved.getText();
            String sBungeniActAprovedDate = null;
            if (dt_ActApproved_date.getDate() != null) {
                sBungeniActAprovedDate = dformatter.format(dt_ActApproved_date.getDate());
            }


            docMetaModel.updateItem("BungeniActRelease", promulagtedLeg.toString());
            docMetaModel.updateItem("BungeniActReleaseID", promulagtedLeg.getPromulagtedLegID());
            docMetaModel.updateItem("BungeniActReleaseDate", sBungeniActReleaseDate);
            docMetaModel.updateItem("BungeniActReleaseDateHijri", sBungeniActReleaseDateHijri);
            docMetaModel.updateItem("BungeniActDeveloped", sBungeniActDeveloped);
            docMetaModel.updateItem("BungeniActDevelopedDate", sBungeniActDevelopedDate);
            docMetaModel.updateItem("BungeniActApproved", sBungeniActApproved);
            docMetaModel.updateItem("BungeniActApprovedDate", sBungeniActAprovedDate);

            //other metadata
            docMetaModel.updateItem("BungeniWorkAuthor", "pna");
            docMetaModel.updateItem("BungeniWorkAuthorAs", "author");
            docMetaModel.updateItem("BungeniWorkAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniWorkDateName", "Issuing");
            docMetaModel.updateItem("BungeniWorkDate", sBungeniActReleaseDate);

            docMetaModel.updateItem("BungeniDocPart", "main");

            //expression
            docMetaModel.updateItem("BungeniExpAuthor", "pna");
            docMetaModel.updateItem("BungeniExpAuthorAs", "editor");
            docMetaModel.updateItem("BungeniExpAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniExpDateName", "Issuing");
            docMetaModel.updateItem("BungeniExpDate", sBungeniActReleaseDate);
            //manifestation
            docMetaModel.updateItem("BungeniManAuthor", "IoL");
            docMetaModel.updateItem("BungeniManAuthorAs", "publisher");
            docMetaModel.updateItem("BungeniManAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniManDateName", "Generation");
            docMetaModel.updateItem("BungeniManDate", sBungeniActReleaseDate);

            spf.setSaveComponent("BungeniActReleaseDate", sBungeniActReleaseDate);
            spf.parseComponents();
            docMetaModel.updateItem("BungeniWorkURI", spf.getWorkURI());
            docMetaModel.updateItem("BungeniExpURI", spf.getExpressionURI());
            docMetaModel.updateItem("BungeniManURI", spf.getExpressionURI() + ".akn");


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
        addFieldsToValidate(new TreeMap<String, Component>() {
            {
                put(lblActReleased.getText().replace("*", ""), cboActRelease);
                put(lblActReleaseDate.getText().replace("*", ""), dt_ActRelease_date);
            }
        });
        return super.validateSelectedMetadata(spf);
    }

//    public void getDBValues(ArrayList values) {
//        String selBungeniActRelease = String.valueOf(this.cboActRelease.getSelectedIndex()+1);
//        String sBungeniActReleaseDate = dbformatter.format(dt_ActRelease_date.getDate());
//        String sBungeniActReleaseDateHijri = dbformatter.format(dt_ActRelease_dateHijri.getDate());
//        String sBungeniActDeveloped = this.txtActDeveloped.getText();
//        String sBungeniActDevelopedDate = "";
//        if (dt_ActDeveloped_date.getDate() != null) {
//            sBungeniActDevelopedDate = dbformatter.format(dt_ActDeveloped_date.getDate());
//        }
//        String sBungeniActApproved = this.txtActApproved.getText();
//        String sBungeniActAprovedDate = "";
//        if (dt_ActApproved_date.getDate() != null) {
//            sBungeniActAprovedDate = dbformatter.format(dt_ActApproved_date.getDate());
//        }
//
//        values.add(selBungeniActRelease);
//        values.add(sBungeniActReleaseDate);
//        values.add(sBungeniActReleaseDateHijri);
//        values.add(sBungeniActDeveloped);
//        values.add(sBungeniActDevelopedDate);
//        values.add(sBungeniActApproved);
//        values.add(sBungeniActAprovedDate);
//       
//    }
}
