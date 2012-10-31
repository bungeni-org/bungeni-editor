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

package org.bungeni.editor.input;

import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;

/**
 * This interface is used for specifying input receiver classes for the editor
 * the default input receiver is the file system
 * it can be extended to support other systems like CMS, Bungeni etc
 *
 * @author Ashok Hariharan
 */
public interface IInputDocumentReceiver {

    public String receiveDocument(JFrame callerFrame, PluggableConfig customConfig, HashMap inputParams);
   
}
