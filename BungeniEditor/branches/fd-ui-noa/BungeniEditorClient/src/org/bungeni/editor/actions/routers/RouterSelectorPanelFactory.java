/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

/**
 *
 * @author undesa
 */
public class RouterSelectorPanelFactory {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RouterSelectorPanelFactory.class.getName());

    public static IRouterSelectorPanel getContainerPanelObject(String panelClass){
            IRouterSelectorPanel panel = null;
            try {
                Class containerPanel = Class.forName(panelClass);
                panel = (IRouterSelectorPanel) containerPanel.newInstance();
             } catch (InstantiationException ex) {
               log.debug("getContainerPanelObject :"+ ex.getMessage());
               } catch (IllegalAccessException ex) {
               log.debug("getContainerPanelObject :"+ ex.getMessage());
               }  catch (ClassNotFoundException ex) {
               log.debug("getContainerPanelObject :"+ ex.getMessage());
              } catch (NullPointerException ex) {
               log.debug("getContainerPanelObject :"+ ex.getMessage());
              } finally {
                  return panel;
              }
        }
}
