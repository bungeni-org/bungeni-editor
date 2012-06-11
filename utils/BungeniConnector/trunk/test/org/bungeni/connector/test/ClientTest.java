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
        
        
       List<ActOrganization> actOrganizations = b.getActOrganizations();

        if (actOrganizations != null) {
            System.out.println("::::::::::::::: ACT ORGANIZATIONS :::::::::::::::::::");
            for (int i = 0; i < actOrganizations.size(); i++) {
                System.out.println(" id:  " + actOrganizations.get(i).getId());
                for(Name objName: (List<Name>)actOrganizations.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
        List<ActFamily> actFamilies = b.getActFamilies();

        if (actFamilies != null) {
            System.out.println("::::::::::::::: ACT FAMILIES :::::::::::::::::::");
            for (int i = 0; i < actFamilies.size(); i++) {
                System.out.println(" id:  " + actFamilies.get(i).getId());
                for(Name objName: (List<Name>)actFamilies.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
                for(SubFamily objSubFamily: (List<SubFamily>)actFamilies.get(i).getSubFamilies()){
                    System.out.println(" lang:  " + objSubFamily.getLang() + ", value: " + objSubFamily.getValue());
                }
            }
        }
        
        List<ActCategory> ActCategories = b.getActCategories();

        if (ActCategories != null) {
            System.out.println("::::::::::::::: ACT CATEGORIES :::::::::::::::::::");
            for (int i = 0; i < ActCategories.size(); i++) {
                System.out.println(" id:  " + ActCategories.get(i).getId());
                for(Name objName: (List<Name>)ActCategories.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
         List<ActCategoryBasic> ActCategoriesBasic = b.getActCategoriesBasic();

        if (ActCategoriesBasic != null) {
            System.out.println("::::::::::::::: ACT CATEGORIES BASIC :::::::::::::::::::");
            for (int i = 0; i < ActCategoriesBasic.size(); i++) {
                System.out.println(" id:  " + ActCategoriesBasic.get(i).getId());
                for(Name objName: (List<Name>)ActCategoriesBasic.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
        List<SrcName> SrcNames = b.getSrcNames();

        if (SrcNames != null) {
            System.out.println("::::::::::::::: ACT SOURCE NAMES :::::::::::::::::::");
            for (int i = 0; i < SrcNames.size(); i++) {
                System.out.println(" id:  " + SrcNames.get(i).getId());
                for(Name objName: (List<Name>)SrcNames.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
        List<ActHistoricalPeriod> actHistoricalPeriods = b.getActHistoricalPeriods();

        if (actHistoricalPeriods != null) {
            System.out.println("::::::::::::::: ACT HISTORICAL PERIODS :::::::::::::::::::");
            for (int i = 0; i < actHistoricalPeriods.size(); i++) {
                System.out.println(" id:  " + actHistoricalPeriods.get(i).getId());
                for(Name objName: (List<Name>)actHistoricalPeriods.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
        
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
        
         List<ActType> actTypes = b.getActTypes();

        if (actTypes != null) {
            System.out.println(":::::::::::::::ACT TYPES:::::::::::::::::::");
            for (int i = 0; i < actTypes.size(); i++) {
                System.out.println(" id:  " + actTypes.get(i).getId());
                for(Name objName: (List<Name>)actTypes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        } else {
             System.out.println("Error : Act Types missing");
        }
        
         List<ActScope> actScopes = b.getActScopes();

        if (actScopes != null) {
            System.out.println(":::::::::::::::ACT Scopes:::::::::::::::::::");
            for (int i = 0; i < actScopes.size(); i++) {
                System.out.println(" id:  " + actScopes.get(i).getId());
                for(Name objName: (List<Name>)actScopes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        } else {
             System.out.println("Error : Act Scopes missing");
        }
        
        
//         List<ActHistoricalPeriod> actHistoricalPeriods = b.getActHistoricalPeriods();
//
//        if (actHistoricalPeriods != null) {
//            System.out.println(":::::::::::::::ACT Historical PeriodS:::::::::::::::::::");
//            for (int i = 0; i < actHistoricalPeriods.size(); i++) {
//                System.out.println(" id:  " + actHistoricalPeriods.get(i).getId());
//                for(Name objName: (List<Name>)actHistoricalPeriods.get(i).getNames()){
//                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
//                }
//            }
//        } else {
//             System.out.println("Error : Act Historical Periods missing");
//        }
        
        
//         List<ActFamily> actFamilies = b.getActFamilies();
//
//        if (actFamilies != null) {
//            System.out.println(":::::::::::::::ACT FAMILIES:::::::::::::::::::");
//            for (int i = 0; i < actFamilies.size(); i++) {
//                System.out.println(" id:  " + actFamilies.get(i).getId());
//                for(Name objName: (List<Name>)actFamilies.get(i).getNames()){
//                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
//                }
//            }
//        } else {
//             System.out.println("Error : Act Families missing");
//        }
        
        
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
