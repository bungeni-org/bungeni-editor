package org.bungeni.db;

import java.util.Vector;

/**
 *
 * @author undesa
 */
public interface IQueryResultsIterator {
    public boolean iterateRow(QueryResults mQR, Vector<String> rowData);
}
