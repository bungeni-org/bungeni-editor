package org.bungeni.odfdom.utils;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.pkg.OdfPackage;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.channels.FileChannel;

/**
 * This class backs up an existing odf file into a subdirectory ".backup"
 * Every refactor action generates a backup
 * @author Ashok Hariharan
 */
public class BungeniOdfFileCopy {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOdfFileCopy.class.getName());
    private OdfPackage                     odfPackage;

    public BungeniOdfFileCopy(OdfPackage pkg) {
        this.odfPackage = pkg;
    }

  /**
     * Copies source file to destination file. Uses the java nio libraries to do a bitstream transfer.
     * May have issues on windows for files > 64mb (to be tested)
     * @param toFile - Destination file
     * @throws java.io.IOException
     */
    public void copyFile(File toFile) throws IOException  {
        File origFile = getPackageFile();
        copyFile(origFile, toFile);
    }


    /**
     * Copies source file to destination file. Uses the java nio libraries to do a bitstream transfer.
     * May have issues on windows for files > 64mb (to be tested)
     * @param sourceFile - file handle to source file
     * @param destFile - file handle to target file
     * @throws java.io.IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source      = null;
        FileChannel destination = null;

        try {
            source      = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }

            if (destination != null) {
                destination.close();
            }
        }
    }

    /**
     * Gets a file handle to the OdfPackage
     * @return
     */
    private File getPackageFile() {
        File origFile = null;

        try {
            URI pkgURI = new URI(odfPackage.getBaseURI());

            origFile = new File(pkgURI);
        } catch (URISyntaxException ex) {
            log.error("getPackageFile : " + ex.getMessage());
        } finally {
            return origFile;
        }
    }

    /**
     * Generates a new backup. Uses the current timestamp to generate a new backup file
     * @return
     */
    public File copyTo(String stringSuffix, boolean bOverWrite) {
        File backupFile = null;

        try {

            // get the original package file
            File   origFile    = getPackageFile();
            String fileName    = origFile.getName();
            String fileDir     = origFile.getParentFile().getPath();
            String newFileName = fileName.substring(0, fileName.indexOf(".odt")) + stringSuffix + ".odt";

            // generate the backup file
            backupFile = new File(fileDir + File.separator + newFileName);

            // copy the current file to the backup file
            copyFile(origFile, backupFile);

            // generate the change log file;
            // generateChangeLog(backupFile);
        } catch (IOException ex) {
            log.error("OdfPackageBackup " + ex.getMessage());
        } finally {
            return backupFile;
        }
    }

    public static void main(String[] args) {
        try {
            BungeniOdfFileCopy bkp = new BungeniOdfFileCopy(OdfPackage.loadPackage("/home/undesa/test.odt"));

            bkp.copyTo("_plain", true);

            // bkp.generateBackup();
        } catch (Exception ex) {}
    }
}
