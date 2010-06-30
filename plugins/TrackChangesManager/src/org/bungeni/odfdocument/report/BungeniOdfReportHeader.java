

package org.bungeni.odfdocument.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A report header contains report lines - and also other child report headers
 * @author Ashok Hariharan
 */
public class BungeniOdfReportHeader {
    private List<BungeniOdfReportLine> reportLines = new ArrayList<BungeniOdfReportLine>(0);
    private List<BungeniOdfReportHeader> reportHeaders = new ArrayList<BungeniOdfReportHeader>(0);
    private final Map<String,String> reportHeaderVariables = initMap();

    private Map<String,String> initMap (){
        Map<String,String> aMap = new HashMap<String,String>();
            aMap.put("SECTION_TYPE", "");
            aMap.put("SECTION_NAME", "");
            aMap.put("SECTION_ID", "");
           return aMap;
    }

    public BungeniOdfReportHeader(){

    }

    public BungeniOdfReportHeader(List<BungeniOdfReportLine> lines , List<BungeniOdfReportHeader> headers ){
        this.reportHeaders = headers;
        this.reportLines = lines;
    }

    /**
     * @return the reportLines
     */
    public List<BungeniOdfReportLine> getReportLines() {
        return reportLines;
    }

    /**
     * @param reportLines the reportLines to set
     */
    public void setReportLines(List<BungeniOdfReportLine> reportLines) {
        this.reportLines = reportLines;
    }

    /**
     * @return the reportHeaders
     */
    public List<BungeniOdfReportHeader> getReportHeaders() {
        return reportHeaders;
    }

    /**
     * @param reportHeaders the reportHeaders to set
     */
    public void setReportHeaders(List<BungeniOdfReportHeader> reportHeaders) {
        this.reportHeaders = reportHeaders;
    }

    public void setHeaderValue(String key, String value) {
        this.reportHeaderVariables.put(key, value);
    }

    public String getHeaderValue (String key) {
          return this.reportHeaderVariables.get(key);
    }

    public Set<String> getHeaderVariableNames(){
        return this.reportHeaderVariables.keySet();
    }

    public static void main (String[] args) {

    }

    


}
