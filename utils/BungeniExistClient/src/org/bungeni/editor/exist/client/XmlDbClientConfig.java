
package org.bungeni.editor.exist.client;

/**
 *
 * @author Ashok
 */
public class XmlDbClientConfig {
    public static String SERVER_NAME ;
    public static String SERVER_PORT;
    public static String URL_PREFIX;
    public static String PROTOCOL ;

    public static String ServerURI() {
        return PROTOCOL + "://" + SERVER_NAME + ":" + SERVER_PORT ;
    }
}
