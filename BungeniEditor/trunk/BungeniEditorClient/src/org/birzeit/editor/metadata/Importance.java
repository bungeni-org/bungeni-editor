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
public class Importance {

    private String CJ_Importance_ID;
    private String CJ_Importance_Name;
     

    public Importance() {
        CJ_Importance_ID = CJ_Importance_Name = "";
    }

    public Importance(String impID, String impName) {
        CJ_Importance_ID = impID;
        CJ_Importance_Name = impName;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getImportanceName();
            }
           
       return getImportanceName();
    }
    
    public String getImportanceID() {
        return CJ_Importance_ID;
    }
   
    public String getImportanceName() {
        return CJ_Importance_Name;
    }
    
      
}
