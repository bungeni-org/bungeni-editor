/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

/**
 *
 * @author undesa
 */
public class ExternalPlugin {
    public final String Name;
    public final String Loader;
    public final String Description;
    public final boolean isEnabled;
    
    public ExternalPlugin(String name, String loader, String desc, int enableDisable) {
        this.Name = name;
        this.Loader = loader;
        this.Description = desc;
        this.isEnabled = (enableDisable == 0 ? false: true);
        
    }
    
}
