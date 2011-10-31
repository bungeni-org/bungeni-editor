package org.bungeni.translators.utility.transformer;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 *
 * @author Ashok
 */
public class GenericTransformer {

    private static GenericTransformer instance = null;

    private TransformerFactory transformerFactory = null;

    private Transformer transformer = null;

    private GenericTransformer() throws TransformerConfigurationException{
        transformerFactory =TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl",
                this.getClass().getClassLoader());

        transformer =  transformerFactory.newTransformer();
       // transformer = l.newTransformer();
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

}
