/*
 * testCommand.java
 *
 * Created on December 21, 2007, 11:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 *
 * @author Administrator
 */
public class testCommand implements Command {
    
    /** Creates a new instance of testCommand */
    public testCommand() {
    }

    public boolean execute(Context context) throws Exception {
        testContext ctx = (testContext) context;
        System.out.println("Executing test command");
        System.out.println("getting context fields = ");
        System.out.println(ctx.getFields().toString());
                
        return false;
    }
    
}
