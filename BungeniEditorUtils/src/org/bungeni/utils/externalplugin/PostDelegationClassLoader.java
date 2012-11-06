package org.bungeni.utils.externalplugin;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
/**
 * Adapted from http://tech.puredanger.com/2001/11/09/classloader/ (Alex Miller)
 * @author Ashok Hariharan
 */
public class PostDelegationClassLoader extends URLClassLoader {
    public PostDelegationClassLoader(URL[] urls) {
        super(urls);
    }

    public PostDelegationClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PostDelegationClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {

        // First check whether it's already been loaded
        Class loadedClass = findLoadedClass(name);

        // Nope, try to load it
        if (loadedClass == null) {
            try {

                // Ignore parent delegation and just try to load locally
                loadedClass = findClass(name);
            } catch (ClassNotFoundException e) {

                // Swallow - does not exist locally
                System.out.println("loadClass " + e.getMessage());
            }

            // If not found, just use the standard URLClassLoader (which follows normal parent delegation)
            if (loadedClass == null) {

                // throws ClassNotFoundException if not found in delegation hierarchy at all
                loadedClass = super.loadClass(name);
            }
        }

        return loadedClass;
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);

        if (null == url) {
            url = super.findResource(name);
        }

        return url;
    }
}
