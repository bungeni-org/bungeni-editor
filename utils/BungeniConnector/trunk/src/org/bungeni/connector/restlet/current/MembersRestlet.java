/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.restlet.current;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.bungeni.connector.entity.controller.PersonJpaController;
import org.bungeni.connector.entity.Person;
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
public class MembersRestlet extends Restlet {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";

    /**
     * MembersRestletHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class MembersRestletHolder {

        public static final MembersRestlet INSTANCE = new MembersRestlet();
    }

    public static MembersRestlet getInstance() {
        return MembersRestletHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) {
        try {
            if (request.getMethod().equals(Method.GET)) {
                response.setStatus(Status.SUCCESS_OK);
                PersonJpaController personController = new PersonJpaController();
                List<Person> people = personController.findPersonEntities();
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(Person.PERSON_ALIAS, Person.class);
                //xStream.("unique", PersonPK.class);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < people.size(); i++) {
                    builder.append(xStream.toXML(people.get(i))).append("\n");
                }
                String members = builder.toString();
                if (!members.isEmpty()) {
                    response.setEntity(HEADER + members + FOOTER, MediaType.TEXT_XML);
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
