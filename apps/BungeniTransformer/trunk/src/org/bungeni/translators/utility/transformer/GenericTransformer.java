package org.bungeni.translators.utility.transformer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 * This class always loads the default transformer factory as saxon.
 * We dont do the specific initialization for XSLT transformation so for default
 * dom node transformation this is the recommended cached transformer
 * @author Ashok
 */
public class GenericTransformer {

    private static GenericTransformer instance = null;

    private TransformerFactory transformerFactory = null;

    private Transformer transformer = null;

    private GenericTransformer() throws TransformerConfigurationException{
        /**
         * We explicity use Saxon 
         */
        transformerFactory =new net.sf.saxon.TransformerFactoryImpl();
        transformer =  transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    }

    public static GenericTransformer getInstance() throws TransformerConfigurationException {
        if (instance == null) {
            instance= new GenericTransformer();
        }
        return instance;
    }

    public Transformer getTransformer() {
        return this.transformer;
    }

    public TransformerFactory getTransformerFactory() {
        return this.transformerFactory;
    }

    /**
     * Returns the Saxon TransformerFactoryImpl object
     * @return
     */
    public net.sf.saxon.TransformerFactoryImpl getTransformerFactoryImpl(){
       return  (net.sf.saxon.TransformerFactoryImpl) this.transformerFactory;
    }

}
