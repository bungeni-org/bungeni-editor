
package org.bungeni.editor.rulesimpl;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.dom4j.DocumentHelper;

/**
 * Class to parse section structural rules
 * @author Ashok Hariharan
 */
public class StructuralRulesParser extends RuleParser {
    private final static String GET_CHILD_REF_TYPES             = "allowedTypes/ref";
    private final static String GET_CHILD_SECTION_TYPES         = "allowedTypes/ref[@type='sectionType']";
    private final static String GET_CHILD_SECTION_TYPES_BY_NAME =
        "allowedTypes/ref[@type='sectionType' and @name='{refSectionType}']";
    private static final String            GET_DOC_STRUCTURE_TYPE = "/DocumentStructure/@type";
    private final static String            GET_SECTION_TYPE_NAME  =
        "/DocumentStructure/sectionType[@name='{thisSectionType}']";
    private final static String            GET_ORDER_OF_CHILDREN = "orderOfChildren/order";

    private ruleOrderOfChildrenByType ruleOrderBytype = null;

    private static org.apache.log4j.Logger log                    =
        org.apache.log4j.Logger.getLogger(StructuralRulesParser.class.getName());

    public StructuralRulesParser() {
        
    }
    /**
     * Path to rules Xml file
     * @param xmlRules
     */
    public StructuralRulesParser(String xmlRules) {
        pathToXmlFile = xmlRules;
        ruleOrderBytype = new ruleOrderOfChildrenByType();
    }

    /**
     * Get the structure type for the document
     * @return
     */
    public String getDocStructureType() {
        String typeValue = "";

        try {
            XPath  selectPath = DocumentHelper.createXPath(GET_DOC_STRUCTURE_TYPE);
            Object foundNode  = selectPath.selectSingleNode(xmlDocument);

            typeValue = ((Attribute) foundNode).getValue();
        } catch (Exception ex) {
            log.error("getDocStructureType :" + ex.getMessage());
        } finally {
            return typeValue;
        }
    }

    /**
     * Gets a section type element from the document;
     * @param sectionTypeName
     * @return an Element object matching the <sectionType /> element
     */
    public Element getSectionType(String sectionTypeName) {
        Element elemSecType = null;

        try {
            String xpathSectionType = GET_SECTION_TYPE_NAME.replaceAll("\\{thisSectionType\\}", sectionTypeName);

            System.out.println("this = " + xpathSectionType);

            XPath selectPath =DocumentHelper.createXPath(xpathSectionType);

            elemSecType = (Element) selectPath.selectSingleNode(this.xmlDocument);
        } catch (Exception ex) {
            System.out.println("getSectionType : " + ex.getMessage());
        } finally {
            return elemSecType;
        }
    }

    /**
     * Returns all allowed types for a section type, returns <ref /> elements
     * @param sectionTypeName
     * @return
     */
    public ArrayList<Element> getAllowedTypesForSectionType(String sectionTypeName) {
        ArrayList<Element> matchingNodes = new ArrayList<Element>(0);

        try {

            // first get the section type element
            Element thisSectionType = getSectionType(sectionTypeName);
            XPath   selectPath      = DocumentHelper.createXPath(GET_CHILD_REF_TYPES);
            List selectNodes = selectPath.selectNodes(thisSectionType);
            if (selectNodes.size() > 0 )
                matchingNodes = (ArrayList<Element>) selectNodes;
        } catch (Exception ex) {
            System.out.println("getAllowedTypesForSectionType :" + ex.getMessage());
        } finally {
            return matchingNodes;
        }
    }

    /**
     * Get the allowed section types for a parent section type
     * @param sectionTypeName
     * @return an ArrayList of <sectionType /> elements
     */
    public ArrayList<Element> getAllowedSectionTypesForType(String sectionTypeName) {
        ArrayList<Element> foundTypes = new ArrayList<Element>(0);

        try {
            Element            foundSectionType  = this.getSectionType(sectionTypeName);
            XPath              selectPath        = DocumentHelper.createXPath(GET_CHILD_SECTION_TYPES);
            ArrayList<Element> foundSectionTypes = new ArrayList<Element>(0);
            List selectedNodes = selectPath.selectNodes(foundSectionType);
            if (selectedNodes.size() > 0 )
                 foundSectionTypes = (ArrayList<Element>) selectedNodes;

            for (Element elemrefSecType : foundSectionTypes) {
                String  nameOfAllowedSectionType = elemrefSecType.attributeValue("name");
                Element secType                  = this.getSectionType(nameOfAllowedSectionType);

                if (secType != null) {
                    foundTypes.add(secType);
                } else {
                    log.error("getAllowedSectionTypesForType : " + sectionTypeName
                              + " has a reference to allowed section-type : " + nameOfAllowedSectionType
                              + " which does not exist");
                }
            }
        } catch (Exception ex) {
            log.error("getAllowedSectionTypesForType : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            return foundTypes;
        }
    }


    /**
     * Stores order of allowed children ... more than one element can be allowed
     * at a particulare order position
     */


   private static String ORDER_TYPE_SECTION_TYPE = "sectionType";

   /***
    * This structure captures allowed <order /> elements for a particular section Type.
    * e.g.
     <orderOfChildren>
    <order type="sectionType" name="Clause" order="1" required="yes"/>
    <order type="sectionType" name="Preface" order="0" required="yes"/>
    <order type="sectionType" name="MastHead" order="0" required="yes"/>
    <order type="sectionType" name="Conclusion" order="2" required="no"/>
    </orderOfChildren>
    * This structure does not hold informatin about the parent section Type
    */
    public class ruleOrderOfChildren {

        /**
         * Multiple elements can exist at an oder level, order level is the order/@order element
         */
        public  Integer nOrder ;
        /**
         * List of allowed elements for this order level, for this e.g. order level 0 can have both the "Preface" and the "MastHead"
         */
        public   ArrayList<Element> allowedElementsAtOrderPosition;

        public ruleOrderOfChildren (Element elem ) {
            nOrder = Integer.parseInt(elem.attributeValue("order"));
            allowedElementsAtOrderPosition.add(elem);
        }

        public ArrayList<String> getSectionTypesForRule() {
            ArrayList<String> allowedSectionTypes = new ArrayList<String>(0);
            for (Element element : allowedElementsAtOrderPosition) {
                String elemType = element.attributeValue("type");
                if (elemType.equals(ORDER_TYPE_SECTION_TYPE)) {
                    String sectionTypeName = element.attributeValue("name");
                    allowedSectionTypes.add(sectionTypeName);
                }
            }
            return allowedSectionTypes;
        }

        /**
         * Checks if a section type is valid for this order element i.e. if a section is indeed present at the specified order
         * @param aSectionType
         * @return
         */
        public boolean isSectionTypeOrderValid(String aSectionType) {
            for (Element element : allowedElementsAtOrderPosition) {
                String orderType = element.attributeValue("type");
                if (orderType.equals(ORDER_TYPE_SECTION_TYPE)) {
                    String orderName = element.attributeValue("name");
                    if (orderName.equals(aSectionType)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


    /**
     * This structure maps <order /> groupings by sectionType
     */
    public class ruleOrderOfChildrenByType {
        /**
         * Maps section types to order groupings
         */
         private HashMap<String, TreeMap<Integer, ruleOrderOfChildren>> rulesBySectionType  = new HashMap<String, TreeMap<Integer, ruleOrderOfChildren>>();

         public ruleOrderOfChildrenByType(){

         }

         public boolean hasRulesForSectionType(String secType) {
             return rulesBySectionType.containsKey(secType);
         }

         public TreeMap<Integer, ruleOrderOfChildren> getRulesForSectionType(String secType) {
             return rulesBySectionType.get(secType);
         }

         public void addRulesForSectionType(String secType, TreeMap<Integer, ruleOrderOfChildren> rules) {
            rulesBySectionType.put(secType, rules);
         }


         /**
          * Find the preceeding sectionType for sectionType within a parent sectionType ->order grouping
          * @param withinThisSectionType
          * @param forThisSectionType
          * @return returns an array of allowed preceeding sections
          */
         public ArrayList<String> getPreceedingAllowedSectionTypes(String withinThisSectionType, String forThisSectionType) {
             ArrayList<String> preceedingSectionTypes = new ArrayList<String>(0);
             try {
                 //check if the parent section type exists within the rule map
                 if (rulesBySectionType.containsKey(withinThisSectionType)) {
                     //get the allowe children
                     TreeMap<Integer, ruleOrderOfChildren> allowedOrderOfChildren = rulesBySectionType.get(withinThisSectionType);
                     Set<Integer> sectionOrder = allowedOrderOfChildren.keySet();
                     for (Integer iorder : sectionOrder) {
                          ruleOrderOfChildren roc = allowedOrderOfChildren.get(iorder);
                         //look for the child section type within the allowed map
                         if (roc.isSectionTypeOrderValid(forThisSectionType)) {
                            //if yes -- get preceeding order element
                             //if there is no preceeding order level then we are at the first level
                             Integer lowerKey = allowedOrderOfChildren.lowerKey(iorder);
                             if (lowerKey == null ) {
                                 return new ArrayList<String>() { { add("AT_FIRST"); }
                                 };
                             }
                             ruleOrderOfChildren previousROC = allowedOrderOfChildren.get(lowerKey);
                             preceedingSectionTypes = previousROC.getSectionTypesForRule();
                             break;
                         }
                     }
                 }
             } catch (Exception ex) {
                 log.error("getPreceedingAllowedSectionType : "+ ex.getMessage());
             } finally {
                 return preceedingSectionTypes;
             }
         }

         public ArrayList<String> getFollowingAllowedSectionTypes(String withinThisSectionType, String forThisSectionType) {
             ArrayList<String> followingSectionTypes = new ArrayList<String>(0);
             try {
                 //check if the parent section type exists within the rule map
                 if (rulesBySectionType.containsKey(withinThisSectionType)) {
                     //get the  allowe children
                     TreeMap<Integer, ruleOrderOfChildren> allowedOrderOfChildren = rulesBySectionType.get(withinThisSectionType);
                     Set<Integer> sectionOrder = allowedOrderOfChildren.keySet();

                     for (Integer iorder : sectionOrder) {
                          ruleOrderOfChildren roc = allowedOrderOfChildren.get(iorder);
                         //look for the child section type within the allowed map
                         if (roc.isSectionTypeOrderValid(forThisSectionType)) {
                            //if yes -- get next order element
                             //get the next higher order element
                              Integer higherKey = allowedOrderOfChildren.higherKey(iorder);
                             if (higherKey == null ) {
                                 return new ArrayList<String>() { { add("AT_LAST"); }
                                 };
                             }
                             ruleOrderOfChildren nextROC = allowedOrderOfChildren.get(higherKey);
                             followingSectionTypes = nextROC.getSectionTypesForRule();
                         }

                     }
                 }
             } catch (Exception ex) {
                 log.error("getFollowingAllowedSectionTypes : "+ ex.getMessage());
             } finally {
                 return followingSectionTypes;
             }

         }



    }



    public ArrayList<String> getFollowingSectionTypes (String withinThisType, String forThisType) {
        //call this first to update the ruleOrderByType object for thr required sectionType
        getOrderOfChildrenForType(withinThisType);
        return this.ruleOrderBytype.getFollowingAllowedSectionTypes(withinThisType, forThisType);
    }

    public ArrayList<String> getPreceedingSectionTypes (String withinThisType, String forThisType) {
        //call this first to update the ruleOrderByType object for thr required sectionType
        getOrderOfChildrenForType(withinThisType);
        return this.ruleOrderBytype.getPreceedingAllowedSectionTypes(withinThisType, forThisType);
    }

    public boolean isAllowedTypeAtPosition(String withinThisType, String lookForThisType, Integer atThisPosition) {
        TreeMap<Integer, ruleOrderOfChildren> orderOfChildren = getOrderOfChildrenForType(withinThisType);
        //check if the order is indeed valid
        if (orderOfChildren.containsKey(atThisPosition)) {
            ruleOrderOfChildren ruleObject = orderOfChildren.get(atThisPosition);
            boolean bState = ruleObject.isSectionTypeOrderValid(lookForThisType);
            return bState;
            //check rule object for element name at position
        } else
            return false;
    }

    private TreeMap<Integer, ruleOrderOfChildren> retrieveOrderRulesForType (String sectionTypeName) {
            TreeMap<Integer, ruleOrderOfChildren> childOrder= new TreeMap<Integer, ruleOrderOfChildren>();
        try {
            Element            foundSectionType  = this.getSectionType(sectionTypeName);
            XPath              selectPath        = DocumentHelper.createXPath(GET_ORDER_OF_CHILDREN);
            List selectNodes = selectPath.selectNodes(foundSectionType);
            ArrayList<Element> foundSectionTypes = new ArrayList<Element>(0);
            if (selectNodes.size() > 0 )
                foundSectionTypes = (ArrayList<Element>) selectNodes;

            for (Element elemrefSecType : foundSectionTypes) {
                Integer elementOrder = Integer.parseInt(elemrefSecType.attributeValue("order"));
                if (childOrder.containsKey(elementOrder)) {
                    childOrder.get(elementOrder).allowedElementsAtOrderPosition.add(elemrefSecType);
                } else {
                   ruleOrderOfChildren roc = new ruleOrderOfChildren(elemrefSecType);
                   childOrder.put(elementOrder, roc);
                }
            }
        } catch (Exception ex) {
            log.error("retrieveOrderRulesForType : " + ex.getMessage());
        } finally {
            return childOrder;
        }
    }

    public TreeMap<Integer, ruleOrderOfChildren> getOrderOfChildrenForType(String sectionTypeName) {
        if (this.ruleOrderBytype.hasRulesForSectionType(sectionTypeName)) {
            return this.ruleOrderBytype.getRulesForSectionType(sectionTypeName);
        } else {
            this.ruleOrderBytype.addRulesForSectionType(sectionTypeName, retrieveOrderRulesForType(sectionTypeName));
            return this.ruleOrderBytype.getRulesForSectionType(sectionTypeName);
        }
    }

    public boolean isAllowedSectionTypeForType(String sectionTypeName, String lookForThisType) {
        Element matchingChildSectionType = null;

        try {

            // Element foundSectionType = this.getSectionType(sectionTypeName);
            String xpathGetparentSection     = GET_SECTION_TYPE_NAME.replaceAll("\\{thisSectionType\\}",
                                                   sectionTypeName);
            String xpathGetChildSectionTypes = GET_CHILD_SECTION_TYPES_BY_NAME.replaceAll("\\{refSectionType\\}",
                                                   lookForThisType);
            XPath selectPath = DocumentHelper.createXPath(xpathGetparentSection + "/" + xpathGetChildSectionTypes);

            matchingChildSectionType = (Element) selectPath.selectSingleNode(xmlDocument);
        } catch (Exception ex) {
            log.error("isAllowedSectionTypeForType : " + ex.getMessage());
        } finally {
            return (matchingChildSectionType == null)
                   ? false
                   : true;
        }
    }

    public static void main(String[] args) {
        StructuralRulesParser srp =
            new StructuralRulesParser("/home/undesa/Projects/Bungeni/BungeniStructuralRules/editorDocRules.xml");

        srp.loadXml();
        System.out.println(srp.getDocStructureType());

        Element pref = srp.getSectionType("Preface");

        System.out.println("found == " + srp.isAllowedSectionTypeForType("XXClause", "XXPart"));
    }
}

