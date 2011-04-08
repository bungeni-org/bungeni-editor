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
package org.bungeni.editor;

import ag.ion.bion.officelayer.application.IApplicationAssistant;
import ag.ion.bion.officelayer.application.ILazyApplicationInfo;
import ag.ion.bion.officelayer.application.IOfficeApplication;
import ag.ion.bion.officelayer.internal.application.ApplicationAssistant;
import java.util.HashMap;
import org.bungeni.editor.extnoa.BungeniLocalOfficeApplication;
import org.bungeni.editor.extnoa.BungeniOfficeApplicationRuntime;

/**
 * Initializes the NOA Application
 * @author ashok
 */
public class BungeniNoaApp {

    private static BungeniNoaApp noaApp = null;
    private BungeniLocalOfficeApplication officeApplication = null;
    private HashMap officeConfiguration = new HashMap();

    public static BungeniNoaApp getInstance() throws Throwable {
        if (noaApp == null) {
            noaApp = new BungeniNoaApp();
        }
        return noaApp;
    }

    private BungeniNoaApp(BungeniLocalOfficeApplication officeApp) {
        this.officeApplication = officeApp;
    }

    private BungeniNoaApp() throws Throwable {
        //get native view lib
        IApplicationAssistant applicationAssistant = new ApplicationAssistant(System.getProperty("user.dir") + "\\lib\\noa-64");
        ILazyApplicationInfo[] appInfos = applicationAssistant.getLocalApplications();
        if (appInfos.length < 1) {
            throw new Throwable("No OpenOffice.org Application found.");
        }
        //build configuration map
        this.officeConfiguration = new HashMap();
        System.out.println(appInfos[0].getHome());
        this.officeConfiguration.put(IOfficeApplication.APPLICATION_HOME_KEY, appInfos[0].getHome());
        this.officeConfiguration.put(IOfficeApplication.APPLICATION_TYPE_KEY, IOfficeApplication.LOCAL_APPLICATION);

        this.officeApplication = (BungeniLocalOfficeApplication) BungeniOfficeApplicationRuntime.getBungeniApplication(this.officeConfiguration);
        this.officeApplication.setConfiguration(this.officeConfiguration);
        this.officeApplication.activate();

    }

    /**
     * This returns a BungeniLocalOfficeApplication object -- OOo context is accessible via
     * the getOfficeConnection() custom API
     * @return
     */
    public BungeniLocalOfficeApplication getOfficeApp(){
        return this.officeApplication;
    }
}
