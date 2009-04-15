package org.bungeni.editor.document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import org.bungeni.db.QueryResults;

/**
 *
 * @author Administrator
 */
public class DocumentSection {
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSection.class.getName());
 
    private String documentType;
    private String sectionType;
    private String sectionNamePrefix;
    private String sectionNumberingStyle;
    private String sectionVisibility;
    private int sectionBackground = 0xffffff;
    private String sectionBackgroundURL = "";
    private boolean isSectionBackgroundURL = false;
    private double sectionLeftMargin = 0;
    private double sectionRightMargin = 0;
    private String sectionNumberingScheme = "";
    private String sectionNumberDecorator = "";
    private boolean Protected = false;
    
    
    /** Creates a new instance of DocumentSection */
    public DocumentSection() {
    }

    public DocumentSection(QueryResults qr, Vector<String> row) {
        setDocumentType(qr.getField(row, "DOC_TYPE"));
        setSectionType(qr.getField(row, "SECTION_TYPE_NAME"));
        setSectionNamePrefix(qr.getField(row, "SECTION_NAME_PREFIX"));
        setSectionNumberingStyle(qr.getField(row, "SECTION_NUMBERING_STYLE"));
        setSectionBackground(qr.getField(row, "SECTION_BACKGROUND"));
        setSectionLeftMargin(qr.getField(row, "SECTION_INDENT_LEFT"));
        setSectionRightMargin(qr.getField(row, "SECTION_INDENT_RIGHT"));
        setSectionVisibility(qr.getField(row, "SECTION_VISIBILITY"));
        setNumberingScheme(qr.getField(row, "NUMBERING_SCHEME"));
        setNumberDecorator(qr.getField(row, "NUMBER_DECORATOR"));
        
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
        try {
        if (sectionBackground.startsWith("0x")) {
            //this is a colored section background
           this.sectionBackground = Integer.decode(sectionBackground.trim());
        } else if (sectionBackground.startsWith("url:")) {
            String urlPath = sectionBackground.replaceAll("url:", "");
            String relPath = System.getProperty("user.dir");
            this.sectionBackgroundURL = relPath + urlPath;
            this.isSectionBackgroundURL = true;
        } else {
            this.sectionBackground = 0xffffff;
        }
       } catch (NumberFormatException ex) {
            this.sectionBackground = 0xffffff;
            log.error("setSectionBackground : there was an error parsing the section background");
      }
    }



    public void setSectionLeftMargin(String sectionLeftMargin) {
      try {
        this.sectionLeftMargin = Double.parseDouble(sectionLeftMargin.trim());
       } catch (NumberFormatException ex) {
            this.sectionLeftMargin = 0;
            log.error("setSectionLeftMargin : error while formatting number :" + sectionLeftMargin);
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
        try {
            this.sectionRightMargin = Double.parseDouble(sectionRightMargin.trim());
        } catch (NumberFormatException ex) {
            this.sectionRightMargin = 0;
            log.error("setSectionRightMargin : error while formatting number:"+sectionRightMargin);
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
    
    public HashMap<String,Object> getSectionProperties(){
        HashMap<String,Object> propsMap = new HashMap<String,Object>();
        propsMap.put ("BackColor", getSectionBackground());
        propsMap.put ("SectionLeftMargin", getSectionLeftMargin());
        propsMap.put ("SectionRightMargin", getSectionRightMargin());
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
        this.sectionVisibility = visibility;
    }
    
    public static void main(String[] args) {
        DocumentSection s = new DocumentSection();
        s.setSectionBackground("0xff");
        s.setSectionLeftMargin(".3");
        s.setSectionRightMargin(".5");
        System.out.println(s.getSectionBackground());
        System.out.println(s.getSectionLeftMargin());
        System.out.println(s.getSectionRightMargin());
        
    }

    public void setNumberDecorator(String string) {
        this.sectionNumberDecorator = string;
    }

    public String getNumberDecorator(){
        return this.sectionNumberDecorator;
    }
    
}

