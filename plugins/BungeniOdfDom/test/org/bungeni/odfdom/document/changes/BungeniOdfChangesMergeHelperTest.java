package org.bungeni.odfdom.document.changes;

import java.io.File;
import org.apache.log4j.Logger;
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
public class BungeniOdfChangesMergeHelperTest {
 BungeniOdfDocumentHelper docHelper = null;
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName());

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
            docHelper = new BungeniOdfDocumentHelper(new File("testdocs/test-merge-changes.odt"));
        } catch (Exception ex) {
            log.error(ex);
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
        BungeniOdfChangesMergeHelper instance = docHelper.getChangesHelper().getChangesMergeHelper();
        boolean result = instance.mergeChanges("Ashok Hariharan", "Flavio Zeni");
        assertEquals(true, result);
    }

}