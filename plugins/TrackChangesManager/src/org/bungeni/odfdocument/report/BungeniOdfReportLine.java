package org.bungeni.odfdocument.report;

import java.util.HashMap;
import java.util.TreeMap;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;

/**
 * Outputs a report line
 * @author Ashok Hariharan
 */
public class BungeniOdfReportLine extends BungeniOdfReportLineBase {

    public BungeniOdfReportLine(){

    }
    public BungeniOdfReportLine(BungeniOdfChangeContext ctx, HashMap<String,String> cmap) {
        super(ctx, cmap);
    }
    
    @Override
    public void buildLineVariables(){
        lineVariables.put("CHANGE_TYPE" , changeMap.get("changeType"));
        String sChangeText = "\n The following text : \n\"" + changeMap.get("changeText") +"\" \n was " + pastTenseForm.get(changeMap.get("changeType"));
        lineVariables.put("CHANGE_TEXT",sChangeText);
        String precText = changeContext.getPrecedingSiblingText();
        String follText = changeContext.getFollowingSiblingText();
        if (precText.length()  > 0) {
            lineVariables.put("AFTER_TITLE", "AFTER :");
            lineVariables.put("AFTER_CHANGE_DESC", "\"" + precText + "\"");
        } else {
            lineVariables.put("AFTER_TITLE", "[DELETE]");
            lineVariables.put("AFTER_CHANGE_DESC", "[DELETE]");
        }
        if (follText.length() > 0 ) {
            lineVariables.put("BEFORE_TITLE", "BEFORE :");
            lineVariables.put("BEFORE_CHANGE_DESC", "\"" + follText + "\"");
        } else {
            lineVariables.put("BEFORE_TITLE", "[DELETE]");
            lineVariables.put("BEFORE_CHANGE_DESC", "[DELETE]");
        }
    }

    @Override
    void init() {
        lineVariables = new TreeMap<String,Object>(){{
            put("CHANGE_TYPE", "");
            put("AFTER_TITLE", "");
            put("AFTER_CHANGE_DESC", "");
            put("BEFORE_TITLE", "");
            put("BEFORE_CHANGE_DESC", "");
            put("CHANGE_TEXT", "");
            put("SECTION_TYPE_INFO", "");
        }};
    }
    /*
    BungeniOdfChangeContext changeContext;
    HashMap<String,String> changeMap;
    TreeMap<String,Object> lineVariables = new TreeMap<String,Object>(){{
        put("CHANGE_TYPE", "");
        put("AFTER_TITLE", "");
        put("AFTER_CHANGE_DESC", "");
        put("BEFORE_TITLE", "");
        put("BEFORE_CHANGE_DESC", "");
        put("CHANGE_TEXT", "");
        put("SECTION_TYPE_INFO", "");
    }};

    public BungeniOdfReportLine(BungeniOdfChangeContext cxt, HashMap<String,String> cMap) {
        this.changeContext = cxt;
        this.changeMap = cMap;
        buildLineVariables();

    }

    private HashMap<String, String> pastTenseForm = new HashMap<String,String>(){
        {
            put("insertion", "inserted");
            put("deletion", "deleted");
        }
    };

    protected void buildLineVariables(){
        lineVariables.put("CHANGE_TYPE" , changeMap.get("changeType"));
        String sChangeText = "\n The following text : \n\"" + changeMap.get("changeText") +"\" \n was " + pastTenseForm.get(changeMap.get("changeType"));
        lineVariables.put("CHANGE_TEXT",sChangeText);
        String precText = changeContext.getPrecedingSiblingText();
        String follText = changeContext.getFollowingSiblingText();
        if (precText.length()  > 0) {
            lineVariables.put("AFTER_TITLE", "AFTER :");
            lineVariables.put("AFTER_CHANGE_DESC", "\"" + precText + "\"");
        } else {
            lineVariables.put("AFTER_TITLE", "[DELETE]");
            lineVariables.put("AFTER_CHANGE_DESC", "[DELETE]");
        }
        if (follText.length() > 0 ) {
            lineVariables.put("BEFORE_TITLE", "BEFORE :");
            lineVariables.put("BEFORE_CHANGE_DESC", "\"" + follText + "\"");
        } else {
            lineVariables.put("BEFORE_TITLE", "[DELETE]");
            lineVariables.put("BEFORE_CHANGE_DESC", "[DELETE]");
        }
    }

    public void addLineVariable(String key, Object value) {
        this.lineVariables.put(key, value);
    }

    public Object getLineVariable(String key) {
        return this.lineVariables.get(key);
    }

    public Set<String> getLineVariableNames() {
        return this.lineVariables.keySet();
    }

   */


    
}
