/*
 *  Copyright (C) 2011 windows
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

package org.bungeni.editor;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XLayoutManager;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.uno.UnoRuntime;
import java.util.ArrayList;
import java.util.List;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniOOoLayout {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOOoLayout.class.getName());

    private static BungeniOOoLayout instance = null;
  
    public class LayoutURL {
        String url;
        boolean visible ; // true or false ~ show or hide
        boolean oldVisible;

        LayoutURL(String url, boolean mode) {
            this.url = url;
            this.visible = mode;
        }
    }

    List<LayoutURL> layoutURLs = new ArrayList<LayoutURL>(0);

    private BungeniOOoLayout(){
        layoutURLs.add(new LayoutURL("private:resource/menubar/menubar", false  ));
        layoutURLs.add(new LayoutURL("private:resource/toolbar/standardbar", true  ));
        layoutURLs.add(new LayoutURL("private:resource/toolbar/textobjectbar", true  ));
    }

    public static BungeniOOoLayout getInstance(){
        if (instance == null ) {
            instance = new BungeniOOoLayout();
        }
        return instance;
    }

    public List<LayoutURL> getLayoutURLs() {
        return this.layoutURLs;
    }

    public void applyLayout(OOComponentHelper ooDocument) {
            XController xcontrol = ooDocument.getDocumentModel().getCurrentController();
            XFrame xframe = xcontrol.getFrame();
            XPropertySet xps = ooQueryInterface.XPropertySet(xframe);
            XLayoutManager xlayout = null;
            try {
                 xlayout = (XLayoutManager) UnoRuntime.queryInterface(XLayoutManager.class, xps.getPropertyValue("LayoutManager"));
                 for (LayoutURL layoutURL : getLayoutURLs()) {
                    layoutURL.oldVisible = xlayout.isElementVisible(layoutURL.url);
                    if (layoutURL.visible) {
                        xlayout.showElement(layoutURL.url);
                    } else  {
                        xlayout.hideElement(layoutURL.url);
                    }
                }
            } catch (UnknownPropertyException ex) {
                log.error("error while applying XLayoutManager layout property" ,ex);
            } catch (WrappedTargetException ex) {
                log.error("error while applying XLayoutManager layout property" ,ex);
            }
    }

   

}
