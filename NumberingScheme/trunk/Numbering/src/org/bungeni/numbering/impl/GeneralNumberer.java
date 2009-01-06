/*
 * GeneralNumberer.java
 *
 * Created on March 18, 2008, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

/**
 * generic wrapper for abstractnumberer, puts dummy place holders for abstract methods from abstractnumberer.
 * Extend and override this class as neccessary (see schemeNumeric for an example)
 * @author Administrator
 */
public class GeneralNumberer extends BaseNumberer{
    
    /** Creates a new instance of GeneralNumberer */
    public GeneralNumberer() {
    }

    public String toWords(long l) {
        return null;
    }

    public String toOrdinalWords(String string, long l, int i) {
        return null;
    }

    public String monthName(int i, int i0, int i1) {
        return null;
    }

    public String dayName(int i, int i0, int i1) {
        return null;
    }
    
}
