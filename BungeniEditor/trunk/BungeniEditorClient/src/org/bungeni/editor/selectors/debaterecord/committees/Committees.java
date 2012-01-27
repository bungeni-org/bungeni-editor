

package org.bungeni.editor.selectors.debaterecord.committees;

import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.awt.Component;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.Committee;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.BungeniRegistryFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.RegistryQueryFactory;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.CommonConnectorFunctions;
import org.bungeni.extutils.CommonStringFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author  Ashok Hariharan
 */
public class Committees extends BaseMetadataPanel {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Committees.class.getName());

    /** Creates new form TabledDocuments */
    public Committees() {
        super();
        initComponents();
        initTable();
    }
    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/committees/Bundle");


    class CommitteesModel extends DefaultTableModel {
        private boolean cellsEditable = false;

        public CommitteesModel(){
            super();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return cellsEditable;
        }

        public void setModelEditable(boolean bState) {
            this.cellsEditable = bState;
        }
    }


    // !+BUNGENI_CONNECTOR (rm, 17-jan 2012) - this method obtains the committes
    // which are then displayed on a JTable for a user to select the
    // relevant committee
    //
    // it however tries to directly access the COMMITTEE table but is being
    // edited to use the BungeniConnector to access the table instead
    private void initTable(){
        // create the columns vector
        String [] columns = {"Id", "Name", "URI", "Country"} ;
        Vector <String> columnNames = new Vector<String>(Arrays.asList(columns));

        // initialise the Bungeni Connector client
        BungeniConnector client = null;
        try {
            // initialize the data store client
            client = CommonConnectorFunctions.getDSClient();

            // get the committees information
            List<Committee> committeesList = client.getCommittees();

            // get all the committee names and details for all
            // of these
            // declare variable to store result
            Vector<Vector<String>> resultRows = new Vector<Vector<String>>();

            for (int i = 0; i < committeesList.size(); i++) {
                
                Committee committee = committeesList.get(i);

                // create variable to store current value
                Vector <String> currRes = new Vector<String>();

                // add the results for each of the fields
                // to the currRes
                currRes.add(committee.getId());
                currRes.add(committee.getName());
                currRes.add(committee.getURI());
                currRes.add(committee.getCountry());

                // add the curr vector to result
                resultRows.add(currRes);
            }

            // create a table model
           CommitteesModel mdl = new CommitteesModel() ;
           mdl.setDataVector(resultRows, columnNames);

            // Add table model to table
            tbl_Committees.setModel(mdl);
            ((CommitteesModel)this.tbl_Committees.getModel()).setModelEditable(false);
            enableButtons(false);

             // set the selection mode
             tbl_Committees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        } catch (IOException ex) {
            log.error("Error initializing the BungeniConnectorClient " + ex) ;
        }

        /**
        HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
            dbInstance.Connect();
            QueryResults qr = dbInstance.QueryResults(getTableQuery());
            dbInstance.EndConnect();
            if (qr != null ) {
                if (qr.hasResults()) {
                    Vector<Vector<String>> resultRows = new Vector<Vector<String>>();
                    resultRows = qr.theResults();
                    CommitteesModel mdl = new CommitteesModel() ;
                    mdl.setDataVector(resultRows, qr.getColumnsAsVector());
                    tbl_Committees.setModel(mdl);
                     ((CommitteesModel)this.tbl_Committees.getModel()).setModelEditable(false);
                     enableButtons(false);
                    }
            }
            tbl_Committees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         **/
     }

    private void enableButtons(boolean b) {
        Enumeration<AbstractButton> buttons = this.grpEditButtons.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton abButton = buttons.nextElement();
            abButton.setEnabled(b);
        }
    }

        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpEditButtons = new javax.swing.ButtonGroup();
        lbl_SelectCommittee = new javax.swing.JLabel();
        scrollTabledDocs = new javax.swing.JScrollPane();
        tbl_Committees = new javax.swing.JTable();

        setName("Form"); // NOI18N

        lbl_SelectCommittee.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/committees/Bundle"); // NOI18N
        lbl_SelectCommittee.setText(bundle.getString("Committees.lbl_SelectCommittee.text")); // NOI18N
        lbl_SelectCommittee.setName("lbl_SelectCommittee"); // NOI18N

        scrollTabledDocs.setName("scrollTabledDocs"); // NOI18N

        tbl_Committees.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
        tbl_Committees.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_Committees.setName("tbl_Committees"); // NOI18N
        scrollTabledDocs.setViewportView(tbl_Committees);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTabledDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_SelectCommittee, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_SelectCommittee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabledDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.ButtonGroup grpEditButtons;
    private javax.swing.JLabel lbl_SelectCommittee;
    protected javax.swing.JScrollPane scrollTabledDocs;
    protected javax.swing.JTable tbl_Committees;
    // End of variables declaration//GEN-END:variables

    public String getPanelName() {
        return java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/committees/Bundle").getString("PanelName");
    }

    public Component getPanelComponent() {
        return this;
    }

      public HashMap<String,ArrayList<String>> getTableSelection() {

         int[] selectedRows = tbl_Committees.getSelectedRows();
         ArrayList<String> committeNames = new ArrayList<String>();
         ArrayList<String> committeeURIs = new ArrayList<String>();
             for (int i=0; i < selectedRows.length; i++) {
                 String docTitle = (String)tbl_Committees.getModel().getValueAt(selectedRows[i], 0 );
                 String docURI = (String) tbl_Committees.getModel().getValueAt(selectedRows[i], 1);
                 committeNames.add(docTitle);
                 committeeURIs.add(docURI);
             }
        HashMap<String,ArrayList<String>> tblData = new HashMap<String,ArrayList<String>>();
        tblData.put("document_titles", committeNames);
        tblData.put("document_uris", committeeURIs);
        return tblData;
    
      }
        
        
    @Override
    public boolean preFullEdit() {
        return true;
    }

    @Override
    public boolean processFullEdit() {
        return true;
    }

    @Override
    public boolean postFullEdit() {
        return true;
    }

    @Override
    public boolean preFullInsert() {
        return true;
    }

    @Override
    public boolean processFullInsert() {
        return true;
    }

    @Override
    public boolean postFullInsert() {
        return true;
    }

    @Override
    public boolean preSelectEdit() {
        return true;
    }

    @Override
    public boolean processSelectEdit() {
        return true;
    }

    @Override
    public boolean postSelectEdit() {
        return true;
    }

   
   
    
    @Override
    public boolean preSelectInsert() {
        HashMap<String,ArrayList<String>> tblData = new HashMap<String,ArrayList<String>>();
        
        
        return true;
    }

    @Override
    public boolean processSelectInsert() {
        // applyBulletedList();
        markupCommittee();
        return true;
    }

    @Override
    public boolean postSelectInsert() {
        return true;
    }

    @Override
    public boolean validateSelectedEdit() {
        return true;
    }

    @Override
    public boolean validateSelectedInsert() {
        //validate the tabled documents
        //all rows in table need to be full.
        boolean bState = true;

        if (this.tbl_Committees.getSelectedRowCount() == 0) {
            addErrorMessage(this.tbl_Committees, bundle.getString("no_row_selected"));
            bState = false;
        }  else {
            //validate selected rows for empty data
            int[] nRows = this.tbl_Committees.getSelectedRows();
            int nCols = this.tbl_Committees.getColumnCount();

            for (int i = 0; i < nRows.length; i++) {
                for (int j = 0; j < nCols; j++) {
                   String sValue = (String) this.tbl_Committees.getValueAt(nRows[i], j);
                   if (sValue.trim().length() == 0) {
                       Object[] values = {Integer.toString(nRows[i]+1), Integer.toString(j+1)};
                       String formattedMsg = MessageFormat.format(bundle.getString("tableValidationError"),
                               values);
                       this.addErrorMessage(this.tbl_Committees, formattedMsg);
                       bState = false;
                   }
                }
            }
        }
        return bState;
    }

    class objCommittee {
        String committeeName ;
        String committeeUri;
        objCommittee() {
            
        }
    }

    private static final String BUNGENI_COMMITTEE_META = "BungeniCommittee.";
    private static final String BUNGENI_COMMITTEE_REF_SEPARATOR = ":";
    private static final String BUNGENI_COMMITTEE_REF_PREFIX = "sCommitteeRef";

    
    private void markupCommittee(){
        //check if selected committee exists in section meta
        //if it does make reference to it
        //if it does not added meta for committe and then make reference
        int nSelectedRow = this.tbl_Committees.getSelectedRow();
        objCommittee aCommittee = new objCommittee();

        aCommittee.committeeName = (String) tbl_Committees.getValueAt(nSelectedRow, 0);
        aCommittee.committeeUri = (String) tbl_Committees.getValueAt(nSelectedRow, 1);
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();

        XTextSection xCurrentSection = ooDoc.currentSection();
        HashMap<String,String> sectionMeta = ooDoc.getSectionMetadataAttributes(xCurrentSection);
        String sMetaValue = aCommittee.committeeName + ";" + aCommittee.committeeUri;
        //String sMetaReference = BUNGENI_COMMITTEE_META + CommonStringFunctions.convertUriForAttrUsage(aCommittee.committeeUri.trim());
        //check if section meta exists
        if (sectionMeta.containsValue(sMetaValue)) {
            //we just add the references
            //get key for value
            String foundKey = "";
            Set<String> committeeKeys = sectionMeta.keySet();
            for (String sCommittee : committeeKeys) {
                String commValue = sectionMeta.get(sCommittee);
                if (commValue.trim().equals(sMetaValue)) {
                    foundKey = sCommittee;
                    break;
                }
            }
            String refString = generateReferenceToCommittee(ooDoc, foundKey);
            addCommitteeReference(ooDoc, refString );
        } else {
            //we append to section meta
            //the key is : bungenicommittee-comitte/uri , value = committee name;
            int i = 1;
            while (sectionMeta.containsKey(BUNGENI_COMMITTEE_META + i)) {
                i++;
            }
            sectionMeta.put(BUNGENI_COMMITTEE_META + i, sMetaValue);
            ooDoc.setSectionMetadataAttributes(xCurrentSection, sectionMeta);
            //now we add the reference
            String refString = generateReferenceToCommittee(ooDoc, BUNGENI_COMMITTEE_META + i);
            addCommitteeReference(ooDoc, refString );

        }

    }

    private String generateReferenceToCommittee (OOComponentHelper ooDoc, String committeeMetaRef) {
        //name of refernce mark
        String sRefMark = BUNGENI_COMMITTEE_REF_PREFIX + BUNGENI_COMMITTEE_REF_SEPARATOR + CommonStringFunctions.makeReferenceFriendlyString(committeeMetaRef, ".");
        int i = 1;
        String newRefNo  = sRefMark + ";#" +i;
        while (ooDoc.referenceExists(newRefNo) ) {
            newRefNo = sRefMark + ";#" + (++i);
        }
        return newRefNo;
    }

    private void addCommitteeReference(OOComponentHelper ooDoc, String referenceString) {
        XTextViewCursor selCursor = ooDoc.getViewCursor();
        ooDoc.insertReferenceMark(selCursor, referenceString);
    }

    @Override
    public boolean validateFullInsert() {
        return true;
    }

    @Override
    public boolean validateFullEdit() {
        return true;
    }

    @Override
    public boolean doCancel() {
        return true;
    }

    @Override
    public boolean doReset() {
        return true;
    }
    @Override
    protected void initFieldsSelectedEdit() {
        return;
    }

    @Override
    protected void initFieldsSelectedInsert() {
      //  BungeniClientDB dbnew = new BungeniClientDB();
        return;
    }

    @Override
    protected void initFieldsInsert() {
        return;
    }

    @Override
    protected void initFieldsEdit() {
        return;
    }
}
