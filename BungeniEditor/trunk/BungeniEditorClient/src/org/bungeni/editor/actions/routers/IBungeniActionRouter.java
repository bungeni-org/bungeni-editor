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

package org.bungeni.editor.actions.routers;

import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
// !+ACTION_RECONF (rm, jan 2012) - ToolbarAction class is deprecated,
// interface methods rewritten to get rid of toolbarAction argument
public interface IBungeniActionRouter {
    // !+ACTION_RECONF (rm, jan 2012) - all these methods do not use action toolbar
    public  org.bungeni.error.BungeniValidatorState route(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDoc);
    public org.bungeni.error.BungeniValidatorState route_DocumentLevelAction(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedInsert(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_TextSelectedEdit(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullInsert(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    public org.bungeni.error.BungeniValidatorState route_FullEdit(toolbarAction subAction, javax.swing.JFrame parentFrame, OOComponentHelper ooDocument) ;
    
}
