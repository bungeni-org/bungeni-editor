/*
 * toolbarButtonCommandFactory.java
 *
 * Created on July 24, 2007, 4:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels;

import java.util.HashMap;
import org.apache.log4j.varia.NullAppender;
import org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler;

/**
 *
 * @author Administrator
 */
public class toolbarButtonCommandFactory extends Object {
    private static java.util.HashMap commandsMap=null;
    private static final ItoolbarButtonEvent DEFAULT_EVENT_HANDLER = new org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler();
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(toolbarButtonCommandFactory.class.getName());
 
    /** Creates a new instance of toolbarButtonCommandFactory */
    public toolbarButtonCommandFactory() {
    }
    
    public static ItoolbarButtonEvent getButtonEventHandler(String cmdName){
        log.debug("in factory class command = "+cmdName);
        ItoolbarButtonEvent iEvent = null;
        try {
            String className = "", namingConvetion = "", numberingType = "";
            className = getCommandClass(cmdName);
             if( className.length() > 0  )
            {
                log.debug("in factory: getButtonEventHandler :" + className );    
                Class eventHandlerClass;
                    eventHandlerClass = Class.forName(className);
                iEvent = (ItoolbarButtonEvent) eventHandlerClass.newInstance();
            }    
            else
                log.debug("in factory, class name was null ");
         } 
        catch (IllegalAccessException ex) {
                log.error("getButtonEventHandler :" + ex.getMessage());
            } catch (InstantiationException ex) {
                log.error("getButtonEventHandler :" + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                log.error("getButtonEventHandler :" + ex.getMessage());
            }
         finally {   
            if( iEvent == null ) {
                log.debug("in factory, iEvent is null");
                return DEFAULT_EVENT_HANDLER;
            }
            else
                 return iEvent;
         }
    }
    
    public static String getCommandClass(String command) {
        HashMap mp;
        String commandClass = "";
        try {
             mp = getClassName(command);
             commandClass = (String) mp.get("class");
         } catch (Exception ex) {
            log.error("error in getCommandClass " + ex.getLocalizedMessage(), ex);
        } finally {
            return commandClass;
        }
    }
    
    public static String getCommandNamingConvention(String command){
        HashMap mp;
        String commandClass = "";
        try {
             mp = getClassName(command);
             commandClass = (String) mp.get("section");
         } catch (Exception ex) {
            log.error("error in getCommandNamingConvention " + ex.getLocalizedMessage(), ex);
        } finally {
            return commandClass;
        }
    }
    
    public static String getCommandNumberingType (String command) {
        HashMap mp;
        String commandClass = "";
        try {
             mp = getClassName(command);
             commandClass = (String) mp.get("type");
         } catch (Exception ex) {
            log.error("error in getCommandNumberingType " + ex.getLocalizedMessage(), ex);
        } finally {
            return commandClass;
        }  
    }
    
    private static HashMap getClassName(String clsName) throws Exception{
        log.debug("in factory: getClass("+clsName+")");
        HashMap cmdMap;
        HashMap cmdsMap;
        cmdsMap = getIdNameMap();
        
        if (cmdsMap.containsKey(clsName)) {
            cmdMap =  (HashMap) cmdsMap.get(clsName);
            return cmdMap;
        } else {
            log.error("in factory: getClass is null");
            throw new Exception("getClassName(), the command name :"+clsName+" was not found.");
            
        }
       
       

    }
      /** Return the Id/Name map, create if necessary */
    private static HashMap getIdNameMap()
    {
        log.debug("in factory, getIdNameMap()");
        try {
        if( commandsMap == null ) {
            log.debug("commandsMap is null");
            commandsMap = createCommandsMap();
            log.debug("commandsMap size = "+commandsMap.size());
        }
        }
        catch (Exception e){
            log.error("exception in getIdNameMap()"+e.getLocalizedMessage());
        }
        return commandsMap;
    }

    private static String getCommandValue(String cmdName, String cmdKey ) throws Exception{
        HashMap cmdsMap = getIdNameMap();
        
        if (cmdsMap.containsKey(cmdName)) 
            {
             HashMap cmdMap = (HashMap) commandsMap.get(cmdName);
             if (cmdMap.containsKey(cmdKey)){
                 String keyValue = (String) cmdMap.get(cmdKey);
                 return keyValue;
             } else {
                 throw new Exception("Command key was not found for the command, key combination of :"+ cmdName + "," + cmdKey );
             }
        }  else
            throw new Exception("Command "+cmdName + " was not found!");
    }
 
    
    private static HashMap<String,HashMap> createCommandsMap(){
        HashMap<String, HashMap> cmds = null;
        try {
            cmds = new HashMap<String, HashMap>();
            
             log.debug("creating commands map");

            cmds.put(new String("makePaperSection"),         
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "paper", "single"));
            cmds.put(new String("makePrayerSection"), 
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "prayer", "single"));
            cmds.put(new String("makeNoticeOfMotionSection"), 
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "notice-of-motion", "single"));
            cmds.put(new String("makeQASection"), 
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "qa-section", "single"));
            cmds.put(new String("makeQuestionBlockSection"), 
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "question-block", "single"));  
            //markup buttons
             
            cmds.put(new String("makePrayerMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "prayers", "markup"));     
            cmds.put(new String("makePaperMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "papers", "markup"));     
            cmds.put(new String("makePaperDetailsMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "paper-details", "markup"));     
            cmds.put(new String("makeNoticeOfMotionMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "notice-of-motion", "markup"));     
            cmds.put(new String("makeNoticeMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "notice", "markup"));     
            cmds.put(new String("makeNoticeDetailsMarkup"),
                    newChildMap("org.bungeni.editor.panels.toolbarevents.toolbarButtonEventHandler", "noticetext", "markup"));     

            log.debug(new String("in factory, createCommandsMap()"));
      }
      catch (Exception e)
      {
          log.error("exception in createCommandsMap() "+ e.getLocalizedMessage());
      }
        finally{
            return cmds;
        }
        
    }
  
    
    
    private static HashMap<String,String> newChildMap(String theClass, String theSection, String theType){
        HashMap<String,String> mp = new HashMap<String,String>();
        mp.put(new String("class"), theClass);
        mp.put(new String("section"), theSection);
        //single / serial
        mp.put(new String("type"), theType );
        return mp;
    }

    
}
