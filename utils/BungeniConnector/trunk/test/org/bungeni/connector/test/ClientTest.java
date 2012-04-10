package org.bungeni.connector.test;

import java.io.File;
import java.util.List;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.Act;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Document;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;
import org.bungeni.connector.server.DataSourceServer;

/**
 *
 * @author Dave
 */
public class ClientTest {

    public static void main(String args[]) {
        //start server
        ConnectorProperties cp = new ConnectorProperties(System.getProperty("user.dir")+ File.separator + "settings" + File.separator + "bungeni-connector.properties");        
        DataSourceServer.getInstance().loadProperties(cp);
        DataSourceServer.getInstance().startServer();

        BungeniConnector b = new BungeniConnector();
        b.init(cp);
        List<MetadataInfo> metadata = b.getMetadataInfo();

        if (metadata != null) {
            System.out.println(":::::::::::::::METADATAINFO:::::::::::::::::::");
            for (int i = 0; i < metadata.size(); i++) {
                System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType()+ " " + metadata.get(i).getValue());
            }
        }

        List<Member> members = b.getMembers();

        if (members != null) {
            System.out.println(":::::::::::::::MEMBERS:::::::::::::::::::");
            for (int i = 0; i < members.size(); i++) {
                System.out.println(members.get(i).getFirst() + " " + members.get(i).getLast());
            }
        }

        List<Bill> bills = b.getBills();

        if (bills != null) {
            System.out.println(":::::::::::::::BILLS:::::::::::::::::::");
            for (int i = 0; i < bills.size(); i++) {
                System.out.println(bills.get(i).getUri() + " " + bills.get(i).getName());
            }
        }

        List<Motion> motions = b.getMotions();

        if (motions != null) {
            System.out.println(":::::::::::::::MOTIONS:::::::::::::::::::");
            for (int i = 0; i < motions.size(); i++) {
                System.out.println(motions.get(i).getUri() + " " + motions.get(i).getTitle());
            }
        }


        List<Question> questions = b.getQuestions();

        if (questions != null) {
            System.out.println(":::::::::::::::QUESTIONS:::::::::::::::::::");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i).getTitle() + " " + questions.get(i).getText());
            }
        }

        List<Act> acts = b.getActs();

        if (acts != null) {
            System.out.println(":::::::::::::::ACTS:::::::::::::::::::");
            for (int i = 0; i < acts.size(); i++) {
                System.out.println(acts.get(i).getName() + " " + questions.get(i).getId());
            }
        } else {
             System.out.println("Error : Acts missing");
        }

        List<Document> documents = b.getDocuments();

        if (documents == null) {
            System.out.println("Error : Documents missing");
        }
        else
        {
            System.out.println(":::::::::::::::DOCUMENTS:::::::::::::::::::");
            for (int i = 0; i < documents.size(); i++) {
                System.out.println(documents.get(i).getTitle() + " " + documents.get(i).getUri());
            }
        }

        b.closeConnector();
        DataSourceServer.getInstance().stopServer();

    }
}
