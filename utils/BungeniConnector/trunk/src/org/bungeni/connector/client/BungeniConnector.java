package org.bungeni.connector.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Document;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;
import org.restlet.resource.ClientResource;

/**
 * This is the client for accessing the REST DataSourceServer
 * The way to use the BungeniConnector client is :
 *  BungeniConnector bc = new BungeniConnector();
 *  bc.init(new ConnectorProperties(StringPathToConnectorPropertiesFile);
 * @author Dave
 */
public class BungeniConnector implements IBungeniConnector {

    //!+CODE_REVIEW(ah, sep-2011) Why hardcode source URLs when we have all the required info
    //in the connector-properties file ? The client and server interface on the same host.
    private String metadataInfoSource = "/metadata";
    private String membersSource = "/members";
    private String motionsSource = "/motions";
    private String questionsSource = "/questions";
    private String billsSource = "/bills";
    private String documentsSource = "/documents";
    private String packageAlias = "package";

    private String SERVER_HOST = "localhost";
    private String SERVER_PORT = "80";
    private String SERVER_PROTOCOL = "http://";
    private String SERVER_VIRT_DIR = "current";

    private String SERVER_UNREACHABLE = " did not respond";
    private static Logger logger = Logger.getLogger(BungeniConnector.class.getName());

    /**
     * The way to use the BungeniConnector client is :
     *  BungeniConnector bc = new BungeniConnector();
     *  bc.init(new ConnectorProperties(StringPathToConnectorPropertiesFile);
     *
     * @param connProps
     */
    public void init(ConnectorProperties connProps) {
         Properties props = connProps.getProperties();
         this.SERVER_HOST = props.getProperty("server-host");
         this.SERVER_PORT = props.getProperty("server-port");
         this.SERVER_VIRT_DIR = "current";
         this.SERVER_PROTOCOL = "http://";

    }

    private String getVirtDirURL(){
        return this.SERVER_PROTOCOL + this.SERVER_HOST + ":" + this.SERVER_PORT
                + "/" + this.SERVER_VIRT_DIR ;
    }

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

    private String getBillsSource() {
        return getVirtDirURL() + billsSource;
    }


    private String getMembersSource() {
        return getVirtDirURL() + membersSource;
    }

    private String getMetadataInfoSource() {
        return getVirtDirURL() + metadataInfoSource;
    }


    private String getMotionsSource() {
        return getVirtDirURL() + motionsSource;
    }

    private String getPackageAlias() {
        return packageAlias;
    }

    private String getQuestionsSource() {
        return getVirtDirURL() + questionsSource;
    }

    private String getDocumentsSource() {
        return getVirtDirURL() + documentsSource;
    }

    public void closeConnector() {
        logger.info("Client Connection Closed");
    }

}
