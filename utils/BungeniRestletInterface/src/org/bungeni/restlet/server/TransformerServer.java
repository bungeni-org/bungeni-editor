package org.bungeni.restlet.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.restlet.resources.OdtResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Protocol;


public class TransformerServer extends Application {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransformerServer.class.getName());
   private static Component serverComponent = null;
   private static int SERVER_PORT = 8182;
   private static String SERVER_TMP_FOLDER = "/home/undesa/tmp";


   public static void setServerPort(int nPort) {
       SERVER_PORT = nPort;
   }

   public static void setServerTempFolder(String sFolder) {
       SERVER_TMP_FOLDER = sFolder;
   }

   public static void startServer() {
    
            try {
                if (serverComponent == null) {
                    serverComponent = new Component();
                    serverComponent.getServers().add(Protocol.HTTP, SERVER_PORT);
                    TransformerServer transServer = new TransformerServer();
                    serverComponent.getDefaultHost().attach("", transServer);
                    serverComponent.start();
                } else {
                    if (serverComponent.isStopped()) {
                        serverComponent.start();
                    } else {
                        System.out.println("TransformServer is already running on port " + SERVER_PORT);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
     }

    public static String getTempFileFolder() {
        return SERVER_TMP_FOLDER;
    }

     @Override
      public Restlet createRoot() {
        Router router = new Router(getContext());
        router.attach("/convert_to_anxml", OdtResource.class);
        return router;
      }
}
