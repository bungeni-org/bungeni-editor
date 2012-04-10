package org.bungeni.connector.restlet.current;

import org.bungeni.connector.server.DataSourceServer;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;

/**
 *
 * @author Dave
 */
public class StopServerRestlet extends Restlet {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";

    /**
     * BillsRestletHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class RestletHolder {

        public static final StopServerRestlet INSTANCE = new StopServerRestlet();
    }

    public static StopServerRestlet getInstance() {
        return RestletHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            response.setStatus(Status.SUCCESS_OK);
            DataSourceServer.getInstance().stopServer();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
