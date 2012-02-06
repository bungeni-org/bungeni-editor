/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.translators.utility.schemavalidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org. junit.BeforeClass;
import org.junit.Test;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;
import static org.junit.Assert.*;

/**
 * Test schema validator
 * @author Ashok Hariharan
 */
public class SchemaValidatorTest {

    SchemaValidator instance = null;
    String[] filesToValidate = {
        /* this one is always valid */
        "test/testdocs/schemavalidate_01.xml",

        /* this one is invalid uses DefaultHandler */
        /** cvc-complex-type.2.4.a: Invalid content was found starting with 
         * element 'p'. One of '{"http://www.akomantoso.org/1.0":from}' is
         * expected. **/
        "test/testdocs/schemavalidate_02.xml",

        /* this one is invalid uses DefaultHandler */
        /** <msg>cvc-complex-type.3.2.2: Attribute 'rff' is not allowed to
         * appear in element 'preface'.</msg>
        </msgs> **/
        "test/testdocs/schemavalidate_03.xml", 

        /* this one is invalid uses DefaultHandler */
        /** <msg>cvc-attribute.3: The value '666' of attribute 'id' on element
         * 'other' is not valid with respect to its type, 'ID'.</msg>
         */
        "test/testdocs/schemavalidate_04.xml",


        /**
         * <msg>cvc-datatype-valid.1.2.1: '1995-02-29' is not a valid value for 'date'.</msg>
         */
        "test/testdocs/schemavalidate_05.xml",


    };
     List<File> files = new ArrayList<File>(0);

     String aSchemaPath = null;
     String aPathToODFDocument = null;


    public SchemaValidatorTest() {
        instance = SchemaValidator.getInstance();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        System.out.println("Validator setup");
        GlobalConfigurations.setApplicationPathPrefix("resources/");
        //this is required by the library but not really for this test since we are
        //not doing a translation
        //GlobalConfigurations.setConfigurationFilePath("configfiles/configs/TranslatorConfig_debaterecord.xml");
        for (String filetovalidate : filesToValidate) {
            files.add(new File(filetovalidate));
        }
         aSchemaPath = "test/testdocs/schemavalidate_test.xsd";
         aPathToODFDocument = "test/testdocs/test.odt";
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of validate method, of class SchemaValidator.
     */
    @Test
    public void testValidate() throws Exception {
        System.out.println("validate");
        for (File f : files) {
            instance.validate(f, aPathToODFDocument, aSchemaPath);
            System.out.println("\nErrors for :" + f.getName());
            List<ValidationError> errors  = instance.getValidationErrors();
            System.out.println("Found " + errors.size() + " errors");
            for (ValidationError validationError : errors) {
                System.out.println(validationError.getXmlString());
            }
        }
        fail("The test case is a prototype.");
    }


}