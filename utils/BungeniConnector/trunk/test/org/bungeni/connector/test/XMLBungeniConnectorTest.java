/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.test;

import java.util.List;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Question;
import org.bungeni.connector.impl.XMLBungeniConnector;

/**
 *
 * @author Dave
 */
public class XMLBungeniConnectorTest{

    public XMLBungeniConnectorTest() {
    }


    public static void main(String args[]) {
        XMLBungeniConnector connector = new XMLBungeniConnector();
        List<Bill> bills = connector.getBills();
        if (bills != null) {
            System.out.println(":::::::::::::::BILLS:::::::::::::::::::");
            for (int i = 0; i < bills.size(); i++) {
                System.out.println(bills.get(i).getUri() + " " + bills.get(i).getName());
            }
        }
        List<Motion> motions = connector.getMotions();

        if (motions != null) {
            System.out.println(":::::::::::::::MOTIONS:::::::::::::::::::");
            for (int i = 0; i < motions.size(); i++) {
                System.out.println(motions.get(i).getUri() + " " + motions.get(i).getTitle());
            }
        }

        List<Question> questions = connector.getQuestions();

        if (questions != null) {
            System.out.println(":::::::::::::::QUESTIONS:::::::::::::::::::");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i).getTitle() + " " + questions.get(i).getText());
            }
        }

        List<MetadataInfo> meta = connector.getMetadataInfo();

        if (meta != null) {
            System.out.println(":::::::::::::::METADATA:::::::::::::::::::");
            for (int i = 0; i < meta.size(); i++) {
                System.out.println(meta.get(i).getName() + " " + meta.get(i).getValue());
            }
        }
    }
}
