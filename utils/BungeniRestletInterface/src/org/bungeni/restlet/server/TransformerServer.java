package org.bungeni.restlet.server;

import org.bungeni.restlet.resources.OdtResource;
import org.bungeni.restlet.restlets.TransformParamsRestlet;
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
   private static Restlet setParamsRestlet;

   static {
       try {
            setParamsRestlet = new TransformParamsRestlet();
       } catch (Exception ex) {
           log.error("TransformServer.setParamsRestlet : ", ex);
       }

   }

   public static void setServerPort(int nPort) {
       SERVER_PORT = nPort;
   }

   public static void setServerTempFolder(String sFolder) {
       SERVER_TMP_FOLDER = sFolder;
   }

   public static TransformerServer startServer() {
            TransformerServer ts = null;
            try {
                if (serverComponent == null) {
                    serverComponent = new Component();
                    serverComponent.getServers().add(Protocol.HTTP, SERVER_PORT);
                    ts = new TransformerServer();
                    serverComponent.getDefaultHost().attach("", ts);
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
            } finally {
                return ts;
            }
     }

    public static String getTempFileFolder() {
        return SERVER_TMP_FOLDER;
    }

     @Override
      public Restlet createRoot() {
        Router router = new Router(getContext());
        router.attach("/convert_to_anxml", OdtResource.class);
        router.attach("/set_convert_params",setParamsRestlet);
        return router;
      }

     public static void main(String[] args) {
         TransformerServer trans =TransformerServer.startServer();
     }
}
