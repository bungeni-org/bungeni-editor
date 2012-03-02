/*
 * postDashedDecorator.java
 *
 * Created on June 5, 2008, 12:14 PM
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
public class postDashedDecorator extends BaseNumberDecorator {
    
    /** Creates a new instance of postDashedDecorator */
  public String getPrefix() {
        return PREFIX;
    }

    public String getSuffix() {
        return SUFFIX;
    }
    
    static String PREFIX = "";
    static String SUFFIX = "-";
    
}
