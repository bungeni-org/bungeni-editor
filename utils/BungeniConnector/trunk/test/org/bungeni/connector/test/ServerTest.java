/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.test;

import java.io.File;
import org.bungeni.connector.server.DataSourceServer;

/**
 *
 * @author Dave
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        DataSourceServer trans = DataSourceServer.getInstance();
        trans.loadProperties(System.getProperty("user.dir")+ File.separator + "settings" + File.separator + "bungeni-connector.properties");
        System.out.println("Server Properties : " + System.getProperty("user.dir")+ File.separator + "settings" + File.separator + "bungeni-connector.properties");
        System.out.println("Starting server");
        trans.startServer();
        Thread.sleep(1000);
        System.out.println("Stopping Server");
       // trans.stopServer();

    }
}
