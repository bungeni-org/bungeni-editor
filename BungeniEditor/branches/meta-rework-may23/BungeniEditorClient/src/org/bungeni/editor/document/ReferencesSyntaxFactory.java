/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.document;

import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class ReferencesSyntaxFactory {
        private final static HashMap<String,String> SyntaxMap = new HashMap<String,String>(){
            {
                put("bill-format-1", "<artPrefix><artType><artSuffix><refPrefix><refPlaceHolder><refSuffix>");
            }
        };
        
        public static HashMap<String,String> getSyntaxMap(){
            return SyntaxMap;
        }
        
        public static ReferencesSyntax getSyntaxObject(String name) {
            if (SyntaxMap.containsKey(name)) {
                return new ReferencesSyntax(SyntaxMap.get(name));
            } else 
                return null;
        }
}
