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

package org.bungeni.utils;

import org.bungeni.editor.config.BungeniEditorProperties;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Ashok
 */
public class CommonBungeniTreeFunctions {

    public static String IMAGE_LOCATION = BungeniEditorProperties.getEditorProperty("iconPath");

    public static ImageIcon treeMinusIcon(){
        return loadIcon("treeMinus.gif");
    }

    public static ImageIcon treePlusIcon(){
        return loadIcon("treePlus.gif");
    }

    public static ImageIcon loadIcon (String iconName) {
               String imgLocation = IMAGE_LOCATION+ "/" + iconName;
                URL imageURL = CommonBungeniTreeFunctions.class.getResource(imgLocation);
                 //Create and initialize the icon.
                if (imageURL != null)                     //image found
                    return new ImageIcon(imageURL, "");
                else
                    return null;
    }


}
