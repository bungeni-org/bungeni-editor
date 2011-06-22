/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.restlet.current;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.bungeni.connector.entity.MetadataInfo;
import org.bungeni.connector.entity.controller.MetadataInfoJpaController;
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
public class MetadataInfoRestlet extends Restlet {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";

    /**
     * MetadataInfoRestletHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class MetadataInfoRestletHolder {

        public static final MetadataInfoRestlet INSTANCE = new MetadataInfoRestlet();
    }

    public static MetadataInfoRestlet getInstance() {
        return MetadataInfoRestletHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            if (request.getMethod().equals(Method.GET)) {
                response.setStatus(Status.SUCCESS_OK);
                MetadataInfoJpaController controller = new MetadataInfoJpaController();
                List<MetadataInfo> items = controller.findMetadataInfoEntities();
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(MetadataInfo.METADATA_ALIAS, MetadataInfo.class);
                //xStream.("unique", PersonPK.class);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    builder.append(xStream.toXML(items.get(i))).append("\n");
                }
                String list = builder.toString();
                if (!list.isEmpty()) {
                    response.setEntity(HEADER + list + FOOTER, MediaType.TEXT_XML);
                } else {
                }


            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
