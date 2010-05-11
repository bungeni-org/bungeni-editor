/**
 * 
 */
package org.bungeni.plugins.translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bungeni.plugins.IEditorPlugin;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.odttoakn.translator.OATranslator;
import org.un.bungeni.translators.utility.files.FileUtility;

/**
 * Bridging class that implements the IEditorPlugin interface for interacting with the bungeni editor.
 * The IEditorPlugin interface is described 
 * http://code.google.com/p/bungeni-editor/source/browse/plugins/BungeniEditorPluginInterface/src/org/bungeni/plugins/IEditorPlugin.java
 * 
 * @author Ashok Hariharan
 *
 */
public class OdtTranslate implements IEditorPlugin {

    private static org.apache.log4j.Logger log            =
        Logger.getLogger(OdtTranslate.class.getName());

    private static OdtTranslate thisTranslator = null;
    
    private HashMap                      editorParams    = null;
    private String                       odfFileUrl      = null;
    private String 					 	 outputFilePath = null;
    private String 						 outputMetalexFilePath = null;
    private String                       translatorRootFolder = null;
    private String						 translatorConfigFile = null;
    private String						 translatorPipeline = null;
    private String						 currentDocType = null;
	private Object					     callerPanel = null;
	private javax.swing.JFrame			 callerFrame = null;
	private String 						 pluginMode = null;
	
	public OdtTranslate(){
		//for call by reflection
	}
	
	public static OdtTranslate getInstance(){
		if (OdtTranslate.thisTranslator == null) {
			thisTranslator = new OdtTranslate();
		}
		return thisTranslator;
	}
	
	public String exec() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String retvalue = "";
		boolean bExceptionOccured = false;
		HashMap<String, File> filesMap = new HashMap<String,File>();
		OATranslator myTranslator = null;
	//	final ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
		try 
		{
			System.out.println("getting translator instance");
			//Thread.currentThread().setContextClassLoader(OdtTranslate.class.getClassLoader());
			myTranslator = OATranslator.getInstance();

			System.out.println("calling translate");
			
			String fullPathToPipeline = GlobalConfigurations.getApplicationPathPrefix() + this.translatorPipeline;
			
			filesMap = myTranslator.translate(this.odfFileUrl, fullPathToPipeline);
			
			System.out.println("no exceptions occured : writing outputs");
			/*
			FileUtility futils = FileUtility.getInstance();
			File foutputAnxml = new File(this.outputFilePath);
			File foutputMetalex = new File(this.outputMetalexFilePath);
			futils.copyFile(filesMap.get("anxml"), foutputAnxml);
			futils.copyFile(filesMap.get("metalex"), foutputMetalex);
			retvalue = myTranslator.getValidationErrors(); */
		} 
		catch (Exception e) 
		{
			log.error("exec()", e);
			bExceptionOccured = true;
		}  finally{
			if (!bExceptionOccured) {
				FileUtility futils = FileUtility.getInstance();
				File foutputAnxml = new File(this.outputFilePath);
				File foutputMetalex = new File(this.outputMetalexFilePath);
				try {
					futils.copyFile(filesMap.get("metalex"), foutputMetalex);
					futils.copyFile(filesMap.get("anxml"), foutputAnxml);
				} catch (IOException e) {
					log.error("exec():finally, writing outputs", e);
				}
			}
			retvalue = myTranslator.getValidationErrors();
		}
	   return retvalue;
	}

	public Object exec2(Object[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The following parameters are supported and mandatory:
	 * OdfFileURL - full path to the Odf file being translated
	 * OutputFilePath - full path to the output Xml file to be generated
	 * TranslatorRootFolder - root folder for the translator usually the folder containing the main directories used by the translator. Path must end in a "/"
	 * TranslatorConfigFile - the configuration file for the document type being transformed (this is a relative path - relative to the translator root folder)
	 * TranslatorPipeline - the translator pipeline to use for the transformation
	 * CurrentDocType - the document type being translated
	 * CallerPanel  - the UI JPanel invoking the translator
	 * PluginMode - 2 modes are supported odt2akn and akn2html.
	 * ParentFrame - the JFrame object to use as the parent frame for any UI interactions
	 */
	public void setParams(HashMap inputParams) {
        try {
            log.debug("setting inputparams");
            
            //first recieve the input parameters for the plugin from the parameter map
            this.editorParams    = inputParams;
            this.odfFileUrl      = (String) this.editorParams.get("OdfFileURL");
            this.outputFilePath = (String) this.editorParams.get("OutputFilePath");
            this.outputMetalexFilePath = (String) this.editorParams.get("OutputMetalexFilePath");
            this.translatorRootFolder = (String) this.editorParams.get("TranslatorRootFolder");
            this.translatorConfigFile = (String)  this.editorParams.get("TranslatorConfigFile");
            this.translatorPipeline = (String) this.editorParams.get("TranslatorPipeline");
            this.currentDocType  = (String) this.editorParams.get("CurrentDocType");
            this.pluginMode = (String) this.editorParams.get("PluginMode");
            if (this.editorParams.containsKey("ParentFrame")) {
                this.callerFrame = (javax.swing.JFrame) this.editorParams.get("ParentFrame");
            }
            if (this.editorParams.containsKey("CallerPanel")) {
            	this.callerPanel = this.editorParams.get("CallerPanel");
            }
            
            //set the parameters for the translator now
            appInit();
            
            } catch (Exception ex) {
                log.error("setParams : " + ex.getMessage());
                log.error("setParams : stacktrace : ", ex);
                ex.printStackTrace(System.out);
            }
	}

	public void updateParams(HashMap updateMap) {
		Set updateKeys = updateMap.keySet();
		Iterator updKeyIterator = updateKeys.iterator();
		while (updKeyIterator.hasNext()) {
			String key = (String) updKeyIterator.next();
			this.editorParams.put(key, updateMap.get(key));
		}
		setParams(editorParams);
	}
	
	public HashMap getParams() {
		return this.editorParams;
	}
	
	/*** Application code **/
	private void appInit(){
		//Do application initialization here
		//setting application prefixes etc..
		GlobalConfigurations.setApplicationPathPrefix(this.translatorRootFolder);
		//GlobalConfigurations.setConfigurationFilePath("configfiles/odttoakn/TranslatorConfig_debaterecord.xml");
		GlobalConfigurations.setConfigurationFilePath(this.translatorConfigFile);

	}
	
	private String getOutputFileName() {
		return this.outputFilePath;
	}
}
