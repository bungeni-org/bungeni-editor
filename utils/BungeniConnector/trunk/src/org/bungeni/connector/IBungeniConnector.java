package org.bungeni.connector;

import java.util.List;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Committee;
import org.bungeni.connector.element.Document;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;

/**
 *
 * @author Dave
 */
public interface IBungeniConnector {

    //!+BUNGENI_CONNECTOR (ah, sep-2011) added an init() to the interface,
    //since we need to pass in a connection properties object
    /**
     * Initializes the Connector object with the ConnectorProperties object
     * @param props
     */
    public void init(ConnectorProperties props);
    /**
     * Closes the connector, any cleanup code for the connector needs to be put
     * in this function
     */
    public void closeConnector();

    /**
     * Data retrieval APIs
     * @return
     */
    public List<Member> getMembers();
    public List<Bill> getBills();
    public List<Motion> getMotions();
    public List<Question> getQuestions();
    public List<MetadataInfo> getMetadataInfo();
    public List<Document> getDocuments();
    public List<Committee> getCommittees();

}
