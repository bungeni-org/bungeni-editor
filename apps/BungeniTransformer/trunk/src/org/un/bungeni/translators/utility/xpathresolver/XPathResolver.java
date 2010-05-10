package org.un.bungeni.translators.utility.xpathresolver;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;

/**
 *  This is the xpath resolvers of the AKOMA NTOSO translators. 
 *  It is used to evaluate an xpath on a document. 
 */
public class XPathResolver implements XPathResolverInterface 
{

	/* The instance of this XPath resolver*/
	private static XPathResolver instance = null;
	

	/**
	 * Private constructor used to create the XPath resolver instance
	 */
	private XPathResolver()
	{
		
	}
	
	/**
	 * Get the current instance of the XPath resolver 
	 * @return the XPath instance
	 */
	public static XPathResolver getInstance()
	{
		//if the instance is null create a new instance
		if (instance == null)
		{
			//create the instance
			instance = new XPathResolver();
		}
		//otherwise return the instance
		return instance;
	}

	/**
	 * Evaluates the given xpath on the given document. 
	 * @param aDocument The document on which the xpath must be evaluated
	 * @param anXPath The xpath to evaluate
	 * @param aResultType The type of the results (Strings, NodeSet and so on)
	 * @return an Object whose type is specified by the "aResultType" parameter 
	 * @throws XPathExpressionException if the Xpath is not well formed 
	 */
	public Object evaluate(Document aDocument, String anXPath, QName aResultType) throws XPathExpressionException 
	{
		//instanciate all the stuffs needed to make xpath queries on the given document
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		//this is needed to make queries with the namespace 
		final PrefixResolver resolver = new PrefixResolverDefault(aDocument.getDocumentElement());
		NamespaceContext specificRulesNSC = new NamespaceContext() 
		{
			public String getNamespaceURI(String prefix) 
			{
				return resolver.getNamespaceForPrefix(prefix);
			}

			public Iterator<?> getPrefixes(String val) 
			{
				return null;
			}

			public String getPrefix(String uri) 
			{
				return null;
			}
		};
		xpath.setNamespaceContext(specificRulesNSC);

		//compile the XPath 
		XPathExpression expr = xpath.compile(anXPath);
		
		//evaluate the XPath
		Object exprResult = expr.evaluate(aDocument, aResultType);
		
		//return the result of the expression
		return exprResult;
	}

}
