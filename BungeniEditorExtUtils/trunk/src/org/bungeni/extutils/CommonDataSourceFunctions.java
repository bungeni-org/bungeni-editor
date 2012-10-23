package org.bungeni.extutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * Access the connector properties file
 * @author Ashok Hariharan
 */
public class CommonDataSourceFunctions {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonDataSourceFunctions.class.getName());


    public static Properties getDataSourceProperties() throws IOException {
            Properties dsProps = null;
            String strUserDirPath = System.getProperty("user.dir");
            log.info("getDataSourceProperties (user.dir) : " + strUserDirPath );
            String dsPropsFile = strUserDirPath + File.separator + "settings" + File.separator + "bungeniconnector.properties";
            File fPropsFile = new File(dsPropsFile);
            if (fPropsFile.exists()) {
                    FileInputStream fPropsStream = null ;
                    try {
                        fPropsStream = new FileInputStream(fPropsFile);
                        dsProps = new Properties();
                        dsProps.load(fPropsStream);
                    } catch (FileNotFoundException ex) {
                            log.error("getDefaultLookAndFeel : " + ex.getMessage());
                         } catch (IOException ex) {
                            log.error("getDefaultLookAndFeel : " + ex.getMessage());
                         }
            }
           return dsProps;
        }

}
