/*
 *  Copyright (C) 2010 africa i-parliaments, undesa
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

import java.util.Locale;

/**
 * This class provides a wrapper on the locale object -- and is used as the
 * object  in the combobox selector for locale
 * @author Ashok Hariharan
 *
 */
public class BungeniEditorLocale implements Comparable{

    Locale locale;

    public BungeniEditorLocale(String lang, String region){
        locale = new Locale(lang, region);
    }

    public BungeniEditorLocale (Locale locale) {
        this.locale = locale;
    }


    @Override
    public String toString(){
        return locale.getDisplayName();
    }

    public Locale getLocale(){
        return locale;
    }

    public int compareTo(Object t) {
        BungeniEditorLocale lt = (BungeniEditorLocale) t;
        return locale.getDisplayName().compareTo(lt.getLocale().getDisplayName());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object t){
        BungeniEditorLocale lt = (BungeniEditorLocale) t;
        if (getLocale().equals(lt.getLocale())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.locale != null ? this.locale.hashCode() : 0);
        return hash;
    }


}
