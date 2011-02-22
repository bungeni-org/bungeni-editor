/*
 * TestOOoBungeniLib.java
 *
 * Created on 2011.02.20 - 23:06:45
 *
 */

package org.bungenitest;

import org.bungeni.editor.test.UnoTest;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.OOComponentHelperOldStyle_Test;
import org.bungeni.ooo.OOComponentHelperTest;
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


           
            UnoTest.disposeDocument();

            OOComponentHelper.USE_OLD_STYLE_METADATA = false;


            OOComponentHelperTest  ooTest= new OOComponentHelperTest();
            ooTest.setupTests("test/testdocs/oo_metadata.odt");
            ooTest.runTests();

            UnoTest.disposeDocument();

            OOComponentHelper.USE_OLD_STYLE_METADATA = true;


            OOComponentHelperOldStyle_Test  ooTestS= new OOComponentHelperOldStyle_Test();
            ooTestS.setupTests("test/testdocs/oo_metadata.odt");
            ooTestS.runTests();
           
           

           
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
     
    }



}
