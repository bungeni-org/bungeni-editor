/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.db;


/**
 *
 * @author undesa
 */
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
        String query = new String("" +
            "SELECT J.ID, J.FIRST_NAME, J.LAST_NAME, J.URI " +
            "FROM JUDGES J INNER JOIN JUDGEMENT_JUDGES JJ ON J.ID = JJ.JUDGE_ID " + 
            "WHERE JJ.JUDGEMENT_ID  = '" + judgementId +  "'");
            return query;
    }
    
    public static String Q_FETCH_JUDGEMENTS (){
        String query = new String("" +
                "SELECT ID, NAME, JUDGEMENT_DATE, HEARING_DATE FROM JUDGEMENTS");
        return query;
    }
}
