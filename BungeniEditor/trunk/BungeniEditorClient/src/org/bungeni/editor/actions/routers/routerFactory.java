package org.bungeni.editor.actions.routers;

import org.bungeni.editor.config.DocumentActionsReader;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import org.jdom.Element;

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
       // !+ACTION_RECONF (ah, 24-01-2012) -
    // The the router class is available directly on the toolbarSubAction object !
    /*
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
    } */
    
    /** Creates a new instance of routerFactory */
    public routerFactory() {
    }
 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerFactory.class.getName());
 
    public static IBungeniActionRouter getRouterClass(toolbarAction currAction) {
       IBungeniActionRouter  router = null;
       try {
             log.debug("getRouterClass: creating event class"+ currAction.router_class());

             // get the router class from the ROUTER_CONFIGS table
             // and use for instantiation
             // !+ACTION_RECONF (rm, jan 2012) - get the action name rather than
             // the action_class and use this to query for the router class
             // String routerClassName = currAction.router_class();
             String actionClassName = currAction.sub_action_name();

             if(null == actionClassName)
             {
                 return null ;
             }

             // !+ACTION_RECONF (rm, jan 2012) - once the router_name is
             // obtained, then a query has to be made to router_configs.xml
             // to determine the router_class
             // note that the var returned above from subAction.router_class()
             // is the router_name rather than the router_class
             // use introspection to create the router class
             Element rName = DocumentActionsReader.getInstance().getRouter(
                     BungeniEditorPropertiesHelper.getCurrentDocType(),
                     actionClassName
                     );

             // get the routerName from the xml tag for the routerName
             String rClassName = rName.getAttributeValue("class") ;

             // use introspection to create new instance of the router class
             Class routerClass;
             routerClass= Class.forName(rClassName);
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
