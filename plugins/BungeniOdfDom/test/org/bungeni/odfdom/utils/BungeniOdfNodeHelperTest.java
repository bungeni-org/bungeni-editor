package org.bungeni.odfdom.utils;

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
import static org.junit.Assert.*;
import org.w3c.dom.Node;

/**
 *
 * @author undesa
 */
public class BungeniOdfNodeHelperTest {
    BungeniOdfDocumentHelper dochelper = null;

    public BungeniOdfNodeHelperTest() {
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
            Logger.getLogger(BungeniOdfNodeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getNodeXPath method, of class BungeniOdfNodeHelper.
     */
    @Test
    public void testGetNodeXPath() {
        try {
            XPath xPath = dochelper.getOdfDocument().getXPath();
            String expResult = "";
            //dochelper.getOdfDocument().
            //text:change-start text:change-id="ct-1413428116"
            //xPath.setNamespaceContext(OdfNamespace.newNamespace(OdfNamespaceNames.OFFICE));
            Node foundNode = (Node) xPath.evaluate("//text:change-start[@text:change-id='ct-1413428116']", dochelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
            String result = BungeniOdfNodeHelper.getXPath(foundNode);
            assertEquals(expResult, result);
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfNodeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 

}