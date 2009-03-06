/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils.externalplugin;

import java.io.File;
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
  //  private static ArrayList<File> pluginFileHandles = new ArrayList<File>(0);
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExternalPluginsFinder.class.getName());
      
    class pluginFileFilter implements FilenameFilter {

        public boolean accept(File dir, String fileName) {
            return fileName.endsWith(".jar");
        }
        
    }
    
    private File jarFile ;
    private String classInstantiator ; 
    
    public ExternalPluginsFinder(String jarFileName, String classInstantiator){
        try {
            //look in this folder for plugin files
            String pathToPluginsFolder =  DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "plugins";
            //pathToPluginsFolder = pathToPluginsFolder + File.separator + "plugins/sectionrefactorplugin";
            File fjar = new File(pathToPluginsFolder + File.separator + jarFileName);
            this.jarFile = fjar;
            this.classInstantiator = classInstantiator;
            // searchFolder(pathToPluginsFolder);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    
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
