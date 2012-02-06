package org.bungeni.translators.utility.runtime;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.globalconfigurations.GlobalConfigurations;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to create a runtime directory - intermediate outputs
 * and cached files are stored here
 * @author Ashok Hariharan
 */
public class Outputs {
    private static Outputs instance = null;

    /* This is the logger */
    private static org.apache.log4j.Logger log              =
        org.apache.log4j.Logger.getLogger(Outputs.class.getName());
    private String                         runtimeDirectory = "";

    private Outputs() throws IOException {
        File fruntimeDir = null;

        if (GlobalConfigurations.getApplicationPathPrefix().endsWith(File.separator)) {
            fruntimeDir = new File(GlobalConfigurations.getApplicationPathPrefix() + "runtime");
        } else {
            fruntimeDir = new File(GlobalConfigurations.getApplicationPathPrefix() + File.separator + "runtime");
        }

        fruntimeDir.mkdirs();
        this.runtimeDirectory = fruntimeDir.getCanonicalPath();
    }

    public static Outputs getInstance() {
        if (instance == null) {
            try {
                instance = new Outputs();
            } catch (IOException ex) {
                Logger.getLogger(Outputs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    public String getRuntimeDir() {
        return this.runtimeDirectory;
    }

    public File File(String aFile) {
        return new File(this.runtimeDirectory + File.separator + aFile);
    }
}
