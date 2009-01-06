/*
 * BungeniTransformationTarget.java
 *
 * Created on June 3, 2008, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.impl;

/**
 *
 * @author Administrator
 */
public class BungeniTransformationTarget {
       public String targetName = "";
       public String targetDescription = "";
       public String targetClass = "";
       public String targetExt = "";
    /** Creates a new instance of BungeniTransformationTarget */
    public BungeniTransformationTarget() {
    }
        
     public BungeniTransformationTarget (String name, String desc, String ext, String sClass ) {
            this.targetName = name;
            this.targetDescription = desc;
            this.targetExt = ext;
            this.targetClass = sClass;
     }

    @Override
    public String toString() {
            return this.targetDescription;
   } 
}
