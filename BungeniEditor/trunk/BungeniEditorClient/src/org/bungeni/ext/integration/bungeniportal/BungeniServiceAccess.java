/*
 * Copyright (C) 2012 Africa i-Parliaments Action Plan
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.ext.integration.bungeniportal;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.ext.integration.bungeniportal.BungeniAppConnector.WebResponse;
import org.bungeni.ext.integration.bungeniportal.BungeniListDocuments.BungeniListDocument;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.properties.BungeniOdfPropertiesHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.jdom.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.w3c.dom.NodeList;

/**
 * This is the class that implements all interaction with Bungeni and abstracts
 * that interaction to the rest of the Bungeni Editor.
 *
 * @author Ashok Hariharan
 */
public class BungeniServiceAccess {

    private static BungeniServiceAccess instance = null;
    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(BungeniServiceAccess.class.getName());
    BungeniAppConnector appConnector = null;
    DefaultHttpClient client = null;

    public static BungeniServiceAccess getInstance() {
        if (null == instance) {
            instance = new BungeniServiceAccess();
        }
        return instance;
    }

    public BungeniAppConnector getAppConnector() {
        return this.appConnector;
    }

    public DefaultHttpClient login(String appServer, String appPort, String appBase, String user, String password, OAuthCredentials oauth) throws UnsupportedEncodingException, IOException, JDOMException {
        if (null == appConnector) {
            this.appConnector = new BungeniAppConnector(appServer, appPort, appBase, user, password, oauth);
            this.client = appConnector.oauthLogin();
            return this.client;
        }
        return null;
    }

    private static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private String getTargetTransitionId(String transitionURL) throws MalformedURLException {
        URL url = new URL(transitionURL);
        String sUrlQuery = url.getQuery();
        Map<String, String> urlMap = getQueryMap(sUrlQuery);
        String sTransition = urlMap.get("transition_id");
        String[] arrTrans = sTransition.split("-");
        // the second element in the array has the transition id 
        String targetTransitionId = arrTrans[1];
        return targetTransitionId;
    }

    public String getTransitionTime(Date dTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(dTime);
        StringBuilder sTime = new StringBuilder();
        sTime.append(c.get(Calendar.HOUR));
        sTime.append(":");
        sTime.append(c.get(Calendar.MINUTE));
        return sTime.toString();
    }

    public String getTransitionDate(Date dtDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dtDate);
    }

    
    /**
    public List<BasicNameValuePair> attachmentUploadFormFields(String sAttURL, List<BasicNameValuePair> inputFormSubmit) {
       List<BasicNameValuePair> nvPair = new ArrayList<BasicNameValuePair>(0);
       //nvPair.add(new BasicNameValuePair("container_contents_versions.commit_message", ))
       
       return null;   
    }
    ***/
    
    public List<BasicNameValuePair> attachmentWorkflowTransitPostQuery(Transition transObj, String sDate, String sTime, List<BasicNameValuePair> inputFormFields) throws MalformedURLException {

        List<BasicNameValuePair> nvPair = new ArrayList<BasicNameValuePair>(0);

        nvPair.add(new BasicNameValuePair("next_url", "./workflow-redirect"));

        nvPair.add(
                new BasicNameValuePair(
                "form.date_active",
                sDate));

        nvPair.add(new BasicNameValuePair(
                "form__date_active__time",
                sTime));
      
        for (BasicNameValuePair inputFormField : inputFormFields) {
            // !+WARNING+WARNING - assumption is that 2 transitions dont have the same
            // name !
            if (inputFormField.getValue().equalsIgnoreCase(transObj.title)){
                nvPair.add(inputFormField);
                break;
            }
        }
        return nvPair;
    }

   public  String toHex(String arg) throws UnsupportedEncodingException {
        return String.format("%x", new BigInteger(arg.getBytes("UTF-8")));
    }

    
    public String transitionNameAsciiAble(String name) throws UnsupportedEncodingException {
        // from zope.formlib.form.Action :
        /**
         * class Action(object):

            interface.implements(interfaces.IAction)
            _identifier = re.compile('[A-Za-z][a-zA-Z0-9_]*$')
         */
        String pattern =  "^[A-Za-z][a-zA-Z0-9_]*$";
        if (name.matches(pattern)) {
            return name.toLowerCase();
        } else {
            return toHex(name);
        }
    }
    
    
    /**
     * This api returns the form information for submitting a transition.
     * We make 2 calls 2 this form
     * this is the first one it determines the fields on the form and the submit actions
     * @param docURL
     * @return 
     */
    public List<BasicNameValuePair>  getWfTransitionInputTypeSubmitInfo(String docURL) {
        List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        WebResponse wr = appConnector.getUrl( makeWFurl(docURL), false);
        if (wr.getStatusCode() == 200 ) {
            Document wfDoc = Jsoup.parse(wr.getResponseBody());
            nvp = getActionsViewButtonInfo(wfDoc);
        }
        return nvp;
    }
    
   
    
   

   public List<BasicNameValuePair> attachmentVersionSubmitPostQuery(List<BasicNameValuePair> pairs, String sComment ){
       
        List<BasicNameValuePair> nvPair = new ArrayList<BasicNameValuePair>(0);
        nvPair.add(
               new BasicNameValuePair("container_contents_versions.commit_message", sComment)
            );
        
        for (BasicNameValuePair inputFormField : pairs) {
            if (inputFormField.getName().equalsIgnoreCase(
                    "container_contents_versions.actions.new_version"
                )){
                nvPair.add(inputFormField);
                break;
            }
        }

       return nvPair;
   }
    
   
   private List<BasicNameValuePair> getActionsViewButtonInfo(Document doc) {
       List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>(0); 
       Elements inputList = doc.select("div#actionsView input");
       for (int i = 0; i < inputList.size(); i++) {
            Element inputItem = inputList.get(i);
            nvp.add(
                    new BasicNameValuePair(
                        inputItem.attr("name"),
                        inputItem.attr("value")
                    )
            );
        }
       return nvp;
   }
   
   private List<BasicNameValuePair> getFormFieldSelectDefaultValues(Document doc, List<String> fieldNames) {
       List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>(0); 
       for (String fieldName : fieldNames) {
           Elements inputItems = doc.select("[name=" + fieldName + "]");
           for (int i = 0; i < inputItems.size(); i++) {
               Element inputItem = inputItems.get(i);
               Elements selItems = inputItem.select("[selected=selected]");
               for (int j = 0; j < selItems.size() ; j++) {
                   Element selItem = selItems.get(j);
                    nvp.add(new  BasicNameValuePair(fieldName, selItem.attr("value")));
               }
           }
       }
       return nvp;
   }
   
   
   /**
    * THis API gets all the submit buttons for Attachment submission
    * Also gets the default values for the selection dropdowns
    * @param docURL
    * @return 
    */
   public HashMap<String, ContentBody> getAttachmentEditSubmitInfo(String docURL){
        HashMap<String, ContentBody> formFields = new HashMap<String, ContentBody>();
        // List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        WebResponse wr = appConnector.getUrl( 
            this.makeEditUrl(docURL), 
            false
        );

        if (wr.getStatusCode() == 200 ) {
            Document wfDoc = Jsoup.parse(wr.getResponseBody());
            // get the action buttons
            List<BasicNameValuePair> nvp = this.getActionsViewButtonInfo(wfDoc);
            // filter the action buttons, we want only the save acttion
            for (BasicNameValuePair pair : nvp) {
                try {
                    if (pair.getName().equals("form.actions.save")) {
                        formFields.put(pair.getName(), new StringBody(pair.getValue()));
                    }
                } catch (UnsupportedEncodingException ex) {
                    log.error("Encoding error while adding field " + pair.getName(), ex);
                }
            }
            // get the other defaulted fields
            List<String> defaultFields = new ArrayList<String>(){{
               add("form.language");
               add("form.type");
            }};
            // get the default values for fields
            for (BasicNameValuePair defField : this.getFormFieldSelectDefaultValues(wfDoc, defaultFields)){
                try {
                    formFields.put(defField.getName(), new StringBody(defField.getValue()));
                } catch (UnsupportedEncodingException ex) {
                    log.error("Error encoding field value " + defField.getName(), ex);
                }
            }
            
        }
        return formFields;
   }

      public HashMap<String, ContentBody> attachmentEditSubmitPostQuery(
         HashMap<String, ContentBody> formFields, 
         String title, 
         String description, 
         String fFileName 
       ) {
        try {
            formFields.put("form.title", new StringBody(title)) ;
            formFields.put("form.data.up_action", new StringBody("update")) ;
            formFields.put("form_data_file", new FileBody(
                    new File(new URI(fFileName)), 
                    getMimeType(fFileName)
                    ) 
               ) ;
            formFields.put("form.description", new StringBody(description));
            formFields.put("form.language-empty-marker", new StringBody("1") ) ;
            formFields.put("form.type-empty-marker", new StringBody("1"));
            return formFields;
        } catch (URISyntaxException ex) {
            log.error("Error getting URI to file ", ex);
            return formFields;
        } catch (UnsupportedEncodingException ex) {
            log.error("Encoding exception for field ", ex);
            return formFields;
        }
   }

   
    public List<BasicNameValuePair>  getAttachmentVersionSubmitInfo(String docURL) {
        
        List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        WebResponse wr = appConnector.getUrl( 
            this.makeVersionUrl(docURL), 
            false
        );

        if (wr.getStatusCode() == 200 ) {
                Document wfDoc = Jsoup.parse(wr.getResponseBody());
                nvp = getActionsViewButtonInfo(wfDoc);
        }
        return nvp;
    }

    
    public String makeWFurl(String docURL) {
        return docURL + "/workflow";
    }
    
    public String makeVersionUrl(String docURL){
        return docURL + "/version-log";
    }
    
    public String makeEditUrl(String docURL){
        return docURL + "/edit";
    }
    
    
    
    public WebResponse doVersion(String docURL, List<BasicNameValuePair> nvp) throws UnsupportedEncodingException {
        String versionURL = makeVersionUrl(docURL);
        WebResponse wr = appConnector.multipartPostUrl(versionURL, false, nvp);
        return wr;
    }
    
    public WebResponse doTransition(String docURL, List<BasicNameValuePair> nvp) throws UnsupportedEncodingException {
        String wfUrl = makeWFurl(docURL);
        WebResponse wr = appConnector.multipartPostUrl(wfUrl, false, nvp);
        return wr;
    }
    
    public WebResponse doEdit(String docURL, HashMap<String, ContentBody> nvp) throws UnsupportedEncodingException {
        String editURL = makeEditUrl(docURL);
        WebResponse wr = appConnector.multipartPostUrl(editURL, false, nvp);
        return wr;
    }

    public List<BungeniListDocument> availableDocumentsForEditing(String sSearchBungeniURL) {
        List<BungeniListDocument> bungeniDocs = new ArrayList<BungeniListDocument>(0);
        WebResponse wr = appConnector.getUrl(sSearchBungeniURL, true);
        if (wr != null) {
            if (wr.getStatusCode() == 200) {
                String sResponseBody = wr.getResponseBody();
                BungeniListDocuments bld = new BungeniListDocuments(sSearchBungeniURL, sResponseBody);
                bungeniDocs = bld.getListDocuments();
            }
        }
        return bungeniDocs;
    }

    /**
     * Service api called from BungeniAttLoadingPanel
     *
     * @param fodf
     * @param aDocument
     * @return
     * @throws Exception
     */
    public File checkOdfDocument(File fodf, BungeniDocument aDocument) throws Exception {
        OdfDocument odf = OdfDocument.loadDocument(fodf);
        BungeniOdfDocumentHelper odfhelper = new BungeniOdfDocumentHelper(odf);
        BungeniOdfPropertiesHelper propshelper = odfhelper.getPropertiesHelper();
        //check if the document has been edited in bungeni editor .. look for some properties
        HashMap<String, String> docPropsMap = propshelper.getUserDefinedPropertyValues();
        //check for root section
        boolean rootSectionExists = false;
        BungeniOdfSectionHelper sechelper = odfhelper.getSectionHelper();
        NodeList sections = sechelper.getDocumentSections();
        if (sections.getLength() > 0) {
            TextSectionElement sectionElement = (TextSectionElement) sections.item(0);
            //check for body section Type
            String sBody = sechelper.getSectionMetadataValue(sectionElement, "BungeniSectionType");
            if (sBody.equals("body")) {
                rootSectionExists = true;
            }
        }

       // if (this.hasDocumentBeeenEditedByTheEditor(docPropsMap, rootSectionExists)) {
            // this is a bungeni document ... load for editing
            // TO DO 
       // } else {
            //first prepare the document
            BungeniAttachment att = aDocument.getSelectedAttachment();
            propshelper.setUserDefinedPropertyValue("BungeniDocType", BungeniEditorPropertiesHelper.getCurrentDocType());
            propshelper.setUserDefinedPropertyValue("DocSource", "BungeniPortal");
            propshelper.setUserDefinedPropertyValue("DocEditor", "BungeniEditor");
            propshelper.setUserDefinedPropertyValue("DocInit", "False");
            propshelper.setUserDefinedPropertyValue("PortalSourceDoc", aDocument.getStatus());
            propshelper.setUserDefinedPropertyValue("PortalSourceTitle", aDocument.getTitle());
            propshelper.setUserDefinedPropertyValue("PortalSourceURL", aDocument.getURL());
            propshelper.setUserDefinedPropertyValue("PortalAttSource", att.url);
            propshelper.setUserDefinedPropertyValue("PortalAttFileName", att.fileName);
            propshelper.setUserDefinedPropertyValue("PortalAttTitle", att.title);
            propshelper.setUserDefinedPropertyValue("PortalAttType", att.attType);
            propshelper.setUserDefinedPropertyValue("PortalAttMimeType", att.mimeType);
            propshelper.setUserDefinedPropertyValue("PortalAttLang", att.language);
            propshelper.setUserDefinedPropertyValue("PortalAttStatus", att.status);
            propshelper.setUserDefinedPropertyValue("PortalAttStatusDate", att.statusDate);
            propshelper.setUserDefinedPropertyValue("PortalAttDownURL", att.downloadUrl);
            propshelper.setUserDefinedPropertyValue("PortalAttDesc", att.description);
            propshelper.setUserDefinedPropertyValue("PortalAttTransCount", Integer.toString(att.transitions.size()));

            int i = 0;
            for (Transition transition : att.transitions) {
                i++;
                //Not more than 99 transitions !!!
                String snum = String.format("%02d", i);
                propshelper.setUserDefinedPropertyValue("PortalAttTransName" + snum, transition.title);
                propshelper.setUserDefinedPropertyValue("PortalAttTransURL" + snum, transition.url);
            }

            odfhelper.saveDocument();
            // create the root section after opening and set initial metadata properties
        //}
        return fodf;
    }

    public boolean hasDocumentBeeenEditedByTheEditor(HashMap<String,String> docPropsMap, boolean rootSectionExists){
        if (docPropsMap.containsKey("BungeniDocType") && 
                docPropsMap.containsKey("DocSource") && 
                docPropsMap.containsKey("DocEditor") && 
                rootSectionExists) {
                if (docPropsMap.get("DocSource").equals("BungeniPortal") && 
                        docPropsMap.get("DocEditor").equals("BungeniEditor")) {
                        return true;
                }
        }
        return false;
    }

    
    
    public BungeniAttachment getAttachmentFromURL(String sURL){
        WebResponse wr = appConnector.getUrl(sURL, false);
        if (wr.getStatusCode() == 200 ){
            String responseBody = wr.getResponseBody();
            if (null != responseBody){
                Document attDoc = Jsoup.parse(responseBody);
                BungeniAttachment att = new BungeniAttachment();
                att.parseAttachment(attDoc);
                return att;
            }
        }
        return null;
    }
    
    public List<Transition> getUpdatedTransitionsForAttachment(String sURL ){
        List<Transition> trans = new ArrayList<Transition>();
        BungeniAttachment att = this.getAttachmentFromURL(sURL);
        if (att != null) {
            return att.transitions;
        }
        return trans;
    }
    
    private String getItemAttributeValue(Document doc, String itemPath, String attributeReturn) {
        Elements item =  doc.select(itemPath);
        return item.get(0).attr(attributeReturn);
    }
    
    public HashMap<String, ContentBody> getAuthorizeFormFieldValues(String sBody) throws UnsupportedEncodingException{
        HashMap<String,ContentBody> nvp = new HashMap<String,ContentBody>();
        Document doc = Jsoup.parse(sBody);
        if (!doc.select("input[name=client_id]").isEmpty()) {
            nvp.put("client_id",
                new StringBody(getItemAttributeValue(doc, "input[name=client_id]", "value"))
                );
            nvp.put("state",
                new StringBody(getItemAttributeValue(doc, "input[name=state]", "value"))
               );
            nvp.put("time",
                new StringBody(getItemAttributeValue(doc, "input[name=time]", "value"))
               );
            nvp.put("nonce",
                new StringBody(getItemAttributeValue(doc, "input[name=nonce]", "value"))
               );
            nvp.put("form.actions.authorize",
              new StringBody(getItemAttributeValue(doc, "input[name=form.actions.authorize]", "value"))
              );
        }
        return nvp;

    }
    
    
    public boolean createNewVersion(String docURL, String comment) {
        // version 
        
        //do soemthing
        return true;
    }
    
    
    public boolean uploadDocument(String toURL, File fFileToUpload) {
        // connect and upload
        
        // do something
        return true;
    }

    private String getMimeType(String fFileName) {
        if (fFileName.endsWith(".odt")) {
            return "application/vnd.oasis.opendocument.text";
        }
        if (fFileName.endsWith(".xml")) {
            return "text/xml";
        }
        return "application/octet-stream";
    }
}
