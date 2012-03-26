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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.editor.config.LanguageCodesReader;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Returns the list of availabel language codes in the system
 * 
 * @author Ashok Hariharan
 */
public class LanguageCodesFactory {

    /**
     * This the internal array of available langauges
     */
    private static List<LanguageCode> language_codes = new ArrayList<LanguageCode>() {

        {
            //this is the fallback default language
            new LanguageCode("eng", "en", "English");
        }
    };

    /**
     * Returns the list of available language in the system as a "lightweight" language description object
     * @return
     */
    public static LanguageCode[] getAvailableLanguageCodes() {
        //get the set of active languages from the db
        fetchAvailableLanguageCodes();
        //sort only when the array has more than one element
        if (language_codes.size() > 1) {
            Collections.sort(language_codes);
        }
        return language_codes.toArray(new LanguageCode[language_codes.size()]);
    }

    private static void fetchAvailableLanguageCodes() {
        List<Element> langCodeElements = null;
        List<LanguageCode> list_langs = new ArrayList<LanguageCode>(0);
        try {
            langCodeElements = LanguageCodesReader.getInstance().getLanguageCodeElements();
        } catch (JDOMException ex) {
            Logger.getLogger(LanguageCodesFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (langCodeElements != null) {
            for (Element langCode : langCodeElements) {
                String sAlpha3 = langCode.getAttributeValue("alpha3");
                String sAlpha2 = langCode.getAttributeValue("alpha2");
                String sName = LanguageCodesReader.getInstance().getLanguageName(langCode, "en");
                list_langs.add(new LanguageCode(sAlpha3, sAlpha2, sName));
            }
        }
        /**
        BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        db.Connect();
        QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_LANGUAGE_CODES());
        db.EndConnect();
        List<LanguageCode> list_langs = new ArrayList<LanguageCode>(0);
        if (qr.hasResults()) {
        String[] langCodes = qr.getSingleColumnResult("LANG_CODE");
        String[] langNames = qr.getSingleColumnResult("LANG_NAME");
        String[] langCodes2 = qr.getSingleColumnResult("LANG_CODE_2");
        for (int i=0; i < langCodes.length ; i++ ) {
        LanguageCode aLanguage = new LanguageCode(langCodes[i], langCodes2[i], langNames[i]);
        list_langs.add(aLanguage);
        }
        } **/
        //if some locales were retrieved ... replace the cache of available locales with the
        //retrieved one
        if (!list_langs.isEmpty()) {
            language_codes = list_langs;
        }
        //else leave the default locale in place
    }
}
