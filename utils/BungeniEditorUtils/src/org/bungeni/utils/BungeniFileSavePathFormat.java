/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import org.bungeni.uri.BungeniManifestationName;
import org.bungeni.uri.BungeniURI;

/**
 *
 * @author undesa
 */
public class BungeniFileSavePathFormat {
    
    BungeniURI workURI;
    BungeniURI expURI;
    BungeniManifestationName fileNameFormat;
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniFileSavePathFormat.class.getName());
  
    public BungeniFileSavePathFormat(String wURI, String eURI, String fnFormat){
        workURI = new BungeniURI(wURI);
        expURI = new BungeniURI(eURI);
        fileNameFormat = new BungeniManifestationName(fnFormat);
    }

    public void setSaveComponent(String compName, String compValue) {
        try {
            setURIFormatComponent(workURI, compName, compValue );
            setURIFormatComponent(expURI, compName, compValue );
            setURIFormatComponent(fileNameFormat, compName, compValue );
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.debug("setSaveComponent missing compName : " + compName);
        }
    }
    
    public void parseComponents(){
        this.expURI.parse();
        this.workURI.parse();
        this.fileNameFormat.parse();
    }
    
    public String getExpressionPath(){
        return this.expURI.get();
    }
    
    public String getManifestationName(){
        return this.fileNameFormat.get();
    }
    
    public void setSaveComponent(String compName , int compValue) {
        String sCompValue = "";
        sCompValue = Integer.toString(compValue);
        setSaveComponent(compName, sCompValue);
    }

    private void setURIFormatComponent(BungeniURI uriComp, String compName, String compValue) {
        try {
            uriComp.setURIComponent(compName, compValue);
        } catch (ArrayIndexOutOfBoundsException ex) {
           log.debug("setURIFormatComponent (BungeniURI) " + ex.getMessage()); 
        }
    }
    
    private void setURIFormatComponent(BungeniManifestationName uriComp, String compName, String compValue) {
        try {
            uriComp.setURIComponent(compName, compValue);
        } catch (ArrayIndexOutOfBoundsException ex) {
           log.debug("setURIFormatComponent (BungeniManifestationName) " + ex.getMessage()); 
        }
    }
    
    
    /*
    private ArrayList<String> savePathFormatOrder = new ArrayList<String>();
    private HashMap<String,String> savePathComponents = new HashMap<String,String>(){
        {
            put("CountryCode", "");
            put("LanguageCode", "");
            put("Year","");
            put("DocumentType", "");
            put("Month", "");
            put("Day", "");
            put("Identifier", "");
            put("PartName", "");
            put("FileName", "");
        }
    };
    
    public BungeniFileSavePathFormat() {
        //get save path format
        //Save Path format is stored in the 'defaultSaveFormat' general editor property.
        String saveFormat = BungeniEditorProperties.getEditorProperty("defaultSaveFormat");
        String[] arrSaveFormat = saveFormat.split("[.]");
        for (String formatComponent : arrSaveFormat) {
            savePathFormatOrder.add(formatComponent);
        }
    }
    
    public void setSaveComponent(String comp, String value) {
        if (savePathComponents.containsKey(comp)) {
            savePathComponents.put(comp, value);
        }
    }
    
    public void setSaveComponent(String comp, int value) {
        Integer iValue = value;
        if (savePathComponents.containsKey(comp)) {
            savePathComponents.put(comp, iValue.toString());
        }
    }
    
    
    
    public HashMap<String,String> getSaveComponents(){
        return savePathComponents;
    }
    
    public String getSavePath() {
        String fullSavePath = "";
        for (int i=0; i < this.savePathFormatOrder.size(); i++) {
            String saveComp = savePathFormatOrder.get(i);
            if (!saveComp.equals("FileName"))
                fullSavePath = fullSavePath + savePathComponents.get(saveComp) + File.separator;
        }
        return fullSavePath;
    }
    

    public String getFileName(){
        String fullFilename = "";
        for (int i=0; i < this.savePathFormatOrder.size(); i++) {
            String saveComp = savePathFormatOrder.get(i);
            if (!saveComp.equals("FileName"))
                fullFilename = fullFilename + savePathComponents.get(saveComp) + "_";
        }
        if (fullFilename.endsWith("_")) {
            fullFilename = fullFilename.substring(0, fullFilename.length() - 1 );
        }
        return fullFilename+".odt";
    } */
}
