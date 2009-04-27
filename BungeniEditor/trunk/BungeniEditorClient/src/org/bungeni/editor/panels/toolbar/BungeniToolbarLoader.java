
package org.bungeni.editor.panels.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.jdom.Element;

/**
 * Reads the toolbar XML - translates the <blockAction> as tabs and <action> and <subaction> as buttons
 * in the tab
 * @author Ashok Hariharan
 */
public class BungeniToolbarLoader {

    private BungeniToolbarParser toolbarParser = null;

    public BungeniToolbarLoader() {
        toolbarParser = new BungeniToolbarParser();
    }

    /**
     * Load the tabs from the toolbar xml config
     * @param thisPane
     */
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
                 String tabTitle = tab.getAttributeValue("name");
                 //create the panel for the tab
                 scrollPanel scrollablePanel = new scrollPanel();
                 //create the button container to embed into the scrollablePanel()
                 //we pass the row size for the buttonContainerPanel()
                 buttonContainerPanel buttonContainer = new buttonContainerPanel(actionElements.size());
                 //now add the button actions to the button container panel
                 for (Element action : actionElements) {
                    String actionTitle = action.getAttributeValue("name");
                    buttonPanel panelButton = new buttonPanel(actionTitle, new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, ((JButton)e.getSource()).getText());
                        }
                    });
                    buttonContainer.add(panelButton);
                 }
                 scrollablePanel.setScrollViewPort(buttonContainer);
                 thisPane.addTab(tabTitle, scrollablePanel);
              }
         }

    }
}
