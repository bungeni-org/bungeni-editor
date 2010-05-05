package org.bungeni.xmlmerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.BasicConfigurator;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.xmlmerge.utils.XPathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ashok
 */
public class xmlMergeMain {

    private Document originalFile ;
    private List<Document> fileVersions = new ArrayList<Document>(0);
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(xmlMergeMain.class.getName());
      private BungeniClientDB mergeDB = null;


    public xmlMergeMain(Document origFile, List<Document> versions){
        this.originalFile = origFile;
        this.fileVersions = versions;
        log.debug(originalFile.getDocumentURI());
        for (Document document : versions) {
            log.debug(document.getDocumentURI());
        }
        this.mergeDB = getDBhandle();
    }

    private BungeniClientDB getDBhandle(){
        BungeniClientDB mdb = new BungeniClientDB(System.getProperty("user.dir") + File.separator + "merge" + File.separator, "merge.db");
       return mdb;
    }


    public void buildChangeInfo()  {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();
        for (Document document : fileVersions) {
            try {
                String xpathExpr = "//change-start";
                NodeList changeStarts = (NodeList) xPath.evaluate(xpathExpr, document, XPathConstants.NODESET);
                for (int i = 0; i < changeStarts.getLength(); i++) {
                    Node changeStart = changeStarts.item(i);
                    String changeId = changeStart.getAttributes().getNamedItem("change-id").getTextContent();
                    String xpathExprEnd = "//change-end[@change-id='" + changeId + "']";
                    Node changeEnd = (Node) xPath.evaluate(xpathExprEnd, document, XPathConstants.NODE);
                    String strXpathStart = XPathUtils.getXPath(changeStart);
                    String strXpathEnd = XPathUtils.getXPath(changeEnd);

                    log.debug("doc = " + document.getDocumentURI() + " , id = " + changeId + " , start = " + strXpathStart + " , end = " + strXpathEnd);
                }
            } catch (XPathExpressionException ex) {
                log.error("buildChangeInfo : " + ex.getMessage());
            }


        }
    }




    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();
            final String userDir = System.getProperty("user.dir");
            final String originalFile = userDir + "/testfiles/original.xml";
            List<String> versions = new ArrayList<String>() {

                {
                    add(userDir + "/testfiles/version-1.xml");
                    add(userDir + "/testfiles/version-2.xml");
                    add(userDir + "/testfiles/version-3.xml");
                }
            };
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document xmlOrigFile = builder.parse(new File(originalFile));
            List<Document> xmlVersions = new ArrayList<Document>(0);

            for (String version : versions) {
               xmlVersions.add(builder.parse(new File(version)));
            }

            xmlMergeMain mergeMain = new xmlMergeMain (xmlOrigFile, xmlVersions);
            mergeMain.buildChangeInfo();

        } catch (SAXException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            log.error(ex.getMessage());
        }




    }
}
