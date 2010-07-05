package org.bungeni.odfdocument.report;

import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author Ashok Hariharan
 */
public interface IBungeniOdfDocumentReportUI {

    public boolean showUI(JFrame parentf);
    public boolean hasUICompleted();
    public boolean setProcessHook(IBungeniOdfDocumentReportProcess processHook);
    public boolean setInputDocuments(BungeniOdfDocumentHelper[] docHelpers);
    public boolean setReportTemplate(BungeniOdfDocumentReportTemplate reportTemplate);
    public IBungeniOdfDocumentReportProcess getProcessHook();
    /**
     * A hashmap to pass key value pairs into the report creation interface
     * @param reportInfo
     * @return
     */
    public boolean initUI(HashMap<String,String> reportInfo);
    public boolean refreshUI();
}
