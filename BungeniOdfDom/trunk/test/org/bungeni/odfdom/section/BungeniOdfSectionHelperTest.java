
package org.bungeni.odfdom.section;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author undesa
 */
public class BungeniOdfSectionHelperTest {
    BungeniOdfSectionHelper secHelper;
    BungeniOdfDocumentHelper docHelper;
    OdfDocument oDoc;
    public BungeniOdfSectionHelperTest() {
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
            this.oDoc = OdfDocument.loadDocument(new File("testdocs/section-test.odt"));
            this.docHelper = new BungeniOdfDocumentHelper (oDoc);
            this.secHelper = docHelper.getSectionHelper();
        } catch (Exception ex) {
            Logger.getLogger(BungeniOdfSectionHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
    }

  
    

    /**
     * Test of getSection method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testGetSection() {
        System.out.println("getSection");
        OdfTextSection result = secHelper.getSection("preface");
        String strSecName = result.getTextNameAttribute();
        assertEquals("preface", strSecName);
      
    }

        /**
     * Test of getChildSections method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testGetChildSections() {
        System.out.println("getChildSections");
         ArrayList<OdfTextSection> expResult = null;
         OdfTextSection aSection = secHelper.getSection("questions1");
        ArrayList<OdfTextSection> result = secHelper.getChildSections(aSection);
        assertEquals(1, result.size());
    }

    /**
     * Test of getSectionType method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testGetSectionType_OdfTextSection() {
        System.out.println("getSectionType");
        OdfTextSection nSection = secHelper.getSection("qa1");
        String result = secHelper.getSectionType(nSection);
        System.out.println(result);
        assertEquals("QuestionAnswer", result);
        }





    /**
     * Test of removeSectionBackgroundImage method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testRemoveSectionBackgroundImage() {
      String foutname = UUID.randomUUID().toString() + ".odt";
      File fout = new File("testdocs/"+ foutname);

      try {
        System.out.println("removeSectionBackgroundImage");
        OdfTextSection oSection = null;
        boolean expResult = true;
        OdfTextSection osec  = secHelper.getSection("qa1");
        boolean result = secHelper.removeSectionBackgroundImage(osec);
        secHelper.getOdfDocument().save(fout);
        assertEquals(expResult, result);
      } catch (Exception ex) {
        fail("exception while removing section background");
      } finally {
            if (fout.exists()) fout.delete();
      }
    }



    /**
     * Test of getSectionMetadataAttributes method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testGetSectionMetadataAttributes() {
        System.out.println("getSectionMetadataAttributes");
        OdfTextSection nSection = null;
        BungeniOdfSectionHelper instance = null;
        String expResult = "QuestionAnswer";
        nSection = secHelper.getSection("qa1");
        NamedNodeMap result = secHelper.getSectionMetadataAttributes(nSection);
        String sType = secHelper.getSectionType(nSection, result);
        assertEquals(expResult, sType);
    }

    /**
     * Test of getDocumentSections method, of class BungeniOdfSectionHelper.
     */
    @Test
    public void testGetDocumentSections() {
        System.out.println("getDocumentSections");
         int expCount = 4;
        NodeList result = secHelper.getDocumentSections();
        assertEquals(expCount, result.getLength());
    }



   
}