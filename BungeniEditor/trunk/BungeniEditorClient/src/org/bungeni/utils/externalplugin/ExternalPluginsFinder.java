/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.plugin.impl.IEditorPluginAll;

/**
 * Maintains file handles to all external plugins found on disk.
 * The folder for plugins is always <editor installation>/plugins
 * @author Ashok Hariharan
 */
public class ExternalPluginsFinder {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExternalPluginsFinder.class.getName());
    
    
    private File jarFile ;
    private String fullPathToJarFile = "";
    private String classInstantiator ; 
    private boolean pluginFound = false;
    
    public ExternalPluginsFinder(String jarFileName, String classInstantiator){
        try {
            //look in this folder for plugin files
            String pathToPluginsFolder =  DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "plugins";
            //look for the plugin jar file in the plugin folder
            //but we look only 1 level down
            File fjar = findPluginJar(pathToPluginsFolder, jarFileName, 0);
            if (fjar != null)  {
                this.jarFile = fjar;
                this.pluginFound = true;
            } else {
                this.jarFile = null;
            }
            this.classInstantiator = classInstantiator;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    
    /**
     * If the plugin jar was found returns true, else false;
     * @return
     */
    public boolean isPluginFound () {
        return pluginFound;
    }

    /**
     * Generates an IEditorPluginAll instance using the jar file / and class instance
     * @return
     */
    public IEditorPluginAll getPluginInstance() {
        IEditorPluginAll iepAll = null;
        try {
            ArrayList<URL> urlArr = new ArrayList<URL>();
            URL fileURL = jarFile.toURI().toURL();
            urlArr.add(fileURL);
            URLClassLoader loader = new URLClassLoader(urlArr.toArray(new URL[urlArr.size()]));
            Class loadingClass = loader.loadClass(classInstantiator);
            iepAll = (IEditorPluginAll) loadingClass.newInstance();
        } catch (Exception ex) {
            log.error("getPluginInstance : " + ex.getMessage());
        } finally {
            return iepAll;
        }

    }
    
    class pluginFileFilter implements FilenameFilter {
        private String lookforPlugin  = "";
        pluginFileFilter (String fname ) {
            lookforPlugin = fname;
        }
        public boolean accept(File dir, String fileName) {
            return fileName.equals(lookforPlugin);
        }
        
    }
    
    class pluginSubDirectoryFilter implements FileFilter {

        public boolean accept(File foundFile) {
            return foundFile.isDirectory();
        }
        
    }
    
    /**
     * Looks for plugin file and returns a handle to the jar file
     * @param pluginFolder
     * @param jarFileName
     * @param nLevel
     * @return
     */
    private File findPluginJar (String pluginFolder, String jarFileName, int nLevel) {
        File fdir = new File (pluginFolder);
        //search in the current folder 
        File[] fFiles = fdir.listFiles(new pluginFileFilter(jarFileName));
        if (fFiles != null) {
            if (fFiles.length > 0) {
                return fFiles[0];
            } else {
                //file was not found... look within subdirectories
                //but we look in only 1 level 
                if (nLevel == 0) {
                    nLevel++;
                    File[] fDirs = fdir.listFiles(new pluginSubDirectoryFilter()); 
                    if (fDirs.length > 0 ) {
                        for (File fDir : fDirs ) {
                            File ffile = findPluginJar(fDir.getAbsolutePath(), jarFileName, nLevel);
                            if (ffile != null) {
                                return ffile;
                            }
                        }
                    }
                } else
                    return null;
            }
                
        } 
        return null;
    }
    
    /*
    private boolean searchFolder(String folderName) throws Exception {
        File searchDir = new File(folderName);
        if (searchDir.isFile()) return false;
        //get all the .jar files in the plugin folder
        File[] pluginFiles = searchDir.listFiles(new pluginFileFilter());
        for (File f : pluginFiles) {
            ArrayList<String> classNames = getClassNamesInFile(f.getAbsolutePath());
            for (String className : classNames) {
                String canName = className.substring(0, className.length() - 6);
                Class clasz = getClassFromJar(f, canName);
                Class[] interfacesInClass = clasz.getInterfaces();
                for (Class anInterface : interfacesInClass) {
                    if (anInterface.getName().equals("base.IEditorPluginAll")) {
                            System.out.println(anInterface.getName())
;                    }
                }
            }
        }   
        return true;
    }
    
    private Class getClassFromJar(File fJar, String className) {
        Class mclass = null;
        try {
            addURL(fJar.toURI().toURL());
            URLClassLoader urlLoader;
            String fPath = fJar.getAbsolutePath();
            fPath = "jar:file://" + fPath + "!/";
            URL url = (new File(fPath)).toURI().toURL();
            urlLoader = new URLClassLoader(new URL[]{url});
            mclass = urlLoader.loadClass(className);
        } catch (ClassNotFoundException ex) {
            log.error("getClassFromJar : " + ex.getMessage());
        } catch (MalformedURLException ex) {
            log.error("getClassFromJar : " + ex.getMessage());
        } finally {
            return mclass;
        }
        
        
    }
    
    private static final Class[] classParameters = new Class[]{URL.class};
    
    private void addURL(URL fileURL ) {
        //get the system class loader
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        //get the urls in the system class loader
        URL[] urls = sysloader.getURLs();
        for (URL url : urls ) {
            //check if already added
            if (url.equals(fileURL)) {
                return;
            }
        }
        //now add the usr
        Class systemClassLoader = URLClassLoader.class;
        try {
             Method method = systemClassLoader.getDeclaredMethod("addURL", classParameters);
             method.setAccessible(true);
             method.invoke(systemClassLoader, new Object[]{fileURL});
             
        } catch (Exception ex) {
            log.error("addUrlToClassLoader:" + ex.getMessage());
        }
        
    }
    
    private ArrayList<String> getClassNamesInFile(String jarFileName) {
        JarInputStream jarFile = null;
        ArrayList<String> foundClasses = new ArrayList<String>(0);

        try {
            jarFile = new JarInputStream(new FileInputStream(jarFileName));
            
            JarEntry jarEntry = null;
            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if (jarEntry.getName().endsWith(".class")) {
                    foundClasses.add(jarEntry.getName().replaceAll("/", "\\."));
                }
            }
        } catch (IOException ex) {
            log.error("getClassNamesInFile : " + ex.getMessage());
        }  finally {
            return foundClasses;
           
        }
    }
    */
    public static void main(String[] args) {
       // ExternalPluginsFinder epf = new ExternalPluginsFinder();
    }
}
