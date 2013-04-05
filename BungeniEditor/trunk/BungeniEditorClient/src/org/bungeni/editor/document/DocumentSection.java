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
 * @author Administrator
 */
public final class DocumentSection {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSection.class.getName());
 
    private String documentType;
    private String sectionType;
    private String sectionNamePrefix;
    private String sectionNumberingStyle;
    private String sectionVisibility;
    private Integer sectionBackground = 0xffffff;
    private String sectionBackgroundURL = "";
    private boolean isSectionBackgroundURL = false;
    private double sectionLeftMargin = 0;
    private double sectionRightMargin = 0;
    private String sectionNumberingScheme = "";
    private String sectionNumberDecorator = "";
    private boolean Protected = false;
    private HashMap<String,String> metadatasMap = new HashMap<String,String>(0);
    
    
    /** Creates a new instance of DocumentSection */
    public DocumentSection() {
    }

    public DocumentSection(Element sectionType, String documentType) {
        setDocumentType(documentType);
        setSectionType(sectionType.getAttributeValue("name"));
        setSectionNamePrefix(sectionType.getAttributeValue("prefix"));
        setSectionNumberingStyle(sectionType.getAttributeValue("numstyle"));
        setSectionBackground(sectionType.getAttributeValue("background"));
        setSectionLeftMargin(sectionType.getAttributeValue("indent-left"));
        setSectionRightMargin(sectionType.getAttributeValue("indent-right"));
        setSectionVisibility(sectionType.getAttributeValue("visibility"));
        /**
         * <numbering scheme="ROMAN"
            decorator="hashPrefix" />
         */
        Element elemNumbering = sectionType.getChild("numbering");
        if (elemNumbering != null) {
             setNumberingScheme(elemNumbering.getAttributeValue("scheme"));
             setNumberDecorator(elemNumbering.getAttributeValue("decorator"));
        } else {
             setNumberingScheme("none");
             setNumberDecorator("none");
        }
        /** <metadatas><metadata name="alpha" ... > 
         */
        Element elemMetadatas = sectionType.getChild("metadatas");
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

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionNamePrefix() {
        return sectionNamePrefix;
    }

    public void setSectionNamePrefix(String sectionNamePrefix) {
        this.sectionNamePrefix = sectionNamePrefix;
    }

    public String getSectionNumberingStyle() {
        return sectionNumberingStyle;
    }

    public void setSectionNumberingStyle(String sectionNumberingStyle) {
        this.sectionNumberingStyle = sectionNumberingStyle;
    }

    public int getSectionBackground() {
            return new Integer(sectionBackground);
    }

    public boolean isSectionBackgroundURL() {
        return this.isSectionBackgroundURL;
    }
    
    public URL getSectionBackgroundURL() {
        URL url = null;
        try {
             url = new URL(this.sectionBackgroundURL);
        } catch (MalformedURLException ex) {
           log.error("getSectionBackgroundURL : " + ex.getMessage());
        } finally {
            return url;
        }
    }

    public void setSectionBackground(int sectionBackground) {
        this.sectionBackground = sectionBackground;
    }

    
    public void setSectionBackground(String sectionBackground) {
        if (null == sectionBackground) {
             this.sectionBackground = 0xffffff;
        } else {
            try {
            if (sectionBackground.startsWith("0x")) {
                //this is a colored section background
               this.sectionBackground = Integer.decode(sectionBackground.trim());
            } else if (sectionBackground.startsWith("url:")) {
                String urlPath = sectionBackground.replaceAll("url:", "");
                //url: uses a relative path !+FIX_THIS support file:// urls too
                String relPath = urlPath.replace('/', File.separatorChar);
                relPath = BaseConfigReader.configsFolder() + File.separator + relPath;
                this.sectionBackground = null;
                this.sectionBackgroundURL = relPath ;
                this.isSectionBackgroundURL = true;
            } else {
                this.sectionBackground = 0xffffff;
            }
           } catch (NumberFormatException ex) {
                this.sectionBackground = 0xffffff;
                log.error("setSectionBackground : there was an error parsing the section background");
          }
        }
    }



    public void setSectionLeftMargin(String sectionLeftMargin) {
      if (null == sectionLeftMargin) {
          this.sectionLeftMargin = 0;
      } else {
          try {
            this.sectionLeftMargin = Double.parseDouble(sectionLeftMargin.trim());
           } catch (NumberFormatException ex) {
                this.sectionLeftMargin = 0;
                log.error("setSectionLeftMargin : error while formatting number :" + sectionLeftMargin);
          }
        }
    }
    
    public void setSectionLeftMargin(double sectionLeftMargin) {
        this.sectionLeftMargin = sectionLeftMargin;
    }

    public Integer getSectionRightMargin(){
        return marginDoubleToInteger(sectionRightMargin);
    }
    
    
    private Long marginDoubleToLong(Double margin) {
        Double bigMargin = margin * 10 * 254;
        return bigMargin.longValue();
    }
    
    private Integer marginDoubleToInteger(Double margin) {
         Double bigMargin = margin * 10 * 254;
        return bigMargin.intValue();
    }
    
    
    public Integer getSectionLeftMargin(){
        return marginDoubleToInteger(sectionLeftMargin);
    }
    
   
    public void setSectionRightMargin(String sectionRightMargin) {
        if (null == sectionRightMargin) {
            this.sectionRightMargin = 0;
        } else {
            try {
                this.sectionRightMargin = Double.parseDouble(sectionRightMargin.trim());
            } catch (NumberFormatException ex) {
                this.sectionRightMargin = 0;
                log.error("setSectionRightMargin : error while formatting number:"+sectionRightMargin);
            }
        }
    }
    
    public void setSectionRightMargin(double sectionRightMargin) {
        this.sectionRightMargin = sectionRightMargin;
    }

    public boolean isProtected() {
        return Protected;
    }

    public void setProtected(boolean Protected) {
        this.Protected = Protected;
    }
    
    private HashMap<String,Object> createSectionPropertyMap(OOComponentHelper ooDocument) {

        HashMap<String,Object> propsMap = new HashMap<String,Object>();
        if (isSectionBackgroundURL()) {
                File f = new File (this.sectionBackgroundURL);
                if (f.exists()) {
                    try {
                        String bgURL = CommonFileFunctions.getFileAuthorityURL(f);
                        String graphicURL = ooDocument.loadGraphic(bgURL);
                        propsMap.put("BackGraphicURL", graphicURL);
                        propsMap.put("BackGraphicFilter", "PNG - Portable Network Graphic");
                        propsMap.put("BackGraphicLocation", com.sun.star.style.GraphicLocation.TILED);
                    } catch (MalformedURLException ex) {
                        log.error("createSectionPropertyMap : " + ex.getMessage());
                        propsMap.put ("BackColor",getSectionBackground());
                    }
                } else {
                    log.error("createSectionPropertyMap : wrong path to background URL");
                    propsMap.put ("BackColor",getSectionBackground());
                }
        } else {
            propsMap.put ("BackColor",getSectionBackground());
        }
        propsMap.put ("SectionLeftMargin", getSectionLeftMargin());
        propsMap.put ("SectionRightMargin", getSectionRightMargin());
        return propsMap;

    }


    public HashMap<String,Object> getSectionProperties(OOComponentHelper ooDocument){
        HashMap<String,Object> propsMap = new HashMap<String,Object>();
        propsMap = createSectionPropertyMap(ooDocument);
        return propsMap;
    }

    public String getSectionVisibilty() {
        return this.sectionVisibility;
    }
    
    public void setNumberingScheme(String scheme) {
        this.sectionNumberingScheme = scheme;
    }
    
    public String getNumberingScheme(){
        return this.sectionNumberingScheme;
    }
    
    public void setSectionVisibility(String visibility) {
        if (null == visibility) {
            this.sectionVisibility = "user";
        } else {
            this.sectionVisibility = visibility;
        }
    }
    

    public void setNumberDecorator(String string) {
        this.sectionNumberDecorator = string;
    }

    public String getNumberDecorator(){
        return this.sectionNumberDecorator;
    }
    
    public HashMap<String,String> getMetadatasMap(){
        return this.metadatasMap;
    }
    
}

