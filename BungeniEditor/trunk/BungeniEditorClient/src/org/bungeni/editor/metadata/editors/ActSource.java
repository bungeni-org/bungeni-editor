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
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import java.awt.Component;
import java.awt.Dimension;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.editor.metadata.ActSourceModel;
import org.bungeni.editor.metadata.BaseEditorDocMetadataDialog;
import org.bungeni.editor.metadata.LanguageCode;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.extutils.*;
import org.bungeni.utils.BungeniFileSavePathFormat;

import org.w3c.dom.Document;  
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author bzuadmin
 */
public class ActSource extends BaseEditorDocMetadataDialog{
    
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
   ActSourceModel docMetaModel = new ActSourceModel();

    /**
     * Creates new customizer ActSource
     */
    public ActSource() {
         super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
    }

     @Override
    public void initialize() {
        super.initialize();
        this.docMetaModel.setup();
             
        try {
 
		File fXmlFile = new File("./ActSourceMetadata.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
 
		NodeList nList = doc.getElementsByTagName("entry");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
 
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
		      Element eElement = (Element) nNode;
                      docMetaModel.updateItem(getTagValue("key", eElement), getTagValue("value", eElement));
		  
		   }
		}
	  } catch (Exception e) {
		e.printStackTrace();
	  }
        
        
            try {
                //retrieve metadata... and set in controls....
                docMetaModel.loadModel(ooDocument);
  
                String sSrcName = docMetaModel.getItem("BungeniSrcName");
                String sSrcPublicationDate = docMetaModel.getItem("BungeniSrcPublicationDate");
                String sPublicationArea = docMetaModel.getItem("BungeniPublicationArea");
                String sSourceType = docMetaModel.getItem("BungeniSourceType");
                String sSourceNo = docMetaModel.getItem("BungeniSourceNo");
                
                SimpleDateFormat dateFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
          
                
                if (!CommonStringFunctions.emptyOrNull(sSrcPublicationDate)) {
                    this.dt_publication_date.setDate(dateFormat.parse(sSrcPublicationDate));
                    ActMainMetadata.setBungeniActEffectiveDate(this.dt_publication_date.getDate());
                }
                if (!CommonStringFunctions.emptyOrNull(sSrcName)) {
                     this.cboSrcName.setSelectedItem(sSrcName);
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
              
                
            } catch (ParseException ex) {
                log.error("initalize()  =  "  + ex.getMessage());
            }
    }
      private static String getTagValue(String sTag, Element eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue != null)
            return nValue.getNodeValue();
        return null;
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
        cboSrcName = new javax.swing.JComboBox();
        lblPublicationDate = new javax.swing.JLabel();
        lblSrcName = new javax.swing.JLabel();
        lblPublicationArea = new javax.swing.JLabel();
        lblSourceNo = new javax.swing.JLabel();
        lblSourceType = new javax.swing.JLabel();
        cboSourceType = new javax.swing.JComboBox();

        setPreferredSize(new java.awt.Dimension(500, 400));

        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        dt_publication_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dt_publication_dateActionPerformed(evt);
            }
        });

        txtPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPublicationArea.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPublicationArea.setMinimumSize(new java.awt.Dimension(107, 21));

        txtSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtSourceNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtSourceNo.setMinimumSize(new java.awt.Dimension(107, 21));

        cboSrcName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboSrcName.setModel(setSrcNamesModel());
        cboSrcName.setMaximumSize(new java.awt.Dimension(107, 21));
        cboSrcName.setMinimumSize(new java.awt.Dimension(107, 21));

        lblPublicationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        lblPublicationDate.setText(bundle.getString("ActSource.lblPublicationDate.text")); // NOI18N

        lblSrcName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSrcName.setText(bundle.getString("ActSource.lblSrcName.text")); // NOI18N

        lblPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationArea.setText(bundle.getString("ActSource.lblPublicationArea.text")); // NOI18N

        lblSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceNo.setText(bundle.getString("ActSource.lblSourceNo.text")); // NOI18N

        lblSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceType.setText(bundle.getString("ActSource.lblSourceType.text")); // NOI18N

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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPublicationDate)
                            .addComponent(lblSrcName))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboSrcName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSourceNo)
                                    .addComponent(dt_publication_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPublicationArea)
                                    .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSourceType)
                                    .addComponent(cboSourceType, 0, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblSrcName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboSrcName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblPublicationDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSourceNo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblSourceType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPublicationArea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(227, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dt_publication_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dt_publication_dateActionPerformed
        ActMainMetadata.setBungeniActEffectiveDate(dt_publication_date.getDate());
    }//GEN-LAST:event_dt_publication_dateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboSourceType;
    private javax.swing.JComboBox cboSrcName;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private javax.swing.JLabel lblPublicationArea;
    private javax.swing.JLabel lblPublicationDate;
    private javax.swing.JLabel lblSourceNo;
    private javax.swing.JLabel lblSourceType;
    private javax.swing.JLabel lblSrcName;
    private javax.swing.JTextField txtPublicationArea;
    private javax.swing.JTextField txtSourceNo;
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
                //get the publication date
            String strPubDate = dformatter.format( this.dt_publication_date.getDate());
            String strPubName = (String) this.cboSrcName.getSelectedItem();
            String strPubArea = this.txtPublicationArea.getText();
            String strSrcNo = this.txtSourceNo.getText();
            String selSrcType = (String) this.cboSrcName.getSelectedItem();

            docMetaModel.updateItem("BungeniWorkDate", strPubDate);
            docMetaModel.updateItem("BungeniDocAuthor", "Samar");
            
            // General Metadata
            docMetaModel.updateItem("BungeniSrcPublicationDate", strPubDate);
            docMetaModel.updateItem("BungeniSrcName", strPubName);
            docMetaModel.updateItem("BungeniPublicationArea", strPubArea);
            docMetaModel.updateItem("BungeniSourceType", selSrcType);
            docMetaModel.updateItem("BungeniSourceNo", strSrcNo);
                      
            //other metadata
            docMetaModel.updateItem("BungeniWorkAuthor", "user.Samar");
            docMetaModel.updateItem("BungeniWorkAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniWorkDateName","workDate");
            docMetaModel.updateItem("BungeniWorkDate", "2012-01-01");
            //expression
            docMetaModel.updateItem("BungeniExpAuthor", "user.Samar");
            docMetaModel.updateItem("BungeniExpAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniExpDateName","expDate");
            docMetaModel.updateItem("BungeniExpDate", "2012-01-01");
            //manifestation
            docMetaModel.updateItem("BungeniManAuthor", "user.Samar");
            docMetaModel.updateItem("BungeniManAuthorURI", "user.Samar");
            docMetaModel.updateItem("BungeniManDateName","manDate");
            docMetaModel.updateItem("BungeniManDate", "2012-01-01");

            spf.setSaveComponent("DocumentType", BungeniEditorPropertiesHelper.getCurrentDocType());
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());
            
            Calendar cal = Calendar.getInstance();
            Date dtHansardDate = cal.getTime();
            GregorianCalendar debateCal = new GregorianCalendar(); 
            debateCal.setTime(dtHansardDate);
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH) + dtHansardDate.getSeconds());
            spf.parseComponents();

            docMetaModel.updateItem("BungeniWorkURI", spf.getWorkURI());
            docMetaModel.updateItem("BungeniExpURI", spf.getExpressionURI());
            docMetaModel.updateItem("BungeniManURI", spf.getExpressionURI() + ".xml");

            docMetaModel.saveModel(ooDocument);
            
            DocumentBuilder builder = null;  
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
            Document document = builder.newDocument();

            Element root = document.createElement("ActSourceMetadata");  
            for(String key : docMetaModel.getMetaNames()){  

                String value = docMetaModel.getItem(key);  

                Element newNode = document.createElement("entry");  
                Element newKey = document.createElement("key");  
                Element newValue = document.createElement("value");  

                newKey.setTextContent(key);  
                newValue.setTextContent(value);  

                newNode.appendChild(newKey);  
                newNode.appendChild(newValue);  

                root.appendChild(newNode);  

            }  
            document.appendChild(root); 

            Transformer transformer = TransformerFactory.newInstance().newTransformer();  
            Source source = new DOMSource(document);
            
            
            File file = new File("./ActSourceMetadata.xml"); 
            
            Result result = new StreamResult(file);  
            transformer.transform(source,result); 
            
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
    
     private ComboBoxModel setSrcNamesModel()
    {
         DefaultComboBoxModel srcNamesModel = null ;
        String [] srcNames = null; // stores all the bill Names

        // initialise the Bungeni Connector Client
        BungeniConnector client = null;
        
        try {
                // initialize the data store client
                 client = CommonConnectorFunctions.getDSClient();

                // get the acts from the registry H2 db
                List<SrcName> SrcNamesList = client.getSrcNames();
                srcNames = new String[SrcNamesList.size()];

                // loop through extracting the acts
                for (int i = 0 ; i < SrcNamesList.size() ; i ++)
                {
                    // get the current act & extract the act Name
                    SrcName currSrcName = SrcNamesList.get(i);
                    srcNames[i] = currSrcName.getNameByLang(Locale.getDefault().getLanguage());
                }
                // create the default acts Names model
                srcNamesModel = new DefaultComboBoxModel(srcNames) ;
            } catch (IOException ex) {
                log.error(ex) ;
            }

        return srcNamesModel;
    }
      
      @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         addFieldsToValidate (new TreeMap<String,Component>(){
            {
                put(lblPublicationDate.getText().replace("*",""), dt_publication_date);
                put(lblSourceType.getText().replace("*",""), cboSourceType);
                put(lblPublicationArea.getText().replace("*",""), txtPublicationArea);
                put(lblSourceNo.getText().replace("*",""), txtSourceNo);
                put(lblSrcName.getText().replace("*",""), cboSrcName);
            }
     });
        return super.validateSelectedMetadata(spf);
    }

}
