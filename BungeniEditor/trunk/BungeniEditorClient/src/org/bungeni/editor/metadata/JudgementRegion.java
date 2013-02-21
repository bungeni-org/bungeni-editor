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
public class JudgementRegion {

    private String CJ_Region_Id;
    private String CJ_Region_Name;
    private String CJ_Region_Name_E;
     

    public JudgementRegion() {
        CJ_Region_Id = CJ_Region_Name = CJ_Region_Name_E = "";
    }

    public JudgementRegion(String jfID, String jfName, String jfName_E) {
        CJ_Region_Id = jfID;
        CJ_Region_Name = jfName;
        CJ_Region_Name_E = jfName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getJudgementRegionName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getJudgementRegionName_E();
            }
       return getJudgementRegionName_E();
    }
    
    public String getJudgementRegionID() {
        return CJ_Region_Id;
    }
   
    public String getJudgementRegionName() {
        return CJ_Region_Name;
    }
    
    public String getJudgementRegionName_E() {
        return CJ_Region_Name_E;
    }
      
}
