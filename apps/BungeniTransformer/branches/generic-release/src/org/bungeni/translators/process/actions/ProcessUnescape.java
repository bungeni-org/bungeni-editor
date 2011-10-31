package org.bungeni.translators.process.actions;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.translator.OADocumentBuilder;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Ashok
 */
public class ProcessUnescape implements IProcessAction {

       private static org.apache.log4j.Logger log
           = Logger.getLogger(ProcessUnescape.class.getName());



    public Document process(Document inputDocument, OAProcessStep processInfo) throws TransformerException, SAXException, IOException {
        Document outputDocument = inputDocument;

        try {
            String xPath = processInfo.getParam();
            NodeList nl = (NodeList) XPathResolver.getInstance().evaluate(outputDocument,
                                xPath,
                                XPathConstants.NODESET);
            //use the cached generic transformer
            GenericTransformer genTrans = GenericTransformer.getInstance();
            Transformer transformer = genTrans.getTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (int i=0 ; i < nl.getLength() ; i++ ) {
                Node nNodetoUnescape = nl.item(i);
                if (nNodetoUnescape.hasChildNodes()) {
                    NodeList childNodes = nNodetoUnescape.getChildNodes();
                    StringWriter swOutputChildren = new StringWriter();
                    //convert the children to strings
                    for (int c=0; c <  childNodes.getLength() ; c++ ) {
                        Node nchild = childNodes.item(c);
                        transformer.transform(new DOMSource(nchild),new StreamResult(swOutputChildren));
                    }
                    String unescapedHTML = StringEscapeUtils.unescapeHtml(swOutputChildren.toString());
                    org.jsoup.nodes.Document jsoup = Jsoup.parse(
                           "<div xmlns=\"http://www.w3.org/1999/xhtml/\">" +
                           unescapedHTML +
                           "</div>"
                           );
                    String jsoup_html = jsoup.body().html();
                    Document docJsoupNode = OADocumentBuilder.getInstance().getDocumentBuilder().parse(
                           new InputSource(
                            new StringReader(jsoup_html)
                            )
                           );
                    Node importThisNode = docJsoupNode.getDocumentElement();

                    //remove all child nodes
                    while (nNodetoUnescape.hasChildNodes()) {
                        nNodetoUnescape.removeChild(nNodetoUnescape.getLastChild());
                    }
                    //now import the node into the document
                    outputDocument.adoptNode(importThisNode);
                    //and append thenew
                    nNodetoUnescape.appendChild(importThisNode);
                }
            }
            //outputDocument = inputDocument;
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        } catch (TransformerConfigurationException ex) {
           log.error(ex);
        } catch (XPathExpressionException ex) {
           log.error(ex);
        } finally {
            try {
                GenericTransformer.getInstance().getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            } catch (TransformerConfigurationException ex) {
               log.error("Error while resetting transformer configuration", ex);
            }
        }
        return outputDocument;
        
    }


}
