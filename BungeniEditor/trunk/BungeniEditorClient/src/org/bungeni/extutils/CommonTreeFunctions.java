/*
 * CommonTreeFunctions.java
 *
 * Created on August 20, 2007, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author Ashok
 */
public class CommonTreeFunctions {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonTreeFunctions.class.getName());
 
    /** Creates a new instance of CommonTreeFunctions */
    public CommonTreeFunctions() {
    }

    public static void expandAll(JTree tree) {
        int i = 0 ;
        while (i < tree.getRowCount()) {
            tree.expandRow(i);
            i++;
        }
    }

    public static void expandAll(JTree tree, int nRow) {
        tree.expandPath(tree.getPathForRow(nRow));
    }

    public static void expandAll(JTree tree, TreePath path) {
        tree.expandPath(path);
    }



    public static void collapseAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
    }

   
}
