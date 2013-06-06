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

package org.bungeni.editor.input;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.utils.fcfilter.ODTFileFilter;

/**
 *
 * @author Ashok Hariharan
 */
public class FSDocumentReceiver implements IInputDocumentReceiver {
  private static final Logger log = Logger.getLogger(FSDocumentReceiver.class.getName());

    public String receiveDocument(JFrame parentFrame, PluggableConfig customConfig, HashMap inputParams) {
        String aDocument = null;
        try {
            String basePath = BaseConfigReader.getWorkspaceFolder();
            File openFile = CommonFileFunctions.getFileFromChooser(basePath, new ODTFileFilter(), JFileChooser.FILES_ONLY, null);
            if (null != openFile) {
                aDocument =  openFile.getAbsolutePath();
            }
        } catch (IOException ex) {
           log.error("Error whil attempting to open file", ex);
        }
        return aDocument;

    }



}
