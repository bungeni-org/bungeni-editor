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
public class Family {

    private String LG_Family_ID;
    private String LG_Family_Name;
    private String LG_Family_Name_E;
     

    public Family() {
        LG_Family_ID = LG_Family_Name = LG_Family_Name_E = "";
    }

    public Family(String fmID, String fmName, String fmName_E) {
        LG_Family_ID = fmID;
        LG_Family_Name = fmName;
        LG_Family_Name_E = fmName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getFamilyName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getFamilyName_E();
            }
       return getFamilyName();
    }
    
    public String getFamilyID() {
        return LG_Family_ID;
    }
   
    public String getFamilyName() {
        return LG_Family_Name;
    }
    
    public String getFamilyName_E() {
        return LG_Family_Name_E;
    }
      
}
