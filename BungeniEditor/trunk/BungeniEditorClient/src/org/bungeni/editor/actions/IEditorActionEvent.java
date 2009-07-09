package org.bungeni.editor.actions;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 *
 * @author Ashok Hariharan
 */
public interface IEditorActionEvent {
    public void doCommand(OOComponentHelper ooDocument, toolbarAction action, javax.swing.JFrame parentFrame);

    public void doCommand(OOComponentHelper ooDocument, toolbarSubAction action, javax.swing.JFrame parentFrame);

    public void doCommand(OOComponentHelper ooDocument, ArrayList<String> action, javax.swing.JFrame parentFrame);
}
