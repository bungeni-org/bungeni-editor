package org.bungeni.utils;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.bungeni.extutils.CommonUIFunctions;
import org.bungeni.utils.CommonBungeniTreeFunctions;

/**
 * Extended JFrame class that abstracts regular boiler plate code for Bungeni Frames
 *
 * @author Ashok Hariharan
 */
public class BungeniDialog extends JDialog {

        public static int BUNGENIFRAME_CLOSE_STYLE = JDialog.DISPOSE_ON_CLOSE;
        public static boolean BUNGENIFRAME_RESIZABLE = true;
        public static boolean BUNGENIFRAME_ALWAYS_ON_TOP = false;

      
        public BungeniDialog(JFrame parent){
            super(parent, true);
            setIconForFrame();
        }
        
        public BungeniDialog(JFrame parent, String titleStr, boolean modal) {
            super(parent, titleStr, modal);
            setIconForFrame();
        }

        /**
         * This must be called after construction
         */
        public void initFrame(){
            CommonUIFunctions.compOrientation(this);
        }

        public BungeniDialog(JFrame parent, String titleStr, Dimension frmSize) {
            super(parent, titleStr, true);
            setIconForFrame();
            this.setSize(frmSize);
        }
        
        private void setIconForFrame(){
           ImageIcon iconApp = CommonBungeniTreeFunctions.loadIcon("bungeni-icon.png");
           setIconImage(iconApp.getImage());

        }



}
