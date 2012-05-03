package org.bungeni.editor.panels.toolbar;

import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.CommonEditorFunctions;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

/**
 * Reads the toolbar XML - translates the <blockAction> as tabs and <action> and <subaction> as buttons
 * in the tab
 * @author Ashok Hariharan
 */
public class BungeniToolbarLoader {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniToolbarLoader.class.getName());

    private ActionListener actionListener = null;
    private String iso3Language = "";
    private XMLOutputter xmlOutputter = new XMLOutputter();
    /**
     * Creates a BungeniToolbarLoader
     * The action listener for the buttons is passed as parameter
     * @param toolbarActionListener
     */
    public BungeniToolbarLoader(ActionListener toolbarActionListener) {
        //instantiate a toolbar parser
        actionListener = toolbarActionListener;
        Locale defLocale = Locale.getDefault();
        //we capture the iso3 default language for the current locale
        //xml config uses iso3 language representation
        iso3Language = defLocale.getISO3Language();

    }

 
    /**
     * Adds an action to a buttonContainerPanel
     * @param buttonContainer
     * @param action
     */
    private void addActionToPanel(buttonContainerPanel buttonContainer, Element action, Element refAction) {
        String actionTitle = CommonXmlUtils.getLocalizedChildElementValue(action, "title");
        if (refAction != null) {
            if (refAction.getChildren("title").size() > 0 ) {
                //use title from refAction
                actionTitle = CommonXmlUtils.getLocalizedChildElementValue(refAction, "title");
            }
        }
        String actionTooltip = CommonXmlUtils.getLocalizedChildElementValue(action, "tooltip");
        BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
        buttonPanel panelButton = new buttonPanel(actionTitle, actionTooltip, actionListener, elem);
        buttonContainer.add(panelButton);
    }
    /**
     * Load the tabs from the toolbar xml config
     * @param thisPane - the JTabbedPane object which acts as the container for the ations
     */
    public void loadToolbar(JTabbedPane thisPane ) {
        //first build the toolbar - and then we processs the xml
       
        thisPane.setBorder(BorderFactory.createEmptyBorder());
       // ArrayList<JTabbedPane> groupTabs = new ArrayList<JTabbedPane>(0);
        //get the tab elements
        ArrayList<Element> groupTabs = BungeniToolbarParser.getInstance().getTabActionGroups();
        for (Element groupTab : groupTabs) {
            String grpTabTitle =  CommonXmlUtils.getLocalizedChildElementValue(groupTab, "title");//i18n groupTab.getAttributeValue("title");
            String grpTabUImodel = groupTab.getAttributeValue("uimodel");
            //create a new group tab
            JTabbedPane grpPane = new JTabbedPane(JTabbedPane.TOP);
            grpPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            grpPane.setTabLayoutPolicy(grpTabUImodel.equals("wrap")?JTabbedPane.WRAP_TAB_LAYOUT:JTabbedPane.SCROLL_TAB_LAYOUT);
            grpPane.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
            // thisPane.addTab(grpTabTitle, grpPane);
            ArrayList<Element> tabs = BungeniToolbarParser.getInstance().getTabElements(groupTab);
        //iterate through the tab elements
             for (Element tab : tabs) {
                 //get the tab title
                 String tabTitle =  CommonXmlUtils.getLocalizedChildElementValue(tab, "title"); //i18n tab.getAttributeValue("title");
                 //get the available actions for the tab
                  ArrayList<Element> actionElements = BungeniToolbarParser.getInstance().getTabActionElements(tab);
                  //check if the tab has any actions -- we add a tab only when it has actions
                  if (actionElements.size()  > 0 ) {
                     //create the panel for the tab
                     scrollPanel scrollablePanel = new scrollPanel();
                     //create the button container to embed into the scrollablePanel()
                     //we pass the row size for the buttonContainerPanel()
                     buttonContainerPanel buttonContainer = new buttonContainerPanel(actionElements.size());
                     //now add the button actions to the button container panel
                     for (Element action : actionElements) {
                        // an action can be included in a different action group by using the ref attribute e.g.
                        // <action ref="#speech.create" /> refers to the action called speech.create
                        // The referenced action allows overriding the title :
                        //  <action ref="#speech.create">
                        //    <title xml:lang="eng">Overriden title </title>
                        //  </action>

                        Attribute refAttr = action.getAttribute("ref");
                        if (refAttr != null) {
                            String sRef = refAttr.getValue();
                            String origId = sRef.replace("#", "");
                            Element origAction = null;
                            origAction = BungeniToolbarParser.getInstance().getTabActionElementByName(origId);
                            if (origAction != null) {
                                this.addActionToPanel(buttonContainer, origAction, action);
                            } else {
                                log.error("Original action could not be added as a refernce action : " + origAction);
                            }
                        } else {
                            this.addActionToPanel(buttonContainer, action, null);

                        /**
                        //get the title for the button
                            String actionTitle = geti18nTitle(action, "title"); //action.getAttributeValue("title");
                            String actionTooltip = geti18nTooltip(action, "tooltip");
                            BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
                            buttonPanel panelButton = new buttonPanel(actionTitle, actionTooltip, actionListener, elem);
                            buttonContainer.add(panelButton); **/
                       }
                     }
                     scrollablePanel.setScrollViewPort(buttonContainer);
                     scrollablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                     grpPane.addTab(tabTitle, scrollablePanel);
                  }
             }
             thisPane.addTab(grpTabTitle, grpPane);
             CommonEditorFunctions.compOrientation(thisPane);
        }
    }

}
