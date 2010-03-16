
package org.bungeni.odfdom.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper class for special node functions
 *  getNodeXPath - gets the XPath value to a node.
 * @author Ashok Hariharan
 */
public class BungeniOdfNodeHelper {

 /**
  * Gets XPath of a Node object
  * Adapted fromhttp://www.wadoku.de/wiki/display/DEV/XPath+of+Node
  * By Mikkel Flindt Heisterberg
  * 
  * @param n
  * @return
  */
    public static String getXPath(Node aNode) {
      /** return if node is null **/
      if (null == aNode) return null;

      /** parent node **/
      Node parent = null;
      /** stack nodes in the parent hierarchy **/
      Stack<Node> hierarchy = new Stack<Node>();
      /** buffer to store node path **/
      StringBuffer buffer = new StringBuffer();

      /** push the current node into the node hierarchy **/
      hierarchy.push(aNode);

      parent = aNode.getParentNode();
      /** go up the hierarchy upto the document node **/
      while (null != parent && parent.getNodeType() != Node.DOCUMENT_NODE) {
         /** push the parent on to the hierafchy stack **/
         hierarchy.push(parent);
         /** travel up the parent hierarchy **/
         parent = parent.getParentNode();
      }
      /** build the XPath here **/
      Object obj = null;
      while (!hierarchy.isEmpty() && null != (obj = hierarchy.pop())) {
         Node node = (Node) obj;
         boolean handled = false;
         /** we only process elements since we are primarily going to use it for text:change nodes **/
         if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            // is this the root element?
            if (buffer.length() == 0) {
               // root element - simply append element name
               buffer.append(node.getNodeName());
            } else {
               // child element - append slash and element name
               buffer.append("/");
               buffer.append(node.getNodeName());

               if (node.hasAttributes()) {
                  // see if the element has a name or id attribute
                  if (e.hasAttribute("id")) {
                     // id attribute found - use that
                     buffer.append("[@id='" + e.getAttribute("id") + "']");
                     handled = true;
                  } else if (e.hasAttribute("name")) {
                     // name attribute found - use that
                     buffer.append("[@name='" + e.getAttribute("name") + "']");
                     handled = true;
                  }
               }

               if (!handled) {
                  // no known attribute we could use - get sibling index
                  int prev_siblings = 1;
                  Node prev_sibling = node.getPreviousSibling();
                  while (null != prev_sibling) {
                     if (prev_sibling.getNodeType() == node.getNodeType()) {
                        if (prev_sibling.getNodeName().equalsIgnoreCase(node.getNodeName())) {
                           prev_siblings++;
                        }
                     }
                     prev_sibling = prev_sibling.getPreviousSibling();
                  }
                  buffer.append("[" + prev_siblings + "]");
               }
            }
         }
      }

      // return buffer
      return buffer.toString();
   }



    /**
     * Calculates the XPath to a node
     * @param aNode - the node for which the XPath is to be calculated
     * @return - string with the XPath to the node
     */
    public static String getNodeXPath(Node aNode) {
        if (aNode == null) {
            return null;
        }
        if (aNode.getNodeType() == Node.DOCUMENT_NODE) {
            /** this is the parent node **/
            return "/" + ((Document)aNode).getDocumentElement().getLocalName();
        }

        if (aNode.getNodeType() == Node.ELEMENT_NODE) {
            Node aNodeParent = aNode.getParentNode();
            /** if the parent is null the aNode is at root **/
            if (aNodeParent == null) {
                return "/" + aNode.getLocalName();
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
            System.out.println(pathStr.toString());
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
                                        .append(aNode.getLocalName())
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
