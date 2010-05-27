/*
 * routerFactory.java
 *
 * Created on March 10, 2008, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarSubAction;

/**
 *
 * @author Administrator
 */
public class routerFactory {
    
    /** Creates a new instance of routerFactory */
    public routerFactory() {
    }
 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerFactory.class.getName());
 
    public static IBungeniActionRouter getRouterClass(toolbarSubAction subAction) {
       IBungeniActionRouter  router = null;
       try {
             log.debug("getRouterClass: creating event class"+ subAction.router_class());
             Class routerClass;
             routerClass= Class.forName(subAction.router_class());
             router = (IBungeniActionRouter) routerClass.newInstance();
       } catch (NullPointerException ex) {
           log.error("getRouterClass:"+ ex.getMessage());
       } catch (ClassNotFoundException ex) {
           log.error("getRouterClass:"+ ex.getMessage());
        } finally {
             return router;
        }
    }


    
}
