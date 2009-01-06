/*
 * NumberDecoratorFactory.java
 *
 * Created on June 11, 2008, 4:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author Administrator
 */
public class NumberDecoratorFactory {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NumberDecoratorFactory.class.getName());
  
        public static TreeMap<String, String> numberingDecorators =
            new TreeMap<String,String>()  {
                {
                    put ("flowerBracket", "org.bungeni.numbering.decorators.flowerBracketDecorator");
                    put ("hashPrefix", "org.bungeni.numbering.decorators.hashPrefixDecorator");
                    put ("postDashed", "org.bungeni.numbering.decorators.postDashedDecorator");
                    put ("postDotted", "org.bungeni.numbering.decorators.postDottedDecorator");
                    put ("parens", "org.bungeni.numbering.decorators.parensDecorator");
                    put ("square", "org.bungeni.numbering.decorators.squareDecorator");
                }
    };
    
    /** Creates a new instance of NumberDecoratorFactory */
    public NumberDecoratorFactory() {
    }
     
    public static INumberDecorator getNumberDecorator(String decorator) {
      INumberDecorator decoratorObj = null;
      //String schemeClassPrefix = "org.bungeni.numbering.schemes.";
      String decClass = null;
       try {
             if (numberingDecorators.containsKey(decorator)) 
                  decClass =  numberingDecorators.get(decorator);
             else
                  return null; // decClass =  numberingDecorators.get("DEFAULT"); 
             Class clsDecorator;
             clsDecorator = Class.forName(decClass);
             decoratorObj = (INumberDecorator) clsDecorator.newInstance();
       } catch (ClassNotFoundException ex) {
                log.error("getNumberDecorator : "+ ex.getMessage());
       } finally {
             return decoratorObj;
        }
    }    
    
     public static void main(String[] args) {
        /*create a numbering scheme object based on a parametr...
         *"ALPHA" will generate a alpha numeric numbering scheme object
         *"ROMAN" will generate a roman numeral numbering scheme object
         *"GENERAL" will generate a numeric serial numbering scheme object
         */
        INumberDecorator inumScheme = null;
        inumScheme = NumberDecoratorFactory.getNumberDecorator("postDotted");
       System.out.println(inumScheme.decorate("1"));
    } 
}
