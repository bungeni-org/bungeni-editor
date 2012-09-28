
package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import java.util.Collection;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.bungeni.extutils.Grep.GrepMatch;

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

  /**
   * Returns RDF3986 compliant File urls.
   * Java by default returns RFC2396 file urls without an authority part.
   * OpenOffice switched to using RFC3986 URLs since OOo 3.3
   * @param f The File handle
   * @return The URL as a String
   * @throws MalformedURLException
   */
  public static String getFileAuthorityURL(File f)
          throws MalformedURLException {
          java.net.URL fileURL = f.toURI().toURL();
          String fileURLAuthority = fileURL.getAuthority();
          //Check if the authority part is null
          if (fileURLAuthority == null) {
              //This is a file:/ type URL so no authority
              String filePath = fileURL.getFile();
              return "file://" + filePath;
          }
          //if the authority was not null -- return the URL as is in its external
          //form
          return fileURL.toExternalForm();
  }

  /**
   * Returns a file name from a path, boolean flag allows returning with or
   * without extension
   * @param pathToFile
   * @param withExt
   * @return
   */
  public static String getFileNameFromPath(String pathToFile, boolean withExt) {
      File f = new File(pathToFile);
      String fileName = f.getName();
      if (withExt == true) {
          return fileName;
      } else {
          int index = fileName.lastIndexOf(".");
          if (index > 0 ) {
              if (index <= fileName.length() - 2) {
                  return fileName.substring(0, index);
              }
              return fileName;
          }
          return fileName;
      }
  }


  public static String getURLPath(String path) throws MalformedURLException{
      File f = new File(path);
      String sURLpath = f.toURI().toURL().toExternalForm();
      //toExternalForm adds a trailing slash for folders, so we remov it
      if (path.endsWith(File.separator))
        return sURLpath;
      else
        return sURLpath.substring(0, sURLpath.length() - 1);
  }


  public static URL[] findInFiles(String inFolder, String fileWildCard, String searchFor ) {
        List<URL> urls = new ArrayList<URL>(0);
        Collection<File> files = FileUtils.listFiles(new File(inFolder),
                new WildcardFileFilter(fileWildCard),
                TrueFileFilter.INSTANCE);
        for (File file : files) {
            try {
                Grep grep = new Grep(searchFor, file.getAbsolutePath());
                GrepMatch gpMatch = grep.search();
                if (gpMatch.matchesInFile.size() > 0) {
                    urls.add(file.toURI().toURL());
                }
             } catch (IOException ex) {
                 log.error("error while accessing file for search",  ex);
            } 
        }
        return urls.toArray(new URL[urls.size()]);
    }

}


