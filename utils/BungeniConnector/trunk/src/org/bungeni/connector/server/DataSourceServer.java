
package org.bungeni.connector.server;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
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

    //!+CODE_REVIEW WHy are these URI variables here ? they are related to the
    //XMLConnector implemenetation they should not be here !
    private String membersURI = null;
    private String motionsURI = null;
    private String questionsURI = null;
    private String billsURI = null;
    private String metadataInfoURI = null;

    private Component serverComponent = null;
    private int serverPort = 8899;
    private static Logger logger = Logger.getLogger(BungeniConnector.class.getName());
    static DataSourceServer INSTANCE = null;
    private ConnectorProperties connectorProps = null;
    private DataSourceType sourceType = DataSourceType.DB;

    /**
     * Available data source types
     */
    //!+DATASOURCE_TYPE(ah, sep-2011) Added enumeration for datasource type
    //!+CODE_REVIEW(ah, sep-2011) This class was initializing the connectors in
    //a hard-coded way - why implement generic interfaces and then directly initialze
    //the connectors ? Factoring this out for now into an enumeration class, but
    //the data source type mapping must be factroed into external configuration
    //Changed name of URI data source to XML
    public enum DataSourceType {
        XML("org.bungeni.connector.impl.XMLBungeniConnector"),
        DB("org.bungeni.connector.impl.RDBMSBungeniConnector");
        //SOMETHING_ELSE("org.implementing.class.forConnector")

        private final String connectorClass;

        DataSourceType(String connectorClass) {
            this.connectorClass = connectorClass;
        }

        public IBungeniConnector getDataSourceConnector(){
            IBungeniConnector iInstance = null;
            try {
                Class clsConnector = Class.forName(this.connectorClass);
                iInstance = (IBungeniConnector) clsConnector.newInstance();
            } catch (InstantiationException ex) {
                logger.error("getDataSourceConnector : error", ex);
            } catch (IllegalAccessException ex) {
                logger.error("getDataSourceConnector : error", ex);
            } catch (ClassNotFoundException ex) {
                logger.error("getDataSourceConnector : error", ex);
            }
            return iInstance;
        }

        @Override
        public String toString(){
            return "DataSourceType :  " + name();
        }
    }

    
    IBungeniConnector bungeniConnector = null;
    
    private static final String RELATIVE_ROOT_FOR_URI = System.getProperty("user.dir") + java.io.File.separator;

    public DataSourceServer() {
        logger.info("DataSourceServer() constructor");
    }

    /**
     * After getting a DataSourceServer instance you will have to set it up
     * by specifying its configuration via loadProperties()
     * @return
     */
    public static DataSourceServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSourceServer();
        }
        return INSTANCE;
    }

    /**
     * Loads a properties file. The file is loaded from a path.
     * @param propertiesFile
     * @return
     */
    public void loadProperties(String propertiesFile) {
        this.connectorProps  = new ConnectorProperties(propertiesFile);
        this.loadProperties(this.connectorProps.getProperties());
    }

    /**
     * Converts a relative URI to an absolute URL
     * @param relativeURI
     * @return
     */
    private String getAbsoluteURL(String relativeURI) {
        String sURL = "";
        try {
            String relativePath = relativeURI.replace("/", File.separator);
            File furi = new File(RELATIVE_ROOT_FOR_URI + relativePath);
            sURL =  furi.toURI().toURL().toExternalForm();
        } catch (MalformedURLException ex) {
            logger.error("Error while generating URL to externalForm", ex);
        }
        return sURL;
    }


    /**
     * Loads the properties for starting the data source server.
     * Expects a loaded Properties object 
     * @param properties
     * @return
     */
     boolean loadProperties(Properties properties) {
        boolean loaded = false;
            try {
                setServerPort(Integer.valueOf(properties.getProperty("server-port")));
            } catch (Exception ex) {
                logger.error("Invalid server-port", ex);
            }
            try {
                String source = properties.getProperty("data-source-type");
                if (source == null || !(source.equals(DataSourceType.DB.name()) || source.equals(DataSourceType.XML.name()))) {
                    throw new Exception("Invalid bungeni-connector-data-source-type");
                } else {
                    this.sourceType = DataSourceType.valueOf(source);
                }
            } catch (Exception ex) {
                logger.error("Error while determining datasource type ! ", ex);
            }
            if ( this.sourceType  == DataSourceType.XML) {
                //!+CODE_REVIEW (ah,sep-2011) WHy is this being done here ?
                //these parameters are required by the XML connector
                //!+XML_DATASOURCE(ah,sep-2011) XML urls are relative uris these
                //are converted to absolute URLs
                this.membersURI = getAbsoluteURL(properties.getProperty("uri-members"));
                this.motionsURI = getAbsoluteURL(properties.getProperty("uri-motions"));
                this.questionsURI = getAbsoluteURL(properties.getProperty("uri-questions"));
                this.billsURI = getAbsoluteURL(properties.getProperty("uri-bills"));
                this.metadataInfoURI = getAbsoluteURL(properties.getProperty("uri-metadata-info"));
                //!+DATASOURCE_TYPE(ah, sep-2011) initialize using the enumerator
                //Factory API
                //!+CODE_REVIEW(ah, sep-2011) switch to using the Factory API
                //rather than the explicit object
                this.bungeniConnector = this.sourceType.getDataSourceConnector();
                this.bungeniConnector.init(this.connectorProps);
            } else {
                this.bungeniConnector = this.sourceType.getDataSourceConnector();
                this.bungeniConnector.init(this.connectorProps);
            }
            loaded = true;
            logger.info("Properties loaded");
            return loaded;
    }

    public void setServerPort(int nPort) {
        serverPort = nPort;
    }

    public boolean startServer() {
        //!+CODE_REVIEW_DATASOURCESERVER(ah,sep-2011) Why have DataSourceServer object here,
        //we are already in a DataSourceServer object!
        //DataSourceServer ts = null;
        boolean bstate = false;
        try {
            if (serverComponent == null) {
                serverComponent = new Component();
                serverComponent.getServers().add(Protocol.HTTP, serverPort);
                //!+CODE_REVIEW_DATASOURCESERVER(ah,sep-2011) Why have DataSourceServer object here,
                //we are already in a DataSourceServer object!
                //ts = new DataSourceServer();
                serverComponent.getDefaultHost().attach(this);
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
           bstate = true;
        } catch (Exception ex) {
            bstate = false;
            logger.error("Error starting DataSourceServer" , ex);
            ex.printStackTrace(System.out);
        }
        return bstate;
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

    public DataSourceType getSource() {
        return this.sourceType;
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
