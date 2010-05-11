package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniOdfChangesMergeHelperTest {
    private static org.apache.log4j.Logger log        =
        Logger.getLogger(BungeniOdfChangesMergeHelperTest.class.getName());
    ArrayList<BungeniOdfDocumentHelper>    docHelpers = new ArrayList<BungeniOdfDocumentHelper>(0);

    // BungeniOdfDocumentHelper docHelper = null;

    public BungeniOdfChangesMergeHelperTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

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
    public void tearDown() {}

    /**
     * Test of mergeChanges method, of class BungeniOdfChangesMergeHelper.
     */
    @Test
    public void testMergeChanges() {
        try {
            BungeniOdfChangesMergeHelper instance = docHelpers.get(0).getChangesHelper().getChangesMergeHelper();
            boolean                      result   = instance.mergeChanges("Ashok Hariharan", "Flavio Zeni");
            File                         fout     = new File("testdocs/refactored-file.odt");

            if (fout.exists()) {
                fout.delete();
            }

            docHelpers.get(0).getOdfDocument().save("testdocs/refactored-file.odt");

            boolean bEqual = FileComparisonUtils.fileEquals("testdocs/refactored-file.odt",
                                 "testdocs/test-merge-changes-result.odt");

            assertEquals(true, bEqual);

            BungeniOdfChangesMergeHelper instance2 = docHelpers.get(1).getChangesHelper().getChangesMergeHelper();
            boolean                      result2   = instance2.mergeChanges("Ashok Hariharan", "Flavio Zeni");
            File                         fout2     = new File("testdocs/refactored-file-2.odt");

            if (fout2.exists()) {
                fout2.delete();
            }

            docHelpers.get(1).getOdfDocument().save("testdocs/refactored-file-2.odt");

            boolean bEqual2 = FileComparisonUtils.fileEquals("testdocs/refactored-file-2.odt",
                                  "testdocs/test-merge-full-result.odt");

            assertEquals(true, bEqual2);
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
