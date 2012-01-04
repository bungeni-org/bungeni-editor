package org.bungeni.connector;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * This loads a Connector Properties file and provides access to it
 * @author Ashok
 */
public class ConnectorProperties {

  
    private Properties connectorProperties = null;

    private static Logger logger = Logger.getLogger(ConnectorProperties.class.getName());

    public ConnectorProperties(String fileProperties) {
        connectorProperties =  this.loadProperties(fileProperties);
    }

    public ConnectorProperties(Properties props) {
        connectorProperties = props;
    }

    public Properties getProperties(){
        return this.connectorProperties;
    }
    
    /**
     * Loads a properties file. The file is loaded from a path.
     * @param propertiesFile
     * @return
     */
    private Properties loadProperties(String propertiesFile) {
        logger.info("loadProperties: loading datasourceserver properties file from " + propertiesFile);
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(propertiesFile);
            properties.load(in);
        } catch (IOException ex) {
            logger.error("unable to find properties file :" + propertiesFile, ex);
        }
        return properties;
    }

    

   
}
