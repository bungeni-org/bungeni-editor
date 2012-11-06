/*
 *  Copyright (C) 2012 PC
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.bungeni.editor.config.BundlesReader;

/**
 *
 * @author Ashok Hariharan
 */
/**
 * Invoke only after loadMessageBundles(), requires messageBundlesFolder variable to be set prior to call
 *
 */
public class BungeniResourceBundleClassLoader extends ClassLoader {
   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniResourceBundleClassLoader.class.getName());

   private static BungeniResourceBundleClassLoader instance = null;

    private BungeniResourceBundleClassLoader(){}

    public static BungeniResourceBundleClassLoader getInstance(){
        if (instance == null) {
            instance = new BungeniResourceBundleClassLoader();
        }
        return instance;
    }

    
    @Override
    public URL findResource(String name) {
        URL urlFile = null;
        try {
            String prefPath = BundlesReader.getBundlesFolder() + File.separator + name;
            File fProps = new File(prefPath);
            urlFile = fProps.toURI().toURL();
        } catch (MalformedURLException ex) {
            log.error("findResource : " + ex.getMessage());
        } finally {
            return urlFile;
        }

    }
}
