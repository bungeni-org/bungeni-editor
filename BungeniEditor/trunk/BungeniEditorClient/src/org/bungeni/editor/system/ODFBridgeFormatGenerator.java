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
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.editor.config.SysTransformsReader;
import org.bungeni.extutils.CommonFileFunctions;
import org.jdom.Document;

/**
 * This class is used to generate the ODF to Meta Language XSLT file.
 * This XSLT file is a bridge format when converting from ODF -> Akoma Ntoso.
 *
 * ODF is hard to work with , the bridge format is a intermediate system format,
 * not intended for customization or output but purely an intermediate simplification
 * of ODF for further processing in the Pipeline.
 *
 * Why is ODF hard to work with ? ODF is a composite package of XML documents that
 * separates content from metadata and styles, and also physically separates them
 * into different files. The bridge format collapses all these individual content,
 * metadata and style documents into 1 big Xml document.
 * 
 * This Bridge XSLT is generated out of different congfiguration files :
 * 
 *  - document metadata configuration (settings/config/metadata ) 
 *  - section metadata configuration (settings/config/section_types)
 *  - inline metadata configuration (settings/config/inline_types)
 *  - annotation metadata configuration (settings/config/annotation_types ) 
 *
 * @author Ashok Hariharan
 */
public class ODFBridgeFormatGenerator {

     private static final Logger log =
         Logger.getLogger(ODFBridgeFormatGenerator.class.getName());


     public class BridgeXSLT {
         public String XSLT ;
         public File inputFile;
         public File outputFile;

         public BridgeXSLT(String XSLT, File inputFile, File outputFile) {
             this.XSLT = XSLT;
             this.inputFile = inputFile;
             this.outputFile = outputFile;
         }
     }

     List<BridgeXSLT> bridgeXSLTs = new ArrayList<BridgeXSLT>(0);


     private ODFBridgeFormatGenerator(){}

     private static ODFBridgeFormatGenerator instance = null ;

     public static ODFBridgeFormatGenerator getInstance(){
         if (null == instance) {
             instance = new ODFBridgeFormatGenerator();
         }
         return instance;
     }

     public void addBridgeXSLT(String XSLT, File inputFile, File outputFile) {
         bridgeXSLTs.add(new BridgeXSLT(XSLT, inputFile, outputFile));
     }

     public void process (
            String docType
            ) throws FileNotFoundException{

         for (BridgeXSLT bxslt : bridgeXSLTs) {
             StreamSource xsltFile = SysTransformsReader.getInstance().getXslt(bxslt.XSLT);

         }

         return;
     }

     public static void main(String[] args){
         /***
         ODFBridgeFormatGenerator inst = ODFBridgeFormatGenerator.getInstance();
         String xsltName =  BaseSystemConfig.SYSTEM_GENERATOR + File.separator + "meta_identi_publi_generator.xsl";
         String sFullXSLTpath= CommonFileFunctions.convertRelativePathToFullPath(xsltName);

         String configName = DocumentMetadataReader.SETTINGS_FOLDER + File.separator + "debaterecord.xml";
         String sFullConfig = CommonFileFunctions.convertRelativePathToFullPath(configName);

         String soutFile = BaseSystemConfig.SYSTEM_CACHE + File.separator + "out1.xsl";
         String sFullout = CommonFileFunctions.convertRelativePathToFullPath(soutFile);

         inst.addBridgeXSLT(
                "meta_identi_publi_generator.xsl",
                 new File(sFullConfig),
                 new File(sFullout)
                  ); **/
     }
}
