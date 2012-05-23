package org.bungeni.editor.selectors;

//~--- non-JDK imports --------------------------------------------------------



import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;

import java.awt.Component;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Ashok Hariharan
 */
public abstract class BaseMetadataPanel extends JPanel implements IMetadataPanel {
    private static org.apache.log4j.Logger                        log                =
        org.apache.log4j.Logger.getLogger(BaseMetadataPanel.class.getName());
    private HashMap<String, Object>                               thePreInsertMap    = new HashMap<String, Object>();
   // protected HashMap<SelectorDialogModes, BungeniCatalogCommand> theCatalogCommands = new HashMap<SelectorDialogModes,
     //                                                                                      BungeniCatalogCommand>();
    private ArrayList<String>          fieldsWithNames = new ArrayList<String>(0);
    private HashMap<String, Boolean>   fieldNamesMap   = new HashMap<String, Boolean>();
    private BaseMetadataContainerPanel containerPanel;
    private BungeniFormContext         formContext;

    public BaseMetadataPanel() {
        super();
    }

    public void initVariables(BaseMetadataContainerPanel panel) {
        this.containerPanel = panel;

        String popupDlgBackColor = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");

        this.setBackground(java.awt.Color.decode(popupDlgBackColor));
        createContext();
        initFields();
        setFieldNamesMap();
    }

    private void setFieldNamesMap() {
        this.fieldsWithNames = CommonUIFunctions.findComponentsWithNames(this);

        for (String sField : fieldsWithNames) {
            this.fieldNamesMap.put(sField, Boolean.TRUE);
        }
    }

    private void createContext() {
        formContext = new BungeniFormContext();
        // getFormContext().setTheAction(getTheAction());
        getFormContext().setTheSubAction(getTheSubAction());
        getFormContext().setOoDocument(getOoDocument());
        getFormContext().setPreInsertMap(getThePreInsertMap());
    }

    public void commonInitFields() {}

    private void initFields() {
        switch (getDialogMode()) {
        case TEXT_SELECTED_EDIT :
            commonInitFields();
            initFieldsSelectedEdit();

            return;

        case TEXT_SELECTED_INSERT :
            commonInitFields();
            initFieldsSelectedInsert();

            return;

        case TEXT_INSERTION :
            commonInitFields();
            initFieldsInsert();

            return;

        case TEXT_EDIT :
            commonInitFields();
            initFieldsEdit();

            return;

        default :
            return;
        }
    }

    /**
     * The following functions are called upon initalization of the panel, depening on the current mode,
     * the appropriate function is called.
     */
    abstract protected void initFieldsSelectedEdit();

    abstract protected void initFieldsSelectedInsert();

    abstract protected void initFieldsInsert();

    abstract protected void initFieldsEdit();

    abstract public String getPanelName();

    abstract public Component getPanelComponent();

    /** helper functions to get variables from parent container */
    public BaseMetadataContainerPanel getContainerPanel() {
        return containerPanel;
    }

    public OOComponentHelper getOoDocument() {
        return getContainerPanel().getOoDocument();
    }

    public JFrame getParentFrame() {
        return getContainerPanel().getParentFrame();
    }

    /**
    public toolbarAction getTheAction() {
        return getContainerPanel().getTheAction();
    }
    **/

    public toolbarAction getTheSubAction() {
        return getContainerPanel().getTheSubAction();
    }

    public SelectorDialogModes getDialogMode() {
        return getContainerPanel().getDialogMode();
    }


    public void addErrorMessage(java.awt.Component p, String msg) {
        getContainerPanel().addErrorMessage(this, p, msg);
    }

    public String ErrorMessagesAsString() {
        return getContainerPanel().ErrorMessagesAsString();
    }

    public boolean doApply() {
        switch (getDialogMode()) {
        case TEXT_SELECTED_EDIT :
            return applySelectEdit();

        case TEXT_SELECTED_INSERT :
            return applySelectInsert();

        case TEXT_EDIT :
            return applyFullEdit();

        case TEXT_INSERTION :
            return applyFullInsert();

        default :
            return true;
        }
    }

    public boolean applyFullEdit() {
        if (validateFields() == false) {
            return false;
        }

        if (preFullEdit() == false) {
            return false;
        }

        if (processFullEdit() == false) {
            return false;
        }

        if (postFullEdit() == false) {
            return false;
        }

        return true;
    }

    abstract public boolean preFullEdit();

    abstract public boolean processFullEdit();

    abstract public boolean postFullEdit();

    public boolean applyFullInsert() {

        // if (validateFields() == false) {
        // return false;
        // }
        if (preFullInsert() == false) {
            return false;
        }

        if (processFullInsert() == false) {
            return false;
        }

        if (postFullInsert() == false) {
            return false;
        }

        return true;
    }

    abstract public boolean preFullInsert();

    abstract public boolean processFullInsert();

    abstract public boolean postFullInsert();

    public boolean applySelectEdit() {

        // if (validateFields() == false) {
        // return false;
        // }
        if (preSelectEdit() == false) {
            return false;
        }

        if (processSelectEdit() == false) {
            return false;
        }

        if (postSelectEdit() == false) {
            return false;
        }

        return true;
    }

    abstract public boolean preSelectEdit();

    abstract public boolean processSelectEdit();

    abstract public boolean postSelectEdit();

    public boolean applySelectInsert() {

        // /if (validateFields() == false) {
        // return false;
        // }
        if (preSelectInsert() == false) {
            return false;
        }

        if (processSelectInsert() == false) {
            return false;
        }

        if (postSelectInsert() == false) {
            return false;
        }

        return true;
    }

    abstract public boolean preSelectInsert();

    abstract public boolean processSelectInsert();

    abstract public boolean postSelectInsert();

    public boolean doValidate() {
        return validateFields();
    }

    // !+ACTION_RECONF (rm, jan 2012) - refactored code to determine the
    // value of the SelectorDialogModes returned using debugger
    protected boolean validateFields() {
        SelectorDialogModes s = getDialogMode();
        log.info("In ValidatorFields [s] : " + s.toString());
        switch (s) {
        case TEXT_SELECTED_EDIT :
            return validateSelectedEdit();

        case TEXT_SELECTED_INSERT :
            return validateSelectedInsert();

        case TEXT_EDIT :
            return validateFullEdit();

        case TEXT_INSERTION :
            return validateFullInsert();

        default :
            return true;
        }
    }

    abstract public boolean validateSelectedEdit();

    abstract public boolean validateSelectedInsert();

    abstract public boolean validateFullInsert();

    abstract public boolean validateFullEdit();

    abstract public boolean doCancel();

    abstract public boolean doReset();

    public BungeniFormContext getFormContext() {
        return formContext;
    }

    public HashMap<String, Object> getThePreInsertMap() {
        return thePreInsertMap;
    }


    public boolean doUpdateEvent() {
        return true;
    }

    public String getSectionMetadataValue(String valueName) {

        // connect fields to metadata...
        OOComponentHelper       ooDoc          = getContainerPanel().getOoDocument();
        XTextSection            currentSection = ooDoc.currentSection();
        HashMap<String, String> sectionMeta    = new HashMap<String, String>();

        if (currentSection != null) {
            sectionMeta = ooDoc.getSectionMetadataAttributes(currentSection);

            if (sectionMeta.containsKey(valueName)) {
                return sectionMeta.get(valueName);
            }
        }

        // return blank if not found
        return "";
    }

    protected boolean pasteTextIntoDocument(String text) {
        boolean bState = false;

        try {
            XTextRange  xStartRange = getContainerPanel().getOoDocument().getViewCursor().getStart();
            XText       xCursorText = getContainerPanel().getOoDocument().getViewCursor().getText();
            XTextCursor startCur    = xCursorText.createTextCursorByRange(xStartRange);

            xCursorText.insertString(startCur, text, false);
            xCursorText.insertControlCharacter(startCur, com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false);
            bState = true;
        } catch (IllegalArgumentException ex) {
            log.error("pasteTextIntoDocument : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    public void enableChildControls(boolean bState) {
        for (String fieldName : fieldsWithNames) {
            Component cc = CommonUIFunctions.findComponentByName(this, fieldName);

            cc.setEnabled(bState);
        }
    }
}
