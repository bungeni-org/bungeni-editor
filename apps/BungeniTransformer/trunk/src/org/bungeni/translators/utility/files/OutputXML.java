package org.bungeni.translators.utility.files;

import java.io.File;
import javax.xml.transform.stream.StreamSource;

/**
 * This class encapsulates a File handle and its corresponding
 * StreamSource. 
 * @author Ashok
 */
public class OutputXML {
        public StreamSource outputxmlStream;
        public File outputxmlFile;

        public OutputXML(StreamSource ss, File mf) {
            this.outputxmlStream = ss;
            this.outputxmlFile = mf;
        }
}
