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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author PC
 */
public class ConnectorFunctions {
        public static ResultSet ConnectMMSM(String dbName, String sqlStm) {
        String connectionUrl = "jdbc:sqlserver://l-share;databaseName="+ dbName+ ";selectMethod=cursor";

        // Declare the JDBC objects.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(connectionUrl, "sayesh", "QWE!@#qwe");

            // Create and execute an SQL statement that returns some data.
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlStm);
        } // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return rs;
        }
    }

}
