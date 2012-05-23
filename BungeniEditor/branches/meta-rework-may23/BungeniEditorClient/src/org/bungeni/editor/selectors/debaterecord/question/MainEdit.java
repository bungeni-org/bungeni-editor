package org.bungeni.editor.selectors.debaterecord.question;

//~--- non-JDK imports --------------------------------------------------------


//~--- JDK imports ------------------------------------------------------------


import java.awt.Component;



/**
 *
 * @author Ashok Hariharan
 */
public class MainEdit extends Main {
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
