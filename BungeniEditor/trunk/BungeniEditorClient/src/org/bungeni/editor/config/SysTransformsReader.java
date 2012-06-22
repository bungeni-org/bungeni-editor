/*
 *  Copyright (C) 2012 Africa iParliaments
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

package org.bungeni.editor.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.bungeni.editor.system.BaseSystemConfig;
import org.bungeni.extutils.CommonFileFunctions;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class SysTransformsReader extends BaseConfigReader {
    private static Logger log = Logger.getLogger(SysTransformsReader.class.getName());


    /**
     * Cached map of XSLT stylesheets
     */
    private HashMap<String, StreamSource> thisXsltMap = new HashMap<String, StreamSource>();

    private static SysTransformsReader thisInstance = null;

  

    private XPath xpathInstance = null;

    private SysTransformsReader(){

    }

    public static SysTransformsReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new SysTransformsReader();
        }
        return thisInstance;
    }



    public StreamSource getXslt(String xsltName ) throws FileNotFoundException {
        if (!thisXsltMap.containsKey(xsltName)){
            String relativePathtoXSLT = BaseSystemConfig.SYSTEM_GENERATOR + File.separator + xsltName;
            String sFullPath = CommonFileFunctions.convertRelativePathToFullPath(relativePathtoXSLT);
            File xsltFile = new File(sFullPath);
            if (xsltFile.exists()) {
                StreamSource sxslt = new StreamSource(xsltFile);
                thisXsltMap.put(xsltName, sxslt);
            } else {
                throw new FileNotFoundException("Xslt file :" + xsltName + " not found on path : "+ sFullPath);
            }
        }
        return thisXsltMap.get(xsltName);
    }


}
