/*
 * NumberingSchemeFactoryTest.java
 * JUnit based test
 *
 * Created on May 7, 2008, 12:01 PM
 */

package org.bungeni.numbering.impl;

import junit.framework.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class NumberingSchemeFactoryTest extends TestCase {
    
    public NumberingSchemeFactoryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(NumberingSchemeFactoryTest.class);
        
        return suite;
    }

    /**
     * Test of getNumberingScheme method, of class org.bungeni.numbering.impl.NumberingSchemeFactory.
     */
    public void testGetNumberingScheme() {
        System.out.println("getNumberingScheme");
        
      /*create a numbering scheme object based on a parametr...
         *"ALPHA" will generate a alpha numeric numbering scheme object
         *"ROMAN" will generate a roman numeral numbering scheme object
         *"GENERAL" will generate a numeric serial numbering scheme object
         */
        IGeneralNumberingScheme inumScheme = null;
        inumScheme = NumberingSchemeFactory.getNumberingScheme("ROMAN");
        assertNotNull(inumScheme);
    }

 
    
}
