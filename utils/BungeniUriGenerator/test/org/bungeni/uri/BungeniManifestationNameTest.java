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
public class BungeniManifestationNameTest {

    BungeniManifestationName fname ;
    public BungeniManifestationNameTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        fname = new BungeniManifestationName("CountryCode~LanguageCode~PartName.odt");
        fname.setURIComponent("CountryCode", "ken");
        fname.setURIComponent("LanguageCode", "eng");
        fname.setURIComponent("PartName", "main");
        fname.parse();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class BungeniManifestationName.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        String expResult = "ken_eng_main.odt";
        String result = fname.get();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

}