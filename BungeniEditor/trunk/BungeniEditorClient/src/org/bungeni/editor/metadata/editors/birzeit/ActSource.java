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

import org.bungeni.editor.metadata.birzeit.DateHijri;
import org.bungeni.editor.metadata.birzeit.PublicationSrc;
import java.awt.Component;
import java.awt.Dimension;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
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
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.connectorutils.CommonConnectorFunctions;
import org.bungeni.editor.metadata.*;
import org.bungeni.editor.metadata.editors.birzeit.ActMainMetadata;
import org.bungeni.editor.metadata.editors.GeneralMetadata;
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
public class ActSource extends BaseEditorDocMetadataDialog {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GeneralMetadata.class.getName());
    private static ActSourceModel docMetaModel = new ActSourceModel();
    private ArrayList<PublicationSrc> SrcNamesList = new ArrayList<PublicationSrc>();
    private final SimpleDateFormat dformatter = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
    private String dbName = "Muqtafi_test";
    private Connection con =  ConnectorFunctions.ConnectMMSM(dbName);
    private Statement conStmt;

    /**
     * Creates new customizer ActSource
     */
    public ActSource() {
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
        docMetaModel.setup();

        try {

            File fXmlFile = new File("ActSourceMetadata.xml");
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

        //   if (theMode == SelectorDialogModes.TEXT_EDIT) {
        try {
            //retrieve metadata... and set in controls....
            docMetaModel.loadModel(ooDocument);

            String sBungeniPublicationName = docMetaModel.getItem("BungeniPublicationSrcName");
            String sSrcPublicationDate = docMetaModel.getItem("BungeniPublicationDate");
            String sSrcPublicationDateHijri = docMetaModel.getItem("BungeniPublicationDateHijri");
            String sPublicationArea = docMetaModel.getItem("BungeniPublicationArea");
            String sSourceType = docMetaModel.getItem("BungeniSourceType");
            String sSourceNo = docMetaModel.getItem("BungeniSourceNo");


            if (!CommonStringFunctions.emptyOrNull(sSrcPublicationDate)) {
                this.dt_publication_date.setDate(dformatter.parse(sSrcPublicationDate));
                ActMainMetadata.setBungeniActEffectiveDate(dformatter.parse(sSrcPublicationDate));
            }

            if (!CommonStringFunctions.emptyOrNull(sSrcPublicationDateHijri)) {
                this.dt_publication_dateHijri.setDate(dformatter.parse(sSrcPublicationDateHijri));
            }

            if (!CommonStringFunctions.emptyOrNull(sBungeniPublicationName)) {
                this.cboSrcName.setSelectedItem(sBungeniPublicationName);
            }

            if (!CommonStringFunctions.emptyOrNull(sPublicationArea)) {
                this.txtPublicationArea.setText(sPublicationArea);
            }

            if (sSourceType.equalsIgnoreCase("true")) {
                this.jcbScrExcIssue.setSelected(true);
            } else {
                this.jcbScrExcIssue.setSelected(false);
            }

            if (!CommonStringFunctions.emptyOrNull(sSourceNo)) {
                this.txtSourceNo.setText(sSourceNo);
            }


        } catch (ParseException ex) {
            log.error("initalize()  =  " + ex.getMessage());
        }
        //    }
    }

    public static ActSourceModel getDocMetaModel() {
        return docMetaModel;
    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue != null) {
            return nValue.getNodeValue();
        }
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

        jcbScrExcIssue = new javax.swing.JCheckBox();
        lblPublicationDateHijri = new javax.swing.JLabel();
        dt_publication_dateHijri = new org.jdesktop.swingx.JXDatePicker();
        lblSourceType = new javax.swing.JLabel();
        lblSourceNo = new javax.swing.JLabel();
        lblPublicationArea = new javax.swing.JLabel();
        lblSrcName = new javax.swing.JLabel();
        lblPublicationDate = new javax.swing.JLabel();
        cboSrcName = new javax.swing.JComboBox();
        dt_publication_date = new org.jdesktop.swingx.JXDatePicker();
        txtPublicationArea = new javax.swing.JTextField();
        txtSourceNo = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(500, 400));

        jcbScrExcIssue.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/metadata/editors/Bundle"); // NOI18N
        jcbScrExcIssue.setText(bundle.getString("ActSource.jcbSrcExcIssue.text")); // NOI18N

        lblPublicationDateHijri.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationDateHijri.setText(bundle.getString("ActSource.lblPublicationDateHijri.text")); // NOI18N

        dt_publication_dateHijri.setFormats("yyyy-MM-dd");
        dt_publication_dateHijri.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        lblSourceType.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceType.setText(bundle.getString("ActSource.lblSourceType.text")); // NOI18N

        lblSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSourceNo.setText(bundle.getString("ActSource.lblSourceNo.text")); // NOI18N

        lblPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationArea.setText(bundle.getString("ActSource.lblPublicationArea.text")); // NOI18N

        lblSrcName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblSrcName.setText(bundle.getString("ActSource.lblSrcName.text")); // NOI18N

        lblPublicationDate.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        lblPublicationDate.setText(bundle.getString("ActSource.lblPublicationDate.text")); // NOI18N

        cboSrcName.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cboSrcName.setModel(setSrcNamesModel());
        cboSrcName.setMaximumSize(new java.awt.Dimension(107, 21));
        cboSrcName.setMinimumSize(new java.awt.Dimension(107, 21));

        dt_publication_date.setFormats("yyyy-MM-dd");
        dt_publication_date.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        dt_publication_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dt_publication_dateActionPerformed(evt);
            }
        });
        dt_publication_date.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                dt_publication_dateInputMethodTextChanged(evt);
            }
        });

        txtPublicationArea.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtPublicationArea.setMaximumSize(new java.awt.Dimension(107, 21));
        txtPublicationArea.setMinimumSize(new java.awt.Dimension(107, 21));

        txtSourceNo.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        txtSourceNo.setMaximumSize(new java.awt.Dimension(107, 21));
        txtSourceNo.setMinimumSize(new java.awt.Dimension(107, 21));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSourceNo)
                    .addComponent(lblSrcName)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cboSrcName, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPublicationDate)
                                .addComponent(dt_publication_date, javax.swing.GroupLayout.DEFAULT_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dt_publication_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblPublicationDateHijri))
                            .addGap(35, 35, 35)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPublicationArea)
                                .addComponent(lblSourceType)
                                .addComponent(jcbScrExcIssue)
                                .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(167, Short.MAX_VALUE))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPublicationDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPublicationDateHijri)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dt_publication_dateHijri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSourceType)
                        .addGap(5, 5, 5)
                        .addComponent(jcbScrExcIssue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPublicationArea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPublicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSourceNo)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txtSourceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(171, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dt_publication_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dt_publication_dateActionPerformed
        ActMainMetadata.setBungeniActEffectiveDate(dt_publication_date.getDate());

        java.util.Date gPublictionDate = dt_publication_date.getDate();
        String sHijriDate = DateHijri.writeIslamicDate(gPublictionDate);
        try {
            dt_publication_dateHijri.setDate(dformatter.parse(sHijriDate));
        } catch (ParseException ex) {
            log.error(ex);
        }
    }//GEN-LAST:event_dt_publication_dateActionPerformed

    private void dt_publication_dateInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_dt_publication_dateInputMethodTextChanged
        // TODO add your handling code here:
        ActMainMetadata.setBungeniActEffectiveDate(dt_publication_date.getDate());

        java.util.Date gPublictionDate = dt_publication_date.getDate();
        String sHijriDate = DateHijri.writeIslamicDate(gPublictionDate);
        try {
            dt_publication_dateHijri.setDate(dformatter.parse(sHijriDate));
        } catch (ParseException ex) {
            log.error(ex);
        }
        
    }//GEN-LAST:event_dt_publication_dateInputMethodTextChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboSrcName;
    private org.jdesktop.swingx.JXDatePicker dt_publication_date;
    private org.jdesktop.swingx.JXDatePicker dt_publication_dateHijri;
    private javax.swing.JCheckBox jcbScrExcIssue;
    private javax.swing.JLabel lblPublicationArea;
    private javax.swing.JLabel lblPublicationDate;
    private javax.swing.JLabel lblPublicationDateHijri;
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
        int DIM_X = 550;
        int DIM_Y = 500;
        return new Dimension(DIM_X, DIM_Y);
    }

    public boolean applySelectedMetadata(BungeniFileSavePathFormat spf) {
        boolean bState = false;
        try {
            String strPubDate = dformatter.format(dt_publication_date.getDate());
            String sPubDateHijri = dformatter.format(dt_publication_dateHijri.getDate());

            PublicationSrc SrcName = SrcNamesList.get(this.cboSrcName.getSelectedIndex());

            String strPubArea = this.txtPublicationArea.getText();
            String strSrcNo = this.txtSourceNo.getText();
            String bSrcType;
            if (jcbScrExcIssue.isSelected()) {
                bSrcType = "True";
            } else {
                bSrcType = "False";
            }

//            docMetaModel.updateItem("BungeniWorkDate", strPubDate);
//            docMetaModel.updateItem("BungeniDocAuthor", "Samar");

            // General Metadata
            docMetaModel.updateItem("BungeniPublicationDate", strPubDate);
            docMetaModel.updateItem("BungeniPublicationDateHijri", sPubDateHijri);
            docMetaModel.updateItem("BungeniPublicationSrcName", SrcName.getPublicationSrcName());
            docMetaModel.updateItem("BungeniPublicationSrcNameID", SrcName.getPublicationSrcID());
            docMetaModel.updateItem("BungeniPublicationSrcName_AN", SrcName.getPublicationSrcName_AN());

            docMetaModel.updateItem("BungeniPublicationArea", strPubArea);
            docMetaModel.updateItem("BungeniSourceType", bSrcType);
            docMetaModel.updateItem("BungeniSourceNo", strSrcNo);

            String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
            if (docType.equalsIgnoreCase("act")) {
                spf.setSaveComponent("DocumentType", "leg");
            } else {
                spf.setSaveComponent("DocumentType", docType);
            }
            spf.setSaveComponent("CountryCode", Locale.getDefault().getCountry());

            Calendar cal = Calendar.getInstance();
            java.util.Date dtHansardDate = cal.getTime();
            GregorianCalendar debateCal = new GregorianCalendar();
            debateCal.setTime(dtHansardDate);
            spf.setSaveComponent("Year", debateCal.get(Calendar.YEAR));
            spf.setSaveComponent("Month", debateCal.get(Calendar.MONTH) + 1);
            spf.setSaveComponent("Day", debateCal.get(Calendar.DAY_OF_MONTH));

            spf.setSaveComponent("LanguageCode", Locale.getDefault().getLanguage());


            spf.parseComponents();

            docMetaModel.saveModel(ooDocument);

            DocumentBuilder builder = null;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("ActSourceMetadata");
            for (String key : docMetaModel.getMetaNames()) {

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


            File file = new File("ActSourceMetadata.xml");

            Result result = new StreamResult(file);
            transformer.transform(source, result);

            bState = true;
        } catch (Exception ex) {
            log.error("applySelectedMetadata : " + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }
    }

    private ComboBoxModel setSrcNamesModel() {
        DefaultComboBoxModel srcNamesModel = null;

        try {

            String colLabel = null;
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                colLabel = "LG_Src_Name";
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                colLabel = "LG_Src_Name_E";
            }
            String sqlStm = "SELECT [LG_Src_ID], [" + colLabel + "], [LG_Src_Name_AN] FROM LG_Src";
            ResultSet rs = conStmt.executeQuery(sqlStm);

            while (rs.next()) {
                PublicationSrc psObj = new PublicationSrc(rs.getString(1), rs.getString(2), rs.getString(3));
                SrcNamesList.add(psObj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ActMainMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] srcNames = new String[SrcNamesList.size()];
        for (int i = 0; i < SrcNamesList.size(); i++) {
            srcNames[i] = SrcNamesList.get(i).getPublicationSrcName();
        }
        // create the default acts Names mode
        srcNamesModel = new DefaultComboBoxModel(srcNames);

        return srcNamesModel;
    }

    @Override
    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
        addFieldsToValidate(new TreeMap<String, Component>() {
            {
                put(lblPublicationDate.getText().replace("*", ""), dt_publication_date);
                put(lblSourceNo.getText().replace("*", ""), txtSourceNo);
                put(lblSrcName.getText().replace("*", ""), cboSrcName);
            }
        });
        return super.validateSelectedMetadata(spf);
    }

    public void resetComponents() {
        cboSrcName.setSelectedIndex(0);
        dt_publication_date.setDate(null);
        dt_publication_dateHijri.setDate(null);
        jcbScrExcIssue.setSelected(false);
        txtPublicationArea.setText("");
        txtSourceNo.setText("");
    }
  
}
