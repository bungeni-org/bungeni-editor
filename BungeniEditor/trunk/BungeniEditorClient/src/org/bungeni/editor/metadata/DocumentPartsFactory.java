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

package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;

/**
 * Returns the list of availabel language codes in the system
 * 
 * @author Ashok Hariharan
 */
public  class DocumentPartsFactory {

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
          BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
          db.Connect();
          QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_DOCUMENT_PARTS(BungeniEditorPropertiesHelper.getCurrentDocType()));
          db.EndConnect();
          List<DocumentPart> list_parts = new ArrayList<DocumentPart>(0);
          if (qr.hasResults()) {
              String[] docParts = qr.getSingleColumnResult("DOC_PART");
              String[] partNames = qr.getSingleColumnResult("PART_NAME");
              for (int i=0; i < docParts.length ; i++ ) {
                  DocumentPart aPart = new DocumentPart( docParts[i], partNames[i]);
                  list_parts.add(aPart);
              }
          }
          //if some locales were retrieved ... replace the cache of available locales with the
          //retrieved one
          if (!list_parts.isEmpty()) {
              document_parts = list_parts;
          }
          //else leave the default locale in place
    }


}
