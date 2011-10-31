package org.bungeni.translators.utility.transformer;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.saxon.Configuration;
import net.sf.saxon.TransformerFactoryImpl;

//~--- JDK imports ------------------------------------------------------------

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This is the  XSLT transformer object.
 * It is used to perform an XSLT transformation on a Document
 */
public class XSLTTransformer implements XSLTTransformerInterface {

    /* The instance of this XSLTTransformer */
    private static XSLTTransformer instance = null;

    // the transformer factory of the XSLTtransformer
    //!+TRANSFORMER_INIT(AH, 2011-09-20) We always use the Saxon transformer ,
    //switch to direct initialization of saxon rather than via the JAXP route,
    //which does some odd things with dynamic class loading.
    private TransformerFactoryImpl transformerFactory;

    /**
     * Private constructor used to create the XSLTTransformer instance
     */
    private XSLTTransformer() {

        // set the compilator to use SAXON
        //!+TRANSFORMER_INIT(AH, 2011-09-20)-- Turned off becasue of direct initialization
        //of Saxon 
        //System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        // create an instance of TransformerFactory
        //!+TRANSFORMER_INIT(AH, 2011-09-20) -- init saxon directly
        this.transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
        // get the configuration of the transoformer factory
        //!+TRANSFORMER_INIT(AH, 2011-09-20) -- init saxon directly
        Configuration transformerFactoryConfiguration =  this.transformerFactory.getConfiguration();

        // set the line numbering true to the configuration
        transformerFactoryConfiguration.setLineNumbering(true);

        // set the new configuration of the transformer factory
        //!+TRANSFORMER_INIT(AH, 2011-09-20)
        this.transformerFactory.setConfiguration(transformerFactoryConfiguration);
    }

    /**
     * Get the current instance of the XSLTTransformer resolver
     * @return the XSLTTransformer instance
     */
    public static XSLTTransformer getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            instance = new XSLTTransformer();
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * This method applies the given XSLT to the given Document and returns the Document obtained after the transformation
     * @param aDocumentSource the stream source of the document to transform
     * @param anXSLTSource the stream source of the XSLT to apply to the document that you want to transform
     * @return the new StreamSource of the Document resulting applying the given XSLT to the given Document
     * @throws TransformerException
     * @throws UnsupportedEncodingException
     */
    public StreamSource transform(StreamSource aDocumentSource, StreamSource anXSLTSource)
            throws TransformerException, UnsupportedEncodingException {

        // create a new transformer
        Transformer trans = this.transformerFactory.newTransformer(anXSLTSource);

        // create the writer for the transformation
        StringWriter resultString = new StringWriter();
        // perform the transformation
        trans.transform(aDocumentSource, new StreamResult(resultString));

        // returns the obtained file
        // return new StreamSource(((InputStream)new ByteArrayInputStream(resultString.toString().getBytes("UTF-8"))));
        // ashok: simplified byte conversion to Stream using StringReader
        return new StreamSource(new java.io.StringReader(resultString.toString()));
    }

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
            throws TransformerException {

        // create a new transformer
        Transformer trans = this.transformerFactory.newTransformer(anXSLTSource);

        // create an iterator on the parameters hash map
        Iterator<String> parametersIterator = aParamSet.keySet().iterator();

        // while the Iterator has steps apply the transformation
        while (parametersIterator.hasNext()) {

            // get the next step
            String nextKey = parametersIterator.next();

            // add the parameter to the transformer
            trans.setParameter(nextKey, (String) aParamSet.get(nextKey));
        }

        // create the writer for the transformation
        StringWriter resultString = new StringWriter();

        // perform the transformation
        trans.transform(aDocumentSource, new StreamResult(resultString));

        // returns the obtained file
        // return new StreamSource(((InputStream)new ByteArrayInputStream(resultString.toString().getBytes())));
        // ashok: simplified byte conversion to Stream using StringReader
        return new StreamSource(new java.io.StringReader(resultString.toString()));
    }
}
