package org.un.bungeni.translators.odttoakn.translator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.odttoakn.configurations.OAConfiguration;
import org.un.bungeni.translators.odttoakn.steps.OAXSLTStep;
import org.un.bungeni.translators.utility.files.FileUtility;
import org.un.bungeni.translators.utility.xslttransformer.XSLTTransformer;

/**
 * Used to resolve the XSLT OUTPUT STEPS of a configuration file
*/
public final class OAOutputStepsResolver 
{
	/**
	 * Return the StreamSource obtained after all the OUTPUT XSLT steps of the given 
	 * configuration Document are applied to the given Stream source of the document
	 * @param aDocument a Stream Source of an ODF DOM document
	 * @param aConfiguration the configuration file that contains the XSLT STEPS
	 * @return a new StreamSource Obtained applying all the steps of the configuration to the 
	 * 			given StreamSource
	 * @throws XPathExpressionException 
	 * @throws TransformerException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	protected static StreamSource resolve(StreamSource anODFDocument, OAConfiguration aConfiguration) throws XPathExpressionException, TransformerException,UnsupportedEncodingException, FileNotFoundException
	{
		//get the steps from the configuration 
		HashMap<Integer,OAXSLTStep> stepsMap = aConfiguration.getOutputSteps();
		
		//create an iterator on the hash map
		Iterator<OAXSLTStep> mapIterator = stepsMap.values().iterator();
		
		//copy the document to translate
		StreamSource iteratedDocument = anODFDocument;
		
		//while the Iterator has steps apply the transformation
		while(mapIterator.hasNext())
		{
			//get the next step
			OAXSLTStep nextStep = (OAXSLTStep)mapIterator.next();
			
			//get the href from the step 
			String stepHref = GlobalConfigurations.getApplicationPathPrefix() + nextStep.getHref();
			
			//create a stream source by the href of the XSLT
			StreamSource xsltStream = FileUtility.getInstance().FileAsStreamSource(stepHref);
			
			//start the transformation
			iteratedDocument = XSLTTransformer.getInstance().transform(iteratedDocument, xsltStream);
		}
		
		//return the StreamSource of the transformed document
		return iteratedDocument;
		
	}

}
