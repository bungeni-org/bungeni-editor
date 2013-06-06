/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
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
public class Domains {

    private String CJ_Domains_ID;
    private String CJ_Domains_Name;
    private String CJ_Domains_Name_E;
     

    public Domains() {
        CJ_Domains_ID = CJ_Domains_Name = CJ_Domains_Name_E = "";
    }

    public Domains(String dID, String dName, String dName_E) {
        CJ_Domains_ID = dID;
        CJ_Domains_Name = dName;
        CJ_Domains_Name_E = dName_E;
    }

    @Override
    public String toString() {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
                 return getDomainName();
            }
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                 return getDomainName_E();
            }
       return getDomainName_E();
    }
    
    public String getDomainID() {
        return CJ_Domains_ID;
    }
   
    public String getDomainName() {
        return CJ_Domains_Name;
    }
    
    public String getDomainName_E() {
        return CJ_Domains_Name_E;
    }
      
}
