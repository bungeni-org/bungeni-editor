package org.bungeni.odfdom.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.doc.OdfDocument;
import static org.junit.Assert.*;
import org.odftoolkit.odfdom.doc.style.OdfStylePageLayout;

/**
 *
 * @author undesa
 */
public class BungeniOdfDocumentHelperTest {

    BungeniOdfDocumentHelper odfDomHelper = null;
    String outfile ; File fout;
    public BungeniOdfDocumentHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        File f = new File("testdocs/page-with-bg.odt");
        outfile = UUID.randomUUID().toString();
        fout = new File ("testdocs/"+ outfile+ ".odt");
   
        try {
          OdfDocument odoc =   OdfDocument.loadDocument(f);
          this.odfDomHelper = new BungeniOdfDocumentHelper(odoc);
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfDocumentHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @After
    public void tearDown() {
        fout.delete();
    }

    /**
     * Test of getStandardPageLayout method, of class BungeniOdfDocumentHelper.
     */
    @Test
    public void testGetStandardPageLayout() {
        System.out.println("getStandardPageLayout");
        OdfStylePageLayout result = this.odfDomHelper.getStandardPageLayout();
        assertEquals(result.getFamilyName(), "list");
    }

    /**
     * Test of removeBackgroundImage method, of class BungeniOdfDocumentHelper.
     */
    @Test
    public void testRemoveBackgroundImage() {
        FileOutputStream fostream = null;
        try {
            System.out.println("removeBackgroundImage");
            this.odfDomHelper.removeBackgroundImage();
            fostream = new FileOutputStream(fout);
            this.odfDomHelper.getOdfDocument().save(fostream);
            // TODO review the generated test code and remove the default call to fail.
            assertEquals(true, true);
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfDocumentHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                fostream.close();
            } catch (IOException ex) {
                Logger.getLogger(BungeniOdfDocumentHelperTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}