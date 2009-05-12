package org.bungeni.plugins.convertsection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.section.IBungeniOdfSectionIterator;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.element.text.OdfSection;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniSectionConvertToPlain  {
    private HashMap editorParams = null;
//    private static org.apache.log4j.Logger log = Logger.getLogger(BungeniSectionConvertToPlain.class.getName());

    public BungeniSectionConvertToPlain(){
        editorParams = new HashMap();
        System.out.println("instantiating BungeniSectionConvertToPlain");
    }

    public void setParams(HashMap params) {
        System.out.println("calling setParams");
        editorParams = params;
    }

      public static  File convertUrlToFile(String sUrl) {
        File f = null;
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            // log.error("convertUrlToFile: "+ ex.getMessage());
            return null;
        }
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        } finally {
            return f;
        }
    }

      private boolean makePlainDocument(OdfDocument odfDoc, File fileCopy) {
        boolean bState = false;
        try {
            BungeniOdfSectionHelper odfDomHelper = new BungeniOdfSectionHelper(odfDoc);
            odfDomHelper.iterateSections(new IBungeniOdfSectionIterator(){
              public boolean nextSection(BungeniOdfSectionHelper helper, OdfSection arg0) {
                   helper.removeSectionBackgroundImage(arg0);
                    return true;
                }
            });
           FileOutputStream fstream = new FileOutputStream(fileCopy);
           odfDoc.save(fstream);
            bState = true;
        } catch (Exception ex) {
       ///     log.error("makePlainDocument : " + ex.getMessage());
        } finally {
            return bState;
        }


    }


    public String exec() {
        String outputFile = null;
        try {
            System.out.println("calling exec()");

            String strOdfFileURL = (String) this.editorParams.get("OdfFileURL");
                        System.out.println("odffileurl = " + strOdfFileURL);

            File fOpenFile = convertUrlToFile(strOdfFileURL);
                        System.out.println("loading odfdocument");

            OdfDocument odfDoc = OdfDocument.loadDocument(fOpenFile);
                                    System.out.println("calling odffilecopy odfdocument");

            BungeniOdfFileCopy fcp = new BungeniOdfFileCopy(odfDoc.getPackage());
            File origFileCopy = fcp.copyTo("_plain", true);
                                    System.out.println("making plain document");

            makePlainDocument(odfDoc, origFileCopy);
            outputFile = origFileCopy.toURI().toURL().toString();
        } catch (Exception ex) {
            System.out.println("exec() "+ ex.getMessage());
            System.out.println("exec() " + ex.getClass().getName());
            System.out.println("exec() " + getStackTrace(ex));
        } finally {
            return outputFile;
        }
    }

     public  String getStackTrace(Exception exception ) {
        StringWriter stringWriter = new StringWriter();
        String stackTrace = "";
        exception.printStackTrace(new PrintWriter(stringWriter));
        // get he stackTrace as String...
        stackTrace = stringWriter.toString();
        return stackTrace;
    }
        public static void main(String[] args) {
            BungeniSectionConvertToPlain p = new BungeniSectionConvertToPlain();
            HashMap mp = new HashMap();
            mp.put("OdfFileURL", "file:/home/undesa/Projects/Bungeni/BungeniEditor/trunk/BungeniEditorClient/dist/workspace/files/ke/debaterecord/2009-5-19/eng/ke_debaterecord_2009-5-19_eng.odt");
            p.setParams(mp);
            p.exec();
        }

}
