package org.bungeni.editor.selectors.debaterecord.masthead;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;


/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {
    public Main() {
        super();
    }


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
