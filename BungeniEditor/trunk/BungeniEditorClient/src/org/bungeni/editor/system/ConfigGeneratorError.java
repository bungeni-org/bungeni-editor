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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Ashok Hariharan
 */
public class ConfigGeneratorError {

    private static Logger log = Logger.getLogger(ConfigGeneratorError.class.getName());

    private final String CONFIG_GENERATOR_FILE = BaseSystemConfig.SYSTEM_TRANSFORMER_ERROR +
            File.separator + "config_generator_errors.xml";

    private Document docError = null;

    public ConfigGeneratorError(){
        File ferrorDir = new File(BaseSystemConfig.SYSTEM_TRANSFORMER_ERROR);
        if (!ferrorDir.exists()) {
            ferrorDir.mkdirs();
        }
        initErrorFile();
    }

    private void initErrorFile() {
            Element rootElement = new Element("errors");
            Date currentDate = GregorianCalendar.getInstance().getTime();
            rootElement.setAttribute("date", currentDate.toString());
            docError = new Document(rootElement);
            saveFile();
    }


    public void add(
            String docType,
            String errorMessage,
            Exception ex
            ) {
      Element errors = docError.getRootElement();
      Element error = new Element("error");
      error.setAttribute("doctype", docType);
      error.addContent((new Element("message")).setText(errorMessage));
      error.addContent((new Element("exception")).setText(ExceptionUtils.getStackTrace(ex)));
      errors.addContent(error);
    }

    public void saveFile(){
        FileWriter fError = null;
        try {
            XMLOutputter xout = new XMLOutputter();
            fError = new FileWriter(CONFIG_GENERATOR_FILE);
            xout.output(docError, fError);
            fError.flush();
         } catch (IOException ex) {
            log.error("Error while saving errors file !", ex);
        } finally {
            if (null != fError) {
                try {
                    fError.close();
                } catch (IOException ex) {
                    log.error("Error while closing file !", ex);
                }
            }
        }
    }

    public void loadFile() throws FileNotFoundException, UnsupportedEncodingException, JDOMException, IOException{
        this.docError = CommonXmlUtils.loadFile(CONFIG_GENERATOR_FILE);
    }

}
