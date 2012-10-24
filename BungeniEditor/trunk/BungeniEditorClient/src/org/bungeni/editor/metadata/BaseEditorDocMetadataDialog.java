package org.bungeni.editor.metadata;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.utils.BungeniEditorProperties;
import org.bungeni.extutils.CommonFormFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.BungeniFileSavePathFormat;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.Dimension;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;

/**
 *
 * @author Ahsok
 */
public abstract class BaseEditorDocMetadataDialog extends javax.swing.JPanel implements IEditorDocMetadataDialog {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BaseEditorDocMetadataDialog.class.getName());
    protected CountryCode[]     countryCodes     =  {};
    protected LanguageCode[]    languageCodes    =  {};
    protected TreeMap<String, Component> fieldsToValidate = new TreeMap<String, Component>();
    protected DocumentPart[]    documentParts    = {};
    protected OOComponentHelper          ooDocument;
    protected JFrame                     parentFrame;
    protected SimpleDateFormat           sdfDateFormat;
    protected SimpleDateFormat           sdfTimeFormat;
    protected String                     tabTitle;
    protected SelectorDialogModes        theMode;

    public BaseEditorDocMetadataDialog() {
        super();
        sdfDateFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
        sdfTimeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
    }

    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) {
        ooDocument  = ooDoc;
        parentFrame = parentFrm;
        theMode     = dlgMode;
    }

    public void initialize() {

        /**
         * Hard code this for now .. in the future this will have to load from the settings db
         */
        //get country codes from
        //AH-09-02-11
        countryCodes = CountryCodesFactory.getAvailableCountryCodes();
        //AH-09-02-11
       
        //AH-09-02-11
        languageCodes = LanguageCodesFactory.getAvailableLanguageCodes();
       
        //AH-10-02-11
        documentParts = DocumentPartsFactory.getAvailableDocumentParts();
    }

    abstract public Component getPanelComponent();

    abstract public Dimension getFrameSize();

    public void setTabTitle(String tt) {
        this.tabTitle = tt;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    protected CountryCode findCountryCode(String countryCode) {
        for (CountryCode c : countryCodes) {
            if (c.getCountryCodeLower().equals(countryCode)) {
                return c;
            }
        }

        return null;
    }

    protected LanguageCode findLanguageCodeAlpha2(String langCode) {
        for (LanguageCode lc : languageCodes) {
            if (lc.getLanguageCodeAlpha2().equals(langCode)) {
                return lc;
            }
        }

        return null;
    }

    protected DocumentPart findDocumentPart(String documentPart) {
        for (DocumentPart dp : documentParts) {
            if (dp.getPartName().equals(documentPart)) {
                return dp;
            }
        }

        return null;
    }

    protected void addFieldsToValidate(TreeMap<String, Component> validFieldsMap) {
        this.fieldsToValidate = validFieldsMap;
    }

    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
        ArrayList<String> errors        = new ArrayList<String>(0);
        Set<String>       fieldsToCheck = fieldsToValidate.keySet();
        Iterator<String>  iterFields    = fieldsToCheck.iterator();

        while (iterFields.hasNext()) {
            String  fieldName = iterFields.next();
            boolean bState    = CommonFormFunctions.validateField(fieldsToValidate.get(fieldName), fieldName);

            if (bState == false) {
                errors.add(fieldName + ": is required and was empty ");
            }
        }

        return errors;
    }
}
