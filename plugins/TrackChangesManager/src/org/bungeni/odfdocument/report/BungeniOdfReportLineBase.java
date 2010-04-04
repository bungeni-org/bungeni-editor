
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
    HashMap<String,String> changeMap;
    TreeMap<String,Object> lineVariables = new TreeMap<String,Object>();

     protected HashMap<String, String> pastTenseForm = new HashMap<String,String>(){
        {
            put("insertion", "inserted");
            put("deletion", "deleted");
        }
    };

    public BungeniOdfReportLineBase(){
        changeMap = new HashMap<String,String>();
        changeContext = null;
    }

     public BungeniOdfReportLineBase(BungeniOdfChangeContext cxt, HashMap<String,String> cMap) {
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

}
