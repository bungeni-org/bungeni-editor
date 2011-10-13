package org.bungeni.translators.translator;

/**
 *This class describes the different input source types for the translator
 * Currently 2 different input source types are supported.
 *
 * @author Ashok
 */
public class XMLSourceFactory {

    public enum XMLSourceType {

        XML("org.bungeni.translators.translator.GenericXMLSource"),
        ODF("org.bungeni.translators.translator.ODFXMLSource"),
        BUNGENI_XML("org.bungeni.translators.translator.BungeniXMLSource");
        private String className;

        XMLSourceType(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return "Source Type : " + name() + "for class: " + this.className;
        }

        public IXMLSource getObjectInstance() throws ClassNotFoundException,
                InstantiationException,
                IllegalAccessException {
            Class sourceClass = Class.forName(this.className);
            IXMLSource xmlSource = (IXMLSource) sourceClass.newInstance();
            return xmlSource;
        }
    }
}