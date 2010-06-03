package org.bungeni.odfdocument.report;

import javax.swing.JFrame;

/**
 *
 * @author Ashok Hariharan
 */
public interface IBungeniOdfDocumentReportUI {

    public boolean showUI(JFrame parentf);
    public boolean hasUICompleted();
    public boolean setProcessHook(IBungeniOdfDocumentReportProcess processHook);

}
