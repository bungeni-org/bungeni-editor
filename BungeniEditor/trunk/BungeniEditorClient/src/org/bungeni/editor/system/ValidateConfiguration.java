/*
 * Copyright (C) 2013 Africa i-Parliaments
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import nl.jj.swingx.gui.modal.JModalFrame;
import org.apache.commons.io.FileUtils;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.SectionTypesReader;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Ashok HAriharan
 */
public class ValidateConfiguration {

    String CONFIG_SCHEMAS_FOLDER = "schemas";
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(ValidateConfiguration.class.getName());
    String configSchemasPath =
            BaseConfigReader.getSettingsFolder()
            + File.separator
            + CONFIG_SCHEMAS_FOLDER;
    HashMap<String, ConfigInfo> xsdConfigInfo = new HashMap<String, ConfigInfo>();

    public ValidateConfiguration() {

        this.xsdConfigInfo.put(
                "docTypes",
                new ConfigInfo("doctypes.xsd", "docTypes"));
        this.xsdConfigInfo.put(
                "sectionTypes",
                new ConfigInfo("sectionTypes.xsd", "sectionTypes"));
        this.xsdConfigInfo.put(
                "inlineTypes",
                new ConfigInfo("inlineTypes.xsd", "inlineTypes"));

    }

    public class ConfigInfo {

        String xsdName;
        String xsdFor;
        List<ErrorInfo> errors = new ArrayList<ErrorInfo>(0);
        
        public class ErrorInfo {
            String fileName  ; 
            List<SAXParseException> exceptions =
                    new ArrayList<SAXParseException>(0);
            public ErrorInfo(String fileName, List<SAXParseException> exceptions) {
                this.fileName = fileName;
                this.exceptions = exceptions;
            }
            
            @Override
            public String toString(){
                return xsdFor + ":" +  fileName;
            }
        }

        ConfigInfo(String xsdName, String xsdFor) {
            this.xsdFor = xsdFor;
            this.xsdName = xsdName;
        }

        public String getXsdPath() {
            return configSchemasPath + File.separator + this.xsdName;
        }

        public FileInputStream getXsdInputStream() throws FileNotFoundException {
            return new FileInputStream(
                    new File(getXsdPath()));
        }

        public void setExceptions(String fileName, List<SAXParseException> exceptions) {
            ErrorInfo info = new ErrorInfo(fileName, exceptions);
            this.errors.add(info);
        }
        
        List<ErrorInfo> realErrors = null;

        public boolean hasExceptions() {
            if (this.errors == null) {
                return false;
            } else if (this.errors.isEmpty()) {
                return false;
            } else {
                if (getExceptions().isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public List<ErrorInfo> getExceptions() {
            if (realErrors == null ) {
                realErrors = new ArrayList<ErrorInfo>(0);
                for(ErrorInfo info : this.errors) {
                    if (!info.exceptions.isEmpty()) {
                        realErrors.add(info);
                    }
                }
            } 
            return realErrors;
        }
        
        @Override
        public String toString(){
            return this.xsdFor;
        }
    }

    public boolean areThereExceptions() {
        Iterator<String> keys = this.xsdConfigInfo.keySet().iterator();
        while (keys.hasNext()) {
            ConfigInfo cfgInfo = xsdConfigInfo.get(keys.next());
            if (cfgInfo.hasExceptions()) {
                return true;
            }
        }
        return false;
    }

    public void validateAll() {
        // start with the doc type 
        String docTypesFile = DocTypesReader.RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE;
        List<SAXParseException> dtypeExceptions = validate(new File(docTypesFile), this.xsdConfigInfo.get("docTypes"));
        this.xsdConfigInfo.get("docTypes").setExceptions(
                DocTypesReader.DOCTYPES_FILE,
                dtypeExceptions
                );
        String [] extensions = {"xml"};
        // sectionTypes 
        String sectionTypesFolder = SectionTypesReader.getSettingsFolder();
        Iterator<File> fileSectionTypes = FileUtils.iterateFiles(
                new File(sectionTypesFolder), 
                extensions, 
                false
                );
        while(fileSectionTypes.hasNext()) {
            //ignore common.xml 
            File f = fileSectionTypes.next();
            if (!f.getName().equals("common.xml")) {
                List<SAXParseException> stypeExceptions = validate(f, this.xsdConfigInfo.get("sectionTypes"));
                this.xsdConfigInfo.get("sectionTypes").setExceptions(f.getName(), stypeExceptions);
            }
        }
        if (areThereExceptions()) {
            showExceptions();
        }
    }

    public void showExceptions() {
        JModalFrame frm = new JModalFrame();
        frm.setTitle("Configuration Errors");
        ConfigValidationErrorsPanel cfgPanel = new ConfigValidationErrorsPanel(frm, this.xsdConfigInfo);
        frm.getContentPane().add(cfgPanel);
        frm.pack();
        frm.centerOfScreen();
        frm.setVisible(true);
        frm.waitForClose();
    }

    public class ResourceResolver implements LSResourceResolver {

        public LSInput resolveResource(String type, String namespaceURI,
                String publicId, String systemId, String baseURI) {
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = new FileInputStream(
                        new File(
                        configSchemasPath + File.separator + systemId
                        )
                        );  
                return new Input(publicId, systemId, resourceAsStream);
            } catch (FileNotFoundException ex) {
                log.error(ex.getMessage());
            } finally {
                /**
                try {
                    if (resourceAsStream != null ) {
                        resourceAsStream.close();
                    }
                } catch (IOException ex) {
                    log.error(ex.getMessage());
                }**/
            }
            return null;
        }
    }

    public class Input implements LSInput {

        private String publicId;
        private String systemId;

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public String getBaseURI() {
            return null;
        }

        public InputStream getByteStream() {
            return null;
        }

        public boolean getCertifiedText() {
            return false;
        }

        public Reader getCharacterStream() {
            return null;
        }

        public String getEncoding() {
            return null;
        }

        public String getStringData() {
            synchronized (inputStream) {
                try {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }
            }
        }

        public void setBaseURI(String baseURI) {
        }

        public void setByteStream(InputStream byteStream) {
        }

        public void setCertifiedText(boolean certifiedText) {
        }

        public void setCharacterStream(Reader characterStream) {
        }

        public void setEncoding(String encoding) {
        }

        public void setStringData(String stringData) {
        }

        public String getSystemId() {
            return systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public BufferedInputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(BufferedInputStream inputStream) {
            this.inputStream = inputStream;
        }
        private BufferedInputStream inputStream;

        public Input(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }
    }

    public List<SAXParseException> validate(File xmlFile, ConfigInfo config) {
        final List<SAXParseException> exceptions = new LinkedList<SAXParseException>();
        try {
            String pathToXSD = config.getXsdPath();
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver(new ResourceResolver());
            Schema schema = factory.newSchema(new StreamSource(config.getXsdInputStream()));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    exceptions.add(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    exceptions.add(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    exceptions.add(exception);
                }
            });
            StreamSource streamXML = new StreamSource(xmlFile);
            validator.validate(streamXML);
        } catch (SAXException ex) {
            log.error("Error during validation", ex);
        } catch (IOException ex) {
            log.error("Error during validation", ex);
        } finally {
        }
        return exceptions;
    }
}
