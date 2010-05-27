
package org.bungeni.editor.selectors.debaterecord.conclusion;

import java.awt.Component;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

/**
 *
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {

    public Main() {
        super();
    }

     public static void main(String[] args){
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
