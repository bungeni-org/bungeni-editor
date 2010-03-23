package org.bungeni.odfdocument.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
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

    public boolean saveReport() {
        boolean bSave = false;
        try {
            this.reportDocument.getOdfDocument().save(this.reportDocument.getDocumentPath());
            bSave = true;
            
        } catch (Exception ex) {
            log.error("saveReport : " + ex.getMessage(), ex);
        } return bSave;
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

    public void addReportLines(List<BungeniOdfReportLine> lines) {
        for (BungeniOdfReportLine bungeniOdfReportLine : lines) {
            reportLines.add(bungeniOdfReportLine);
        }
    }

    /**
     * This function generates the report lines within the report body
     * @return
     */
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

    private String genericTextMatchXpathTemplate(){
        return "//office:text//text()[contains(.,'[%var%]')]";
    }

    private String genericRelativeTextMatchXpathTemplate(){
        return "./descendant::text()[contains(.,'[%var%]')]";
    }

    private String genericTextMatchXpath(String variable) {
        return genericTextMatchXpathTemplate().replaceAll("%var%", variable);
    }

    private String genericRelativeTextMatchXpath(String variable) {
        return genericRelativeTextMatchXpathTemplate().replaceAll("%var%", variable);
    }

    private String buildEscapedKey(String key) {
        return "\\[" + key + "\\]";
    }

    public void processReportVariables(){
        try {
            XPath xpath = this.reportDocument.getOdfDocument().getXPath();
            OdfFileDom contentDom = this.reportDocument.getOdfDocument().getContentDom();
            Iterator<String> keyIter = reportVariables.keySet().iterator();

            while (keyIter.hasNext()) {
                String sKey = keyIter.next();
                String sValue = reportVariables.get(sKey).toString();
                System.out.println("processing Key : " + sValue);
                String xpathSrch = this.genericTextMatchXpath(sKey);
                NodeList nltextNodes = (NodeList) xpath.evaluate(xpathSrch, contentDom, XPathConstants.NODESET);
                for (int i = 0; i < nltextNodes.getLength(); i++) {
                     Node ntextNode =  nltextNodes.item(i);
                     String stextContent = ntextNode.getTextContent();
                     String rptVariable = buildEscapedKey(sKey);
                     String newContent = stextContent.replaceAll(rptVariable, sValue);
                     ntextNode.setTextContent(newContent);
                }
            }
        } catch (Exception ex) {
            log.error("processReportVariable : " + ex.getMessage(), ex);
        }
    }

    private void feedReportLines(){
        BungeniOdfSectionHelper secHelper = this.reportDocument.getSectionHelper();
        OdfTextSection nsection = secHelper.getSection("ReportBody");
        XPath xpath = this.reportDocument.getOdfDocument().getXPath();

        ArrayList<OdfTextSection> reportLineSections = secHelper.getChildSections(nsection);
        List<Node> nodesMarkedForDeletion = new ArrayList<Node>(0);
        //iterate through each of the sections
        for (int iSecIndex = 0; iSecIndex < reportLineSections.size(); iSecIndex++) {
            //get the input section
            OdfTextSection aSection = reportLineSections.get(iSecIndex);
            //get the input section feeder
            BungeniOdfReportLine reportLine = this.reportLines.get(iSecIndex);
            //get the available report line variables
            Set<String> lineVariableNames = reportLine.getLineVariableNames();
            Iterator<String> varIterator = lineVariableNames.iterator();
            while (varIterator.hasNext()) {
                try {
                    String ssVar = varIterator.next();
                    String xpathString = genericRelativeTextMatchXpath(ssVar);
                    NodeList matchingTextNodes = (NodeList) xpath.evaluate(xpathString, aSection, XPathConstants.NODESET);
                    for (int j = 0 ; j < matchingTextNodes.getLength() ; j++) {
                        Node aNode = matchingTextNodes.item(j);
                        String sContent = aNode.getTextContent();
                        String escKey = this.buildEscapedKey(ssVar);
                        String lineVariable = reportLine.getLineVariable(ssVar).toString();
                        if (lineVariable.equals("[DELETE]")) {
                            if (aNode.getParentNode().getTextContent().equals(aNode.getTextContent()))
                                nodesMarkedForDeletion.add(aNode.getParentNode());
                            else
                                nodesMarkedForDeletion.add(aNode);
                        } else {
                            sContent = sContent.replaceAll(escKey, lineVariable);
                            aNode.setTextContent(sContent);
                        }
                    }
                }
                //now that we have the report line text node
                //  String xpathString = this.genericRelativeTextMatchXpath(null)
                catch (XPathExpressionException ex) {
                    log.error("feedReportLines : " + ex.getMessage(), ex);
                }
            }
            for (Node delNode :  nodesMarkedForDeletion) {
                delNode.getParentNode().removeChild(delNode);
            }
            nodesMarkedForDeletion.clear();
            //now that we have the report line text node
           //  String xpathString = this.genericRelativeTextMatchXpath(null)
        }
    }

    public void processReportLines(){
        //first generate report lines
        generateReportLines();
        //build the data into the report lines
        feedReportLines();
    }

    public void generateReport(){
        try {
            processReportVariables();
            processReportLines();
            String savePath = this.reportDocument.getDocumentPath();
            this.reportDocument.getOdfDocument().save(savePath);
        } catch (Exception ex) {
          log.error("generateReport :  " + ex.getMessage(), ex);
        }
    }
   
    public static void main(String[] args) {
        String s = "[REPORT]";
        System.out.println(s.replaceAll("\\[REPORT\\]", "This is a report"));
    }

}
