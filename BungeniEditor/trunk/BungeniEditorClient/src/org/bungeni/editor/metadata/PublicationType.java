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

import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class PublicationType {

    private String LG_Type_ID;
    private String LG_Type_Name;
    private String LG_Type_Name_E;
    private String LG_Type_Name_AN;
     

    public PublicationType() {
        LG_Type_ID  = LG_Type_Name = LG_Type_Name_E = LG_Type_Name_AN = "";
    }

    public PublicationType(String ptID, String ptName, String ptName_E, String ptName_AN) {
        LG_Type_ID = ptID;
        LG_Type_Name = ptName;
        LG_Type_Name_E = ptName_E;
        LG_Type_Name_AN = ptName_AN;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getPublicationTypeName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getPublicationTypeName_E();
            }
       return getPublicationTypeName();
    }

    
    public String getPublicationTypeID() {
        return LG_Type_ID;
    }

   
    public String getPublicationTypeName() {
        return LG_Type_Name;
    }
    
    public String getPublicationTypeName_E() {
        return LG_Type_Name_E;
    }
     
     public String getPublicationTypeName_AN() {
        return LG_Type_Name_AN;
    }
      
}
