/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.test;

import org.bungeni.connector.server.DataSourceServer;

/**
 *
 * @author Dave
 */
public class ServerTest {

    public static void main(String[] args) {
        DataSourceServer trans = DataSourceServer.getInstance();
        trans.startServer();

    }
}
