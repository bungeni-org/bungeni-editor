/*
 * IEditorActionEvent.java
 *
 * Created on August 20, 2007, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions;

import java.util.ArrayList;
import javax.swing.JFrame;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface IEditorActionEvent {
       public void doCommand(OOComponentHelper ooDocument, toolbarAction action, javax.swing.JFrame parentFrame);
       public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, javax.swing.JFrame parentFrame);
       public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, javax.swing.JFrame parentFrame);

   
}
