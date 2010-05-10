package org.un.bungeni.translators.akntohtml.xslprocbuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.utility.files.FileUtility;
import org.un.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AHXSLProcBuilder 
{
	//the instance of this Configuration Builder
	private static AHXSLProcBuilder instance;
	
	//the path of the document that contains the default values
	private String defaultValuesPath;
	
	//the path of the document that contains the empty mini XSLT
	private String emptyMiniXSLTPath;

	//the path of the document that contains the empty pipeline
	private String emptyPipelinePath;
	
	/**
	 * Protected constructor
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
	protected AHXSLProcBuilder() throws InvalidPropertiesFormatException, IOException
	{
		//create the Properties object
		Properties properties = new Properties();
	
		//read the properties file
		InputStream propertiesInputStream = new FileInputStream(GlobalConfigurations.getApplicationPathPrefix() + GlobalConfigurations.getConfigurationFilePath());
			
		//load the properties
		properties.loadFromXML(propertiesInputStream);
		
		//get the defaut values path
		this.defaultValuesPath = properties.getProperty("defaultValuesPath");
		
		//get the empty mini XSLT path
		this.emptyMiniXSLTPath = properties.getProperty("emptyMiniXSLTPath");

		//get the empty pipeline path
		this.emptyPipelinePath = properties.getProperty("emptyPipelinePath");
	}
	
	/**
	 * Get a new instance of the Configuration Builder
	 * @return the instance of the configuration builder
	*/
	public static AHXSLProcBuilder newInstance()
	{
		//if there is already an active instance return the instance 
		if(instance != null)
		{
			//return the instance
			return instance;
		}
		//if the Configuration Builder is not instanciated create a new instance 
		else
		{
			try
			{
				//create the instance
				instance = new AHXSLProcBuilder();
			
				//return the instance
				return instance;
			}
			catch(Exception e)
			{
				return null;
			}
		}
	}
		
	/**
	 * Create a new set of mini XSLT and the XSLT (pipeline) that manages all the transformation operations
	 * @param outputDirectory The directory in which the mini XSLT and the pipeline will be written
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public void createXSLProc(String outputDirectory) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException
	{
		//get the default values container
		Document defaultValuesDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FileUtility.getInstance().FileAsInputSource(this.defaultValuesPath));
		
		//retreive the XPath resolver instance 
		XPathResolver xresolver = XPathResolver.getInstance();
		
		//get all the elements in the dafault_values XML
		NodeList elements = (NodeList)xresolver.evaluate(defaultValuesDocument, "//element", XPathConstants.NODESET);

		//get the empty XPROC string
		String emptyXPROCPipeline = FileUtility.getInstance().FileToString(this.emptyPipelinePath); 
		
		//the pipeline steps to add
		String pipelineSteps = "";
		
		//for each element create a MINI XSLT
		for (int i = 0; i < elements.getLength(); i++) 
		{
			//get the element 
			Node element = elements.item(i);
			
			//if the element must be translated create the mini XSLT
			if (element.getAttributes().getNamedItem("transformTo").getNodeValue().compareTo("") != 0)
			{
				//get the string of the mini XSLT
				String emptyXSLTString = FileUtility.getInstance().FileToString(this.emptyMiniXSLTPath);
								
				//replace the value of the current Element into the emptyMiniXSLTFile
				emptyXSLTString = emptyXSLTString.replaceAll("element-to-replace","akn:" + element.getAttributes().getNamedItem("name").getNodeValue());
				emptyXSLTString = emptyXSLTString.replaceAll("new-element", element.getAttributes().getNamedItem("transformTo").getNodeValue());
				emptyXSLTString = emptyXSLTString.replaceAll("element-class", element.getAttributes().getNamedItem("class").getNodeValue());
				
				//add the attributes to mantain
				if(element.getAttributes().getNamedItem("mantain") != null)
				{
					//get all the attribute to mantain
					String[] attributesToMantain = element.getAttributes().getNamedItem("mantain").getNodeValue().split(",");
					
					//the string that will contain the XSLT expression of the mantain
					String attributeStringXSLT = "";
					
					//for each attribute to mantain 
					if (attributesToMantain.length != 0)
					{
						for(int k = 0; k < attributesToMantain.length; k++)
						{
							attributeStringXSLT = 	attributeStringXSLT + 
							 						"\t\t\t<xsl:if test=\"@" + attributesToMantain[k] /*element.getAttributes().getNamedItem("mantain").getNodeValue()*/ +  "\">\n" +
							 						"\t\t\t\t<xsl:attribute name=\"" + 
													attributesToMantain[k] + 
													"\"" + 
													//" select=\"@" + 
													//attributesToMantain[k] +
													"><xsl:value-of select=\"@" + attributesToMantain[k] + "\" /></xsl:attribute>\n" +
													"\t\t\t</xsl:if>\n";
						}
					}
					else
						attributeStringXSLT = attributeStringXSLT +
											  "\t\t\t<xsl:if test=\"@" + element.getAttributes().getNamedItem("mantain").getNodeValue() +  "\">\n" +
											  "\t\t\t\t<xsl:attribute name=\"" + 
											  element.getAttributes().getNamedItem("mantain").getNodeValue() + 
											  "\"" + 
											  " select=\"@" + 
											  element.getAttributes().getNamedItem("mantain").getNodeValue() + 
											  "\" />\n" + 
											  "\t\t\t</xsl:if>";
						
					//add the XSLT code for the translation of the attributes to mantain
					emptyXSLTString = emptyXSLTString.replaceAll("other-attributes",attributeStringXSLT);
				
				}
				//remove other-attribute in case there are no attributes to mantain
				emptyXSLTString = emptyXSLTString.replaceAll("other-attributes"," ");

				//write the string to the file in the path composed by the given outputDirectory and the name of the element
				FileUtility.getInstance().StringToFile(outputDirectory + element.getAttributes().getNamedItem("name").getNodeValue() + ".xsl", emptyXSLTString);
			
				//create the pipelinestep 
				pipelineSteps = pipelineSteps + "\t<xsl:template match=\"akn:" + element.getAttributes().getNamedItem("name").getNodeValue() +  "\">\n\t\t<xslt step=\"" + i +  "\" name=\"" + element.getAttributes().getNamedItem("name").getNodeValue() + "\" href=\"" + outputDirectory + element.getAttributes().getNamedItem("name").getNodeValue() + ".xsl" + "\" />\n\t\t<xsl:apply-templates />\n\t</xsl:template>\n\n"; 
			}
			//otherwise the element will not be translated
			else
			{
				//get the string of the mini XSLT
				String emptyXSLTString = FileUtility.getInstance().FileToString(this.emptyMiniXSLTPath);
								
				//replace the value of the current Element into the emptyMiniXSLTFile
				emptyXSLTString = emptyXSLTString.replaceAll("element-to-replace","akn:" + element.getAttributes().getNamedItem("name").getNodeValue());
				emptyXSLTString = emptyXSLTString.replaceAll("<new-element>", "");
				emptyXSLTString = emptyXSLTString.replaceAll("<xsl:attribute name=\"class\">element-class</xsl:attribute>", "");
				emptyXSLTString = emptyXSLTString.replaceAll("other-attributes", "");
				emptyXSLTString = emptyXSLTString.replaceAll("</new-element>", "");
						
				//write the string to the file in the path composed by the given outputDirectory and the name of the element
				FileUtility.getInstance().StringToFile(outputDirectory + element.getAttributes().getNamedItem("name").getNodeValue() + ".xsl", emptyXSLTString);
			
				//create the pipelinestep 
				pipelineSteps = pipelineSteps + "\t<xsl:template match=\"akn:" + element.getAttributes().getNamedItem("name").getNodeValue() +  "\">\n\t\t<xslt step=\"" + i +  "\" name=\"" + element.getAttributes().getNamedItem("name").getNodeValue() + "\" href=\"" + outputDirectory + element.getAttributes().getNamedItem("name").getNodeValue() + ".xsl" + "\" />\n\t\t<xsl:apply-templates />\n\t</xsl:template>\n\n"; 
			}
		}	
		//fill the pipelines in the empty pipeline XSLT
		emptyXPROCPipeline = emptyXPROCPipeline.replaceAll("<xsl:template match=\"to_replace\"><XSLT_steps /></xsl:template>", pipelineSteps);
		
		//write the pipeline to a file
		FileUtility.getInstance().StringToFile(outputDirectory + "pipeline.xsl", emptyXPROCPipeline);
	}
	
}
