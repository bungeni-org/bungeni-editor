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
public class Category_Basic {

    private String LG_Basic_ID;
    private String LG_Basic_Name;
     

    public Category_Basic() {
        LG_Basic_ID  = LG_Basic_Name = "";
    }

    public Category_Basic(String cbID, String cbName) {
        LG_Basic_ID = cbID;
        LG_Basic_Name = cbName;
    }

    
    public String getCategoryBasicID() {
        return LG_Basic_ID;
    }
   
    public String getCategoryBasicName() {
        return LG_Basic_Name;
    }
      
}
