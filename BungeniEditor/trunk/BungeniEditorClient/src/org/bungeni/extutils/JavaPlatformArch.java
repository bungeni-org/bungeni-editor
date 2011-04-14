/*
 *  Copyright (C) 2011 Africa i-Parliaments
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

package org.bungeni.extutils;

/**
 *Checks if the OS platform is 64 or 32 bits
 * @author ashok
 */
public class JavaPlatformArch {
    private static final String keys [] = {
        "sun.arch.data.model",
        "com.ibm.vm.bitmode",
        "os.arch",
    };

    public static int platform() {
        for (String key : keys ) {
            String arch = System.getProperty(key);
            if (arch != null) {
                if (arch.indexOf("64") >= 0 ) {
                    return 64;
                }
            }
        }
        return 32;
    }

    public static void main(String[] args) {
        System.out.println(JavaPlatformArch.platform());
    }
}
