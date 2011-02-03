/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.fcfilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author undesa
 */
  public  class ODTFileFilter extends FileFilter {

        @Override
        public boolean accept(File arg0) {
            if (arg0.isDirectory()) return true;
       
            String extension = getExtension(arg0);
            if (extension.equals("odt")) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
           return "OpenDocument files";
        }
        
        private String getExtension(File fname){
            String filename = fname.getName();
            int i = filename.lastIndexOf(".");
            if (i > 0 && i < filename.length() - 1)
                return filename.substring(i+1).toLowerCase();
            return "";
        }
       
        
    }