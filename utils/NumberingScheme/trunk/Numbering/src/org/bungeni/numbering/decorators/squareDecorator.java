/*
 * squareDecorator.java
 *
 * Created on June 5, 2008, 12:12 PM
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
public class squareDecorator extends BaseNumberDecorator {

    public String getPrefix() {
        return PREFIX;
    }

    public String getSuffix() {
        return SUFFIX;
    }
    static String PREFIX = "[";
    static String SUFFIX = "]";

    
}
