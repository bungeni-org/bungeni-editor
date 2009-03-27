/*
 * BungeniUUID.java
 *
 * Created on October 1, 2007, 5:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 *
 * @author Administrator
 */
public class BungeniUUID {
    
    /** Creates a new instance of BungeniUUID */
    public BungeniUUID() {
    }
    
    public static String getStringUUID() {
            UUIDGenerator gen = UUIDGenerator.getInstance();
            UUID uuid = gen.generateTimeBasedUUID();
            return uuid.toString();
    }
}
