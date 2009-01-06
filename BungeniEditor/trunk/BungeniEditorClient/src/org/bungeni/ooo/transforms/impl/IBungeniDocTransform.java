/*
 * IBungeniDocTransform.java
 *
 * Created on June 3, 2008, 1:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.impl;

import java.util.HashMap;
import javax.swing.JFrame;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author Administrator
 */
public interface IBungeniDocTransform {
    public void setParentFrame(JFrame frm);
    public void setParams (HashMap<String, Object> params);
    public boolean transform(OOComponentHelper ooDocument);
   
}
