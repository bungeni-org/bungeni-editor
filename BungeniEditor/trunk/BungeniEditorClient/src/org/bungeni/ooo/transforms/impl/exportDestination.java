/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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

package org.bungeni.ooo.transforms.impl;

/**
 *
 * @author Ashok Hariharan
 */
public class exportDestination extends Object {

    String exportDestName;
    String exportDestDesc;

    public exportDestination(String name, String desc) {
        this.exportDestDesc = desc;
        this.exportDestName = name;
    }

    @Override
    public String toString() {
        return this.exportDestDesc;
    }
}