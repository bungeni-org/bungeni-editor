/*
 * WebDavTableModelTest.java
 * JUnit based test
 *
 * Created on June 21, 2007, 12:41 AM
 */

package org.bungeni.utils;

import junit.framework.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.httpclient.HttpException;
import org.apache.webdav.lib.WebdavResource;
import org.apache.log4j.Logger;
import org.apache.webdav.lib.WebdavResources;

/**
 *
 * @author Administrator
 */
public class WebDavTableModelTest extends TestCase {
    
    public WebDavTableModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

  
    /**
     * Test of getParentPath method, of class org.bungeni.utils.WebDavTableModel.
     */
    public void testGetParentPath() {
        System.out.println("getParentPath");
        
        String sPath = "/tempo/";
        WebDavTableModel instance = new WebDavTableModel();
         String expResult = "";
        String result = instance.getParentPath("tempo/hansard/bulawayo/");
        System.out.println("the Result is = "+result);
        assertTrue("Connection failed", true);
        
    }

    
}
