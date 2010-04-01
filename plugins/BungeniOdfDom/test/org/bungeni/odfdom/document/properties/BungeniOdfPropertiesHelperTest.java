
package org.bungeni.odfdom.document.properties;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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


    /**
     * Test of getUserDefinedPropertyValues method, of class BungeniOdfPropertiesHelper.
     */
    @Test
    public void testGetUserDefinedPropertyValues() {
        HashMap<String,String> testmap = new HashMap<String,String> (){
            {
                put("BungeniDocOwner", "Abo Iringo");
                put("BungeniDocAuthor", "Clerk");
            }
        };
        Iterator testKeys = testmap.keySet().iterator();
        boolean bFail = false;
        HashMap<String,String> values = docHelper.getPropertiesHelper().getUserDefinedPropertyValues();
        if (values.size() == testmap.size()) {
            Iterator valKeys = values.keySet().iterator();

            while (testKeys.hasNext()) {
                String keytest = (String) testKeys.next();
                if (values.containsKey(keytest)) {
                    String testVal = testmap.get(keytest).trim();
                    String controlVal = values.get(keytest).trim();
                    if (!testVal.equals(controlVal)) {
                        bFail = true;
                        break;
                    }
                }
            }
        }
        assertEquals(false, bFail);
        }

    /**
     * Test of getUserDefinedPropertyValues method, of class BungeniOdfPropertiesHelper.
     */
    @Test
    public void testGetMetaEditingCycles() {
            String result = docHelper.getPropertiesHelper().getMetaEditingCycles();
            assertEquals("7", result);
        }

    @Test
    public void testGetMetaEditingDuration() {
            String result = docHelper.getPropertiesHelper().getMetaEditingDuration();
            System.out.println(result);
            assertEquals("0:9:3", result);
        }

    @Test
    public void testSetUserDefinedPropertyValue() {
        System.out.println("setUserDefinedPropertyValue");
        docHelper.getPropertiesHelper().setUserDefinedPropertyValue("alpha", "alphaValue");
        String sVal = docHelper.getPropertiesHelper().getUserDefinedPropertyValue("alpha");
        assertEquals(sVal, "alphaValue");

    }
}