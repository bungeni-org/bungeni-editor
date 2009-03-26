/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author undesa
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

        public BungeniFrame(String titleStr, Dimension frmSize) {
            super(titleStr);
            setIconForFrame();
            this.setSize(frmSize);
        }
        
        private void setIconForFrame(){
           ImageIcon iconApp = CommonTreeFunctions.loadIcon("bungeni.jpg");
           setIconImage(iconApp.getImage());

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
