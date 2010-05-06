package org.bungeni.xmlmerge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.BasicConfigurator;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.xmlmerge.queries.xmlMergeQueries;
import org.bungeni.xmlmerge.utils.XPathUtils;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.text.OdfTextChange;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.odftoolkit.odfdom.doc.text.OdfTextSequenceDecls;
import org.odftoolkit.odfdom.doc.text.OdfTextTrackedChanges;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ashok
 */
public class xmlMergeMain {

    private OdfDocument originalFile ;
    private List<OdfDocument> fileVersions = new ArrayList<OdfDocument>(0);
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(xmlMergeMain.class.getName());
      private BungeniClientDB mergeDB = null;


    public xmlMergeMain(OdfDocument origFile, List<OdfDocument> versions){
        this.originalFile = origFile;
        this.fileVersions = versions;
        this.mergeDB = getDBhandle();
    }

    private BungeniClientDB getDBhandle(){
        BungeniClientDB mdb = new BungeniClientDB(System.getProperty("user.dir") + File.separator + "merge" + File.separator, "merge.db");
       return mdb;
    }


    public void buildChangeInfo()  {
        //create an xpath object
        XPath xPath = originalFile.getXPath();
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


    private void extractTrackChangesContainer(OdfDocument document) {
        BungeniOdfDocumentHelper odoc = new BungeniOdfDocumentHelper(document);
        OdfTextTrackedChanges tcElement = (OdfTextTrackedChanges) odoc.getChangesHelper().getTrackedChangeContainer();
        //write to disk
    }

    private void extractTextSequenceDecls (OdfDocument document) throws XPathExpressionException, Exception {
        XPath xPath = document.getXPath();
        OdfTextSequenceDecls seqDecls = (OdfTextSequenceDecls) xPath.evaluate("//text:sequence-decls", document.getContentDom(), XPathConstants.NODE);
        //write to disk
    }

    
    private void buildChangeNodePoints(XPath xPath) throws Exception {
        List<String> addQueries = new ArrayList<String>(0);
        NodeList nodeListPrev = null;
        for (OdfDocument document : fileVersions) {
            try {
                //extract track changes container
                extractTrackChangesContainer(document);
                //extract sequence decls
                extractTextSequenceDecls(document);
                //find all change start nodes
                String xpathExpr = "//node()[name()='text:change-start' or name()='text:change']";
                NodeList changeNodes = (NodeList) xPath.evaluate(xpathExpr, document.getContentDom(), XPathConstants.NODESET);
                //iterate through the change nodes
                for (int i = 0; i < changeNodes.getLength(); i++) {
                    String xpathStart = "", xpathEnd = "";
                    Node changeStart = changeNodes.item(i);
                    Node changeEnd = null;
                    String changeId = changeStart.getAttributes().getNamedItem("text:change-id").getTextContent();
                    xpathStart = XPathUtils.getXPath(changeStart);
                    //check if the node is a change start node
                    if (changeStart.getNodeName().equals(OdfTextChangeStart.ELEMENT_NAME.getQName())){
                        //get the change id
                        //get the node path to the change-start item
                        //get the xpath to the expresion end
                        String xpathExprEnd = "//text:change-end[@text:change-id='" + changeId + "']";
                        changeEnd = (Node) xPath.evaluate(xpathExprEnd, document.getContentDom(), XPathConstants.NODE);
                        xpathEnd = BungeniOdfNodeHelper.getXPath(changeEnd);
                    }

                    String addQuery = xmlMergeQueries.ADD_CHANGE(document.getBaseURI(), changeId, "", xpathStart,
                            xpathEnd, "", Integer.toString(i));
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
            final String originalFile = userDir + "/testfiles/original.odt";
            List<String> versions = new ArrayList<String>() {

                {
                    add(userDir + "/testfiles/version-1.odt");
                    add(userDir + "/testfiles/version-2.odt");
                }
            };

            OdfDocument xmlOrigFile = OdfDocument.loadDocument(new File(originalFile));
            List<OdfDocument> xmlVersions = new ArrayList<OdfDocument>(0);

            for (String version : versions) {
               xmlVersions.add(OdfDocument.loadDocument(new File(version)));
            }

            xmlMergeMain mergeMain = new xmlMergeMain (xmlOrigFile, xmlVersions);
            mergeMain.cleanupChangeInfo();
            mergeMain.buildChangeInfo();

        } catch (java.lang.Exception ex) {
            log.error(ex.getMessage());
        } 




    }
}
