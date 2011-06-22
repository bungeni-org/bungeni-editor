/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.server;

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

    public static final String BASE_ROUTE = "/current/";
    public static final String STOP_SERVER_ROUTE = "/stop_server";
    public static final String MEMEBERS_ROUTE = BASE_ROUTE + "members";
    public static final String MOTIONS_ROUTE = BASE_ROUTE + "motions";
    public static final String QUESTIONS_ROUTE = BASE_ROUTE + "questions";
    public static final String BILLS_ROUTE = BASE_ROUTE + "bills";
    public static final String METADATA_INFO_ROUTE = BASE_ROUTE + "metadata";
    private static Component serverComponent = null;
    private static int SERVER_PORT = 8899;

    public static void setServerPort(int nPort) {
        SERVER_PORT = nPort;
    }

    public static DataSourceServer startServer() {
        DataSourceServer ts = null;
        try {
            if (serverComponent == null) {
                serverComponent = new Component();
                serverComponent.getServers().add(Protocol.HTTP, SERVER_PORT);
                ts = new DataSourceServer();
                serverComponent.getDefaultHost().attach(ts);
                serverComponent.start();
            } else {
                if (serverComponent.isStopped()) {
                    serverComponent.start();
                } else {
                    System.out.println("DataSourceServer is already running on port " + SERVER_PORT);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            return ts;
        }
    }

    public static boolean stopServer() {
        boolean stopped = false;
        try {
            if (serverComponent != null) {
                serverComponent.stop();
                stopped = true;
                    System.out.println("DataSourceServer stopped");

            } else {
                System.out.println("DataSourceServer is not running");
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            return stopped;
        }
    }

    @Override
    public Restlet createRoot() {
        Router router = new Router(getContext());
        router.attach(METADATA_INFO_ROUTE, MetadataInfoRestlet.getInstance());
        router.attach(BILLS_ROUTE, BillsRestlet.getInstance());
        router.attach(MEMEBERS_ROUTE, MembersRestlet.getInstance());
        router.attach(MOTIONS_ROUTE, MotionsRestlet.getInstance());
        router.attach(QUESTIONS_ROUTE, QuestionsRestlet.getInstance());
        router.attach(STOP_SERVER_ROUTE, StopServerRestlet.getInstance());
        router.attach("/", APIRestlet.getInstance());

        return router;
    }
}
