/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class LaunchablePanelFactory {

    
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LaunchablePanelFactory.class.getName());
       private final static HashMap<String,String> launchablePanelMap 
               = new HashMap<String, String>(){
                                       {
                                           put("browseReferences", "org.bungeni.editor.panels.loadable.refmgr.browseReferences");
                                           put("brokenReferences", "org.bungeni.editor.panels.loadable.refmgr.brokenReferences");
                                           put("externalReferences", "org.bungeni.editor.panels.loadable.refmgr.externalReferences");
                                       }

                                   };
    /** Creates a new instance of TabbedPanelFactory */
    public LaunchablePanelFactory() {
    }
   
    public static ILaunchablePanel getPanelClass (String panelClassName) {
       Class panelClass;
       ILaunchablePanel newPanel = null;
        try {
            if (launchablePanelMap.containsKey(panelClassName))  {
                String className = launchablePanelMap.get(panelClassName);
                panelClass = Class.forName(className);
                newPanel = (ILaunchablePanel)panelClass.newInstance();
            } else {
                log.error ("getPanelClass:" + panelClassName + " was not found in the map");
            }
        } catch (ClassNotFoundException ex) {
            log.error("getPanelClass : " + ex.getMessage());
        } catch (InstantiationException ex) {
            log.error("getPaneClass : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("getPanelClass : " + ex.getMessage());
        } finally {
            return newPanel;
        }
    }
    
}
