/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;

/**
 *
 * @author Ashok Hariharan
 */
public class FrameLauncher {

  
    public static JFrame InitializeFrame(String frameTitle, Component panelComponent, Dimension launchDimension) {
            //initialize
            JFrame launch = new JFrame();
            launch.setTitle(frameTitle);
            if (launchDimension != null)
                launch.setSize(launchDimension);
            launch.add(panelComponent);
            
            return launch;
    }

    public static void LaunchFrame (JFrame f, boolean alwaysOnTop, boolean centerWindow) {
        f.setVisible(true);
        if (alwaysOnTop) {
            f.toFront();
            f.setAlwaysOnTop(alwaysOnTop);
        }
        if (centerWindow) {
            CenterFrame(f);
        }
    }
    
    public static void CenterFrame (JFrame frame) {
        Dimension screenSize = getScreenSize();
        frame.setLocation(screenSize.width/4, screenSize.height/4);
  }

   private static Dimension getScreenSize(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.getScreenSize();
   }

    public static void CenterFrame (JDialog frame) {
        Dimension screenSize = getScreenSize();
        frame.setLocation(screenSize.width/4, screenSize.height/4);
    }
}
