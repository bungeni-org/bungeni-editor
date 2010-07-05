
package org.bungeni.odfdocument.report;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import org.bungeni.odfdom.document.changes.BungeniOdfChangeContext;

/**
 *
 * @author Ashok Hariharan
 */
public abstract class BungeniOdfReportLineBase {
    BungeniOdfChangeContext changeContext;
    HashMap<String,Object> changeMap;
    TreeMap<String,Object> lineVariables = new TreeMap<String,Object>();
    private String reportLineSectionName = "";
     protected HashMap<String, String> pastTenseForm = new HashMap<String,String>(){
        {
            put("insertion", "inserted");
            put("deletion", "deleted");
        }
    };

    public BungeniOdfReportLineBase(){
        changeMap = new HashMap<String,Object>();
        changeContext = null;
    }

     public BungeniOdfReportLineBase(BungeniOdfChangeContext cxt, HashMap<String,Object> cMap) {
        this.changeContext = cxt;
        this.changeMap = cMap;
        init();
    }

    abstract void init();
    abstract public void buildLineVariables();

    public void addLineVariable(String key, Object value) {
        this.lineVariables.put(key, value);
    }

    public Object getLineVariable(String key) {
        return this.lineVariables.get(key);
    }

    public Set<String> getLineVariableNames() {
        return this.lineVariables.keySet();
    }

    /**
     * @return the reportLineSectionName
     */
    public String getReportLineSectionName() {
        return reportLineSectionName;
    }

    /**
     * @param reportLineSectionName the reportLineSectionName to set
     */
    public void setReportLineSectionName(String reportLineSectionName) {
        this.reportLineSectionName = reportLineSectionName;
    }

}
