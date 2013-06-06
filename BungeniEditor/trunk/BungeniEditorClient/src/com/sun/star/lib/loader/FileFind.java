/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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

package com.sun.star.lib.loader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 *This class is used to look for Jar files used by NOA which come with openoffice.
 *
 * Makes use of commons-io library
 * 
 * @author Ashok
 */
public class FileFind {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FileFind.class.getName());



    /**
     * Looks for files recursively in a directory tree
     * @param inFolder
     * @param findTheseFiles
     * @return
     */
    public static URL[] findFiles(String inFolder, String[] findTheseFiles ) {
        List<URL> urls = new ArrayList<URL>(0);
        Collection<File> files = FileUtils.listFiles(new File(inFolder), 
                new NameFileFilter(findTheseFiles),
                TrueFileFilter.INSTANCE);
        for (File file : files) {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException ex) {
                log.error("error while locating jar",  ex);
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }

 

}
