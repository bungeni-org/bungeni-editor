/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.io.File;
import org.bungeni.db.DefaultInstanceFactory;

/**
 *
 * @author undesa
 */
public class CommonEditorFunctions {
    public static String getSettingsFolder(){
        return DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "settings";
    }
}
