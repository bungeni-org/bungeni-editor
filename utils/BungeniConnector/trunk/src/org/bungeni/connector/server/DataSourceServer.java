package org.bungeni.connector.server;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.restlet.current.*;
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
    private String judgementDomainsRoute = "/current/judgementDomains";
    private String judgementRegionsRoute = "/current/judgementRegions";
    
    private String judgementLitigationTypesRoute = "/current/judgementLitigationTypes";
    private String actCategoriesRoute = "/current/actCategories";
    private String actCategoriesBasicRoute = "/current/actCategoriesBasic";
   
    
    private String judgementCourtsRoute = "/current/judgementCourts";
    private String srcNamesRoute = "/current/srcNames";
    private String actTypesRoute = "/current/actTypes";
    private String actScopesRoute = "/current/actScopes";
    private String actOrganizationsRoute = "/current/actOrganizations";
    private String actFamiliesRoute = "/current/actFamilies";
    private String actHistoricalPeriodsRoute = "/current/actHistoricalPeriods";
    
    private String sourceTypesRoute = "/current/sourceTypes";
    private String metadataInfoRoute = "/current/metadata";
    private String documentsRoute = "/current/documents";
    private String committeesRoute = "/current/committees";
    //!+CODE_REVIEW WHy are these URI variables here ? they are related to the
    //XMLConnector implemenetation they should not be here !
    private String membersURI = null;
    private String motionsURI = null;
    private String questionsURI = null;
    private String billsURI = null;
    private String judgementDomainsURI = null;
    private String judgementRegionsURI = null;
   
    private String actCatergoriesURI = null;
    private String actCatergoriesBasicURI = null;
    
    private String judgementLitigationTypesURI = null;
    
    private String judgementCourtsURI = null;
    
    private String actFamiliesURI = null;
    private String srcNamesURI = null;
    private String actOrganizationsURI = null;
    private String actHistoricalPeriodsURI = null;
    private String actTypesURI = null;
    private String actScopesURI = null;
    private String sourceTypesURI = null;
    private String metadataInfoURI = null;
    private String documentsURI = null ;
    private String committeesURI = null ;
    private Component serverComponent = null;
    private int serverPort = 8899;
    private static Logger logger = Logger.getLogger(BungeniConnector.class.getName());
    static DataSourceServer INSTANCE = null;
    private ConnectorProperties connectorProps = null;
    private DataSourceServer.DataSourceType sourceType = DataSourceServer.DataSourceType.DB;

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

        public IBungeniConnector getDataSourceConnector() {
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
        public String toString() {
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
     * by specifying its configuration via init()
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
        this.connectorProps = new ConnectorProperties(propertiesFile);
        this.init(this.connectorProps.getProperties());
    }


    /**
     * Loads a properties file. The file is loaded from a path.
     * @param Properties object
     * @return
     */
    public void loadProperties(Properties properties) {
        this.connectorProps = new ConnectorProperties(properties);
        this.init(this.connectorProps.getProperties());
    }

    public void loadProperties(ConnectorProperties cp) {
        this.connectorProps = cp;
        this.init(this.connectorProps.getProperties());
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
            sURL = furi.toURI().toURL().toExternalForm();
        } catch (MalformedURLException ex) {
            logger.error("Error while generating URL to externalForm", ex);
        }
        return sURL;
    }

    /**
     * Initializes the data source server after loading properties for startup.
     * Expects a loaded Properties object 
     * @param properties
     * @return
     */
    boolean init(Properties properties) {
        boolean loaded = false;
        try {
            setServerPort(Integer.valueOf(properties.getProperty("server-port")));
        } catch (Exception ex) {
            logger.error("Invalid server-port", ex);
        }
        // !+DATA_SOURCE_CLASS (ah, nov-2011) -- REMOVED
        //There was no check here for the Appropriate data source type
        //adding it for data source type
        String sDataSourceType = properties.getProperty("data-source-type");
        this.sourceType = DataSourceServer.DataSourceType.valueOf(sDataSourceType);
        
        try {
            bungeniConnector = this.sourceType.getDataSourceConnector();
            if (bungeniConnector == null) {
                throw new Exception("Invalid DataSource Type, faile initialization: " + sDataSourceType);
            }
            bungeniConnector.init(this.connectorProps);
            loaded = true;
        } catch (Exception ex) {
            logger.error("Error while determining datasource class ! ", ex);
        }
        return loaded;
    }

    public void setServerPort(int nPort) {
        serverPort = nPort;
    }

    public boolean startServer() {
        boolean bstate = false;
        if(bungeniConnector==null){
            logger.error("Server cannot be started. Invalid connector state");
            return false;
        }
        try {
            if (serverComponent == null) {
                serverComponent = new Component();
                serverComponent.getServers().add(Protocol.HTTP, serverPort);
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
            logger.error("Error starting DataSourceServer", ex);
            ex.printStackTrace(System.out);
        }
        return bstate;
    }

    public boolean stopServer() {
        if (bungeniConnector != null) {
            bungeniConnector.closeConnector();
        }
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
        router.attach(judgementDomainsRoute, new JudgementDomainsRestlet(bungeniConnector));
       
        router.attach(judgementRegionsRoute, new JudgementRegionsRestlet(bungeniConnector));
        
        router.attach(judgementLitigationTypesRoute, new JudgementLitigationTypesRestlet(bungeniConnector));
        
        router.attach(judgementCourtsRoute, new JudgementCourtsRestlet(bungeniConnector));
         
        router.attach(actCategoriesRoute, new ActCategoriesRestlet(bungeniConnector));
        router.attach(actCategoriesBasicRoute, new ActCategoriesBasicRestlet(bungeniConnector));
        
        router.attach(actOrganizationsRoute, new ActOrganizationsRestlet(bungeniConnector));
        
        router.attach(actTypesRoute, new ActTypesRestlet(bungeniConnector));
        router.attach(actScopesRoute, new ActScopesRestlet(bungeniConnector));
        router.attach(actFamiliesRoute, new ActFamiliesRestlet(bungeniConnector));
        router.attach(actHistoricalPeriodsRoute, new ActHistoricalPeriodsRestlet(bungeniConnector));
        router.attach(srcNamesRoute, new SrcNamesRestlet(bungeniConnector));
        
        
        router.attach(sourceTypesRoute, new SourceTypesRestlet(bungeniConnector));
        router.attach(membersRoute, new MembersRestlet(bungeniConnector));
        router.attach(motionsRoute, new MotionsRestlet(bungeniConnector));
        router.attach(questionsRoute, new QuestionsRestlet(bungeniConnector));
        router.attach(documentsRoute, new DocumentsRestlet(bungeniConnector));
        router.attach(committeesRoute, new CommitteesRestlet(bungeniConnector)) ;
        router.attach(STOP_SERVER_ROUTE, StopServerRestlet.getInstance());
        router.attach("/", APIRestlet.getInstance());
        return router;
    }

    public DataSourceServer.DataSourceType getSource() {
        return this.sourceType;
    }

    public String getBillsURI() {
        return billsURI;
    }
    
    public String getJudgementDomainsURI() {
        return judgementDomainsURI;
    }
    
     public String getJudgementRegionsURI() {
        return judgementRegionsURI;
    }
    
    public String getJudgementLitigationTypesURI() {
        return judgementLitigationTypesURI;
    }
    
    
    public String getJudgementCourtsURI() {
        return judgementCourtsURI;
    }
    
    public String getActTypesURI() {
        return actTypesURI;
    }
    
    
    public String getActFamiliesURI() {
        return actFamiliesURI;
    }
     public String getActHistoricalPeriodsURI() {
        return actHistoricalPeriodsURI;
    }
    
    public String getActScopesURI() {
        return actScopesURI;
    }
    
    public String getActOrganizationURI(){
        return actOrganizationsURI;
    }
    
    public String getActCategoriesURI() {
        return actCatergoriesURI;
    }
    
    public String getActCategoriesBasicURI() {
        return actCatergoriesBasicURI;
    }
    
     public String getSrcNamesURI() {
        return srcNamesURI;
    }
     
     public String getSourceTypesURI() {
        return sourceTypesURI;
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

    public String getCommitteesURI() {
        return committeesURI ;
    }

    public String getDocumentsURI(){
            return documentsURI ;
    }

    public static void main(String[] args) {
        DataSourceServer ds = DataSourceServer.getInstance();
        ds.loadProperties("settings/bungeni-connector.properties");
        ds.startServer();
    }
}