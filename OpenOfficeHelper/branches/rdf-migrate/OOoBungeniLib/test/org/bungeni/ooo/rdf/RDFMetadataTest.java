
package org.bungeni.ooo.rdf;

import com.sun.star.rdf.Statement;
import com.sun.star.rdf.XMetadatable;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.XURI;
import com.sun.star.text.XTextSection;
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
public class RDFMetadataTest {

    public RDFMetadataTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMetadataRootURI method, of class RDFMetadata.
     */
    @Test
    public void testGetMetadataRootURI() throws Exception {
        System.out.println("getMetadataRootURI");
        RDFMetadata instance = null;
        XURI expResult = null;
        XURI result = instance.getMetadataRootURI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetaURI method, of class RDFMetadata.
     */
    @Test
    public void testGetMetaURI() throws Exception {
        System.out.println("getMetaURI");
        String metaName = "";
        RDFMetadata instance = null;
        XURI expResult = null;
        XURI result = instance.getMetaURI(metaName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDocumentMetadataGraph method, of class RDFMetadata.
     */
    @Test
    public void testGetDocumentMetadataGraph() throws Exception {
        System.out.println("getDocumentMetadataGraph");
        RDFMetadata instance = null;
        XNamedGraph expResult = null;
        XNamedGraph result = instance.getDocumentMetadataGraph();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initDocumentMetadataGraph method, of class RDFMetadata.
     */
    @Test
    public void testInitDocumentMetadataGraph() throws Exception {
        System.out.println("initDocumentMetadataGraph");
        RDFMetadata instance = null;
        XNamedGraph expResult = null;
        XNamedGraph result = instance.initDocumentMetadataGraph();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addSectionMetadata method, of class RDFMetadata.
     */
    @Test
    public void testAddSectionMetadata() {
        System.out.println("addSectionMetadata");
        XTextSection aSection = null;
        String sectionMetaName = "";
        String sectionMetaValue = "";
        RDFMetadata instance = null;
        Statement expResult = null;
        Statement result = instance.addSectionMetadata(aSection, sectionMetaName, sectionMetaValue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetadataByPredicate method, of class RDFMetadata.
     */
    @Test
    public void testGetMetadataByPredicate() {
        System.out.println("getMetadataByPredicate");
        XNamedGraph metadataGraph = null;
        XMetadatable xSubject = null;
        XURI xPredicate = null;
        RDFMetadata instance = null;
        Statement expResult = null;
        Statement result = instance.getMetadataByPredicate(metadataGraph, xSubject, xPredicate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSectionMetadataByName method, of class RDFMetadata.
     */
    @Test
    public void testGetSectionMetadataByName() {
        System.out.println("getSectionMetadataByName");
        XTextSection aSection = null;
        String sectionMetaName = "";
        RDFMetadata instance = null;
        Statement expResult = null;
        Statement result = instance.getSectionMetadataByName(aSection, sectionMetaName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeMetadataByPredicate method, of class RDFMetadata.
     */
    @Test
    public void testRemoveMetadataByPredicate() {
        System.out.println("removeMetadataByPredicate");
        XNamedGraph metadataGraph = null;
        XMetadatable xSubject = null;
        XURI xPredicate = null;
        RDFMetadata instance = null;
        boolean expResult = false;
        boolean result = instance.removeMetadataByPredicate(metadataGraph, xSubject, xPredicate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMetadata method, of class RDFMetadata.
     */
    @Test
    public void testGetMetadata() {
        System.out.println("getMetadata");
        XNamedGraph aGraph = null;
        XMetadatable xSubject = null;
        RDFMetadata instance = null;
        Statement[] expResult = null;
        Statement[] result = instance.getMetadata(aGraph, xSubject);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}