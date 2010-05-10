package org.bungeni.restlet.server;

import java.io.File;

import org.bungeni.restlet.TransformerRestletDefaultConfiguration;
import org.bungeni.restlet.resources.OdtResource;
import org.bungeni.restlet.restlets.TransformParamsRestlet;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;


public class TransformerServer extends Application {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransformerServer.class.getName());
   private static Component serverComponent = null;
   /**
    * Set from configServer()
    */
   private static int SERVER_PORT = 8182;
   /**
    * Set from configServer()
    */
   private static String SERVER_TMP_FOLDER = "/home/undesa/tmp";
   /**
    * This is assumed to be in the root folder
    */
   public static final String SERVER_CONFIG_FILE = "transformer.ini";
   /**
    * Initialized in startServer()
    */
   private static Restlet setParamsRestlet  = null;


   public static void setServerPort(int nPort) {
       SERVER_PORT = nPort;
   }

   public static void setServerTempFolder(String sFolder) {
       SERVER_TMP_FOLDER = sFolder;
   }


   /**
    * configures the server by reading transformer.ini
    * @param workingDir
    * @return
    */
private static boolean configServer(String workingDir) {
	   boolean bState = false;
	   try {
		   String iniFile = workingDir + File.separator + SERVER_CONFIG_FILE;
		   File fIni = new File(iniFile);
		   if (fIni.exists()) {
			   TransformerRestletDefaultConfiguration config = TransformerRestletDefaultConfiguration.getInstance(fIni);
			   SERVER_PORT = config.getServerPort();
			   SERVER_TMP_FOLDER = config.getServerTmpFolder();
			   bState = true;
		   } 
	   } catch (Exception ex) {
		   log.error("configServer", ex);
	   } finally {
		   
	   }
	   return bState;
   }
   
   
  /**
   * API used to start the transformer server
   * @param workingDir - the directory to be used as the working directory by the transformer server.
   * The resource folders for the transformer are assumed to be subdirectories of this folder
   * @return - TransformerServer -handle to transformer server
   */
public static TransformerServer startServer(String workingDir) {
	   		System.out.println("Starting server with working directory : " + workingDir);
            TransformerServer ts = null;
            try {
                if (serverComponent == null) {
                	if (configServer(workingDir)) {
                		serverComponent = new Component();
                		serverComponent.getServers().add(Protocol.HTTP, SERVER_PORT);
                		ts = new TransformerServer();
                		serverComponent.getDefaultHost().attach("", ts);
                		//create the restlet instance here, before calling start
                		setParamsRestlet = new TransformParamsRestlet(workingDir);
                		serverComponent.start();
                	} else {
                		System.out.println("Failed while configuring TransformServer");
                	}
                } else {
                    if (serverComponent.isStopped()) {
                        serverComponent.start();
                    } else {
                        System.out.println("TransformServer is already running on port " + SERVER_PORT);
                        log.info("TransformServer is already running on port " + SERVER_PORT);
                    }
                }

            } catch (Exception ex) {
                log.error("startServer : " , ex);
            } finally {
              }
            return ts;
            
     }

    public static String getTempFileFolder() {
        return SERVER_TMP_FOLDER;
    }
    
    private String SERVER_RUNNING_RESPONSE="SERVER_RUNNING";

    /**
     * Called before serverComponent.start();
     */
     @Override
      public Restlet createRoot() {
        Router router = new Router(getContext());
        router.attach("/", new Restlet(){
        	@Override
            public void handle(Request request, Response response) {
        		if (request.getMethod().equals(Method.GET)){
        			response.setEntity(SERVER_RUNNING_RESPONSE, MediaType.TEXT_PLAIN);
        		}
             
           }
        });
        router.attach("/convert_to_anxml", OdtResource.class);
        router.attach("/set_convert_params",setParamsRestlet);
        router.attach("/stop_server", new Restlet(){
        	@Override
            public void handle(Request request, Response response) {
        		if (request.getMethod().equals(Method.GET)){
        			try {
        				response.setEntity("Stoping Server", MediaType.TEXT_PLAIN);
						System.exit(0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("/stop_server : unable to stop server.", e);
						System.exit(0);
					}
        		} else {
        	       response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        		}
        	}
        });
        return router;
      }

     public static void main(String[] args) {
    	 String workDir = args[0];  //"/Users/ashok/Projects/ODTTranslator/translator/ODTtoAKN/bin/";
    	 TransformerServer.startServer(workDir);

     }

     /**
      * Returns the server port
      * @return Integer
      */
	public static int getServerPort() {
		return SERVER_PORT;
	}


}
