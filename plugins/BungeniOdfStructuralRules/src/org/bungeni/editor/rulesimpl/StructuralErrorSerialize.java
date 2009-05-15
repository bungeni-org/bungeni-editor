package org.bungeni.editor.rulesimpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Error logs are stored hierarchically :
 *  file-name-as-folder-name/<timestamp>.xml
 * @author Ashok Hariharan
 */
public class StructuralErrorSerialize {
        String logFileName = "";
        String pathToLogFile = "";
        String logFileFolder  = "";
        String sourceDocument =  "";
        Date timeStamp = null;
        File fLogFile = null;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StructuralErrorSerialize.class.getName());

      public  StructuralErrorSerialize(String sourceDocUrl){
            this.sourceDocument = sourceDocUrl;
            //returns path to the log folder
            String pathToLogFolder = StructuralRulesConfig.getLogPath();
            //get the source filename
            String sourceFileName = getSourceFileName(sourceDocUrl);
            //get the path to the log folder for the file - if the
            //log folder doesnt exist it gets created
            this.logFileFolder  = getLogFolder(sourceFileName, pathToLogFolder);
            //get the log file name

        }

        /**
         * Creates a new log file
         * @return
         */
        public File newLogFile() {
         File flog = null;
                try {
                    this.logFileName = getTimestamp() + ".xml";
                    this.pathToLogFile = logFileFolder + logFileName;
                    URL logURL = new URL(this.pathToLogFile);
                    flog = new File(logURL.toURI());
                    if (!flog.exists()) {
                        flog.createNewFile();
                    }
                } catch (Exception ex) {
                   log.error("newLogFile :"+ ex.getMessage());
                } finally{
                    this.fLogFile = flog;
                    return flog;
                }
        }

        /**
         * Writes structural errors to a log file
         * @param errors
         * @return
         */
        public boolean writeErrorsToLog(ArrayList<StructuralError> errors) {
            boolean bState = false;
            try {
                //make new log file if it doesnt exit
                if (fLogFile == null) {
                    this.newLogFile();
                }
                //build the error log object
                StructuralErrorLog sel  = new StructuralErrorLog();
                sel.sourceFile = this.sourceDocument;
                sel.timeStamp = this.timeStamp;
                sel.structuralErrors = errors;
                //write the object to xml
                XStream xst = new XStream();
                FileWriter fwLogFile = new FileWriter(fLogFile);
                xst.toXML(sel, fwLogFile);
                bState = true;
            } catch (java.io.IOException ex) {
                    log.error("writeErrorsToLog :" + ex.getMessage());
            } catch (XStreamException ex) {
                    log.error("writeErrorsToLog :" + ex.getMessage());
            } finally {
                return bState;
            }
        }

        private String getLogFolder (String sourceFileName, String pathToLogFolder) {
            String fullPathToLogFolder = "";
            try {
                int nIndex = sourceFileName.lastIndexOf(".");
                String logFolderName = "";
                if (nIndex != -1) {
                    logFolderName = sourceFileName.substring(0, nIndex);
                }
                File fLogFolder = new File(pathToLogFolder + logFolderName);
                if (!fLogFolder.exists()) {
                    fLogFolder.mkdirs();
                }
                fullPathToLogFolder = fLogFolder.toURI().toURL().toString();
             } catch (MalformedURLException ex) {
                log.error("getLogFolder :" + ex.getMessage());
             } finally {
                 return fullPathToLogFolder;
             }
        }

        private String getSourceFileName(String sourceDocUrl) {
            String sourceFileName = "";
            try {
                URL sourceURL = new URL(sourceDocUrl);
                File fsource = new File(sourceURL.toURI());
                sourceFileName =  fsource.getName();
            } catch (URISyntaxException ex) {
                log.error("getSourceFileName : " + ex.getMessage());
            } catch (MalformedURLException ex) {
                log.error("getSourceFileName : " + ex.getMessage());
            } finally {
                return sourceFileName;
            }

        }


        private String getTimestamp(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date date = new Date();
            this.timeStamp = date;
            return dateFormat.format(date);
        }

        public Date getSerializedTimestamp(){
            return timeStamp;
        }

}
