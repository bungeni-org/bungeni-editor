package org.bungeni.connector;

import java.util.List;
// always explicit import rather than .* for better readability
import org.bungeni.connector.element.*;

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
    public List<JudgementDomain> getJudgementDomains();
    public List<JudgementCourt> getJudgementCourts(); 
     public List<JudgementRegion> getJudgementRegions();
    public List<JudgementLitigationType> getJudgementLitigationTypes();
    public List<ActType> getActTypes();
    public List<ActScope> getActScopes();
    public List<ActFamily> getActFamilies();
    public List<ActHistoricalPeriod> getActHistoricalPeriods();
    public List<SourceType> getSourceTypes();
    public List<Motion> getMotions();
    public List<Question> getQuestions();  
    public List<MetadataInfo> getMetadataInfo();
    public List<Document> getDocuments();
    public List<Committee> getCommittees();
    public List<PublicationName> getPublicationNames();
    public List<ActClassification> getActClassifications();

}
