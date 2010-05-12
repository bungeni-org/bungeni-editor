/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import org.bungeni.test.BungeniEditorTestCase;
import org.bungeni.utils.BungeniBTree;

/**
 *
 * @author undesa
 */
public class DocumentSectionProviderTest extends BungeniEditorTestCase {

    public DocumentSectionProviderTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DocumentSectionProvider.initialize(ooDoc);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Test of getNewTree method, of class DocumentSectionProvider.
     */
    public void testGetNewTree() {
        System.out.println("getNewTree");
        BungeniBTree expResult = null;
        BungeniBTree result = DocumentSectionProvider.getNewTree();
        if (result == null) {
            fail("getNewTree generated null");
        }
        assertNotNull(result);
        String treeOutput = result.toString();
        System.out.println("newTree = " + treeOutput);        
        int nCount = result.getRootCount();
        System.out.println("treeCount = " + treeOutput);      
        assertEquals(nCount > 0 , true);
        
    }

    /**
     * Test of getNewFriendlyTree method, of class DocumentSectionProvider.
     */
   public void testGetNewFriendlyTree() {
        System.out.println("getNewFriendlyTree ");
        BungeniBTree expResult = null;
        BungeniBTree result = DocumentSectionProvider.getNewFriendlyTree();
        if (result == null) {
            fail("getNewFriendlyTree generated null");
        }
        assertNotNull(result);
        String treeOutput = result.toString();
        System.out.println("NewFriendlyTree = " + treeOutput);        
        int nCount = result.getRootCount();
        System.out.println("treeCount = " + treeOutput);      
        assertEquals(nCount > 0 , true);
        
    }
  

}
