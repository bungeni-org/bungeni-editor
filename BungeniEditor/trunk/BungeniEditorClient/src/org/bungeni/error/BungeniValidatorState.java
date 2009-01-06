/*
 * BungeniValidator.java
 *
 * Created on March 4, 2008, 1:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.error;

/**
 *
 * @author Administrator
 */
public class BungeniValidatorState {
    public boolean state = false;
    public BungeniMsg msg;
    /** Creates a new instance of BungeniValidator */
    public BungeniValidatorState(boolean bstate, BungeniMsg mesg) {
        state = bstate;
        msg = mesg;
    }
    
}
