package org.bungeni.connector.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.connector.impl.XMLBungeniConnector;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.restlet.resource.ClientResource;
import org.xml.sax.InputSource;

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
    private String actsSource = "/acts";
    private String sourceTypesSource = "/sourceTypes";
    private String documentsSource = "/documents";
    private String committeeSource = "/committee";
    private String packageAlias = "package";

    private String SERVER_HOST = "localhost";
    private String SERVER_PORT = "80";
    private String SERVER_PROTOCOL = "http://";
    private String SERVER_VIRT_DIR = "current";

    
    public static String RELATIVE_PATH = null;

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
    
    public List<Act> getActs() {
        return getList(getActsSource(), Act.PACKAGE_ALIAS, Act.CLASS_ALIAS, Act.class);
    }
   
    public List<SourceType> getSourceTypes() {
        return getList(getSourceTypesSource(), SourceType.PACKAGE_ALIAS, SourceType.CLASS_ALIAS, SourceType.class);
    }
      
    public List<Motion> getMotions() {
        return getList(getMotionsSource(), Motion.PACKAGE_ALIAS, Motion.CLASS_ALIAS, Motion.class);
    }

    public List<Question> getQuestions() {
        return getList(getQuestionsSource(), Question.PACKAGE_ALIAS, Question.CLASS_ALIAS, Question.class);
    }

    public List<MetadataInfo> getMetadataInfo() {
        System.out.println("Metadata source : "  + getMetadataInfoSource());
        return getList(getMetadataInfoSource(), MetadataInfo.PACKAGE_ALIAS, MetadataInfo.CLASS_ALIAS, MetadataInfo.class);
    }

    // !+ ADDED FUNCTIONALITY TO BUNGENI CONNECTOR (rm, jan 2012)
    // this method is useful in serializing the data from the documents table
    // and populating the Import documents JTable displayed to the user
    public List<Document> getDocuments() {
        System.out.println("Document source : "  + getDocumentsSource());
        return getList(getDocumentsSource(), Document.PACKAGE_ALIAS, Document.CLASS_ALIAS, Document.class);
    }
//
//    // !+ ADDED FUNCTIONALITY TO BUNGENI CONNECTOR (rm, jan 2012)
//    // this method is useful towards serializing the data from the committees table
    public List<Committee> getCommittees(){
        System.out.println("Committee source : " + getCommitteeSource());
        return getList(getCommitteeSource(), Committee.PACKAGE_ALIAS, Committee.CLASS_ALIAS, Committee.class);
    }
    
    private String getBillsSource() {
        return getVirtDirURL() + billsSource;
    }
    
    private String getActsSource() {
        return getVirtDirURL() + actsSource;
    }
     
    private String getSourceTypesSource() {
        return getVirtDirURL() + sourceTypesSource;
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

    private String getCommitteeSource() {
        return getVirtDirURL() +  committeeSource ;
    }

    public void closeConnector() {
        logger.info("Client Connection Closed");
    }

}