/*
 *  Copyright (C) 2012 Africa i-Parliaments
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

package org.bungeni.editor.panels.impl;

import java.io.File;
import java.util.List;

/**
 * This is the main interface for the panel used to contain all the tabbed panels
 * @author Ashok Hariharan
 */
public interface IMainContainerPanel {

    public List<ITabbedPanel> getTabbedPanels();
    public void loadDocumentInPanel(File f);
}
