/*
 * OOComponentHelperTest.java
 * JUnit based test
 *
 * Created on July 24, 2007, 11:48 PM
 */

package org.bungeni.ooo;

import junit.framework.*;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.document.XDocumentInfo;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;

/**
 *
 * @author Administrator
 */
public class OOComponentHelperTest extends TestCase {
    
    public OOComponentHelperTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getServiceInfo method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetServiceInfo() {
        System.out.println("getServiceInfo");
        
        Object obj = null;
        OOComponentHelper instance = null;
        
        XServiceInfo expResult = null;
        XServiceInfo result = instance.getServiceInfo(obj);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTextContent method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetTextContent() {
        System.out.println("getTextContent");
        
        Object element = null;
        OOComponentHelper instance = null;
        
        XTextContent expResult = null;
        XTextContent result = instance.getTextContent(element);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTextDocument method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetTextDocument() {
        System.out.println("getTextDocument");
        
        OOComponentHelper instance = null;
        
        XTextDocument expResult = null;
        XTextDocument result = instance.getTextDocument();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDocumentModel method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetDocumentModel() {
        System.out.println("getDocumentModel");
        
        OOComponentHelper instance = null;
        
        XModel expResult = null;
        XModel result = instance.getDocumentModel();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDocumentFactory method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetDocumentFactory() {
        System.out.println("getDocumentFactory");
        
        OOComponentHelper instance = null;
        
        XMultiServiceFactory expResult = null;
        XMultiServiceFactory result = instance.getDocumentFactory();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createInstance method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testCreateInstance() {
        System.out.println("createInstance");
        
        String instanceName = "";
        OOComponentHelper instance = null;
        
        Object expResult = null;
        Object result = instance.createInstance(instanceName);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTextSection method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testCreateTextSection() {
        System.out.println("createTextSection");
        
        String sectionName = "";
        short numberOfColumns = 0;
        OOComponentHelper instance = null;
        
        XTextContent expResult = null;
        XTextContent result = instance.createTextSection(sectionName, numberOfColumns);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getObjectPropertySet method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetObjectPropertySet() {
        System.out.println("getObjectPropertySet");
        
        Object obj = null;
        OOComponentHelper instance = null;
        
        XPropertySet expResult = null;
        XPropertySet result = instance.getObjectPropertySet(obj);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getViewCursor method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetViewCursor() {
        System.out.println("getViewCursor");
        
        OOComponentHelper instance = null;
        
        XTextViewCursor expResult = null;
        XTextViewCursor result = instance.getViewCursor();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDocumentInfo method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetDocumentInfo() {
        System.out.println("getDocumentInfo");
        
        OOComponentHelper instance = null;
        
        XDocumentInfo expResult = null;
        XDocumentInfo result = instance.getDocumentInfo();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProperty method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testAddProperty() {
        System.out.println("addProperty");
        
        String propertyName = "";
        String value = "";
        OOComponentHelper instance = null;
        
        instance.addProperty(propertyName, value);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPropertyValue method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testSetPropertyValue() {
        System.out.println("setPropertyValue");
        
        String propertyName = "";
        String propertyValue = "";
        OOComponentHelper instance = null;
        
        instance.setPropertyValue(propertyName, propertyValue);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertyValue method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetPropertyValue() throws Exception {
        System.out.println("getPropertyValue");
        
        String propertyName = "";
        OOComponentHelper instance = null;
        
        String expResult = "";
        String result = instance.getPropertyValue(propertyName);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTextSections method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetTextSections() {
        System.out.println("getTextSections");
        
        OOComponentHelper instance = null;
        
        XNameAccess expResult = null;
        XNameAccess result = instance.getTextSections();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testGetComponent() {
        System.out.println("getComponent");
        
        OOComponentHelper instance = null;
        
        XComponent expResult = null;
        XComponent result = instance.getComponent();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of propertyExists method, of class org.bungeni.ooo.OOComponentHelper.
     */
    public void testPropertyExists() {
        System.out.println("propertyExists");
        
        String propertyName = "";
        OOComponentHelper instance = null;
        
        boolean expResult = true;
        boolean result = instance.propertyExists(propertyName);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
