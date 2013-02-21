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
package org.bungeni.editor.metadata;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class CourtType {

    private String CJ_CourtTypes_ID;
    private String CJ_CourtTypes_Name;
    private String CJ_CourtTypes_Name_E;
     

    public CourtType() {
        CJ_CourtTypes_ID = CJ_CourtTypes_Name = CJ_CourtTypes_Name_E = "";
    }

    public CourtType(String ctID, String ctName, String ctName_E) {
        CJ_CourtTypes_ID = ctID;
        CJ_CourtTypes_Name = ctName;
        CJ_CourtTypes_Name_E = ctName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getCourtTypeName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getCourtTypeName_E();
            }
       return getCourtTypeName_E();
    }
    
    public String getCourtTypeID() {
        return CJ_CourtTypes_ID;
    }
   
    public String getCourtTypeName() {
        return CJ_CourtTypes_Name;
    }
    
    public String getCourtTypeName_E() {
        return CJ_CourtTypes_Name_E;
    }
      
}
