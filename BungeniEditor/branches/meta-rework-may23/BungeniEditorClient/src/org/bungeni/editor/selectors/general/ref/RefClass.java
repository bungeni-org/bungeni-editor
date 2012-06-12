
package org.bungeni.editor.selectors.general.ref;

import java.awt.Component;
import java.lang.String;
import java.util.HashMap;
import org.bungeni.editor.selectors.BaseMetadataPanel;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author  undesa
 */
public class RefClass extends  BaseMetadataPanel {

    /** Creates new form PersonSelector */
    public RefClass() {
        super();
        initComponents();
        CommonUIFunctions.compOrientation(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_RefURI = new javax.swing.JLabel();
        cbo_refClass = new javax.swing.JComboBox();

        setName("Speech By"); // NOI18N

        lbl_RefURI.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/bungeni/editor/selectors/general/ref/Bundle"); // NOI18N
        lbl_RefURI.setText(bundle.getString("RefClass.lbl_RefURI.text")); // NOI18N

        cbo_refClass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "heading", "subheading", "title", "subtitle", "shorttitle", "alternativetitle" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_RefURI, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
            .addComponent(cbo_refClass, 0, 241, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_RefURI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbo_refClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        //!+FIX_THIS(ah, may-2012) The edit mode metadata processing needs to be fixed.
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
          final String strClass = (String) this.cbo_refClass.getSelectedItem();
          HashMap<String,String> paragraphMetaMap = new HashMap<String,String>(){
                {
                    put("BungeniAnnotationType", "class");
                    put("BungeniRefClass", strClass );
                }
          };

          ooDoc.setCurrentParagraphAttributes(paragraphMetaMap);
        return true;
    }

    @Override
    public boolean doUpdateEvent(){
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
    private javax.swing.JComboBox cbo_refClass;
    private javax.swing.JLabel lbl_RefURI;
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
        //!+FIX_THIS (ah, may-2012) Fix this for inline metadata editing
        //this.txt_RefURI.setText(getSectionMetadataValue("BungeniRefURI"));
        return;
    }
}
