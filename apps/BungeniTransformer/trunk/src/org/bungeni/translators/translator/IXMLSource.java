package org.bungeni.translators.translator;

import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Ashok
 */
public interface IXMLSource {

    public StreamSource getSource(String documentPath);

}
