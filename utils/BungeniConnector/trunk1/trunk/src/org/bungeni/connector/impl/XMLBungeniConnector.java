package org.bungeni.connector.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.*;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

/**
 *
 * @author Dave
 */
public class XMLBungeniConnector implements IBungeniConnector {

    private static final String RELATIVE_ROOT_FOR_URI = System.getProperty("user.dir") + java.io.File.separator;
    //!+CODE_REVIEW(ah, sep-2011) -- the URI variables below hard code the info.
    //the same info can be generated out of the connector properties
    //see RDBMSConnector for an example 
    static final String SERVER_UNREACHABLE = " did not respond";
    private static Logger logger = Logger.getLogger(XMLBungeniConnector.class.getName());
    private String metadataInfoPackageAlias = null;
    private String metadataInfoAlias = null;
    private String metadataInfoIdAlias = null;
    private String metadataInfoTypeAlias = null;
    private String metadataInfoNameAlias = null;
    private String metadataInfoValueAlias = null;
    private String metadataInfoSourceURI = null;

    private String membersPackageAlias = null;
    private String memberAlias = null;
    private String memberIdAlias = null;
    private String memberUriAlias = null;
    private String memberFirstNameAlias = null;
    private String memberLastNameAlias = null;
    private String memberRoleAlias = null;
    private String memberSourceURI = null;

    private String billsPackageAlias = null;
    private String billAlias = null;
    private String billIdAlias = null;
    private String billUriAlias = null;
    private String billNameAlias = null;
    private String billOntologyAlias = null;
    private String billCountryAlias = null;
    private String billsSourceURI = null;

    private String actsPackageAlias = null;
    private String actAlias = null;
    private String actIdAlias = null;
    private String actNameAlias = null;
    private String actsSourceURI = null;
    
    private String motionsPackageAlias = null;
    private String motionAlias = null;
    private String motionIdAlias = null;
    private String motionUriAlias = null;
    private String motionNameAlias = null;
    private String motionTitleAlias = null;
    private String motionByAlias = null;
    private String motionTextAlias = null;
    private String motionsSourceURI = null;

    private String questionsPackageAlias = null;
    private String questionAlias = null;
    private String questionIdAlias = null;
    private String questionTitleAlias = null;
    private String questionFromAlias = null;
    private String questionToAlias = null;
    private String questionTextAlias = null;
    private String questionsSourceURI = null;

    private String documentsPackageAlias = null;
    private String documentAlias = null;
    private String documentIdAlias = null;
    private String documentTitleAlias = null;
    private String documentDateAlias = null;
    private String documentSourceAlias = null;
    private String documentUriAlias = null;
    private String documentSittingAlias = null;
    private String documentsSourceURI = null;

    private String committeesSourceURI = null ;
    private String committeesPackageAlias = null;
    private String committeetAlias = null ;
    private String committeeIdAlias = null ;
    private String committeeNameAlias = null ;
    private String committeeUriAlias = null ;
    private String committeeCountryAlias = null ;
    
    public XMLBungeniConnector() {
    }

    public void init(ConnectorProperties props) {
        /**
         * Generating URI variables from connector properties
         */
        this.metadataInfoSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-metadata-info"));
        this.metadataInfoPackageAlias = props.getProperties().getProperty("xml-metadata-info-package-alias");
        this.metadataInfoAlias = props.getProperties().getProperty("xml-metadata-info-alias", "");
        this.metadataInfoIdAlias = props.getProperties().getProperty("xml-metadata-info-id-alias");
        this.metadataInfoTypeAlias = props.getProperties().getProperty("xml-metadata-info-type-alias");
        this.metadataInfoNameAlias = props.getProperties().getProperty("xml-metadata-info-name-alias");
        this.metadataInfoValueAlias = props.getProperties().getProperty("xml-metadata-info-value-alias");

        this.billsSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-bills"));
        this.billsPackageAlias = props.getProperties().getProperty("xml-bills-package-alias");
        this.billAlias = props.getProperties().getProperty("xml-bill-alias");
        this.billIdAlias = props.getProperties().getProperty("xml-bill-id-alias");
        this.billUriAlias = props.getProperties().getProperty("xml-bill-uri-alias");
        this.billNameAlias = props.getProperties().getProperty("xml-bill-name-alias");
        this.billOntologyAlias = props.getProperties().getProperty("xml-bill-ontology-alias");
        this.billCountryAlias = props.getProperties().getProperty("xml-bill-country-alias");
        
        this.actsSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-acts"));
        this.actsPackageAlias = props.getProperties().getProperty("xml-acts-package-alias");
        this.actAlias = props.getProperties().getProperty("xml-act-alias");
        this.actIdAlias = props.getProperties().getProperty("xml-act-id-alias");
        this.actNameAlias = props.getProperties().getProperty("xml-act-name-alias");
        
        this.motionsSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-motions"));
        this.motionsPackageAlias = props.getProperties().getProperty("xml-motions-package-alias");
        this.motionAlias = props.getProperties().getProperty("xml-motion-alias");
        this.motionIdAlias = props.getProperties().getProperty("xml-motion-id-alias");
        this.motionUriAlias = props.getProperties().getProperty("xml-motion-uri-alias");
        this.motionNameAlias = props.getProperties().getProperty("xml-motion-name-alias");
        this.motionTitleAlias = props.getProperties().getProperty("xml-motion-title-alias");
        this.motionByAlias = props.getProperties().getProperty("xml-motion-by-alias");
        this.motionTextAlias = props.getProperties().getProperty("xml-motion-text-alias");

        this.memberSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-members"));
        this.membersPackageAlias = props.getProperties().getProperty("xml-members-package-alias");
        this.memberAlias = props.getProperties().getProperty("xml-member-alias");
        this.memberIdAlias = props.getProperties().getProperty("xml-member-id-alias");
        this.memberUriAlias = props.getProperties().getProperty("xml-member-uri-alias");
        this.memberFirstNameAlias = props.getProperties().getProperty("xml-member-first-name-alias");
        this.memberLastNameAlias = props.getProperties().getProperty("xml-member-last-name-alias");
        this.memberRoleAlias = props.getProperties().getProperty("xml-member-role-alias");

        this.questionsSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-questions"));
        this.questionsPackageAlias = props.getProperties().getProperty("xml-questions-package-alias");
        this.questionAlias = props.getProperties().getProperty("xml-question-alias");
        this.questionIdAlias = props.getProperties().getProperty("xml-question-id-alias");
        this.questionTitleAlias = props.getProperties().getProperty("xml-question-title-alias");
        this.questionFromAlias = props.getProperties().getProperty("xml-question-from-alias");
        this.questionToAlias = props.getProperties().getProperty("xml-question-to-alias");
        this.questionTextAlias = props.getProperties().getProperty("xml-question-text-alias");

        this.documentsSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-documents"));
        this.documentsPackageAlias = props.getProperties().getProperty("xml-documents-package-alias");
        this.documentAlias = props.getProperties().getProperty("xml-document-alias");
        this.documentIdAlias = props.getProperties().getProperty("xml-document-id-alias");
        this.documentTitleAlias = props.getProperties().getProperty("xml-document-title-alias");
        this.documentDateAlias = props.getProperties().getProperty("xml-document-date-alias");
        this.documentSourceAlias = props.getProperties().getProperty("xml-document-source-alias");
        this.documentUriAlias = props.getProperties().getProperty("xml-document-uri-alias");
        this.documentSittingAlias = props.getProperties().getProperty("xml-document-sitting-alias");
        
        this.committeesSourceURI = getAbsoluteURL(props.getProperties().getProperty("xml-committees"));
        this.committeesPackageAlias = props.getProperties().getProperty("xml-committees-package-alias");
        this.committeetAlias = props.getProperties().getProperty("xml-committee-alias");
        this.committeeIdAlias = props.getProperties().getProperty("xml-committee-id-alias");
        this.committeeNameAlias = props.getProperties().getProperty("xml-committee-name-alias");
        this.committeeUriAlias = props.getProperties().getProperty("xml-committee-uri-alias");
        this.committeeCountryAlias = props.getProperties().getProperty("xml-committee-country-alias");

     
    }

    public List<Member> getMembers() {
        ClientResource resource = new ClientResource(getMemberSourceURI());
        try {
            Status status = resource.getStatus();
            if (Status.SUCCESS_OK.equals(status)) {
                XStream xStream = new XStream(new DomDriver());
                xStream.alias(this.getMembersPackageAlias(), List.class);
                xStream.alias(this.getMemberAlias(), Member.class);
                xStream.aliasField(this.getMemberIdAlias(), Member.class, "id");
                xStream.aliasField(this.getMemberUriAlias(), Member.class, "uri");
                xStream.aliasField(this.getMemberFirstNameAlias(), Member.class, "first");
                xStream.aliasField(this.getMemberLastNameAlias(), Member.class, "last");
                xStream.aliasField(this.getMemberRoleAlias(), Member.class, "role");
                String xml = resource.get().getText();
                if (xml != null) {
                    resource.release();
                    return (List) xStream.fromXML(xml);
                }
            } else {
                if (status != null) {
                    logger.error(status.getDescription());
                } else {
                    logger.error(getMemberSourceURI() + SERVER_UNREACHABLE);
                }
            }
        } catch (Exception ex) {
            logger.error(getMemberSourceURI() + SERVER_UNREACHABLE, ex);
        } finally {
            resource.release();
        }
        return null;
    }

    public List<Bill> getBills() {
        ClientResource resource = new ClientResource(getBillsSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getBillsPackageAlias(), List.class);
            xStream.alias(this.getBillAlias(), Bill.class);
            xStream.aliasField(this.getBillIdAlias(), Bill.class, "id");
            xStream.aliasField(this.getBillUriAlias(), Bill.class, "uri");
            xStream.aliasField(this.getBillNameAlias(), Bill.class, "name");
            xStream.aliasField(this.getBillOntologyAlias(), Bill.class, "ontology");
            xStream.aliasField(this.getBillCountryAlias(), Bill.class, "country");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getBillsSourceURI(), ex);
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public List<Act> getActs() {
        ClientResource resource = new ClientResource(getActsSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getActsPackageAlias(), List.class);
            xStream.alias(this.getActAlias(), Act.class);
            xStream.aliasField(this.getActIdAlias(), Act.class, "id");
            xStream.aliasField(this.getActNameAlias(), Act.class, "name");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getActsSourceURI(), ex);
        }
        return null;
    }
      
    public List<Motion> getMotions() {
        ClientResource resource = new ClientResource(getMotionsSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getMotionsPackageAlias(), List.class);
            xStream.alias(this.getMotionAlias(), Motion.class);
            xStream.aliasField(this.getMotionIdAlias(), Motion.class, "id");
            xStream.aliasField(this.getMotionUriAlias(), Motion.class, "uri");
            xStream.aliasField(this.getMotionNameAlias(), Motion.class, "name");
            xStream.aliasField(this.getMotionTitleAlias(), Motion.class, "title");
            xStream.aliasField(this.getMotionByAlias(), Motion.class, "by");
            xStream.aliasField(this.getMotionTextAlias(), Motion.class, "text");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getMotionsSourceURI(), ex);
        }
        return null;
    }

    public List<Question> getQuestions() {
        ClientResource resource = new ClientResource(getQuestionsSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getQuestionsPackageAlias(), List.class);
            xStream.alias(this.getQuestionAlias(), Question.class);
            xStream.aliasField(this.getQuestionIdAlias(), Question.class, "id");
            xStream.aliasField(this.getQuestionTitleAlias(), Question.class, "title");
            xStream.aliasField(this.getQuestionFromAlias(), Question.class, "from");
            xStream.aliasField(this.getQuestionToAlias(), Question.class, "to");
            xStream.aliasField(this.getQuestionTextAlias(), Question.class, "text");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getQuestionsSourceURI(), ex);
        }
        return null;
    }

    public List<MetadataInfo> getMetadataInfo() {
        ClientResource resource = new ClientResource(getMetadataInfoSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.setClassLoader(getClass().getClassLoader());
            
            xStream.alias(this.getMetadataInfoPackageAlias(), List.class);
            xStream.alias(this.getMetadataInfoAlias(), MetadataInfo.class);
            xStream.aliasField(this.getMetadataInfoIdAlias(), MetadataInfo.class, "id");
            xStream.aliasField(this.getMetadataInfoTypeAlias(), MetadataInfo.class, "type");
            xStream.aliasField(this.getMetadataInfoNameAlias(), MetadataInfo.class, "name");
            xStream.aliasField(this.getMetadataInfoValueAlias(), MetadataInfo.class, "value");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error("Error : " + getMetadataInfoSourceURI(), ex);
        }
        return null;
    }

    // !+ BUNGENI CONNECTOR ADDED FUNCTIONALITY (rm, jan 2012) - added functionality into
    // class, this method retrieves the committes data from Committees.xml
    public List<Document> getDocuments()
    {
        ClientResource resource = new ClientResource(getDocumentsSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getDocumentsPackageAlias(), List.class);
            xStream.alias(this.getDocumentAlias(), Document.class);
            xStream.aliasField(this.getDocumentIdAlias(), Document.class, "id");
            xStream.aliasField(this.getDocumentTitleAlias(), Document.class, "title");
            xStream.aliasField(this.getDocumentDateAlias(), Document.class, "date");
            xStream.aliasField(this.getDocumentSourceAlias(), Document.class, "source");
            xStream.aliasField(this.getDocumentUriAlias(), Document.class, "uri");
            xStream.aliasField(this.getDocumentSittingAlias(), Document.class, "sitting");
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getDocumentsSourceURI(), ex);
        }
        return null;
    }
    /**
     * 
     * @return
     */
    public List<Committee> getCommittees() {
        ClientResource resource = new ClientResource(getCommitteesSourceURI());
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(this.getCommitteesPackageAlias(), List.class);
            xStream.alias(this.getCommitteeAlias(), Committee.class);
            xStream.aliasField(this.getCommitteeIdAlias(), Committee.class, "id");
            xStream.aliasField(this.getCommitteeNameAlias(), Committee.class, "name");
            xStream.aliasField(this.getCommitteeUriAlias(), Committee.class, "uri");
            xStream.aliasField(this.getCommitteeCountryAlias(), Committee.class, "country");
            
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }
        } catch (Exception ex) {
            logger.error(getCommitteesSourceURI(), ex);
        }
        return null;
    }

    public void stopServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String getBillAlias() {
        return billAlias;
    }

    private String getBillCountryAlias() {
        return billCountryAlias;
    }

    private String getBillIdAlias() {
        return billIdAlias;
    }

    private String getBillNameAlias() {
        return billNameAlias;
    }

    private String getBillOntologyAlias() {
        return billOntologyAlias;
    }

    private String getBillUriAlias() {
        return billUriAlias;
    }

    private String getBillsPackageAlias() {
        return billsPackageAlias;
    }

    private String getCommitteesPackageAlias(){
        return committeesPackageAlias ;
    }

    private String getCommitteeNameAlias(){
        return committeeNameAlias ;
    }

    public String getCommitteeCountryAlias(){
        return committeeCountryAlias ;
    }

    private String getBillsSourceURI() {
        return billsSourceURI;
    }
    
    private String getActsSourceURI() {
        return actsSourceURI;
    }

     private String getActAlias() {
        return actAlias;
    }
     
    private String getActIdAlias() {
        return actIdAlias;
    }

    private String getActNameAlias() {
        return actNameAlias;
    }

    private String getActsPackageAlias() {
        return actsPackageAlias;
    }
    
    private String getDocumentAlias() {
        return documentAlias;
    }

    private String getDocumentDateAlias() {
        return documentDateAlias;
    }

    private String getDocumentIdAlias() {
        return documentIdAlias;
    }

    private String getDocumentSittingAlias() {
        return documentSittingAlias;
    }

    private String getDocumentSourceAlias() {
        return documentSourceAlias;
    }

    private String getDocumentTitleAlias() {
        return documentTitleAlias;
    }

    private String getDocumentUriAlias() {
        return documentUriAlias;
    }

    private String getDocumentsPackageAlias() {
        return documentsPackageAlias;
    }

    private String getCommitteeAlias(){
        return committeetAlias ;
    }

    private String getCommitteeIdAlias(){
        return committeeIdAlias ;
    }

    private String getCommitteeUriAlias(){
        return committeeUriAlias ;
    }

    private String getDocumentsSourceURI() {
        return documentsSourceURI;
    }

    private String getCommitteesSourceURI(){
        return committeesSourceURI ;
    }

    private String getMemberAlias() {
        return memberAlias;
    }

    private String getMemberFirstNameAlias() {
        return memberFirstNameAlias;
    }

    private String getMemberIdAlias() {
        return memberIdAlias;
    }

    private String getMemberLastNameAlias() {
        return memberLastNameAlias;
    }

    private String getMemberRoleAlias() {
        return memberRoleAlias;
    }

    private String getMemberSourceURI() {
        return memberSourceURI;
    }

    private String getMemberUriAlias() {
        return memberUriAlias;
    }

    private String getMembersPackageAlias() {
        return membersPackageAlias;
    }

    private String getMetadataInfoAlias() {
        return metadataInfoAlias;
    }

    private String getMetadataInfoIdAlias() {
        return metadataInfoIdAlias;
    }

    private String getMetadataInfoNameAlias() {
        return metadataInfoNameAlias;
    }

    private String getMetadataInfoPackageAlias() {
        return metadataInfoPackageAlias;
    }

    private String getMetadataInfoSourceURI() {
        return metadataInfoSourceURI;
    }

    private String getMetadataInfoTypeAlias() {
        return metadataInfoTypeAlias;
    }

    private String getMetadataInfoValueAlias() {
        return metadataInfoValueAlias;
    }

    private String getMotionAlias() {
        return motionAlias;
    }

    private String getMotionByAlias() {
        return motionByAlias;
    }

    private String getMotionIdAlias() {
        return motionIdAlias;
    }

    private String getMotionNameAlias() {
        return motionNameAlias;
    }

    private String getMotionTextAlias() {
        return motionTextAlias;
    }

    private String getMotionTitleAlias() {
        return motionTitleAlias;
    }

    private String getMotionUriAlias() {
        return motionUriAlias;
    }

    private String getMotionsPackageAlias() {
        return motionsPackageAlias;
    }

    private String getMotionsSourceURI() {
        return motionsSourceURI;
    }

    private String getQuestionAlias() {
        return questionAlias;
    }

    private String getQuestionFromAlias() {
        return questionFromAlias;
    }

    private String getQuestionIdAlias() {
        return questionIdAlias;
    }

    private String getQuestionTextAlias() {
        return questionTextAlias;
    }

    private String getQuestionTitleAlias() {
        return questionTitleAlias;
    }

    private String getQuestionToAlias() {
        return questionToAlias;
    }

    private String getQuestionsPackageAlias() {
        return questionsPackageAlias;
    }

    private String getQuestionsSourceURI() {
        return questionsSourceURI;
    }

    public void closeConnector() {
        logger.info("Connector Closed");
    }

    /**
     * Converts a relative URI to an absolute URL
     * @param relativeURI
     * @return
     */
    private String getAbsoluteURL(String relativeURI) {
        String sURL = "";
        try {
            String relativePath = relativeURI.replace("/", File.separator);
            File furi = new File(RELATIVE_ROOT_FOR_URI + relativePath);
            sURL = furi.toURI().toURL().toExternalForm();
        } catch (MalformedURLException ex) {
            logger.error("Error while generating URL to externalForm", ex);
        }
        return sURL;
    }
}