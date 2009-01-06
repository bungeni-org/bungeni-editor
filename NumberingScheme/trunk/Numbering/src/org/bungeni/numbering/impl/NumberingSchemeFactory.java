/*
 * NumberingSchemeFactory.java
 *
 * Created on March 18, 2008, 2:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Factory class to generate Numbering scheme objects
 * @author Ashok
 */
public class NumberingSchemeFactory {
    public static TreeMap<String, String> numberingSchemes =
            new TreeMap<String,String>()  {
                {
                    put("ROMAN", "org.bungeni.numbering.schemes.schemeRoman" );
                    put("ROMAN-Upper", "org.bungeni.numbering.schemes.schemeRomanUpper" );
                    put("NUMERIC", "org.bungeni.numbering.schemes.schemeNumeric" );
                    put("ALPHABETICAL", "org.bungeni.numbering.schemes.schemeAlphabetical" );
                    put("AlPHABETICAL-Lower", "org.bungeni.numbering.schemes.schemeAlphabeticalLower" );
                    put("DEFAULT", "org.bungeni.numbering.schemes.schemeNumeric" );
                }
    };
    
    /** Creates a new instance of NumberingSchemeFactory */
    public NumberingSchemeFactory() {
    }
    
    /**
     * takes a parameter based on which the appropriate numbering scheme class is generated
     */
     public static IGeneralNumberingScheme getNumberingScheme(String schemeName) {
      IGeneralNumberingScheme scheme = null;
      //String schemeClassPrefix = "org.bungeni.numbering.schemes.";
      String schemeClass = null;
       try {
             if (numberingSchemes.containsKey(schemeName)) 
                  schemeClass =  numberingSchemes.get(schemeName);
             else
                  schemeClass =  numberingSchemes.get("DEFAULT"); 
             Class clsScheme;
             clsScheme = Class.forName(schemeClass);
             scheme = (IGeneralNumberingScheme) clsScheme.newInstance();
       } catch (ClassNotFoundException ex) {
        System.out.println("class not found = " + ex.getMessage());
       } finally {
             return scheme;
        }
    }
     
    public static void main(String[] args) {
        /*create a numbering scheme object based on a parametr...
         *"ALPHA" will generate a alpha numeric numbering scheme object
         *"ROMAN" will generate a roman numeral numbering scheme object
         *"GENERAL" will generate a numeric serial numbering scheme object
         */
        IGeneralNumberingScheme inumScheme = null;
        inumScheme = NumberingSchemeFactory.getNumberingScheme("ROMAN");
        inumScheme.setRange(new NumberRange((long)12, (long)26));
        inumScheme.generateSequence();
        /*the numbering sequence has been generated, now display the sequence*/
        ArrayList<String> seq = inumScheme.getGeneratedSequence();
        Iterator<String> iter = seq.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().toString());
        }
    } 
}
