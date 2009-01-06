/*
 * CollapsiblePanelFactory.java
 *
 * Created on July 31, 2007, 3:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

/**
 *
 * @author Administrator
 *Needs to be implemented to generate Panel UI classes
 */
public class CollapsiblePanelFactory {
    private static java.util.HashMap panelsMap=null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CollapsiblePanelFactory.class.getName());
   
    /** Creates a new instance of CollapsiblePanelFactory */
    public CollapsiblePanelFactory() {
    }
 
    public static ICollapsiblePanel getPanelClass(String panelName){
        
        ICollapsiblePanel newPanel = null;
        try {
        String className="";
        if (panelName.equals("textmarkupPanel")){
            className="org.bungeni.editor.panels.textmarkupPanel";
        } else
        if (panelName.equals("sectionPanel")){
            className="org.bungeni.editor.panels.sectionPanel";
        } else
        if (panelName.equals("generalEditorPanel")) {
            className = "org.bungeni.editor.panels.generalEditorPanel";
        } else
        if (panelName.equals("generalEditorPanel3")) {
            className = "org.bungeni.editor.panels.generalEditorPanel3";
        }  else
        if (panelName.equals("generalEditorPanel4")) {
            className = "org.bungeni.editor.panels.generalEditorPanel4";
        }
        
        Class eventClass;
            eventClass = Class.forName(className);
            newPanel = (ICollapsiblePanel)eventClass.newInstance();
      
        return newPanel;
          } catch (InstantiationException ex) {
            ex.printStackTrace();
           } catch (IllegalAccessException ex) {
            ex.printStackTrace();
           }  catch (ClassNotFoundException ex) {
            ex.printStackTrace();
          }
        finally {
            return newPanel;
        }
    }
  }
