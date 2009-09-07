
package org.bungeni.editor.exist.client;

/**
 *
 * @author Ashok Hariharan
 */
public class XmlDb {
    String userName = "";
    String userPassword = "";
 
    public static final String XML_MIMETYPE = "text/xml";
    public static final String ZIP_MIMETYPE = "application/zip";
    public static final String TEXT_MIMETYPE = "text/text";

    public XmlDb(String u, String p ) {
        userName = u;
        userPassword = p;
    }

}
