/*
 * BungeniToolbarConditionTest.java
 * JUnit based test
 *
 * Created on May 22, 2008, 1:02 PM
 */

package org.bungeni.editor.toolbar.conditions;

import junit.framework.*;

/**
 *
 * @author Administrator
 */
public class BungeniToolbarConditionTest extends TestCase {
    BungeniToolbarCondition instanceOne = null;
    BungeniToolbarCondition instanceTwo = null;
    
    public BungeniToolbarConditionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        instanceOne = new BungeniToolbarCondition("sectionNotExists:root");
        instanceTwo = new BungeniToolbarCondition("sectionNotExists", "root");
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getConditionName method, of class org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition.
     */
    public void testGetConditionName() {
        System.out.println("getConditionName");
        
        assertTrue("instanceOne is null ", instanceOne == null );
        String result = instanceOne.getConditionName();
        System.out.println(result);
        
   
        // TODO review the generated test code and remove the default call to fail.
        
    }

    

    /**
     * Test of getConditionValue method, of class org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition.
     */
    public void testGetConditionValue() {
        System.out.println("getConditionValue");
        
      //  BungeniToolbarCondition instance = null;
        
        //String expResult = "";
       // String result = instance.getConditionValue();
       // assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of getConditionClass method, of class org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition.
     */
    public void testGetConditionClass() {
        System.out.println("getConditionClass");
        
//        BungeniToolbarCondition instance = null;
  //      
       // String expResult = "";
    ///    String result = instance.getConditionClass();
       // assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    
}
