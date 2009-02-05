/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openoffice.odf.pkg.OdfPackage;

/**
 *
 * @author ashok
 */
public class OdfPackageBackup {

    class FileUtils{

        public void copyFile(File in, File out)
        throws IOException
    {
        FileChannel inChannel = new
            FileInputStream(in).getChannel();
        FileChannel outChannel = new
            FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
    }

    public class DateUtils {
        public  String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
        }
  }



    public OdfPackageBackup(OdfPackage pkg) {
        try {
            File origFile = new File(pkg.getBaseURI());
            String fileName = origFile.getName();
            String fileDir = origFile.getParentFile().getPath();
            DateUtils du = new DateUtils();
            String currentDateTime = du.now("yyyy-MM-dd-hh-mm");
            File backupFile = new File(fileDir + File.separator + "bk_" + currentDateTime + "_" + fileName);
            FileUtils fu = new FileUtils();
            fu.copyFile(origFile, backupFile);
            System.out.println("parent path = " + origFile.getParentFile().getPath());
        } catch (IOException ex) {
            Logger.getLogger(OdfPackageBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        try {
            OdfPackageBackup bkp = new OdfPackageBackup(OdfPackage.loadPackage("/Users/ashok/Desktop/ken_bill_2009_1_10_eng_main.odt"));

        } catch (Exception ex) {
            Logger.getLogger(OdfPackageBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
