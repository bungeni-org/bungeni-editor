/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(ExternalPlugin.class.getName());

    //plugin instantation objects
    private ClassLoader pluginClassLoader = null;
    private Class pluginClass = null;
    private Object pluginInstance = null;
    private HashMap<String, Method> methodMap = new HashMap<String, Method>();

   
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

    /**
     * Sets the classloader for the plugin
     * @param loader
     */
    public void setPluginClassLoader(ClassLoader loader) {
        this.pluginClassLoader = loader;
    }

    /**
     * Calls the plugin method via reflections
     * @param methodname
     * @param arguments
     * @return
     */
    public Object callMethod(String methodname, Object[] arguments) {
        Object returnValue = null;
        try {
            if (this.methodMap.containsKey(methodname)) {
                Method aMethod = this.methodMap.get(methodname);
                returnValue = aMethod.invoke(this.pluginInstance, arguments);
            } else {
                log.error("callMethod : method : " + methodname + "  does not exist in the method map");
            }
        } catch (IllegalAccessException ex) {
            log.error("callMethod : " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("callMethod : " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            log.error("callMethod : " + ex.getMessage());
        } finally {
            return returnValue;
        }
    }

    public boolean instantiatePlugin(){
        boolean bState = false;
        try {
                if (pluginClassLoader != null) {
                        if (pluginClass == null ) {
                                this.pluginClass = pluginClassLoader.loadClass(this.Loader);
                                //call the constructor for the plugin class
                                Class[] convCtorParams = {};
                                Constructor convPlainCtor = pluginClass.getConstructor(convCtorParams);
                                this.pluginInstance = convPlainCtor.newInstance();
                                //instantiate the methods and cache them for calling
                                //-----setParams method ------
                                Class[] mSetParamParams = {HashMap.class};
                                Method mSetParam = this.pluginClass.getDeclaredMethod("setParams", mSetParamParams);
                                this.methodMap.put("setParams", mSetParam);
                                //---- exec method ------------
                                Class[] mExecParams = {};
                                Method mExec = this.pluginClass.getDeclaredMethod("exec", mExecParams);
                                this.methodMap.put("exec", mExec);
                                bState = true ;
                        } else {
                            bState = true;
                        }
                } else {
                    log.error("instantiatePlugin : classloader is null");
                    bState = false;
                }
            } catch (InstantiationException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (IllegalAccessException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (InvocationTargetException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (NoSuchMethodException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (SecurityException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                log.error("instantiatePlugin : " + ex.getMessage());
            } finally {
                return bState;
            }
    }
   
}
