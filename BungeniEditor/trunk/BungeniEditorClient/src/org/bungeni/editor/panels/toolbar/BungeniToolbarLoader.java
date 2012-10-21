package org.bungeni.editor.panels.toolbar;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import org.bungeni.utils.CommonResourceBundleHelperFunctions;
import org.bungeni.extutils.CommonUIFunctions;
import org.japura.gui.CollapsiblePanel;
import org.japura.gui.CollapsibleRootPanel;
import org.jdom.Attribute;
import org.jdom.Element;
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

    public enum toolbarStyleType {
        collapsible,
        tabbed
    };
    
   toolbarStyleType toolbarStyle ;

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

    private String getTitle(Element action) {
        String sTitle = action.getAttributeValue("title");
        if (sTitle != null) {
           return CommonResourceBundleHelperFunctions.getToolbarString(sTitle);
        } else  {
            log.error("Title was set to null in toolbar action !");
            return "";
        }
    }
    
    private String getTooltip(Element action) {
        String sTitle = action.getAttributeValue("tooltip");
        if (sTitle != null) {
           return CommonResourceBundleHelperFunctions.getToolbarString(sTitle);
        } else  {
            log.error("Tooltip was set to null in toolbar action !");
            return "";
        }
    }
 
    /**
     * Adds an action to a buttonContainerPanel
     * @param buttonContainer
     * @param action
     */
    private void addActionToPanel(buttonContainerPanel buttonContainer, Element action, Element refAction) {
        String actionTitle = getTitle(action);
        String actionTooltip = getTooltip(action);
        BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
        buttonPanel panelButton = new buttonPanel(actionTitle, actionTooltip, actionListener, elem);
        buttonContainer.add(panelButton);
    }
    /**
     * Load the tabs from the toolbar xml config
     * @param thisPane - the JTabbedPane object which acts as the container for the actionGroups
     *
     * tabbed Pane
     *   + Tab
     *     + Collapsible Root Pane
     *        - Collapsible Pane
     *        - Collapsible Pane
     *   + Tab
     *
     */
    public void loadToolbar(JTabbedPane thisPane ) {
        //first build the toolbar - and then we processs the xml
       
        thisPane.setBorder(BorderFactory.createEmptyBorder());
      
        Element toolbarRoot = BungeniToolbarParser.getInstance().getToolbarRoot();
        String stoolbarStyle = toolbarRoot.getAttributeValue("style");
        if (stoolbarStyle == null ) {
            stoolbarStyle = "collapsible";
        } else {
            stoolbarStyle = stoolbarStyle.trim();
        }

        this.toolbarStyle = toolbarStyleType.valueOf(stoolbarStyle);
        
        //get the tab elements
        ArrayList<Element> groupTabs = BungeniToolbarParser.getInstance().getTabActionGroups();
        for (Element groupTab : groupTabs) {
            String grpTabTitle =  getTitle(groupTab);//i18n groupTab.getAttributeValue("title");
            // thisPane.addTab(grpTabTitle, grpPane);
            ArrayList<Element> tabs = BungeniToolbarParser.getInstance().getTabElements(groupTab);
            //iterate through the tab elements
            if (stoolbarStyle.equals("collapsible")) {
                CollapsibleRootPanel crp = new CollapsibleRootPanel();
                crp.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                Rectangle rBound = thisPane.getBounds();
                java.awt.Dimension rSize = rBound.getSize();
                rSize.setSize(rSize.getWidth() - 5, rSize.getHeight() - 5);
                rBound.setSize(rSize);
                crp.setBounds(rBound);
                this.renderActionsInCollapsiblePanes(crp, tabs);
                thisPane.addTab(grpTabTitle, crp);
            } else {
                String grpTabUImodel = groupTab.getAttributeValue("uimodel");
                //create a new group tab
                JTabbedPane grpPane = new JTabbedPane(JTabbedPane.TOP);
                grpPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                grpPane.setTabLayoutPolicy(
                        grpTabUImodel.equals("wrap") ?
                            JTabbedPane.WRAP_TAB_LAYOUT :
                            JTabbedPane.SCROLL_TAB_LAYOUT
                );
                grpPane.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
                this.renderActionsInTabs(grpPane, tabs);
                thisPane.addTab(grpTabTitle, grpPane);
            }
            //thisPane.addTab(grpTabTitle, grpPane);
        }
    CommonUIFunctions.compOrientation(thisPane);
    }


    /**
     * These are a list of block actions every block action is rendered as a collapsible panel
     * inside a collapsibel root pane
     *
     * Main Tab
     *   -- Collapsible Root Panel
     *      + CollapsiblePanel
     *           + buttonContainer
     *             - buttonPanel
     *             - buttonPanel
     *      + CollapsiblePanel
     *           + ....
     * 
     * @param grpPane
     * @param tabs
     */
    private void renderActionsInCollapsiblePanes(CollapsibleRootPanel crp, ArrayList<Element> tabs) {
             for (Element tab : tabs) {
                 //get the tab title
                 String tabTitle =  getTitle(tab); //i18n tab.getAttributeValue("title");
                 //get the available actions for the tab
                  ArrayList<Element> actionElements = BungeniToolbarParser.getInstance().getTabActionElements(tab);
                  //check if the tab has any actions -- we add a tab only when it has actions
                  if (actionElements.size()  > 0 ) {
                     //create the panel for the tab
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
                       }
                     }
                     CollapsiblePanel cp = new CollapsiblePanel();
                     cp.setTitle(tabTitle);
                     cp.add(buttonContainer);
                     cp.collapse();
                     crp.add(cp);
                  }
             }
    }

    
    private void renderActionsInTabs(JTabbedPane grpPane, ArrayList<Element> tabs){
            for (Element tab : tabs) {
                 //get the tab title
                 String tabTitle =  getTitle(tab); //i18n tab.getAttributeValue("title");
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
                       }
                     }
                     scrollablePanel.setScrollViewPort(buttonContainer);
                     scrollablePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                     grpPane.addTab(tabTitle, scrollablePanel);
                  }
             }

    }

    public toolbarStyleType getToolbarStyle() {
        return this.toolbarStyle;
    }

}
