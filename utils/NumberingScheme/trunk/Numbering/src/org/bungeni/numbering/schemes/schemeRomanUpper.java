/*
 * schemeRomanUpper.java
 *
 * Created on May 24, 2008, 5:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.schemes;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class schemeRomanUpper extends schemeRoman {
    
    /** Creates a new instance of schemeRomanUpper */
    public schemeRomanUpper() {
        super();
    }
    
    public schemeRomanUpper(long s, long e) {
        super(s, e);
    }
    
    @Override
    public void addNumberToSequence(String number) {
         super.addNumberToSequence(number.toUpperCase());
    }
    
        
    public static void main(String[] args) {
        schemeRomanUpper numObj = new schemeRomanUpper((long)12, (long) 33);
        numObj.setParentPrefix("1");
        numObj.generateSequence();
        ArrayList<String> seq = numObj.getGeneratedSequence();
        Iterator<String> iter = seq.iterator();
        while (iter.hasNext()) {
            String elem = iter.next();
            System.out.println(elem);
        }
            
    }
}
