/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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

package org.bungeni.editor.selectors.general;

import java.awt.Component;
import org.bungeni.editor.selectors.BaseMetadataContainerPanel;

/**
 * This is a generic Main container panel
 * @author Ashok Hariharan
 */
public class Main extends BaseMetadataContainerPanel {

    public Main() {
        super();
    }

    @Override
    public Component getPanelComponent() {
        return this;
    }

}
