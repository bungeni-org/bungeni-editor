/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.birzeit.editor.metadata;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class City {

    private String CJ_Cities_ID;
    private String CJ_Cities_Name;
    private String CJ_Cities_Name_E;
     

    public City() {
        CJ_Cities_ID = CJ_Cities_Name = CJ_Cities_Name_E = "";
    }

    public City(String dID, String dName, String dName_E) {
        CJ_Cities_ID = dID;
        CJ_Cities_Name = dName;
        CJ_Cities_Name_E = dName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getCityName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getCityName_E();
            }
       return getCityName_E();
    }
    
    public String getCityID() {
        return CJ_Cities_ID;
    }
   
    public String getCityName() {
        return CJ_Cities_Name;
    }
    
    public String getCityName_E() {
        return CJ_Cities_Name_E;
    }
      
}
