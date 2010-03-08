package org.bungeni.odfdom.document.changes;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
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
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;
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
            String xPathExpr = "//change-start[@id='2']/following::text()[not(preceding::change-end[id='2'])]";
            NodeList foundNodes = (NodeList) xpath.evaluate(xPathExpr, wdoc, XPathConstants.NODESET);
            //System.out.println(foundNode.getNodeName());
            /*
            BungeniOdfChangesMergeHelper instance = docHelper.getChangesHelper().getChangesMergeHelper();
            boolean result = instance.mergeChanges("Ashok Hariharan", "Flavio Zeni");
            assertEquals(true, result);
             *
             */
        } catch (XPathExpressionException ex) {
            java.util.logging.Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}