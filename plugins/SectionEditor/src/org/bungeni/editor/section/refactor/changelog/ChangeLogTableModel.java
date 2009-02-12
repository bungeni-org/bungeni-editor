/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.changelog;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.bungeni.editor.section.refactor.xml.OdfPackageBackup;

/**
 *
 * @author undesa
 */
public class ChangeLogTableModel  extends DefaultTableModel {
    private String masterFileName;
    private String masterFileDir ;
    private ArrayList<ChangeLog> changeLogs = new ArrayList<ChangeLog>(0);
    private final String[] COLUMNS = {"Source Section", "Action" , "Target Section", "On" };
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeLogTableModel.class.getName());

    private static final String BACKUP_FOLDER = OdfPackageBackup.BACKUP_FILE_FOLDER;
    /**
     * The Master file name is passed and the table model is generated from the corresponding backup folder
     * @param fileNameMaster
     */
    public ChangeLogTableModel(String fileMaster) {
        File fMaster = new File(fileMaster);
        String fileName = fMaster.getName();
        this.masterFileName = fileName;
        String filePath = fMaster.getParent();
        this.masterFileDir = filePath;
        File fbackupDir = new File(filePath + File.separator + BACKUP_FOLDER);
        ChangeLogFileFilter clf = new ChangeLogFileFilter();
        if (fbackupDir.isDirectory()) {
           File[] fileList= fbackupDir.listFiles(clf);  
           Arrays.sort(fileList, new Comparator<File>() {

                public int compare(File o1, File o2) {
                    return (int)o1.lastModified()  - (int)o2.lastModified();
                }
    
           });
 
          
  
           for (int i=0; i < fileList.length ; i++ ) {
               ChangeLog cl = ChangeLog.load(fileList[i].getPath());
               if (cl != null) {
                   changeLogs.add(cl);
               }
           }
        }
     
    }

    
class ChangeLogFileFilter implements FilenameFilter {
   private String matchThis = "";
    
    public ChangeLogFileFilter(){
        matchThis = masterFileName.replaceAll(".odt", "");
    }
    public boolean accept(File dir, String name) {
        if (name.contains(matchThis)) {
            if (name.endsWith(".xml")){
                return true;
            }
        }
        return false;
    }
}



    @Override
    public int getColumnCount(){
        return COLUMNS.length;
    }
    
    @Override
    public String getColumnName(int columnNo){
        return COLUMNS[columnNo];
    }
    
    @Override
    public Object getValueAt(int rowNo, int colNo){
        ChangeLog cl = this.changeLogs.get(rowNo);
        switch (colNo) {
            case 0 :
                return cl.getChangeSource();
            case 1 :
                return cl.getChangeType();
            case 2 : 
                return cl.getChangeTarget();
            case 3 :
                String fullDateTime =  cl.getChangeDateTime();
                String parsedDate = parseDate(fullDateTime);
                return parsedDate;
            default:
                return cl.getChangeSource();
        }
    }
    
    @Override
    public int getRowCount() {
        if (this.changeLogs == null) 
            return 0;
        else
            return changeLogs.size();
    }

    @Override
     public Class getColumnClass(int col) { 
      return String.class;
  }

    private String parseDate(String stringDate) {
        String outputDate = stringDate;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(OdfPackageBackup.BACKUP_FILE_NAME_DATE_FORMAT);
            Date outDate = sf.parse(stringDate);
            outputDate =  outDate.toString();
        } catch (ParseException ex) {
           log.error("parseDate  : "+ ex.getMessage());
        } finally {
            return outputDate;
        }
    }

    public ChangeLog getModelObjectAt(int nIndex) {
        if (nIndex < this.changeLogs.size()){
            ChangeLog clObj = changeLogs.get(nIndex);
            return clObj;
        }
        return null;
    }

}
