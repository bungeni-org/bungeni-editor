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

package org.bungeni.ooo.transforms.impl;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniTransformationTargets {

   private static final ResourceBundle bundle = ResourceBundle.getBundle("org/bungeni/ooo/transforms/impl/Bundle");


    public static final HashMap<String, BungeniTransformationTarget> __TRANSFORMATION_TARGETS__ = new HashMap<String, BungeniTransformationTarget>() {

        {
            put("PDF", new BungeniTransformationTarget("PDF", bundle.getString("PDF_Format"), "pdf", "org.bungeni.ooo.transforms.loadable.PDFTransform"));
            put("HTML", new BungeniTransformationTarget("HTML", bundle.getString("HTML_Format"), "html", "org.bungeni.ooo.transforms.loadable.HTMLTransform"));
            put("AN-XML", new BungeniTransformationTarget("AN-XML", bundle.getString("AN_Format"), "xml", "org.bungeni.ooo.transforms.loadable.AnXmlTransform"));
            put("DOC", new BungeniTransformationTarget("DOC", bundle.getString("MS_Word_97_Format"), "xml", "org.bungeni.ooo.transforms.loadable.MSWordTransform"));
        }
    };
    public static  final HashMap<String, exportDestination> __EXPORT_DESTINATIONS__ = new HashMap<String, exportDestination>() {

        {
            put("ToBungeniServer", new exportDestination("ToBungeniServer", bundle.getString("Export_into_a_Bungeni_Server")));
            put("FTP", new exportDestination("FTP", bundle.getString("Export_using_FTP")));
            put("FileSystem", new exportDestination("FileSystem", bundle.getString("Folder_on_your_computer")));
        }
    };

    public static final HashMap<String,BungeniTransformationTarget> getTransformationTargets() {
        return __TRANSFORMATION_TARGETS__;
    }

    public static final HashMap<String,exportDestination> getExportDestinations() {
        return __EXPORT_DESTINATIONS__;
    }

}
