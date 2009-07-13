package org.bungeni.editor.selectors.debaterecord.masthead;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.selectors.BaseMetadataContainerPanel;
import org.bungeni.editor.selectors.panelInfo;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;

import java.util.ArrayList;

/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {
    public Main() {
        super();
    }

    /*
    @Override
    protected void setupPanels() {

        // to do : make this a base class function and move the usage to the the base class...


        if (this.theSubAction != null) {
            m_activePanels = new ArrayList<panelInfo>() {
                {
                    add(new panelInfo(theSubAction.sub_action_name(), theSubAction.dialog_class()));
                }
            };
        } else {
            m_activePanels = new ArrayList<panelInfo>(0);
        }
    } */

    public static void main(String[] args) {
        Main m = new Main();

        // m.initVariables(ooDoc, parentFrm, aAction, aSubAction, dlgMode);
        m.initialize();

        javax.swing.JFrame f = new javax.swing.JFrame("MastHead title");

        f.add(m);
        f.pack();
        f.setVisible(true);
    }

    @Override
    public Component getPanelComponent() {
        return this;
    }
}
