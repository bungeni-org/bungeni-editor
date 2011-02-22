/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.test;

import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import com.sun.star.frame.XComponentLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class UnoTest {


    protected static XComponentContext m_xContext = null;
    protected static XMultiComponentFactory m_ooMCF = null;
    protected static XDesktop m_ooDesktop = null;
    protected static XComponentLoader m_ooComponentLoader = null;
    protected static OOComponentHelper ooComponent = null;




    public static void disposeDocument(){
        ooComponent.getComponent().dispose();
        ooComponent = null;
    }

    protected static void initoOo() {

        try {
            if (m_xContext == null ) {

            m_xContext = Bootstrap.bootstrap();
            if (m_xContext == null) {
                System.out.println("OpenOffice wasnt initialized");
            }
            if (m_ooMCF == null) {
                m_ooMCF = m_xContext.getServiceManager();
            }
            if (m_ooMCF != null) {
                Object oDesktop = m_ooMCF.createInstanceWithContext("com.sun.star.frame.Desktop", m_xContext);
                // (4a) get the XDesktop interface object
                m_ooDesktop = (com.sun.star.frame.XDesktop) com.sun.star.uno.UnoRuntime.queryInterface(
                        com.sun.star.frame.XDesktop.class, oDesktop);

                // (4b) get the desktop's component loader interface object
                m_ooComponentLoader = (com.sun.star.frame.XComponentLoader) com.sun.star.uno.UnoRuntime.queryInterface(
                        com.sun.star.frame.XComponentLoader.class, m_ooDesktop);
            }
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public void setupTests(String document) throws IOException {
        initoOo();
        initDocument(document);
    }

    public void runTests() throws InterruptedException {
        Thread t = Thread.currentThread();
        synchronized (t) {
            t.wait(5000);
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }

            if (destination != null) {
                destination.close();
            }
        }
    }

    protected static void initDocument(String pathToFile) throws IOException {

        File fileTest = new File(pathToFile);
        String fname = fileTest.getName();
        String fpath = fileTest.getParent();
        File fcopy = new File(fpath + File.separator + "copy_" + fname);
        if (fcopy.exists()) {
            fcopy.delete();
        }
        copyFile(fileTest, fcopy);

        XComponent xComp = OOComponentHelper.openExistingDocument(fcopy.getAbsolutePath(), m_ooComponentLoader);
        if (xComp == null) {
            System.out.println("XComponent was null for : " + fcopy.getAbsolutePath());
        }
        ooComponent = new OOComponentHelper(xComp, m_xContext);
    }


   


     protected void assertNotTrue(boolean bState, String method, String message) {
      if (bState) {
          SUCCESS(method, message);
      } else {
          FAIL(method, message);
      }
    }

    protected void SUCCESS (String method, String message) {
        System.out.println("SUCCESS : " + method +  " -- " + message);
    }

    protected void FAIL(String method, String message) {
        System.out.println("FAIL : " + method +  " -- " + message);

    }

    protected void p(String msg) {
        System.out.println(msg);
    }

}
