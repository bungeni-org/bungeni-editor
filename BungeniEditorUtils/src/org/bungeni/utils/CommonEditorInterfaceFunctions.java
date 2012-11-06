/*
 *  Copyright (C) 2012 Africa i-Parliaments
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

package org.bungeni.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.editor.panels.impl.IMainContainerPanel;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonEditorInterfaceFunctions {
  private static org.apache.log4j.Logger log = Logger.getLogger(CommonEditorInterfaceFunctions.class.getName());
  private static IMainContainerPanel IMAIN_CONTAINER_PANEL = null;
  /**
   * Method to get the main panel by Plugin and extension classes
   * All this ugliness will go by switching to the spring framework.
   */
    public static synchronized IMainContainerPanel  getMainPanel() {
        if (null == IMAIN_CONTAINER_PANEL) {
            String mainPanel = "";
            try {
                mainPanel = BaseConfigReader.getMainPanelImpl();
            } catch (IOException ex) {
                log.error("Error getting panel ");
            }
            Class mainPanelClass = null;
            try {
                mainPanelClass = Class.forName(mainPanel);
            } catch (ClassNotFoundException ex) {
                log.error("main panel class : " + mainPanel + " was not found");
            }
            if (null != mainPanelClass) {
                Method mgetInstance = null;
                try {
                    mgetInstance = mainPanelClass.getDeclaredMethod("getInstance");
                } catch (NoSuchMethodException ex) {
                    log.error("No such method in class " + mainPanel);
                } catch (SecurityException ex) {
                    log.error("Security accessor exception in class " + mainPanel);
                }
                if (null != mgetInstance) {
                    try {
                       IMAIN_CONTAINER_PANEL = (IMainContainerPanel) mgetInstance.invoke(
                                null,
                                new Object[0]);
                    } catch (Exception ex) {
                        log.error("Error while invoking getInstance()");
                    }
                }

            }
        }
        return IMAIN_CONTAINER_PANEL;
    }

}
