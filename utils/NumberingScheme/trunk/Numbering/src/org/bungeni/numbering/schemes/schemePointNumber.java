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
 * class generates point ma
 * @author ashok
 */
public class schemePointNumber extends BaseNumberingScheme implements IGeneralNumberingScheme {
    public static String POINT_SEPARATOR=".";
    class NumToPointNumber extends GeneralNumberer {
        public String toPointNum(long number){
            return format(number, "1", 1, POINT_SEPARATOR, "", "");
        }
    } ;
    
    NumToPointNumber numberer = new NumToPointNumber();
    /** Creates a new instance of newNumbersToAlphabet */
    public schemePointNumber(){
        super();
      }
    
    /**
     * class to generate a Point numbering scheme , 1.1.1, 1.1.2, 1.1.3, 1.1.4 and so on....
     * takes 2 long numbers 112 - 114 and generates 1.1.2, 1.1.3, 1.1.4 from that
     */
    public schemePointNumber(long s, long e) {
        super(s, e);
    }

    public void generateSequence() {
        //seed base sequence first
        super.generateSequence();
        //now generate the class sequence
        Iterator<Long> baseIterator = baseSequence.iterator();
        while (baseIterator.hasNext()) {
            Long baseNumber = baseIterator.next();
            String pointNumber = numberer.toPointNum(baseNumber);
            addNumberToSequence(pointNumber);
        }
    }
   
    
    public static void main(String[] args) {
    schemePointNumber numObj = new schemePointNumber((long)12341, (long) 12349);
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
