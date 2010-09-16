

package org.bungeni.editor.panels.loadable.structuralerror;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.apache.log4j.Logger;
import org.bungeni.editor.panels.loadable.validationErrorTableModel;
import org.jdom.Document;

////import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class panelStructuralError2 extends javax.swing.JPanel  {

    private static org.apache.log4j.Logger log            =
        Logger.getLogger(panelStructuralError2.class.getName());



    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_HEIGHT = 300;
    private static final int PANEL_HEIGHT_PADDING = 15;

    ArrayList<StructuralError> structuralErrors = new ArrayList<StructuralError>(0);
    Document validationErrors = null;
    Window parentFrame = null;
    Window containerFrame = null;
    Object callerPanel = null;

    /** Creates new form panelStructuralError */
    public panelStructuralError2(ArrayList<StructuralError> serror , Document xmlErrors, Object cPanel) {
        this.structuralErrors = serror;
        this.validationErrors = xmlErrors;
        this.callerPanel = cPanel;
        initComponents();
        initStructErrorTable();
        initValidationErrorTable();
    }

    public void update (ArrayList<StructuralError> serror) {
        this.structuralErrors = serror;
        initStructErrorTable();
        initValidationErrorTable();
    }

    /**
     * Returns the frame size for the panel
     * @return
     */
    public java.awt.Dimension getFrameSize(){
        return new Dimension (PANEL_WIDTH, PANEL_HEIGHT + 15);
    }


    /**
     * Setups up the table model, then configures the column widths, finally attaches a mouse listener
     * to detect double click events
     */
      
    private void initStructErrorTable(){
        initTableModel();
        initColumnConfig();
        initMouseListeners();
    }

    private void initValidationErrorTable(){
        if (this.validationErrors != null ) {
            validationErrorTableModel tblModel = new validationErrorTableModel(this.validationErrors);
            this.tblValidationErrors.setModel(tblModel);
            this.tblValidationErrors.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        }
     //   this.tblValidationErrors.addMouseListener(new tblValidationErrorMouseListener());
    
    }

      public class tblValidationErrorMouseListener extends MouseAdapter {

        @Override
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
     /*   new SwingWorker<Object, Object>() {

            protected Object doInBackground() throws Exception {
                CommonDocumentUtilFunctions.selectSection(ooDocument, matchedSectionName);
                return null;
            }
        }.execute();*/
    }


    /**
     * We scroll to the  point in the document where the error occured during double click
     */
    private void initMouseListeners(){
        this.tblErrors.addMouseListener(new MouseAdapter(){
            @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        /**
                         * match the row with the index of the array of errors
                         */
                        int i = tblErrors.getSelectedRow();
                        StructuralError foundError = structuralErrors.get(i);
                        //call the api to scroll the document
                        pointToErrorInDocument(foundError);
                    }
            }
        });
    }

    Class eventDispatchClass = null;
    Method dispatchMethodObject = null;
    Object dispatchObject = null;
    
    private void dispatchEventToCaller(String sectionName){
        try {

            if (dispatchMethodObject == null ) {
            dispatchMethodObject = this.callerPanel.getClass().getDeclaredMethod("goToSectionPosition", new Class[]{String.class});
            }
            Object[] methodParam = {sectionName};
            dispatchMethodObject.invoke(this.callerPanel, methodParam);
            
         }  catch (IllegalAccessException ex) {
             System.out.println("error : " + ex.getMessage());
             ex.printStackTrace();
            log.error("dispatchEvent :" + ex.getMessage());
        }  catch (IllegalArgumentException ex) {
             System.out.println("error :" + ex.getMessage());
             ex.printStackTrace();
             log.error("dispatchEvent :" + ex.getMessage());
         } catch (InvocationTargetException ex) {
             System.out.println("error :" + ex.getMessage());
             ex.printStackTrace();
            log.error("dispatchEvent :" + ex.getMessage());
         } catch (NoSuchMethodException ex) {
             System.out.println("error :" + ex.getMessage());
             ex.printStackTrace();
            log.error("dispatchEvent :" + ex.getMessage());
         } catch (SecurityException ex) {
             System.out.println("error :" + ex.getMessage());
             ex.printStackTrace();
            log.error("dispatchEvent :" + ex.getMessage());
         } 
    }


    /**
     * Moves the document cursor to the erroneous section and selects it
     * @param foundError
     */
    private void pointToErrorInDocument(StructuralError foundError) {
        //get section name
        try {
        String errorInSection = foundError.childSectionName;
        dispatchEventToCaller(errorInSection);
        } catch (NullPointerException ex) {
            log.error("pointToErrorInDocument : " + ex.getMessage());
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
        /*
        XTextSection xerrorSection = ooDocument.getSection(errorInSection);
        XTextRange sectionRange = xerrorSection.getAnchor();
        XTextViewCursor viewCursor = ooDocument.getViewCursor();
        //move the view cursor range to the start of the seciton range
        //set 'false' to negate any existing selections
        viewCursor.gotoRange(sectionRange.getStart(), false);
        //now select the section range.
        viewCursor.gotoRange(sectionRange, true);
         */
    }

    /**
     * Sets up the table model
     */
    private void initTableModel(){
        //create the table model for the JTable to display structuralerrors
        StructuralErrorTableModel stModel = new StructuralErrorTableModel(this.structuralErrors);
        //some required jtable voodoo as we use a single renderer to change the color
        //of the row instead of setting individual renderers for each cell
        this.tblErrors = new JTable(stModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!c.getBackground().equals(getSelectionBackground())) {
                    //change the background color based on the Boolean value returned by the model
                    Boolean bChecked = (Boolean)getModel().getValueAt(row, 2);
                    c.setBackground(bChecked ? Color.ORANGE : Color.LIGHT_GRAY);
                }
                return c;
            }
        };
        tblErrors.setRowSelectionAllowed(true);
        tblErrors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.scrollTblErrors.setViewportView(tblErrors);
    }

    public void updatePanel(ArrayList<StructuralError> errors) {
        this.structuralErrors = errors;
        StructuralErrorTableModel stModel = new StructuralErrorTableModel(this.structuralErrors);
        this.tblErrors.setModel(stModel);
        initColumnConfig();
    }


    private void initColumnConfig(){
       //set the second column to multiline mode
        TableColumnModel tblColumnModel = this.tblErrors.getColumnModel();
        if (tblColumnModel.getColumnCount() <= 2) {
            return;
        }
        tblColumnModel.getColumn(1).setCellRenderer(new TextAreaRenderer());
       //set column widths
        int tblWidth = 375;
        //give the errors column 60% of the width
         Double ddMsgColWidth = (.6) * tblWidth;
         //set the column widths
        int noColWidth = tblWidth - ddMsgColWidth.intValue();
        tblColumnModel.getColumn(0).setPreferredWidth(noColWidth / 2);
        tblColumnModel.getColumn(1).setPreferredWidth(ddMsgColWidth.intValue())   ;
        tblColumnModel.getColumn(2).setPreferredWidth(noColWidth / 2);

    }

       public void setContainerFrame(Window frm) {
       //set the container frame
        this.containerFrame = frm;
        //add a frame closing listener
        this.containerFrame.addWindowListener( new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent wEvent) {
                //cleanupBookmarks();
                parentWindowClosing();
            }
        });
        }

       public Window getContainerFrame() {
           return this.containerFrame;
       }
       private void parentWindowClosing(){
            //save the panel xml to file
            //first convert the error information to xml
            StructuralErrorTableModel stModel = (StructuralErrorTableModel) this.tblErrors.getModel();
            ArrayList<StructuralError> modelErrors = stModel.getStructuralErrors();
          //  StructuralErrorSerialize seSerialize = new StructuralErrorSerialize(ooDocument.getDocumentURL());
          //  seSerialize.writeErrorsToLog(modelErrors);
            containerFrame.dispose();
        }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblErrors = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        tabErrors = new javax.swing.JTabbedPane();
        scrollTblErrors = new javax.swing.JScrollPane();
        tblErrors = new javax.swing.JTable();
        scrollTblValidationErrors = new javax.swing.JScrollPane();
        tblValidationErrors = new javax.swing.JTable();

        lblErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblErrors.setText("<html><p>Please review the structural errors listed below. Double clicking on an error will take you to the point in the document where the error occurs. </p></html>");

        btnClose.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tabErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N

        tblErrors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrollTblErrors.setViewportView(tblErrors);

        tabErrors.addTab("Structural Errors", scrollTblErrors);

        tblValidationErrors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrollTblValidationErrors.setViewportView(tblValidationErrors);

        tabErrors.addTab("Validation Errors", scrollTblValidationErrors);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblErrors, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
            .addComponent(tabErrors, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblErrors, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabErrors, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClose))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.containerFrame.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblErrors;
    private javax.swing.JScrollPane scrollTblErrors;
    private javax.swing.JScrollPane scrollTblValidationErrors;
    private javax.swing.JTabbedPane tabErrors;
    private javax.swing.JTable tblErrors;
    private javax.swing.JTable tblValidationErrors;
    // End of variables declaration//GEN-END:variables


    private static panelStructuralError2 instance = null;
    private static JDialog floatingFrame = null;

    public static panelStructuralError2 launchFrame(ArrayList<StructuralError> errors, Document xmlErrors, Window callerFrame, Object callerPanel){
                if (instance == null) {
                    instance   = new panelStructuralError2(errors, xmlErrors, callerPanel);
                    instance.setBorder(LineBorder.createGrayLineBorder());
                } else {
                    instance.updatePanel(errors);
                }
                if (floatingFrame == null ) {
                    if (callerFrame == null) {
                        floatingFrame = new JDialog(callerFrame);
                    } else {
                        floatingFrame = new JDialog();
                    }
                    floatingFrame.setTitle("Structural Errors");
                    if (callerFrame != null)
                    floatingFrame.setLocationRelativeTo(null);
                    instance.setContainerFrame(floatingFrame);
                    floatingFrame.add(instance);
                    floatingFrame.setSize(465, 320);
                    floatingFrame.pack();
                }
              //  floatingFrame.getContentPane().add(instance);
                floatingFrame.setAlwaysOnTop(true);
                floatingFrame.setVisible(true);
                floatingFrame.toFront();
                return instance;

    }
}
