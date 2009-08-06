package org.bungeni.plugins.structuralvalidator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import org.bungeni.editor.rulesimpl.StructuralRulesConfig;
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
public class BungeniStructuralValidatorTest {

    BungeniStructuralValidator validator = null;
    String inputFile = "test/testdoc/ke_debaterecord_2009-7-26_eng.odt";
    String rulesRoot = "test/structural_rules";
    String expectedOutputFile = "test/testdoc/plugin_test_output.txt";
    String expectedResult = "<error />";

    public BungeniStructuralValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws MalformedURLException, FileNotFoundException, IOException {
        //build expected output
        FileReader freader = new FileReader(this.expectedOutputFile);
        StringWriter swriter = new StringWriter();
        int c ;
        while ( (c = freader.read()) != -1) {
            swriter.write(c);
        }
        this.expectedResult = swriter.toString().trim();
        //build object
        validator = new BungeniStructuralValidator();
        HashMap structValidator = new HashMap();
        File fFile = new File(this.inputFile);
        File fRules = new File (rulesRoot);
        System.out.println("rules path = " + fRules.getAbsolutePath());
        String filePath = fFile.toURI().toURL().toString();
        structValidator.put("OdfFileURL", filePath);
        structValidator.put("RulesRootFolder", fRules.getAbsolutePath() );
        structValidator.put("CurrentDocType", "debaterecord");
        String[] runtheseChecks = {"AllowedChildSections", "OrderOfChildSections"};
        structValidator.put("RunChecks", runtheseChecks );
        validator.setParams(structValidator);
        StructuralRulesConfig.APPLN_PATH_PREFIX = fRules.getAbsolutePath();
    }

    @After
    public void tearDown() {
    }



  
    /**
     * Test of exec method, of class BungeniStructuralValidator.
     */
    @Test
    public void testExec() throws UnsupportedEncodingException, IOException {
        String result = this.validator.exec().trim();
        assertEquals(result, this.expectedResult);
    }



}