/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;

/**
 *
 * @author Dave
 */
public class XMLBungeniConnector implements IBungeniConnector {

    static final String PACKAGE_ALIAS = "package";
    static final String SERVER_UNREACHABLE = " did not respond";
    private static Logger logger = Logger.getLogger(XMLBungeniConnector.class.getName());
    private String metadataInfoPackageAlias = "metadata";
    private String metadataInfoAlias = "metadatainfo";
    private String metadataInfoIdAlias = "id";
    private String metadataInfoTypeAlias = "type";
    private String metadataInfoNameAlias = "name";
    private String metadataInfoValueAlias = "value";
    private String metadataInfoSourceURI = "file://D:/PROJECTS/JSE/UNDESA/BungeniConnector/datasource/xml/metadata.xml";
    private String membersPackageAlias = "members";
    private String memberAlias = "member";
    private String memberIdAlias = "id";
    private String memberUriAlias = "uri";
    private String memberFirstNameAlias = "first";
    private String memberLastNameAlias = "last";
    private String memberRoleAlias = "role";
    private String memberSourceURI = "file://D:/PROJECTS/JSE/UNDESA/BungeniConnector/datasource/xml/members.xml";
    private String billsPackageAlias = "bills";
    private String billAlias = "bill";
    private String billIdAlias = "id";
    private String billUriAlias = "uri";
    private String billNameAlias = "name";
    private String billOntologyAlias = "ontology";
    private String billCountryAlias = "country";
    private String billsSourceURI = "file://D:/PROJECTS/JSE/UNDESA/BungeniConnector/datasource/xml/bills.xml";
    private String motionsPackageAlias = "motions";
    private String motionAlias = "motion";
    private String motionIdAlias = "id";
    private String motionUriAlias = "uri";
    private String motionNameAlias = "name";
    private String motionTitleAlias = "title";
    private String motionByAlias = "by";
    private String motionTextAlias = "text";
    private String motionsSourceURI = "file://D:/PROJECTS/JSE/UNDESA/BungeniConnector/datasource/xml/motions.xml";
    private String questionsPackageAlias = "questions";
    private String questionAlias = "question";
    private String questionIdAlias = "id";
    private String questionTitleAlias = "title";
    private String questionFromAlias = "from";
    private String questionToAlias = "to";
    private String questionTextAlias = "text";
    private String questionsSourceURI = "file://D:/PROJECTS/JSE/UNDESA/BungeniConnector/datasource/xml/questions.xml";

    public XMLBungeniConnector() {
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
        }finally{
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
            logger.error(getMetadataInfoSourceURI(), ex);
        }
        return null;
    }

    public void stopServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getMemberSourceURI() {
        return memberSourceURI;
    }

    public void setMemberSourceURI(String memberSourceURI) {
        this.memberSourceURI = memberSourceURI;
    }

    public String getMemberAlias() {
        return memberAlias;
    }

    public void setMemberAlias(String memberAlias) {
        this.memberAlias = memberAlias;
    }

    public String getMemberFirstNameAlias() {
        return memberFirstNameAlias;
    }

    public void setMemberFirstNameAlias(String memberFirstNameAlias) {
        this.memberFirstNameAlias = memberFirstNameAlias;
    }

    public String getMemberIdAlias() {
        return memberIdAlias;
    }

    public void setMemberIdAlias(String memberIdAlias) {
        this.memberIdAlias = memberIdAlias;
    }

    public String getMemberLastNameAlias() {
        return memberLastNameAlias;
    }

    public void setMemberLastNameAlias(String memberLastNameAlias) {
        this.memberLastNameAlias = memberLastNameAlias;
    }

    public String getMemberRoleAlias() {
        return memberRoleAlias;
    }

    public void setMemberRoleAlias(String memberRoleAlias) {
        this.memberRoleAlias = memberRoleAlias;
    }

    public String getMemberUriAlias() {
        return memberUriAlias;
    }

    public void setMemberUriAlias(String memberUriAlias) {
        this.memberUriAlias = memberUriAlias;
    }

    public String getMembersPackageAlias() {
        return membersPackageAlias;
    }

    public void setMembersPackageAlias(String membersPackageAlias) {
        this.membersPackageAlias = membersPackageAlias;
    }

    public String getBillAlias() {
        return billAlias;
    }

    public void setBillAlias(String billAlias) {
        this.billAlias = billAlias;
    }

    public String getBillCountryAlias() {
        return billCountryAlias;
    }

    public void setBillCountryAlias(String billCountryAlias) {
        this.billCountryAlias = billCountryAlias;
    }

    public String getBillIdAlias() {
        return billIdAlias;
    }

    public void setBillIdAlias(String billIdAlias) {
        this.billIdAlias = billIdAlias;
    }

    public String getBillNameAlias() {
        return billNameAlias;
    }

    public void setBillNameAlias(String billNameAlias) {
        this.billNameAlias = billNameAlias;
    }

    public String getBillOntologyAlias() {
        return billOntologyAlias;
    }

    public void setBillOntologyAlias(String billOntologyAlias) {
        this.billOntologyAlias = billOntologyAlias;
    }

    public String getBillUriAlias() {
        return billUriAlias;
    }

    public void setBillUriAlias(String billUriAlias) {
        this.billUriAlias = billUriAlias;
    }

    public String getBillsPackageAlias() {
        return billsPackageAlias;
    }

    public void setBillsPackageAlias(String billsPackageAlias) {
        this.billsPackageAlias = billsPackageAlias;
    }

    public String getBillsSourceURI() {
        return billsSourceURI;
    }

    public void setBillsSourceURI(String billsSourceURI) {
        this.billsSourceURI = billsSourceURI;
    }

    public String getMetadataInfoAlias() {
        return metadataInfoAlias;
    }

    public void setMetadataInfoAlias(String metadataInfoAlias) {
        this.metadataInfoAlias = metadataInfoAlias;
    }

    public String getMetadataInfoIdAlias() {
        return metadataInfoIdAlias;
    }

    public void setMetadataInfoIdAlias(String metadataInfoIdAlias) {
        this.metadataInfoIdAlias = metadataInfoIdAlias;
    }

    public String getMetadataInfoNameAlias() {
        return metadataInfoNameAlias;
    }

    public void setMetadataInfoNameAlias(String metadataInfoNameAlias) {
        this.metadataInfoNameAlias = metadataInfoNameAlias;
    }

    public String getMetadataInfoPackageAlias() {
        return metadataInfoPackageAlias;
    }

    public void setMetadataInfoPackageAlias(String metadataInfoPackageAlias) {
        this.metadataInfoPackageAlias = metadataInfoPackageAlias;
    }

    public String getMetadataInfoSourceURI() {
        return metadataInfoSourceURI;
    }

    public void setMetadataInfoSourceURI(String metadataInfoSourceURI) {
        this.metadataInfoSourceURI = metadataInfoSourceURI;
    }

    public String getMetadataInfoTypeAlias() {
        return metadataInfoTypeAlias;
    }

    public void setMetadataInfoTypeAlias(String metadataInfoTypeAlias) {
        this.metadataInfoTypeAlias = metadataInfoTypeAlias;
    }

    public String getMetadataInfoValueAlias() {
        return metadataInfoValueAlias;
    }

    public void setMetadataInfoValueAlias(String metadataInfoValueAlias) {
        this.metadataInfoValueAlias = metadataInfoValueAlias;
    }

    public String getMotionAlias() {
        return motionAlias;
    }

    public void setMotionAlias(String motionAlias) {
        this.motionAlias = motionAlias;
    }

    public String getMotionByAlias() {
        return motionByAlias;
    }

    public void setMotionByAlias(String motionByAlias) {
        this.motionByAlias = motionByAlias;
    }

    public String getMotionIdAlias() {
        return motionIdAlias;
    }

    public void setMotionIdAlias(String motionIdAlias) {
        this.motionIdAlias = motionIdAlias;
    }

    public String getMotionNameAlias() {
        return motionNameAlias;
    }

    public void setMotionNameAlias(String motionNameAlias) {
        this.motionNameAlias = motionNameAlias;
    }

    public String getMotionTextAlias() {
        return motionTextAlias;
    }

    public void setMotionTextAlias(String motionTextAlias) {
        this.motionTextAlias = motionTextAlias;
    }

    public String getMotionTitleAlias() {
        return motionTitleAlias;
    }

    public void setMotionTitleAlias(String motionTitleAlias) {
        this.motionTitleAlias = motionTitleAlias;
    }

    public String getMotionUriAlias() {
        return motionUriAlias;
    }

    public void setMotionUriAlias(String motionUriAlias) {
        this.motionUriAlias = motionUriAlias;
    }

    public String getMotionsPackageAlias() {
        return motionsPackageAlias;
    }

    public void setMotionsPackageAlias(String motionsPackageAlias) {
        this.motionsPackageAlias = motionsPackageAlias;
    }

    public String getMotionsSourceURI() {
        return motionsSourceURI;
    }

    public void setMotionsSourceURI(String motionsSourceURI) {
        this.motionsSourceURI = motionsSourceURI;
    }

    public String getQuestionAlias() {
        return questionAlias;
    }

    public void setQuestionAlias(String questionAlias) {
        this.questionAlias = questionAlias;
    }

    public String getQuestionFromAlias() {
        return questionFromAlias;
    }

    public void setQuestionFromAlias(String questionFromAlias) {
        this.questionFromAlias = questionFromAlias;
    }

    public String getQuestionIdAlias() {
        return questionIdAlias;
    }

    public void setQuestionIdAlias(String questionIdAlias) {
        this.questionIdAlias = questionIdAlias;
    }

    public String getQuestionTextAlias() {
        return questionTextAlias;
    }

    public void setQuestionTextAlias(String questionTextAlias) {
        this.questionTextAlias = questionTextAlias;
    }

    public String getQuestionTitleAlias() {
        return questionTitleAlias;
    }

    public void setQuestionTitleAlias(String questionTitleAlias) {
        this.questionTitleAlias = questionTitleAlias;
    }

    public String getQuestionToAlias() {
        return questionToAlias;
    }

    public void setQuestionToAlias(String questionToAlias) {
        this.questionToAlias = questionToAlias;
    }

    public String getQuestionsPackageAlias() {
        return questionsPackageAlias;
    }

    public void setQuestionsPackageAlias(String questionsPackageAlias) {
        this.questionsPackageAlias = questionsPackageAlias;
    }

    public String getQuestionsSourceURI() {
        return questionsSourceURI;
    }

    public void setQuestionsSourceURI(String questionsSourceURI) {
        this.questionsSourceURI = questionsSourceURI;
    }
}
