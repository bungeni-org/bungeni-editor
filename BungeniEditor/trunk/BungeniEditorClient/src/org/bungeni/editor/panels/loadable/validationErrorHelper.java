package org.bungeni.editor.panels.loadable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonANUtils;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 *
 * @author undesa
 */
public class validationErrorHelper {

        private static org.apache.log4j.Logger log            =
        Logger.getLogger(validationErrorHelper.class.getName());

    public static boolean errorsExist(Document xmlDoc) {
        boolean bState = false;
        try {
            XPath xmlPath = XPath.newInstance("/validationErrors/validationError");
            Element foundNode = (Element) xmlPath.selectSingleNode(xmlDoc);
            if (foundNode == null) {
                bState = false;
            } else {
                bState = true;
            }
        } catch (JDOMException ex) {
            bState = false;
        }
        return bState;

    }
    private static SAXBuilder saxBuilder = null;

    public static Document getValidationErrors(String sourceFileURL) {
         Document xmlErrors = null;
         File fErrors = CommonANUtils.getNamedComponentFromFile(sourceFileURL, "errors.xml");
            if (fErrors.exists()) {
                if (saxBuilder == null) {
                    saxBuilder = CommonEditorXmlUtils.getNonValidatingSaxBuilder();
                }
               
                try {
                    BufferedReader errReader = CommonFileFunctions.getFileasBufferedReader(fErrors);
                    xmlErrors = saxBuilder.build(errReader);
                } catch (JDOMException ex) {
                    log.error("viewXmErrors ", ex);
                } catch (IOException ex) {
                    log.error("viewXmErrors ", ex);
                }
        }
       return xmlErrors;
    }
}