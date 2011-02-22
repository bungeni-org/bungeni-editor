/*
 * TestOOoBungeniLib.java
 *
 * Created on 2011.02.20 - 23:06:45
 *
 */

package org.bungenitest;

import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.rdf.Statement;
import com.sun.star.rdf.XMetadatable;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.XURI;
import com.sun.star.text.XTextSection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.bungeni.editor.test.UnoTest;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.rdf.RDFMetadata;
import org.bungeni.ooo.rdf.RDFMetadataTest;

/**
 *
 * @author Ashok Hariharan
 */
public class Tester extends UnoTest {
    
    /** Creates a new instance of TestOOoBungeniLib */
    public Tester() {
    }

  
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
          
           //RDF Metadata test
            RDFMetadataTest rdfTest = new RDFMetadataTest();
            rdfTest.setupTests("test/testdocs/rdf_metadata.odt");
            rdfTest.runTests();
            

           
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
     
    }



}
