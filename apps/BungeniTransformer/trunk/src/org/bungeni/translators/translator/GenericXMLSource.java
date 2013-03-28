package org.bungeni.translators.translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.stream.StreamSource;
import org.bungeni.translators.utility.files.FileUtility;

/**
 *
 * @author Ashok
 */
public class GenericXMLSource implements IXMLSource {

    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(GenericXMLSource.class.getName());

    @Override
    public StreamSource getSource(String documentPath) {
            File fxmlSource = new File(documentPath);
            StreamSource XmlDocument = null;
            try {
                XmlDocument = FileUtility.getInstance().FileAsStreamSource(fxmlSource);
            } catch (UnsupportedEncodingException ex) {
                log.error("getSource : Encoding error ", ex);
            } catch (FileNotFoundException ex) {
                log.error("getSource : File not found", ex);
            }
            return XmlDocument;
    }


}
