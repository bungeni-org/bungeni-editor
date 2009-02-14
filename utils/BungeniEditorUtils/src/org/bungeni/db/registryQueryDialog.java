/*
 * registryQueryDialog.java
 *
 * Created on August 31, 2007, 3:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.db;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class registryQueryDialog {
   private static org.apache.log4j.Logger log = Logger.getLogger(registryQueryDialog.class.getName());

    private String registryQuery;
    private String dialogTitle;
    private boolean applyClicked=false;
    private JDialog parentDlg = null;
    JTable dataTable;
    JFrame parentFrame = null;
    JDialog dataDialog;
    JLabel lblMessage;
    QueryResults theResults;
    private int columnCount=0;
    private int rowCount=0;
    private HashMap<String,String> selectedRow = new HashMap<String,String>();
    /** Creates a new instance of registryQueryDialog */
    public registryQueryDialog(String title, String query, JDialog parent) {
        
        registryQuery =  query;
        dialogTitle = title;
        parentDlg = parent;
    }
    
    public registryQueryDialog(String title, String query, JFrame parent) {
        
        registryQuery =  query;
        dialogTitle = title;
        parentFrame = parent;
    }
    
    private void btnSelectMP_Clicked(java.awt.event.ActionEvent evt){
       int nRow =  dataTable.getSelectedRow();
       log.debug("Select clicked: row : " + nRow);
       if (nRow == -1 ) {
          lblMessage.setForeground(new Color(255,0,0));
           lblMessage.setText("You must select a Row");
           return;
       }
     
       String[] theColumns = theResults.getColumns();
       selectedRow = new HashMap<String,String>();
       log.debug("adding column info");
      for (int i=0 ; i < columnCount;  i++ ) {
           log.debug("adding data to map = " + (String) dataTable.getValueAt(nRow, i));
            selectedRow.put(theColumns[i], 
                    (String)dataTable.getValueAt(nRow, i));
      }
     //set the MP data in parent list box
       applyClicked = true;
    }
 
  private void btnClosedataDialog_Clicked(java.awt.event.ActionEvent evt){
      applyClicked = false;
      dataDialog.dispose();
  }
 
  public boolean applyClicked() {
      return applyClicked;
  }
  public HashMap<String,String> getData() {
      return selectedRow;
  }
  public void show () {
      HashMap<String,String> registryMap = BungeniRegistryFactory.fullConnectionString();  
      BungeniClientDB dbReg = new BungeniClientDB(registryMap);
      dbReg.Connect();
      HashMap<String,Vector<Vector<String>>> results = dbReg.Query(registryQuery);
      dbReg.EndConnect();
      theResults = new QueryResults(results);
      if (theResults.hasResults()) {
          String[] columns = theResults.getColumns();
          
          columnCount = columns.length;
          rowCount = theResults.theResults().size();
          
          Vector<String> vMpColumns = new Vector<String>();
          Vector<Vector<String>> vMps = new Vector<Vector<String>>();
          Collections.addAll(vMpColumns, columns);
          vMps = theResults.theResults();
          
           DefaultTableModel dtm = new DefaultTableModel(vMps,vMpColumns);
             dataTable = new JTable() {
                 public boolean isCellEditable(int rowIndex, int vColIndex) {
                    return false;
                    }
             };
             dataTable.setModel(dtm);
             dataTable.setRowSelectionAllowed(true);
             dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
             //dataTable.getSelectionModel().addListSelectionListener(new tblMembersOfParliamentListRowListener());

             JScrollPane sp = new JScrollPane(dataTable);
             sp.setPreferredSize(new Dimension(400,200));
             JPanel panel = new JPanel(new FlowLayout());
             panel.setPreferredSize(new Dimension(400,300));
             panel.add(sp);
             lblMessage = new JLabel();
             lblMessage.setPreferredSize(new Dimension(400, 20));
             lblMessage.setText("Please make a selection");

             JButton btnSelectMp = new JButton();
             JButton btnClose = new JButton();
             btnClose.setText("Close");
             btnClose.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnClosedataDialog_Clicked(evt);
                    }
                });
             btnSelectMp.setText("Select Data");
             btnSelectMp.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnSelectMP_Clicked(evt);
                    }
                });
             panel.add(lblMessage);
             panel.add(btnSelectMp);
             panel.add(btnClose);
             if (parentFrame == null) {
                dataDialog = new JDialog(parentDlg);
                dataDialog.setLocationRelativeTo(parentDlg);
             } else {
                dataDialog = new JDialog(parentFrame);
                dataDialog.setLocationRelativeTo(parentFrame);
                 
             }
             dataDialog.setTitle(dialogTitle);
             dataDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
             dataDialog.setPreferredSize(new Dimension(420, 300));
             dataDialog.getContentPane().add(panel);
             dataDialog.pack();
             dataDialog.setModal(true);
             dataDialog.setVisible(true);
             dataDialog.setAlwaysOnTop(true);
    
    }
    
  }  
}
