
package org.bungeni.editor.selectors.debaterecord.speech;

import java.awt.Component;
import java.util.HashMap;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author  undesa
 */
public class SpeechBy extends  BaseMetadataPanel {

    /** Creates new form PersonSelector */
    public SpeechBy() {
        super();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_SpeechBy = new javax.swing.JLabel();
        txt_SpeechBy = new javax.swing.JTextField();

        setName("Speech By"); // NOI18N

        lbl_SpeechBy.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/debaterecord/speech/Bundle"); // NOI18N
        lbl_SpeechBy.setText(bundle.getString("SpeechBy.lbl_SpeechBy.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lbl_SpeechBy, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
            .addComponent(txt_SpeechBy, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_SpeechBy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_SpeechBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

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
          OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        String editSection = (getContainerPanel()).getEditSectionName();
        sectionMeta.put("BungeniSpeechBy", this.txt_SpeechBy.getText());
        //sectionMeta.put("BungeniQuestionByURI", this.txtPersonURI.getText());
        ooDoc.setSectionMetadataAttributes(editSection, sectionMeta);      
        
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
        OOComponentHelper ooDoc = getContainerPanel().getOoDocument();
        HashMap<String,String> sectionMeta = new HashMap<String,String>();
        String newSectionName = (getContainerPanel()).mainSectionName;
        sectionMeta.put("BungeniSpeechBy", this.txt_SpeechBy.getText());
        //sectionMeta.put("BungeniQuestionByURI", this.txtPersonURI.getText());
        ooDoc.setSectionMetadataAttributes(newSectionName, sectionMeta);      
        
        return true;
    }

    @Override
    public boolean doUpdateEvent(){
        HashMap<String,String> selectionData = (getContainerPanel()).selectionData;
        if (selectionData.containsKey("FIRST_NAME"))
            this.txt_SpeechBy.setText(selectionData.get("FIRST_NAME") + " " + selectionData.get("LAST_NAME"));
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
    private javax.swing.JLabel lbl_SpeechBy;
    private javax.swing.JTextField txt_SpeechBy;
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
        this.txt_SpeechBy.setText(getSectionMetadataValue("BungeniSpeechBy"));
        return;
    }
}
