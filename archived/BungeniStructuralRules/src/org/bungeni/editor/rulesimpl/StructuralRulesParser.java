
package org.bungeni.editor.rulesimpl;

//~--- non-JDK imports --------------------------------------------------------

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

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

    private static org.apache.log4j.Logger log                    =
        org.apache.log4j.Logger.getLogger(StructuralRulesParser.class.getName());

    /**
     * Path to rules Xml file
     * @param xmlRules
     */
    public StructuralRulesParser(String xmlRules) {
        pathToXmlFile = xmlRules;
    }

    /**
     * Get the structure type for the document
     * @return
     */
    public String getDocStructureType() {
        String typeValue = "";

        try {
            XPath  selectPath = XPath.newInstance(GET_DOC_STRUCTURE_TYPE);
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

            XPath selectPath = XPath.newInstance(xpathSectionType);

            elemSecType = (Element) selectPath.selectSingleNode(this.xmlDocument);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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
            XPath   selectPath      = XPath.newInstance(GET_CHILD_REF_TYPES);

            matchingNodes = (ArrayList<Element>) selectPath.selectNodes(thisSectionType);
        } catch (JDOMException ex) {
            System.out.println("getAllowedSectionTypeForSectionType :" + ex.getMessage());
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
            XPath              selectPath        = XPath.newInstance(GET_CHILD_SECTION_TYPES);
            ArrayList<Element> foundSectionTypes = (ArrayList<Element>) selectPath.selectNodes(foundSectionType);

            for (Element elemrefSecType : foundSectionTypes) {
                String  nameOfAllowedSectionType = elemrefSecType.getAttributeValue("name");
                Element secType                  = this.getSectionType(nameOfAllowedSectionType);

                if (secType != null) {
                    foundTypes.add(secType);
                } else {
                    log.error("getAllowedSectionTypes : " + sectionTypeName
                              + " has a reference to allowed section-type : " + nameOfAllowedSectionType
                              + " which does not exist");
                }
            }
        } catch (Exception ex) {
            log.error("getAllowedSectionTypes : " + ex.getMessage());
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
            nOrder = Integer.parseInt(elem.getAttributeValue("order"));
            allowedElementsAtOrderPosition.add(elem);
        }

        public ArrayList<String> getSectionTypesForRule() {
            ArrayList<String> allowedSectionTypes = new ArrayList<String>(0);
            for (Element element : allowedElementsAtOrderPosition) {
                String elemType = element.getAttributeValue("type");
                if (elemType.equals(ORDER_TYPE_SECTION_TYPE)) {
                    String sectionTypeName = element.getAttributeValue("name");
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
                String orderType = element.getAttributeValue("type");
                if (orderType.equals(ORDER_TYPE_SECTION_TYPE)) {
                    String orderName = element.getAttributeValue("name");
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

         /**
          * Find the preceeding sectionType for sectionType within a parent sectionType ->order grouping
          * @param withinThisSectionType
          * @param forThisSectionType
          * @return returns an array of allowed preceeding sections
          */
         public ArrayList<String> getPreceedingAllowedSectionType(String withinThisSectionType, String forThisSectionType) {
             ArrayList<String> preceedingSectionTypes = new ArrayList<String>(0);
             try {
                 //check if the parent section type exists within the rule map
                 if (rulesBySectionType.containsKey(withinThisSectionType)) {
                     //get the allowe children
                     TreeMap<Integer, ruleOrderOfChildren> allowedOrderOfChildren = rulesBySectionType.get(withinThisSectionType);
                     Set<Integer> sectionOrder = allowedOrderOfChildren.keySet();
                     Integer trailingOrder = null;
                     for (Integer iorder : sectionOrder) {
                         ruleOrderOfChildren roc = allowedOrderOfChildren.get(iorder);
                         //look for the child section type within the allowed map
                         if (roc.isSectionTypeOrderValid(forThisSectionType)) {
                            //if yes -- get preceeding order element
                             //if there is no preceeding order level then we are at the first level
                             if (trailingOrder == null ) {
                                 return new ArrayList<String>() { { add("AT_FIRST"); }
                                 };
                             }
                             ruleOrderOfChildren previousROC = allowedOrderOfChildren.get(trailingOrder);
                             preceedingSectionTypes = previousROC.getSectionTypesForRule();
                         }
                         trailingOrder = iorder;
                     }
                 }
             } catch (Exception ex) {
                 log.error("getPreceedingAllowedSectionType : "+ ex.getMessage());
             } finally {
                 return preceedingSectionTypes;
             }
         }

         public String getFollowingAllowedSectionType(String withinThisSectionType, String forThisSectionType) {

         }



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
            XPath              selectPath        = XPath.newInstance(GET_ORDER_OF_CHILDREN);
            ArrayList<Element> foundSectionTypes = (ArrayList<Element>) selectPath.selectNodes(foundSectionType);

            for (Element elemrefSecType : foundSectionTypes) {
                Integer elementOrder = Integer.parseInt(elemrefSecType.getAttributeValue("order"));
                if (childOrder.containsKey(elementOrder)) {
                    childOrder.get(elementOrder).allowedElementsAtOrderPosition.add(elemrefSecType);
                } else {
                   ruleOrderOfChildren roc = new ruleOrderOfChildren(elemrefSecType);
                   childOrder.put(elementOrder, roc);
                }
            }
        } catch (Exception ex) {
            log.error("getAllowedSectionTypes : " + ex.getMessage());
        } finally {
            return childOrder;
        }
    }

    public TreeMap<Integer, ruleOrderOfChildren> getOrderOfChildrenForType(String sectionTypeName) {
        if (rulesBySectionType.containsKey(sectionTypeName)) {
            return rulesBySectionType.get(sectionTypeName);
        } else {
            rulesBySectionType.put(sectionTypeName, retrieveOrderRulesForType(sectionTypeName));
            return rulesBySectionType.get(sectionTypeName);
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
            XPath selectPath = XPath.newInstance(xpathGetparentSection + "/" + xpathGetChildSectionTypes);

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

