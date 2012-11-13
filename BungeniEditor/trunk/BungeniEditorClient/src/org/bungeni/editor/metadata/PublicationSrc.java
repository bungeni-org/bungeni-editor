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

/**
 *
 * @author bzuadmin
 */
public class PublicationSrc {

    private String LG_Src_ID;
    private String LG_Src_Name;
    private String LG_Src_Name_AN;

    public PublicationSrc() {
        LG_Src_ID = LG_Src_Name = LG_Src_Name_AN = "";
    }

    public PublicationSrc(String psID, String psName, String psName_AN) {
        LG_Src_ID = psID;
        LG_Src_Name = psName;
        LG_Src_Name_AN = psName_AN;
    }

    @Override
    public String toString() {
        return getPublicationSrcName();
    }

    
    public String getPublicationSrcID() {
        return LG_Src_ID;
    }

   
    public String getPublicationSrcName() {
        return LG_Src_Name;
    }
    
     public String getPublicationSrcName_AN() {
        return LG_Src_Name_AN;
    }
}
