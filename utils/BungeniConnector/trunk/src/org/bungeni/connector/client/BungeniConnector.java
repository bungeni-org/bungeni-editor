/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.entity.Bill;
import org.bungeni.connector.entity.MetadataInfo;
import org.bungeni.connector.entity.Motion;
import org.bungeni.connector.entity.Person;
import org.bungeni.connector.entity.Question;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

/**
 *
 * @author Dave
 */
public class BungeniConnector implements IBungeniConnector {

    static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n<package>";
    static final String FOOTER = "\n</package>";
    static final String BASE_URL = "http://localhost:8899/current/";
    static final String METADATA_URL = BASE_URL + "metadata";
    static final String MEMEBERS_URL = BASE_URL + "members";
    static final String MOTIONS_URL = BASE_URL + "motions";
    static final String QUESTIONS_URL = BASE_URL + "questions";
    static final String BILLS_URL = BASE_URL + "bills";
    static final String STOP_URL = "http://localhost:8899/stop_server";
    static final String PACKAGE_ALIAS = "package";
    static final String SERVER_UNREACHABLE = " did not respond";

    private List getList(String url, String alias, Class aliasClass) {
        ClientResource resource = new ClientResource(url);
        try {
            Status status = resource.getStatus();
            if (Status.SUCCESS_OK.equals(status)) {
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(PACKAGE_ALIAS, List.class);
                xStream.alias(alias, aliasClass);
                String xml = resource.get().getText();
                if (xml != null) {
                    return (List) xStream.fromXML(xml);
                }
            } else {
                if (status != null) {
                    Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, status.getDescription());
                } else {
                    Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, url + SERVER_UNREACHABLE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, url + SERVER_UNREACHABLE, ex.getMessage());
        }
        return null;
    }

    public List<Person> getMembers() {
        return getList(MEMEBERS_URL, Person.PERSON_ALIAS, Person.class);
    }

    public List<Bill> getBills() {
        return getList(BILLS_URL, Bill.BILL_ALIAS, Bill.class);
    }

    public List<Motion> getMotions() {
        return getList(MOTIONS_URL, Motion.MOTION_ALIAS, Motion.class);
    }

    public List<Question> getQuestions() {
        return getList(QUESTIONS_URL, Question.QUESTION_ALIAS, Question.class);
    }

    public List<MetadataInfo> getMetadataInfo() {
        return getList(METADATA_URL, MetadataInfo.METADATA_ALIAS, MetadataInfo.class);
    }

    public void stopServer() {
        ClientResource resource = new ClientResource(STOP_URL);
        try {
            Status status = resource.getStatus();
            if (Status.SUCCESS_OK.equals(status)) {
                Logger.getLogger(BungeniConnector.class.getName()).log(Level.INFO, "Server stopped");
            } else {
                if (status != null) {
                    Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, status.getDescription());
                } else {
                    Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, SERVER_UNREACHABLE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BungeniConnector.class.getName()).log(Level.SEVERE, STOP_URL + SERVER_UNREACHABLE, ex.getMessage());
        }
    }
}
