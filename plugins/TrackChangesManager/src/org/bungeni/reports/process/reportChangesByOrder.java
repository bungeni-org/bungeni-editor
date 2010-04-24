package org.bungeni.reports.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReport;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportProcess;
import org.bungeni.odfdocument.report.BungeniOdfDocumentReportTemplate;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;
import org.bungeni.odfdom.utils.BungeniOdfNodeHelper;
import org.bungeni.trackchanges.utils.AppProperties;
import org.bungeni.trackchanges.utils.CommonFunctions;
import org.odftoolkit.odfdom.doc.text.OdfTextChange;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeEnd;
import org.odftoolkit.odfdom.doc.text.OdfTextChangeStart;

/**
 *
 * @author Ashok Hariharan
 */
public class reportChangesByOrder  extends BungeniOdfDocumentReportProcess {


    @Override
    public BungeniOdfDocumentReport generateReport(BungeniOdfDocumentReportTemplate aTemplate, BungeniOdfDocumentHelper aDochelper) {
        /**
         * We iterate through the change identifiers for each document.
         * for each change identifier we capture the start and end node position in the db
         * 
         */
       BungeniOdfTrackedChangesHelper changesHelper = aDochelper.getChangesHelper();
       List<StructuredChangeType> changes = changesHelper.getAllChanges();
       List<String> queries = new ArrayList<String>(0);
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
                    aDochelper.getDocumentPath(),
                    structuredChangeType.changeId,
                    structuredChangeType.changetype,
                    xpathStart,
                    xpathEnd);
            queries.add(strQuery);
        }
        BungeniClientDB db = BungeniClientDB.defaultConnect();
        db.Connect();
        db.Update(queries, true);
        db.EndConnect();
        File fNewReport = BungeniOdfDocumentReport.getNewReport(aDochelper.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor"), aTemplate);
        return new BungeniOdfDocumentReport(fNewReport, aTemplate);
    }



}
