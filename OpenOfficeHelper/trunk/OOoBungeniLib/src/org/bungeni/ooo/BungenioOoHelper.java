package org.bungeni.ooo;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.awt.Rectangle;
import com.sun.star.awt.XWindow;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


/**
 * 11-04-2011 -- This class has been converted to a singleton
 * newDocument() and loadDocument() APIs have been removed
 * @author Ashok Hariharan
 */
public class BungenioOoHelper {
    private static BungenioOoHelper        instance            = null;
    private static org.apache.log4j.Logger log                 =
        org.apache.log4j.Logger.getLogger(BungenioOoHelper.class.getName());

    private XComponentContext              m_ooContext         = null;
    private XComponentLoader        m_ooComponentLoader = null;
    private XDesktop                m_ooDesktop         = null;
    private XMultiComponentFactory  m_ooMCF             = null;


    private BungenioOoHelper() {

    }

    /**
     * Singleton for BungenioOoHelper specific to a XComponentContext object
     * To use the returned object correctly call the init() method with the
     * XComponentContext handle
     * @param context
     * @return
     */
    public static BungenioOoHelper getInstance() {
        if (instance == null) {
            instance =  new BungenioOoHelper();
        }
        return instance;

    }

    /**
     * It is necessary to call this once after getInstance().
     * After the object is initialized
     * @param inContext
     */
    public void init(XComponentContext inContext){
        this.m_ooContext = inContext;
    }

    /**
     * Set the desktop handle
     * @param xDesk
     */
    public void setDesktop(XDesktop xDesk) {
        this.m_ooDesktop = xDesk;
    }

    /**
     * Get the OOo desktop handle
     * Warning : Do not call this directly from the Bungeni Editor code --
     * In Bungeni Editor desktop creation is done using the NOA library
     * @return XDesktop
     *
     */
    public XDesktop getDesktop() throws com.sun.star.uno.Exception {
        return m_ooDesktop;
    }


    /**
     * Return a OOo component creation factory object
     * @return
     */
    public XMultiComponentFactory getMultiComponentFactory() throws Exception {
        if (m_ooMCF == null) {
              m_ooMCF = m_ooContext.getServiceManager();
        }
        return m_ooMCF;
    }

    /**
     * Returns the XComponentContext
     * @return
     */
    public XComponentContext getComponentContext() {
        return m_ooContext;
    }

    /**
     * Returns a XComponent loader handle
     * @return
     * @throws com.sun.star.uno.Exception
     */
    public XComponentLoader getComponentLoader() throws com.sun.star.uno.Exception {
        if (m_ooComponentLoader == null) {
            if (m_ooDesktop == null ) {
                return null;
            }
            m_ooComponentLoader = ooQueryInterface.XComponentLoader(m_ooDesktop);
        }
        return m_ooComponentLoader;
    }



    /*
     * Returns component handle to newly opened document
     */
    public static String convertPathToURL(String path) {
        File   file   = new File(path);
        String strURL = "";
        URL    url  ;
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

}
