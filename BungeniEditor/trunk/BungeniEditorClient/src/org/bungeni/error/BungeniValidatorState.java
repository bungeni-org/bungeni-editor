
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
