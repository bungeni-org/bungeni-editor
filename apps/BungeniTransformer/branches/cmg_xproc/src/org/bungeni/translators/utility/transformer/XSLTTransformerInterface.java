package org.bungeni.translators.utility.transformer;

//~--- JDK imports ------------------------------------------------------------

import java.io.UnsupportedEncodingException;

import java.util.HashMap;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

/**
 * This is the interface for the java XSLT transformer object.
 * It is used to perform an XSLT transformation on a Document
 */
public interface XSLTTransformerInterface {

    /**
     * This method applies the given XSLT to the given Document and returns the Document obtained after the transformation
     * @param aDocumentSource the stream source of the document to transform
     * @param anXSLTSource the stream source of the XSLT to apply to the document that you want to transform
     * @return the new Document resulting applying the given XSLT to the given Docuement
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public StreamSource transform(StreamSource aDocumentSource, StreamSource anXSLTSource)
            throws TransformerException, UnsupportedEncodingException;

    /**
     * This method applies the given XSLT with the given parameters to the given Document and returns the Document obtained after the transformation
     * @param aDocumentSource the stream source of the document to transform
     * @param anXSLTSource the stream source of the XSLT to apply to the document that you want to transform
     * @param aParamSet an Hash Map containing a set of pair (name, object) that are the parameters of the XSLT transformation
     * @return the new Document resulting applying the given XSLT to the given Docuement
     * @throws TransformerException
     */
    public StreamSource transformWithParam(StreamSource aDocumentSource, StreamSource anXSLTSource,
            HashMap<String, Object> aParamSet)
            throws TransformerException;
}
