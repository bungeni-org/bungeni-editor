/*
 *  Copyright (C) 2011 undesa
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.noa;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniNoaTabbedPane {

    private static BungeniNoaTabbedPane thisTabbedPane = null;

    private JTabbedPane tabbedPane = null;

    private BungeniNoaTabbedPane(){
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(300,0)) ;
        tabbedPane.setTabLayoutPolicy(JTabbedPane.HORIZONTAL);
    }           
    
    public static BungeniNoaTabbedPane getInstance(){
        if (null == thisTabbedPane) {
            thisTabbedPane = new BungeniNoaTabbedPane();
        }
        return thisTabbedPane;
    }

    public JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public boolean setActiveTab(JPanel panel){
        if (!this.tabbedPane.getSelectedComponent().equals(panel)) {
            this.tabbedPane.setSelectedComponent(panel);
            return true;
        } else {
            return false;
        }
    }

    public void updateTitleForActiveTab(){
        
    }
}
