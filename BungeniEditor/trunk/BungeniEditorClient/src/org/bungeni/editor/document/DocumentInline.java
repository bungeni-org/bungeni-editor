/*
 * Copyright (C) 2013 PC
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
package org.bungeni.editor.document;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.ooo.OOComponentHelper;
import org.jdom.Element;

/**
 *
 * @author PC
 */
public class DocumentInline {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentInline.class.getName());
 
    private String documentType;
    private String inlineType;

    private HashMap<String,String> metadatasMap = new HashMap<String,String>(0);
    
    
    /** Creates a new instance of DocumentSection */
    public DocumentInline() {
    }

    public DocumentInline(Element inlineType, String documentType) {
        setDocumentType(documentType);
        setInlineType(inlineType.getAttributeValue("name"));
        /** <metadatas><metadata name="alpha" ... > 
         */
        Element elemMetadatas = inlineType.getChild("metadatas");
        if (elemMetadatas != null) {
            List<Element> childrenMetadata = elemMetadatas.getChildren("metadata");
            if (childrenMetadata != null) {
                for (Element elemMetadata : childrenMetadata) {
                    String metaName = elemMetadata.getAttributeValue("name");
                    String metaDef = elemMetadata.getAttributeValue("default");
                    if (null == metaDef) {
                        metaDef = "";
                    }
                    this.metadatasMap.put(metaName, metaDef);
                }
            }
        }
  
    }

    
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getInlineType() {
        return inlineType;
    }

    public void setInlineType(String inlineType) {
        this.inlineType = inlineType;
    }

    public HashMap<String,String> getMetadatasMap(){
        return this.metadatasMap;
    }
    
    
    
    
}
