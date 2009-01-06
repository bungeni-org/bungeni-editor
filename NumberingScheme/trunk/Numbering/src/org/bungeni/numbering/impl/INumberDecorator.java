/*
 * INumberDecorator.java
 *
 * Created on June 5, 2008, 11:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

/**
 *
 * @author Administrator
 */
public interface INumberDecorator {
    
    public String getPrefix();

    public String getSuffix();
    
    public String decorate(String aNumber);
}
