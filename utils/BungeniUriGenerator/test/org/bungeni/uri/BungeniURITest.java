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

    BungeniURI myUri;
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
        myUri = new BungeniURI("CountryCode~LanguageCode~PartName");
        myUri.setURIComponent("CountryCode", "ken");
        myUri.setURIComponent("LanguageCode", "eng");
        myUri.setURIComponent("PartName", "main");
        myUri.parse();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class BungeniURI.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        String expResult = "ken/eng/main";
        String result = myUri.get();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

}