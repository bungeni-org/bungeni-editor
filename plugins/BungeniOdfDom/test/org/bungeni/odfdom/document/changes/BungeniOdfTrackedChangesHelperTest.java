
package org.bungeni.odfdom.document.changes;

import java.io.File;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import static org.junit.Assert.*;
import org.w3c.dom.Element;

/**
 *
 * @author undesa
 */
public class BungeniOdfTrackedChangesHelperTest {
  BungeniOdfDocumentHelper docHelper = null;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfTrackedChangesHelperTest.class.getName());

    public BungeniOdfTrackedChangesHelperTest() {
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
            docHelper = new BungeniOdfDocumentHelper(new File("testdocs/tracked-changes.odt"));
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTrackedChangeContainer method, of class BungeniOdfTrackedChangesHelper.
     */
   @Test
    public void testGetTrackedChangeContainer() {
        System.out.println("getTrackedChangeContainer");
        Element result = docHelper.getChangesHelper().getTrackedChangeContainer();
        assertEquals("text:tracked-changes", result.getNodeName());
    }
    /**
     * Test of getChangedRegions method, of class BungeniOdfTrackedChangesHelper.
     */
    @Test
    public void testGetChangedRegions() {
        System.out.println("getChangedRegions");
        Element changeContainer = docHelper.getChangesHelper().getTrackedChangeContainer();
       
        ArrayList<OdfTextChangedRegion> result = docHelper.getChangesHelper().getChangedRegions(changeContainer);
        assertEquals(8, result.size());
    }

    
    /**
     * Test of getChange method, of class BungeniOdfTrackedChangesHelper.
     */
    @Test
    public void testGetChange() {
        System.out.println("getChange");
        Element changeContainer = docHelper.getChangesHelper().getTrackedChangeContainer();

        ArrayList<OdfTextChangedRegion> result = docHelper.getChangesHelper().getChangedRegions(changeContainer);

        Element changeElem = docHelper.getChangesHelper().getChange(result.get(0));
        assertEquals("deletion", changeElem.getLocalName());
    }

    
    /**
     * Test of getStructuredChangeType method, of class BungeniOdfTrackedChangesHelper.
     */
    @Test
    public void testGetStructuredChangeType() {
        System.out.println("getStructuredChangeType");
        Element changeContainer = docHelper.getChangesHelper().getTrackedChangeContainer();
        ArrayList<OdfTextChangedRegion> result = docHelper.getChangesHelper().getChangedRegions(changeContainer);
        BungeniOdfTrackedChangesHelper.StructuredChangeType stype = docHelper.getChangesHelper().getStructuredChangeType(result.get(0));
        assertEquals("deletion", stype.changetype);
    }


}