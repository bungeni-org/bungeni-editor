/*
 *  Copyright (C) 2011 Africa i-Parliaments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.ds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Ashok
 */
public class DataSourceFactory {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataSourceFactory.class.getName());

    /**
     * Returns the active look and feel
     * @return
     */
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
