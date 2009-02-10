/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;
/*
 * OfdfJDomTransferable.java
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
import javax.swing.tree.TreePath;

/**
 *
 * @author Ashok Hariharan
 */
  class OdfJDomTransferable implements Transferable {

	/**
	 * construct a transferabe with a given object to transfer
	 * @param data  the data object to transfer, in this case an array of treepath selections
	 */
      public OdfJDomTransferable(TreePath[] data) {
        super();
        this.data = data;
      }

      /**
       * get the data flavors supported by this object
       * @return an array of supported data flavors
       */
      public DataFlavor[] getTransferDataFlavors() {
         return (DataFlavor[])flavors.clone();
      }

      /**
       * determine whether or not a given data flavor is supported by this transferable
       * @return true, if the given data flavor is supported
       */
      public boolean isDataFlavorSupported(DataFlavor flavor) {
            for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
      }

      /**
       * get the data this transferable transports
       * @return the data transported by this transferable
       */
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(flavors[0])) {
            return (Object)data;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
      }

      
      /** the data this transferable transports */
      private TreePath[] data;

      /** storage for data flavors supported of this transferable */
      public static DataFlavor localObjectFlavor;

      /** the actual flavors supported by this transferable */
        static {
            try {
                localObjectFlavor =
                    new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                        "; class=" + "\"" + TreePath.class.getName() + "\"");
                } catch (ClassNotFoundException e) {
                    // can not occur
                    e.printStackTrace();
                }
            }

        private static DataFlavor[] flavors = { localObjectFlavor };
      
      
      
    }

