package org.bungeni.connector.test;

import java.util.List;
import org.bungeni.connector.element.*;
import org.bungeni.connector.impl.XMLBungeniConnector;

/**
 *
 * @author Dave
 */
public class XMLBungeniConnectorTest{

    public XMLBungeniConnectorTest() {
    }


    public static void main(String args[]) {
        XMLBungeniConnector connector = new XMLBungeniConnector();

        List<ActType> actTypes = connector.getActTypes();

        if (actTypes != null) {
            System.out.println(":::::::::::::::ACTS:::::::::::::::::::");
            for (int i = 0; i < actTypes.size(); i++) {
                System.out.println(" id:  " + actTypes.get(i).getId());
                for(Name objName: (List<Name>)actTypes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }

        List<ActScope> actScopes = connector.getActScopes();

        if (actScopes != null) {
            System.out.println(":::::::::::::::ACT SCOPES:::::::::::::::::::");
            for (int i = 0; i < actScopes.size(); i++) {
                System.out.println(" id:  " + actScopes.get(i).getId());
                for(Name objName: (List<Name>)actScopes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        
//         List<ActHistoricalPeriod> actHistoricalPeriods = connector.getActHistoricalPeriods();
//
//        if (actHistoricalPeriods != null) {
//            System.out.println(":::::::::::::::ACT HistoricalPeriodS:::::::::::::::::::");
//            for (int i = 0; i < actHistoricalPeriods.size(); i++) {
//                System.out.println(" id:  " + actHistoricalPeriods.get(i).getId());
//                for(Name objName: (List<Name>)actHistoricalPeriods.get(i).getNames()){
//                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
//                }
//            }
//        }
//        
//         List<ActFamily> actFamilies = connector.getActFamilies();
//
//        if (actFamilies != null) {
//            System.out.println(":::::::::::::::ACT SCOPES:::::::::::::::::::");
//            for (int i = 0; i < actFamilies.size(); i++) {
//                System.out.println(" id:  " + actFamilies.get(i).getId());
//                for(Name objName: (List<Name>)actFamilies.get(i).getNames()){
//                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
//                }
//            }
//        }
        
        List<Bill> bills = connector.getBills();
        if (bills != null) {
            System.out.println(":::::::::::::::BILLS:::::::::::::::::::");
            for (int i = 0; i < bills.size(); i++) {
                System.out.println(" id:  " + bills.get(i).getId());
                for(Name objName: (List<Name>)bills.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }
        List<Motion> motions = connector.getMotions();

        if (motions != null) {
            System.out.println(":::::::::::::::MOTIONS:::::::::::::::::::");
            for (int i = 0; i < motions.size(); i++) {
                System.out.println(motions.get(i).getUri() + " " + motions.get(i).getTitle());
            }
        }

        List<Question> questions = connector.getQuestions();

        if (questions != null) {
            System.out.println(":::::::::::::::QUESTIONS:::::::::::::::::::");
            for (int i = 0; i < questions.size(); i++) {
                System.out.println(questions.get(i).getTitle() + " " + questions.get(i).getText());
            }
        }
        
        
        List<SourceType> SourceTypes = connector.getSourceTypes();

        if (SourceTypes != null) {
            System.out.println(":::::::::::::::SourceTypes:::::::::::::::::::");
            for (int i = 0; i < SourceTypes.size(); i++) {
                System.out.println(" id:  " + SourceTypes.get(i).getId());
                for(Name objName: (List<Name>)SourceTypes.get(i).getNames()){
                    System.out.println(" lang:  " + objName.getLang() + ", value: " + objName.getValue());
                }
            }
        }

        List<MetadataInfo> meta = connector.getMetadataInfo();

        if (meta != null) {
            System.out.println(":::::::::::::::METADATA:::::::::::::::::::");
            for (int i = 0; i < meta.size(); i++) {
                System.out.println(meta.get(i).getName() + " " + meta.get(i).getValue());
            }
        }
    }
}
