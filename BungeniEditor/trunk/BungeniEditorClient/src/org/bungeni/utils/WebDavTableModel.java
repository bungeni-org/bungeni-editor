/*
 * WebDavTableModel.java
 *
 * Created on June 13, 2007, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.httpclient.HttpException;
import org.apache.webdav.lib.WebdavResource;
import org.apache.log4j.Logger;
import org.apache.webdav.lib.WebdavResources;
/**
 * Table Model Class for WebDav JTable
 * @author Administrator
 */
public class WebDavTableModel extends AbstractTableModel {
    private WebDavStore dav;
    private Vector dir;
    private static org.apache.log4j.Logger log = Logger.getLogger(WebDavTableModel.class.getName());
    private String homeFolder;
    private String currentPath;
    private JTable tblParent;
    private JProgressBar progresBar;
    /**
     * Column headings for Table Columns
     */
    protected String[] columnNames = new String[] {
    "name", "size", "last modified" , "type"
  };
    /**
     * Name column
     */
    public static int _COL1_NAME_ = 4;
    /**
     * Size of file
     */
    public static int _COL2_SIZE_ = 1;
    /**
     * Modified Date
     */
    public static int _COL3_DATE_ = 3;
    /**
     * Type of file : Collection or Other Mime types
     */
    public static int _COL4_TYPE_ = 2;
    
/*    
     INFO [org.bungeni.test.WebDavTest] array size = 5
     INFO [org.bungeni.test.WebDavTest] s[0] = debates
     INFO [org.bungeni.test.WebDavTest] s[1] = 0
     INFO [org.bungeni.test.WebDavTest] s[2] = COLLECTION
     INFO [org.bungeni.test.WebDavTest] s[3] = Jun 10, 2007 5:56:39 PM
     INFO [org.bungeni.test.WebDavTest] s[4] = tempo
    inside while loop
     INFO [org.bungeni.test.WebDavTest] array size = 5
     INFO [org.bungeni.test.WebDavTest] s[0] = self potrait
     INFO [org.bungeni.test.WebDavTest] s[1] = 62341
     INFO [org.bungeni.test.WebDavTest] s[2] = image/jpeg
     INFO [org.bungeni.test.WebDavTest] s[3] = Jun 10, 2007 5:54:52 PM
     INFO [org.bungeni.test.WebDavTest] s[4] = self potrait
*/
    /**
     * Datatypes of columns
     */
    protected Class[] columnClasses = new Class[] { 
    String.class, Long.class, Date.class, String.class
  };
  
    /**
     * Default Constructor
     */
    public WebDavTableModel(){
        
    }
    /**
     * Creates a new instance of WebDavTableModel, takes WebDav handle as paramaeter
     */
    public WebDavTableModel(WebDavStore dav, String serverHome, JTable tblParentTable, JProgressBar progresBar)  {
    
        log.debug("in WebDavTableModel constructor");
        
        this.dav = dav;
        this.homeFolder = serverHome;
        this.currentPath = serverHome;
        this.tblParent = tblParentTable;
        this.progresBar = progresBar; 
        this.progresBar.setVisible(false);
        
        log.debug("currentFolder = " + currentPath);
        
        this.dir = new Vector();
        try {
            dir = dav.listBasic();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       // davUpdate(currentPath);
    }

    /**
     * Number of rows in Table
     */
    public int getRowCount() {
        return dir.size();
        
    }
    
    public String getParentPath(String sPath) throws Exception{
       int iIndex = 0;
       int homeIndex = 0;
       String sReturnPath = "";
       String homeDir = this.homeFolder;
       //if string passed in is blank use the current path to determine its parent path.
       if (sPath.equals("")) sPath = this.currentPath;
       log.debug("In getParentPath : homeDir = "+ homeDir + ", currentPath = "+ sPath);
       if (sPath.equals(homeDir+"/")|| sPath.equals(homeDir) ){
                sReturnPath = sPath;
                throw new Exception("root-reached");
       }
       iIndex = sPath.substring(homeIndex, sPath.length()-2).lastIndexOf('/');
	if (iIndex == 0 || iIndex == -1)
		sReturnPath = "/";
	else
        	sReturnPath = sPath.substring(0, iIndex);
        return sReturnPath;
    }
    
    /**
     * Debugging function to display Dav Brains.
     */
    public void brains(){
        log.debug("******starting brains output******");
        if (dav.exists())
            log.debug("dav exists with size =" +  dav.getChildResources().list().length);
        else
            log.debug("dav does not exist ");
        log.debug("******ending brains output******");
        
  }

    /**
     * Number of columns returned by Dav...
     */
    public int getColumnCount() {
        return 4;
    }
  // Information about each column
    /**
     * Get the name of a particular column
     */
  public String getColumnName(int col) { 
      return columnNames[col]; 
  }
    /**
     * Get the Data type of a particular column
     */
  public Class getColumnClass(int col) { 
      return columnClasses[col]; 
  }
  
    /**
     * Set the Dav Path...
     */
   public void setPathRelative(String subFolder ){
       String slas = (subFolder.startsWith("/") || currentPath.endsWith("/") ? "" : "/");
       log.debug("setPathRelative : subFolder = "+ subFolder + ", currentPath = "+ currentPath);
       String newPath = this.currentPath + slas +  subFolder;
       log.debug("new path in setPathRelative is : "+ newPath);
       //change the path in the JTable model if the clicked element is a colleciton
       setCurrentPath(newPath);
       davUpdate(newPath);
      
   } 
   
   public void setPath(String folderPath ){
        log.debug("new path in setPath is : "+ folderPath);
       //change the path in the JTable model if the clicked element is a colleciton
       setCurrentPath(folderPath);
       davUpdate(folderPath);
      
   } 
   private void setCurrentPath(String newPath){
       this.currentPath = (newPath.endsWith("/") ? newPath : newPath + "/");
   }

   public String getCurrentPath(){
       return this.currentPath;
   }
   
   private void updateDav(Vector v, String newPath){
       this.dir = v;
       setCurrentPath(newPath);
       log.debug("updateDav newpath = " + this.currentPath);
   }
    /**
     * Get the value at a particular row & column in a JTable
     */
     public Object getValueAt(int rowIndex, int columnIndex) {
        String[] dirRow = (String[])dir.elementAt(rowIndex);
        //log.debug("getValueAt = row "+rowIndex+ " , column = "+ columnIndex + " value = "+ dirRow[columnIndex] );
            switch(columnIndex) {
                case 0: 
                        //log.debug("column 1" + dirRow[_COL1_NAME_] );
                        return dirRow[_COL1_NAME_];
                case 1: 
                        //log.debug("column 2" + dirRow[_COL2_SIZE_] );
                        return new Long(dirRow[_COL2_SIZE_]);
                case 2: 
                        //log.debug("column 3" + dirRow[_COL3_DATE_]);
                        String modifDate = dirRow[_COL3_DATE_];
                        DateFormat df = DateFormat.getDateInstance();
                            try {
                             return df.parse(modifDate);
                            } 
                            catch (ParseException ex) {
                                    ex.printStackTrace();
                            }
                case 3: 
                    if  (dirRow[_COL4_TYPE_].equals(org.apache.webdav.lib.properties.ResourceTypeProperty.TAG_COLLECTION.toUpperCase()))
                                return "folder";
                        else
                            return dirRow[_COL4_TYPE_];
                        
                    /*
                    case 3: return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
                    case 4: return f.canRead() ? Boolean.TRUE : Boolean.FALSE;
                    case 5: return f.canWrite() ? Boolean.TRUE : Boolean.FALSE;
                     */ 
                 default: return null;
        
            }
    
    }

    private class davUpdater implements Runnable {
        
        private Thread dav_thread;
        private progressMonitor progress;
        private String davPath;
        private String logPrefix="[davUpdater]";
         
        public davUpdater(String newPath, progressMonitor pm){
            //this thread will invoke the run() class runner
            this.dav_thread = new Thread(this);
            this.davPath = newPath;
            this.progress = pm;
            debug("in constructor");
        }
        
        private void debug(String msg){
            log.debug(logPrefix + msg);
        }
        public void start(){
            dav_thread.start();
            debug("in start()");
        }
        
	public void join() {
		try {
                    dav_thread.join();
		} 
                catch (InterruptedException e) {
		e.printStackTrace();
            	}
	}
        public void run() {
                    debug("in class run() function");
                    progress.start();
                    tblParent.setVisible(false);
                    WebDavStore run_dav = new WebDavStore();
                    run_dav.setConnectionUrl(dav.getConnectionUrl());
                    run_dav.setConnectionPort(dav.getConnectionPort());
                    run_dav.setConnectionUsername(dav.getConnectionUsername());
                    run_dav.setConnectionPassword(dav.getConnectionPassword());
                    run_dav.setConnectionBaseDirectory(davPath);
                    run_dav.connect("");
                    if (run_dav.isCollection() ) {
                        debug("if new path is a collection : YES");
                        try {
                            //update the vector object of the parent classs
                            dir = run_dav.listBasic();
                            updateDav (run_dav.listBasic(), davPath);
                           
                         } catch (IOException ex) {
                            debug(ex.getLocalizedMessage());
                        }
                            finally {
                                tblParent.setVisible(true);
                                progress.stop();
                            }
                    }
                    
                }
       
    }
    
    private class progressMonitor implements Runnable  {
        private Thread progressThread;
        private boolean endTimeout = false;
        private int timeoutCoeff = 30;
        
        progressMonitor() {
            
            progressThread = new Thread(this);
        }
        
        public void start (){
            progressThread.start();
        }
        
         public void join (){
            progressThread.start();
        }
         
        public void stop(){
            this.endTimeout = true;
        } 
        
        
        public void run() {
            progresBar.setVisible(true);
            
            for (int i = 0; i < 100; i += (100/timeoutCoeff)) {
                try {
                    for (int j = 0; j < 10; j++) {
                            if (endTimeout == true) {
                                progresBar.setVisible(false);
                                return;
                            }

                            progressThread.sleep(100);			
                        }
                } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                progresBar.setValue(i);
            }    
        
        
        }
        
        
    }
        
    private void davUpdate(String newPath){
        log.debug("invoking davUpdater");
        progressMonitor pm = new progressMonitor();
        davUpdater davUpd = new davUpdater(newPath, pm);
        davUpd.start();
    }
   
}
