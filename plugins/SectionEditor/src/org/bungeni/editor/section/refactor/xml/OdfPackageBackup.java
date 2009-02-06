/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openoffice.odf.pkg.OdfPackage;

/**
 * This class backs up an existing odf file into a subdirectory ".backup"
 * Every refactor action generates a backup
 * @author Ashok Hariharan
 */
public class OdfPackageBackup {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdfPackageBackup.class.getName());
    
    private OdfPackage odfPackage;

    public final static String BACKUP_FILE_NAME_FORMAT = "yyyyMMddhhmm";
    public final static String BACKUP_FILE_FOLDER = ".backup";
    /**
     * Copies source file to destination file. Uses the java nio libraries to do a bitstream transfer.
     * May have issues on windows for files > 64mb (to be tested)
     * @param sourceFile - file handle to source file
     * @param destFile - file handle to target file
     * @throws java.io.IOException
     */
    public void copyFile(File sourceFile, File destFile) throws IOException {
         if(!destFile.exists()) {
          destFile.createNewFile();
         }

         FileChannel source = null;
         FileChannel destination = null;
         try {
          source = new FileInputStream(sourceFile).getChannel();
          destination = new FileOutputStream(destFile).getChannel();
          destination.transferFrom(source, 0, source.size());
         }
         finally {
          if(source != null) {
           source.close();
          }
          if(destination != null) {
              destination.close();
            }
        }
    }


    public  String dateNow(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
        }



    public OdfPackageBackup(OdfPackage pkg) {
            this.odfPackage = pkg;
    }

    /**
     * Generates a new backup. Uses the current timestamp to generate a new backup file
     * @return
     */
    public File generateBackup() {
        File backupFile = null;
        try {
            URI pkgURI = new URI(odfPackage.getBaseURI());
            File origFile = new File(pkgURI);
            String fileName = origFile.getName();
            String fileDir = origFile.getParentFile().getPath();
            String currentDateTime = dateNow("yyyyMMddhhmm");
            String pathtoBackupDir = fileDir + File.separator+".backup";
            File fbackupdir = new File(pathtoBackupDir);
            if (!fbackupdir.exists()) {
                fbackupdir.mkdir();
            }
            backupFile = new File(pathtoBackupDir + File.separator + "bk_" + currentDateTime + "_" + fileName);
            copyFile(origFile, backupFile);
        } catch (URISyntaxException ex) {
            log.error("OdfPackageBackup " + ex.getMessage());
        } catch (IOException ex) {
            log.error("OdfPackageBackup " + ex.getMessage());
        } finally {
            return backupFile;
        }
    }
    
    public static void main(String[] args) {
        try {
            OdfPackageBackup bkp = new OdfPackageBackup(OdfPackage.loadPackage("/Users/ashok/Desktop/ken_bill_2009_1_10_eng_main.odt"));
            bkp.generateBackup();
        } catch (Exception ex) {
            Logger.getLogger(OdfPackageBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
