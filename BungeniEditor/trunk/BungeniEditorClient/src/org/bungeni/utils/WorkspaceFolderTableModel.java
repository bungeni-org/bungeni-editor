/*
 * WorkspaceFolderTableModel.java
 *
 * Created on July 3, 2007, 12:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.io.File;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class WorkspaceFolderTableModel  extends AbstractTableModel  {
  protected File dir;
  protected String[] filenames;

  protected String[] columnNames = new String[] {
    "name", "size", "last modified" //, "directory?", "readable?", "writable?"
  };

  protected Class[] columnClasses = new Class[] { 
    String.class, Long.class, Date.class, 
      Boolean.class, Boolean.class, Boolean.class
  };

    /** Creates a new instance of WorkspaceFolderTableModel */
    public WorkspaceFolderTableModel(File dir) {
          this.dir = dir; 
          this.filenames = dir.list();  // Store a list of files in the directory
  }
   

    public int getRowCount() { 
        return filenames.length;
    }

    public int getColumnCount() {
        return 3;
    }

     // Information about each column
  public String getColumnName(int col) { return columnNames[col]; }
  public Class getColumnClass(int col) { return columnClasses[col]; }

  // The method that must actually return the value of each cell
  public Object getValueAt(int row, int col) {
    File f = new File(dir, filenames[row]);
    switch(col) {
    case 0: return filenames[row];
    case 1: return new Long(f.length());
    case 2: return new Date(f.lastModified());
    /*
    case 3: return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
    case 4: return f.canRead() ? Boolean.TRUE : Boolean.FALSE;
    case 5: return f.canWrite() ? Boolean.TRUE : Boolean.FALSE;
     */ 
    default: return null;
    }
  }
    
}
