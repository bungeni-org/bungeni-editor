/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.interfaces.ui;

import javax.swing.LookAndFeel;

/**
 * Generic interface for Theme / look and feel classes.
 * A theme used by the editor must support this interface
 * @author Ashok Hariharan
 */
public interface ILookAndFeel {
    public LookAndFeel newLAFInstance();
}
