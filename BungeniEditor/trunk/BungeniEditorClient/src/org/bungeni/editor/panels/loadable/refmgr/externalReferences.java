package org.bungeni.editor.panels.loadable.refmgr;//GEN-FIRST:event_btnAddURIActionPerformed
//GEN-LAST:event_btnAddURIActionPerformed
//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.editor.panels.impl.BaseLaunchablePanel;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniUUID;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooDocMetadataFieldSet;
import org.bungeni.ooo.ooQueryInterface;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author  undesa
 */
public class externalReferences extends BaseLaunchablePanel {
    final static String                    REF_SEPARATOR = "~~";
    private final static String            __TITLE__     = "External References";
    private static org.apache.log4j.Logger log           =
        org.apache.log4j.Logger.getLogger(externalReferences.class.getName());

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton              btnAddRef;
    private javax.swing.JButton              btnAddURI;
    private javax.swing.JButton              btnClose;
    private javax.swing.JButton              btnInsertCrossRef;
    private javax.swing.JButton              btnViewAll;
    private javax.swing.JComboBox            cboDocType;
    private org.jdesktop.swingx.JXDatePicker dtURIdate;
    private javax.swing.JScrollPane          jScrollPane1;
    private javax.swing.JLabel               lblDate;
    private javax.swing.JLabel               lblDocId;
    private javax.swing.JLabel               lblExtRef;
    private javax.swing.JLabel               lblExtRefText;
    private javax.swing.JLabel               lblNewReference;
    private javax.swing.JLabel               lblType;
    private javax.swing.JLabel               lblURIlabel;
    private javax.swing.JLabel               lblUriReference;
    private javax.swing.JTable               tblExternalReferences;
    private javax.swing.JTextField           txtDocId;
    private javax.swing.JTextField           txtExternalReference;
    private javax.swing.JTextField           txtRefDisplayText;
    private javax.swing.JTextField           txtURIlabel;

    // End of variables declaration//GEN-END:variables

    /** Creates new form browseReferences */
    public externalReferences() {

        // initComponents();
    }

    private void init() {
        initTableModel();
    }

    private void initTableModel() {

        // lazy load of tree....
        ExtReferencesTableModelAgent rtmAgent = new ExtReferencesTableModelAgent(this.tblExternalReferences);

        rtmAgent.execute();
        tblExternalReferences.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {

                    // doublic clicked
                    int nRow = tblExternalReferences.getSelectedRow();

                    if (nRow != -1) {

                        /*
                         * ExtReferencesTableModel model = (ExtReferencesTableModel)tblExternalReferences.getModel();
                         * DocumentInternalReference ref = model.getRowData(nRow);
                         * String refName = ref.getActualReferenceName();
                         * //move the cursor lazily
                         * //MoveCursorToRefRangeAgent moveAgent = new MoveCursorToRefRangeAgent(refName);
                         * //moveAgent.execute();
                         */
                    }
                }
            }
        });
    }

    private void applyInsertExternalReference() {
        final int nSelectedRow = this.tblExternalReferences.getSelectedRow();

        if (nSelectedRow != -1) {    // nothing was selected
            try {

                // nothing was selected
                final ExtReferencesTableModel   model       =
                    (ExtReferencesTableModel) tblExternalReferences.getModel();
                final DocumentExternalReference ref         = model.getRowData(nSelectedRow);
                XTextViewCursor                 xtextCursor = ooDocument.getViewCursor();
                XTextCursor                     hrefCursor  = ooDocument.getTextDocument().getText().createTextCursor();

                hrefCursor.gotoRange(xtextCursor.getStart(), false);
                hrefCursor.goRight((short) 0, true);
                hrefCursor.setString("[[" + ref.DisplayText + "]]");

                XPropertySet hrefProps = ooQueryInterface.XPropertySet(hrefCursor);

                if (ref.ReferenceType.equals("external")) {
                    hrefProps.setPropertyValue("HyperLinkTarget", ref.Href);
                    hrefProps.setPropertyValue("HyperLinkURL", ref.Href);
                } else {
                    hrefProps.setPropertyValue("HyperLinkTarget", "http://uriresolver.bungeni.org?q=" + ref.URItext);
                    hrefProps.setPropertyValue("HyperLinkURL", "http://uriresolver.bungeni.org?q=" + ref.URItext);
                }

                // //continue from here////
            } catch (UnknownPropertyException ex) {
                log.error("applyExternalReference :" + ex.getMessage());
            } catch (PropertyVetoException ex) {
                log.error("applyExternalReference :" + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("applyExternalReference :" + ex.getMessage());
            } catch (WrappedTargetException ex) {
                log.error("applyExternalReference :" + ex.getMessage());
            }

            // //continue from here////
        }
    }

    private ExtReferencesTableModel buildExtReferencesTableModel() {

        // we can get all the references from the document properties
        ArrayList<ooDocMetadataFieldSet> metadataFieldSets = ooDocMetadata.getMetadataObjectsByType(ooDocument,
                                                                 OOoNumberingHelper.EXTERNAL_REF_PREFIX);
        ArrayList<DocumentExternalReference> docExtRefs = new ArrayList<DocumentExternalReference>(0);

        // but they are not in document sequential order
        // add them sequentially to our table with their contained text
        // the contained text can be retrieved form the cached document metadata
        for (ooDocMetadataFieldSet field : metadataFieldSets) {
            DocumentExternalReference extRef = DocumentExternalReference.createFromDocMeta(ooDocument,
                                                   field.getMetadataName(), (String) field.getMetadataValue());

            docExtRefs.add(extRef);
        }

        ExtReferencesTableModel rtm = new ExtReferencesTableModel(docExtRefs);

        return rtm;
    }

    private void filterTableModel() {
        String                  fieldFilter = this.txtExternalReference.getText();
        ExtReferencesTableModel model       = (ExtReferencesTableModel) this.tblExternalReferences.getModel();

        model.filterByType(fieldFilter);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        btnInsertCrossRef     = new javax.swing.JButton();
        btnViewAll            = new javax.swing.JButton();
        btnClose              = new javax.swing.JButton();
        jScrollPane1          = new javax.swing.JScrollPane();
        tblExternalReferences = new javax.swing.JTable();
        txtExternalReference  = new javax.swing.JTextField();
        lblNewReference       = new javax.swing.JLabel();
        btnAddRef             = new javax.swing.JButton();
        lblUriReference       = new javax.swing.JLabel();
        dtURIdate             = new org.jdesktop.swingx.JXDatePicker();
        lblDate               = new javax.swing.JLabel();
        cboDocType            = new javax.swing.JComboBox();
        lblType               = new javax.swing.JLabel();
        txtDocId              = new javax.swing.JTextField();
        btnAddURI             = new javax.swing.JButton();
        lblDocId              = new javax.swing.JLabel();
        txtRefDisplayText     = new javax.swing.JTextField();
        lblExtRef             = new javax.swing.JLabel();
        lblExtRefText         = new javax.swing.JLabel();
        lblURIlabel           = new javax.swing.JLabel();
        txtURIlabel           = new javax.swing.JTextField();
        btnInsertCrossRef.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnInsertCrossRef.setText("Insert External Ref");
        btnInsertCrossRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertCrossRefActionPerformed(evt);
            }
        });
        btnViewAll.setFont(new java.awt.Font("DejaVu Sans", 0, 11));    // NOI18N
        btnViewAll.setText("View All");
        btnViewAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllActionPerformed(evt);
            }
        });
        btnClose.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tblExternalReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tblExternalReferences.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
            { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
            { null, null, null, null }
        }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        tblExternalReferences.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblExternalReferences);
        txtExternalReference.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        txtExternalReference.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExternalReferenceKeyPressed(evt);
            }
        });
        lblNewReference.setFont(new java.awt.Font("DejaVu Sans", 2, 11));
        lblNewReference.setText("New External Reference");
        btnAddRef.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnAddRef.setText("Add..");
        btnAddRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRefActionPerformed(evt);
            }
        });
        lblUriReference.setFont(new java.awt.Font("DejaVu Sans", 2, 11));
        lblUriReference.setForeground(java.awt.Color.blue);
        lblUriReference.setText("New URI reference");
        dtURIdate.setForeground(java.awt.Color.blue);
        dtURIdate.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblDate.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblDate.setForeground(java.awt.Color.blue);
        lblDate.setText("Date");
        cboDocType.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboDocType.setForeground(java.awt.Color.blue);
        cboDocType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Act", "Bill", "Debaterecord" }));
        lblType.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblType.setForeground(java.awt.Color.blue);
        lblType.setText("Type");
        txtDocId.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        txtDocId.setForeground(java.awt.Color.blue);
        btnAddURI.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnAddURI.setForeground(java.awt.Color.blue);
        btnAddURI.setText("Add..");
        btnAddURI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddURIActionPerformed(evt);
            }
        });
        lblDocId.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblDocId.setForeground(java.awt.Color.blue);
        lblDocId.setText("Identifier");
        txtRefDisplayText.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblExtRef.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblExtRef.setText("Reference URL");
        lblExtRefText.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblExtRefText.setText("Reference Label");
        lblURIlabel.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblURIlabel.setForeground(java.awt.Color.blue);
        lblURIlabel.setText("URI Label");
        txtURIlabel.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        txtURIlabel.setForeground(java.awt.Color.blue);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);

        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addComponent(
                            jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE).addContainerGap()).addGroup(
                                layout.createSequentialGroup().addComponent(
                                    btnInsertCrossRef, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                    btnViewAll, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                    btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addContainerGap()).addGroup(
                                        layout.createSequentialGroup().addComponent(
                                            lblNewReference, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE).addGap(
                                                136, 136, 136)).addGroup(
                                                    javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(
                                                        layout.createParallelGroup(
                                                            javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                                                                dtURIdate, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                                                                    lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                                                            layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                                                                                    cboDocType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                                                                                        lblType)).addPreferredGap(
                                                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(
                                                                                                layout.createParallelGroup(
                                                                                                    javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                                                                                                        lblDocId).addComponent(
                                                                                                            txtDocId, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(
                                                                                                                layout.createSequentialGroup().addComponent(
                                                                                                                    lblUriReference, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(
                                                                                                                        121, Short.MAX_VALUE)).addGroup(
                                                                                                                            layout.createSequentialGroup().addGroup(
                                                                                                                                layout.createParallelGroup(
                                                                                                                                    javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(
                                                                                                                                        lblExtRefText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblExtRef, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(txtRefDisplayText, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnAddRef, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)).addComponent(txtExternalReference, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(lblURIlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(46, 46, 46).addComponent(txtURIlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnAddURI, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE).addContainerGap()))));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGap(5, 5, 5).addComponent(lblNewReference).addGap(7, 7, 7).addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                        lblExtRef).addComponent(
                        txtExternalReference, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                                txtRefDisplayText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblExtRefText).addComponent(
                                    btnAddRef)).addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
                                        lblUriReference).addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                        layout.createParallelGroup(
                                            javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                                                lblDate).addComponent(lblType).addComponent(lblDocId)).addPreferredGap(
                                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                                        layout.createParallelGroup(
                                                            javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                                                                dtURIdate, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                                                                            cboDocType,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                                                                                            txtDocId,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                                                                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                                                                                                layout.createParallelGroup(
                                                                                                                    javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                                                                                                                        btnAddURI).addComponent(
                                                                                                                            txtURIlabel,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                                                                                                                                            lblURIlabel)).addGap(
                                                                                                                                                10,
                                                                                                                                                10,
                                                                                                                                                10).addComponent(
                                                                                                                                                    jScrollPane1,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                            101,
                                                                                                                                                            javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                                                                                                                                                    layout.createParallelGroup(
                                                                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                                                                                                                                                                            btnInsertCrossRef).addComponent(
                                                                                                                                                                                btnViewAll).addComponent(
                                                                                                                                                                                    btnClose)).addContainerGap(
                                                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                                            Short.MAX_VALUE)));
    }    // </editor-fold>//GEN-END:initComponents

    private void btnInsertCrossRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertCrossRefActionPerformed
        applyInsertExternalReference();
    }//GEN-LAST:event_btnInsertCrossRefActionPerformed

    private void btnViewAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllActionPerformed
        // parentFrame.dispose();
    }//GEN-LAST:event_btnViewAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        parentFrame.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtExternalReferenceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExternalReferenceKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            filterTableModel();
        }
    }//GEN-LAST:event_txtExternalReferenceKeyPressed

    private void btnAddRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRefActionPerformed
        String eRef     = this.txtExternalReference.getText();
        String eRefText = this.txtRefDisplayText.getText();

        if (!eRef.startsWith("http://")) {
            eRef = "http://" + eRef;
        }

        // String refMetaName = OOoNumberingHelper.EXTERNAL_REF_PREFIX + BungeniUUID.getStringUUID();
        DocumentExternalReference dextRef = new DocumentExternalReference(BungeniUUID.getStringUUID(), eRef, eRefText);

        dextRef.update(ooDocument);

        ExtReferencesTableModel model = (ExtReferencesTableModel) this.tblExternalReferences.getModel();

        if (!model.containsRef(eRef)) {
            model.addToModel(dextRef);
        }
    }//GEN-LAST:event_btnAddRefActionPerformed

    private void btnAddURIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddURIActionPerformed
       String                    buildURI      = "";
        Date                      selectedDate  = dtURIdate.getDate();
        String                    docType       = ((String) cboDocType.getSelectedItem()).toLowerCase();
        String                    langISO       = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
        String                    countryISO    =
            BungeniEditorProperties.getEditorProperty("locale.Country.iso3166-1-a2");
        String                    docIdentifier = this.txtDocId.getText();
        String                    refId         = BungeniUUID.getStringUUID();
        DocumentExternalReference extRef        = new DocumentExternalReference(refId, selectedDate, docType, langISO,
                                                      countryISO, docIdentifier, txtURIlabel.getText());
        ExtReferencesTableModel model = (ExtReferencesTableModel) this.tblExternalReferences.getModel();

        extRef.update(ooDocument);

        if (!model.containsRef(extRef.URItext)) {
            model.addToModel(extRef);
        }
    }//GEN-LAST:event_btnAddURIActionPerformed

    @Override
    public Component getObjectHandle() {
        return this;
    }

    @Override
    public void initUI() {
        initComponents();
        init();
    }

    @Override
    public String getPanelTitle() {
        return __TITLE__;
    }

    static class DocumentExternalReference implements Cloneable {
        String DisplayText;
        String Href;

        // name of the reference preceded by rf:
        String Name;
        String ReferenceType;    // external, uri
        String URItext;

        DocumentExternalReference(String name, String refText, String displayText) {
            Name          = makeName(name);
            Href          = refText;
            DisplayText   = displayText;
            ReferenceType = "external";
            URItext       = "";

            // ParentType = parentType;
        }

        DocumentExternalReference(String name, Date dt, String docType, String langCode, String countryCode,
                                  String docIdentifier, String displayText) {
            Name          = makeName(name);
            Href          = "";
            DisplayText   = displayText;
            ReferenceType = "uri";
            URItext       = makeURI(dt, docType, langCode, countryCode, docIdentifier);

            // ParentType = parentType;
        }

        private String makeName(String n) {
            return OOoNumberingHelper.EXTERNAL_REF_PREFIX + n;
        }

        private String makeURI(Date dt, String docType, String langISO, String countryISO, String docId) {
            Date             selectedDate     = dt;
            SimpleDateFormat df               = new SimpleDateFormat("yyyy/MM/dd");
            String           formattedURIdate = df.format(selectedDate);
            String           URIseparator     = "/";

            return URIseparator + countryISO + URIseparator + docType + URIseparator + formattedURIdate + URIseparator
                   + langISO + URIseparator + docId;
        }

        public String getActualReferenceName() {
            int nNameIndex = Name.indexOf(":");

            if (nNameIndex != -1) {
                return Name.substring(nNameIndex + 1);
            } else {
                return Name;
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            DocumentExternalReference cloneRef = (DocumentExternalReference) super.clone();

            return cloneRef;
        }

        private void update(OOComponentHelper ooDoc) {
            ooDocMetadata docMeta = new ooDocMetadata(ooDoc);

            if (ReferenceType.equals("external")) {
                docMeta.AddProperty(this.Name, this.Href + "~~" + this.DisplayText);
            } else if (ReferenceType.equals("uri")) {
                docMeta.AddProperty(this.Name, this.URItext + "~~" + this.DisplayText);
            }
        }

        public static DocumentExternalReference createFromDocMeta(OOComponentHelper ooDoc, String refName,
                String refValue) {

            // split into meta name and meta value;
            String[] referenceComponents = refValue.split("~~");

            return new DocumentExternalReference(refName, referenceComponents[0], referenceComponents[1]);
        }
    }


    class ExtReferencesTableModel extends AbstractTableModel {
        private String[]                     columns                    = { "URL", "Display Text", "Reference Type", };
        ArrayList<DocumentExternalReference> documentReferences         = new ArrayList<DocumentExternalReference>();
        ArrayList<DocumentExternalReference> filteredDocumentReferences = new ArrayList<DocumentExternalReference>();
        private Class[]                      column_class               = { String.class, String.class, String.class };

        public ExtReferencesTableModel(ArrayList<DocumentExternalReference> dref) {
            super();
            documentReferences = dref;

            // make a memcopy of the dref variable since refArr will be changing
            filteredDocumentReferences = (ArrayList<DocumentExternalReference>) dref.clone();
        }

        public void resetModel() {
            synchronized (filteredDocumentReferences) {
                filteredDocumentReferences = (ArrayList<DocumentExternalReference>) documentReferences.clone();
            }

            fireTableDataChanged();
        }

        public boolean containsRef(String refValue) {
            boolean bState = false;

            for (DocumentExternalReference dref : documentReferences) {
                if (dref.Href.equals(refValue)) {
                    bState = true;

                    break;
                }
            }

            return bState;
        }

        public DocumentExternalReference findMatchingRef(String refName) {
            refName = OOoNumberingHelper.INTERNAL_REF_PREFIX + refName;

            DocumentExternalReference foundRef = null;

            for (DocumentExternalReference dref : documentReferences) {
                if (dref.Name.toLowerCase().equals(refName.toLowerCase())) {

                    // matched
                    foundRef = dref;

                    break;
                }
            }

            return foundRef;
        }

        public void filterByType(String filterRefTo) {
            filterRefTo = filterRefTo.toLowerCase();
            log.debug("filterByType : filter for : " + filterRefTo);

            /*
             * FilterSettings filterBy = (FilterSettings) cboFilterSettings.getSelectedItem();
             * synchronized(filteredDocumentReferences) {
             *   filteredDocumentReferences.clear();
             *   for (DocumentInternalReference dref : documentReferences) {
             *       String matchWithThis = "";
             *        if (filterBy.Name.equals("by-container")) {
             *           matchWithThis = dref.ParentType.toLowerCase();
             *        } else if (filterBy.Name.equals("by-type")) {
             *           matchWithThis = dref.ReferenceType.toLowerCase();
             *        } else if (filterBy.Name.equals("by-reftext")) {
             *           matchWithThis = dref.ReferenceText.toLowerCase();
             *        }
             *      log.debug("filterByType : filter by : " + matchWithThis);
             *      if (matchWithThis.contains(filterRefTo)) {
             *          //matching table model
             *          filteredDocumentReferences.add(dref);
             *      }
             *  }
             * }
             */
            fireTableDataChanged();
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Class getColumnClass(int col) {
            return column_class[col];
        }

        public int getRowCount() {
            return filteredDocumentReferences.size();
        }

        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 1) {
                return true;
            } else {
                return false;
            }
        }

        public Object getValueAt(int row, int col) {
            DocumentExternalReference rfObj = filteredDocumentReferences.get(row);

            switch (col) {
            case 0 :
                if (rfObj.ReferenceType.equals("external")) {
                    return rfObj.Href;
                } else {
                    return rfObj.URItext;
                }
            case 1 :
                return rfObj.DisplayText;

            case 2 :
                return rfObj.ReferenceType;

            default :
                return rfObj.Href;
            }
        }

        public DocumentExternalReference getRowData(int row) {
            return this.filteredDocumentReferences.get(row);
        }

        private void addToModel(DocumentExternalReference dextRef) {
            synchronized (documentReferences) {
                documentReferences.add(dextRef);
            }

            synchronized (filteredDocumentReferences) {
                filteredDocumentReferences.add(dextRef);
            }

            fireTableDataChanged();
        }
    }


    /**
     * SwingWorker agent to do a lazy load of the references tree...
     * The document references are gathered using threaded agent
     * and the tree is loaded once all the references have been gathered.
     */
    class ExtReferencesTableModelAgent extends SwingWorker<ExtReferencesTableModel, Void> {
        JTable updateThisTable = null;

        ExtReferencesTableModelAgent(JTable inputTable) {

            // tableModel = model;
            updateThisTable = inputTable;
        }

        @Override
        protected ExtReferencesTableModel doInBackground() throws Exception {
            ExtReferencesTableModel rtm = buildExtReferencesTableModel();

            return rtm;
        }

        @Override
        protected void done() {
            try {
                ExtReferencesTableModel rtm = get();

                if (rtm != null) {
                    updateThisTable.setModel(rtm);
                }
            } catch (InterruptedException ex) {
                log.error("ReferencesTableModelAgent : " + ex.getMessage());
            } catch (ExecutionException ex) {
                log.error("ReferencesTableModelAgent : " + ex.getMessage());
            }
        }
    }


    class FilterSettings {
        String DisplayText;
        String Name;

        FilterSettings(String n, String d) {
            Name        = n;
            DisplayText = d;
        }

        @Override
        public String toString() {
            return DisplayText;
        }
    }


    class MoveCursorToRefRangeAgent extends SwingWorker<Boolean, Void> {
        String thisRange;

        MoveCursorToRefRangeAgent(String refname) {
            thisRange = refname;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            XNameAccess refMarks = ooDocument.getReferenceMarks();

            if (refMarks.hasByName(thisRange)) {
                try {
                    Object       oRefMark     = refMarks.getByName(thisRange);
                    XTextContent xRefContent  = ooQueryInterface.XTextContent(oRefMark);
                    XTextRange   refMarkRange = xRefContent.getAnchor();

                    ooDocument.getViewCursor().gotoRange(refMarkRange, false);
                } catch (NoSuchElementException ex) {
                    log.error("tblAllReferences:double_click : " + ex.getMessage());
                } catch (WrappedTargetException ex) {
                    log.error("tblAllReferences:double_click : " + ex.getMessage());
                }
            }

            return true;
        }
    }
}
