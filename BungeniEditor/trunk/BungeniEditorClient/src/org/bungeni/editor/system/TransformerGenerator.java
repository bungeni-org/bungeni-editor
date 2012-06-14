/*
 *  Copyright (C) 2012 Africa i-Parliaments
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

package org.bungeni.editor.system;

import java.util.logging.Level;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import org.apache.log4j.Logger;
import org.jdom.Document;

/**
 *
 * @author Ashok Hariharan
 */
public class TransformerGenerator {

 private static final Logger log =
     Logger.getLogger(TransformerGenerator.class.getName());

    private static TransformerGenerator instance = null;

    private TransformerFactory thisTransFactory = null;
    private Transformer thisTransformer = null;

    private TransformerGenerator() throws TransformerConfigurationException{
        thisTransFactory = net.sf.saxon.TransformerFactoryImpl.newInstance();
        thisTransformer = thisTransFactory.newTransformer();
    }

    public static TransformerGenerator getInstance(){
        if (null == instance) {
            try {
                instance = new TransformerGenerator();
            } catch (TransformerConfigurationException ex) {
              log.error("Error while initializaing TransformerGenerator object", ex);
            }
        }
        return instance;
    }

    public Document generateTemplates() {
        ConfigurationProvider configs = ConfigurationProvider.getInstance();
        Document mergedConfigs = configs.getMergedDocument();

        return mergedConfigs; 
    }



}
