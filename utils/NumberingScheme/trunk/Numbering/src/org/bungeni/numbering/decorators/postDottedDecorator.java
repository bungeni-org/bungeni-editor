/*
 * postDottedDecorator.java
 *
 * Created on June 5, 2008, 12:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.decorators;

import org.bungeni.numbering.impl.BaseNumberDecorator;

/**
 *
 * @author Administrator
 */
public class postDottedDecorator extends BaseNumberDecorator {
    
    /** Creates a new instance of postDottedDecorator */
  public String getPrefix() {
        return PREFIX;
    }

    public String getSuffix() {
        return SUFFIX;
    }
    static String PREFIX = "";
    static String SUFFIX = ".";    
}
