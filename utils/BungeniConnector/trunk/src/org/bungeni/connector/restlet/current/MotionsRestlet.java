/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.restlet.current;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.bungeni.connector.entity.controller.MotionJpaController;
import org.bungeni.connector.entity.Motion;
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
public class MotionsRestlet extends Restlet  {
    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";
   /**
    * MotionsRestletHolder is loaded on the first execution of Singleton.getInstance()
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
   private static class MotionsRestletHolder {
     public static final MotionsRestlet INSTANCE = new MotionsRestlet();
   }

   public static MotionsRestlet getInstance() {
     return MotionsRestletHolder.INSTANCE;
   }
    @Override
    public void handle(Request request, Response response) {
        System.out.println("handling method : " + request.getMethod().getName());
        try {
            if (request.getMethod().equals(Method.GET)) {
                response.setStatus(Status.SUCCESS_OK);
                response.setStatus(Status.SUCCESS_OK);
                MotionJpaController motionController = new MotionJpaController();
                List<Motion> motions = motionController.findMotionEntities();
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(Motion.MOTION_ALIAS, Motion.class);
                   StringBuilder builder = new StringBuilder();
                for (int i = 0; i < motions.size(); i++) {
                    builder.append(xStream.toXML(motions.get(i))).append("\n");
                }
                String content = builder.toString();
                if (!content.isEmpty()) {
                    response.setEntity(HEADER + content + FOOTER, MediaType.TEXT_XML);
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
