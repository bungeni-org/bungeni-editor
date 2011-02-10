package org.bungeni.ooo;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.awt.Rectangle;
import com.sun.star.awt.XWindow;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.TerminationVetoException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XTerminateListener;
import com.sun.star.io.IOException;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Hariharan
 */
public class BungenioOoHelper {
    private static BungenioOoHelper        instance            = null;
    private static XComponentLoader        m_ooComponentLoader = null;
    private static XDesktop                m_ooDesktop         = null;
    private static XMultiComponentFactory  m_ooMCF             = null;
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(BungenioOoHelper.class.getName());
    private XComponent                     m_ooComponent       = null;
    private static XComponentContext              m_ooContext         = null;
    private static boolean m_bDesktopTerminated = false;

    public BungenioOoHelper(XComponentContext context) {
        m_ooContext = context;
    }

    public static BungenioOoHelper refreshInstance(XComponentContext context) {
        instance = new BungenioOoHelper(context);
        m_ooComponentLoader = null;
        m_ooDesktop         = null;
        m_ooMCF             = null;
        instance.initoOo();
        return instance;

    }

    /**
     * Singleton for BungenioOoHelper specific to a XComponentContext object
     * @param context
     * @return
     */
    public static BungenioOoHelper getInstance(XComponentContext context) {
        if (instance == null) {
            return new BungenioOoHelper(context);
        } else {
            return instance;
        }
    }

    /**
     * Get the OOo desktop handle
     * @return
     */
    public static XDesktop getDesktop() throws com.sun.star.uno.Exception {
        if (m_ooDesktop == null) {
                m_ooDesktop = newDesktop();
            if (m_ooDesktop != null ) m_bDesktopTerminated = false;
            return m_ooDesktop;
        }
        if (true == m_bDesktopTerminated) {
                m_ooDesktop = newDesktop();
            if (m_ooDesktop != null ) m_bDesktopTerminated = false;
            return m_ooDesktop;
        }

        return m_ooDesktop;
    }

    public static boolean isDesktopTerminated(){
        return m_bDesktopTerminated;
    }


    private static XDesktop newDesktop() throws com.sun.star.uno.Exception {
            Object oDesktop = getMultiComponentFactory().createInstanceWithContext("com.sun.star.frame.Desktop", m_ooContext);
                // (4a) get the XDesktop interface object
            XDesktop xDsk = (com.sun.star.frame.XDesktop) com.sun.star.uno.UnoRuntime.queryInterface(
                        com.sun.star.frame.XDesktop.class, oDesktop);
            xDsk.addTerminateListener(new XTerminateListener(){

                    public void queryTermination(EventObject arg0) throws TerminationVetoException {
                     
                    }

                    public void notifyTermination(EventObject arg0) {
                       log.debug("XTerminateListener : disposing desktop");
                       System.out.println("XTerminateListener : disposing desktop");
                       m_bDesktopTerminated = true;
                     
                    }

                    public void disposing(EventObject arg0) {
                    }

                });
           return xDsk;
    }

    /**
     * Return a OOo component creation factory ibject
     * @return
     */
    public static XMultiComponentFactory getMultiComponentFactory() throws Exception {
        if (m_ooMCF == null) {
              m_ooMCF = m_ooContext.getServiceManager();
        }
        return m_ooMCF;
    }

    public static XComponentContext getComponentContext() {
        return m_ooContext;
    }

    public static XComponentLoader getComponentLoader() throws com.sun.star.uno.Exception {
        if (m_ooComponentLoader == null) {
                //  get the desktop's component loader interface object
                m_ooComponentLoader = newComponentLoader();
                return m_ooComponentLoader;
        }
        if (true == m_bDesktopTerminated) {
                m_ooComponentLoader = newComponentLoader();
                return m_ooComponentLoader;
        }

        return m_ooComponentLoader;
    }

    private static XComponentLoader newComponentLoader() throws com.sun.star.uno.Exception{
        XComponentLoader xLoader = (com.sun.star.frame.XComponentLoader) com.sun.star.uno.UnoRuntime.queryInterface(
                    com.sun.star.frame.XComponentLoader.class, getDesktop());
        return xLoader;
    }

    public XComponent getComponent() {
        return m_ooComponent;
    }

    /*
     * Returns component handle to newly opened document
     */
    public static String convertPathToURL(String path) {
        File   file   = new File(path);
        String strURL = "";
        URL    url    = null;
        try {
            // file.toURL() was deprecated for JDK 1.6
            URI fileURI = file.toURI();
            url    = fileURI.toURL();
            strURL = url.toString();
            strURL = strURL.replace("file:/", "file:///");
        } catch (MalformedURLException ex) {
            log.debug(ex.getLocalizedMessage(), ex);
        } finally {
            return strURL;
        }
    }

    /**
     * openDocument()
     * @param strDocument
     * @return
     */
    public XComponent openDocument(String strDocument) {
        try {
            XComponent      xComponent    = null;
            PropertyValue[] loadProps     = new com.sun.star.beans.PropertyValue[1];
            PropertyValue   xOpenProperty = new com.sun.star.beans.PropertyValue();

            xOpenProperty.Name  = "MacroExecutionMode";
            xOpenProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
            loadProps[0]        = xOpenProperty;
            m_ooComponent       = m_ooComponentLoader.loadComponentFromURL(strDocument, "_blank", 0, loadProps);
            positionWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } finally {
            return m_ooComponent;
        }
    }

    /**
     * newDocument()
     * @param strTemplatePath
     * @return
     */
    public XComponent newDocument(String strTemplatePath) {
        XComponent xComponent = null;

        try {
            PropertyValue[] loadProps         = new com.sun.star.beans.PropertyValue[2];
            PropertyValue   xTemplateProperty = new com.sun.star.beans.PropertyValue();

            xTemplateProperty.Name  = "Template";
            xTemplateProperty.Value = true;
            loadProps[0]            = xTemplateProperty;

            com.sun.star.beans.PropertyValue xMacroExecProperty = new com.sun.star.beans.PropertyValue();

            xMacroExecProperty.Name  = "MacroExecutionMode";
            xMacroExecProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
            loadProps[1]             = xMacroExecProperty;

            if (strTemplatePath.equals("")) {
                strTemplatePath = "private:factory/swriter";
            }

            // launch window
            m_ooComponent = m_ooComponentLoader.loadComponentFromURL(strTemplatePath, "_blank", 0, loadProps);

            // now maximize document frame
            positionWindow();
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } finally {
            return m_ooComponent;
        }
    }

    /**
     * Positions the OpenOffice.org editor window to maximise and centered on the screen
     */
    private void positionWindow() {
        XWindow            xWind      = m_ooDesktop.getCurrentFrame().getContainerWindow();
        Rectangle          rect       = xWind.getPosSize();
        int                intXPos    = rect.X;
        int                intYPos    = rect.Y;
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int                intHeight  = screenSize.height - 30;    // 712;
        int                intWidth   = screenSize.width;          //
        short              nSetpos    = 15;

        xWind.setPosSize(intXPos, intYPos, intWidth, intHeight, nSetpos);
    }

    /**
     * Initializes OpenOffice.org
     */
    public void initoOo() {
        try {

            if (BungenioOoHelper.getMultiComponentFactory() != null) {
                if (BungenioOoHelper.getDesktop() != null) {
                    if (BungenioOoHelper.getComponentLoader() != null) {
                        return;
                    }
                }
                /*
                Object oDesktop = m_ooMCF.createInstanceWithContext("com.sun.star.frame.Desktop", m_ooContext);

                // (4a) get the XDesktop interface object
                m_ooDesktop = (com.sun.star.frame.XDesktop) com.sun.star.uno.UnoRuntime.queryInterface(
                    com.sun.star.frame.XDesktop.class, oDesktop);

                // (4b) get the desktop's component loader interface object
                m_ooComponentLoader = (com.sun.star.frame.XComponentLoader) com.sun.star.uno.UnoRuntime.queryInterface(
                    com.sun.star.frame.XComponentLoader.class, m_ooDesktop); */
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
