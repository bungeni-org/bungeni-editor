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
package org.bungeni.editor.metadata.birzeit;

import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class HistoricalPeriod {

    private String LG_HisPer_ID;
    private String LG_HisPer_Name;
    private String LG_HisPer_Name_E;
     

    public HistoricalPeriod() {
        LG_HisPer_ID = LG_HisPer_Name = LG_HisPer_Name_E = "";
    }

    public HistoricalPeriod(String hpID, String hpName, String hpName_E) {
        LG_HisPer_ID = hpID;
        LG_HisPer_Name = hpName;
        LG_HisPer_Name_E = hpName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getHistoricalPeriodName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getHistoricalPeriodName_E();
            }
       return getHistoricalPeriodName();
    }

    
    public String getHistoricalPeriodID() {
        return LG_HisPer_ID;
    }

   
    public String getHistoricalPeriodName() {
        return LG_HisPer_Name;
    }
    
    public String getHistoricalPeriodName_E() {
        return LG_HisPer_Name_E;
    }
      
}
