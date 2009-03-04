

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.plugin;

import java.util.HashMap;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.bungeni.editor.plugin.impl.IEditorPluginAll;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public abstract class EditorPlugin implements IEditorPluginAll {
    
    protected String installDirectory;
    protected OOComponentHelper ooDocument ;
    protected JFrame frame;
    protected HashMap<String,Object> paramaterMap = new HashMap<String, Object>();
    private static org.apache.log4j.Logger log = Logger.getLogger(EditorPlugin.class.getName());
  
    public  void setInstallDirectory(String path){
        this.installDirectory = path;
    }

    public  String getInstallDirectory() {
        return installDirectory;
    }

    public abstract boolean invoke() ;

    public void setOOComponentHelper(OOComponentHelper ooDocument) {
        this.ooDocument = ooDocument;
    }

    public void setParentFrame(JFrame frame){
        this.frame = frame;
    }
    
    public void addParameter(String paramName, Object parameterValue) {
        paramaterMap.put(paramName, parameterValue);
    }

    public  Object getParameter(String paramName) {
        return this.paramaterMap.get(paramName);
    }
    
    public final HashMap<String,Object> getParameters() {
        return this.paramaterMap;
    }
    
    public static IEditorPluginAll makeInstance(String classPath) {
             IEditorPluginAll pPlugin = null;
            try {
                Class containerPanel = Class.forName(classPath);
                pPlugin = (IEditorPluginAll) containerPanel.newInstance();
             } catch (InstantiationException ex) {
               log.debug("makeInstance :"+ ex.getMessage());
               } catch (IllegalAccessException ex) {
               log.debug("makeInstance :"+ ex.getMessage());
               }  catch (ClassNotFoundException ex) {
               log.debug("makeInstance :"+ ex.getMessage());
              } catch (NullPointerException ex) {
               log.debug("makeInstance :"+ ex.getMessage());
              } finally {
                  return pPlugin;
              }
        }

}
