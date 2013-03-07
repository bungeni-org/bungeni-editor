
package org.bungeni.translators.process.actions;

import java.io.IOException;
import javax.xml.transform.TransformerException;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Ashok
 */
public interface IProcessAction {

     public Document process(
            Document inputDocument, 
            OAProcessStep processInfo
             ) throws 
                TransformerException, 
                SAXException, 
                IOException ;
   
}
