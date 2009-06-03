package org.bungeni.editor.rulesimpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
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

        private File getLogFolderHandle() {
            File fLogs = null;
        try {
            URL urlLogFolder = new URL(this.logFileFolder);
            fLogs = new File(urlLogFolder.getPath());
        } catch (MalformedURLException ex) {
            log.error("getLogFolderHandle : ", ex);
        } finally {
            return fLogs;
        }

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

        public String getLogFolder() {
            return this.logFileFolder;
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


        public File[] browseErrorLogFiles() {
            File[] fLogs = null;
            try {
                    File fLogsFolder = this.getLogFolderHandle();

                    if (fLogsFolder.isDirectory()) {
                        fLogs = fLogsFolder.listFiles(new FilenameFilter() {
                                public boolean accept(File dir, String name) {
                                    boolean bState = false;
                                    if (name.endsWith(".xml")) {
                                        int nIndex = name.indexOf(".xml");
                                        String fileStartsWith = name.substring(0, nIndex);
                                        try {
                                            timestampFileNameDateFormat().parse(fileStartsWith);
                                            bState = true;
                                        } catch (ParseException ex) {
                                            bState = false;
                                        }
                                    }
                                    return bState;
                                }
                        });
                }
            } catch (Exception ex) {
                log.error("browseErrorLogFiles : ", ex);
            } finally {
                return fLogs;
            }
            
        }

        public DateFormat timestampFileNameDateFormat() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            return dateFormat;
        }

        private String getTimestamp(){
            Date date = new Date();
            this.timeStamp = date;
            return timestampFileNameDateFormat().format(date);
        }

        public Date getSerializedTimestamp(){
            return timeStamp;
        }

        public static void main(String[] args) {
            StructuralRulesConfig.LOG_PATH_PREFIX = "/home/undesa/test/logs/";
            StructuralErrorSerialize ss = new StructuralErrorSerialize("file:/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/workspace/files/ke/debaterecord/2009-5-26/eng/ke_debaterecord_2009-5-26_eng.odt");
            //ss.browseLogs();
     
     
        }
}
