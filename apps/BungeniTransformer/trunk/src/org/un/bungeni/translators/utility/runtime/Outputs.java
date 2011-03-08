package org.un.bungeni.translators.utility.runtime;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;

/**
 *
 * @author Ashok Hariharan
 */
public class Outputs {

    private String runtimeDirectory = "";
     /* This is the logger */
    private static org.apache.log4j.Logger log               =
        org.apache.log4j.Logger.getLogger(Outputs.class.getName());

    private static Outputs instance = null;

    public static Outputs getInstance(){
        if (instance == null) {
            try {
                instance = new Outputs();
            } catch (IOException ex) {
                Logger.getLogger(Outputs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    private Outputs() throws IOException{
        File fruntimeDir = new File(GlobalConfigurations.getApplicationPathPrefix()+ "../runtime");
        fruntimeDir.mkdirs();
        this.runtimeDirectory = fruntimeDir.getCanonicalPath();
    }

    public String getRuntimeDir(){
        return this.runtimeDirectory;
    }

    public File File(String aFile) {
        return new File(this.runtimeDirectory + File.separator + aFile);
    }



}
