/*
 * routerFactory.java
 *
 * Created on March 10, 2008, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.actions.toolbarSubAction;

/**
 * @author Administrator
 *
 * 
 * //!+ACTION_RECONF (rm, jan 2012) - ADDED COMMENTS & REFACTORED THE CODE
 * so that the code looks for the router class from the db table
 * ROUTER_CONFIGS rather than from ACTION_SETTINGS2
 *
 */
public class routerFactory {

    // !+ACTION_RECONF (rm, jan 2012)- this method obtains the name for the 
    // router_class
    private static String getRouterClassName(String router_class_name) {

        String routerClassName = null  ;
        BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        db.Connect();
        QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_ROUTER_CLASS(router_class_name));
        db.EndConnect();

        //get the  data from the query
        if (qr == null) {
            log.error("Router Class for router " + routerClassName + " not found!");
            return null;
        }
        if (qr.hasResults()) {
            Vector <String> res = qr.theResults().elementAt(0);
            return  res.elementAt(0);
        }
        return routerClassName ;
    }
    
    /** Creates a new instance of routerFactory */
    public routerFactory() {
    }
 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerFactory.class.getName());
 
    public static IBungeniActionRouter getRouterClass(toolbarSubAction subAction) {
       IBungeniActionRouter  router = null;
       try {
             log.debug("getRouterClass: creating event class"+ subAction.router_class());

             // get the router class from the ROUTER_CONFIGS table
             // and use for instantiation
             String routerClassName = getRouterClassName(subAction.router_class());

             if(null == routerClassName)
             {
                 return null ;
             }

             // use introspection to create the router class
             Class routerClass;
             routerClass= Class.forName(routerClassName);
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
