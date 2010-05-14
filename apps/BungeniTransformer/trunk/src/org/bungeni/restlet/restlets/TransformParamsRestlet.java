package org.bungeni.restlet.restlets;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.plugins.translator.OdtTranslate;
import org.bungeni.restlet.TransformerRestletDefaultConfiguration;
import org.bungeni.restlet.docs.Documentation;
import org.bungeni.restlet.server.TransformerServer;

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.HashMap;

/**
 * Sets the dynamic parameters for the Odt Transformer.
 * Currently the only dynamic parameter is the document type and the plugin mode
 * - First an http post is sent to set the parameters o
 * @author Ashok Hariharan
 */
public class TransformParamsRestlet extends Restlet {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(TransformParamsRestlet.class.getName());
    private String         __DOC__         = "/org/bungeni/restlet/docs/set_convert_params.html";
    TransformParamsRestlet restletInstance = null;
    private String         documentType;
    private String         pluginMode;
    private String         workingDir;

    public TransformParamsRestlet(String workDir) {
        this.workingDir = workDir;
    }

    public TransformParamsRestlet getInstance(String workDir) {
        if (restletInstance == null) {
            restletInstance = new TransformParamsRestlet(workDir);
        }

        return restletInstance;
    }

    public void setDefaultParameters(HashMap paramMap) {
        File iniFile = new File(this.workingDir + File.separator + TransformerServer.SERVER_CONFIG_FILE);

        if (iniFile.exists()) {
            TransformerRestletDefaultConfiguration defConfig =
                TransformerRestletDefaultConfiguration.getInstance(iniFile);

            paramMap.put("TranslatorRootFolder", this.workingDir);
            paramMap.put("TranslatorConfigFile", defConfig.getTranslatorConfigFile(this.documentType, this.pluginMode));
            paramMap.put("TranslatorPipeline", defConfig.getTranslatorPipeline(this.documentType, this.pluginMode));
            paramMap.put("CurrentDocType", this.documentType);
            paramMap.put("CallerPanel", null);
            paramMap.put("PluginMode", this.pluginMode);
        }
    }

    @SuppressWarnings("unchecked")
    private void setTranslatorParams() {
        OdtTranslate translator = OdtTranslate.getInstance();

        // set the parameters for the server
        // get the default parameters
        HashMap translatorParameterMap = new HashMap();

        setDefaultParameters(translatorParameterMap);
        System.out.println(translatorParameterMap);
        translator.setParams(translatorParameterMap);

        // this parameter is set at runtime before calling exec
        // paramMap.put("OdfFileURL", currentDirectory + "/bin/debaterecord_ken_eng_2008_12_17_main.odt");
        // this parameter is set at runtime before calling exec
        // paramMap.put("OutputFilePath", currentDirectory + "/bin/debaterecord_ken_eng_2008_12_17_main.xml");
        // set the root directory / path prefix for the translator
        // translatorParameterMap.put("TranslatorRootFolder", currentDirectory);

        /*
         * paramMap.put("CallerPanel", null);
         * paramMap.put("PluginMode", "odt2akn");
         */

        // set the dynamic parameters
        // translator.setParams(inputParams)
    }

    @Override
    public void handle(Request request, Response response) {
        log.debug("handling method : " + request.getMethod().getName());

        try {
            if (request.getMethod().equals(Method.POST)) {
                Form postedForm = request.getEntityAsForm();

                this.documentType = (String) postedForm.getFirstValue("DocumentType");
                this.pluginMode   = (String) postedForm.getFirstValue("PluginMode");
                this.setTranslatorParams();
                System.out.println("doc type = " + this.documentType);
                response.setStatus(Status.SUCCESS_NO_CONTENT);
            } else if (request.getMethod().equals(Method.GET)) {
                StringRepresentation srepdoc = new StringRepresentation(Documentation.getDocumentation(__DOC__),
                                                   MediaType.TEXT_HTML);

                response.setEntity(srepdoc);
            } else {
                response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("handle : ", ex);
        }
    }

    public static void main(String[] args) {
        TransformParamsRestlet r = new TransformParamsRestlet(System.getProperty("user.dir"));

        r.setTranslatorParams();
    }
}
