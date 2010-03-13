package org.bungeni.trackchanges.utils;

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
 * @author Ashok Hariharan
 */
public class ReviewDocumentsTest {
    File ftest = null;
    BungeniOdfDocumentHelper aDoc = null;
    ReviewDocuments rd = null;
    public ReviewDocumentsTest() {
        try {
            ftest = new File("testdocs/u2841_ke_bill_2010-2-25_eng.odt");
            System.out.println(ftest.getAbsolutePath());
            aDoc = new BungeniOdfDocumentHelper(ftest);
        } catch (Exception ex) {
           System.out.println(ex.getMessage());
        }
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
            rd = new ReviewDocuments(this.aDoc, "ClerkReview");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        File f1 = new File("testdocs/clerk_u2841_ke_bill_2010-2-25_eng.odt");
        if (f1.exists()) f1.delete();
        File f2 = new File("testdocs/cons_u2841_ke_bill_2010-2-25_eng.odt");
        if (f2.exists()) f2.delete();
    }

    /**
     * Test of getStagePrefix method, of class ReviewDocuments.
     */
    @Test
    public void testGetStagePrefix() {
       String sPref = rd.getStagePrefix();
       assertEquals("clerk", sPref);

    }

    /**
     * Test of getDocumentNameWithoutPrefix method, of class ReviewDocuments.
     */
    @Test
    public void testGetDocumentNameWithoutPrefix() {
        System.out.println("getDocumentNameWithoutPrefix");
        String result = rd.getDocumentNameWithoutPrefix();
        assertEquals("u2841_ke_bill_2010-2-25_eng.odt", result);
    }

    /**
     * Test of getReviewCopy method, of class ReviewDocuments.
     */
    @Test
    public void testGetReviewCopy() {
        try {
            System.out.println("getReviewCopy");
            BungeniOdfDocumentHelper hdoc = rd.getReviewCopy();
            File fhdoc = new File(hdoc.getDocumentPath());
            String hdocname = fhdoc.getName();
            System.out.println(hdocname);

            assertEquals("clerk_u2841_ke_bill_2010-2-25_eng.odt", hdocname);

            ReviewDocuments rd2 = new ReviewDocuments(hdoc, "ClerkConsolidationReview");
            BungeniOdfDocumentHelper hdoc2 = rd2.getReviewCopy();
            File fhdoc2 = new File(hdoc2.getDocumentPath());
            String hdocname2 = fhdoc2.getName();
            System.out.println(hdocname2);

            assertEquals("cons_u2841_ke_bill_2010-2-25_eng.odt", hdocname2);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Test of getOriginal method, of class ReviewDocuments.
     */
    @Test
    public void testGetOriginal() {
        System.out.println("getOriginal");
        BungeniOdfDocumentHelper result = rd.getOriginal();
        assertEquals("u2841_ke_bill_2010-2-25_eng.odt", (new File(result.getDocumentPath()) ).getName());
    }


}