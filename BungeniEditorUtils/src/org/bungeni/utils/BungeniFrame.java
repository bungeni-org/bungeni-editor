package org.bungeni.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.bungeni.extutils.CommonUIFunctions;

/**
 * Extended JFrame class that abstracts regular boiler plate code for Bungeni Frames
 *
 * @author Ashok Hariharan
 */
public class BungeniFrame extends JFrame {

        public static int BUNGENIFRAME_CLOSE_STYLE = JFrame.DISPOSE_ON_CLOSE;
        public static boolean BUNGENIFRAME_RESIZABLE = true;
        public static boolean BUNGENIFRAME_ALWAYS_ON_TOP = false;

      
        public BungeniFrame(){
            super();
            setIconForFrame();
        }
        
        public BungeniFrame(String titleStr) {
            super(titleStr);
            setIconForFrame();
        }

        /**
         * This must be called after construction
         */
        public void initFrame(){
            CommonUIFunctions.compOrientation(this);
        }

        public BungeniFrame(String titleStr, Dimension frmSize) {
            super(titleStr);
            setIconForFrame();
            this.setSize(frmSize);
        }
        
        private void setIconForFrame(){
           ImageIcon iconApp = CommonBungeniTreeFunctions.loadIcon("bungeni-icon.png");
           setIconImage(iconApp.getImage());

        }

        /**
         * Removes the min / max / close buttons used by the frames
         */
        public void removeMinMaxClose() {
            removeMinMaxClose(this);
        }

        private void removeMinMaxClose(Component comp) {
             if(comp instanceof AbstractButton)
             {
               AbstractButton btn = (AbstractButton) comp;
               comp.getParent().remove(comp);
             }
             if (comp instanceof Container)
             {
               Component[] comps = ((Container)comp).getComponents();
               for(int x = 0, y = comps.length; x < y; x++)
               {
                 removeMinMaxClose(comps[x]);
               }
             }
       }

        /**
         * API to launch a frame with a panel and of a particular size
         * @param panel - JPanel to be added to the frame
         * @param dSize - size of the JFrame
         */
        public void launch(JPanel panel, Dimension dSize){
           this.setDefaultCloseOperation(BUNGENIFRAME_CLOSE_STYLE);
           this.add(panel);
           this.setSize(dSize);
           this.setResizable(BUNGENIFRAME_RESIZABLE);
           this.setAlwaysOnTop(BUNGENIFRAME_ALWAYS_ON_TOP);
           this.setVisible(true);
        }

}
