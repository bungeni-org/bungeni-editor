package org.bungeni.connector.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.Document;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;

/**
 *
 * @author Dave
 */
public class RDBMSBungeniConnector implements IBungeniConnector {

    /**
     * Implementation specific members
     */
    private java.sql.Connection dbConnection = null;

    //!+CODE_REVIEW(ah,sep-2011) Factored out hardcoded parameters for driver, url , password etc
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;

    private String membersQuery = "SELECT ID,FIRST_NAME,LAST_NAME,URI,ROLE FROM PUBLIC.PERSONS ";
    private String billsQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    private String metadataInfoQuery = "SELECT KEY_ID,KEY_TYPE,KEY_NAME,KEY_VALUE FROM PUBLIC.METADATA_INFO;";
    private String motionsQuery = "SELECT MOTION_ID,MOTION_URI,MOTION_TITLE,MOTION_NAME,MOTION_BY,MOTION_TEXT FROM PUBLIC.MOTIONS;";
    private String questionsQuery = "SELECT ID,QUESTION_TITLE,QUESTION_FROM,QUESTION_TO,QUESTION_TEXT FROM PUBLIC.QUESTIONS;";
    private String documentsQuery = "SELECT ID,DOCUMENT_TITLE,DOCUMENT_DATE,DOCUMENT_SOURCE,DOCUMENT_URI,SITTING_ID FROM PUBLIC.TABLED_DOCUMENTS;";

    private static Logger logger = Logger.getLogger(RDBMSBungeniConnector.class.getName());

    private ConnectorProperties m_props = null;

    public RDBMSBungeniConnector() {
    }

    /**
     * All the public APIs start below, these are all implemented by the
     * IBungeniConnector interface
     */

    /**
     * !+CODE_REVIEW(ah,sep-2011) See review comment above, the connection
     * parameters are setup below
     * @param props
     */
    public void init(ConnectorProperties props) {
        this.m_props = props;
        /**
        db-connection-string=jdbc:h2:{0}/settings/datasource/db/registry.db
        db-user=sa
        db-password=
        db-driver=org.h2.Driver
         **/
        this.dbDriver = m_props.getProperties().getProperty("db-driver");
        this.dbUsername = m_props.getProperties().getProperty("db-user");
        this.dbPassword = m_props.getProperties().getProperty("db-password");
        MessageFormat mfConnString = new MessageFormat(props.getProperties().
                getProperty("db-connection-string"));
        Object[] userDir = {System.getProperty("user.dir")};
        this.dbUrl = mfConnString.format(userDir);
    }


    public List<Member> getMembers() {
        List<Member> items = new java.util.ArrayList<Member>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getMembersQuery());
                while (resultSet.next()) {
                    Member item = new Member();
                    item.setId(resultSet.getInt(1));
                    item.setFirst(resultSet.getString(2));
                    item.setLast(resultSet.getString(3));
                    item.setUri(resultSet.getString(4));
                    item.setRole(resultSet.getString(5));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

    public List<Bill> getBills() {
        List<Bill> items = new java.util.ArrayList<Bill>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getBillsQuery());
                while (resultSet.next()) {
                    Bill item = new Bill();
                    item.setId(resultSet.getInt(1));
                    item.setName(resultSet.getString(2));
                    item.setUri(resultSet.getString(3));
                    item.setOntology(resultSet.getString(4));
                    item.setCountry(resultSet.getString(5));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

    public List<Motion> getMotions() {
        List<Motion> items = new java.util.ArrayList<Motion>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getMotionsQuery());
                while (resultSet.next()) {
                    Motion item = new Motion();
                    item.setId(resultSet.getString(1));
                    item.setUri(resultSet.getString(2));
                    item.setTitle(resultSet.getString(3));
                    item.setName(resultSet.getString(4));
                    item.setBy(resultSet.getString(5));
                    item.setText(resultSet.getString(6));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

    public List<Question> getQuestions() {
        List<Question> items = new java.util.ArrayList<Question>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getQuestionsQuery());
                while (resultSet.next()) {
                    Question item = new Question();
                    item.setId(resultSet.getInt(1));
                    item.setTitle(resultSet.getString(2));
                    item.setFrom(resultSet.getString(3));
                    item.setTo(resultSet.getString(4));
                    item.setText(resultSet.getString(5));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

    public List<MetadataInfo> getMetadataInfo() {
        List<MetadataInfo> items = new java.util.ArrayList<MetadataInfo>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getMetadataInfoQuery());
                while (resultSet.next()) {
                    MetadataInfo item = new MetadataInfo();
                    item.setId(resultSet.getInt(1));
                    item.setType(resultSet.getString(2));
                    item.setName(resultSet.getString(3));
                    item.setValue(resultSet.getString(4));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

    public List<Document> getDocuments() {
        List<Document> items = new java.util.ArrayList<Document>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getDocumentsQuery());
                while (resultSet.next()) {
                    Document item = new Document();
                    item.setId(resultSet.getString(1));
                    item.setTitle(resultSet.getString(2));
                    item.setDate(resultSet.getString(3));
                    item.setSource(resultSet.getString(4));
                    item.setUri(resultSet.getString(5));
                    item.setSitting(resultSet.getString(6));
                    items.add(item);
                }
                statement.close();
                resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException ex) {
                logger.error(ex);
            }
        } else {
            logger.error("DB Connection Error");
        }
        return items;
    }

   public void closeConnector() {
        close();
        logger.error("Connector Closed");
    }



    /**
     * All the Private APIs start below
     */
    private void close() {
        if (getDbConnection() != null) {
            try {
                getDbConnection().close();
            } catch (SQLException ex) {
                logger.error("DB Connection Close Error");
            }
        }
    }

    private boolean dbConnectionClosed() {
        boolean closed = true;
        try {
            closed = dbConnection.isClosed();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        return closed;
    }

    private Connection getDbConnection() {
        if (dbConnection == null || dbConnectionClosed()) {
            try {
                Class.forName(getDbDriver()).newInstance();
                dbConnection = java.sql.DriverManager.getConnection(getDbUrl(), getDbUsername(), getDbPassword());
            } catch (SQLException ex) {
                logger.error(ex);
            } catch (InstantiationException ex) {
                logger.error(ex);
            } catch (IllegalAccessException ex) {
                logger.error(ex);
            } catch (ClassNotFoundException ex) {
                logger.error(ex);
            }
        }
        return dbConnection;
    }

    private void setDbConnection(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private String getDbDriver() {
        return dbDriver;
    }

    private void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    private String getDbUrl() {
        String sURL = this.dbUrl.replace("/", File.separator);
        sURL = sURL.replace("%user_dir%", System.getProperty("user.dir"));
        logger.info("getDbUrl = " + sURL);
        return sURL;
    }


    public String getDbPassword() {
        return dbPassword;
    }


    public String getDbUsername() {
        return dbUsername;
    }


    private String getMembersQuery() {
        return membersQuery;
    }

    private String getBillsQuery() {
        return billsQuery;
    }

    private String getMetadataInfoQuery() {
        return metadataInfoQuery;
    }

    private String getMotionsQuery() {
        return motionsQuery;
    }

    private String getQuestionsQuery() {
        return questionsQuery;
    }


    private String getDocumentsQuery() {
        return documentsQuery;
    }

}
