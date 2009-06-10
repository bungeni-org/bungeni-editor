package org.bungeni.odf.sectioninfo;

import java.io.StringWriter;
import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.OdfFileDom;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ashok Hariharan
 */
public class XtractSections {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(XtractSections.class.getName());
    String fullPathToOdfFile = "";
    OdfDocument odfPackage ;

    /**
     * loads the input file as a odf document
     * @param odfFile - path to odf file e.g. /home/undesa/files/file_with_sections.odt
     */
    public XtractSections(String odfFile) {
        try {
            //get full path to file
            fullPathToOdfFile = odfFile;
            //load the odf package
            odfPackage = OdfDocument.loadDocument(odfFile);
        } catch (Exception ex) {
            log.error("XtractSections() = " + ex.getMessage());
        }
    }


    /**
     * Outputs the section info as a ":" delimited file.
     * @return
     */
    public String outputSectionInfo() {
        StringWriter out = new StringWriter();
        try {
            OdfFileDom fileDom = odfPackage.getContentDom();
            NodeList sectionList = fileDom.getElementsByTagName("text:section");
           // FileWriter fw = new FileWriter(outputFile);
            for (int i = 0; i < sectionList.getLength(); i++) {
                Element elem = (Element) sectionList.item(i);
                String outputLine = elem.getAttribute("text:name");
                String secContent = elem.getTextContent();
                secContent = (secContent.length() > 80 ? secContent.substring(0, 80) : secContent);
                secContent = secContent.replace("\n", "");
                outputLine = outputLine + ":" + secContent + "\n";
                out.append(outputLine);
            }
        } catch (Exception ex) {
            log.error("outputSectionInfo : "  + ex.getMessage());
        } finally {
            return out.toString();
        }
    }


    public static void main(String[] args) {
        try {
            XtractSections xs = new XtractSections(args[0]);
            System.out.print(xs.outputSectionInfo());
      //      secs.output();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
