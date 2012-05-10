package org.bungeni.connector.test;

import java.io.File;
import java.util.List;
import org.bungeni.connector.ConnectorProperties;
import org.bungeni.connector.client.BungeniConnector;
import org.bungeni.connector.element.*;
import org.bungeni.connector.server.DataSourceServer;

/**
 *
 * @author Dave
 */
public class ClientTest {

    public static void main(String args[]) {
        //start server
        ConnectorProperties cp = new ConnectorProperties(System.getProperty("user.dir")+ File.separator + "settings" + File.separator + "bungeni-connector.properties");        
        DataSourceServer.getInstance().loadProperties(cp);
        DataSourceServer.getInstance().startServer();

        BungeniConnector b = new BungeniConnector();
        b.init(cp);
        
        
   List<Bill> bills = b.getBills();

        if (bills != null) {
            System.out.println(":::::::::::::::BILLS:::::::::::::::::::");
            for (int i = 0; i < bills.size(); i++) {
                System.out.println(" id:  " + bills.get(i).getId());
                for(Name objName: (List<Name>)bills.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
      
            
     
     List<JudgementRegion> judgementRegions = b.getJudgementRegions();

        if (judgementRegions != null) {
            System.out.println(":::::::::::::::judgement Regions:::::::::::::::::::");
            for (int i = 0; i < judgementRegions.size(); i++) {
                System.out.println(" id:  " + judgementRegions.get(i).getId());
                for(Name objName: (List<Name>)judgementRegions.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }          
                
            
            
         
      List<JudgementLitigationType> judgementLitigationTypes = b.getJudgementLitigationTypes();

        if (judgementLitigationTypes != null) {
            System.out.println(":::::::::::::::judgement LitigationType:::::::::::::::::::");
            for (int i = 0; i < judgementLitigationTypes.size(); i++) {
                System.out.println(" id:  " + judgementLitigationTypes.get(i).getId());
                for(Name objName: (List<Name>)judgementLitigationTypes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }          
         
                
       List<JudgementCourt> judgementCourts = b.getJudgementCourts();

        if (judgementCourts != null) {
            System.out.println(":::::::::::::::judgement Court:::::::::::::::::::");
            for (int i = 0; i < judgementCourts.size(); i++) {
                System.out.println(" id:  " + judgementCourts.get(i).getId());
                for(Name objName: (List<Name>)judgementCourts.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }          
                
                
        
        List<JudgementDomain> judgementDomains = b.getJudgementDomains();

        if (judgementDomains != null) {
            System.out.println(":::::::::::::::judgementDomain:::::::::::::::::::");
            for (int i = 0; i < judgementDomains.size(); i++) {
                System.out.println(" id:  " + judgementDomains.get(i).getId());
                for(Name objName: (List<Name>)judgementDomains.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        } 
        
        
        
        
         List<Act> acts = b.getActs();

        if (acts != null) {
            System.out.println(":::::::::::::::ACTS:::::::::::::::::::");
            for (int i = 0; i < acts.size(); i++) {
                System.out.println(" id:  " + acts.get(i).getId());
                for(Name objName: (List<Name>)acts.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        } else {
             System.out.println("Error : Acts missing");
        }
        
        List<MetadataInfo> metadata = b.getMetadataInfo();

        if (metadata != null) {
            System.out.println(":::::::::::::::METADATAINFO:::::::::::::::::::");
            for (int i = 0; i < metadata.size(); i++) {
                System.out.println(metadata.get(i).getName() + " " + metadata.get(i).getType()+ " " + metadata.get(i).getValue());
            }
        }

        List<Member> members = b.getMembers();

        if (members != null) {
            System.out.println(":::::::::::::::MEMBERS:::::::::::::::::::");
            for (int i = 0; i < members.size(); i++) {
                System.out.println(members.get(i).getFirst() + " " + members.get(i).getLast());
            }
        }

        List<Motion> motions = b.getMotions();

        if (motions != null) {
            System.out.println(":::::::::::::::MOTIONS:::::::::::::::::::");
            for (int i = 0; i < motions.size(); i++) {
                System.out.println(motions.get(i).getUri() + " " + motions.get(i).getTitle());
            }
        }


        List<Question> questions = b.getQuestions();

        if (questions != null) {
            System.out.println(":::::::::::::::QUESTIONS:::::::::::::::::::");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i).getTitle() + " " + questions.get(i).getText());
            }
        }

        
     

         List<SourceType> SourceTypes = b.getSourceTypes();

        if (SourceTypes != null) {
            System.out.println(":::::::::::::::SourceTypes:::::::::::::::::::");
            for (int i = 0; i < SourceTypes.size(); i++) {
                System.out.println(" id:  " + SourceTypes.get(i).getId());
                for(Name objName: (List<Name>)SourceTypes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        } else {
             System.out.println("Error : SourceTypes missing");
        }
        
        List<Document> documents = b.getDocuments();

        if (documents == null) {
            System.out.println("Error : Documents missing");
        }
        else
        {
            System.out.println(":::::::::::::::DOCUMENTS:::::::::::::::::::");
            for (int i = 0; i < documents.size(); i++) {
                System.out.println(documents.get(i).getTitle() + " " + documents.get(i).getUri());
            }
        }

        b.closeConnector();
        DataSourceServer.getInstance().stopServer();

    }
}
