/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.CommonFormFunctions;
import org.bungeni.utils.BungeniFileSavePathFormat;

/**
 *
 * @author undesa
 */
public abstract class BaseEditorDocMetadataDialog extends javax.swing.JPanel implements IEditorDocMetadataDialog {

    protected OOComponentHelper ooDocument;
    protected JFrame parentFrame;
    protected SelectorDialogModes theMode;
    protected  ArrayList<CountryCode> countryCodes = new ArrayList<CountryCode>(0);
    protected String tabTitle;
    protected  ArrayList<LanguageCode> languageCodes = new ArrayList<LanguageCode>(0);
    protected  ArrayList<DocumentPart> documentParts = new ArrayList<DocumentPart>(0);
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BaseEditorDocMetadataDialog.class.getName());
    protected SimpleDateFormat sdfDateFormat ;
    protected SimpleDateFormat sdfTimeFormat;

   
    public BaseEditorDocMetadataDialog(){
        super();
        sdfDateFormat = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataDateFormat"));
        sdfTimeFormat = new SimpleDateFormat(BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
    }

    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) {
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        theMode = dlgMode;
    }

    public void initialize(){
        countryCodes.add(new CountryCode("ke", "Kenya"));
        countryCodes.add(new CountryCode("ug", "Uganda"));
        countryCodes.add(new CountryCode("tz", "Tanzania"));
        
        languageCodes.add(new LanguageCode("eng", "English"));
        languageCodes.add(new LanguageCode("fra", "French"));
        
        documentParts.add(new DocumentPart("main", "Main"));
        documentParts.add(new DocumentPart("annex", "Annex"));
        documentParts.add(new DocumentPart("attachment", "Attachment"));
        
    }

    abstract public Component getPanelComponent() ;
    abstract public Dimension getFrameSize();
    
    public void setTabTitle(String tt) {
        this.tabTitle = tt;
    }
    
    public String getTabTitle(){
        return tabTitle;
    }
    
    
       protected CountryCode findCountryCode (String countryCode) {
        for (CountryCode c : countryCodes) {
            if (c.countryCode.equals(countryCode)) {
                return c;
            }
        }
        return null;
    }
    
    protected LanguageCode findLanguageCode(String langCode) {
        for (LanguageCode lc : languageCodes){
            if (lc.languageCode.equals(langCode)){
                return lc;
            }
        }
        return null;
    }
    
    protected DocumentPart findDocumentPart(String documentPart) {
        for (DocumentPart dp : documentParts) {
            if (dp.PartName.equals(documentPart)){
                return dp;
            }
        }
        return null;
    }

  protected Component findComponentByName(Container container, String componentName) {
  for (Component component: container.getComponents()) {
    if (componentName.equals(component.getName())) {
      return component;
    }
    if (component instanceof JRootPane) {
      // According to the JavaDoc for JRootPane, JRootPane is
      // "A lightweight container used behind the scenes by JFrame,
      // JDialog, JWindow, JApplet, and JInternalFrame.". The reference
      // to the RootPane is set up by implementing the RootPaneContainer
      // interface by the JFrame, JDialog, JWindow, JApplet and
      // JInternalFrame. See also the JavaDoc for RootPaneContainer.
      // When a JRootPane is found, recurse into it and continue searching.
      JRootPane nestedJRootPane = (JRootPane)component;
      return findComponentByName(nestedJRootPane.getContentPane(), componentName);
    }
    if (component instanceof JPanel) {
      // JPanel found. Recursing into this panel.
      JPanel nestedJPanel = (JPanel)component;
      return findComponentByName(nestedJPanel, componentName);
    }
  }
  return null;
}


    protected TreeMap<String,Component> fieldsToValidate = new TreeMap<String, Component>() ;

    protected void addFieldsToValidate(TreeMap<String,Component> validFieldsMap) {
        this.fieldsToValidate = validFieldsMap;
    }

    public ArrayList<String> validateSelectedMetadata(BungeniFileSavePathFormat spf) {
         ArrayList<String> errors = new ArrayList<String>(0);
         Set<String> fieldsToCheck = fieldsToValidate.keySet();
         Iterator<String> iterFields = fieldsToCheck.iterator();
         while (iterFields.hasNext()) {
             String fieldName = iterFields.next();
             boolean bState = CommonFormFunctions.validateField(fieldsToValidate.get(fieldName), fieldName);
             if (bState == false) {
                 errors.add(fieldName + ": is required and was empty ");
             }
         }
         return errors;
    }

   
}
