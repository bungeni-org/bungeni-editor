
package org.bungeni.odfdom.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper class for special node functions
 *  getNodeXPath - gets the XPath value to a node.
 * @author Ashok Hariharan
 */
public class BungeniOdfNodeHelper {



    /**
     * Calculates the XPath to a node
     * @param aNode - the node for which the XPath is to be calculated
     * @return - string with the XPath to the node
     */
    public static String getNodeXPath(Node aNode) {
        if (aNode == null) {
            return null;
        }
        if (aNode.getNodeType() == Node.ELEMENT_NODE) {
            Node aNodeParent = aNode.getParentNode();
            /** if the parent is null the aNode is at root **/
            if (aNodeParent == null) {
                return "/" + aNode.getNodeName();
            }
            /** build a stack of parent nodes **/
            Stack<Node> stack = new Stack<Node>();
            stack.add(aNode);
            do {
                stack.add(aNodeParent);
                aNodeParent = aNodeParent.getParentNode();
            } while (aNodeParent != null);
            /** build a string path to the nodes **/
            StringBuffer pathStr = new StringBuffer();
            while (!stack.isEmpty()) {
                pathStr.append(getNodeXPath(stack.pop()));
            }
            return pathStr.toString();
        }
        if (aNode.getNodeType() == Node.ATTRIBUTE_NODE) {
            /** an element node is the parent of an attribute node **/
            Node aNodeParentOfAttr = aNode.getParentNode();
            System.out.println("parent node type = " + ((aNodeParentOfAttr.getNodeType() == Node.ELEMENT_NODE)?"Element Node":"Unknonw"));
            /** get the path to the parent node **/
            String parentNodePath = getNodeXPath(aNodeParentOfAttr);
            StringBuffer attrXpathStr = new StringBuffer(parentNodePath)
                                        .append("[@")
                                        .append(aNode.getNodeName())
                                        .append("='")
                                        .append(aNode.getTextContent())
                                        .append("']");
            return attrXpathStr.toString();
        }
        if (aNode.getNodeType() == Node.TEXT_NODE) {
            Node aParentNodeOfText = aNode.getParentNode();
            StringBuffer textXpathStr = new StringBuffer(getNodeXPath(aParentNodeOfText))
                                            .append("[child::text()]");
            return textXpathStr.toString();
        }
        return "UNSUPPORTED_NODE_TYPE";
    }

    /**
     * Find siblings with the same node name and namespace
     * @param listofSiblings
     * @param currentNodeName
     * @param currentNsURI
     * @return
     */
    public static List<Node> getNodesByNameAndNS(NodeList listofSiblings, String currentNodeName, String currentNsURI) {
       List<Node> filteredSiblings = new ArrayList<Node>(0);
       /** iterate through node list of children **/
        for (int i = 0; i < listofSiblings.getLength(); i++) {
             Node siblingNode = listofSiblings.item(i);
             /** check if child node has the same name **/
             if (siblingNode.getNodeName().equals(currentNodeName)) {
                 /** check if child node has the same ns uri **/
                 if (siblingNode.getNamespaceURI().equals(currentNsURI)) {
                     filteredSiblings.add(siblingNode);
                 }
             }
        }
       return filteredSiblings;
    }

    /**
     * Gets the XPath to an element node relative to its siblings
     * @param currentNode
     * @return
     */
     public static String getElementXPath(Node currentNode) {
        StringBuffer pathStr = new StringBuffer("/");
        pathStr.append(currentNode.getNodeName());
        /** now we get the path relative to siblings **/
        Node parentNode = currentNode.getParentNode();
        /** check if we are at the root element **/
        if (parentNode == null ) {
            return pathStr.toString();
        }
        /** check for other siblings of the current namespace **/
        String currentNsURI =  currentNode.getNamespaceURI();
        String currentNodeName = currentNode.getNodeName();
        NodeList listofSiblings = parentNode.getChildNodes();
        List<Node> filteredSiblings = getNodesByNameAndNS(listofSiblings, currentNodeName, currentNsURI);
        int nodeIndex = 0;
          for (nodeIndex = 0; nodeIndex < filteredSiblings.size(); nodeIndex++) {
              Node aSibling = filteredSiblings.get(nodeIndex);
              if (aSibling == currentNode) break;
          }
            /** if the index is 0 and there are no next elements - then this currentNode is the sole sibling **/
            if ((nodeIndex == 0) && ((nodeIndex + 1) == filteredSiblings.size())) {
                return pathStr.toString();
            }
            return pathStr.append("[")
                          .append(String.valueOf(nodeIndex + 1))
                          .append("]").toString();
    }


}
