/*
 * browseReferences.java
 *
 * Created on July 14, 2008, 3:43 PM
 */

package org.bungeni.editor.panels.loadable.refmgr;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.bungeni.editor.document.ReferencesSyntax;
import org.bungeni.editor.document.ReferencesSyntax.QuerySyntax;
import org.bungeni.editor.document.ReferencesSyntaxFactory;
import org.bungeni.editor.numbering.ooo.OOoNumberingHelper;
import org.bungeni.editor.panels.impl.BaseLaunchablePanel;
import org.bungeni.editor.providers.DocumentSectionIterator;
import org.bungeni.editor.providers.IBungeniSectionIteratorListener;
import org.bungeni.ooo.ooDocMetadata;
import org.bungeni.ooo.ooDocMetadataFieldSet;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.utils.BungeniBNode;
import org.bungeni.utils.MessageBox;

/**
 *
 * @author  undesa
 */
public class browseReferences extends BaseLaunchablePanel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(browseReferences.class.getName());
    private final static String __TITLE__ = "Browse References";      
    
    private int m_tableSelectedRow = -1;
    
    /** Creates new form browseReferences */
    public browseReferences() {
       // initComponents();
    }


    private void init(){
        initTableModel();
        initFilter();
    }
    
    private void initTableModel(){
        //lazy load of tree....
        ReferencesTableModelAgent rtmAgent = new ReferencesTableModelAgent(this.tblAllReferences);
        rtmAgent.execute();
        tblAllReferences.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    //doublic clicked
                    int nRow = tblAllReferences.getSelectedRow();
                    if (nRow != -1) {
                        ReferencesTableModel model = (ReferencesTableModel)tblAllReferences.getModel();
                        DocumentInternalReference ref = model.getRowData(nRow);
                        String refName = ref.getActualReferenceName();
                        //move the cursor lazily
                        MoveCursorToRefRangeAgent moveAgent = new MoveCursorToRefRangeAgent(refName);
                        moveAgent.execute();
                    }
                }
            }
        });
    }
    
    
    class MoveCursorToRefRangeAgent extends SwingWorker<Boolean, Void>{
        String thisRange ;
        MoveCursorToRefRangeAgent (String refname) {
            thisRange = refname;
        }
        @Override
        protected Boolean doInBackground() throws Exception{
              XNameAccess refMarks = ooDocument.getReferenceMarks();
              if (refMarks.hasByName(thisRange)){
                  try {
                    Object oRefMark = refMarks.getByName(thisRange);
                    XTextContent xRefContent = ooQueryInterface.XTextContent(oRefMark);
                    XTextRange refMarkRange = xRefContent.getAnchor();
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
    
    class FilterSettings {
        String Name;
        String DisplayText;
        
        FilterSettings(String n, String d) {
            Name = n;
            DisplayText = d;
        }
        
        @Override
        public String toString(){
            return DisplayText;
        }
    }
    
    private void initFilter(){
        ArrayList<FilterSettings> filterSettings=new ArrayList<FilterSettings>(0);
        filterSettings.add(new FilterSettings("by-container", "By Ref To"));
        filterSettings.add(new FilterSettings("by-type", "By Type"));
        filterSettings.add(new FilterSettings("by-reftext", "By Ref Text"));
        
        cboFilterSettings.setModel(new DefaultComboBoxModel(filterSettings.toArray()));
    }

    
    private void applyInsertCrossReference() {
        final int nSelectedRow = this.tblAllReferences.getSelectedRow();
        if (nSelectedRow !=  -1) { //nothing was selected
            int nRow = tblAllReferences.getSelectedRow();
            final ReferencesTableModel model = (ReferencesTableModel) tblAllReferences.getModel();
            final DocumentInternalReference ref = model.getRowData(nRow);
             XTextSection selectedSection = ooDocument.getSection(ref.ContainerSection);
            //get the complete reference chain
           ///build an arraylist of documentinternl referecnes  
           // ArrayList<String> referenceSourceChain = getReferenceSourceChain(selectedSection);
           ArrayList<DocumentInternalReference> referenceSourceChain = getReferenceSourceChain(ref.ReferenceType, selectedSection);
             insertCrossRef(referenceSourceChain);
            MessageBox.OK(this, "The reference was successfully inserted!");
            
          //  insertCrossRef (new ArrayList<String>() {
          //          {  add(ref.getActualReferenceName()) ;}
          //      });
        }
    }
    
       private ArrayList<DocumentInternalReference> getReferenceSourceChain(String refType, XTextSection refSection){
        //refSection has the insertable reference, we go up the chain to insert all parent references
        //ArrayList<String> numberReferencesList = new ArrayList<String>(0);
           ArrayList<DocumentInternalReference> numberReferencesList = new ArrayList<DocumentInternalReference>(0);
          while (refSection != null ) {
            String refSource = getNumberedReferenceSource(refType, refSection);
            ReferencesTableModel refModel = (ReferencesTableModel) this.tblAllReferences.getModel();
            DocumentInternalReference dInternalRef = refModel.findMatchingRef(refSource);
            //if (refSource != null ) {
            if (dInternalRef != null) {
                //found a number reference add it to our list
                //numberReferencesList.add(refSource);
                numberReferencesList.add(dInternalRef);
            }   //go up 2 levles and see if the grand parent is valid and has a numbered child container
            refSection = getParentNumberedSection(refSection);
          }
         return numberReferencesList;
    }
    
       
    private XTextSection getParentNumberedSection(XTextSection aChild){
            XTextSection aGrandParent = getGrandParentSection(aChild);
            XTextSection aNumberedSection = ooDocument.getChildSectionByType(aGrandParent, "NumberedContainer");
            return aNumberedSection;
    }
    
        
    private XTextSection getGrandParentSection(XTextSection aSection) {
        XTextSection aParent = aSection.getParentSection();
        if (aParent == null ) return null;
        XTextSection aGrandParent = aParent.getParentSection();
        return aGrandParent;
    }
    
 
    private String getNumberedReferenceSource(String refType, XTextSection refSection) {
        HashMap<String,String> refMeta = ooDocument.getSectionMetadataAttributes(refSection);
        if (refMeta.containsKey("BungeniSectionUUID")) {
            String uuidStr = refMeta.get("BungeniSectionUUID");
            //get both heading and number references for this match
            //conditionally use heading or number prefix
          //  String referenceName = OOoNumberingHelper.HEADING_REF_PREFIX+uuidStr;
            String referenceName = "";
            if (refType.toLowerCase().equals("number")) {
                referenceName = OOoNumberingHelper.NUMBER_REF_PREFIX + uuidStr;
            } else if (refType.toLowerCase().equals("heading")){
                referenceName = OOoNumberingHelper.HEADING_REF_PREFIX + uuidStr;
            } else {
                referenceName = OOoNumberingHelper.HEADING_REF_PREFIX + uuidStr;
            }
            //String referenceName = OOoNumberingHelper.NUMBER_REF_PREFIX+uuidStr;
            if (ooDocument.getReferenceMarks().hasByName(referenceName)) {
                return referenceName;
            }
        }
        return null;
    }
 
 
    private boolean insertCrossRef(ArrayList<DocumentInternalReference> referenceChain) {
          final int lastIndex = referenceChain.size() - 1;  
          boolean bState = false;
          XTextViewCursor viewCursor = ooDocument.getViewCursor();
          XTextDocument xDoc = ooDocument.getTextDocument();

          try {
              if (lastIndex > 0) {
                  xDoc.getText().insertString(viewCursor, "[[", false);
              }
              for (int i = lastIndex ; i >= 0 ; i--) {
                  //for every item in the array we insert on the basic of referencessyntax
                  ReferencesSyntax billRefSyntax = ReferencesSyntaxFactory.getSyntaxObject("bill-format-1");
                   ArrayList<ReferencesSyntax.QuerySyntax> qs = 
                      new ArrayList<ReferencesSyntax.QuerySyntax>(){
                       {  
                        add(ReferencesSyntax.newQS("articleTypePrefix", ""));
                        add(ReferencesSyntax.newQS("articleTypeSuffix", " "));
                        add(ReferencesSyntax.newQS("referencePrefix", ""));
                        add(ReferencesSyntax.newQS("referenceSuffix", ""));
                       }
                   };
                  String billRef = billRefSyntax.getSyntax(QuerySyntax.toArray(qs));
                  DocumentInternalReference iRef = referenceChain.get(i);
                  Object oRefField=ooDocument.createInstance("com.sun.star.text.TextField.GetReference");
                  XPropertySet refFieldProps = ooQueryInterface.XPropertySet(oRefField);
                    refFieldProps.setPropertyValue("ReferenceFieldSource", com.sun.star.text.ReferenceFieldSource.REFERENCE_MARK);
                    refFieldProps.setPropertyValue("SourceName", iRef.getActualReferenceName());
                    refFieldProps.setPropertyValue("ReferenceFieldPart", com.sun.star.text.ReferenceFieldPart.TEXT);
                    XTextContent fieldContent = ooQueryInterface.XTextContent(oRefField);  
                    xDoc.getText().insertString(viewCursor, iRef.ParentType, false);
                    if (iRef.ReferenceType.equals("number"))
                        xDoc.getText().insertString(viewCursor, " " , false);
                    else
                        xDoc.getText().insertString(viewCursor, ":", false);
                    xDoc.getText().insertTextContent(viewCursor, fieldContent, true);
                    if (i > 0 ) { //no comma for 
                        xDoc.getText().insertString(viewCursor, ",", false);
                    }
              }
              xDoc.getText().insertString(viewCursor, "]]", false);
              //XRefreshable xRefresh = ooQueryInterface.XRefreshable(xDoc);
              //xRefresh.refresh();
              ooDocument.textFieldsRefresh();
              bState = true;
            } catch (WrappedTargetException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } catch (UnknownPropertyException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } catch (com.sun.star.lang.IllegalArgumentException ex) {
                log.error("insertCrossRef ("+ex.getClass().getName() +") " + ex.getMessage());
            } finally {
                return bState;
            }
    }

    /**
     * SwingWorker agent to do a lazy load of the references tree...
     * The document references are gathered using threaded agent
     * and the tree is loaded once all the references have been gathered.
     */
    class ReferencesTableModelAgent extends SwingWorker<ReferencesTableModel, Void>{
        JTable updateThisTable = null;
        ReferencesTableModelAgent(JTable inputTable){
           // tableModel = model;
            updateThisTable = inputTable;
        }
        @Override
        protected ReferencesTableModel doInBackground() throws Exception {
            ReferencesTableModel rtm = buildReferencesTableModel();
            return rtm;
        }
        
        @Override
        protected void done(){
            try {
                ReferencesTableModel rtm = get();
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
    
      class ReferencesInSectionListener implements IBungeniSectionIteratorListener{
         ArrayList<ooDocMetadataFieldSet> referenceNames = new ArrayList<ooDocMetadataFieldSet>(0);
         HashMap<String, String> documentReferences = new HashMap<String,String>();
         ArrayList<DocumentInternalReference> referenceList = new ArrayList<DocumentInternalReference>(0);
         ReferencesInSectionListener(ArrayList<ooDocMetadataFieldSet> meta) {
             referenceNames = meta;
         }
         
         private void findMatchingRefs (XTextSection foundSection, String uuidStr) {
             for (ooDocMetadataFieldSet metaField : referenceNames) {
                 if (metaField.getMetadataName().indexOf(uuidStr) != -1) {
                     if (! documentReferences.containsKey(metaField.getMetadataName())) {
                         String foundSectionType = ooDocument.getSectionType(foundSection);
                         XNamed secName = ooQueryInterface.XNamed(foundSection);
                         String visibility = DocumentSectionsContainer.getDocumentSectionsContainer().get(foundSectionType).getSectionVisibilty();
                         DocumentInternalReference refObj = new DocumentInternalReference(metaField.getMetadataName(), metaField.getMetadataValue(), secName.getName());
                       //  MessageBox.OK("Section : " + secName.getName() + " , visiblity = " + visibility);
                         if (visibility.equals("user")) {
                            refObj.setParentType(foundSectionType);
                         } else {
                             //if its not a user section, we get the parent section
                             //and check its visibility
                             XTextSection xSection = foundSection.getParentSection();
                             if (xSection != null) {
                                 String sectionType = ooDocument.getSectionType(xSection);
                                 if (sectionType != null) {
                                     refObj.setParentType(sectionType);
                                 } else {
                                     refObj.setParentType(foundSectionType);
                                 }
                             } else {
                                refObj.setParentType(foundSectionType);
                             }
                         }
                         documentReferences.put(metaField.getMetadataName(), "");
                         this.referenceList.add(refObj);
                     }
                         
                 }
             }
         }
         
         public boolean iteratorCallback(BungeniBNode bNode) {
             String sectionName = bNode.getName();
             XTextSection foundSection = ooDocument.getSection(sectionName);
             HashMap<String,String> sectionmeta = ooDocument.getSectionMetadataAttributes(foundSection);
             if (sectionmeta.containsKey(OOoNumberingHelper.SECTION_IDENTIFIER)) {
                 String sectionUUID = sectionmeta.get(OOoNumberingHelper.SECTION_IDENTIFIER);
                 //look for references with this UUID
                 findMatchingRefs (foundSection,  sectionUUID);
             }
            return true;
        }

     }
    
     private ReferencesTableModel buildReferencesTableModel(){
        //we can get all the references from the document properties
         ArrayList<ooDocMetadataFieldSet> metadataFieldSets = ooDocMetadata.getMetadataObjectsByType(ooDocument, OOoNumberingHelper.INTERNAL_REF_PREFIX);
        //but they are not in document sequential order
         ReferencesInSectionListener allRefsListener = new ReferencesInSectionListener(metadataFieldSets);
         DocumentSectionIterator iterateRefs = new DocumentSectionIterator(allRefsListener);
        //so we iterate through all the sections in the document
         iterateRefs.startIterator();
        //find the references in each section
         ArrayList<DocumentInternalReference> docRefs = allRefsListener.referenceList;
         //add them sequentially to our table with their contained text
        //the contained text can be retrieved form the cached document metadata
         ReferencesTableModel rtm = new ReferencesTableModel(docRefs);
         return rtm;
    }
     
     class ReferencesTableModel extends AbstractTableModel {
         
         ArrayList<DocumentInternalReference> documentReferences = new ArrayList<DocumentInternalReference>();
         ArrayList<DocumentInternalReference> filteredDocumentReferences = new ArrayList<DocumentInternalReference>();
         
         public ReferencesTableModel (ArrayList<DocumentInternalReference> dref) {
            super();
            documentReferences = dref;
            //make a memcopy of the dref variable since refArr will be changing
           filteredDocumentReferences = (ArrayList<DocumentInternalReference>) dref.clone();
         }
        
         public void resetModel() {
            synchronized(filteredDocumentReferences) {
                filteredDocumentReferences = (ArrayList<DocumentInternalReference>) documentReferences.clone();
            }
            fireTableDataChanged();
         }
         
         public DocumentInternalReference findMatchingRef(String refName) {
             refName = OOoNumberingHelper.INTERNAL_REF_PREFIX + refName;
             DocumentInternalReference foundRef = null;
             for (DocumentInternalReference dref : documentReferences) {
                 if (dref.Name.toLowerCase().equals(refName.toLowerCase())) {
                     //matched 
                     foundRef = dref;
                     break;
                 }
             }
             return foundRef;
         }
         
         public void filterByType(String filterRefTo) {
             
             filterRefTo = filterRefTo.toLowerCase();
             log.debug("filterByType : filter for : " + filterRefTo);
             FilterSettings filterBy = (FilterSettings) cboFilterSettings.getSelectedItem();
             synchronized(filteredDocumentReferences) {
                 filteredDocumentReferences.clear();
                 for (DocumentInternalReference dref : documentReferences) {
                     String matchWithThis = "";
                      if (filterBy.Name.equals("by-container")) {
                         matchWithThis = dref.ParentType.toLowerCase();
                      } else if (filterBy.Name.equals("by-type")) {
                         matchWithThis = dref.ReferenceType.toLowerCase();  
                      } else if (filterBy.Name.equals("by-reftext")) {
                         matchWithThis = dref.ReferenceText.toLowerCase();
                      }
                    log.debug("filterByType : filter by : " + matchWithThis);
                    if (matchWithThis.contains(filterRefTo)) {
                        //matching table model
                        filteredDocumentReferences.add(dref);
                    }
                }
             }
             fireTableDataChanged();
         }
         
        private String[] columns = { /*"Ref Name" ,*/ "Reference To", "Reference Type", "Reference Text" };
        private Class[] column_class = { /*String.class,*/ String.class, String.class, String.class };
        
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

        public Object getValueAt(int row, int col) {
           DocumentInternalReference rfObj = filteredDocumentReferences.get(row);
           //DocumentInternalReference rfObj = documentReferences.get(keys[row]);
           switch (col) {
              // case 0: 
              //     return rfObj.Name;
               case 0:
                   return rfObj.ParentType;
               case 1:
                   return rfObj.ReferenceType;
               case 2:
                   return rfObj.ReferenceText;
               default:
                   return rfObj.Name;
           }
        }
         
        public DocumentInternalReference getRowData (int row) {
            return this.filteredDocumentReferences.get(row);
        } 
        
 
     }

     class DocumentInternalReference implements Cloneable {
         //name of the reference preceded by rf:
         String Name;
         String ReferenceText;
         String ReferenceType;
         String ParentType;
         String ContainerSection;
         
         private String getReferenceType(String name) {
             if (name.startsWith(OOoNumberingHelper.INTERNAL_REF_PREFIX + OOoNumberingHelper.HEADING_REF_PREFIX)) {
                 return "heading";
             }
             if (name.startsWith(OOoNumberingHelper.INTERNAL_REF_PREFIX + OOoNumberingHelper.NUMBER_REF_PREFIX)) {
                 return "number";
             } else 
                 return "other";
         }
         
         DocumentInternalReference(String name, String refText, String cSection) {
             Name = name;
             ReferenceText = refText;
             ReferenceType =  getReferenceType(name);
             ContainerSection = cSection;
            // ParentType = parentType;
         }
         
         public String getActualReferenceName(){
             int nNameIndex = Name.indexOf(":");
             if (nNameIndex != -1)
                return Name.substring(nNameIndex+1);
             else
                 return Name;
         }
         
        @Override
         protected Object clone() throws CloneNotSupportedException{
             DocumentInternalReference cloneRef = (DocumentInternalReference) super.clone();
             return cloneRef;
         }
         
         public void setParentType(String ppType) {
             ParentType = ppType;
         }
     }

     private void filterTableModel(){
         String fieldFilter = this.txtFilterReferences.getText();
         ReferencesTableModel model = (ReferencesTableModel) this.tblAllReferences.getModel();
         model.filterByType(fieldFilter);
     }
     
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnInsertCrossRef = new javax.swing.JButton();
        btnBrowseBroken = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllReferences = new javax.swing.JTable();
        txtFilterReferences = new javax.swing.JTextField();
        lblFilterReferences = new javax.swing.JLabel();
        cboFilterSettings = new javax.swing.JComboBox();
        btnRefresh = new javax.swing.JButton();

        btnInsertCrossRef.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnInsertCrossRef.setText("Insert Cross Ref");
        btnInsertCrossRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertCrossRefActionPerformed(evt);
            }
        });

        btnBrowseBroken.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnBrowseBroken.setText("Browse Broken ");
        btnBrowseBroken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseBrokenActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tblAllReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tblAllReferences.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAllReferences.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblAllReferences);

        txtFilterReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        txtFilterReferences.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFilterReferencesKeyPressed(evt);
            }
        });

        lblFilterReferences.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        lblFilterReferences.setText("Filter References");

        cboFilterSettings.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        cboFilterSettings.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnRefresh.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnInsertCrossRef, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowseBroken, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFilterReferences, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addGap(136, 136, 136))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFilterReferences, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboFilterSettings, 0, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lblFilterReferences)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilterReferences, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh)
                    .addComponent(cboFilterSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(btnInsertCrossRef)
                    .addComponent(btnBrowseBroken))
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btnInsertCrossRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertCrossRefActionPerformed
// TODO add your handling code here:
     applyInsertCrossReference();
}//GEN-LAST:event_btnInsertCrossRefActionPerformed

private void btnBrowseBrokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseBrokenActionPerformed
// TODO add your handling code here:
   // parentFrame.dispose();
}//GEN-LAST:event_btnBrowseBrokenActionPerformed

private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
// TODO add your handling code here:
    parentFrame.dispose();
}//GEN-LAST:event_btnCloseActionPerformed

private void txtFilterReferencesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilterReferencesKeyPressed
// TODO add your handling code here:
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){
       filterTableModel();
    }
    
}//GEN-LAST:event_txtFilterReferencesKeyPressed

private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
// TODO add your handling code here:
    ReferencesTableModel model = (ReferencesTableModel) this.tblAllReferences.getModel();
    model.resetModel();
}//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseBroken;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnInsertCrossRef;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox cboFilterSettings;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFilterReferences;
    private javax.swing.JTable tblAllReferences;
    private javax.swing.JTextField txtFilterReferences;
    // End of variables declaration//GEN-END:variables

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

}
