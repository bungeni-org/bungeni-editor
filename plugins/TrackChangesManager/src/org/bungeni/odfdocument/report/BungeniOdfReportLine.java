package org.bungeni.odfdocument.report;

import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;
import org.bungeni.odfdom.document.changes.BungeniOdfTrackedChangesHelper.StructuredChangeType;

/**
 * Outputs a report line
 * @author Ashok Hariharan
 */
public class BungeniOdfReportLine {
    BungeniOdfChangeContext changeContext;
    StructuredChangeType changeType;

    public BungeniOdfReportLine(BungeniOdfChangeContext cxt, StructuredChangeType scType) {
        this.changeContext = cxt;
        this.changeType = scType;
    }

    
}
