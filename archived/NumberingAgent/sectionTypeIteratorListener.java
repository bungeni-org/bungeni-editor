/*
 *  Copyright (C) 2012 windows
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

// !+ (rm, feb 2012) - refactored code to use bungeniOdfDom
package org.bungeni.editor.panels.loadable;

import java.util.ArrayList;
import org.bungeni.editor.providers.IBungeniSectionIteratorListener;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.bungeni.utils.BungeniBNode;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;

public class sectionTypeIteratorListener implements IBungeniSectionIteratorListener {

    public ArrayList<String> sectionsMatchingType = new ArrayList<String>(0);
    private String inputSectionType;
    private OdfDocument odfDocument ;

    sectionTypeIteratorListener(String input, OdfDocument odfDocument) {
        this.inputSectionType = input;
        this.odfDocument = odfDocument ;
    }

    public boolean iteratorCallback(BungeniBNode bNode) {
        String foundsectionName = bNode.getName();

        BungeniOdfSectionHelper sectionHelper = new BungeniOdfSectionHelper(odfDocument) ;
        TextSectionElement foundsectionType = sectionHelper.getSection(foundsectionName);
        if (foundsectionType != null) {
            if (sectionHelper.getSectionType(foundsectionType).equals(inputSectionType)) {
                sectionsMatchingType.add(foundsectionName);
            }
        }
        return true;
    }
}
