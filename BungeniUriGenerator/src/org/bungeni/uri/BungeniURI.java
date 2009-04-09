/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Extends the URIBase class overrides the get() API of URIBase
 * @author Ashok Hariharan
 */
public class BungeniURI extends URIBase {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniURI.class.getName());

     public BungeniURI(String orderOfURI) {
        super(orderOfURI);
     }
     
    @Override
     public final String get(){
         log.debug("BungeniURI.get() : " + ((outputUriOrderString == null)? new String("") : outputUriOrderString));
         //quoteReplacement is required since File separators on windows use the \ character, which 
         //gets interpreted as a regex character while doing a replaceAll ... 
         //this problem will not manifest on *nix platforms as they use the "/" character as the
         //file separator
         String separatorCharacter = Matcher.quoteReplacement(File.separator);
         return this.outputUriOrderString.replaceAll("~", separatorCharacter);
     }

    //test quote replacement
     public static void main(String[] args) {
          String s = "~ken~judgement~2009-4-23~eng";
          System.out.println(s.replaceAll("~", Matcher.quoteReplacement("\\")));
     }
}
