package org.bungeni.connector.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.IBungeniConnector;
import org.bungeni.connector.element.*;

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
    private String judgementDomainsQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    private String judgementRegionsQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    
    private String judgementLitigationTypesQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    
    private String judgementCourtsQuery = "SELECT ID,BILL_NAME,BILL_URI,BILL_ONTOLOGY,COUNTRY FROM PUBLIC.BILLS";
    
    private String actsQuery = "SELECT ID,ACT_NAME FROM PUBLIC.ACTS";
    private String sourceTypesQuery = "SELECT ID,SourceType_NAME FROM PUBLIC.SOURCETYPES";
    private String metadataInfoQuery = "SELECT KEY_ID,KEY_TYPE,KEY_NAME,KEY_VALUE FROM PUBLIC.METADATA_INFO;";
    private String motionsQuery = "SELECT MOTION_ID,MOTION_URI,MOTION_TITLE,MOTION_NAME,MOTION_BY,MOTION_TEXT FROM PUBLIC.MOTIONS;";
    private String questionsQuery = "SELECT ID,QUESTION_TITLE,QUESTION_FROM,QUESTION_TO,QUESTION_TEXT FROM PUBLIC.QUESTIONS;";
    private String documentsQuery = "SELECT ID,DOCUMENT_TITLE,DOCUMENT_DATE,DOCUMENT_SOURCE,DOCUMENT_URI,SITTING_ID FROM PUBLIC.TABLED_DOCUMENTS;";
    private String committeeQuery = "SELECT ID, COMMITTEE_NAME, COMMITTEE_URI, COUNTRY FROM PUBLIC.COMMITTEES" ;

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
                    item.addName(new Name(resultSet.getString(2)));
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
    
    
    public List<JudgementDomain> getJudgementDomains() {
        List<JudgementDomain> items = new java.util.ArrayList<JudgementDomain>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getJudgementDomainsQuery());
                while (resultSet.next()) {
                    JudgementDomain item = new JudgementDomain();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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
    
    
    
    public List<JudgementRegion> getJudgementRegions() {
        List<JudgementRegion> items = new java.util.ArrayList<JudgementRegion>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getJudgementRegionsQuery());
                while (resultSet.next()) {
                    JudgementRegion item = new JudgementRegion();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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
    
    
    
    
    
    
    public List<JudgementLitigationType> getJudgementLitigationTypes() {
        List<JudgementLitigationType> items = new java.util.ArrayList<JudgementLitigationType>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getJudgementLitigationTypesQuery());
                while (resultSet.next()) {
                    JudgementLitigationType item = new JudgementLitigationType();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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
    
    
    
    
    public List<JudgementCourt> getJudgementCourts() {
        List<JudgementCourt> items = new java.util.ArrayList<JudgementCourt>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getJudgementCourtsQuery());
                while (resultSet.next()) {
                    JudgementCourt item = new JudgementCourt();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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
    
    

     public List<ActType> getActs() {
        List<ActType> items = new java.util.ArrayList<ActType>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getBillsQuery());
                while (resultSet.next()) {
                   
                    ActType item = new ActType();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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

      public List<SourceType> getSourceTypes() {
        List<SourceType> items = new java.util.ArrayList<SourceType>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getSourceTypesQuery());
                while (resultSet.next()) {
                    SourceType item = new SourceType();
                    item.setId(resultSet.getInt(1));
                    item.addName(new Name(resultSet.getString(2)));
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

    // !+ BUNGENI CONNECTOR (rm, 2012) - added this method to enable the
    // retrieval of the committees into a table which the user then selects the
    // committee for which a particular bill is for
    public List<Committee> getCommittees() {

        List<Committee> committees = new java.util.ArrayList<Committee>();
        if (getDbConnection() != null) {
            try {
                java.sql.Statement statement = getDbConnection().createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery(getCommitteesQuery());
                while (resultSet.next()) {
                    Committee committee = new Committee();
                    committee.setId(resultSet.getString(1));
                    committee.addName(new Name(resultSet.getString(2)));
                    committee.setURI(resultSet.getString(3));
                    committee.setCountry(resultSet.getString(3));

                    committees.add(committee);
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
        return committees;
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
    
    private String getJudgementDomainsQuery() {
        return judgementDomainsQuery;
    }
    
    private String getJudgementRegionsQuery() {
        return judgementRegionsQuery;
    }
    
    
    
     private String getJudgementLitigationTypesQuery() {
        return judgementLitigationTypesQuery;
    }
   
    
    
     private String getJudgementCourtsQuery() {
        return judgementCourtsQuery;
    }
    
    
    
    private String getActsQuery() {
        return actsQuery;
    }
     
    private String getSourceTypesQuery() {
        return sourceTypesQuery;
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

    private String getCommitteesQuery() {
        return committeeQuery ;
    }

    public List<ActType> getActTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActScope> getActScopes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActFamily> getActFamilies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActHistoricalPeriod> getActHistoricalPeriods() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SrcName> getSrcNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActCategory> getActCategories() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActCategoryBasic> getActCategoriesBasic() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ActOrganization> getActOrganizations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}