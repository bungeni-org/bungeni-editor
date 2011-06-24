/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector;

import java.util.List;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;

/**
 *
 * @author Dave
 */
public interface IBungeniConnector {

    public List<Member> getMembers();
    public List<Bill> getBills();
    public List<Motion> getMotions();
    public List<Question> getQuestions();
    public List<MetadataInfo> getMetadataInfo();

}
