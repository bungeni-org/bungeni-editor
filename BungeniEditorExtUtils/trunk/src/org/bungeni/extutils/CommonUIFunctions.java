
package org.bungeni.extutils;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 *
 * All UI Related common functions
 *
 * @author Ashok Hariharan
 */
public class CommonUIFunctions {

    public static ArrayList<String> findComponentsWithNames(Container container) {
        ArrayList<String> fieldsWithNames = new ArrayList<String>(0);
        for (Component component: container.getComponents()) {
            String sCompName = component.getName();
            if (sCompName == null) {
                continue;
            } else {
                if (sCompName.trim().length() == 0) {
                    continue;
                } else {
                    fieldsWithNames.add(sCompName.trim());
                }
            }
        }
        return fieldsWithNames;
      }

    public static Component findComponentByName(Container container, String componentName) {
        for (Component component : container.getComponents()) {
            if (componentName.equals(component.getName())) {
                return component;
            }

            if (component instanceof JRootPane) {

                // According to the JavaDoc for JRootPane, JRootPane is
                // "A lightweight container used behind the scenes by JFrame,
                // JDialog, JWindow, JApplet, and JInternalFrame.". The reference
                // to the RootPane is set up by implementing the RootPaneContainer
                // interface by the JFrame, JDialog, JWindow, JApplet and
                // JInternalFrame. See also the JavaDoc for RootPaneContainer.
                // When a JRootPane is found, recurse into it and continue searching.
                JRootPane nestedJRootPane = (JRootPane) component;

                return findComponentByName(nestedJRootPane.getContentPane(), componentName);
            }

            if (component instanceof JPanel) {

                // JPanel found. Recursing into this panel.
                JPanel nestedJPanel = (JPanel) component;

                return findComponentByName(nestedJPanel, componentName);
            }
        }

        return null;
    }

/**
 * Changes the Orientation of the components depending on the Locale
 *
 * @param comp Component handle
 */
 public static void compOrientation(Component comp)
 {
   //!+ORIENTATION(ah, 03-05-2012) - original version by smr.ayesh was appling orientation
   //as Container, added a fallback if it isnt a container, to use the regular Component class
   //apply orientation api
   //!+ORIENTATION(ah, 04-05-2012) - moved to CommonUIFunctions 
   if (comp instanceof Container) {
        ((Container)comp).applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
   } else {
        comp.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
   }

 }

  /**
   * Requires the use of DisabledGlassPane 
   * @param glassPane
   * @param parentDialog
   * @param message 
   */
  public void disablePanel(DisabledGlassPane glassPane, JDialog parentDialog, String message){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(message);
  }

  public void disablePanel(DisabledGlassPane glassPane, JFrame parentDialog, String message){
        JRootPane rootPane = SwingUtilities.getRootPane(parentDialog);
        rootPane.setGlassPane(glassPane);
        glassPane.activate(message);
  }

}
