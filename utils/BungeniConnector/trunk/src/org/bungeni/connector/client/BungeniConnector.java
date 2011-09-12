/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Document;
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
public class BungeniConnector implements IBungeniConnector {

    private String metadataInfoSource = "http://localhost:8899/current/metadata";
    private String membersSource = "http://localhost:8899/current/members";
    private String motionsSource = "http://localhost:8899/current/motions";
    private String questionsSource = "http://localhost:8899/current/questions";
    private String billsSource = "http://localhost:8899/current/bills";
    private String documentsSource = "http://localhost:8899/current/documents";
    private String packageAlias = "package";
    private String SERVER_UNREACHABLE = " did not respond";
    private static Logger logger = Logger.getLogger(BungeniConnector.class.getName());

    private List getList(String source, String packageAlias, String alias, Class aliasClass) {
        ClientResource resource = new ClientResource(source);
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.alias(packageAlias, List.class);
            xStream.alias(alias, aliasClass);
            String xml = resource.get().getText();
            if (xml != null) {
                resource.release();
                return (List) xStream.fromXML(xml);
            }

        } catch (Exception ex) {
            logger.error(source + SERVER_UNREACHABLE, ex);
        } finally {
            resource.release();
        }
        return null;
    }

    public List<Member> getMembers() {
        return getList(getMembersSource(), Member.PACKAGE_ALIAS, Member.CLASS_ALIAS, Member.class);
    }

    public List<Bill> getBills() {
        return getList(getBillsSource(), Bill.PACKAGE_ALIAS, Bill.CLASS_ALIAS, Bill.class);
    }

    public List<Motion> getMotions() {
        return getList(getMotionsSource(), Motion.PACKAGE_ALIAS, Motion.CLASS_ALIAS, Motion.class);
    }

    public List<Question> getQuestions() {
        return getList(getQuestionsSource(), Question.PACKAGE_ALIAS, Question.CLASS_ALIAS, Question.class);
    }

    public List<MetadataInfo> getMetadataInfo() {
        return getList(getMetadataInfoSource(), MetadataInfo.PACKAGE_ALIAS, MetadataInfo.CLASS_ALIAS, MetadataInfo.class);
    }

    public List<Document> getDocuments() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public String getBillsSource() {
        return billsSource;
    }

    public void setBillsSource(String billsSource) {
        this.billsSource = billsSource;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        BungeniConnector.logger = logger;
    }

    public String getMembersSource() {
        return membersSource;
    }

    public void setMembersSource(String membersSource) {
        this.membersSource = membersSource;
    }

    public String getMetadataInfoSource() {
        return metadataInfoSource;
    }

    public void setMetadataInfoSource(String metadataInfoSource) {
        this.metadataInfoSource = metadataInfoSource;
    }

    public String getMotionsSource() {
        return motionsSource;
    }

    public void setMotionsSource(String motionsSource) {
        this.motionsSource = motionsSource;
    }

    public String getPackageAlias() {
        return packageAlias;
    }

    public void setPackageAlias(String packageAlias) {
        this.packageAlias = packageAlias;
    }

    public String getQuestionsSource() {
        return questionsSource;
    }

    public void setQuestionsSource(String questionsSource) {
        this.questionsSource = questionsSource;
    }

    public String getDocumentsSource() {
        return documentsSource;
    }

    public void setDocumentsSource(String documentsSource) {
        this.documentsSource = documentsSource;
    }

    public void closeConnector() {
        logger.info("Client Connection Closed");
    }
}
