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

/**
 * Returns the set of available country codes in the system
 * @author Ashok Hariharan
 */
public  class CountryCodesFactory {

     /**
     * This the internal array of available locales
     */
    private static List<CountryCode> country_codes =  new ArrayList<CountryCode>(){
        {
        //this is the fallback default locale
        new CountryCode("US", "United States");
        }
    };

    /**
     * Returns the list of available countries in the system as a "lightweight" country description object
     * @return
     */
    public static CountryCode[] getAvailableCountryCodes(){
       //get the set of active locales from the db
       fetchAvailableCountryCodes();
       //sort only when the array has more than one element
       if (country_codes.size() > 1 ) {
            Collections.sort(country_codes);
        }
       return country_codes.toArray(new CountryCode[country_codes.size()]);
    }

    private static void fetchAvailableCountryCodes(){
          BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
          db.Connect();
          QueryResults qr = db.QueryResults(SettingsQueryFactory.Q_FETCH_COUNTRY_CODES());
          db.EndConnect();
          List<CountryCode> list_countries = new ArrayList<CountryCode>(0);
          if (qr.hasResults()) {
              String[] countryCodes = qr.getSingleColumnResult("COUNTRY_CODE");
              String[] countryNames = qr.getSingleColumnResult("COUNTRY_NAME");
              for (int i=0; i < countryCodes.length ; i++ ) {
                  CountryCode aCountry = new CountryCode(countryCodes[i], countryNames[i]);
                  list_countries.add(aCountry);
              }
          }
          //if some locales were retrieved ... replace the cache of available locales with the
          //retrieved one
          if (!list_countries.isEmpty()) {
              country_codes = list_countries;
          }
          //else leave the default locale in place
    }


}
