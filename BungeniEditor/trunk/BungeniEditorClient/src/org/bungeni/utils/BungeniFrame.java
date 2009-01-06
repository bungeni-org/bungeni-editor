/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author undesa
 */
public class BungeniFrame extends JFrame {
       
        public BungeniFrame(){
            super();
            setIconForFrame();
        }
        
        public BungeniFrame(String titleStr) {
            super(titleStr);
            setIconForFrame();
        }
        
        private void setIconForFrame(){
           ImageIcon iconApp = CommonTreeFunctions.loadIcon("bungeni.jpg");
           setIconImage(iconApp.getImage());
        }

}
