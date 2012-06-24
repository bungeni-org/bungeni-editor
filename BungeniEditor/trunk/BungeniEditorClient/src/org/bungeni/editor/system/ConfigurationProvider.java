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
    public void generateMergedConfiguration(String forDocType){
        //merge sectionType and inlineTypes configuration.
        //This is the root element for the temporary merged config document
        Element allConfigs = new Element("allConfigs");
        Document docAllConfigs = new Document(allConfigs);
        addSectionTypesConfig(allConfigs);
        addInlineTypesConfig(allConfigs);
        this.thisDocument = docAllConfigs ;
    }

    public void writeMergedConfig(File f) throws IOException {
        FileWriter fout = new FileWriter(f);
        XMLOutputter xout = new XMLOutputter();
        xout.output(this.getMergedDocument(), fout);
        fout.close();
    }

    private void addSectionTypesConfig(Element allConfigs) {
        List<Element> sectionTypes = new ArrayList<Element>(0);
        sectionTypes = SectionTypesReader.getInstance().getSectionTypesClone();
        if (sectionTypes != null) {
            try {
                allConfigs.addContent(sectionTypes);
            } catch (Exception ex) {
                log.error("Error while importing section Types", ex);
            }
        }
    }

    private void addInlineTypesConfig(Element allConfigs) {
        List<Element> inlineTypes = new ArrayList<Element>(0);
        inlineTypes = InlineTypesReader.getInstance().getInlineTypesClone();
        if (inlineTypes != null) {
            try {
                allConfigs.addContent(inlineTypes);
            }catch(Exception ex) {
                log.error("Error while importing inline Types", ex);
            }
        }
    }

    

    /**
    public static void main(String[] args){
        ConfigurationProvider gen = ConfigurationProvider.getInstance();
        gen.generateMergedConfiguration("debaterecord");
    }
     ***/
}
