package org.bungeni.odfdocument.report;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;

/**
 * Outputs a report line
 * @author Ashok Hariharan
 */
public class BungeniOdfReportLine {
    BungeniOdfChangeContext changeContext;
    HashMap<String,String> changeMap;
    TreeMap<String,Object> lineVariables = new TreeMap<String,Object>();

    public BungeniOdfReportLine(BungeniOdfChangeContext cxt, HashMap<String,String> cMap) {
        this.changeContext = cxt;
        this.changeMap = cMap;
        buildLineVariables();

    }

    private void buildLineVariables(){
        lineVariables.put("CHANGE_TYPE" , changeMap.get("changeType"));
        lineVariables.put("CHANGE_TEXT", changeMap.get("changeText"));
        String precText = changeContext.getPrecedingSiblingText();
        String follText = changeContext.getFollowingSiblingText();
        lineVariables.put("CHANGE_DESC", ((precText.length() > 0)? "After : \n \"" + precText + "\"" :"") + ((follText.length() > 0)?"Before : \n \"" +follText + "\"" :""));
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

   
    
}
