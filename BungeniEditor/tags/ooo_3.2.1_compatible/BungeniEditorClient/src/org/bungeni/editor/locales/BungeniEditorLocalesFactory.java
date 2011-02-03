/*
 *  Copyright (C) 2010 undesa
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.locales;

import java.util.Arrays;

/**
 * Provides a list of supported locales by the editor
 * @author Ashok Hariharan
 *
 */
public class BungeniEditorLocalesFactory {

    private static BungeniEditorLocale[] locales = {
        new BungeniEditorLocale("en", "US"),
        new BungeniEditorLocale("en", "KE"),
        new BungeniEditorLocale("fr", "FR"),
        new BungeniEditorLocale("it", "IT"),
    };

    public static BungeniEditorLocale[] getAvailableLocales(){
       Arrays.sort(locales);
       return locales;
    }


    public static void main(String[] args) {
        for (BungeniEditorLocale l : getAvailableLocales()) {
            System.out.println(l);
        }
    }
}
