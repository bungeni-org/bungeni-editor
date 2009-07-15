
package org.bungeni.ooo;

import com.sun.star.awt.Rectangle;
import com.sun.star.awt.XWindow;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
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

       private static XDesktop m_ooDesktop = null;
       private static XMultiComponentFactory m_ooMCF = null;
       private XComponentContext m_ooContext = null;
       private static XComponentLoader m_ooComponentLoader = null;
       private XComponent m_ooComponent = null;
       private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungenioOoHelper.class.getName());
       
    public BungenioOoHelper(XComponentContext context) {
        m_ooContext = context;
    }
    
    public static XDesktop getDesktop(){
        return m_ooDesktop;
    }
    
    
    public static XMultiComponentFactory getMultiComponentFactory(){
        return m_ooMCF;
    }
    
    
    public XComponentContext getComponentContext(){
        return m_ooContext;
    }
    
    
    public static XComponentLoader getComponentLoader(){
        return m_ooComponentLoader;
    }
    
    public XComponent getComponent(){
        return m_ooComponent;
    }
    
    /*
     * Returns component handle to newly opened document
     */
    public static String convertPathToURL(String path){
        File file = new File(path);
        String strURL = "";
        URL url = null;
        try {
            //file.toURL() was deprecated for JDK 1.6
            URI fileURI = file.toURI();
            url = fileURI.toURL();
            strURL = url.toString();
            strURL = strURL.replace("file:/", "file:///");
        } catch (MalformedURLException ex) {
           log.debug(ex.getLocalizedMessage(), ex); 
        }
        finally {
            return strURL;
        }
        
    }
    
    public XComponent openDocument(String strDocument){
            try {
        XComponent xComponent = null;
            PropertyValue[] loadProps = new com.sun.star.beans.PropertyValue[1];
            PropertyValue xOpenProperty = new com.sun.star.beans.PropertyValue();
            xOpenProperty.Name = "MacroExecutionMode";
            xOpenProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE ;
            loadProps[0] = xOpenProperty;
    
            m_ooComponent =  m_ooComponentLoader.loadComponentFromURL(strDocument, "_blank", 0, loadProps);
             positionWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
          finally{
                return m_ooComponent;
            }
    }
    public XComponent newDocument(String strTemplatePath){
        XComponent xComponent = null ;
        try {
            PropertyValue[] loadProps = new com.sun.star.beans.PropertyValue[2];
            PropertyValue xTemplateProperty = new com.sun.star.beans.PropertyValue();
            xTemplateProperty.Name = "Template";
            xTemplateProperty.Value = true;
            loadProps[0] = xTemplateProperty;
            com.sun.star.beans.PropertyValue xMacroExecProperty = new com.sun.star.beans.PropertyValue();
            xMacroExecProperty.Name = "MacroExecutionMode";
            xMacroExecProperty.Value = com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE;
            loadProps[1] = xMacroExecProperty;
            if (strTemplatePath.equals(""))
                strTemplatePath = "private:factory/swriter";
            //launch window
            m_ooComponent = m_ooComponentLoader.loadComponentFromURL(strTemplatePath, "_blank", 0, loadProps);
            //now maximize document frame
            positionWindow();
 
           
            
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        finally {
            return m_ooComponent;
        }
        }
    
    private void positionWindow(){
            XWindow xWind =  m_ooDesktop.getCurrentFrame().getContainerWindow();
            Rectangle rect = xWind.getPosSize();
           int intXPos = rect.X;
            int intYPos = rect.Y; 
           java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        
            int intHeight = screenSize.height - 30; //712;
            int intWidth = screenSize.width ; //
            short nSetpos=15;
            xWind.setPosSize(intXPos, intYPos, intWidth, intHeight, nSetpos); 
     
        
    }
    public void initoOo(){
         try{
            if (m_ooMCF==null) {
                 m_ooMCF = m_ooContext.getServiceManager();
            }
            if (m_ooMCF != null )
            {
                Object oDesktop = m_ooMCF.createInstanceWithContext("com.sun.star.frame.Desktop", m_ooContext);
                // (4a) get the XDesktop interface object
               m_ooDesktop = (com.sun.star.frame.XDesktop)
                   com.sun.star.uno.UnoRuntime.queryInterface(
                   com.sun.star.frame.XDesktop.class, oDesktop);
               
                 // (4b) get the desktop's component loader interface object
               m_ooComponentLoader = (com.sun.star.frame.XComponentLoader)
                   com.sun.star.uno.UnoRuntime.queryInterface(
                   com.sun.star.frame.XComponentLoader.class, m_ooDesktop);
       }  
    }
    catch (java.lang.Exception e)
    {
                    e.printStackTrace();
    }
    }
}
