/*
 * Clipboard.java
 *
 * Created on November 14, 2007, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.extutils;

/**
 * Based on code by Alex Ruiz (http://www.jroller.com/alexRuiz/)
 *Clipboard manager class
 */
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public final class Clipboard {

  public static String text() throws Exception {
    Object data = clipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
    if (data instanceof String) return (String)data;
    return null;
  }

  public static void clear() { 
    try {
      clipboard().setContents(new Transferable() {
        public DataFlavor[] getTransferDataFlavors() {
          return new DataFlavor[0];
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
          return false;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
          throw new UnsupportedFlavorException(flavor);
        }
      }, null);
    } catch (IllegalStateException e) {}        
  }
  
  public static void copy(String toCopy) {
    clipboard().setContents(new StringSelection(toCopy), null);    
  }
  
  private static java.awt.datatransfer.Clipboard clipboard() {
    return Toolkit.getDefaultToolkit().getSystemClipboard();
  }
  
  private Clipboard() {}
}
