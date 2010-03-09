package org.bungeni.odfdom.document.changes;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
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
 * @author Ashok Hariharan
 */
public class BungeniOdfChangesMergeHelperTest {
    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName());
    String testDoc = "testdocs/test-merge-changes.odt";
    ArrayList<BungeniOdfDocumentHelper> docHelpers = new ArrayList<BungeniOdfDocumentHelper>(0);
    //BungeniOdfDocumentHelper docHelper = null;

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
            docHelpers.add(new BungeniOdfDocumentHelper(new File("testdocs/test-merge-changes.odt")));
            docHelpers.add(new BungeniOdfDocumentHelper(new File("testdocs/test-merge-full.odt")));
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
        try {

            BungeniOdfChangesMergeHelper instance = docHelpers.get(0).getChangesHelper().getChangesMergeHelper();
            boolean result = instance.mergeChanges("Ashok Hariharan", "Flavio Zeni");
            File fout = new File("testdocs/refactored-file.odt");
            if (fout.exists()) {
                fout.delete();
            }
            docHelpers.get(0).getOdfDocument().save("testdocs/refactored-file.odt");

            BungeniOdfChangesMergeHelper instance2 = docHelpers.get(1).getChangesHelper().getChangesMergeHelper();
            boolean result2 = instance2.mergeChanges("Ashok Hariharan", "Flavio");
            File fout2 = new File("testdocs/refactored-file-2.odt");
            if (fout2.exists()) {
                fout2.delete();
            }
            docHelpers.get(1).getOdfDocument().save("testdocs/refactored-file-2.odt");


            assertEquals(true, result);
        } catch (Exception ex) {
           log.error(ex);
        }
    }

}