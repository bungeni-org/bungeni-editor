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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.InlineTypesReader;
import org.bungeni.editor.config.SectionTypesReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * This class is the transformer generator for the Editor
 *
 * This generates a mergedConfiguration file for downstream transformation :
 *
 * <allConfigs>
 *   <sectionTypes ..>
 *     <sectionType .. />
 *     <sectionType .. />
 *   </sectionTypes>
 *   <inlineTypes ....>
 *     <inlineType .../>
 *     <inlineType .../>
 *
 *   <inlineTypes/>
 *
 *
 * </allConfigs>
 *
 *
 * @author Ashok Hariharan
 */
public final class ConfigurationProvider {

     private static final Logger log =
             Logger.getLogger(ConfigurationProvider.class.getName());

     private Document thisDocument = null;

    /**
     * Singleton class, so a private constructor
     */
    private ConfigurationProvider(){
      // generateMergedConfiguration(BungeniEditorPropertiesHelper.getCurrentDocType());
    }

    private static ConfigurationProvider thisInstance = null;

    public static ConfigurationProvider getInstance(){
        if (null == thisInstance) {
            thisInstance = new ConfigurationProvider();
        }
        return thisInstance;
    }

    public Document getMergedDocument(){
        return this.thisDocument;
    }

    /**
     * Generates a merged configuration document in memory
     * This is called during instantiation of the ConfigurationProvider instance
     * @param forDocType
     */
    public void generateMergedConfiguration(ConfigGeneratorError err, String forDocType){
        //merge sectionType and inlineTypes configuration.
        //This is the root element for the temporary merged config document
        Element allConfigs = new Element("allConfigs");
        Document docAllConfigs = new Document(allConfigs);
        //!+PROP_METADATA_SUPPORT (ah, 03-12-2012) Adding proprietary metadata
        //support - adding <prop> metadata from doctype 
        try {
            addDocTypesConfig(forDocType, allConfigs);
        } catch (Exception ex){
         log.error(ex);
             err.add(forDocType, "Error while processing section type config "
                     + "while merging section Type configuration", ex);
        }
        
        try {
         addSectionTypesConfig(forDocType, allConfigs);
        }  catch (Exception ex) {
         log.error(ex);
             err.add(forDocType, "Error while processing section type config "
                     + "while merging section Type configuration", ex);
        }
        try {
         addInlineTypesConfig(forDocType, allConfigs);
        } catch (Exception ex) {
             err.add(forDocType, "Error while processing inline type config "
                     + "while merging inline type configuration", ex);
        }
        this.thisDocument = docAllConfigs ;
    }

    public void writeMergedConfig(File f) throws IOException {
        FileWriter fout = new FileWriter(f);
        XMLOutputter xout = new XMLOutputter();
        xout.output(this.getMergedDocument(), fout);
        fout.close();
    }

    private void addDocTypesConfig(String docType, Element allConfigs) {
       // DocTypesReader.getInstance().get
        Element docTypeElem = DocTypesReader.getInstance().getDocTypeByNameClone(docType);
        Element outputs = DocTypesReader.getInstance().getOutputsBlock();
        Element docOutputs = null;
        if (outputs != null) {
            docOutputs = (Element) outputs.clone();
            allConfigs.addContent(docOutputs);
        }
        if (docTypeElem != null) {
            allConfigs.addContent(docTypeElem);
        }
    }
    
    private void addSectionTypesConfig(String docType, Element allConfigs) {
        List<Element> sectionTypes = new ArrayList<Element>(0);
        sectionTypes = SectionTypesReader.getInstance().getSectionTypesClone(docType);
        if (sectionTypes != null) {
            try {
                allConfigs.addContent(sectionTypes);
            } catch (Exception ex) {
                log.error("Error while importing section Types", ex);
            }
        }
    }

    private void addInlineTypesConfig(String docType, Element allConfigs) {
        //!+FIX_THIS - Add handler for missing config information !
        List<Element> inlineTypes = new ArrayList<Element>(0);
        inlineTypes = InlineTypesReader.getInstance().getInlineTypesClone(
                docType
                );
        if (inlineTypes != null) {
            try {
                allConfigs.addContent(inlineTypes);
            }catch(Exception ex) {
                log.error("Error while importing inline Types", ex);
            }
        }
    }

    

}
