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

package org.bungeni.editor.noa.ext;

import ag.ion.bion.officelayer.application.connection.IOfficeConnection;
import ag.ion.bion.officelayer.internal.application.LocalOfficeApplication;
import java.util.Map;

/**
 * This class is a neccessary "hack" since IOfficeApplication (LocalOfficeApplication)
 * conceals the IOfficeConnection member as a 'private' member. So we override the
 * setOfficeConnection() API to create a "shadow" copy of the connection object
 * @author ashok
 */
public class BungeniLocalOfficeApplication extends LocalOfficeApplication {
    IOfficeConnection shadow_officeConnection = null;

    public BungeniLocalOfficeApplication(Map config ) {
        super(config);
    }

   @Override
   protected void setOfficeConnection(IOfficeConnection officeConnection) {
    super.setOfficeConnection(officeConnection);
    this.shadow_officeConnection = officeConnection;
  }

   public IOfficeConnection getOfficeConnection(){
       return this.shadow_officeConnection;
   }

}
