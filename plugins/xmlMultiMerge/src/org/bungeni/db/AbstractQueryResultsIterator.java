
package org.bungeni.db;

import java.util.Vector;

/**
 *
 * @author Ashok Hariharan
 */
public abstract class AbstractQueryResultsIterator implements IQueryResultsIterator {

    abstract public boolean iterateRow(QueryResults mQR, Vector<String> rowData);

}
