package org.bungeni.restlet;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
/**
 * Sets up default configurations for the REST transformer server.
 * Configuration is done via transformer.ini
 * @author Ashok Hariharan
 *
 */
public class TransformerRestletDefaultConfiguration {
	private Ini ini = null;
	private static TransformerRestletDefaultConfiguration configInstance = null;
	
	
	private static org.apache.log4j.Logger log =
	        Logger.getLogger(TransformerRestletDefaultConfiguration.class.getName());
	
	
	/**
	 * Returns a static instance to the configuration object
	 * @param iniFile - file Handle to ini file
	 * @return TransformerRestletDefaultConfiguration
	 */
	public static TransformerRestletDefaultConfiguration getInstance(File iniFile) {
		if (configInstance == null) {
			configInstance = new TransformerRestletDefaultConfiguration(iniFile);
		}
		return configInstance;
	}
	
	public TransformerRestletDefaultConfiguration(File iniFile) {
		try {
			ini = new Ini(iniFile);
		} catch (InvalidFileFormatException e) {
			log.error("RestletDefaultConfiguration:", e);
			e.printStackTrace(System.out);
		} catch (IOException e) {
			log.error("RestletDefaultConfiguration:", e);
			e.printStackTrace(System.out);
		}
	}
	
	private Section getModeConfig(String sectionname) {
		Section modeSection = ini.get(sectionname);
		return modeSection;
	}

	/**
	 * Returns a key value from the transformer.ini file
	 * @param sectionName - name of the section  containing the key
	 * @param keyName - the key name whose value is to be retrieved
	 * @return String
	 */
	private String getIniConfig(String sectionName, String keyName) {
			Section nSec = getModeConfig(sectionName);
			return nSec.get(keyName);
	}
	
	/**
	 * The transformer.ini uses a doctype:plugin-mode pattern to define section names, e,g.
	 * [debaterecord:odt2akn] is the section for debaterecord in odt2akn mode
	 * [bill:akn2html] is the section for the bill document type in akn2html mode
	 * @param docType - the document type
	 * @param pluginMode - the plugin mode either akn2html or odt2akn
	 * @param keyName - the key to be retrieved
	 * @return String
	 */
    private String getIniConfig (String docType, String pluginMode, String keyName) {
    	Section modeSection = getModeConfig(docType + ":" + pluginMode);
		String configFile = modeSection.get(keyName);
		return configFile;
		
    }
	
	public String getTranslatorConfigFile(String docType, String pluginMode) {
		return getIniConfig(docType, pluginMode, "translator_config");
	}


	public String getTranslatorPipeline(String docType, String pluginMode) {
		return getIniConfig(docType, pluginMode, "pipeline");
	}


	/**
	 * Retrieved the configured server port. This config is found in transformer.ini e.g.:
	 * [common]
	 * server_port=8082
	 * 
	 * @return Integer
	 */
	public int getServerPort() {
		String sPort =  getIniConfig("common", "server_port");
		return Integer.parseInt(sPort);
	}
	
	public String getServerTmpFolder() {
		return getIniConfig("common", "server_tmp_folder");
	}
	
}
