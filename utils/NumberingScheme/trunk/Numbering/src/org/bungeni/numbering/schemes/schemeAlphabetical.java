/*
 * schemeAlphabetical.java
 *
 * Created on March 18, 2008, 12:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.schemes;

import java.util.ArrayList;
import java.util.Iterator;
import org.bungeni.numbering.impl.BaseNumberingScheme;
import org.bungeni.numbering.impl.GeneralNumberer;
import org.bungeni.numbering.impl.IGeneralNumberingScheme;

/**
 * Generates an Alphbetical sequence.
 * @author Administrator
 */
public class schemeAlphabetical extends BaseNumberingScheme implements IGeneralNumberingScheme {
   
    
    private class NumToAlpha extends GeneralNumberer {
        public String toAlpha(long number){
            return toAlphaSequence(number, "ABCDEFGHIJKLMOPQRSTUVWXYZ" );
        }
    } ;
    
    private NumToAlpha numberer ;
    
    /** Creates a new instance of newNumbersToAlphabet */
    /**default constructor required**/
    public schemeAlphabetical(){
        super();
        numberer = new NumToAlpha();
    }
    /**parametered constructor**/
    public schemeAlphabetical(long s, long e) {
        super(s, e);
        numberer = new NumToAlpha();
    }


    public void generateSequence() {
        //seed base sequence first
        super.generateSequence();
        Iterator<Long> baseIterator = baseSequence.iterator();
        while (baseIterator.hasNext()) {
            Long baseNumber = baseIterator.next();
            addNumberToSequence(numberer.toAlpha(baseNumber));
        }
    }
   

    
    public static void main(String[] args) {
        schemeAlphabetical numObj = new schemeAlphabetical((long)12, (long) 33);
        numObj.setParentPrefix("1.1");
        numObj.generateSequence();
        ArrayList<String> seq = numObj.getGeneratedSequence();
        Iterator<String> iter = seq.iterator();
        while (iter.hasNext()) {
            String elem = iter.next();
            System.out.println(elem);
        }
            
    }
}
