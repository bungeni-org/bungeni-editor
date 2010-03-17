
package org.bungeni.odfdom.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ashok Hariharan
 */
public class XPathComponentTest {

    XPathComponent xpathComp = null;

    public XPathComponentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        xpathComp = new XPathComponent("text:change-start[32]");
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of getPrefix method, of class XPathComponent.
     */
    @Test
    public void testGetPrefix() {
        String expResult = "text";
        String result = xpathComp.getPrefix();
        assertEquals(expResult, result);
    }

    /**
     * Test of getElement method, of class XPathComponent.
     */
    @Test
    public void testGetElement() {
        String expResult = "change-start";
        String result = xpathComp.getElement();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIndex method, of class XPathComponent.
     */
    @Test
    public void testGetIndex() {
        String expResult = "32";
        String result = xpathComp.getIndex();
        assertEquals(expResult, result);
    }

}