package org.bungeni.xmlmerge.utils;

import java.util.Stack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author ashok
 */
public class XPathUtils {

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


}
