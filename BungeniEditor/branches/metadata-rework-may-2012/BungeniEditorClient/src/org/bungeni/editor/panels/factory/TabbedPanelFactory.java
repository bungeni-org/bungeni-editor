
package org.bungeni.editor.panels.factory;

import org.bungeni.editor.config.PanelsReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Ashok Hariharan
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
        } catch(Exception ex) {
            log.error("makePanel : " + ex.getMessage());
        }
        /**
        (ClassNotFoundException ex) {
            log.error("makePanel : " + ex.getMessage());
        } catch (InstantiationException ex) {
            log.error("makePanel : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("makePanel : " + ex.getMessage());
        } **/
       return newPanel;
        
    }

    /**
     * 
     * @param docType the doctype associated with the panel or 'internal' for panels not associated with a doctype
     * @param panelName
     * @return
     */
    public static ITabbedPanel getPanelByName(String docType, String panelName) {
       ITabbedPanel panel = null;
        try {
            //
            //<panel name="docmeta" class="org.bungeni.editor.panels.loadable.documentMetadataPanel" state="1">
            //   <title xml:lang="eng">Document</title>
            //</panel>

            
            Element namedPanel = PanelsReader.getInstance().getPanelByName(docType, panelName);
            String panelClass = namedPanel.getAttributeValue("class");
            panel = makePanel(panelClass, 0, PanelsReader.getInstance().getLocalizedTitleForPanel(namedPanel) );

            /***
            BungeniClientDB instance = new BungeniClientDB (DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
            QueryResults qr = instance.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_TABS_BY_NAME(docType, panelName));
            if (qr.hasResults()) {
                Iterator<Vector<String>> resultsIterator = qr.theResultsIterator();
                while (resultsIterator.hasNext()) {
                    Vector<String> resultRow = resultsIterator.next();
                    String panelClass = qr.getField(resultRow, "PANEL_CLASS");
                    String panelTitle = qr.getField(resultRow, "PANEL_TITLE");
                    String panelLoadOrder = qr.getField(resultRow, "PANEL_LOAD_ORDER");
                    Integer panelLoad = Integer.parseInt(panelLoadOrder);
                    panel = makePanel(panelClass, panelLoad, panelTitle);
                }

            }
             **/
        } catch (Exception ex) {
            log.error("getPanelByName : " + ex.getMessage());
            log.error("getPanelByName : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return panel;
        }

    }
    public static  ArrayList<ITabbedPanel> getPanelsByDocType(String docType){
            
        ArrayList<ITabbedPanel> tabbedPanels = new ArrayList<ITabbedPanel>();
        try {

            List<Element> panelElements = PanelsReader.getInstance().getPanelsByDocType(docType);
            for (Element aPanel : panelElements) {
                 String panelClass = aPanel.getAttributeValue("class");
                 String panelTitle = PanelsReader.getInstance().getLocalizedTitleForPanel(aPanel);
                 ITabbedPanel panel = makePanel(panelClass, 0, panelTitle );
                 if (null != panel) {
                     tabbedPanels.add(panel);
                 }
            }
            /**
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
                    if (panel == null) {
                        log.error("getPanelsByDocType: the panel :" + panelClass + " could not be loaded");
                    } else
                        tabbedPanels.add(panel);
                }
               **/
            }
        catch (JDOMException ex) {
            log.error("getPanelsByDocType : " + ex.getMessage());
            log.error("getPanelsByDocType : " + CommonExceptionUtils.getStackTrace(ex));
        } catch (IOException ex) {
            log.error("getPanelsByDocType : " + ex.getMessage());
            log.error("getPanelsByDocType : " + CommonExceptionUtils.getStackTrace(ex));
        }
        catch (Exception ex) {
            log.error("getPanelsByDocType : " + ex.getMessage());
            log.error("getPanelsByDocType : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return tabbedPanels;
        }   
    }
}
