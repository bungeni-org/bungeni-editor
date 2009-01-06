/*
 * toolbarButtonEventInterface.java
 *
 * Created on July 24, 2007, 4:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels;

import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface ItoolbarButtonEvent {
    public void doCommand(OOComponentHelper ooDocument);
    public void doCommand(OOComponentHelper ooDocument, String cmd);
}
