/*
 * HTMLTransform.java
 *
 * Created on June 3, 2008, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.loadable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;
import org.bungeni.ooo.transforms.impl.TransformerConfigurationFactory;
import org.bungeni.ooo.transforms.impl.TransformerConfigurationFactory.Transformer;
import org.bungeni.utils.MessageBox;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.odttoakn.translator.OATranslator;

/**
 *
 * @author Administrator
 */
public class AnXmlTransform extends BungeniDocTransform {
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AnXmlTransform.class.getName());
    /** Creates a new instance of HTMLTransform */
    public AnXmlTransform() {
        super();
    }

    
    
    private File convertUrlToFile(String sUrl) {
        File f = null;
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            log.error("convertUrlToFile: "+ ex.getMessage());
            return null;
        }
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        } finally {
            return f;
        }
    }
    
    private boolean writeOutputFile(File outputTrans)  {
        boolean bState = false;
		try 
		{
                FileInputStream fTrans = new FileInputStream(outputTrans);
                //String outputFilename = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + ooDocument.
                FileOutputStream fOutTrans = new FileOutputStream(EXPORT_OUTPUT_FILE);
                    byte[] buf = new byte[1024];
		    int i = 0;
		    while ((i = fTrans.read(buf)) != -1) 
		    {
		            fOutTrans.write(buf, 0, i);
		    }
                     if (fTrans != null) fTrans.close();
		     if (fOutTrans != null) fOutTrans.close();
                    bState = true;
		}	
		catch (Exception ex) 
		{
                    bState = false;
                    log.error("writeOutputFile : " + ex.getMessage());
		} finally {
                    return bState;
                }
    }

    String EXPORT_OUTPUT_FILE  = "";
    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;
        try {
       //      XStorable docStore =ooDocument.getStorable();
       //     String urlString = (String) getParams().get("StoreToUrl");
       //     docStore.storeToURL(urlString, getTransformProps().toArray(new PropertyValue[getTransformProps().size()]));
           
            //1 - query interface the document for the storable url
            
            File fopenDocumentFile  = null ;
            if (ooDocument.isDocumentOnDisk())  {
                String sDocUrl = ooDocument.getDocumentURL();
                EXPORT_OUTPUT_FILE = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH()+ File.separator + "workspace" + File.separator + "export" + File.separator + "result.xml";
                
                fopenDocumentFile = convertUrlToFile(sDocUrl);
                String pathPrefix = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "transformer" + File.separator;
                GlobalConfigurations.setApplicationPathPrefix(pathPrefix);
                OATranslator ODTtrans = OATranslator.getInstance();
               
                Transformer tf = TransformerConfigurationFactory.getConfiguration(BungeniEditorPropertiesHelper.getCurrentDocType(), "debateRecordCommon");
                File outputTrans = ODTtrans.translate(fopenDocumentFile, tf.configFile);
                if (writeOutputFile(outputTrans)) 
                    //MessageBox.OK(this.callerFrame, "Document was transformed!");
                    bState = true;
                else 
                    bState = false;
                    //MessageBox.OK(this.callerFrame, "Document transformation failed");
            } else {
                MessageBox.OK("Please save the document before trying to transform it!");
                bState = false;
            }
           // bState= true;
        } catch (Exception ex) {
            log.error("transform : " + ex.getMessage());
        }
        return bState;
    }
    

}
