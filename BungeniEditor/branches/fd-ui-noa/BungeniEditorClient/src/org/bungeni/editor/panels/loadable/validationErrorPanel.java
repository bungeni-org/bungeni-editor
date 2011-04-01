/*
 * validationErrorPanel.java
 *
 * Created on Jun 25, 2009, 10:30:04 AM
 */
package org.bungeni.editor.panels.loadable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.extutils.CommonDocumentUtilFunctions;
import org.jdom.Document;

/**
 * Panel that displays validation error messages and handles user interaction and feedback
 * @author Ashok Hariharan
 */
public class validationErrorPanel extends javax.swing.JPanel {

    /**
     * handle to caller panel
     */
    ITabbedPanel callerPanel = null;
    /**
     * xml document containing validation errors
     */
    Document docValidationErrors = null;
    JFrame parentFrame = null;

    /** Creates new form validationErrorPanel */
    public validationErrorPanel() {
        initComponents();
    }

    public validationErrorPanel(ITabbedPanel callingPanel, JFrame parentFrme, Document xmlErrorDoc) {
        initComponents();
        this.callerPanel = callingPanel;
        this.docValidationErrors = xmlErrorDoc;
        this.parentFrame = parentFrme;
        initTables();
    }

    /**
     * Sets up the jtable displaying the error mesages
     */
    private void initTables() {
        validationErrorTableModel tblModel = new validationErrorTableModel(this.docValidationErrors);
        this.tblValidationErrors.setModel(tblModel);
        this.tblValidationErrors.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        this.tblValidationErrors.addMouseListener(new tblValidationErrorMouseListener());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnClose = new javax.swing.JButton();
        scrollValidationErrors = new javax.swing.JScrollPane();
        tblValidationErrors = new javax.swing.JTable();

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tblValidationErrors.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        scrollValidationErrors.setViewportView(tblValidationErrors);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(scrollValidationErrors, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(195, 195, 195).addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(scrollValidationErrors, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnClose)));
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        parentFrame.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JScrollPane scrollValidationErrors;
    private javax.swing.JTable tblValidationErrors;
    // End of variables declaration//GEN-END:variables

    public class tblValidationErrorMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point p = e.getPoint();
                int row = tblValidationErrors.rowAtPoint(p);
                pointErrorInDocument(row);
            // ...
            }
        }
    }

    private void pointErrorInDocument(int nrow) {

        validationErrorTableModel tblModel = (validationErrorTableModel) this.tblValidationErrors.getModel();
        final String matchedSectionName = tblModel.getSectionId(nrow);
        new SwingWorker<Object, Object>() {

            protected Object doInBackground() throws Exception {
                CommonDocumentUtilFunctions.selectSection(callerPanel.getOOComponentHandle(), matchedSectionName);
                return null;
            }
        }.execute();
    }

    public class TextAreaRenderer extends JTextArea
            implements TableCellRenderer {

        private final DefaultTableCellRenderer adaptee =
                new DefaultTableCellRenderer();
        /** map from table to map of rows to map of column heights */
        private final Map cellSizes = new HashMap();

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(//
                JTable table, Object obj, boolean isSelected,
                boolean hasFocus, int row, int column) {
            // set the colours, etc. using the standard for that platform
            adaptee.getTableCellRendererComponent(table, obj,
                    isSelected, hasFocus, row, column);
            setForeground(adaptee.getForeground());
            setBackground(adaptee.getBackground());
            setBorder(adaptee.getBorder());
            setFont(adaptee.getFont());
            setText(adaptee.getText());

            // This line was very important to get it working with JDK1.4
            TableColumnModel columnModel = table.getColumnModel();
            setSize(columnModel.getColumn(column).getWidth(), 100000);
            int height_wanted = (int) getPreferredSize().getHeight();
            addSize(table, row, column, height_wanted);
            height_wanted = findTotalMaximumRowSize(table, row);
            if (height_wanted != table.getRowHeight(row)) {
                table.setRowHeight(row, height_wanted);
            }
            return this;
        }

        private void addSize(JTable table, int row, int column,
                int height) {
            Map rows = (Map) cellSizes.get(table);
            if (rows == null) {
                cellSizes.put(table, rows = new HashMap());
            }
            Map rowheights = (Map) rows.get(new Integer(row));
            if (rowheights == null) {
                rows.put(new Integer(row), rowheights = new HashMap());
            }
            rowheights.put(new Integer(column), new Integer(height));
        }

        /**
         * Look through all columns and get the renderer.  If it is
         * also a TextAreaRenderer, we look at the maximum height in
         * its hash table for this row.
         */
        private int findTotalMaximumRowSize(JTable table, int row) {
            int maximum_height = 0;
            Enumeration columns = table.getColumnModel().getColumns();
            while (columns.hasMoreElements()) {
                TableColumn tc = (TableColumn) columns.nextElement();
                TableCellRenderer cellRenderer = tc.getCellRenderer();
                if (cellRenderer instanceof TextAreaRenderer) {
                    TextAreaRenderer tar = (TextAreaRenderer) cellRenderer;
                    maximum_height = Math.max(maximum_height,
                            tar.findMaximumRowSize(table, row));
                }
            }
            return maximum_height;
        }

        private int findMaximumRowSize(JTable table, int row) {
            Map rows = (Map) cellSizes.get(table);
            if (rows == null) {
                return 0;
            }
            Map rowheights = (Map) rows.get(new Integer(row));
            if (rowheights == null) {
                return 0;
            }
            int maximum_height = 0;
            for (Iterator it = rowheights.entrySet().iterator();
                    it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                int cellHeight = ((Integer) entry.getValue()).intValue();
                maximum_height = Math.max(maximum_height, cellHeight);
            }
            return maximum_height;
        }
    }
}
