/*
 * BungeniCatalogCommand.java
 *
 * Created on December 26, 2007, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands.chains;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import org.bungeni.db.DefaultInstanceFactory;

/**
 *
 * @author Administrator
 */
public class BungeniCatalogCommand {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniCatalogCommand.class.getName());

     private String formName;
     private String catalogSource;
     private URL catalogSourceURL;
     private String formMode ;
     private String commandCatalog;
     private String commandChain;
             
    /** Creates a new instance of BungeniCatalogCommand */
    public BungeniCatalogCommand() {
    }
    
    public BungeniCatalogCommand(HashMap<String,String> cmdCatalog) {

        setFormName(cmdCatalog.get("FORM_NAME"));
        setCatalogSource(cmdCatalog.get("CATALOG_SOURCE"));
        setFormMode(cmdCatalog.get("FORM_MODE"));
        setCommandCatalog(cmdCatalog.get("COMMAND_CATALOG"));
        setCommandChain(cmdCatalog.get("COMMAND_CHAIN"));

    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getCatalogSource() {
        return catalogSource;
    }

    public URL getCatalogSourceURL() {
        return catalogSourceURL;
    }
    
    
    public void setCatalogSource(String cSource) {
        //convert the relative path to absolute path
        if (cSource.startsWith("/"))
            this.catalogSource = cSource;
        else {
            String normalizedPath = cSource.replace('/', File.separatorChar);
            this.catalogSource =  DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + normalizedPath ;                    
            File newFile = new File(this.catalogSource);
            try {
                this.catalogSourceURL = newFile.toURL();
            } catch (java.net.MalformedURLException urlEx) {
                log.error("setCatalogSource = " + urlEx.getMessage());
            }    
            log.info("setCatalogSource: was set = " + catalogSource);
            log.info("setCatalogSourceURL: was set = " + catalogSourceURL.toString());
        }
    }

    public org.bungeni.editor.selectors.SelectorDialogModes getFormMode() {
        return Enum.valueOf(org.bungeni.editor.selectors.SelectorDialogModes.class, formMode);
    }

    public void setFormMode(String formMode) {
        this.formMode = formMode;
    }

    public String getCommandCatalog() {
        return commandCatalog;
    }

    public void setCommandCatalog(String commandCatalog) {
        this.commandCatalog = commandCatalog;
    }

    public String getCommandChain() {
        return commandChain;
    }

    public void setCommandChain(String commandChain) {
        this.commandChain = commandChain;
    }
    
}
