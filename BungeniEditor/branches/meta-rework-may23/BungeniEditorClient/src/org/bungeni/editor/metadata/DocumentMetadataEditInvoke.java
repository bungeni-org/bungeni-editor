/*
 * DocumentMetadataEditInvoke.java
 *
 * Created on October 30, 2007, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Administrator
 */
public class DocumentMetadataEditInvoke extends JButton implements TableCellRenderer {
    
    /** Creates a new instance of DocumentMetadataEditInvoke */
    public DocumentMetadataEditInvoke() {
        
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : ".." );
        return this;
    }
    
}
