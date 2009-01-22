/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

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
public class BungeniURITest {

    
    BungeniURI myURI = null;
    public BungeniURITest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    
    }

    @Before
    public void setUp() {
        myURI = new BungeniURI("CountryCode~LanguageCode", "CountryCode~LanguageCode");
    }

    
    @After
    public void tearDown() {
    }



    /**
     * Test of parseURIs method, of class BungeniURI.
     */
    @Test
    public void testParseURIs() {
        System.out.println("parseURIs");
       myURI.setURIComponent("CountryCode", "ken");
       myURI.setURIComponent("LanguageCode", "eng");
       myURI.setURIComponent("PartName", "main");
       myURI.parseURIs();
       System.out.println("URI = " + myURI.getURI() + ", File URI= "+ myURI.getFileURI());
       // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

 

  


}