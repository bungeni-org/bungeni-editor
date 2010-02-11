/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdom.document.properties;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author undesa
 */
public class BungeniOdfPropertiesHelperTest {
    BungeniOdfDocumentHelper docHelper = null;
    public BungeniOdfPropertiesHelperTest() {
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
            docHelper = new BungeniOdfDocumentHelper(new File("testdocs/metadata-properties.odt"));
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfPropertiesHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getUserDefinedPropertyValue method, of class BungeniOdfPropertiesHelper.
     */
    @Test
    public void testGetUserDefinedPropertyValue() {
       
        String result = docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
        assertEquals("Clerk", result);
        
    }

}