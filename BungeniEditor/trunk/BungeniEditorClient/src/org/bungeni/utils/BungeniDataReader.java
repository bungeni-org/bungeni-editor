/*
 * BungeniDataReader.java
 *
 * Created on August 8, 2007, 7:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class BungeniDataReader {
   
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniDataReader.class.getName());
 
    /** Creates a new instance of BungeniDataReader */
    public BungeniDataReader() {
     }
    
    public Vector<Vector> read(String fileName){
              Vector<Vector> vFile = new Vector<Vector>();
    
           try {
          
          CommonFileFunctions cfsObject = new CommonFileFunctions();
          Installation installObject = new Installation();
          File dir =  installObject.getInstallDirectory(this.getClass());
         
          String dataFilePath = dir.getAbsolutePath()+dir.separator+"data" +dir.separator +fileName;
          FileReader frData = new FileReader(dataFilePath);
          CSVReader reader = new CSVReader(frData);
          String[] nextLine;
            
            while ((nextLine = reader.readNext()) != null ) {
            Vector<String> vNextLine = new Vector<String>();
              
              for (int i=0; i < nextLine.length ; i++ ) {
                  vNextLine.add(nextLine[i]);
              }
            
              vFile.add(vNextLine);
            }
          
        } catch (IOException ex) {
            log.error("BungeniDataReader.get : " + ex.getLocalizedMessage(), ex);
        }  finally {
            return vFile;
        }
    }
 
}
