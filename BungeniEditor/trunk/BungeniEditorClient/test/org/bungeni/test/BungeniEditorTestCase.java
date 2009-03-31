/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.test;

import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import java.io.File;
import javax.swing.JFileChooser;
import junit.framework.TestCase;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.utils.fcfilter.ODTFileFilter;

/**
 *
 * @author undesa
 */
public class BungeniEditorTestCase extends TestCase {
    public XComponentContext m_xContext;
    public XMultiComponentFactory m_ooMCF;
    public XDesktop m_ooDesktop;
    public OOComponentHelper ooDoc;
    public XComponentLoader m_ooComponentLoader;
    
    public BungeniEditorTestCase(String name) {
        super(name);
        initoOo();
        File fileName=org.bungeni.extutils.CommonFileFunctions.getFileFromChooser("/home/undesa/Documents", new ODTFileFilter(), JFileChooser.FILES_ONLY, null);
        if (fileName == null) {
            fail("Document file handle was null");
        }
        XComponent xComp = OOComponentHelper.openExistingDocument(fileName.getAbsolutePath(), m_ooComponentLoader);
        if (xComp == null) {
            fail("XComponent was null for : "+ fileName.getAbsolutePath());
        }
        ooDoc = new OOComponentHelper(xComp, m_xContext);
        assertNotNull("OOComponentHandle was not initialized", ooDoc);
    }
    
    private void initoOo() {
        try {
            m_xContext = Bootstrap.bootstrap();
            if (m_xContext == null) {
                fail("OpenOffice wasnt initialized");
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
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

}
