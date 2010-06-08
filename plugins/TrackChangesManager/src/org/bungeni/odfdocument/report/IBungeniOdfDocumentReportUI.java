package org.bungeni.odfdocument.report;

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
    public IBungeniOdfDocumentReportProcess getProcessHook();
    public boolean initUI();

}
