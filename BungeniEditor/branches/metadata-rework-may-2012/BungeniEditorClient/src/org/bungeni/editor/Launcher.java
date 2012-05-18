/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *This class encompasses the custom class loader for the Bungeni Editor.
 * The editor uses a mechanism of chained class loaders as follows :
 * 1) the UNO class loader searches for an openoffice instance on the computer
 * and adds the OOo class libraries to the classpath.
 * 2) the UNO class loader then invokes the EditorClassLoader, which in turn searches
 * for plugin libraries and adds them to the class path.
 * 3) the EditorClassLoader, after adding the plugin libraries to the class path,
 * invokes the main editor application, using a Reflection method.
 *
 * @author Ashok Hariharan
 */
public final class Launcher {

    public static final class EditorClassLoader extends URLClassLoader {

        public EditorClassLoader( URL[] urls ) {
            super( urls );
        }

        @Override
        protected Class findClass( String name ) throws ClassNotFoundException {
            // This is only called via this.loadClass -> super.loadClass ->
            // this.findClass, after this.loadClass has already called
            // super.findClass, so no need to call super.findClass again:
            throw new ClassNotFoundException( name );
        }

        @Override
        protected Class loadClass( String name, boolean resolve )
            throws ClassNotFoundException
        {
            Class c = findLoadedClass( name );
            if ( c == null ) {
                try {
                    c = super.findClass( name );
                } catch ( ClassNotFoundException e ) {
                    return super.loadClass( name, resolve );
                } catch ( SecurityException e ) {
                    // A SecurityException "Prohibited package name: java.lang"
                    // may occur when the user added the JVM's rt.jar to the
                    // java.class.path:
                    return super.loadClass( name, resolve );
                }
            }
            if ( resolve ) {
                resolveClass( c );
            }
            return c;
        }
    }


    private static ClassLoader editorClassLoader = null;
    
    public static synchronized ClassLoader getEditorClassLoader() {
        //path where plugin libs are stored
        final String PLUGINLIBSDIR = "plugin_libs";

        if (editorClassLoader==null) {
            Vector vecLibs = new Vector();
            String classpath = null;
            try {
                //get application class path
                classpath = System.getProperty( "java.class.path" );
            } catch ( SecurityException e ) {
                // don't add the class path entries to the list of class
                // loader URLs
                System.err.println( "getEditorClassLoader::" +
                    "getEditorClassLoader: cannot get system property " +
                    "java.class.path: " + e );
            }
            //add the  class path entries into a Vector
            if ( classpath != null ) {
                addUrls(vecLibs, classpath, File.pathSeparator);
            }
            //get the URLs to Jar files from the plugin_libs folder
            String userDirJarFiles = System.getProperty("user.dir") + File.separator + PLUGINLIBSDIR;
            //filter for jar files
            FileFilter fJarFiles = new FileFilter(){
                public boolean accept(File pathname) {
                    if (pathname.isFile()) {
                        if (pathname.getName().endsWith(".jar")) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            File fPluginLibs = new File(userDirJarFiles);
            File[] foundJars = null;
            if (fPluginLibs.exists()) {
                foundJars = fPluginLibs.listFiles(fJarFiles);
            }
            if (foundJars != null ) { //jar files were found
                for (File fJar : foundJars) {
                    //add the found jar files to the existing classpath vector list
                    try {
                        URL jarURL = fJar.toURI().toURL();
                        vecLibs.add(jarURL);
                    } catch (MalformedURLException ex) {
                        System.err.println("");
                    }

                }
            }

            //copy URLs to Array
            URL[] jarURLs = new URL[vecLibs.size()];
            vecLibs.toArray(jarURLs);
            //finally create the custom editor class loader with the plugin libraries and return it.
            editorClassLoader = new EditorClassLoader(jarURLs);

        }

        return editorClassLoader;

    }
        /**
         * API copied from the UNO class loader, converts a java classpath into a vector array.
         * @param urls
         * @param data
         * @param delimiter
         */
        private static void addUrls(Vector urls, String data, String delimiter) {
        StringTokenizer tokens = new StringTokenizer( data, delimiter );
        while ( tokens.hasMoreTokens() ) {
            try {
                urls.add( new File( tokens.nextToken() ).toURI().toURL() );
            } catch ( MalformedURLException e ) {
                // don't add this class path entry to the list of class loader
                // URLs
                System.err.println( "Launcher::" +
                    "getEditorLoader: bad pathname: " + e );
            }
        }
    }

   
}
