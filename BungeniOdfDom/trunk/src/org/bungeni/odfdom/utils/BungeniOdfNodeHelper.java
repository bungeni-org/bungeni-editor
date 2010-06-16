package org.bungeni.odfdom.utils;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Element;
import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.StringWriter;

import java.util.Stack;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.odftoolkit.odfdom.OdfNamespace;
import org.odftoolkit.odfdom.dom.OdfNamespaceNames;
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
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniOdfNodeHelper.class.getName());
    private static Transformer staticTransformer = null;

    public static String getXPath(Node aNode) {

        /** return if node is null */
        if (null == aNode) {
            return null;
        }

        /** parent node */
        Node parent = null;

        /** stack nodes in the parent hierarchy */
        Stack<Node> hierarchy = new Stack<Node>();

        /** buffer to store node path */
        StringBuffer buffer = new StringBuffer();

        /** push the current node into the node hierarchy */
        hierarchy.push(aNode);
        parent = aNode.getParentNode();

        /** go up the hierarchy upto the document node */
        while ((null != parent) && (parent.getNodeType() != Node.DOCUMENT_NODE)) {

            /** push the parent on to the hierafchy stack */
            hierarchy.push(parent);

            /** travel up the parent hierarchy */
            parent = parent.getParentNode();
        }

        /** build the XPath here */
        Object obj = null;

        while (!hierarchy.isEmpty() && (null != (obj = hierarchy.pop()))) {
            Node    node    = (Node) obj;
            boolean handled = false;

            /** we only process elements since we are primarily going to use it for text:change nodes */
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
                        int  prev_siblings = 1;
                        Node prev_sibling  = node.getPreviousSibling();

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

    private static synchronized Transformer getTransformer() throws TransformerConfigurationException {
        if (staticTransformer == null) {
            staticTransformer = TransformerFactory.newInstance().newTransformer();
        }

        return staticTransformer;
    }

    public static StringWriter outputNodeAsXML(Node outputNode, StringWriter outputString) throws TransformerException {
        try {

            // Prepare the DOM document for writing
            Source source = new DOMSource(outputNode);
            Result result = new StreamResult(outputString);

            // Write the DOM document to the file
            Transformer xformer = getTransformer();

            xformer.transform(source, result);
        } catch (TransformerException ex) {
            log.error(ex);

            throw ex;
        }

        return outputString;
    }

    public static File outputNodeAsXML(Node outputNode, File foutfile)
            throws TransformerConfigurationException, TransformerException {

        // Prepare the DOM document for writing
        Source source = new DOMSource(outputNode);
        Result result = new StreamResult(foutfile);

        // Write the DOM document to the file
        Transformer xformer = getTransformer();
        OdfNamespace ns = OdfNamespace.newNamespace(OdfNamespaceNames.TEXT);
        xformer.setParameter("namespace", ns.toString());
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xformer.transform(source, result);

        return foutfile;
    }

    public static StringWriter outputNodesAsXML(NodeList outputNodes, StringWriter outputString) throws TransformerException {
        for (int i = 0; i < outputNodes.getLength(); i++) {
            Node aNode = outputNodes.item(i);
            outputNodeAsXML(aNode, outputString);
        }
        return outputString;
    }

    
}
