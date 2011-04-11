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

import ag.ion.bion.officelayer.application.IOfficeApplication;
import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.bion.officelayer.application.OfficeApplicationRuntime;
import java.util.Map;

/**
 * OfficeApplicationRuntime returns objects with the IOfficeApplication interface,
 * However -- OfficeApplicationRuntime only returns LocalOfficeApplication,
 * RemoteOfficeApplication objects -- we have to use an extended class to
 * make it return a BungeniLocalOfficeApplication.
 *
 * @author ashok
 */
public class BungeniOfficeApplicationRuntime extends OfficeApplicationRuntime {

 /**
  * Hacked out of OfficeApplicationRuntime.getApplication() - why does it
  * make critical OOo context handles and service accessors as private ??
  * @param configuration
  * @return
  * @throws OfficeApplicationException
  */
 public static IOfficeApplication getBungeniApplication(Map configuration) throws OfficeApplicationException {

     if(configuration == null)
      return OfficeApplicationRuntime.getApplication(configuration);

     Object type = configuration.get(IOfficeApplication.APPLICATION_TYPE_KEY);

     if(type == null)
       return OfficeApplicationRuntime.getApplication(configuration);

    if(type.toString().equals(IOfficeApplication.LOCAL_APPLICATION)) {
      return new BungeniLocalOfficeApplication(configuration);
    }
    else
      return OfficeApplicationRuntime.getApplication(configuration);
 }


}
