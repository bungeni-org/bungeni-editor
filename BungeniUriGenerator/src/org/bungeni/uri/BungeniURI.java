/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class BungeniURI extends URIBase {
   
     public BungeniURI(String orderOfURI) {
        super(orderOfURI);
     }
     
    @Override
     public final String get(){
         return this.outputUriOrderString.replaceAll("~", File.separator);
     }
     
     
}
