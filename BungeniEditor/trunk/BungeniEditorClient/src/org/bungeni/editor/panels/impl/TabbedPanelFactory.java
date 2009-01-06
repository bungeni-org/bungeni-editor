/*
 * TabbedPanelFactory.java
 *
 * Created on May 20, 2008, 11:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.ooo.utils.CommonExceptionUtils;

/**
 *
 * @author Administrator
 */
public class TabbedPanelFactory {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TabbedPanelFactory.class.getName());
   
    /** Creates a new instance of TabbedPanelFactory */
    public TabbedPanelFactory() {
    }
   
    private static ITabbedPanel makePanel (String className, Integer loadOrder, String panelTitle) {
       Class panelClass;
       ITabbedPanel newPanel = null;
        try {
            panelClass = Class.forName(className);
            newPanel = (ITabbedPanel)panelClass.newInstance();
            newPanel.setPanelLoadOrder(loadOrder);
            newPanel.setPanelTitle(panelTitle);
        } catch (ClassNotFoundException ex) {
            log.error("makePanel : " + ex.getMessage());
        } catch (InstantiationException ex) {
            log.error("makePanel : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("makePanel : " + ex.getMessage());
        } finally {
            return newPanel;
        }
    }

    public static  ArrayList<ITabbedPanel> getPanelsByDocType(String docType){
            
        ArrayList<ITabbedPanel> tabbedPanels = new ArrayList<ITabbedPanel>();
        try {
            BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
            QueryResults qr = instance.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_TABS_BY_DOC_TYPE(docType));
     
            
            if (qr.hasResults()) {
                Iterator<Vector<String>> resultsIterator = qr.theResultsIterator();
                while (resultsIterator.hasNext()) {
                    Vector<String> resultRow = resultsIterator.next();
                    String panelClass = qr.getField(resultRow, "PANEL_CLASS");
                    String panelTitle = qr.getField(resultRow, "PANEL_TITLE");
                    String panelLoadOrder = qr.getField(resultRow, "PANEL_LOAD_ORDER");
                    Integer panelLoad = Integer.parseInt(panelLoadOrder);
                    ITabbedPanel panel = makePanel(panelClass, panelLoad, panelTitle);
                    tabbedPanels.add(panel);
                }
                
            }
        } catch (Exception ex) {
            log.error("getPanelsByDocType : " + ex.getMessage());
            log.error("getPanelsByDocType : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return tabbedPanels;
        }   
    }
}
