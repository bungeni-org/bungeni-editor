/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.changelog;

import java.io.File;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author undesa
 */
public class ChangeLogTableModel  extends DefaultTableModel {
    
    private ArrayList<ChangeLog> changeLogs = new ArrayList<ChangeLog>(0);
    private static final String BACKUP_FOLDER = ".backup";
    /**
     * The Master file name is passed and the table model is generated from the corresponding backup folder
     * @param fileNameMaster
     */
    public ChangeLogTableModel(String fileMaster) {
        File fMaster = new File(fileMaster);
        String fileName = fMaster.getName();
        String filePath = fMaster.getParent();
        File fbackupDir = new File(filePath + File.separator + BACKUP_FOLDER);
        if (fbackupDir.isDirectory()) {
            
        }
     
    }
}
