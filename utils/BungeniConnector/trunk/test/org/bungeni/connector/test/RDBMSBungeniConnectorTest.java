
package org.bungeni.connector.test;

import org.bungeni.connector.impl.*;
import java.util.List;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;

/**
 *
 * @author Dave
 */
public class RDBMSBungeniConnectorTest {

    public static void main(String args[]) {
        RDBMSBungeniConnector connector = new RDBMSBungeniConnector();
        List<Member> members = connector.getMembers();
        if (members != null) {
            System.out.println(":::::::::::::::MEMBERS:::::::::::::::::::");
            for (int i = 0; i < members.size(); i++) {
                System.out.println(members.get(i).getFirst() + " " + members.get(i).getLast());
            }
        }
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
        connector.close();

    }
}
