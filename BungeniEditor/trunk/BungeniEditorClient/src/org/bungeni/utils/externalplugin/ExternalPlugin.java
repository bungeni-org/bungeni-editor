/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

import java.util.ArrayList;

/**
 *
 * @author Ashok Hariharan
 */
public class ExternalPlugin {
    public  String Name;
    public  String JarFile;
    public String Loader;
    public String PluginBaseFolder ;
    public ArrayList<String> dependentJars= new ArrayList<String>(0);

    public ExternalPlugin(){
        
    }

    public ExternalPlugin(String name, String jarFile, String loader) {
        this.Name = name;
        this.JarFile = jarFile;
        this.Loader = loader;
    }

    public void addRequiredJars(String reqdJar) {
        dependentJars.add(reqdJar);
    }
    
}
