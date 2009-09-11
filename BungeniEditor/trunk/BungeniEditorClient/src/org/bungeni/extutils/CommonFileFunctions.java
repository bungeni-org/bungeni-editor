
package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import java.io.IOException;
import org.bungeni.db.DefaultInstanceFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonFileFunctions {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonFileFunctions.class.getName());

    public CommonFileFunctions() {}

    public static String getLocalDirName() {
        String localDirName;

        // Use that name to get a URL to the directory we are
        java.net.URL myURL = CommonFileFunctions.class.getClass().getResource(getClassName());    // Open a URL to the our .class file

        localDirName = myURL.getPath();                          // Strip path to URL object
        localDirName = myURL.getPath().replaceAll("%20", "");    // change %20 chars to spaces

        // Get the current execution directory
        localDirName = localDirName.substring(0, localDirName.lastIndexOf("/"));    // clean off the file

        return localDirName;
    }

    public static String getClassName() {
        String thisClassName = "";

        // Build a string with executing class's name
        thisClassName = CommonFileFunctions.class.getName();
        thisClassName = thisClassName.substring(thisClassName.lastIndexOf(".") + 1, thisClassName.length());
        thisClassName += ".class";

        return thisClassName;
    }

    public static File getFileFromChooser(String basePath, FileFilter filter, int fileSelectionMode, JFrame pFrame) {
        File returnFile = null;

        try {
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);

            final JFileChooser fc = new JFileChooser(basePath);
            fc.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
            fc.setFileFilter(filter);
            fc.setFileSelectionMode(fileSelectionMode);

            int nReturnVal = fc.showOpenDialog(pFrame);

            if (nReturnVal == JFileChooser.APPROVE_OPTION) {
                returnFile = fc.getSelectedFile();

                return returnFile;
            }
        } catch (Exception ex) {
            log.error("getFileFromChooser :" + ex.getMessage());
        } finally {
            return returnFile;
        }
    }

    public static String convertRelativePathToFullPath(String relativePath) {
        String fullPath = "";

        // logoPath = BungeniEditorProperties.getEditorProperty("logoPath");
        // log.debug("logo path = " + logoPath);
        String strPath = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH();

        fullPath = strPath + File.separator + relativePath.replace('/', File.separatorChar);

        return fullPath;
    }

    public static String getPathFromParams(ArrayList<String> params, String fileNameExt) {
        String fullPath = "";
        String fileName = "";

        for (int i = 0; i < params.size(); i++) {
            fullPath = fullPath + params.get(i) + File.separator;

            if (i == params.size() - 1) {
                fileName = fileName + params.get(i);
            } else {
                fileName = fileName + params.get(i) + "_";
            }
        }

        return fullPath + File.separator + fileName;
    }

    public static File convertUrlToFile(String sUrl) {
        File f   = null;
        URL  url = null;

        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            log.error("convertUrlToFile: " + ex.getMessage());

            return null;
        }

        try {
            f = new File(url.toURI());
        } catch (URISyntaxException e) {
            f = new File(url.getPath());
        } finally {
            return f;
        }
    }



    /**
     * Helper function to load a file as a string
     * @param pathToFile - path to file
     * @return
     */
    public static String getFileAsString(String pathToFile) {
        String strResult = null;

        try {
            FileReader fr;
            fr = new FileReader(pathToFile);
            strResult = fileReaderAsString(fr);

        } catch (FileNotFoundException ex) {
            log.error("getToolbarXMLFile: toolbar.xml not found in path : " + ex.getMessage());
        } finally {
            return strResult;
        }
    }


    /**
     * Converts FileReader to a string.
     * @param fr
     * @return
     */
    private static String fileReaderAsString(FileReader fr) {
            String strResult = "";

        try {
            BufferedReader xmlReader = new BufferedReader(fr);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = xmlReader.readLine()) != null) {
                result.append(line);
            }
            strResult = result.toString();
        } catch (IOException ex) {
            log.error("fileReaderAsString : " + ex.getMessage());
        } finally {
            return strResult;
        }
    }

   /**
     * Helper function to load a file as a string
     * @param pathToFile - path to file
     * @return
     */
    public static String getFileAsString(File handleToFile) {
        String strResult = null;

        try {
            FileReader fr;
            fr = new FileReader(handleToFile);
            strResult = fileReaderAsString(fr);
        } catch (FileNotFoundException ex) {
            log.error("getToolbarXMLFile: toolbar.xml not found in path : " + ex.getMessage());
        } finally {
            return strResult;
        }
    }

    public static BufferedReader getFileasBufferedReader (File fFile) {
        FileReader freader = null;
        BufferedReader bufReader = null;
        try {
            freader = new FileReader(fFile);
            bufReader = new BufferedReader(freader);
        } catch (FileNotFoundException ex) {
           log.error("getFileasBufferedReader", ex);
        }
        return bufReader;
    }

  

}


