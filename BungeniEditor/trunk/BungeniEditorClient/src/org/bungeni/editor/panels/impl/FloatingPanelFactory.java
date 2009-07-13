package org.bungeni.editor.panels.impl;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.ooo.utils.CommonExceptionUtils;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

/**
 *
 * @author Ashok Hariharan
 */
public class FloatingPanelFactory {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(FloatingPanelFactory.class.getName());
    public static String panelClass;
    public static String panelDescription;
    public static String panelHeight;
    public static String panelType;
    public static String panelWidth;
    public static String panelX;
    public static String panelY;

    /** Creates a new instance of FloatingPanelFactory */
    public FloatingPanelFactory() {}

    public static IFloatingPanel getPanelClass(String panelName) {
        IFloatingPanel newPanel = null;

        try {
            String          className = "";
            BungeniClientDB db        = new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(),
                                            DefaultInstanceFactory.DEFAULT_DB());

            db.Connect();

            QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_PANEL_BY_TYPE(panelName, "floatingPanel"));

            if (qr.hasResults()) {
                Vector<Vector<String>> resultRows = new Vector<Vector<String>>();

                resultRows = qr.theResults();

                for (Vector<String> resultRow : resultRows) {
                    panelType        = resultRow.elementAt(qr.getColumnIndex("PANEL_TYPE") - 1);
                    panelName        = resultRow.elementAt(qr.getColumnIndex("PANEL_NAME") - 1);
                    panelDescription = resultRow.elementAt(qr.getColumnIndex("PANEL_DESC") - 1);
                    panelClass       = resultRow.elementAt(qr.getColumnIndex("PANEL_CLASS") - 1);
                    panelWidth       = resultRow.elementAt(qr.getColumnIndex("PANEL_WIDTH") - 1);
                    panelHeight      = resultRow.elementAt(qr.getColumnIndex("PANEL_HEIGHT") - 1);
                    panelX           = resultRow.elementAt(qr.getColumnIndex("PANEL_X") - 1);
                    panelY           = resultRow.elementAt(qr.getColumnIndex("PANEL_Y") - 1);

                    break;
                }
            }

            db.EndConnect();

            Class eventClass;

            eventClass = Class.forName(panelClass);
            newPanel   = (IFloatingPanel) eventClass.newInstance();

            return newPanel;
        } catch (InstantiationException ex) {
            log.error("getPanelClass : " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error("getPanelClass : " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            log.error("getPanelClass : " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("getPanelClass : " + ex.getMessage());
            log.error("getPanelClass : " + CommonExceptionUtils.getStackTrace(ex));
        } finally {
            return newPanel;
        }
    }
}
