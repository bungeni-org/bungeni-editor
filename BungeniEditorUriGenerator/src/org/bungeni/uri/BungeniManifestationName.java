package org.bungeni.uri;

/**
 *
 * @author Ashok Hariharan
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
