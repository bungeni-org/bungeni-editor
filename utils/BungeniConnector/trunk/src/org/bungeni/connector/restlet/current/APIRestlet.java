
package org.bungeni.connector.restlet.current;

import org.bungeni.connector.server.DataSourceServer;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

/**
 *
 * @author Dave
 */
public class APIRestlet extends Restlet {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";
    DataSourceServer dss;

    /**
     * BillsRestletHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class ActTypesRestletHolder {

        public static final APIRestlet INSTANCE = new APIRestlet();
    }

    public static APIRestlet getInstance() {

        return ActTypesRestletHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            if (request.getMethod().equals(Method.GET)) {
                response.setStatus(Status.SUCCESS_OK);
                StringBuilder builder = new StringBuilder();
//                builder.append(DataSourceServer.metadataInfoRoute+" For current metadata info").append("\n");
//                builder.append(DataSourceServer.billsRoute+" For current bills").append("\n");
//                builder.append(DataSourceServer.membersRoute+" For current members").append("\n");
//                builder.append(DataSourceServer.motionsRoute+" For current motions").append("\n");
//                builder.append(DataSourceServer.questionsRoute+" For current questions").append("\n");
//                builder.append(DataSourceServer.STOP_SERVER_ROUTE+" To shut down server ").append("\n");
                response.setEntity(builder.toString(), MediaType.TEXT_PLAIN);
            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
