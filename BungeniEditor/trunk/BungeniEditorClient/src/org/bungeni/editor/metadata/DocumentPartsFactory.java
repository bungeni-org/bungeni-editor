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

package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Returns the list of available language codes in the system
 * 
 * @author Ashok Hariharan
 */
public  class DocumentPartsFactory {

        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentPartsFactory.class.getName());

     /**
     * This the internal array of available langauges
     */
    private static List<DocumentPart> document_parts =  new ArrayList<DocumentPart>(){
        {
        //this is the fallback default language
        new DocumentPart("main", "Main");
        }
    };

    /**
     * Returns the list of available language in the system as a "lightweight" language description object
     * @return
     */
    public static DocumentPart[] getAvailableDocumentParts(){
       //get the set of active languages from the db
       fetchAvailableDocumentParts();
       //sort only when the array has more than one element
       if (document_parts.size() > 1 ) {
            Collections.sort(document_parts);
        }
       return document_parts.toArray(new DocumentPart[document_parts.size()]);
    }

    private static void fetchAvailableDocumentParts(){
        Element doctypeElem = null;
        List<DocumentPart> list_docparts = new ArrayList<DocumentPart>(0);

        try {
            doctypeElem = DocTypesReader.getInstance().getDocTypeByName(BungeniEditorPropertiesHelper.getCurrentDocType());
        } catch (JDOMException ex) {
            log.error("Error while getting doctype element" , ex);
        }
        if (null != doctypeElem) {
            List<Element> listParts =  DocTypesReader.getInstance().getPartsForDocType(doctypeElem);
            for (Element partElem : listParts) {
                DocumentPart dp = new DocumentPart(
                        partElem.getAttributeValue("name"),
                        CommonEditorXmlUtils.getLocalizedChildElementValue(
                            BungeniEditorPropertiesHelper.getLangAlpha3Part2(),
                            partElem,
                            "title"
                            )
                       );
                list_docparts.add(dp);
            }
        }
         //if some locales were retrieved ... replace the cache of available locales with the
          //retrieved one
          if (!list_docparts.isEmpty()) {
              document_parts = list_docparts;
          }
          //else leave the default locale in place
    }


}
