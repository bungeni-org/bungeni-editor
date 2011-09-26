package org.bungeni.translators.translator;

/**
 *
 * @author Ashok
 */
public class XMLSourceFactory {


    enum SourceTypes {
        XML,
        ODF;

        @Override
        public String toString(){
            return "Source Type : "+name();
        }
    }

}
