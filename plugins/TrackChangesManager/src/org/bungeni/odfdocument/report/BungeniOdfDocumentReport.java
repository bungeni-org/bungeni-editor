package org.bungeni.odfdocument.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The main reporting class
 * @author Ashok Hariharan
 */
public class BungeniOdfDocumentReport {

    BungeniOdfDocumentReportTemplate basedOnTemplate = null;
    BungeniOdfDocumentHelper reportDocument = null;
    List<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);
    TreeMap<String,Object> reportVariables = new TreeMap<String,Object>();

         private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOdfDocumentReport.class.getName());

    public BungeniOdfDocumentReport(File fnewReport, BungeniOdfDocumentReportTemplate fromTemplate){
        basedOnTemplate = fromTemplate;
        reportDocument = createNewReport(fnewReport);
    }

    public BungeniOdfDocumentHelper createNewReport(File fnewReport){
        return basedOnTemplate.documentFromTemplate(fnewReport);
    }


    public void addReportVariable(String varKey, Object varObject) {
        reportVariables.put(varKey, varObject);
    }

    public Object getReportVariable(String varKey) {
        return reportVariables.get(varKey);
    }



    public void addReportLine(BungeniOdfReportLine reportLine) {
        reportLines.add(reportLine);
    }


    public boolean generateReportLines() {
        //first duplicate sections for number of lines.
        boolean bReturn = false;
        int nCount = this.reportLines.size();
        BungeniOdfSectionHelper secHelper = this.reportDocument.getSectionHelper();
        OdfTextSection masterSection = secHelper.getSection("ReportLine-Repeat");
        OdfTextSection prevSection = masterSection;
        for (int i = nCount; i > 0; i--) {
             OdfTextSection copySection = (OdfTextSection) masterSection.cloneNode(true);
             copySection.setTextNameAttribute("reportline-"+ i);
             prevSection.getParentNode().insertBefore(copySection, prevSection);
             prevSection = copySection;
        }
        prevSection.getParentNode().removeChild(masterSection);
        bReturn = true;
        return bReturn;
    }

    public void processReportVariables(){
        try {
            XPath xpath = this.reportDocument.getOdfDocument().getXPath();
            String xpathString = "//office:text//text()[contains(.,'[%var%]')]";
            OdfFileDom contentDom = this.reportDocument.getOdfDocument().getContentDom();
            Iterator<String> keyIter = reportVariables.keySet().iterator();

            while (keyIter.hasNext()) {
                String sKey = keyIter.next();
                String sValue = reportVariables.get(sKey).toString();
                System.out.println("processing Key : " + sValue);
                String xpathSrch = xpathString.replaceAll("%var%", sKey);
                NodeList nltextNodes = (NodeList) xpath.evaluate(xpathSrch, contentDom, XPathConstants.NODESET);
                for (int i = 0; i < nltextNodes.getLength(); i++) {
                     Node ntextNode =  nltextNodes.item(i);
                     String stextContent = ntextNode.getTextContent();
                     String rptVariable = "\\[" + sKey + "\\]";
                     String newContent = stextContent.replaceAll(rptVariable, sValue);
                     ntextNode.setTextContent(newContent);
                }
            }
        } catch (Exception ex) {
            log.error("processReportVariable : " + ex.getMessage(), ex);
        }
    }

    public void generateReport(){
        try {
            processReportVariables();
            processReportLines();
            this.reportDocument.getOdfDocument().save(this.reportDocument.getDocumentPath());
        } catch (Exception ex) {
          log.error("generateReport :  " + ex.getMessage(), ex);
        }
    }
   
    public static void main(String[] args) {
        String s = "[REPORT]";
        System.out.println(s.replaceAll("\\[REPORT\\]", "This is a report"));
    }

}
