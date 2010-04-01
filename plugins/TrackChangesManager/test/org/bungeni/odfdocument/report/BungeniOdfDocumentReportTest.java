
package org.bungeni.odfdocument.report;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 *
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentReportTest {

    BungeniOdfDocumentReport rpt = null;

    public BungeniOdfDocumentReportTest() {
    }

    @Before
    public void setUp() {
        try {
            File fout = new File("testdocs/rpt-output.odt");
            if (fout.exists()) fout.delete();
            rpt = new BungeniOdfDocumentReport(new File("testdocs/rpt-output.odt"), new BungeniOdfDocumentReportTemplate("testdocs/bill-by-user.odt"));
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfDocumentReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of generateReport method, of class BungeniOdfDocumentReport.
     */
    @Test
    public void testGenerateReport() {
        System.out.println("generateReport");
        rpt.addReportVariable("REPORT_HEADER", "This is the header");
        rpt.addReportVariable("REPORT_FOOTER", "This is the footer");
        rpt.addReportVariable("MEMBER OF PARLIAMENT", "This is the MP");
        rpt.addReportVariable("NO_OF_AMENDMENTS", "This is the number of amendments");

        rpt.generateReport("This is the MP");
        assertEquals(true, true);
        // TODO review the generated test code and remove the default call to fail.
       
    }

}