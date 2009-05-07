
package org.bungeni.editor.panels.toolbar;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import org.jdom.Element;

/**
 * Reads the toolbar XML - translates the <blockAction> as tabs and <action> and <subaction> as buttons
 * in the tab
 * @author Ashok Hariharan
 */
public class BungeniToolbarLoader {

    private BungeniToolbarParser toolbarParser = null;
    private ActionListener actionListener = null;

    /**
     * Creates a BungeniToolbarLoader
     * The action listener for the buttons is passed as parameter
     * @param toolbarActionListener
     */
    public BungeniToolbarLoader(ActionListener toolbarActionListener) {
        //instantiate a toolbar parser
        toolbarParser = new BungeniToolbarParser();
        actionListener = toolbarActionListener;
    }

    /**
     * Load the tabs from the toolbar xml config
     * @param thisPane - the JTabbedPane object which acts as the container for the ations
     */
    public void loadToolbar(JTabbedPane thisPane ) {
        //first build the toolbar - and then we processs the xml
        toolbarParser.buildToolbar();
       // ArrayList<JTabbedPane> groupTabs = new ArrayList<JTabbedPane>(0);
        //get the tab elements
        ArrayList<Element> groupTabs = toolbarParser.getTabActionGroups();
        for (Element groupTab : groupTabs) {
            String grpTabTitle = groupTab.getAttributeValue("title");
            String grpTabUImodel = groupTab.getAttributeValue("uimodel");
            //create a new group tab
            JTabbedPane grpPane = new JTabbedPane(JTabbedPane.TOP);
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
                     String tabTitle = tab.getAttributeValue("title");
                     //create the panel for the tab
                     scrollPanel scrollablePanel = new scrollPanel();
                     //create the button container to embed into the scrollablePanel()
                     //we pass the row size for the buttonContainerPanel()
                     buttonContainerPanel buttonContainer = new buttonContainerPanel(actionElements.size());
                     //now add the button actions to the button container panel
                     for (Element action : actionElements) {
                        //get the title for the button
                        String actionTitle = action.getAttributeValue("title");
                        BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
                        buttonPanel panelButton = new buttonPanel(actionTitle, actionListener, elem);
                        buttonContainer.add(panelButton);
                     }
                     scrollablePanel.setScrollViewPort(buttonContainer);
                     grpPane.addTab(tabTitle, scrollablePanel);
                  }
             }
             thisPane.addTab(grpTabTitle, grpPane);
        }
    }

    /*
    public void loadToolbar(JTabbedPane thisPane ) {
        //first build the toolbar - and then we processs the xml
        toolbarParser.buildToolbar();
        //get the tab elements
        ArrayList<Element> tabs = toolbarParser.getTabElements();
        //iterate through the tab elements
         for (Element tab : tabs) {
              //get the available actions for the tab
              ArrayList<Element> actionElements = toolbarParser.getTabActionElements(tab);
              //check if the tab has any actions -- we add a tab only when it has actions
              if (actionElements.size()  > 0 ) {
                 //get the tab title
                 String tabTitle = tab.getAttributeValue("title");
                 //create the panel for the tab
                 scrollPanel scrollablePanel = new scrollPanel();
                 //create the button container to embed into the scrollablePanel()
                 //we pass the row size for the buttonContainerPanel()
                 buttonContainerPanel buttonContainer = new buttonContainerPanel(actionElements.size());
                 //now add the button actions to the button container panel
                 for (Element action : actionElements) {
                    //get the title for the button
                    String actionTitle = action.getAttributeValue("title");
                    BungeniToolbarActionElement elem = new BungeniToolbarActionElement(action);
                    buttonPanel panelButton = new buttonPanel(actionTitle, actionListener, elem);
                    buttonContainer.add(panelButton);
                 }
                 scrollablePanel.setScrollViewPort(buttonContainer);
                 thisPane.addTab(tabTitle, scrollablePanel);
              }
         }

    }
     */
}
