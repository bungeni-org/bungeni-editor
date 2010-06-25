
package org.bungeni.reports.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportProcess;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportTemplate;
import org.bungeni.odfdocument.report.BungeniOdfReportLine;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.w3c.dom.Node;

/**
 *
 * @author Ashok
 */
public class reportChangesByUser extends BungeniOdfDocumentReportProcess {
    private static org.apache.log4j.Logger log            =
        org.apache.log4j.Logger.getLogger(reportChangesByUser.class.getName());


    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate reportTemplate, BungeniOdfDocumentHelper docHelper ) {
        String reportOdfDoc = null;
        BungeniOdfDocumentReport reportObject = null;
        try {

            // generate the report from the MP's document
            String billReviewFolder =
                CommonFunctions.getWorkspaceForBill((String) AppProperties.getProperty("CurrentBillID"));
            String templatesFolder = CommonFunctions.getTemplateFolder();
            String sAuthor = docHelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
            File   freportFile = new File(billReviewFolder + File.separator +
                                        CommonFunctions.normalizeName(reportTemplate.toString())  + "_report_" + CommonFunctions.normalizeName(sAuthor) +".odt");


            // create an xpath object
            XPath docXpath = docHelper.getOdfDocument().getXPath();

            // get the changes helper ont he merged document
            BungeniOdfTrackedChangesHelper changesHelper = docHelper.getChangesHelper();

            // get all the change regions created by the mp
            List<OdfTextChangedRegion> changedRegions =
                changesHelper.getChangedRegionsByCreator(changesHelper.getTrackedChangeContainer(), sAuthor);

            // iterate through all the changed regions
            // build the report lines
            ArrayList<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);

            for (OdfTextChangedRegion odfTextChangedRegion : changedRegions) {

                // get the change map for the change - to be sent to the report line object
                StructuredChangeType    scType      = changesHelper.getStructuredChangeType(odfTextChangedRegion);
                HashMap<String, Object> mapOfChange = changesHelper.getChangeInfo(scType);
                String                  changeType  = mapOfChange.get("changeType").toString();
                String                  changeText  = mapOfChange.get("changeText").toString();
                String                  changeId    = mapOfChange.get("changeId").toString();

                // below we build the change context object for the change
                Node                    foundNodeStart = null;
                Node                    foundNodeEnd   = null;
                Node                    foundNode      = null;
                BungeniOdfChangeContext changeContext  = null;

                if (changeType.equals("insertion")) {

                    // look for text:change-start[@text:change-id
                    String matchNodeStart = "//text:change-start[@text:change-id='" + changeId + "']";
                    String matchNodeEnd   = "//text:change-end[@text:change-id='" + changeId + "']";

                    foundNodeStart = (Node) docXpath.evaluate(matchNodeStart,
                            docHelper.getOdfDocument().getContentDom(), XPathConstants.NODE);
                    foundNodeEnd = (Node) docXpath.evaluate(matchNodeEnd, docHelper.getOdfDocument().getContentDom(),
                            XPathConstants.NODE);
                    changeContext = new BungeniOdfChangeContext(foundNodeStart, foundNodeEnd, docHelper);
                } else if (changeType.equals("deletion")) {

                    // look for text:change [@text:change-id
                    String matchNode = "//text:change[@text:change-id='" + changeId + "']";

                    foundNode = (Node) docXpath.evaluate(matchNode, docHelper.getOdfDocument().getContentDom(),
                            XPathConstants.NODE);
                    changeContext = new BungeniOdfChangeContext(foundNode, docHelper);
                }

                BungeniOdfReportLine reportLine = new BungeniOdfReportLine(changeContext, mapOfChange);
                reportLine.buildLineVariables();

                reportLines.add(reportLine);
            }

            reportObject = new BungeniOdfDocumentReport(freportFile, reportTemplate);

            // reportObject.addReportVariable("REPORT_HEADER", "Bill Amendments Report");
            // TODO : get the bill name from them MP documents
            reportObject.addReportVariable("BILL_NAME", CommonFunctions.getCurrentBillName());

            // reportObject.addReportVariable("REPORT_FOOTER", "Bill Amendments Report");
            //
            reportObject.addReportVariable("NO_OF_AMENDMENTS", reportLines.size());
            reportObject.addReportVariable("MEMBER_OF_PARLIAMENT", sAuthor);
            reportObject.addReportVariable(
                "OFFICIAL_DATE", reportTemplate.getReportDateFormat().format(Calendar.getInstance().getTime()));
            reportObject.addReportLines(reportLines);
            reportObject.generateReport(sAuthor);
           // reportObject.saveReport();
           // reportOdfDoc = reportObject.getReportPath();
        } catch (Exception ex) {
            log.error("doReport : " + ex.getMessage(), ex);
        }

        return reportObject;

    }

    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper[] aDochelper) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean prepareProcess(BungeniOdfDocumentHelper[] aDochelper, HashMap<String,Object> paramMap) {
       return true;
    }


}
