/*
 * FragmentsFactory.java
 *
 * Created on August 29, 2007, 12:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.fragments;

import java.io.File;
import org.bungeni.extutils.CommonFileFunctions;

/**
 *
 * @author Administrator
 */
public class FragmentsFactory {
    private static String FRAGMENTS_FOLDER = "fragments" ;
    /** Creates a new instance of FragmentsFactory */
    public FragmentsFactory() {
    }
    
    public static String getFragment (String fragName) {
        String strPath = CommonFileFunctions.getAbsoluteInstallDir();
        strPath = strPath + File.separator + "settings" + File.separator + FRAGMENTS_FOLDER + File.separator + fragName + ".odt";
        return strPath;
    }
}
