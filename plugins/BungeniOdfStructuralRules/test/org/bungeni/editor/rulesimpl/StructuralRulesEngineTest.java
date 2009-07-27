

package org.bungeni.editor.rulesimpl;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openoffice.odf.doc.OdfDocument;

/**
 *
 * @author Ashok Hariharan
 */
public class StructuralRulesEngineTest {
          String docrules = "test/structural_rules/doc_rules/debaterecord.xml";
            String enginerules = "test/structural_rules/engine_rules/debaterecord.xml";
            OdfDocument odoc = null;
            int ERRORS_RESULT = 18;

    public StructuralRulesEngineTest() {
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
            odoc = OdfDocument.loadDocument(new File("test/testdoc/ke_debaterecord_2009-7-26_eng.odt"));
        } catch (Exception ex) {
          ex.printStackTrace();
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of processRulesForDocument method, of class StructuralRulesEngine.
     */
    @Test
    public void testProcessRulesForDocument() {

            //configure the source files
            String ruleEnginesFile = enginerules;
            String docRulesFile = docrules;
            String[] runTheseRules =  {"AllowedChildSections", "OrderOfChildSections"};
            //initalize the rules engine
            StructuralRulesEngine sre = new StructuralRulesEngine(docRulesFile, ruleEnginesFile, runTheseRules);
            boolean bState = sre.processRulesForDocument(odoc);
            org.junit.Assert.assertEquals(ERRORS_RESULT, sre.getErrors().size());
     
    }

    

}