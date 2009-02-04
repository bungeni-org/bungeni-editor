/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.section.refactor.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author ashok
 */

public class OdfJDomTreeCellRenderer extends DefaultTreeCellRenderer {

    //colors for tree items
    private final Color elementColor = new Color(0, 0, 128);
    private final Color textColor = new Color(0, 128, 0);

    //remove icons
    public OdfJDomTreeCellRenderer() {
    //    setOpenIcon(new ImageIcon("open.gif"));
    //    setClosedIcon(new ImageIcon("closed.gif"));
    //    setLeafIcon(new ImageIcon("leaf.gif"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        OdfJDomTreeNode adapterNode = (OdfJDomTreeNode)value;
        /*
        if(adapterNode.node.isRootElement()) {
            value = adapterNode.node.getName();
        } else if(adapterNode.node.getChildren().size() > 0) {
            value = adapterNode.node.getName();
        } else {
            value = adapterNode.node.getName() +" ["+adapterNode.node.getTextTrim()+"]";
        }*/
        value = adapterNode.toString();

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if(!selected) {
            if(adapterNode.node.getTextTrim().length() == 0) {
                setForeground(elementColor);
            } else {
                setForeground(textColor);
            }
        }

        return this;
    }
}


