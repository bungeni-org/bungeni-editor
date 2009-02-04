/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;
/*
 * GenericTransferHandler.java
 *
 * Created on October 4, 2007, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
  class OdfJDomTransferable implements Transferable {

	/**
	 * construct a transferabe with a given object to transfer
	 * @param data  the data object to transfer
	 */
      public OdfJDomTransferable(Object data) {
        super();
        this.data = data;
      }

      /**
       * get the data flavors supported by this object
       * @return an array of supported data flavors
       */
      public DataFlavor[] getTransferDataFlavors() {
        return flavors;
      }

      /**
       * determine whether or not a given data flavor is supported by this transferable
       * @return true, if the given data flavor is supported
       */
      public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
      }

      /**
       * get the data this transferable transports
       * @return the data transported by this transferable
       */
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return data;
      }

      /** the data this transferable transports */
      private Object data;

      /** storage for data flavors supported of this transferable */
      private static final DataFlavor[] flavors = new DataFlavor[1];

      /** the actual flavors supported by this transferable */
      static {
        flavors[0] = DataFlavor.stringFlavor;
      }
    }

