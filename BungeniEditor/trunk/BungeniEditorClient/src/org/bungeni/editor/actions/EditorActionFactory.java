
package org.bungeni.editor.actions;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class EditorActionFactory  {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditorActionFactory.class.getName());
  
    /** Creates a new instance of EditorActionFactory */
    public EditorActionFactory() {
    }

    public static IEditorActionEvent getEventClass(toolbarAction action) {
       IEditorActionEvent eventHandler = null;
       try {
             Class eventHandlerClass;
             eventHandlerClass = Class.forName(action.action_class());
             eventHandler = (IEditorActionEvent) eventHandlerClass.newInstance();
       } catch (ClassNotFoundException ex) {
           log.error("getEventClass:"+ ex.getMessage());
        } finally {
             return eventHandler;
        }
    }

    // !+ACTION_RECONF (rm, jan 2012) - toolbarAction has been deprecated
    /**
    public static IEditorActionEvent getEventClass(toolbarAction action) {
      IEditorActionEvent eventHandler = null;
       try {
             log.debug("getEventClass: creating event class"+ action.action_class());
             Class eventHandlerClass;
             eventHandlerClass = Class.forName(action.action_class());
             eventHandler = (IEditorActionEvent) eventHandlerClass.newInstance();
       } catch (ClassNotFoundException ex) {
           log.error("getEventClass:"+ ex.getMessage());
        } finally {
             return eventHandler;
        }
    }
    **/
    
    public static IEditorActionEvent getEventClass(ArrayList<String> action) {
      IEditorActionEvent eventHandler = null;
       try {
             //log.debug("getEventClass: creating event class"+ action.action_class());
             Class eventHandlerClass;
             eventHandlerClass = Class.forName("org.bungeni.editor.actions.EditorGeneralActionHandler");
             eventHandler = (IEditorActionEvent) eventHandlerClass.newInstance();
       } catch (ClassNotFoundException ex) {
           log.error("getEventClass:"+ ex.getMessage());
        } finally {
             return eventHandler;
        }
    }
}
