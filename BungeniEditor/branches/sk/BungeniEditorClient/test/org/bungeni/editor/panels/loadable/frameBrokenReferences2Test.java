/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.loadable;

import com.sun.star.text.XTextField;
import java.util.ArrayList;
import javax.swing.JFrame;
import junit.framework.TestCase;
import org.bungeni.editor.panels.loadable.frameBrokenReferences2.LaunchMode;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class frameBrokenReferences2Test extends TestCase {
    
    public frameBrokenReferences2Test(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of findBrokenFrameSetExpanded method, of class frameBrokenReferences2.
     */
    public void testFindBrokenFrameSetExpanded() {
        System.out.println("findBrokenFrameSetExpanded");
        boolean bState = false;
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        instance.findBrokenFrameSetExpanded(bState);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fixBrokenFrameSetExpanded method, of class frameBrokenReferences2.
     */
    public void testFixBrokenFrameSetExpanded() {
        System.out.println("fixBrokenFrameSetExpanded");
        boolean bState = false;
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        instance.fixBrokenFrameSetExpanded(bState);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Launch method, of class frameBrokenReferences2.
     */
    public void testLaunch() {
        System.out.println("Launch");
        OOComponentHelper ooDoc = null;
        JFrame parentFrame = null;
        ArrayList<XTextField> brokenReferences = null;
        LaunchMode mode = null;
        frameBrokenReferences2 expResult = null;
        frameBrokenReferences2 result = frameBrokenReferences2.Launch(ooDoc, parentFrame, brokenReferences, mode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class frameBrokenReferences2.
     */
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        frameBrokenReferences2.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOOComponentHelper method, of class frameBrokenReferences2.
     */
    public void testSetOOComponentHelper() {
        System.out.println("setOOComponentHelper");
        OOComponentHelper ooDoc = null;
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        instance.setOOComponentHelper(ooDoc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOrphanedReferences method, of class frameBrokenReferences2.
     */
    public void testSetOrphanedReferences() {
        System.out.println("setOrphanedReferences");
        ArrayList<XTextField> brokenReferences = null;
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        instance.setOrphanedReferences(brokenReferences);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParentFrame method, of class frameBrokenReferences2.
     */
    public void testSetParentFrame() {
        System.out.println("setParentFrame");
        JFrame parentFrame = null;
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        instance.setParentFrame(parentFrame);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLaunchedState method, of class frameBrokenReferences2.
     */
    public void testGetLaunchedState() {
        System.out.println("getLaunchedState");
        frameBrokenReferences2 instance = new frameBrokenReferences2();
        boolean expResult = false;
        boolean result = instance.getLaunchedState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
