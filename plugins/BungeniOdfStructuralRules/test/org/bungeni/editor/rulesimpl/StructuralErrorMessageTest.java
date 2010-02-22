
package org.bungeni.editor.rulesimpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Ashok
 */
public class StructuralErrorMessageTest {

    public StructuralErrorMessageTest() {
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
     * Test of toErrorMessage method, of class StructuralErrorMessage.
     */
    @Test
    public void testToErrorMessage() {
        System.out.println("toErrorMessage");

       StructuralError err = new StructuralError();
       err.parentSectionType = "parent section";
       err.parentSectionName = "parent name";
       err.childSectionType = "child section";
       err.childSectionName = "child section name";
       err.failRuleType = "AllowedChildSections";
       String result = StructuralErrorMessage.toErrorMessage(err);
       org.junit.Assert.assertTrue("Error message parsing failed = " + result, !result.contains("{"));
    }

 

}