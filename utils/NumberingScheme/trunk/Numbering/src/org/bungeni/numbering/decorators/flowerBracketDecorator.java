/*
 * parensDecorator.java
 *
 * Created on June 5, 2008, 11:51 AM
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
public class flowerBracketDecorator extends BaseNumberDecorator {
    static String PREFIX = "{";
    static String SUFFIX = "}";
   
    
    public String getPrefix(){
        return PREFIX;
    }
    
    public String getSuffix(){
        return SUFFIX;
    }
    
    /** Creates a new instance of parensDecorator */

}
