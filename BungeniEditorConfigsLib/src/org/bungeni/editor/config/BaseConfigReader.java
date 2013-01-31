/*
 *  Copyright (C) 2012 Africa i-Parlaiments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.extutils.CommonFileFunctions;

import org.jdom.JDOMException;

/**
 *
 * @author Ashok
 */
public class BaseConfigReader {

    private static final Logger log = Logger.getLogger(BaseConfigReader.class.getName());
    public static final String BASE_SETTINGS_FOLDER = "settings" ;
    private static  String CONFIGS_FOLDER =  getConfigsFolder() ;
    public static final String WORKSPACE_PROPS_FILE = "workspace.properties" ;
    public static final String SYSTEM_PROPS_FILE = "system.properties";
    private static String PLUGGABLE_CONFIGS_FOLDER = null;
    private static String WORKSPACE_FOLDER = null;
    private static String MAIN_PANEL_IMPL = null;

    private static String getSettingsFolder(){
        return CommonFileFunctions.getAbsoluteInstallDir() + File.separator + BASE_SETTINGS_FOLDER ;
    }

    public static String getWorkspaceFolder() throws IOException{
      if (null == WORKSPACE_FOLDER) {
          Properties pini = new Properties();
          String wsProps = getSettingsFolder() + File.separator + WORKSPACE_PROPS_FILE ;
          pini.load(new FileInputStream(wsProps));
          String workspaceFolder = pini.getProperty("workspace", "./workspace").trim();
          WORKSPACE_FOLDER = workspaceFolder;
          File f = new File(WORKSPACE_FOLDER);
          if (f.exists()) {
              if (!f.isDirectory()) {
                  throw new java.io.IOException("Workspace path already exists and is a file !");
              }
          } else {
              f.mkdirs();
          }
      }
      return WORKSPACE_FOLDER;
    }

      public static String getMainPanelImpl() throws IOException{
      if (null == MAIN_PANEL_IMPL) {
          Properties pini = new Properties();
          String wsProps = getSettingsFolder() + File.separator + SYSTEM_PROPS_FILE ;
          pini.load(new FileInputStream(wsProps));
          String mainPanelImpl = pini.getProperty("main-panel-impl").trim();
          MAIN_PANEL_IMPL = mainPanelImpl;
      }
      return MAIN_PANEL_IMPL;
    }

   public static void refreshConfigsFolder(){
       BaseConfigReader.CONFIGS_FOLDER = getConfigsFolder();
   }
      
    private static String getConfigsFolder(){
            try {
              PluggableConfig cfg = PluggableConfigReader.getInstance().getDefaultConfig();
              String folderBase = cfg.folderBase;
              if (null == folderBase) {
                  String urlBase = cfg.url;
                  //download config into settings configs folder and return that path
                  PLUGGABLE_CONFIGS_FOLDER = BASE_SETTINGS_FOLDER + File.separator + "configs"  ;
              } else {
                  //check if its a URL file path
                  if (folderBase.startsWith("file:")){
                      URL fileURL = new URL(folderBase);
                      File f ;
                      try {
                        f = new File(fileURL.toURI());
                      } catch(URISyntaxException ex){
                        f = new File(fileURL.getPath());
                      }
                      PLUGGABLE_CONFIGS_FOLDER = f.getAbsolutePath();
                  } else {
                      //otherwise assume its a regular path
                      File f = new File(folderBase);
                      PLUGGABLE_CONFIGS_FOLDER = f.getAbsolutePath();
                  }
              }
            } catch (MalformedURLException ex) {
                log.error("Error initialization configuration", ex);
            } catch (JDOMException ex) {
              log.error("Error getting configs folder", ex);
            }
        return PLUGGABLE_CONFIGS_FOLDER;
    }

    /**
     * @return the CONFIGS_FOLDER
     */
    public static String configsFolder() {
        return CONFIGS_FOLDER;
    }



}
