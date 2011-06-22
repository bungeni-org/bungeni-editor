/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.test;

import java.util.List;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.entity.Bill;
import org.bungeni.connector.entity.MetadataInfo;
import org.bungeni.connector.entity.Motion;
import org.bungeni.connector.entity.Person;
import org.bungeni.connector.entity.Question;

/**
 *
 * @author Dave
 */
public class Client {

    public static void main(String args[]) {
        BungeniConnector b = new BungeniConnector();
        List<MetadataInfo> metadata = b.getMetadataInfo();

        if (metadata != null) {
            System.out.println(":::::::::::::::METADATAINFO:::::::::::::::::::");
            for (int i = 0; i < metadata.size(); i++) {
                System.out.println(metadata.get(i).getKeyName() + " " + metadata.get(i).getKeyType()+ " " + metadata.get(i).getKeyValue());
            }
        }

        List<Person> members = b.getMembers();

        if (members != null) {
            System.out.println(":::::::::::::::MEMBERS:::::::::::::::::::");
            for (int i = 0; i < members.size(); i++) {
                System.out.println(members.get(i).getFirstName() + " " + members.get(i).getLastName());
            }
        }

        List<Bill> bills = b.getBills();

        if (bills != null) {
            System.out.println(":::::::::::::::BILLS:::::::::::::::::::");
            for (int i = 0; i < bills.size(); i++) {
                System.out.println(bills.get(i).getBillUri() + " " + bills.get(i).getBillName());
            }
        }

        List<Motion> motions = b.getMotions();

        if (motions != null) {
            System.out.println(":::::::::::::::MOTIONS:::::::::::::::::::");
            for (int i = 0; i < motions.size(); i++) {
                System.out.println(motions.get(i).getMotionUri() + " " + motions.get(i).getMotionTitle());
            }
        }


        List<Question> questions = b.getQuestions();

        if (questions != null) {
            System.out.println(":::::::::::::::QUESTIONS:::::::::::::::::::");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i).getQuestionTitle() + " " + questions.get(i).getQuestionText());
            }
        }
        b.stopServer();

    }
}
