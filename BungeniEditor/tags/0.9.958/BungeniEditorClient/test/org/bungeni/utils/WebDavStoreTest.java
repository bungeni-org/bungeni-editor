/*
 * WebDavStoreTest.java
 * JUnit based test
 *
 * Created on June 19, 2007, 5:36 PM
 */

package org.bungeni.utils;

import junit.framework.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.webdav.lib.WebdavResource;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.apache.webdav.lib.WebdavResources;

/**
 *
 * @author Administrator
 */
public class WebDavStoreTest extends TestCase {

        
    public WebDavStoreTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of connect method, of class org.bungeni.utils.WebDavStore.
     */
    public void testConnect() {
        System.out.println("connect");
        
        String filePath = "";
        
        WebDavStore instance = new WebDavStore();
        instance.setConnectionUrl("http://131.107.4.213");
	instance.setConnectionPort(1980);
	instance.setConnectionUsername("admin");
	instance.setConnectionPassword("password");
	instance.setConnectionBaseDirectory("tempo");
        
        instance.connect("");
        WebdavResource dav =instance.getResourceHandle();
        assertTrue("Connection failed", (dav != null));
  
    }

    /**
     * Test of setConnectionBaseDirectory method, of class org.bungeni.utils.WebDavStore.
     */
    
}
