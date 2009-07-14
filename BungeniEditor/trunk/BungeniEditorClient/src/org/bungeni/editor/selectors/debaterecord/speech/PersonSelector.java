
package org.bungeni.editor.selectors.debaterecord.speech;

import com.sun.star.text.XTextSection;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.BungeniRegistryFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.registryQueryDialog;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author  undesa
 */
public class PersonSelector extends  BaseMetadataPanel {
     registryQueryDialog rqs = null;
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PersonSelector.class.getName());
 //    HashMap<String, String> selectionData = new HashMap<String,String>();
     private ArrayList<ObjectPerson> arrPersons = new ArrayList<ObjectPerson>(0);
    /** Creates new form PersonSelector */
    public PersonSelector() {
        super();
        initComponents();

    }
    
    @Override
    public void commonInitFields(){
        initComboSelect();
        //this.cboPersonSelect.addActionListener(new PersonSelect());
      
        this.btn_SpeechBy.setVisible(false);
    }
    
    private ArrayList<ObjectPerson> getPersonObjects(String bypersonId) {
            ArrayList<ObjectPerson> personObjects = new ArrayList<ObjectPerson>(0);
            HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
            dbInstance.Connect();
            String query = "";
            if (bypersonId.length() == 0) {
                query = "Select ID, FIRST_NAME, LAST_NAME, URI, ROLE from persons order by last_name, first_name";
            } else {
                query = "Select ID, FIRST_NAME, LAST_NAME, URI, ROLE from persons where ID='"+ bypersonId + "' order by last_name, first_name";
            }
            QueryResults qr = dbInstance.QueryResults(query);
            dbInstance.EndConnect();
            String personId, personFirstName, personLastName, personURI, personRole;
            if (qr.hasResults()) {
                Vector<Vector<String>> theResults = qr.theResults();
                for (Vector<String> row : theResults) {
                     personId = qr.getField(row, "ID");
                     personFirstName = qr.getField(row, "FIRST_NAME");
                     personLastName = qr.getField(row, "LAST_NAME");
                     personURI = qr.getField(row, "URI");
                     personRole = qr.getField(row, "ROLE");
                     ObjectPerson m = new ObjectPerson(personId, personFirstName, personLastName, personURI, personRole);
                     personObjects.add(m);
                }
            }
        return personObjects;
    }
    
     private void initComboSelect(){
         
         //ArrayList<ObjectPerson> personObjects = new ArrayList<ObjectPerson>(0);
         /*
            Vector<ObjectPerson> personObjects = new Vector<ObjectPerson>();
           HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
            BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
            dbInstance.Connect();
            QueryResults qr = dbInstance.QueryResults("Select ID, FIRST_NAME, LAST_NAME, URI, ROLE from persons order by last_name, first_name");
            dbInstance.EndConnect();
            String personId, personFirstName, personLastName, personURI, personRole;
            if (qr.hasResults()) {
                Vector<Vector<String>> theResults = qr.theResults();
                for (Vector<String> row : theResults) {
                     personId = qr.getField(row, "ID");
                     personFirstName = qr.getField(row, "FIRST_NAME");
                     personLastName = qr.getField(row, "LAST_NAME");
                     personURI = qr.getField(row, "URI");
                     personRole = qr.getField(row, "ROLE");
                     ObjectPerson m = new ObjectPerson(personId, personFirstName, personLastName, personURI, personRole);
                     personObjects.add(m);
                }
            }
          * */
           this.arrPersons = getPersonObjects("");
           this.cboPersonSelect.addActionListener(new PersonSelect());
            this.cboPersonSelect.setModel(new DefaultComboBoxModel(arrPersons.toArray()));
            AutoCompleteDecorator.decorate(cboPersonSelect);
    }

     class PersonSelect implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            try {
            if (cboPersonSelect.getSelectedIndex() != -1) {
               ObjectPerson selectedPerson = (ObjectPerson)cboPersonSelect.getModel().getSelectedItem();

               HashMap<String,String> selData = new HashMap<String,String>();
               selData.put("ID", selectedPerson.personId);
               selData.put("FIRST_NAME", selectedPerson.firstName);
               selData.put("LAST_NAME", selectedPerson.lastName);
               selData.put("URI", selectedPerson.personURI);
               selData.put("ROLE", selectedPerson.personRole);
                (getContainerPanel()).selectionData = selData;
                if ( (getContainerPanel()).selectionData.size() > 0 ) 
                    (getContainerPanel()).updateAllPanels();
            }
            } catch (Exception ex) {
                log.error("PersonSelect:actionPerformed : " + ex.getMessage());
            }
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

        btn_SpeechBy = new javax.swing.JButton();
        cboPersonSelect = new javax.swing.JComboBox();

        setName("Person Selector"); // NOI18N

        btn_SpeechBy.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/speech/Bundle"); // NOI18N
        btn_SpeechBy.setText(bundle.getString("PersonSelector.btn_SpeechBy.text")); // NOI18N
        btn_SpeechBy.setActionCommand(bundle.getString("PersonSelector.btn_SpeechBy.actionCommand")); // NOI18N
        btn_SpeechBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SpeechByActionPerformed(evt);
            }
        });

        cboPersonSelect.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboPersonSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboPersonSelect, 0, 271, Short.MAX_VALUE)
                    .addComponent(btn_SpeechBy, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboPersonSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_SpeechBy))
        );
    }// </editor-fold>//GEN-END:initComponents

private void btn_SpeechByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SpeechByActionPerformed
// TODO add your handling code here:
        rqs = new registryQueryDialog("Select A Person", "Select ID, FIRST_NAME, LAST_NAME, URI from persons", getParentFrame());
        rqs.show();
        log.debug("Moved on before closing child dialog");
        (getContainerPanel()).selectionData = rqs.getData();
        if ((getContainerPanel()).selectionData.size() > 0 ) {
           // txt_SpeechBy.setText(selectionData.get("FIRST_NAME") + " " + selectionData.get("LAST_NAME"));
           // txt_URIofPerson.setText(selectionData.get("URI"));
            getContainerPanel().updateAllPanels();
        } else {
            log.debug("selected keyset empty");
        }
}//GEN-LAST:event_btn_SpeechByActionPerformed

      public String getPanelName() {
        return getName();
    }

    public Component getPanelComponent() {
        return this;
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
    public boolean preFullEdit() {
        return true;
    }

    @Override
    public boolean processFullEdit() {
         Object selItem = this.cboPersonSelect.getSelectedItem();
        if (selItem != null) {
            if (selItem.getClass().getName().equals(ObjectPerson.class.getName())) {
                ObjectPerson selPerson = (ObjectPerson) selItem;
                HashMap<String,String> sectionmeta = new HashMap<String,String>();
                sectionmeta.put("BungeniPersonID", selPerson.personId);
                sectionmeta.put("BungeniPersonRole", selPerson.personRole);
                OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
                ooDoc.setSectionMetadataAttributes(getContainerPanel().getEditSectionName(), sectionmeta);
            }
        }
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
        return true;
    }

    @Override
    public boolean processSelectInsert() {
        String personId = (getContainerPanel()).selectionData.get("ID");
        String personRole =  (getContainerPanel()).selectionData.get("ROLE");
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        String newSectionName = (getContainerPanel()).mainSectionName;
        sectionMeta.put("BungeniPersonID", personId);
        sectionMeta.put("BungeniPersonRole", personId);
        ooDoc.setSectionMetadataAttributes(newSectionName, sectionMeta);
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
        return true;
    }

    @Override
    public boolean validateFullInsert() {
        return true;
    }

    @Override
    public boolean validateFullEdit() {
        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_SpeechBy;
    private javax.swing.JComboBox cboPersonSelect;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void initFieldsSelectedEdit() {
        return;
    }

    @Override
    protected void initFieldsSelectedInsert() {
        return;
    }

    @Override
    protected void initFieldsInsert() {
        return;
    }

    @Override
    protected void initFieldsEdit() {
          //get the combo model and select the found question
        try {
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        DefaultComboBoxModel model = (DefaultComboBoxModel)this.cboPersonSelect.getModel();
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        XTextSection currentSection  = ooDoc.currentSection();
         if (currentSection != null ) {
             sectionMeta=  ooDoc.getSectionMetadataAttributes(currentSection);
             if (sectionMeta.containsKey("BungeniPersonID")) {
                 String personId =     sectionMeta.get("BungeniPersonID");
                 ArrayList<ObjectPerson> pPerson = getPersonObjects(personId);
                 ObjectPerson oq = pPerson.get(0);
                 int nIndex = findPerson(oq.personId);
                 if (nIndex  != -1) {
                   this.cboPersonSelect.setSelectedIndex(nIndex);
                   //this.cboQuestionSelect.setPopupVisible(true);
                   this.cboPersonSelect.showPopup();
                  // updateQuestionSelection(oq);
                 }
                // this.cboQuestionSelect.setSelectedItem(oq);
             }
         }
        } catch (NullPointerException ex) {
            log.error("initFieldsEdit : " + ex.getMessage());
            log.error("initFieldsEdit : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return; 
        }
        
    }
     private int findPerson (String personId) {
           int i = 0;
        for (ObjectPerson c : arrPersons) {
            if (c.personId.equals(personId)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
