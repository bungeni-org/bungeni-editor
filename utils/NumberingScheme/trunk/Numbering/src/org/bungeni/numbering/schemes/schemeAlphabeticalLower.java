/*
 * schemeAlphabeticalLower.java
 *
 * Created on June 5, 2008, 1:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.schemes;

/**
 *
 * @author Administrator
 */
public class schemeAlphabeticalLower extends schemeAlphabetical {
    
    /** Creates a new instance of schemeAlphabeticalLower */
    public schemeAlphabeticalLower() {
         super();
    }
    
    public schemeAlphabeticalLower(long s, long e) {
        super(s, e);
    }
    
    public void addNumberToSequence(String number) {
         super.addNumberToSequence(number.toLowerCase());
    }


    
    public static void main(String[] args) {
        schemeAlphabeticalLower numObj = new schemeAlphabeticalLower((long)12, (long) 33);
        //numObj.setParentPrefix("1.1");
        numObj.generateSequence();
        java.util.ArrayList<String> seq = numObj.getGeneratedSequence();
        java.util.Iterator<String> iter = seq.iterator();
        while (iter.hasNext()) {
            String elem = iter.next();
            System.out.println(elem);
        }
            
    }

}
