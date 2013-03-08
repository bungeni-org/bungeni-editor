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

import java.util.List;
import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class LitigationType {

    private String CJ_CaseTypes_NameAlternate;
    private String CJ_CaseTypes_NameAlternate_E;
     

    public LitigationType() {
        CJ_CaseTypes_NameAlternate = CJ_CaseTypes_NameAlternate_E = "";
    }

    public LitigationType(String lgName, String lgName_E) {
        CJ_CaseTypes_NameAlternate = lgName;
        CJ_CaseTypes_NameAlternate_E = lgName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getLitigationTypeName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getLitigationTypeName_E();
            }
       return getLitigationTypeName_E();
    }
   
    public String getLitigationTypeName() {
        return CJ_CaseTypes_NameAlternate;
    }
    
    public String getLitigationTypeName_E() {
        return CJ_CaseTypes_NameAlternate_E;
    }
      
}
