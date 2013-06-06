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
public class Category {

    private String LG_Category_ID;
    private String LG_Category_Name;
     

    public Category() {
        LG_Category_ID  = LG_Category_Name = "";
    }

    public Category(String cID, String cName) {
        LG_Category_ID = cID;
        LG_Category_Name = cName;
    }

    
    public String getCategoryID() {
        return LG_Category_ID;
    }
   
    public String getCategoryName() {
        return LG_Category_Name;
    }
      
}
