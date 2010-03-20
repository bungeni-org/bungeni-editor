package org.bungeni.odfdocument.report;

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
 * @author Ashok
 */
public class BungeniOdfDocumentReportTemplateTest {

    File foutput = new File("testdocs/test-report-output.odt");
    public BungeniOdfDocumentReportTemplateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        if (foutput.exists()) {
            foutput.delete();
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of documentFromTemplate method, of class BungeniOdfDocumentReportTemplate.
     */
    @Test
    public void testDocumentFromTemplate() {
        try {
            System.out.println("documentFromTemplate");
            BungeniOdfDocumentReportTemplate rpt = new BungeniOdfDocumentReportTemplate("testdocs/test-report-template.odt");
            BungeniOdfDocumentHelper result = rpt.documentFromTemplate(foutput);
            String mediaType = result.getOdfDocument().getPackage().getMediaType();
            assertEquals("application/vnd.oasis.opendocument.text", mediaType);
            // TODO review the generated test code and remove the default call to fail.
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfDocumentReportTemplateTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}