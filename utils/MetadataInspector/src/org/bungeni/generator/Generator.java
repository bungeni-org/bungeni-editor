/*
 * Generator.java
 *
 * Created on September 5, 2008, 12:08 PM
 */

package org.bungeni.generator;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameContainer;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.XComponentContext;
import java.io.File;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author  undesa
 */
public class Generator extends javax.swing.JFrame {
    org.bungeni.ooo.BungenioOoHelper openofficeObject;
    OOComponentHelper ooDocument;
    String pathToOpenFile;
    String selectedSection;
    /** Creates new form Generator */
    public Generator() {
        initComponents();
    }

    public Generator(XComponentContext xContext) {
        initComponents();
        openofficeObject = new BungenioOoHelper(xContext);
        openofficeObject.initoOo();
        
    }
    
    private  File getFileFromChooser(String basePath, FileFilter filter, int fileSelectionMode) {
        File returnFile = null;
        try {
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            final JFileChooser fc = new JFileChooser(basePath);
            fc.setFileFilter((javax.swing.filechooser.FileFilter) filter);
            fc.setFileSelectionMode(fileSelectionMode);
            int nReturnVal = fc.showOpenDialog(this);
            if (nReturnVal == JFileChooser.APPROVE_OPTION) {
                    returnFile = fc.getSelectedFile();
                    return returnFile; 
                } 
        } catch (Exception ex) {
            
        } finally {
            return returnFile;
        }
}
    
        public static XComponent newDocument(String templatePath) {
        XComponent xComponent = null;
        
        try {
            PropertyValue[] loadProps = new com.sun.star.beans.PropertyValue[2];
            PropertyValue xTemplateProperty = new com.sun.star.beans.PropertyValue();
            xTemplateProperty.Name = "Template";
            xTemplateProperty.Value = true;
            loadProps[0] = xTemplateProperty;
            com.sun.star.beans.PropertyValue xMacroExecProperty = new com.sun.star.beans.PropertyValue();
            xMacroExecProperty.Name = "MacroExecutionMode";
            xMacroExecProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
            loadProps[1] = xMacroExecProperty;
            if (templatePath.equals(""))
                templatePath = "private:factory/swriter";
            else 
                templatePath = BungenioOoHelper.convertPathToURL(templatePath);
            //launch window
             xComponent = BungenioOoHelper.getComponentLoader().loadComponentFromURL(templatePath, "_blank", 0, loadProps);
        } catch (Exception ex) {
           
        } finally {
            return xComponent;
        }
     }
   
    private OOComponentHelper openOOoFileAsTemplate(){
        boolean bFiles = optionFile.isSelected();
        File fileName= getFileFromChooser("/home/undesa/Documents", new ODTFileFilter(bFiles), JFileChooser.FILES_ONLY);
        if (fileName == null) {
           return null;
        }
        XComponent xComp;
        xComp = OOComponentHelper.openTemplate(fileName.getAbsolutePath());
        //XComponent xComp = newDocument(fileName.getAbsolutePath());
        if (xComp == null) {
            return null;
        }
        this.txtTemplate.setText(fileName.getAbsolutePath());
       return new OOComponentHelper(xComp, openofficeObject.getComponentContext());
        
    }
        
    private OOComponentHelper openOOoFile(){
        boolean bFiles = optionFile.isSelected();
        File fileName= getFileFromChooser("/home/undesa/Documents", new ODTFileFilter(bFiles), JFileChooser.FILES_ONLY);
        if (fileName == null) {
           return null;
        }
        XComponent xComp;
        if (bFiles) {
            xComp = OOComponentHelper.openExistingDocument(fileName.getAbsolutePath());
        } else {
            xComp = newDocument(fileName.getAbsolutePath());
        }
        //XComponent xComp = newDocument(fileName.getAbsolutePath());
        if (xComp == null) {
            return null;
        }
      this.txtTemplate.setText(fileName.getAbsolutePath());
       return new OOComponentHelper(xComp, openofficeObject.getComponentContext());
    }

    private void updateMetaModel() {
        if (ooDocument != null) {
            DocumentMetadataTableModel mdl = new DocumentMetadataTableModel(ooDocument);
            this.tblDocMetadata.setModel(mdl);
            this.tblDocMetadata.getModel().addTableModelListener(new TableModelListener(){

                    public void tableChanged(TableModelEvent e) {
                       int row = e.getFirstRow();
                       int col = e.getColumn();
                       TableModel model = (TableModel)e.getSource();
                       final Object value = model.getValueAt(row, col);
                       final Object metaName = model.getValueAt(row, col -1);
                       ooDocument.setPropertyValue(metaName.toString(), value.toString());
                    }

            });
        }
    }
    private void updateModel(String sectName) {
        final String currentSectionName = sectName;
        this.tblSectionMetadata.setModel(new SectionMetadataLoad(ooDocument, currentSectionName));
        this.tblSectionMetadata.getModel().addTableModelListener(new TableModelListener(){

                public void tableChanged(TableModelEvent e) {
                   int row = e.getFirstRow();
                   int col = e.getColumn();
                   TableModel model = (TableModel)e.getSource();
                   final Object value = model.getValueAt(row, col);
                   final Object metaName = model.getValueAt(row, col -1);
                   HashMap<String, String> putMeta = new HashMap(){
                       {
                           put(metaName.toString(), value.toString());
                       }
                   };
                   ooDocument.setSectionMetadataAttributes(currentSectionName, putMeta);
                }
            
        });

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filetype = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        txtTemplate = new javax.swing.JTextField();
        btnSelectDocument = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSectionMetadata = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        cursorInSection = new javax.swing.JLabel();
        optionFile = new javax.swing.JRadioButton();
        optionTemplate = new javax.swing.JRadioButton();
        btnAddRow = new javax.swing.JButton();
        txtNewRow = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDocMetadata = new javax.swing.JTable();
        btnAddDocMeta = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDocMeta = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Document Metadata Inspector");

        txtTemplate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        btnSelectDocument.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnSelectDocument.setText("Select Doc");
        btnSelectDocument.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDocumentActionPerformed(evt);
            }
        });

        tblSectionMetadata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblSectionMetadata);

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        cursorInSection.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        cursorInSection.setText("None");

        filetype.add(optionFile);
        optionFile.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        optionFile.setSelected(true);
        optionFile.setText("File");
        optionFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionFileActionPerformed(evt);
            }
        });

        filetype.add(optionTemplate);
        optionTemplate.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        optionTemplate.setText("Template");

        btnAddRow.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnAddRow.setText("Add ");
        btnAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRowActionPerformed(evt);
            }
        });

        txtNewRow.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        tblDocMetadata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblDocMetadata);

        btnAddDocMeta.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnAddDocMeta.setText("Add Row");
        btnAddDocMeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDocMetaActionPerformed(evt);
            }
        });

        jLabel1.setText("Section Metadata ::");

        jLabel2.setText("Document Metadata ::");

        txtDocMeta.setFont(new java.awt.Font("DejaVu Sans", 0, 10));

        jButton1.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jButton1.setText("Del");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(optionFile)
                        .addGap(47, 47, 47)
                        .addComponent(optionTemplate)
                        .addContainerGap(117, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(48, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtDocMeta, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddDocMeta, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSelectDocument, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addGap(60, 60, 60))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cursorInSection, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtNewRow, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnAddRow, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optionFile)
                    .addComponent(optionTemplate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectDocument))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh)
                    .addComponent(cursorInSection))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNewRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnAddRow))
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddDocMeta)
                    .addComponent(txtDocMeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 293, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void optionFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionFileActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_optionFileActionPerformed

private void btnSelectDocumentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDocumentActionPerformed
// TODO add your handling code here:
        if (optionFile.isSelected())
        ooDocument = openOOoFile();//GEN-LAST:event_btnSelectDocumentActionPerformed
        else
        ooDocument = openOOoFileAsTemplate();
        
        updateMetaModel();
        
        if (ooDocument == null) {
            JOptionPane.showMessageDialog(null, "Unable to get handle to file!");
        }
}

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    
    final String currentSectionName = ooDocument.currentSectionName();
    this.cursorInSection.setText(currentSectionName);
    this.selectedSection = currentSectionName;
    if (currentSectionName.length() != 0) {
        updateModel(currentSectionName);
    } else {
        JOptionPane.showMessageDialog(this, "Cursor was not placed in a section !");
    }
}//GEN-LAST:event_btnRefreshActionPerformed

    
    
private void btnAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRowActionPerformed
// TODO add your handling code here:
   if (selectedSection.length() != 0) {
       final String newRowText = this.txtNewRow.getText().trim();
       if (newRowText.length() > 0 ) {
           if (newRowText.contains(",")){
               final String[] newKeyValue = newRowText.split(",");
               HashMap<String,String> putMeta = new HashMap<String,String>(){  {
                       put(newKeyValue[0].trim(), newKeyValue[1].trim());
                   } };
               if (ooDocument.getSectionMetadataAttributes(selectedSection).containsKey(newKeyValue[0].trim()))
                   return;
               ooDocument.setSectionMetadataAttributes(selectedSection, putMeta);
           } else {
               HashMap<String,String> putMeta = new HashMap<String,String>(){  {
                       put(newRowText.trim(), "");
                   } };
               if (ooDocument.getSectionMetadataAttributes(selectedSection).containsKey(newRowText.trim()))
                   return;
               ooDocument.setSectionMetadataAttributes(selectedSection, putMeta);
           }
           
           updateModel(selectedSection);
           
       }
   }
}//GEN-LAST:event_btnAddRowActionPerformed

private void btnAddDocMetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDocMetaActionPerformed
// TODO add your handling code here:
   if (ooDocument != null) {
       final String newMeta = this.txtDocMeta.getText().trim();
       if (newMeta.length() > 0 ) {
           if (newMeta.contains(",")){
               final String[] newKeyValue = newMeta.split(",");
               if (ooDocument.propertyExists(newKeyValue[0].trim()) )
                       return;
               ooDocument.addProperty(newKeyValue[0].trim(), newKeyValue[1].trim());
           } else {
               if (ooDocument.propertyExists(newMeta.trim()) )
                       return;
               ooDocument.addProperty(newMeta.trim(), "");
           }
           this.updateMetaModel();
           
       }
   }    
    
}//GEN-LAST:event_btnAddDocMetaActionPerformed

  private XNameContainer _getAttributeContainer(XPropertySet xProperties) {
        XNameContainer attributeContainer = null;
        try {
            //get cursor property set
           //we wante the textuserdefinedattributes propertycontainer
           attributeContainer = (XNameContainer) AnyConverter.toObject(new Type(XNameContainer.class), xProperties.getPropertyValue("UserDefinedAttributes"));
        } catch (Exception  ex) {
        } finally {
            return attributeContainer; 
        }
   }

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    int nRow = this.tblSectionMetadata.getSelectedRow();
    String sValue = (String) this.tblSectionMetadata.getModel().getValueAt(nRow, 0);
    String currentSection = this.cursorInSection.getText();
    if (currentSection.length() > 0 ) {
            try {
                XTextSection xSection = ooDocument.getSection(currentSection);
                XPropertySet xProps = ooQueryInterface.XPropertySet(xSection);
                XNameContainer xname = this._getAttributeContainer(xProps);
                xname.removeByName(sValue);
                this.updateModel(sValue);
            } catch (NoSuchElementException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}//GEN-LAST:event_jButton1ActionPerformed

      public  class ODTFileFilter extends FileFilter {
        String ext = "";
          public ODTFileFilter(){
            super();
        }
        public ODTFileFilter(boolean bType){
            super();
            if (bType) {
                ext = "odt";
            } else {
                ext = "ott";
            }
        }
        
        @Override
        public boolean accept(File arg0) {
            if (arg0.isDirectory()) return true;
       
            String extension = getExtension(arg0);
            if (extension.equals(ext)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
            if (ext.equals("odt"))
                return "OpenDocument files";
            else
                return "OpenDocument Template files";
        }
        
        private String getExtension(File fname){
            String filename = fname.getName();
            int i = filename.lastIndexOf(".");
            if (i > 0 && i < filename.length() - 1)
                return filename.substring(i+1).toLowerCase();
            return "";
        }
       
        
    }
      
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Generator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDocMeta;
    private javax.swing.JButton btnAddRow;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSelectDocument;
    private javax.swing.JLabel cursorInSection;
    private javax.swing.ButtonGroup filetype;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton optionFile;
    private javax.swing.JRadioButton optionTemplate;
    private javax.swing.JTable tblDocMetadata;
    private javax.swing.JTable tblSectionMetadata;
    private javax.swing.JTextField txtDocMeta;
    private javax.swing.JTextField txtNewRow;
    private javax.swing.JTextField txtTemplate;
    // End of variables declaration//GEN-END:variables

}
