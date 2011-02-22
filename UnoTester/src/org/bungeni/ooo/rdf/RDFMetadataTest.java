package org.bungeni.ooo.rdf;

import com.sun.star.rdf.Statement;
import com.sun.star.rdf.XMetadatable;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.XURI;
import com.sun.star.text.XTextSection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.test.UnoTest;


/**
 * Unit tests for RDFMetadata class.
 * Creates a new openoffice document out the template copy.
 * Applies and Reads metadata to a section during the tests.
 * @author Ashok Hariharan
 */
public class RDFMetadataTest extends UnoTest{

/**
     * Test of getMetadataRootURI method, of class RDFMetadata.
     */

    public void testGetMetadataRootURI() throws Exception {
        System.out.println("getMetadataRootURI");
        RDFMetadata instance = new RDFMetadata(ooComponent);
        XURI result = instance.getMetadataRootURI();
        p("result = " + result.getStringValue());
        assertNotTrue("http://editor.bungeni.org/1.0/anx".equals(result.getStringValue()), "testGetMetadataRootURI", "");
    }

    /**
     * Test of getMetaURI method, of class RDFMetadata.
     */

    public void testGetMetaURI() throws Exception {
        System.out.println("getMetaURI");
        RDFMetadata instance = new RDFMetadata(ooComponent);
        XURI result = instance.getMetaURI("Type");
        p("result = " + result.getStringValue());
        assertNotTrue("http://editor.bungeni.org/1.0/anx/Type".equals(result.getStringValue()), "testGetMetaURI", "");
    }


    /**
     * Test of initDocumentMetadataGraph method, of class RDFMetadata.
     */

    public void testInitDocumentMetadataGraph() throws Exception {
        System.out.println("initDocumentMetadataGraph");
        RDFMetadata instance = new RDFMetadata(ooComponent);
        XNamedGraph result = instance.initDocumentMetadataGraph();
        assertNotTrue(result != null, "testInitDocumentMetadataGraph", "");
    }

    /**
     * Test of getDocumentMetadataGraph method, of class RDFMetadata.
     */

    public void testGetDocumentMetadataGraph() throws Exception {
        System.out.println("getDocumentMetadataGraph");
        RDFMetadata instance = new RDFMetadata(ooComponent);
        XNamedGraph result = instance.getDocumentMetadataGraph();
         p(result.getStringValue());
        assertNotTrue(result != null, "testGetDocumentMetadataGraph", "");
    }


    /**
     * Test of addSectionMetadata method, of class RDFMetadata.
     */

    public void testAddSectionMetadata() {
        System.out.println("addSectionMetadata");
        XTextSection aSection = ooComponent.getSection("Section1");
        String sectionMetaName = "Type";
        String sectionMetaValue = "Clause";
        RDFMetadata instance = new RDFMetadata(ooComponent);
        Statement result = instance.addSectionMetadata(aSection, sectionMetaName, sectionMetaValue);
        assertNotTrue(result.Predicate.getLocalName().equals("Type"), "testAddSectionMetadata", "");
        assertNotTrue(result.Object.getStringValue().equals("Clause"), "testAddSectionMetadata", "");
    }

    public void testUpdateSectionMetadata() {
        System.out.println("addSectionMetadata");
        XTextSection aSection = ooComponent.getSection("Section1");
        String sectionMetaName = "Type";
        String sectionMetaValue = "OldTypeWasClause";
        RDFMetadata instance = new RDFMetadata(ooComponent);
        Statement result = instance.addSectionMetadata(aSection, sectionMetaName, sectionMetaValue);
        assertNotTrue(result.Predicate.getLocalName().equals("Type"), "testAddSectionMetadata", "");
        assertNotTrue(result.Object.getStringValue().equals("OldTypeWasClause"), "testAddSectionMetadata", "");
    }

    /**
     * Test of getSectionMetadataByName method, of class RDFMetadata.
     */

    public void testGetSectionMetadataByName() {
        System.out.println("getSectionMetadataByName");
        XTextSection aSection = ooComponent.getSection("Section1");
        String sectionMetaName = "Type";
        RDFMetadata instance = new RDFMetadata(ooComponent);
        Statement result = instance.getSectionMetadataByName(aSection, sectionMetaName);
        p(result.toString());
        assertNotTrue(result.Predicate.getLocalName().equals("Type"), "testGetSectionMetadataByName", "");
    }

    /**
     * Test of removeMetadataByPredicate method, of class RDFMetadata.
     */

    //@Test
    public void testRemoveMetadataByPredicate() {
        System.out.println("removeMetadataByPredicate");
        XNamedGraph metadataGraph = null;
        XMetadatable xSubject = null;
        XURI xPredicate = null;
        RDFMetadata instance = null;
        boolean expResult = false;
        boolean result = instance.removeMetadataByPredicate(metadataGraph, xSubject, xPredicate);
        assertNotTrue(expResult == result, "testRemoveMetadataByPredicate", "");
    }


    /**
     * Test of getMetadata method, of class RDFMetadata.
     */

    public void testGetSectionMetadata() {
        System.out.println("getMetadata");
        XTextSection aSection = ooComponent.getSection("Section1");
        RDFMetadata instance = new RDFMetadata(ooComponent);
        Statement[] result = instance.getSectionMetadata(aSection);
        for (Statement statement : result) {
            p(statement.toString());
        }
        assertNotTrue(1 == result.length, "testGetSectionMetadata", "");
    }


    @Override
    public void runTests(){
        try {
            super.runTests();

            testGetMetadataRootURI();
            testGetMetaURI();
            testInitDocumentMetadataGraph();
            testGetDocumentMetadataGraph();
            testAddSectionMetadata();
            testGetSectionMetadataByName();
            testGetSectionMetadata();
            testUpdateSectionMetadata();
            
        } catch (Exception ex) {
            Logger.getLogger(RDFMetadataTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
