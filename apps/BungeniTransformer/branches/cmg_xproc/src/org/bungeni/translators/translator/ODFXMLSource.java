package org.bungeni.translators.translator;

import java.io.File;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.odf.ODFUtility;

/**
 * Provides the ODF document as an IXMLSource source
 *
 * @author Ashok
 */
public class ODFXMLSource implements IXMLSource {

    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(ODFXMLSource.class.getName());


    public StreamSource getSource(String documentPath) {
        StreamSource ss = null;
        try {
            ss = mergeODFXML(documentPath);
        } catch (TransformerFactoryConfigurationError ex) {
            log.error("getSource : TransformerConfigurationException", ex);
        } catch (Exception ex) {
            log.error("getSource : TransformerConfigurationException", ex);
        }
        return ss;
    }

        /***
     * Combines the ODF content.xml, meta.xml, styles.xml into one XML stream
     * @param aDocumentPath
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    private StreamSource mergeODFXML(String aDocumentPath) throws TransformerFactoryConfigurationError, Exception {
            ODFUtility odfUtil       = ODFUtility.getInstance();
            File       mergedOdfFile = odfUtil.mergeODF(aDocumentPath);
            StreamSource ODFDocument = FileUtility.getInstance().FileAsStreamSource(mergedOdfFile);
            return ODFDocument;
    }


}
