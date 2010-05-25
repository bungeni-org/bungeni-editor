package org.bungeni.odfdom.document.changes;

//~--- non-JDK imports --------------------------------------------------------

import java.io.IOException;
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
import java.io.InputStream;

import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

/**
 *
 * @author undesa
 */
public class BungeniOdfTrackedChangesRevertTest03 {
    private static org.apache.log4j.Logger log       =
        Logger.getLogger(BungeniOdfTrackedChangesRevertTest03.class.getName());
BungeniOdfDocumentHelper               revertTestHelper = null;
String inputFile = "testdocs/tracked-changes-revert-test-03.odt";
String outputFile = "testdocs/test-revert-output-03.odt";
String testCaseName = "Paragraph deletion with trailing line breaks ";
long validChecksum = 1505828212L;
    public BungeniOdfTrackedChangesRevertTest03() {
        BasicConfigurator.configure();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Before
    public void setUp() {
        try {
            revertTestHelper = new BungeniOdfDocumentHelper(new File(inputFile));
            System.out.println(testCaseName);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @After
    public void tearDown() {}




    /**
     * Test of getChange method, of class BungeniOdfTrackedChangesHelper.
     */


    @Test
    public void testRevertAllChangesByCreatorWithException() throws IOException, Exception {
            BungeniOdfFileCopy.copyFile(new File(this.revertTestHelper.getDocumentPath()), new File(outputFile));
            BungeniOdfDocumentHelper testhelper = new BungeniOdfDocumentHelper(new File(outputFile));
            testhelper.getChangesHelper().revertAllChangesByCreatorWithException("Ashok Hariharan", new ArrayList<String>(){
                {
                 new String("ct-1423998720");
                }
            });
            testhelper.saveDocument();

            InputStream contentXml = testhelper.getOdfDocument().getPackage().getInputStream("content.xml");
            long ichecksum = ChecksumCRC32.doChecksum(contentXml);
            
            assertEquals(validChecksum, ichecksum);
    }
    
}
