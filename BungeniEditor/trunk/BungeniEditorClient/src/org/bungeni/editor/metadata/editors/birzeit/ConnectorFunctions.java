/*
 * Copyright (C) 2013 PC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.metadata.editors.birzeit;

import com.mysql.jdbc.Field;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.server.DataSourceServer;
import org.bungeni.extutils.CommonDataSourceFunctions;

/**
 * Common library for interfacing with BungeniConnector
 *
 * @author Reagan
 */
public class ConnectorFunctions {

    public static DataSourceServer startDSServer() throws IOException {
        DataSourceServer dss = DataSourceServer.getInstance();
        Properties dsProps = CommonDataSourceFunctions.getDataSourceProperties();
        dss.loadProperties(dsProps);
        dss.startServer();
        return dss;
    }

    // !+BUNGENI_CONNECTOR(Ashok ,05-01-2012)
    // This method changes the way classes acting as BungeniConnector
    // clients initialise themselves to ensure that they load
    //  the properties using the REST API rather than directly
    public static BungeniConnector getDSClient() throws IOException {
        BungeniConnector client = new BungeniConnector();
        client.init(new ConnectorProperties(CommonDataSourceFunctions.getDataSourceProperties()));
        return client;
    }

    public static Connection ConnectMMSM(String dbName) {
        
         String connectionUrl = "";

 //       String connectionUrl = "jdbc:sqlserver://BZUADMIN-HP;instance=SQLEXPRESS;databaseName=Muqtafi_test;selectMethod=cursor;CharacterSet=UTF-8";
        // Declare the JDBC objects.
        Connection con = null;

        //   ResultSet rs = null;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            System.setProperty("java.library.path", "C:\\Users\\bzuadmin\\Desktop\\BungeniClientEditor\\BungeniEditorClient\\lib\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86\\sqljdbc_auth.dll");
//
//            java.lang.reflect.Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
//            fieldSysPath.setAccessible(true);
//            fieldSysPath.set(null, null);
            //   con = DriverManager.getConnection(connectionUrl, "sayesh", "QWE!@#qwe"); 
            con = DriverManager.getConnection(connectionUrl, "***", "****");
        } // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return con;
        }
    }
}
