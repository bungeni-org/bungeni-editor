/*
 *  Copyright (C) 2010 undesa
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
package org.bungeni.editor.locales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.LocalesReader;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Provides a list of supported locales by the editor
 * @author Ashok Hariharan
 *
 */
public class BungeniEditorLocalesFactory {

       private static Logger log = Logger.getLogger(BungeniEditorLocalesFactory.class.getName());

    /**
     * This the internal array of available locales
     */
    private static List<BungeniEditorLocale> locales =  new ArrayList<BungeniEditorLocale>(){
        {
        //this is the fallback default locale
        new BungeniEditorLocale("en", "US");
        }
    };

    /**
     * Returns the list of available locales in the system as a "lightweight" locale description object
     * @return
     */
    public static BungeniEditorLocale[] getAvailableLocales(){
       //get the set of active locales from the db
       fetchAvailableLocales();
       //sort only when the array has more than one element
       if (locales.size() > 1 ) {
            Collections.sort(locales);
        }
       return locales.toArray(new BungeniEditorLocale[locales.size()]);
    }



    private static void fetchAvailableLocales(){
         List<BungeniEditorLocale> list_locales = new ArrayList<BungeniEditorLocale>(0);
         List<Element> localeElements = null;
        try {
            localeElements = LocalesReader.getInstance().getLocales();
        } catch (JDOMException ex) {
            log.error("getting locales got jdomexception", ex);
        }
        if (null != localeElements) {
            for (Element localeElem : localeElements) {
                BungeniEditorLocale aLocale = new BungeniEditorLocale(
                        localeElem.getAttributeValue("lang"),
                        localeElem.getAttributeValue("country")
                        );
                      list_locales.add(aLocale);
            }
        }
         /**
          BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
          db.Connect();
          QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_LOCALES());
          db.EndConnect();
         
          if (qr.hasResults()) {
              String[] countryCodes = qr.getSingleColumnResult("COUNTRY_CODE");
              String[] langCodes = qr.getSingleColumnResult("LANG_CODE_2");
              for (int i=0; i < langCodes.length ; i++ ) {
                  BungeniEditorLocale aLocale = new BungeniEditorLocale(langCodes[i], countryCodes[i]);
                  list_locales.add(aLocale);
              }
          }
          ***/
          //if some locales were retrieved ... replace the cache of available locales with the 
          //retrieved one
          if (!list_locales.isEmpty()) {
              locales = list_locales;
          }
          //else leave the default locale in place 
    }

}
