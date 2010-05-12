package org.bungeni.editor.selectors.debaterecord.motions;


/**
 *
 * @author Ashok Hariharan
 */
public class MainEdit extends Main {
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(MainEdit.class.getName());

  
    public MainEdit() {
        super();
    }

    @Override
    protected void setupPanels() {
        super.setupPanels();
        this.enableAllChildPanels(true);
    }

   
}
