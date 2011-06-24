/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.connector.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.Bill;
import org.bungeni.connector.element.MetadataInfo;
import org.bungeni.connector.element.Motion;
import org.bungeni.connector.element.Member;
import org.bungeni.connector.element.Question;

/**
 *
 * @author Dave
 */
public class RDBMSBungeniConnector implements IBungeniConnector {

    private java.sql.Connection dbConnection = null;
    private String dbUrl = "jdbc:h2:datasource/db/registry.db";
    private String dbUsername = "sa";
    private String dbPassword = "";
    private String dbDriver = "org.h2.Driver";
    private String membersQuery = "SELECT ID,FIRST_NAME,LAST_NAME,URI,ROLE FROM PUBLIC.PERSONS ";
    private String billsQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    private String metadataInfoQuery = "SELECT KEY_ID,KEY_TYPE,KEY_NAME,KEY_VALUE FROM PUBLIC.METADATA_INFO;";
    private String motionsQuery = "SELECT MOTION_ID,MOTION_URI,MOTION_TITLE,MOTION_NAME,MOTION_BY,MOTION_TEXT FROM PUBLIC.MOTIONS;";
    private String questionsQuery = "SELECT ID,QUESTION_TITLE,QUESTION_FROM,QUESTION_TO,QUESTION_TEXT FROM PUBLIC.QUESTIONS;";
    private static Logger logger = Logger.getLogger(RDBMSBungeniConnector.class.getName());

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

    public void close() {
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

    public Connection getDbConnection() {
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

    public void setDbConnection(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getMembersQuery() {
        return membersQuery;
    }

    public void setMembersQuery(String memberQuery) {
        this.membersQuery = memberQuery;
    }

    public String getBillsQuery() {
        return billsQuery;
    }

    public void setBillsQuery(String billQuery) {
        this.billsQuery = billQuery;
    }

    public String getMetadataInfoQuery() {
        return metadataInfoQuery;
    }

    public void setMetadataInfoQuery(String metadataInfoQuery) {
        this.metadataInfoQuery = metadataInfoQuery;
    }

    public String getMotionsQuery() {
        return motionsQuery;
    }

    public void setMotionsQuery(String motionsQuery) {
        this.motionsQuery = motionsQuery;
    }

    public String getQuestionsQuery() {
        return questionsQuery;
    }

    public void setQuestionsQuery(String questionsQuery) {
        this.questionsQuery = questionsQuery;
    }
}
