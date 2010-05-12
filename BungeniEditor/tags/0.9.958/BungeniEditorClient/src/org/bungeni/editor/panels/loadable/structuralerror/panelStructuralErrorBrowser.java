package org.bungeni.editor.panels.loadable.structuralerror;

import com.thoughtworks.xstream.XStream;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import org.apache.log4j.Logger;


/**
 *
 * @author undesa
 */
public class panelStructuralErrorBrowser extends javax.swing.JPanel {
    private String sourceDocURL;
    private JFrame callerFrame ;
    private JDialog containerFrame;
    private Object callerPanel ;
    private panelStructuralError structuralErrorPanel = null;

        private static org.apache.log4j.Logger log            =
        Logger.getLogger(panelStructuralErrorBrowser.class.getName());


    /** Creates new form panelStructuralErrorBrowser */
    public panelStructuralErrorBrowser(String sourceDocURL, JFrame callerFrame, Object callerPanel) {
        this.sourceDocURL = sourceDocURL;
        this.callerFrame = callerFrame;
        this.callerPanel = callerPanel;
        initComponents();
        initModel();
    }

    public void updatePanel(String  sourceDocURL) {
        this.sourceDocURL = sourceDocURL;
        initModel();
    }

    class ErrorLogFile implements Comparable {
        File logFile ;
        Date logTimestamp;

        public ErrorLogFile(File fFile, Date logDate) {
            this.logFile = fFile;
            this.logTimestamp = logDate;
        }

        public int compareTo(Object o) {
            ErrorLogFile that;
            that = (ErrorLogFile) o;
            return this.logTimestamp.compareTo(that.logTimestamp);
        }

        @Override
        public String toString(){
            return this.logTimestamp.toString();
        }
    }


    public void setContainerFrame (JDialog containerFrame) {
        this.containerFrame = containerFrame;
        this.containerFrame.addWindowListener( new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent wEvent) {
                //cleanupBookmarks();
                parentWindowClosing();
            }
        });
    }

    private void parentWindowClosing(){
        if (this.structuralErrorPanel != null) {
            Window frm = this.structuralErrorPanel.getContainerFrame();
            if (frm.isShowing()) {
                frm.dispose();
            }
        }
        this.containerFrame.dispose();
    }
    
    private void initModel(){
        StructuralErrorSerialize errorSerialize = new StructuralErrorSerialize(this.sourceDocURL);
        File[] fLogFiles = errorSerialize.browseErrorLogFiles();
        ArrayList<ErrorLogFile> listErrorLogs = new ArrayList<ErrorLogFile>(0);
        DefaultListModel logFilesModel  = new DefaultListModel();
        for (File file : fLogFiles) {
            String fName = file.getName();
            int nIndex = fName.indexOf(".xml");
            String filenamePrefix = fName.substring(0, nIndex);
            String filenameString = "";
            try {
                Date fileNameDate = errorSerialize.timestampFileNameDateFormat().parse(filenamePrefix);
                filenameString = fileNameDate.toString();
                ErrorLogFile elf = new ErrorLogFile(file, fileNameDate);
                listErrorLogs.add(elf);
            } catch (ParseException ex) {
               //will never occurs since the browseErorrLogFiles already traps it
            }
        }
        Collections.sort(listErrorLogs);
        for (ErrorLogFile errorLogFile : listErrorLogs) {
            logFilesModel.addElement(errorLogFile);
        }
        this.listErrorLog.setModel(logFilesModel);
        listErrorMouseListener mouseListener = new listErrorMouseListener(this.listErrorLog);
        this.listErrorLog.addMouseListener(mouseListener);
    }

    private void launchErrorLogView(ErrorLogFile elfFile) {
        try {
            FileReader freader = new FileReader(elfFile.logFile);
            XStream xst = new XStream();
            StructuralErrorHelper.structuralErrorAlias(xst);
            StructuralErrorLog errorLog = (StructuralErrorLog) xst.fromXML(freader);
            if (structuralErrorPanel == null) {
                structuralErrorPanel = panelStructuralError.launchFrame(errorLog.structuralErrors, callerFrame, callerPanel);
            }
            else if (structuralErrorPanel.getContainerFrame().isShowing() == false) {
                structuralErrorPanel = panelStructuralError.launchFrame(errorLog.structuralErrors, containerFrame, callerPanel);
            }
            else
                 structuralErrorPanel.updatePanel(errorLog.structuralErrors);
        } catch (FileNotFoundException ex) {
           log.error("launchErrorLogView :" , ex);
        }

    }

    class listErrorMouseListener extends MouseAdapter {
        JList errorList;
        public listErrorMouseListener(JList l) {
            errorList = l;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int nindex = errorList.locationToIndex(e.getPoint());
                if (nindex != -1) {
                    final ErrorLogFile elfFile = (ErrorLogFile) errorList.getSelectedValue();
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            launchErrorLogView(elfFile);
                        }
                    });
    
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listErrorLog = new javax.swing.JList();
        lblErrors = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();

        listErrorLog.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        listErrorLog.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listErrorLog);

        lblErrors.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lblErrors.setText("Browse the Error Log");

        btnCancel.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        btnCancel.setText("Close");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(120, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblErrors, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblErrors)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        parentWindowClosing();
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblErrors;
    private javax.swing.JList listErrorLog;
    // End of variables declaration//GEN-END:variables

    private static panelStructuralErrorBrowser instance = null;
    private static JDialog floatingFrame = null;

    public static panelStructuralErrorBrowser launchFrame(String sourceUrl, JFrame callerFrame, Object callerPanel) {
                try {
                if (instance == null ) {
                        instance    = new panelStructuralErrorBrowser(sourceUrl, callerFrame, callerPanel);
                        instance.setBorder(LineBorder.createGrayLineBorder());
                } else {
                    instance.updatePanel(sourceUrl);
                }

                if (floatingFrame == null) {
                    if (callerFrame == null) {
                        floatingFrame = new JDialog(callerFrame);
                    } else {
                        floatingFrame = new JDialog();
                    }
                    floatingFrame.setTitle("Browse Error Archive");
                    floatingFrame.getContentPane().add(instance);
                    floatingFrame.setAlwaysOnTop(true);
                    if (callerFrame != null)
                        floatingFrame.setLocationRelativeTo(null);
                    instance.setContainerFrame(floatingFrame);
                    floatingFrame.add(instance);
                    floatingFrame.setSize(315, 226);
                    floatingFrame.pack();
                }
                floatingFrame.setVisible(true);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    log.error("Error launching error browser", ex);
                } finally {
                return instance;
                }
    }

}
