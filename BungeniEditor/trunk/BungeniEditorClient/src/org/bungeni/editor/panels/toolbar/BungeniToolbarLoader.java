
package org.bungeni.editor.panels.toolbar;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import org.bungeni.extutils.BungeniEditorProperties;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Reads the toolbar XML - translates the <blockAction> as tabs and <action> and <subaction> as buttons
 * in the tab
 * @author Ashok Hariharan
 */
public class BungeniToolbarLoader {

    private BungeniToolbarParser toolbarParser = null;
    private ActionListener actionListener = null;
    private String iso3Language = "";

    /**
     * Creates a BungeniToolbarLoader
     * The action listener for the buttons is passed as parameter
     * @param toolbarActionListener
     */
    public BungeniToolbarLoader(ActionListener toolbarActionListener) {
        //instantiate a toolbar parser
        toolbarParser = new BungeniToolbarParser();
        actionListener = toolbarActionListener;
        Locale defLocale = Locale.getDefault();
        //we capture the iso3 default language for the current locale
        //xml config uses iso3 language representation
        iso3Language = defLocale.getISO3Language();

    }


    /**
     * Retrieves the localized title for toolbar actions.
     * Localized titles are entered in a element called 'title' instead of as an attribute
     *
     * <subaction.... >
     *      <title xml:lang="fra">French Action Title</title>
     *      <title xml:lang="eng">English Action Title</title>
     * </subaction ....>
     * 
     * @param anElement
     * @param localizedAttr
     * @return
     */
    private String geti18nTitle(Element anElement, String localizedAttr ) {
        List<Element> childTitles = anElement.getChildren(localizedAttr);
        //get the default
        String langCodeDefault = BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
        String foundTitle = _findi18nTitle(childTitles, this.iso3Language);
        if (foundTitle.equals("UNDEFINED")) {
            foundTitle = _findi18nTitle(childTitles, langCodeDefault );
        }
        return foundTitle;
    }

    private String _findi18nTitle(List<Element> childTitles, String langCode) {
         for (Element title : childTitles) {
           String foundLang =  title.getAttributeValue("lang", Namespace.XML_NAMESPACE);
           if (foundLang.equals(langCode)) {
               return title.getText();
           }
        }
       return "UNDEFINED";
    }
    /**
     * Load the tabs from the toolbar xml config
     * @param thisPane - the JTabbedPane object which acts as the container for the ations
     */
    public void loadToolbar(JTabbedPane thisPane ) {
        //first build the toolbar - and then we processs the xml
        toolbarParser.buildToolbar();
        thisPane.setBorder(BorderFactory.createEmptyBorder());
       // ArrayList<JTabbedPane> groupTabs = new ArrayList<JTabbedPane>(0);
        //get the tab elements
        ArrayList<Element> groupTabs = toolbarParser.getTabActionGroups();
        for (Element groupTab : groupTabs) {
            String grpTabTitle =  geti18nTitle(groupTab, "title");//i18n groupTab.getAttributeValue("title");
            String grpTabUImodel = groupTab.getAttributeValue("uimodel");
            //create a new group tab
            JTabbedPane grpPane = new JTabbedPane(JTabbedPane.TOP);
            grpPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            grpPane.setTabLayoutPolicy(grpTabUImodel.equals("wrap")?JTabbedPane.WRAP_TAB_LAYOUT:JTabbedPane.SCROLL_TAB_LAYOUT);
            grpPane.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
            // thisPane.addTab(grpTabTitle, grpPane);
            ArrayList<Element> tabs = toolbarParser.getTabElements(groupTab);
        //iterate through the tab elements
             for (Element tab : tabs) {
                  //get the available actions for the tab
                  ArrayList<Element> actionElements = toolbarParser.getTabActionElements(tab);
                  //check if the tab has any actions -- we add a tab only when it has actions
                  if (actionElements.size()  > 0 ) {
                     //get the tab title
                     String tabTitle =  geti18nTitle(tab, "title"); //i18n tab.getAttributeValue("title");
                     //create the panel for the tab
                     scrollPanel scrollablePanel = new scrollPanel();
                     //create the button container to embed into the scrollablePanel()
                     //we pass the row size for the buttonContainerPanel()
                     buttonContainerPanel buttonContainer = new buttonContainerPanel(actionElements.size());
                     //now add the button actions to the button container panel
                     for (Element action : actionElements) {
                        //get the title for the button
                        String actionTitle = geti18nTitle(action, "title"); //action.getAttributeValue("title");
                        BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
                        buttonPanel panelButton = new buttonPanel(actionTitle, actionListener, elem);
                        buttonContainer.add(panelButton);
                     }
                     scrollablePanel.setScrollViewPort(buttonContainer);
                     scrollablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());                     grpPane.addTab(tabTitle, scrollablePanel);
                  }
             }
             thisPane.addTab(grpTabTitle, grpPane);
        }
    }

}
