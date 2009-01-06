/*
 * newNumbersToAlphabet.java
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
 *
 * @author Administrator
 */
public class schemeRoman extends BaseNumberingScheme implements IGeneralNumberingScheme {
   
    GeneralNumberer numberer ;
    
    /** Creates a new instance of newNumbersToAlphabet */
    public schemeRoman(){
        super();
        numberer = new GeneralNumberer();
    }
    
    public schemeRoman(long s, long e) {
        super(s, e);
        numberer = new GeneralNumberer();
    }

    public void generateSequence() {
        //seed base sequence first
        super.generateSequence();
        //now generate the class sequence
        Iterator<Long> baseIterator = baseSequence.iterator();
        while (baseIterator.hasNext()) {
            Long baseNumber = baseIterator.next();
            addNumberToSequence(numberer.toRoman(baseNumber));
        }
    }
   
    
    public static void main(String[] args) {
        schemeRoman numObj = new schemeRoman((long)12, (long) 33);
        numObj.setParentPrefix("1");
        numObj.generateSequence();
        ArrayList<String> seq = numObj.getGeneratedSequence();
        for (int i=0; i < seq.size() ; i++ ){
            System.out.println("number in sequence = " + seq.get(i));
            System.out.println("base number insequence = " + numObj.getSequence().get(i));
        }
            
    }

}
