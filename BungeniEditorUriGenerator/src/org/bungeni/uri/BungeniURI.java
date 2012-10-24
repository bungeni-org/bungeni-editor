package org.bungeni.uri;

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
         log.debug("BungeniURI.get() : " + ((outputUriOrderString == null)? "" : outputUriOrderString));
         //!+FILE_SEPARATOR_USAGE(AH,2011-09-20) Removed use of File.separator since URIs always
         //use the same separator
         return this.outputUriOrderString.replaceAll("~", URIBase.separator());
     }

    //test quote replacement
     public static void main(String[] args) {
          String s = "~ken~judgement~2009-4-23~eng";
          System.out.println(s.replaceAll("~", URIBase.separator()));
     }
}
