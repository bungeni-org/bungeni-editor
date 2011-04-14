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

package org.bungeni.editor.noa;

import ag.ion.bion.officelayer.NativeView;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import org.bungeni.extutils.JavaPlatformArch;

/**
 * Prepares a NOA native view
 * Provides API to map a awt Container to a NativeView
 * Loads the correct NativeView DLL or SO file depending on the platform
 * architecture
 * @author Ashok
 */
public class BungeniNoaNativeView {

    private static BungeniNoaNativeView noaNativeView = null;

    private NativeView nativeView = null;
    private Container parentContainer = null;

    /**
     * Create the nativeView using the NOA C runtime libraries
     */
    private BungeniNoaNativeView() {
        //32 bit dlls / so files are in lib/noa32
        //64 bit dlls / so files are in lib/noa64
        int javaPlatform = JavaPlatformArch.platform();
        String nativeViewPath = System.getProperty("user.dir") +
                File.separator + "lib" +
                File.separator + "noa" + javaPlatform;
        nativeView = new NativeView(nativeViewPath);
    }

    public static BungeniNoaNativeView getInstance(){
        if (null == noaNativeView) {
            noaNativeView = new BungeniNoaNativeView();
        }
        return noaNativeView;
    }

    public NativeView getNativeView(){
        return this.nativeView;
    }

    /**
     * This API is used to associate the BungeniNoaPanel with an OpenOffice
     * Frame
     * @param parent
     */
    public void attachContainerToNativeView(Container parent) {
        //add nativeview to the container
        this.parentContainer = parent;
        parentContainer.add(getNativeView());
        //add a resize event handler to the parent
        parentContainer.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                getNativeView().setPreferredSize(new Dimension(parentContainer.getWidth() - 5, parentContainer.getHeight() - 5));
                parentContainer.getLayout().layoutContainer(parentContainer);
            }

        });
        getNativeView().setPreferredSize(new Dimension(parent.getWidth() - 5, parent.getHeight() - 5));
        parent.getLayout().layoutContainer(parent);
    }


}
