/*
 * BaseNumberDecorator.java
 *
 * Created on June 5, 2008, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

/**
 *
 * @author Administrator
 */
abstract public class BaseNumberDecorator implements INumberDecorator {
  
    /** Creates a new instance of BaseNumberDecorator */
    abstract public String getPrefix();
   
    abstract public String getSuffix();
    
    public String decorate(String aNum){
        return getPrefix() + aNum + getSuffix();
    }
}
