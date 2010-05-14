package org.un.bungeni.translators.utility.xpathresolver;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

/**
 *  This is the interface for the xpath resolvers of the AKOMA NTOSO translators.
 *  It is used to evaluate an xpath on a document.
 */
public interface XPathResolverInterface {

    /**
     * Evaluates the given xpath on the given document.
     * @param aDocument The document on which the xpath must be evaluated
     * @param anXPath The xpath to evaluate
     * @param aResultType The type of the results (Strings, NodeSet and so on)
     * @return an Object whose type is specified by the "aResultType" parameter
     * @throws XPathExpressionException if the Xpath is not well formed
     */
    public Object evaluate(Document aDocument, String anXPath, QName aResultType) throws XPathExpressionException;
}
