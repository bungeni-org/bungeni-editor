/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.bungeni.editor.selectors.SelectorDialogModes;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public abstract class BaseEditorDocMetadataDialog extends javax.swing.JPanel implements IEditorDocMetadataDialog {

    protected OOComponentHelper ooDocument;
    protected JFrame parentFrame;
    protected SelectorDialogModes theMode;
    protected  ArrayList<CountryCode> countryCodes = new ArrayList<CountryCode>(0);
    protected  ArrayList<LanguageCode> languageCodes = new ArrayList<LanguageCode>(0);
    protected  ArrayList<DocumentPart> documentParts = new ArrayList<DocumentPart>(0);
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BaseEditorDocMetadataDialog.class.getName());
 
    public BaseEditorDocMetadataDialog(){
        super();
    }

    public void initVariables(OOComponentHelper ooDoc, JFrame parentFrm, SelectorDialogModes dlgMode) {
        ooDocument = ooDoc;
        parentFrame = parentFrm;
        theMode = dlgMode;
    }

    public void initialize(){
        countryCodes.add(new CountryCode("ken", "Kenya"));
        countryCodes.add(new CountryCode("uga", "Uganda"));
        countryCodes.add(new CountryCode("tza", "Tanzania"));
        
        languageCodes.add(new LanguageCode("eng", "English"));
        languageCodes.add(new LanguageCode("fra", "French"));
        
        documentParts.add(new DocumentPart("main", "Main"));
        documentParts.add(new DocumentPart("annex", "Annex"));
        documentParts.add(new DocumentPart("attachment", "Attachment"));
        
    }

    abstract public Component getPanelComponent() ;
    
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


}
