package org.bungeni.xmlmerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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
import org.bungeni.db.QueryResults;
import org.bungeni.xmlmerge.queries.xmlMergeQueries;
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
        //create an xpath object
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xPath = xpathFactory.newXPath();
        //build the change node points in the db in the document order
        buildChangeNodePoints(xPath);
        processChangesInOrder(xPath);
    }

    class changePosition {
        String nodeAddressStart ;
        String nodeAddressEnd ;
        String changeId ;
        String docName ;

        public changePosition (String dName, String cId, String nAddressS, String nAddressE) {
            this.nodeAddressStart = nAddressS;
            this.nodeAddressEnd = nAddressE;
            this.changeId = cId;
            this.docName = dName;
        }

    }

    class changePositions {

        List<changePosition> arrPostions = new ArrayList<changePosition>(0);
        int changeOrder ;

        public changePositions (int order) {
            this.changeOrder = order;
        }

        public void add(changePosition pPos) {
            this.arrPostions.add(pPos);
        }




    }




    private void processChangesInOrder (XPath xPath) {
       boolean bContinue = true;
       int i=0;
        do {
            //iterate changes in order from the smallest to the largest
            QueryResults qr = mergeDB.ConnectAndQuery(xmlMergeQueries.GET_ALL_CHANGES_W_ORDER(i));
            //now we get the first change from each document
            //we are first interested in the lowest level change
            if (qr.hasResults()) {
                    //get all the change node positons for order i
                    changePositions changePos = buildChangeNodeArray(i, qr);
                    processShallowestNodes(changePos);
            } else {
                bContinue = false;
            }


            i++;
       } while (bContinue);
    }

    private void processShallowestNodes (changePositions cpObject) {
        //find the shallowest node
            //step 1
                //get the immediately preceding node
                //compare document positions of the parent node of the change node
                //an actual text:insert change can insert a whole section ... which may affect
                //the hierarchy... so ideally we have to collapse all insert hierachys ..store
                //them temporarily in a file and then recreate them
            for (changePosition cp : cpObject.arrPostions) {
                cp.nodeAddressStart.lastIndexOf("/");

            }
    }

    private changePositions buildChangeNodeArray(int norder, QueryResults qr) {
        changePositions cpos = new changePositions (norder);
           for (Vector<String> dataRow : qr.theResults()) {
                     String docname = qr.getField(dataRow, "DOC_NAME");
                    String changeId = qr.getField(dataRow, "CHANGE_ID");
                    String changeType = qr.getField(dataRow, "CHANGE_TYPE");
                    String changeBefore = qr.getField(dataRow, "CHANGE_POS_BEFORE");
                    String changeAfter = qr.getField(dataRow, "CHANGE_POS_AFTER");
                    changePosition cpObject =  new changePosition(docname, changeId, changeBefore, changeAfter);
                    cpos.add(cpObject);
                }
           return cpos;
    }

    private void buildChangeNodePoints(XPath xPath) {
        List<String> addQueries = new ArrayList<String>(0);
        NodeList nodeListPrev = null;
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
                    String addQuery = xmlMergeQueries.ADD_CHANGE(document.getDocumentURI(), changeId, "", strXpathStart,
                            strXpathEnd, "", Integer.toString(i));
                    addQueries.add(addQuery);
                    log.debug("buildChangeInfo : " + addQuery);
                }
               
            } catch (XPathExpressionException ex) {
                log.error("buildChangeInfo : " + ex.getMessage());
            }
        }
        this.mergeDB.Connect();
        this.mergeDB.Update(addQueries, true);
        this.mergeDB.EndConnect();

    }

    private void cleanupChangeInfo(){
        this.mergeDB.Connect();
        this.mergeDB.Update(xmlMergeQueries.DELETE_ALL_CHANGES());
        this.mergeDB.EndConnect();
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
            mergeMain.cleanupChangeInfo();
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
