package org.bungeni.editor.selectors.debaterecord.question;

import com.sun.star.text.XTextSection;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.Question;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.utils.CommonConnectorFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author  Ashok Hariharan
 */
public class QuestionSelect extends BaseMetadataPanel {

  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(QuestionSelect.class.getName());
    // HashMap<String, String> selectionData = new HashMap<String,String>();

    /** Creates new form QuestionSelect */
    public QuestionSelect() {
        super();
        initComponents();
        //   initComboSelect();
      }

    @Override
    public void commonInitFields() {
        initComboSelect();
    }

    // !+ (rm, feb 2012) - removed ununsed arg in method to fix bug...
//     private Vector<ObjectQuestion> getQuestionObjects(String byQuestionNo) {
    private Vector<ObjectQuestion> getQuestionObjects() {

        Vector<ObjectQuestion> questionObjects = new Vector<ObjectQuestion>();


        // !+BUNGENI_CONNECTOR(reagan,06-01-2012)
        // Changed the Initialization of the BungeniConnector Object
        // to ensure that metadata is accessed using the REST API
        // rather than directly from the datasource
        BungeniConnector client = null;

        try {
            client = CommonConnectorFunctions.getDSClient();

           List<Question> questionsList = client.getQuestions();
           
            for (int i = 0; i < questionsList.size(); i++) {
                Question question = questionsList.get(i);
                ObjectQuestion m = new ObjectQuestion(String.valueOf(question.getId()), question.getTitle(), question.getFrom(), question.getTo(), question.getText());
                questionObjects.add(m);
            }

        } catch (IOException ex) {
            log.error("Error initializing the BungeniConnectorClient " + ex) ;
        }

        
        return questionObjects;

//           HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();
//
//           String qQuery = "";
//           if (byQuestionNo.length() == 0 )
//             qQuery = "Select ID, QUESTION_TITLE, QUESTION_FROM, QUESTION_TO, QUESTION_TEXT  from questions order by question_title " ;
//           else
//             qQuery = "Select ID, QUESTION_TITLE, QUESTION_FROM, QUESTION_TO, QUESTION_TEXT  from questions Where ID = '"+ byQuestionNo+ "' order by question_title " ;
//
//           BungeniClientDB dbInstance = new BungeniClientDB(registryMap);
//            dbInstance.Connect();
//            QueryResults qr = dbInstance.QueryResults(qQuery);
//            dbInstance.EndConnect();
//            String questionId, questionTitle, questionFrom, questionTo, questionText ;
//            if (qr.hasResults()) {
//                Vector<Vector<String>> theResults = qr.theResults();
//                for (Vector<String> row : theResults) {
//                     questionId = qr.getField(row, "ID");
//                     questionTitle = qr.getField(row, "QUESTION_TITLE");
//                     questionFrom = qr.getField(row, "QUESTION_FROM");
//                     questionTo = qr.getField(row, "QUESTION_TO");
//                     questionText = qr.getField(row, "QUESTION_TEXT");
//                    ObjectQuestion m = new ObjectQuestion(questionId, questionTitle, questionFrom, questionTo, questionText);
//                    questionObjects.add(m);
//                }
//            }
//        return questionObjects;
    }

    private void initComboSelect() {
        Vector<ObjectQuestion> questionObjects = new Vector<ObjectQuestion>();
        questionObjects = getQuestionObjects();
        this.cboQuestionSelect.addActionListener(new QuestionSelector());
        this.cboQuestionSelect.setModel(new DefaultComboBoxModel(questionObjects));
        AutoCompleteDecorator.decorate(cboQuestionSelect);
    }

    class QuestionSelector implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            try {

                if (cboQuestionSelect.getSelectedIndex() != -1) {
                    ObjectQuestion selectedQuestion = (ObjectQuestion) cboQuestionSelect.getSelectedItem();
                    updateQuestionSelection(selectedQuestion);

                } else {
                    log.error("QuestionSelect:actionperformed : for -1 index");
                }
            } catch (Exception ex) {
                log.error("QuestionSelector:actionPerformed : " + ex.getMessage());
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

        cboQuestionSelect = new javax.swing.JComboBox();

        setName("Select a Question"); // NOI18N

        cboQuestionSelect.setEditable(true);
        cboQuestionSelect.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        cboQuestionSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboQuestionSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboQuestionSelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboQuestionSelect, 0, 279, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboQuestionSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
    }// </editor-fold>//GEN-END:initComponents

private void cboQuestionSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboQuestionSelectActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_cboQuestionSelectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboQuestionSelect;
    // End of variables declaration//GEN-END:variables

    public String getPanelName() {
        return "Title";
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

    /**
     * Once the user has added all details about an imported question,
     * this method inserts the data about the selected question
     * @return
     */
    @Override
    public boolean processFullEdit() {
        Object selItem = this.cboQuestionSelect.getSelectedItem();
        if (selItem != null) {
            if (selItem.getClass().getName().equals(ObjectQuestion.class.getName())) {
                ObjectQuestion selQuestion = (ObjectQuestion) selItem;
                HashMap<String, String> sectionmeta = new HashMap<String, String>();
                sectionmeta.put("BungeniQuestionNo", selQuestion.questionId);
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
        String questionId = (getContainerPanel()).selectionData.get("ID");
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        HashMap<String, String> sectionMeta = new HashMap<String, String>();
        String newSectionName = (getContainerPanel()).mainSectionName;
        sectionMeta.put("BungeniQuestionNo", questionId);
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
            DefaultComboBoxModel model = (DefaultComboBoxModel) this.cboQuestionSelect.getModel();
            HashMap<String, String> sectionMeta = new HashMap<String, String>();
            XTextSection currentSection = ooDoc.currentSection();
            if (currentSection != null) {
                sectionMeta = ooDoc.getSectionMetadataAttributes(currentSection);
                if (sectionMeta.containsKey("BungeniQuestionNo")) {
                    String questionNo = sectionMeta.get("BungeniQuestionNo");
                    Vector<ObjectQuestion> vQuestion = getQuestionObjects();
                    ObjectQuestion oq = vQuestion.elementAt(Integer.parseInt(questionNo)-1); // offset since JCombobox starts from index 1
                    int nIndex = findItem(oq);
                    if (nIndex != -1) {
                        this.cboQuestionSelect.setSelectedIndex(nIndex);
                        //this.cboQuestionSelect.setPopupVisible(true);
                        this.cboQuestionSelect.showPopup();
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

    private void updateQuestionSelection(ObjectQuestion selectedQuestion) {

        HashMap<String, String> selData = new HashMap<String, String>();
        selData.put("ID", selectedQuestion.questionId);
        selData.put("QUESTION_TITLE", selectedQuestion.questionTitle);
        // selData.put("QUESTION_FROM", selectedQuestion.questionFrom);
        // selData.put("QUESTION_TO", selectedQuestion.questionTo);
        selData.put("QUESTION_TEXT", selectedQuestion.questionText);

        (getContainerPanel()).selectionData = selData;
        if ((getContainerPanel()).selectionData.size() > 0) {
            getContainerPanel().updateAllPanels();
        }
    }

    private int findItem(ObjectQuestion oq) {
        for (int i = 0; i < cboQuestionSelect.getItemCount(); i++) {
            ObjectQuestion oQuestion = (ObjectQuestion) cboQuestionSelect.getItemAt(i);
            if (oQuestion.compare(oq)) {
                return i;
            }
        }
        return -1;
    }
}
