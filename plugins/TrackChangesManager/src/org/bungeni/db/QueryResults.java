package org.bungeni.db;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author Ashok Hariharan
 */
public class QueryResults {
    static int                             METADATA_ROW_INDEX    = 0;
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(QueryResults.class.getName());
    boolean                                hasResults            = false;
    HashMap<String, Integer>               metadataColumnNameMap = null;
    IQueryResultsIterator                  resultsIterator       = null;
    Vector<String>                         theColumns            = null;
    Vector<Vector<String>>                 theResults            = null;

    /** Creates a new instance of QueryResults */
    public QueryResults(HashMap<String, Vector<Vector<String>>> results) {
        if (results.containsKey("results")) {
            theResults = (Vector<Vector<String>>) results.get("results");
            hasResults = true;
        }

        metadataColumnNameMap = new HashMap<String, Integer>();

        if (results.containsKey("columns")) {
            Vector<Vector<String>> tmpColumns = (Vector<Vector<String>>) results.get("columns");

            theColumns = new Vector<String>();
            buildMetadataInfo(tmpColumns.elementAt(0));

            // buildMetadataInfo((Vector<String>)results.get("columns"));
        }
    }

    public HashMap columnNameMap() {
        return metadataColumnNameMap;
    }

    public void print_columns() {
        Set      keys     = metadataColumnNameMap.keySet();
        String[] keyNames = new String[keys.size()];

        keyNames = (String[]) keys.toArray();
        System.out.println("Printing Columns");

        for (int i = 0; i < keyNames.length; i++) {
            System.out.println(keyNames[i]);
        }
    }

    public boolean hasResults() {
        return hasResults;
    }

    public Vector<Vector<String>> theResults() {
        return theResults;
    }

    public java.util.Iterator<Vector<String>> theResultsIterator() {
        return theResults.iterator();
    }

    private void buildMetadataInfo(Vector<String> metadataRow) {
        if (hasResults) {
            if (theResults.size() > 0) {

                // build metadata column map
                for (int n = 0; n < metadataRow.size(); n++) {
                    String column_name = "";

                    column_name = (String) metadataRow.elementAt(n);
                    theColumns.add(column_name);

                    // add to column name ==> column number mapping map
                    metadataColumnNameMap.put(column_name, new Integer(n + 1));
                }
            }
        }
    }

    public int getColumnIndex(String column_name) {
        if (metadataColumnNameMap.containsKey(column_name)) {
            Integer column = (Integer) metadataColumnNameMap.get(column_name);

            return (int) column;
        }

        return -1;
    }

    public String[] getColumns() {
        String[] arrayColumns = theColumns.toArray(new String[theColumns.size()]);

        return arrayColumns;
    }

    public int getColumnCount() {
        return theColumns.size();
    }

    public String getField(Vector<String> row, String fieldName) {
        return row.elementAt(getColumnIndex(fieldName) - 1).trim();
    }

    public Vector<String> getColumnsAsVector() {
        return theColumns;
    }

    public String[] getSingleColumnResult(String theColumn) {
        String[] specificresult = null;

        if (hasResults()) {
            specificresult = new String[theResults.size()];

            for (int i = 0; i < theResults.size(); i++) {
                Vector<String> tableRow = theResults.elementAt(i);

                specificresult[i] = tableRow.elementAt(getColumnIndex(theColumn) - 1);
            }

            return specificresult;
        } else {
            return null;
        }
    }

    public void resultsIterator(IQueryResultsIterator iterResults) {
        this.resultsIterator = iterResults;

        if (hasResults()) {
            Vector<Vector<String>> resultRows = new Vector<Vector<String>>();

            resultRows = theResults();

            for (Vector<String> resultRow : resultRows) {
                if (!iterResults.iterateRow(this, resultRow)) {
                    break;
                }
            }
        } else {
            System.out.println("No Results Found");
        }
    }
}
