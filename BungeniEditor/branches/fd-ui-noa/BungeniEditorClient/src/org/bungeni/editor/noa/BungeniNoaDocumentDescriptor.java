/*
 *  Copyright (C) 2011 undesa
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

package org.bungeni.editor.noa;

import ag.ion.bion.officelayer.document.DocumentDescriptor;

/**
 * Class with static methods to return the appropriate document descriptor for NOA
 * @author ashok
 */
public class BungeniNoaDocumentDescriptor {

    public static DocumentDescriptor forTemplate(String templatePath){
        DocumentDescriptor ddc = new DocumentDescriptor();
        ddc.setMacroExecutionMode(com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE);
        ddc.setAsTemplate(true);
        ddc.setURL(templatePath);
        return ddc;
    }

    public static DocumentDescriptor forDocument(String docPath) {
        DocumentDescriptor ddc = new DocumentDescriptor();
        ddc.setMacroExecutionMode(com.sun.star.document.MacroExecMode.ALWAYS_EXECUTE);
        ddc.setURL(docPath);
        return ddc;
    }

}
