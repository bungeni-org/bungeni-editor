
package org.bungeni.editor.exist.client;

/**
 *
 * @author Ashok
 */
public class XmlDbClient {
    private static XmlDbClient instance = null;
    
    
    protected XmlDbClient() {
        
    }
    
    public static synchronized XmlDbClient getInstance() {
        if (instance == null) {
            instance = new XmlDbClient();
        }
        return instance;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }




}
