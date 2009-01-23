/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.uri;

/**
 *
 * @author undesa
 */
public class BungeniManifestationName extends URIBase {
  public  BungeniManifestationName(String fileNamePattern) {
        super(fileNamePattern);
     }
     
    @Override
     public final String get(){
         return this.outputUriOrderString.replaceAll("~", "_");
     }
}
