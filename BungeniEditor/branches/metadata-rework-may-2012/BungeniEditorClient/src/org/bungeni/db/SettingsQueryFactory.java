package org.bungeni.db;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;

/**
 *
 * @author Ashok Hariharan
 */
@Deprecated
public class SettingsQueryFactory {

    /** Creates a new instance of SettingsQueryFactory */
    public SettingsQueryFactory() {}

 
    /**
    public static String Q_FETCH_DOCUMENT_METADATA_VARIABLES(String visibleFilter) {
        String activeDocument = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String query          =
            "select metadata_name, metadata_datatype, metadata_type, display_name, visible, tabular_config  "
            + " from document_metadata " + "where metadata_type = 'document'" + "and doc_type = '" + activeDocument
            + "'  and visible=" + visibleFilter + " order by display_name asc";

        return query;
    }

    public static String Q_FETCH_DOCUMENT_METADATA_VARIABLE(String variableName) {
        String activeDocument = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String query          =
            "select metadata_name, metadata_datatype, metadata_type, display_name, visible, tabular_config  "
            + " from document_metadata " + "where metadata_type = 'document'" + "and doc_type = '" + activeDocument
            + "' " + "and metadata_name = '" + variableName + "' " + "order by display_name asc";

        return query;
    }***/

    /**
    public static String Q_FETCH_EDITOR_PROPERTY(String propertyName) {
        String query =  "Select property_name, property_value from general_editor_properties " + "where property_name='" + propertyName + "' ";

        return query;
    }
    **/
   

    /***
    public static String Q_FETCH_CONDITIONAL_OPERATORS() {
        String query = "SELECT condition_name, condition_syntax, condition_class FROM CONDITIONAL_OPERATORS";

        return query;
    }
    ***/

    /**
    public static String Q_FETCH_CONDITION_CLASS_BY_NAME(String conditionName, String doctype) {
        String query = "Select condition_class from toolbar_conditions \n" + "where condition_name = '" + conditionName
                       + "' and doctype = '" + doctype + "'";

        return query;
    }
    **/
    /**
    public static String Q_FETCH_ALL_ACTIVE_DOCUMENT_TYPES() {
        String query = "Select doc_type, description, template_path from DOCUMENT_TYPES where state = 1";
        return query;
    }**/


    /***
    public static String Q_FETCH_DOCUMENT_TYPE_BY_NAME(String docType) {
        String query =
            "Select doc_type, description, template_path, metadata_model_editor, metadata_editor_title, work_uri, exp_uri, file_name_scheme from DOCUMENT_TYPES ";

        query += " where doc_type='" + docType + "'";

        return query;
    }
    ***/
    
    /**
    public static String Q_SET_EDITOR_PROPERTY(String propertyName, String propertyValue) {
        String query = "update general_editor_properties set property_value ='" + propertyValue
                       + "' where property_name ='" + propertyName + "'";

        return query;
    }**/

    /**

    public static String Q_FETCH_TABS_BY_NAME(String docType, String name) {
        String query = "SELECT panel_class, panel_title, panel_load_order FROM EDITOR_PANELS "
                       + "Where state = 1 and doctype = '" + docType + "' and panel_type = 'tabbed' "
                       + "and panel_name = '" + name + "' " + "order by panel_load_order";

        return query;
    }

    public static String Q_FETCH_TABS_BY_DOC_TYPE(String docType) {
        String query = "SELECT panel_class, panel_title, panel_load_order FROM EDITOR_PANELS "
                       + "Where state = 1 and doctype = '" + docType + "' and panel_type = 'tabbed'  "
                       + "order by panel_load_order";

        return query;
    }
**/

    /***
    public static String Q_FETCH_NUMBER_DECORATORS() {
        String query = "select decorator_name, decorator_desc, decorator_class from " + "number_decorators";

        return query;
    }
    ***/

    /**
    public static String Q_FETCH_TRANSFORM_CONFIG(String docType) {
        String query = "select DOC_TYPE, CONFIG_NAME, CONFIG_FILE from TRANSFORM_CONFIGURATIONS where "
                       + "DOC_TYPE = '" + docType + "' ";

        return query;
    }**/

    /**
    public static String Q_FETCH_MESSAGE_BUNDLES() {
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        String query   = "select BUNDLE_NAME from RESOURCE_MESSAGE_BUNDLES where doc_type='" + docType + "'";

        return query;
    }
    **/
    
    /**
    public static String Q_FETCH_TRANSFORM_TARGETS(String targetName) {
        String query =
            "SELECT target_name, target_desc, target_ext, target_class FROM TRANSFORM_TARGETS where target_name='"
            + targetName + "'";

        return query;
    }
    **/

    /**
    public static String Q_FETCH_EXTERNAL_PLUGINS() {
        String query =
            "SELECT plugin_name, plugin_loader, plugin_desc, plugin_enabled, plugin_jar FROM EXTERNAL_PLUGINS ";

        return query;
    }
    **/
    /***
    public static String Q_FETCH_METADATA_MODEL_EDITORS(String docType) {
        String query = "SELECT DOC_TYPE, METADATA_MODEL_EDITOR, METADATA_EDITOR_TITLE  FROM METADATA_MODEL_EDITORS "
                       + "WHERE DOC_TYPE = '" + docType + "'  ORDER BY ORDER_OF_LOADING";

        return query;
    }
    ***/

    /**
    public static String Q_FETCH_ONTOLOGY_FOR_EVENT (String docType, String eventName) {
        String query = "SELECT ONTOLOGY, EVENT_NAME, EVENT_DESC FROM" +
                " EVENT_ONTOLOGIES WHERE DOC_TYPE = '"+ docType + "' " +
                "and event_name= '" +  eventName + "'";
        return query;
    }
    **/
    
    /***
    public static String Q_FETCH_LOCALES() {
        String query = "SELECT LANG_CODE_2, COUNTRY_CODE FROM LOCALES ";
        return query;
    }
    **/
    
    /**
    public static String Q_FETCH_COUNTRY_CODES(){
        String query = "SELECT COUNTRY_CODE, COUNTRY_NAME FROM COUNTRY_CODES";
        return query;
    }**/

    /**
    public static String Q_FETCH_LANGUAGE_CODES() {
      String query = "SELECT LANG_CODE, LANG_CODE_2, LANG_NAME FROM LANGUAGE_CODES";
        return query;

    }**/

    /***
    public static String Q_FETCH_DOCUMENT_PARTS(String docType) {
       String query = "SELECT DOC_TYPE, DOC_PART, PART_NAME from DOCUMENT_PART "
               + " where DOC_TYPE = '" + docType + "'" ;
       return query ;
    }
     ***/

}
