/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector;

import java.util.List;
import org.bungeni.connector.entity.Bill;
import org.bungeni.connector.entity.MetadataInfo;
import org.bungeni.connector.entity.Motion;
import org.bungeni.connector.entity.Person;
import org.bungeni.connector.entity.Question;

/**
 *
 * @author Dave
 */
public interface IBungeniConnector {

    public List<Person> getMembers();
    public List<Bill> getBills();
    public List<Motion> getMotions();
    public List<Question> getQuestions();
    public List<MetadataInfo> getMetadataInfo();
    public void stopServer();

}
