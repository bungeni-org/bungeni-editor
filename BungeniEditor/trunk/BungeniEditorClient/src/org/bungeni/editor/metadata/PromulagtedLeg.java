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
public class PromulagtedLeg {

    private String LG_Body_ID;
    private String LG_Body_Name;
    private String LG_Body_Name_E;
    
     

    public PromulagtedLeg() {
        LG_Body_ID  = LG_Body_Name = LG_Body_Name_E = "";
    }

    public PromulagtedLeg(String plID, String plName, String plName_E) {
        LG_Body_ID = plID;
        LG_Body_Name = plName;
        LG_Body_Name_E = plName_E;
    }

     @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getPromulagtedLegName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getPromulagtedLegName_E();
            }
       return getPromulagtedLegName_E();
    }
     
    public String getPromulagtedLegID() {
        return LG_Body_ID;
    }
   
    public String getPromulagtedLegName() {
        return LG_Body_Name.trim();
    }
      
     public String getPromulagtedLegName_E() {
        return LG_Body_Name_E.trim();
    }
}
