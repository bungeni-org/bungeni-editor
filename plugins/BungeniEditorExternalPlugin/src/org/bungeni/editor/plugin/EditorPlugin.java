

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.plugin;

import javax.swing.JFrame;
import org.bungeni.editor.plugin.impl.IEditorPluginAll;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public abstract class EditorPlugin implements IEditorPluginAll {

    public abstract void setInstallDirectory(String path);

    public abstract String getInstallDirectory() ;

    public abstract boolean invoke() ;

    public abstract void setOOComponentHelper(OOComponentHelper ooDocument) ;

    public abstract void setParentFrame(JFrame frame);

}
