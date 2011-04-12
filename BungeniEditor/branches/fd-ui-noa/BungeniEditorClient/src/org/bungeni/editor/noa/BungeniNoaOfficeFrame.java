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

import ag.ion.bion.officelayer.desktop.IFrame;

/**
 * Singleton class to factor out OpenOffice frame creation.
 * Note that this is different from BungeniNoaFrame -- which is a JFrame derived class.
 * This is a container for an OpenOffice XFrame document window
 * Assumption here currently is we will have only 1 office Frame
 * Until we understand how NOA handles multiple frames ?
 * @author Ashok
 */
public class BungeniNoaOfficeFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaOfficeFrame.class.getName());

    private static BungeniNoaOfficeFrame thisFrame = null;

    private IFrame officeFrame = null;

    /**
     * Assumption here is that the native instance has already been attached
     * to a container panel. 
     */
    private BungeniNoaOfficeFrame(){
        try {
            officeFrame =
                  BungeniNoaApp.getInstance().getOfficeApp().
                    getDesktopService().
                      constructNewOfficeFrame(BungeniNoaNativeView.getInstance().
                         getNativeView());
        } catch (Throwable ex) {
            log.error("Error while getting NoaOfficeFrame");
        }
    }

    public static BungeniNoaOfficeFrame getInstance(){
        if (thisFrame == null) {
            thisFrame = new BungeniNoaOfficeFrame();
        }
        return thisFrame;
    }

    public IFrame getFrame(){
        return this.officeFrame;
    }



}
