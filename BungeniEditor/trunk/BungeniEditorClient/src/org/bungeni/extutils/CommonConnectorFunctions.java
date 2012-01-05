/*
 *  Copyright (C) 2012 windows
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

package org.bungeni.extutils;

import java.io.IOException;
import java.util.Properties;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.server.DataSourceServer;
import org.bungeni.ds.DataSourceFactory;

/**
 * Common library for interfacing with BungeniConnector 
 * @author Reagan
 */
public class CommonConnectorFunctions {

    public static DataSourceServer startDSServer() throws IOException {
         DataSourceServer dss = DataSourceServer.getInstance();
         Properties dsProps = DataSourceFactory.getDataSourceProperties();
         dss.loadProperties(dsProps);
         dss.startServer();
         return dss;
    }
    
    
    public static BungeniConnector getDSClient() throws IOException{
        BungeniConnector client = new BungeniConnector();
        client.init(new ConnectorProperties(DataSourceFactory.getDataSourceProperties()));
        return client;
    }


}