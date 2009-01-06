/*
 * BungeniError.java
 *
 * Created on December 12, 2007, 3:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.error;

/**
 *
 * @author Administrator
 */
public class BungeniError {
    /** Creates a new instance of BungeniError */
    public BungeniError() {
    }
   
    
    //Editor Selection Action errors
    
    public static final int SYSTEM_CONTAINER_NOT_REQD = -1000;
    public static final int SYSTEM_CONTAINER_CHECK_OK = -1001;
    public static final int SYSTEM_CONTAINER_WRONG_POSITION = -1002;
    public static final int SYSTEM_CONTAINER_NOT_PRESENT = -1003;
    public static final int SYSTEM_CONTAINER_ALREADY_EXISTS = -1004;
    
    public static final int NO_TEXT_SELECTED = -2001;
    public static final int DOCUMENT_ROOT_EXISTS = 2002 ;
    public static final int DOCUMENT_ROOT_DOES_NOT_EXIST = -2002;
    
    public static final int DOCUMENT_LEVEL_ACTION_RO0T_EXISTS = 3001;
    public static final int DOCUMENT_LEVEL_ACTION_ROOT_DOES_NOT_EXIST = -3001;
    
    public static final int MESSAGE_NEXT_ACTION_INIT = 5998;
    public static final int MESSAGE_DEFAULT_MESSAGE = 5999;
    
    public static final int DOCUMENT_LEVEL_ACTION_PROCEED = 6000;
    public static final int DOCUMENT_LEVEL_ACTION_FAIL = -6000;
    public static final int TEXT_SELECTED_INSERT_ACTION_PROCEED = 6100;
    public static final int TEXT_SELECTED_INSERT_ACTION_FAIL = -6100;
    
    public static final int TEXT_SELECTED_SYSTEM_ACTION_PROCEED = 6200;
    public static final int TEXT_SELECTED_SYSTEM_ACTION_FAIL = -6200;
    
    public static final int METHOD_NOT_IMPLEMENTED = -9000;
    public static final int ACTION_CANCELLED = -9001;
    public static final int VALID_SECTION_CONTAINER = 9002;
    public static final int INVALID_SECTION_CONTAINER = -9002;
    public static final int SECTION_EXISTS = 9003;
    public static final int SECTION_DOES_NOT_EXIST = -9003;
    public static final int INVALID_CONTAINER_FOR_SYSTEM_ACTION = -9004;
    public static final int VALID_CONTAINER_FOR_SYSTEM_ACTION = 9004;
    public static final int MARKUP_LOGO_PROCEED = 9005;
    public static final int GENERAL_ERROR = -9999;



    
}
