package org.bungeni.db;

import java.text.MessageFormat;


/**
 * This class is deprecated
 * @author Ashok Hariharan
 */
@Deprecated
public class RegistryQueryFactory {
    public RegistryQueryFactory() {
    }

    public static String Q_FETCH_JUDGEMENT_PARTIES(String judgementId) {
      
        String query = new String("" +
                "SELECT P.ID, P.PARTY_NAME, P.PARTY_TYPE " +
                "FROM PARTIES P INNER JOIN " +
                "JUDGEMENT_PARTIES JP ON JP.PARTY_ID = P.ID " +
                "WHERE JP.JUDGEMENT_ID  = '"+judgementId+"'");
        return  query;
    }

    public static String Q_FETCH_JUDGEMENT_JUDGES(String judgementId) {
        Object[] params = {judgementId};

        String query = MessageFormat.format(
            "SELECT J.FIRST_NAME, J.LAST_NAME, J.URI " +
            "FROM JUDGES J INNER JOIN JUDGEMENT_JUDGES JJ ON J.ID = JJ.JUDGE_ID " + 
            "WHERE JJ.JUDGEMENT_ID  = ''{0}''", params);
            return query;
    }
    
    public static String Q_FETCH_JUDGEMENTS (){
        String query = "SELECT ID, NAME, JUDGEMENT_DATE, HEARING_DATE FROM JUDGEMENTS";
        return query;
    }

 
}
