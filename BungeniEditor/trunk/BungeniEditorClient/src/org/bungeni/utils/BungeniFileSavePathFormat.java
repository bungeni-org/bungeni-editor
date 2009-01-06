/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.bungeni.editor.BungeniEditorProperties;

/**
 *
 * @author undesa
 */
public class BungeniFileSavePathFormat {
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
    }
}
