/*
 * Installation.java
 * 
 * Created on Jun 4, 2007, 10:13:41 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;


import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 *
 * @author Administrator
 */
public class Installation {
 
  /**
   * Test is the current running program is installed.
   * <br>
   * This method assumes that an installed java program is contained
   * in a jar file.
   * 
   * @param clazz 
   * @return    <code>true</code> if installed
   */
  public static boolean isInstalled(Class clazz) {
    try {
      URL classUrl = getClassURL(clazz);
    
      return "jar".equals(classUrl.getProtocol());
    } catch (Exception ex) {
      // fall through
    }
    return false;   
  }
 
  /**
   * Get the directory where the current running program is installed,
   * i.e. the location of the jar file the given class is contained in. 
   * <br>
   * If it is not installed (see {@link #isInstalled()}) the <em>current
   * user working directory</em> (as denoted by the system property
   * <code>user.dir<code>) is returned instead.
   * 
   * @param  clazz  class to check for installation
   * @return        the installation directory
   * @see
   */
  public static File getInstallDirectory(Class clazz) {
    if (isInstalled(clazz)) {
      try {
        JarURLConnection jarCon = (JarURLConnection)getClassURL(clazz).openConnection();
 
        URL jarUrl = jarCon.getJarFileURL();
    
        File jarFile = new File(URLDecoder.decode(jarUrl.getPath(), "UTF-8"));
    
        return jarFile.getParentFile();
      } catch (Exception ex) {
        // fall through
      }
    }
    
    return new File(System.getProperty("user.dir"));
  } 
  
  public  String getAbsoluteInstallDir() {
       File dir = getInstallDirectory(this.getClass());
       String full_path = dir.getAbsolutePath();
       return full_path;
  }
  
  /**
   * Get URL of the given class.
   * 
   * @param  clazz  class to get URL for
   * @return the URL this class was loaded from
   */
  private static URL getClassURL(Class clazz) {
          
           return clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class");
  }
  
}