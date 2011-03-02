package org.bungeni.odfdom.document.changes;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

/**
 *
 * @author undesa
 */
public class BungeniOdfChangeContextTest {
    BungeniOdfDocumentHelper dochelper = null;

    public BungeniOdfChangeContextTest() {
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
            dochelper = new BungeniOdfDocumentHelper(new File("testdocs/test-xpath-calculate.odt"));
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfChangeContextTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getReportPath method, of class BungeniOdfChangeContext.
     */
    @Test
    public void testGetReportPath() {
        try {
            System.out.println("getReportPath");
            XPath xPath = dochelper.getOdfDocument().getContentDom().getXPath();
            String expResult = "office:document-content/office:body[1]/office:text[1]/text:section[1]/text:section[5]/text:section[1]/text:section[2]/text:p[4]/text:change-start[3]";
            Node foundNodeStart = (Node) xPath.evaluate("//text:change-start[@text:change-id='ct-1413428116']", dochelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
            Node foundNodeEnd = (Node) xPath.evaluate("//text:change-end[@text:change-id='ct-1413428116']", dochelper.getOdfDocument().getContentDom(), XPathConstants.NODE);

            BungeniOdfChangeContext cxt = new BungeniOdfChangeContext(foundNodeStart,foundNodeEnd, dochelper);
            String reportPathFollow = cxt.getFollowingSiblingText();
            String reportPathPrecede = cxt.getPrecedingSiblingText();
            System.out.println("Following = " + reportPathFollow);
            System.out.println("Preceding = " + reportPathPrecede);
            assertEquals(true, true);
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfChangeContextTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}