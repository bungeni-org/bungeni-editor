

package org.bungeni.editor.rulesimpl;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.doc.OdfDocument;

/**
 * !+ (rm, feb 2012) - ADDED COMMENTS
 * This test determines in the specified odf file has the required structure for
 * the relevant parliamentary document. However, the rules checker requires
 * that all the required sections for the parliamentary document be available or
 * else the structural rules checker will fail!
 * @author Ashok Hariharan
 */
public class StructuralRulesEngineTest {
          String docrules = "structural_rules/doc_rules/debaterecord.xml";
            String enginerules = "structural_rules/engine_rules/debaterecord.xml";
            OdfDocument odoc = null;
            
            // !+ (rm, feb 2012) - no errors expected
            // int ERRORS_RESULT = 18;
            int ERRORS_RESULT = 0;

            // !+ (rm, jan 2012) - added log4j instance
            private static org.apache.log4j.Logger log =
                    org.apache.log4j.Logger.getLogger(
                                        StructuralRulesEngineTest.class.getName());

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
            odoc = OdfDocument.loadDocument(new File("test/testdoc/ke_debaterecord_2010-5-13_eng.odt"));
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
            String[] runTheseRules =  { "AllowedChildSections", "OrderOfChildSections"};
            //initalize the rules engine
            StructuralRulesEngine sre = new StructuralRulesEngine(docRulesFile, ruleEnginesFile, runTheseRules);
            boolean bState = sre.processRulesForDocument(odoc);
            log.error("No. of errors = " + sre.getErrors().size());
            for (StructuralError e : sre.getErrors()) {
                log.info(e.toString());
            }
            org.junit.Assert.assertEquals(ERRORS_RESULT, sre.getErrors().size());
     
    }

    

}