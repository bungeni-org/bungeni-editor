package org.un.bungeni.translators.utility.odf;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openoffice.odf.doc.OdfDocument;
import org.openoffice.odf.doc.OdfFileDom;
import org.un.bungeni.translators.utility.files.FileUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * This class supplies several methods useful for the management of the ODF documents. 
 *
 */
public class ODFUtility 
{
	/* The instance of this ODFUtility object*/
	private static ODFUtility instance = null;
	
	/*The Logger*/
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ODFUtility.class.getName());
    
	/* The path to the ODF File used by the ExstractSection*/
	private String fullPathToOdfFile = "";
    
	/*The ODF package used by the ExtractSection*/
	private OdfDocument odfPackage ;


	/**
	 * Private constructor used to create the ODFUtility instance
	 */
	private ODFUtility()
	{
		
	}
	
	/**
	 * Get the current instance of the ODFUtility class 
	 * @return the Utility instance
	 */
	public static ODFUtility getInstance()
	{
		//if the instance is null create a new instance
		if (instance == null)
		{
			//create the instance
			instance = new ODFUtility();
		}
		//otherwise return the instance
		return instance;
	}

	
	/**
	 * This method returns a File that is formed merging in a single file the "content.xlm"
	 * "meta.xml" and "style.xml" files of the given ODF package
	 * @param anODFPath the path of the ODF package to merge 
	 * @return
	 * @throws TransformerFactoryConfigurationError 
	 * @throws Exception 
	 */
	public File mergeODF(String anODFPath) throws TransformerFactoryConfigurationError, Exception
	{
		//get the ODF package
		OdfDocument odf = OdfDocument.loadDocument(anODFPath);
		File returnFile = commonMergeODF (odf);
		return returnFile;
	}
	
	public File mergeODF(File aDocumentHandle ) throws TransformerFactoryConfigurationError, Exception {
		OdfDocument odf = OdfDocument.loadDocument(aDocumentHandle);
		File returnFile = commonMergeODF (odf);
		return returnFile;
	}
	
	private File commonMergeODF (OdfDocument odf)  throws TransformerFactoryConfigurationError, Exception 
	{
		
		//get the DOM of the content.xml file
		Document odfDom = odf.getContentDom();
		
		//get the content of the style.xml file
		Document odfStyle = odf.getStylesDom();
		
		//get the content of the meta.xml file
		InputStream odfMetaStream = odf.getMetaStream();
		
		//create the dom of the metadata from the stream 
		Document odfMeta = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FileUtility.getInstance().StreamAsInputSource(odfMetaStream));
		
		//get all the style nodes contained in the in the style.xml file
		Node stylesNodes = odfStyle.getElementsByTagName("office:styles").item(0);

		//get all the meta nodes contained in the in the meta.xml file
		Node metaNodes = odfMeta.getElementsByTagName("office:document-meta").item(0);
		
		//appends the style nodes to the content.xml document 
		odfDom.getElementsByTagName("office:document-content").item(0).appendChild(odfDom.adoptNode(stylesNodes));	

		//appends the meta nodes to the content.xml document 
		odfDom.getElementsByTagName("office:document-content").item(0).appendChild(odfDom.adoptNode(metaNodes));	

		 // Prepare the DOM document for writing
        Source source = new DOMSource(odfDom);

		//create a temp file for   
		File returnFile = File.createTempFile("temp",".xml");
		
		//create the result on the temp file
		Result result = new StreamResult(returnFile);

        // get the instance of the transformer
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        
        //set the UTF-8 encoding
        xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
        
        //Write the DOM document to the file
        xformer.transform(source, result);
        
        //return the file
        return returnFile;
		
	}
	
	 /**
     * loads the input file as a odf document
     * @param odfFile - path to odf file e.g. /home/undesa/files/file_with_sections.odt
     */
    public void configureXtractSections(String odfFile) 
    {
        try 
        {
            //get full path to file
            this.fullPathToOdfFile = odfFile;
            
            //load the odf package
            this.odfPackage = OdfDocument.loadDocument(this.fullPathToOdfFile);
        } 
        catch (Exception ex) 
        {
            log.error("XtractSections() = " + ex.getMessage());
        }
    }
    
    /**
     * Outputs the section info as a ":" delimited file.
     * @return
     */
    public String outputSectionInfo() 
    {
        StringWriter out = new StringWriter();
        try 
        {
            OdfFileDom fileDom = odfPackage.getContentDom();
            NodeList sectionList = fileDom.getElementsByTagName("text:section");
            
            // FileWriter fw = new FileWriter(outputFile);
            for (int i = 0; i < sectionList.getLength(); i++) 
            {
                Element elem = (Element)sectionList.item(i);
                String outputLine = elem.getAttribute("text:name");
                String secContent = elem.getTextContent();
                secContent = (secContent.length() > 80 ? secContent.substring(0, 80) : secContent);
                secContent = secContent.replace("\n", "");
                outputLine = outputLine + ":" + secContent + "\n";
                out.append(outputLine);
            }
            return out.toString();
        } 
        catch (Exception ex) 
        {
            log.error("outputSectionInfo : "  + ex.getMessage());
            return out.toString();
        } 
    }
    
    public String ExtractSection(String pathToOdf) 
    {
        try 
        {
            this.configureXtractSections(pathToOdf);
            return this.outputSectionInfo();
            //System.out.print(this.outputSectionInfo());
        } 
        catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
