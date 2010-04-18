package org.bungeni.db;

//~--- non-JDK imports --------------------------------------------------------

import java.util.logging.Level;
import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Database connector class fro settings database
 * @author Administrator
 */
public class BungeniClientDB {
    private static org.apache.log4j.Logger log              = Logger.getLogger(BungeniClientDB.class.getName());
    private String                         DEFAULT_DB       = "process.db";
    private String                         DRIVER           = "org.h2.Driver";
    private String                         JDBC_PREFIX      = "jdbc:h2:";
    private String                         PASS_WORD        = "";
    private String                         TRACE_LEVEL_FILE = "TRACE_LEVEL_FILE=0";
    private String                         USER_NAME        = "sa";
    private String                         connection_string;
    private String                         current_database;
    private Connection                     db_connection;
    private String                         path_to_database;

    /*
     *
     *
     *
     */
    public BungeniClientDB(HashMap<String, String> map) {
        this.connection_string = map.get("ConnectionString");
        this.USER_NAME         = map.get("UserName");
        this.PASS_WORD         = map.get("Password");
        this.DRIVER            = map.get("Driver");
    }

    /**
     * Creates a new instance of BungeniClientDB
     * @param pathToDb
     * @param dbName
     */
    public BungeniClientDB(String pathToDb, String dbName) {
        if (dbName.equals("")) {
            current_database = DEFAULT_DB;
        } else {
            current_database = dbName;
        }

        path_to_database  = pathToDb;
        connection_string = JDBC_PREFIX + path_to_database + current_database + ";" + TRACE_LEVEL_FILE;
        log.debug("connection string = " + connection_string);
    }

    public boolean ConnectWithAutoCommitOff() {
        return Connect(";AUTOCOMMIT=OFF");
    }

    public boolean Connect(String extConString) {
        boolean bState = true;

        try {
            Class.forName(DRIVER);
            db_connection = DriverManager.getConnection(connection_string + extConString, USER_NAME, PASS_WORD);
        } catch (SQLException ex) {
            log.error("Connect:" + ex.getMessage());
            bState = false;
        } catch (ClassNotFoundException ex) {
            log.error("Connect:" + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }
    }

    public boolean Connect() {
        boolean bState = true;

        try {
            Class.forName(DRIVER);
            db_connection = DriverManager.getConnection(connection_string, USER_NAME, PASS_WORD);
        } catch (SQLException ex) {
            log.error("Connect:" + ex.getMessage());
            bState = false;
        } catch (ClassNotFoundException ex) {
            log.error("Connect:" + ex.getMessage());
            bState = false;
        } finally {
            return bState;
        }
    }


    public QueryResults QueryResults(String expression) {
        HashMap<String, Vector<Vector<String>>> qResults = Query(expression);
        QueryResults                            qr       = null;
        if (qResults != null) {
            qr = new QueryResults(qResults);
            return qr;
        } else {
            return null;
        }
    }

    public QueryResults ConnectAndQuery(String query) {
        this.Connect();
        QueryResults qr = QueryResults(query);
        this.EndConnect();
        return qr;
    }

    public int Update(List<String> queries, boolean bTransacted) {
        Statement st       = null;
        int       nReturns = 0;
        try {
            st = db_connection.createStatement();    // statement objects can be reused with
            // repeated calls to execute but we
            // choose to make a new one each time
            if (bTransacted) {
                st.addBatch("set autocommit off");
            }
            for (String squery : queries) {
                st.addBatch(squery);
            }
            if (bTransacted ) {
            st.addBatch("prepare commit x_trans");
            st.addBatch("commit");
            }
            st.executeBatch();
            st.close();
        } catch (SQLException ex) {
            log.error("Update: " + ex.getMessage());
            try {
                if (bTransacted) {
                if (!st.isClosed()) {
                    st.execute("rollback transaction x_trans");
                    st.close();
                }
                }
            } catch (SQLException ex1) {
                log.error("RollBack: " + ex.getMessage());
            }
        }

        return nReturns;
        

    }

    public int Update(String expression) {
        Statement st       = null;
        int       nReturns = 0;
        try {
            st = db_connection.createStatement();    // statement objects can be reused with
            // repeated calls to execute but we
            // choose to make a new one each time

            nReturns = st.executeUpdate(expression);    // run the query
            st.close();
        } catch (SQLException ex) {
            log.error("Update: " + ex.getMessage());
        } finally {
            return nReturns;
        }
    }

    public HashMap<String, Vector<Vector<String>>> Query(String expression) {
        Statement                               st            = null;
        ResultSet                               rs            = null;
        HashMap<String, Vector<Vector<String>>> query_results = new HashMap<String, Vector<Vector<String>>>();
        Vector<Vector<String>>                  results       = new Vector<Vector<String>>();
        Vector<Vector<String>>                  columns       = new Vector<Vector<String>>();
        Vector<String>                          columnsMeta   = new Vector<String>();

        /*
         * Vector<Vector<String>> columns = new Vector<Vector<String>>();
         */

        try {
            st = db_connection.createStatement();    // statement objects can be reused with

            // repeated calls to execute but we
            // choose to make a new one each time
            rs = st.executeQuery(expression);    // run the query

            // process result set metadata
            ResultSetMetaData meta   = rs.getMetaData();
            int               colmax = meta.getColumnCount();
            int               i;
            Object            o = null;

            for (int iMeta = 0; iMeta < colmax; ++iMeta) {

                // System.out.println("column no."+(iMeta+1)+" = "+meta.getColumnName(iMeta+1));
                columnsMeta.addElement(meta.getColumnName(iMeta + 1));
            }

            columns.addElement(columnsMeta);
            query_results.put("columns", columns);

            /*
             * columns.addElement(columnsMeta);
             * query_results.put("columns", columns);
             */
            boolean returned_results = false;

            for (; rs.next(); ) {
                Vector<String> resultsRow = new Vector<String>();

                for (i = 0; i < colmax; ++i) {
                    o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                    // with 1 not 0
                    resultsRow.addElement(o.toString());

                    // System.out.print(o.toString() + " | ");
                }

                // System.out.println(" ");
                results.addElement(resultsRow);
                returned_results = true;
            }

            log.debug("Query Results = " + results.size());

            if (returned_results) {
                query_results.put("results", results);
            }

            st.close();    // NOTE!! if you close a statement the associated ResultSet is
        } catch (SQLException ex) {
            log.error("query:" + ex.getLocalizedMessage());
        } finally {
            return query_results;
        }
    }

    public void EndConnect() {
        Statement st;

        try {
            st = db_connection.createStatement();
            st.execute("SHUTDOWN");
            st.close();
        } catch (SQLException ex) {
            log.error("EndConnect:" + ex.getMessage());
        }
    }

    public static BungeniClientDB defaultConnect() {
        return new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
    }
}
