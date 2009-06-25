package org.bungeni.editor.panels.loadable;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok Hariharan
 */
public class validationErrorTableModel extends AbstractTableModel {
    private Document errorDocument  = null;
    private static Logger log = Logger.getLogger(validationErrorTableModel.class.getName());
    private List elemValidationErrors = new ArrayList(0);
    private String __VALIDATION_ERRORS__ = "/validationErrors/validationError";
    private final static String[] __COLUMNS__  = {"No" , "Error" };
    

    public validationErrorTableModel(Document errorDoc) {
        this.errorDocument = errorDoc;
        try {
            XPath validationErrors = XPath.newInstance(__VALIDATION_ERRORS__);
            elemValidationErrors =  validationErrors.selectNodes(errorDoc);
        } catch (JDOMException ex) {
            log.info("validationErrorTableModel:", ex);
            elemValidationErrors = new ArrayList<Element>(0);
        }
    }

    public int getRowCount() {
       return elemValidationErrors.size();
    }

    public int getColumnCount() {
       return __COLUMNS__.length;
    }

    @Override
    public Class getColumnClass(int nCol) {
        switch (nCol) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return String.class;
        }
    }


     @Override
     public String getColumnName(int column) {
        return __COLUMNS__[column];
     }
    public Object getValueAt(int rowIndex, int columnIndex) {
       switch (columnIndex) {
           case 0 :
              String s = "" + rowIndex + 1;
              return s;
           case 1 :
               return getErrorMessage(rowIndex);
            default:
               return null;
       }
    }

    private String getErrorMessage(int rowIndex) {
        StringBuffer strError = new StringBuffer();
        try {
        Element validationError = (Element) this.elemValidationErrors.get(rowIndex);
        Element errorMessage = validationError.getChild("msgs");
        if (errorMessage != null)  {
          List msgs = errorMessage.getChildren("msg");
          if (msgs != null) {
              Iterator iterMsgs = msgs.iterator();
              while (iterMsgs.hasNext()) {
                 Element msg =  (Element) iterMsgs.next();
                 strError.append(msg.getValue() + System.getProperty("line.separator"));
              }
          }
        }
        } catch (Exception ex) {
            log.error("getErrroMessage :",ex);
        }
        return strError.toString();
    }
}
