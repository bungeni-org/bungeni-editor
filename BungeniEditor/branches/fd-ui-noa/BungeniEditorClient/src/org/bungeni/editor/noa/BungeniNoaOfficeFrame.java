/*
 *  Copyright (C) 2011 Africa i-Parliaments Action Plan
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

import ag.ion.bion.officelayer.desktop.IDesktopService;
import ag.ion.bion.officelayer.desktop.IFrame;
import ag.ion.noa.NOAException;
import ag.ion.noa.frame.ILayoutManager;

/**
 * Class that does OpenOffice frame creation.
 * Note that this is different from BungeniNoaFrame -- which is a JFrame derived class.
 * This is a container for an OpenOffice XFrame document window
 * 1 XFrame window for 1 OOo document
 * @author Ashok
 */
public class BungeniNoaOfficeFrame {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniNoaOfficeFrame.class.getName());

   
    private IFrame officeFrame = null;

    /**
     * Assumption here is that the native instance has already been attached
     * to a container panel. 
     */
    public BungeniNoaOfficeFrame(BungeniNoaNativeView nativeView){
        try {
            IDesktopService idesk = BungeniNoaApp.getInstance().getOfficeApp().getDesktopService();

            officeFrame = idesk.constructNewOfficeFrame(nativeView.getNativeView());

           // initFrameBehavior();

        } catch (Throwable ex) {
            log.error("Error while getting NoaOfficeFrame", ex);
        }
    }

    /**
     * Do all the initializations for IOfficeFrame behaviors, look and feel here
     */
    protected final void initFrameBehavior() {
        try {
            //hide the menu bar
            officeFrame.getLayoutManager().hideElement(ILayoutManager.URL_TOOLBAR_STANDARDBAR);
        } catch (NOAException ex) {
            log.error("Error while adding custom initialization to the Frame via the layout manager", ex);
        }
    }

    public IFrame getFrame(){
        return this.officeFrame;
    }



}
