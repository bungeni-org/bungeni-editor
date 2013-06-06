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


package org.bungeni.ooo.transforms.impl;

import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public abstract class BungeniDocTransform implements IBungeniDocTransform {
    /** Creates a new instance of BungeniDocTransform */
    protected JFrame callerFrame;
    protected HashMap<String,Object> transformParams = new HashMap<String,Object>();

    public BungeniDocTransform(HashMap<String, Object> params) {
        setParams(params);
    }
    
    public BungeniDocTransform(){
        
    }
    
    public void setParams(HashMap<String, Object> params) {
        this.transformParams = params;
    }
    
    protected HashMap<String,Object> getParams(){
        return this.transformParams;
    }
    
    abstract public boolean transform(OOComponentHelper ooDocument) ;
    
    public void setParentFrame(JFrame frm){
        callerFrame = frm;
    }
   
}
