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
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.ActMetadataModel;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
/**
 *
 * @author bzuadmin
 */
public class ActMetadata extends BaseEditorDocMetadataDialog {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ActMetadata.class.getName());
    private ActMetadataModel docMetaModel = new ActMetadataModel();
    private XPath xpathInstance = null;

    
    private HashMap<String,org.jdom.Document> cachedActNames = new HashMap<String,org.jdom.Document>();


    /**
     * Creates new customizer ActMetadata
     */
    public ActMetadata() {
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
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
           }
           
           try{
             
                String sBungeniActType = docMetaModel.getItem("BungeniActType");
                String sEffectiveDate =docMetaModel.getItem("BungeniActEffectiveDate");
                String sSourceSide = docMetaModel.getItem("BungeniActSourceSide");
                String sBungeniActDeveloped = docMetaModel.getItem("BungeniActDeveloped");
                String sBungeniActDevelopedDate = docMetaModel.getItem("BungeniActDevelopedDate");
                String sBungeniActApproved = docMetaModel.getItem("BungeniActApproved");
                String sBungeniActAprovedDate = docMetaModel.getItem("BungeniActAprovedDate");
                String sBungeniActPromulgated = docMetaModel.getItem("BungeniActPromulgated");
                String sBungeniActPromulgatedDate = docMetaModel.getItem("BungeniActPromulgatedDate");
                String sBungeniActImplementation = docMetaModel.getItem("BungeniActImplementation");
                String sBungeniSecondaryActPromulgating = docMetaModel.getItem("BungeniSecondaryActPromulgating");
                
                SimpleDateFormat timeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                   
                 
                if (!CommonStringFunctions.emptyOrNull(sBungeniActType))
                    this.cboActType.setSelectedItem(sBungeniActType);
               
                 if (!CommonStringFunctions.emptyOrNull(sEffectiveDate)) {
                    SimpleDateFormat formatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
                    this.dt_effective_date.setDate(formatter.parse(sEffectiveDate));
                }
                 
                if (!CommonStringFunctions.emptyOrNull(sSourceSide)) {
                    this.txtSourceSide.setText(sSourceSide);
                }
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDeveloped))
                    this.txtActDeveloped.setText(sBungeniActDeveloped);
                
                if (!CommonStringFunctions.emptyOrNull(sBungeniActDevelopedDate)){
                    this.dt_ActDeveloped_date.setDate(timeFormat.parse(sBungeniActDevelopedDate));
                }
                 
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActApproved))
                    this.txtActDeveloped.setText(sBungeniActApproved);
                  
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActAprovedDate)){
                    this.dt_ActApproved_date.setDate(timeFormat.parse(sBungeniActAprovedDate));
                 }
                   
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActPromulgated))
                    this.txtActDeveloped.setText(sBungeniActPromulgated);
                    
                 if (!CommonStringFunctions.emptyOrNull(sBungeniActImplementation))
                    this.txtActDeveloped.setText(sBungeniActImplementation);
                  
                 if (!CommonStringFunctions.emptyOrNull(sBungeniSecondaryActPromulgating))
                    this.txtActDeveloped.setText(sBungeniSecondaryActPromulgating);
               
           }catch (Exception ex) {
            log.error("initalize()  =  " + ex.getMessage());
           }
    }

    public Component getPanelComponent() {
        return this;
    }
    
        private void loadActInfo() {
            BungeniConnector client = null ;
            try {
                client = CommonConnectorFunctions.getDSClient();
                List<MetadataInfo> metadata = client.getMetadataInfo();
                if (metadata != null) {                     
                    for (int i = 0; i < metadata.size(); i++) {
                        if (metadata.get(i).getName().equalsIgnoreCase("BungeniActType")) {
                            docMetaModel.setBungeniActType(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActEffectiveDate")) {
                            docMetaModel.setBungeniActEffectiveDate(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActSourceSide")) {
                            docMetaModel.setBungeniActSourceSide(metadata.get(i).getValue());
                        } else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDeveloped")) {
                            docMetaModel.setBungeniActDeveloped(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActDevelopedDate")) {
                            docMetaModel.setBungeniActDevelopedDate(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActApproved")) {
                            docMetaModel.setBungeniActApproved(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActAprovedDate")) {
                            docMetaModel.setBungeniActAprovedDate(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActPromulgated")) {
                            docMetaModel.setBungeniActPromulgated(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActPromulgatedDate")) {
                            docMetaModel.setBungeniActPromulgatedDate(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniActImplementation")) {
                            docMetaModel.setBungeniActImplementation(metadata.get(i).getValue());
                        }else if (metadata.get(i).getName().equalsIgnoreCase("BungeniSecondaryActPromulgating")) {
                            docMetaModel.setBungeniSecondaryActPromulgating(metadata.get(i).getValue());
                        }
                        System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType() + " " + metadata.get(i).getValue());
                    }
                }
                client.closeConnector();
            }catch (IOException ex) {
                log.error("THe connector client could not be initialized" , ex);
            }
    }

       
       private ComboBoxModel setActTypesModel()
       {
            DefaultComboBoxModel actsNamesModel = null ;
            String[] actsNames = null ; // stores all the bill Names
            // initialise the Bungeni Connector Client
            BungeniConnector client = null ;
            try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();
                // get the acts from the registry H2 db
                List<Act> actsList = client.getActs();
                actsNames = new String[actsList.size()];
                
                // loop through extracting the acts
                for (int i = 0 ; i < actsList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    Act currAct = actsList.get(i);
                    actsNames[i] = currAct.getNameByLang(Locale.getDefault().getLanguage());
                }
                
                // create the default acts Names model
                actsNamesModel = new DefaultComboBoxModel(actsNames) ;

            } catch (IOException ex) {
                log.error(ex) ;
            }
            return actsNamesModel ;
    }
          
    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf){
    boolean bState = false;
    try {
        SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
            
        String strEffectiveDate = dformatter.format(dt_effective_date.getDate());
           
        String selBungeniActType = (String)this.cboActType.getSelectedItem();
        String strSrcSide = this.txtSourceSide.getText();
           
        String sBungeniActDeveloped =  this.txtActDeveloped.getText();
        String sBungeniActDevelopedDate = dformatter.format(this.dt_ActDeveloped_date.getDate());
        String sBungeniActApproved =  this.txtActApproved.getText();
        String sBungeniActAprovedDate = dformatter.format(this.dt_ActApproved_date.getDate());
        String sBungeniActImplementation =  this.txtActImplementation.getText();
        String sBungeniSecondaryActPromulgating =  this.txtSecondaryActPromulgating.getText();
    
        docMetaModel.updateItem("BungeniActType",selBungeniActType);
        docMetaModel.updateItem("BungeniActEffectiveDate", strEffectiveDate);   
        docMetaModel.updateItem("BungeniActSourceSide", strSrcSide);
        docMetaModel.updateItem("BungeniActDeveloped",sBungeniActDeveloped);
        docMetaModel.updateItem("BungeniActDevelopedDate",sBungeniActDevelopedDate);
        docMetaModel.updateItem("BungeniActApproved",sBungeniActApproved);
        docMetaModel.updateItem("BungeniActAprovedDate",sBungeniActAprovedDate);
        docMetaModel.updateItem("BungeniActImplementation",sBungeniActImplementation);
        docMetaModel.updateItem("BungeniSecondaryActPromulgating",sBungeniSecondaryActPromulgating);
        docMetaModel.saveModel(ooDocument);
        bState = true;
    } catch (Exception ex) {
        log.error("applySelectedMetadata : " + ex.getMessage());
        bState = false;
    } finally {
        return bState;
    }
}    

     /**
     * This mehod is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboActType = new javax.swing.JComboBox();
        lblActType = new javax.swing.JLabel();
        txtActDeveloped = new javax.swing.JTextField();
        lblActDeveloped = new javax.swing.JLabel();
        lblActApproved = new javax.swing.JLabel();
        lblSecondaryActPromulgating = new javax.swing.JLabel();
        lblActImplementation = new javax.swing.JLabel();
        txtActApproved = new javax.swing.JTextField();
        txtActImplementation = new javax.swing.JTextField();
        txtSecondaryActPromulgating = new javax.swing.JTextField();
        dt_ActApproved_date = new org.jdesktop.swingx.JXDatePicker();
        lblActDevelopedDate = new javax.swing.JLabel();
        lblActApprovedDate = new javax.swing.JLabel();
        dt_ActDeveloped_date = new org.jdesktop.swingx.JXDatePicker();
        txtSourceSide = new javax.swing.JTextField();
        lblEffectiveDate = new javax.swing.JLabel();
        dt_effective_date = new org.jdesktop.swingx.JXDatePicker();
        lblSourceSide = new javax.swing.JLabel();

        cboActType.setModel(setActTypesModel());
        cboActType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActTypeActionPerformed(evt);
            }
        });

        lblActType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblActType.setText(bundle.getString("ActMetadata.lblActType.text")); // NOI18N

        txtActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActDeveloped.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDeveloped.setText(bundle.getString("ActMetadata.lblActDeveloped.text")); // NOI18N

        lblActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApproved.setText(bundle.getString("ActMetadata.lblActApproved.text")); // NOI18N

        lblSecondaryActPromulgating.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSecondaryActPromulgating.setText(bundle.getString("ActMetadata.lblSecondaryActPromulgating.text")); // NOI18N

        lblActImplementation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActImplementation.setText(bundle.getString("ActMetadata.lblActImplementation.text")); // NOI18N

        txtActApproved.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtActImplementation.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtSecondaryActPromulgating.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        dt_ActApproved_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblActDevelopedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActDevelopedDate.setText(bundle.getString("ActMetadata.lblActDevelopedDate.text")); // NOI18N

        lblActApprovedDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblActApprovedDate.setText(bundle.getString("ActMetadata.lblActApprovedDate.text")); // NOI18N

        dt_ActDeveloped_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        txtSourceSide.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblEffectiveDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblEffectiveDate.setText(bundle.getString("ActMetadata.lblEffectiveDate.text")); // NOI18N
        lblEffectiveDate.setName("lbl.BungeniLanguageID"); // NOI18N

        dt_effective_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblSourceSide.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceSide.setText(bundle.getString("GeneralMetadata.lblSourceSide.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActDeveloped)
                            .addComponent(lblActApproved)
                            .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActApprovedDate)
                            .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActDevelopedDate)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSourceSide, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                .addComponent(lblSourceSide, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblActType)
                            .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSecondaryActPromulgating)
                            .addComponent(lblActImplementation)
                            .addComponent(txtSecondaryActPromulgating, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActImplementation, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dt_effective_date, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblActType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboActType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblEffectiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_effective_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSourceSide, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSourceSide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActDeveloped)
                    .addComponent(lblActDevelopedDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtActDeveloped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActDeveloped_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblActApproved)
                    .addComponent(lblActApprovedDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtActApproved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ActApproved_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblActImplementation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtActImplementation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSecondaryActPromulgating)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSecondaryActPromulgating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

     @Override
    public Dimension getFrameSize() {
        int DIM_X = 450 ; int DIM_Y = 350 ;
        return new Dimension(DIM_X, DIM_Y);
    }

    
    @Override
     public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblEffectiveDate.getText().replace("*",""), dt_effective_date);
                put(lblActDevelopedDate.getText().replace("*",""), dt_ActDeveloped_date);
                put(lblActApprovedDate.getText().replace("*",""), dt_ActApproved_date);
                
            }
         });
        return super.validateSelectedMetadata(spf);
     }
                                                
    private void cboActTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActTypeActionPerformed
        JComboBox cb = (JComboBox) evt.getSource();

        // get the selected item and extract id for Act
        // from vector
        final String selectedAct = (String) cb.getSelectedItem();

        if (null != selectedAct) {
            // get the Act & set it to the Act #
            // textfield
            SwingWorker setActName = new SwingWorker() {

                @Override
                protected String doInBackground() throws Exception {
                    String ActId = null;

                    BungeniConnector client = null;

                    // initialize the data store client
                    client = CommonConnectorFunctions.getDSClient();

                    // get the Acts from the registry H2 db
                    List<Act> ActsList = client.getActs();

                    // search for the ActNo
                    for (Act Act : ActsList) {
                        for(Name name: (List<Name>)Act.getNames()){
                            if (selectedAct.equals(name)) {
                                ActId = Act.getId().toString();
                                return ActId;
                            } 
                        }
                       
                    }

                    return ActId;
                }

                public void done() {
                    try {
                        // set the Act No
                        txtActDeveloped.setText((String) get());
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    } catch (ExecutionException ex) {
                        log.error(ex);
                    }
                }
            };

            setActName.execute();
        }
    }//GEN-LAST:event_cboActTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboActType;
    private org.jdesktop.swingx.JXDatePicker dt_ActApproved_date;
    private org.jdesktop.swingx.JXDatePicker dt_ActDeveloped_date;
    private org.jdesktop.swingx.JXDatePicker dt_effective_date;
    private javax.swing.JLabel lblActApproved;
    private javax.swing.JLabel lblActApprovedDate;
    private javax.swing.JLabel lblActDeveloped;
    private javax.swing.JLabel lblActDevelopedDate;
    private javax.swing.JLabel lblActImplementation;
    private javax.swing.JLabel lblActType;
    private javax.swing.JLabel lblEffectiveDate;
    private javax.swing.JLabel lblSecondaryActPromulgating;
    private javax.swing.JLabel lblSourceSide;
    private javax.swing.JTextField txtActApproved;
    private javax.swing.JTextField txtActDeveloped;
    private javax.swing.JTextField txtActImplementation;
    private javax.swing.JTextField txtSecondaryActPromulgating;
    private javax.swing.JTextField txtSourceSide;
    // End of variables declaration//GEN-END:variables
}
