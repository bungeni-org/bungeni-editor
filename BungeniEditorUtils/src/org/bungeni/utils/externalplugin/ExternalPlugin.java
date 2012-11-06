package org.bungeni.utils.externalplugin;

import java.io.File;
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
    private  String JarFile;
    public String Loader;
    public String PluginBaseFolder ;
    private ArrayList<String> dependentJars= new ArrayList<String>(0);
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
        this.JarFile = jarFile.replaceAll("[/\\\\]+", "\\" + File.separator);
        this.Loader = loader;
    }

    public void addRequiredJars(String reqdJar) {
        getDependentJars().add(reqdJar);
    }

    /**
     * Sets the classloader for the plugin
     * @param loader
     */
    public void setPluginClassLoader(ClassLoader loader) {
        this.pluginClassLoader = loader;
    }

    public ClassLoader getPluginClassLoader() {
        return this.pluginClassLoader;
    }

    /**
     * Calls the plugin method via reflections
     * (rm, feb 2012) - ADDED COMMENTS
     * callMethod() calls one of 2 methods to determine the validation action being
     * carried out. These methods, determined via reflection are setParams() and exec()
     * setParams() takes a HashMap arg of the arguments ODF document that is being verified
     * and the rules to be checked against the document while exec() runs the rules on
     * the documents
     *
     * passing a non_HashMap arg to setParams() was causing a JOptionPane to be
     * displayed in BungeniEditor showing the "An exception occurred while the
     * Structural Rules checker was running"
     * 
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
            log.error("callMethod - Illegal Access Exception : " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("callMethod - Illegal Argument Exception : " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            log.error("callMethod InvocationTargetException : " + ex.getMessage());
        } finally {
            return returnValue;
        }
    }

    //!+ (rm, feb 2012) - ADDED COMMENTS
    // This method instanciates either setParams() or exec() that runs the document
    // rules against the selected document
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
                                //---- exec2 method ------------
                                Class[] mExec2Params = {Object[].class};
                                Method mExec2 = this.pluginClass.getDeclaredMethod("exec2", mExec2Params);
                                this.methodMap.put("exec2", mExec2);


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

    /**
     * @return the JarFile
     */
    public String getJarFile() {
        return JarFile;
    }

    private String prefixJarPath(String jarFile) {
        return jarFile.replaceAll("[/\\\\]+", "\\" + File.separator);
    }

    /**
     * @param JarFile the JarFile to set
     */
    public void setJarFile(String JarFile) {
        this.JarFile = prefixJarPath(JarFile);
    }

    /**
     * @return the dependentJars
     */
    public ArrayList<String> getDependentJars() {

        return dependentJars;
    }

    /**
     * @param dependentJars the dependentJars to set
     */
    public void setDependentJars(ArrayList<String> dependentJars) {
        for (String depJar : dependentJars) {
            this.dependentJars.add(prefixJarPath(depJar));
        }
    }
   
}
