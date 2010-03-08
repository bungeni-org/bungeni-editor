package org.bungeni.odfdom.document.changes;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author undesa
 */
public class BungeniOdfChangesMergeHelperTest {
 BungeniOdfDocumentHelper docHelper = null;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName());
    String testDoc = "testdocs/test-test-move.xml";
    Document wdoc = null;

    public BungeniOdfChangesMergeHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            DocumentBuilder dBuilder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
            wdoc = dBuilder.parse(testDoc);
            /*
            try {
            docHelper = new BungeniOdfDocumentHelper(new File("testdocs/test-merge-changes.odt"));
            } catch (Exception ex) {
            log.error(ex);
            }*/
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of mergeChanges method, of class BungeniOdfChangesMergeHelper.
     */
    @Test
    public void testMergeChanges() {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String xPathExpr = "//change-start[@id='2']";
            Node startNode = (Node)  xpath.evaluate(xPathExpr, wdoc, XPathConstants.NODE);
            Node endNode = (Node) xpath.evaluate("//change-end[@id='2']", wdoc, XPathConstants.NODE);
            Node nextSibling = startNode.getNextSibling();
            ArrayList<Node> clonedNodes = new ArrayList<Node>(0);
            while (!nextSibling.isSameNode(endNode)) {
                Node clonedNode = nextSibling.cloneNode(true);
                clonedNodes.add(clonedNode);
                System.out.println(nextSibling.getNodeName());
                nextSibling = nextSibling.getNextSibling();
            }

            nextSibling = startNode.getNextSibling();
            while(!nextSibling.isSameNode(endNode)) {
                nextSibling.getParentNode().removeChild(nextSibling);
                nextSibling = startNode.getNextSibling();
            }
            startNode.getParentNode().removeChild(startNode);
            endNode.getParentNode().removeChild(endNode);

            /*
            String xPath2 = "//change-end[@id='1']";
            Node endNode = (Node) xpath.evaluate(xPath2, wdoc, XPathConstants.NODE);
          */
            Node endNodeOne = (Node) xpath.evaluate("//change-end[@id='1']", wdoc, XPathConstants.NODE);
            for (Node node : clonedNodes) {
                endNodeOne.getParentNode().insertBefore(node, endNodeOne);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");

//initialize StreamResult with File object to save to file
StreamResult result = new StreamResult(new StringWriter());
DOMSource source = new DOMSource(wdoc);
transformer.transform(source, result);

String xmlString = result.getWriter().toString();
System.out.println(xmlString);
            //System.out.println(foundNode.getNodeName());
            /*
            BungeniOdfChangesMergeHelper instance = docHelper.getChangesHelper().getChangesMergeHelper();
            boolean result = instance.mergeChanges("Ashok Hariharan", "Flavio Zeni");
            assertEquals(true, result);
             *
             */
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}