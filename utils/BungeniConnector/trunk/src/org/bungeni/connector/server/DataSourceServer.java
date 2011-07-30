
package org.bungeni.connector.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.impl.RDBMSBungeniConnector;
import org.bungeni.connector.impl.XMLBungeniConnector;
import org.bungeni.connector.restlet.current.APIRestlet;
import org.bungeni.connector.restlet.current.BillsRestlet;
import org.bungeni.connector.restlet.current.MembersRestlet;
import org.bungeni.connector.restlet.current.MetadataInfoRestlet;
import org.bungeni.connector.restlet.current.MotionsRestlet;
import org.bungeni.connector.restlet.current.QuestionsRestlet;
import org.bungeni.connector.restlet.current.StopServerRestlet;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 *
 * @author Dave
 */
public class DataSourceServer extends Application {

    private String STOP_SERVER_ROUTE = "/stop_server";
    private String membersRoute = "/current/members";
    private String motionsRoute = "/current/motions";
    private String questionsRoute = "/current/questions";
    private String billsRoute = "/current/bills";
    private String metadataInfoRoute = "/current/metadata";
    private String membersURI = null;
    private String motionsURI = null;
    private String questionsURI = null;
    private String billsURI = null;
    private String metadataInfoURI = null;
    private Component serverComponent = null;
    private int serverPort = 8899;
    private static Logger logger = Logger.getLogger(BungeniConnector.class.getName());
    static DataSourceServer INSTANCE = null;
    private String source = "db";
    public static final String URI = "uri";
    public static final String DB = "db";
    IBungeniConnector bungeniConnector = null;
    //THIS IS TO BE USED ONLY FOR TESTING
    public static final String PROPERTIES_FILE = System.getProperty("user.dir") + System.getProperty("file.separator") + "settings" + System.getProperty("file.separator") + "bungeni-connector.properties";

    public DataSourceServer() {
    }

    public static DataSourceServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSourceServer();
        }
        return INSTANCE;
    }

    /**
     * Loads the properties for starting the data source server.
     * Expects a loaded Properties object 
     * @param properties
     * @return
     */
    public boolean loadProperties(Properties properties) {
        boolean loaded = false;
            try {
                setServerPort(Integer.valueOf(properties.getProperty("server-port")));
            } catch (Exception ex) {
                logger.error("Invalid server-port", ex);
            }
            try {
                this.source = properties.getProperty("data-source-type");
                if (this.source == null || !(this.source.equalsIgnoreCase(URI) || this.source.equalsIgnoreCase(DB))) {
                    throw new Exception("Invalid bungeni-connector-data-source-type");
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
            if (this.source.equalsIgnoreCase(URI)) {
                this.membersURI = properties.getProperty("uri-members");
                this.motionsURI = properties.getProperty("uri-motions");
                this.questionsURI = properties.getProperty("uri-questions");
                this.billsURI = properties.getProperty("uri-bills");
                this.metadataInfoURI = properties.getProperty("uri-metadata-info");
                this.bungeniConnector = new XMLBungeniConnector();
            } else {
                this.bungeniConnector = new RDBMSBungeniConnector();
            }

            loaded = true;
            logger.info("Properties loaded");
            return loaded;
        }
    

    /**
     * Loads a properties file. The file is loaded from a path.
     * @param propertiesFile
     * @return
     */
    public boolean loadProperties(String propertiesFile) {
        boolean loaded = false;
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(propertiesFile);
            properties.load(in);
            loaded = this.loadProperties(properties);
        } catch (IOException ex) {
            logger.error("unable to find properties file :" + propertiesFile, ex);
        } 
        return loaded;
    }

    public void setServerPort(int nPort) {
        serverPort = nPort;
    }

    public DataSourceServer startServer() {
        DataSourceServer ts = null;
        try {
            if (serverComponent == null) {
                serverComponent = new Component();
                serverComponent.getServers().add(Protocol.HTTP, serverPort);
                ts = new DataSourceServer();
                serverComponent.getDefaultHost().attach(ts);
                serverComponent.getClients().add(Protocol.HTTP);
                serverComponent.getClients().add(Protocol.HTTPS);
                serverComponent.getClients().add(Protocol.FILE);
                serverComponent.start();
            } else {
                if (serverComponent.isStopped()) {
                    serverComponent.start();
                } else {
                    logger.info("DataSourceServer is already running on port " + serverPort);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            return ts;
        }
    }

    public boolean stopServer() {
        if (bungeniConnector != null)
             bungeniConnector.closeConnector();
        boolean stopped = false;
        try {
            if (serverComponent != null) {
                serverComponent.stop();
                stopped = true;
                logger.info("DataSourceServer stopped");
            } else {
                logger.info("DataSourceServer is not running");
            }

        } catch (Exception ex) {
            logger.error("Error Stopping server", ex);
        } finally {
            return stopped;
        }
    }

    @Override
    public Restlet createRoot() {
        Router router = new Router(getContext());

        router.attach(metadataInfoRoute, new MetadataInfoRestlet(bungeniConnector));
        router.attach(billsRoute, new BillsRestlet(bungeniConnector));
        router.attach(membersRoute, new MembersRestlet(bungeniConnector));
        router.attach(motionsRoute, new MotionsRestlet(bungeniConnector));
        router.attach(questionsRoute, new QuestionsRestlet(bungeniConnector));
        router.attach(STOP_SERVER_ROUTE, StopServerRestlet.getInstance());
        router.attach("/", APIRestlet.getInstance());
        return router;
    }

    public String getSource() {
        return source;
    }
    

    public String getBillsURI() {
        return billsURI;
    }

    public String getMembersURI() {
        return membersURI;
    }

    public String getMetadataInfoURI() {
        return metadataInfoURI;
    }

    public String getMotionsURI() {
        return motionsURI;
    }

    public String getQuestionsURI() {
        return questionsURI;
    }

}
