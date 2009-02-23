/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ashok
 */
public class OdfRefactorTest {
    OdfRefactor ref = null;

    public OdfRefactorTest() {
              
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        ref = new OdfRefactor("/home/undesa/Projects/Bungeni/SectionRefactorPlugin/testdocs/ken_bill_2009_1_10_eng_main.odt");
    }

    @After
    public void tearDown() {
    }

   

    /**
     * Test of saveDocument method, of class OdfRefactor.
     */
    @Test
    public void testSaveDocument() {
        try {
            boolean b = ref.moveSectionBefore("preamble", "part1");
            boolean result = ref.saveDocument();
            assertEquals(result, true);
            // TODO review the generated test code and remove the default call to fail.
        } catch (Exception ex) {
            Logger.getLogger(OdfRefactorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}