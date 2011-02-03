/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.document;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author undesa
 */
public class ReferencesSyntax {
/*
 * articlePrefix;art
 * 
 */
    String seedSyntax ;
    
    public final static HashMap<String,String> SYNTAX_COMPONENTS = new HashMap<String,String>()
    {
        {
            put("articleTypePrefix", "<artPrefix>");
            put("articleTypeSuffix", "<artSuffix>");
            put("articleType", "<artType>");
            put("referencePrefix", "<refPrefix>");
            put("referenceSuffix", "<refSuffix>");
            put("referencePlaceHolder", "<refPlaceHolder>");
        }
    };

    public static QuerySyntax newQS(String n, String v) {
        return new QuerySyntax(n, v);
    }
    
    
   
    public static class QuerySyntax extends Object {

        String Name;
        String Value;
        
        public QuerySyntax(String n , String v) {
            Name = n.trim();
            Value = v.trim();
        }
        
        public static QuerySyntax[] toArray(ArrayList<QuerySyntax> qs) {
                return qs.toArray(new QuerySyntax[qs.size()]);
        }
        

    };
    
    public ReferencesSyntax (String inputSyntax) {
        seedSyntax = inputSyntax;
    }
    
    public String getSyntax(QuerySyntax[] replacements) {
        String outputString = seedSyntax.toString();
        
        for (QuerySyntax repObj : replacements) {
            String lookForThisPattern = SYNTAX_COMPONENTS.get(repObj.Name);
            String replaceWithThis = repObj.Value;
            //System.out.println("look for this : " + lookForThisPattern);
            outputString = outputString.replaceAll(lookForThisPattern, replaceWithThis);
        }

        return outputString;
    }

    
    public static void main (String[] args){
        
        ReferencesSyntax synt = new ReferencesSyntax("<artPrefix><artType><artSuffix><refPrefix><refPlaceHolder><refSuffix>");
        ArrayList<ReferencesSyntax.QuerySyntax> qs = new ArrayList<ReferencesSyntax.QuerySyntax>(0);
        qs.add(ReferencesSyntax.newQS("articleTypePrefix", "before article "));
        qs.add(ReferencesSyntax.newQS("articleTypeSuffix", "after article "));
        qs.add(ReferencesSyntax.newQS("referencePrefix", "before ref "));
        qs.add(ReferencesSyntax.newQS("referenceSuffix", "after ref "));
        String out = synt.getSyntax(QuerySyntax.toArray(qs));
        System.out.println(out);
    }
    
    

}
