
package org.bungeni.editor.selectors.debaterecord.speech;

import java.awt.Component;

/**
 *
 * @author Ashok
 */
public class MainEdit extends Main{
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(MainEdit.class.getName());

    public MainEdit() {
        super();
    }

    @Override
    public Component getPanelComponent() {
        return this;
    }

}
