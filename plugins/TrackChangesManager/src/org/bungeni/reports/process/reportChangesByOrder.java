package org.bungeni.reports.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.IQueryResultsIterator;
import org.bungeni.db.QueryResults;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportProcess;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportTemplate;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.text.OdfTextChange;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;
import org.odftoolkit.odfdom.doc.text.OdfTextChangedRegion;
import org.odftoolkit.odfdom.doc.text.OdfTextSection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ashok Hariharan
 */
public class reportChangesByOrder  extends BungeniOdfDocumentReportProcess {

      private static org.apache.log4j.Logger log                  =
        Logger.getLogger(reportChangesByOrder.class.getName());

    private final String[] FILTER_SECTION_TYPES = {
        "body"
    };

    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper) {
        /**
         * We iterate through the change identifiers for each document.
         * for each change identifier we capture the start and end node position in the db
         * 
         */
       final BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
       List<StructuredChangeType> changes = changesHelper.getAllChanges();
       List<String> queries = new ArrayList<String>(0);
       String documentPath = aDochelper.getDocumentPath();
       Integer iOrder = 0;
       queries.add(reportChangesByOrder_Queries.CLEANUP_QUERY(CommonFunctions.getCurrentBillID(), documentPath));
        for (StructuredChangeType structuredChangeType : changes) {
            String xpathStart = "";
            String xpathEnd = "";

            if (structuredChangeType.changetype.equals(BungeniOdfTrackedChangesHelper.__CHANGE_TYPE_DELETION__)) {
               OdfTextChange textChange = changesHelper.getChangeItem(structuredChangeType.changeId);
               xpathStart  = BungeniOdfNodeHelper.getXPath(textChange);
            } else {

               OdfTextChangeStart changeNodeStart = changesHelper.getChangeStartItem(structuredChangeType.changeId);
               OdfTextChangeEnd changeNodeEnd = changesHelper.getChangeEndItem(structuredChangeType.changeId);
               xpathStart = BungeniOdfNodeHelper.getXPath(changeNodeStart);
               xpathEnd = BungeniOdfNodeHelper.getXPath(changeNodeEnd);
            }

            String strQuery = reportChangesByOrder_Queries.ADD_CHANGE_BY_ORDER(
                    CommonFunctions.getCurrentBillID(), 
                    documentPath,
                    structuredChangeType.changeId,
                    structuredChangeType.changetype,
                    xpathStart,
                    xpathEnd,
                    Boolean.FALSE, iOrder);
            queries.add(strQuery);
            System.out.println(strQuery);
        }
        // first we build up a data store with the nodes and then we proces the document
        // with the nodes in the db
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        db.Connect();
        db.Update(queries, true);
        db.EndConnect();
        //now we iterate through the document ... and process all the top level nodes
        //lets do the sections first
       final BungeniOdfSectionHelper asectionHelper = aDochelper.getSectionHelper();
       Integer isecWeight = 0;
       NodeList nSections = asectionHelper.getDocumentSections();
        for (int i = 0; i < nSections.getLength(); i++) {
            Node sectionNode = nSections.item(i);
            OdfTextSection aSection = (OdfTextSection) sectionNode;
            String aSectionType = asectionHelper.getSectionType(aSection);
            //if it is a different section
            String aSectionName = aSection.getTextNameAttribute();
            if (!aSectionType.equals(aSectionName)) {
                if (!isInFilter(aSectionType)) {
                    //check for this section type
                    String sectionxPath = BungeniOdfNodeHelper.getXPath(sectionNode);
                    String sCheckQuery = reportChangesByOrder_Queries.UPDATE_CHANGES_FOR_SECTION_NODE(CommonFunctions.getCurrentBillID(),
                            documentPath,
                            sectionxPath,
                            aSectionType + "." + aSectionName, ++isecWeight);
                    db.Connect();
                    db.Update(sCheckQuery);
                    db.EndConnect();
                }
            }
        }

       
        //now look for hte deleted one
        String sQueryDeleted = reportChangesByOrder_Queries.CHECK_DELETED(CommonFunctions.getCurrentBillID(), documentPath);
        //look for the change id that was deleted
        QueryResults qr = db.ConnectAndQuery(sQueryDeleted);
        log.debug(" deletion query = " + sQueryDeleted);
        qr.resultsIterator(new IQueryResultsIterator(){

            public boolean iterateRow(QueryResults mQR, Vector<String> rowData) {
                String docName = mQR.getField(rowData, "DOC_NAME");
                String changeId = mQR.getField(rowData, "CHANGE_ID");
                String changeType = mQR.getField(rowData, "CHANGE_TYPE");
                if (changeType.equals("deletion")){
                    log.debug("Processing deletion node ");
                    OdfTextChangedRegion aRegion = changesHelper.getChangedRegionById(changeId);
                    StructuredChangeType scType = changesHelper.getStructuredChangeType(aRegion);
                    NodeList nodesList = changesHelper.getDeletedNodesExt(scType.elementChange.get(0), "//text:section");
                   log.debug("Found nodes : " + nodesList.getLength());
                    for (int i = 0; i < nodesList.getLength(); i++) {
                        // this is the first section ... we only want the first section since its one change
                        OdfTextSection aSection = (OdfTextSection) nodesList.item(i);
                        String sectionName = aSection.getTextNameAttribute();
                        String sectionType = asectionHelper.getSectionType(aSection);
                        //here we have to update the changes order table with the deletion info
                        log.debug(":::::: DELETED :::::::::::");
                        log.debug(" section = " + sectionName + " , sectionType = " + sectionType );

                        break;
                    }
                }
                return true;
            }

        });
      
        File fNewReport = BungeniOdfDocumentReport.getNewReport(aDochelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor"), aTemplate);
        return new BungeniOdfDocumentReport(fNewReport, aTemplate);
    }


    private boolean isInFilter (String sectionType) {
        for (String sType : FILTER_SECTION_TYPES) {
            if (sType.equals(sectionType)) {
                return true;
            }
        }
        return false;
    }

}
