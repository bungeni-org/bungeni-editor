package org.bungeni.db;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;

/**
 *
 * @author Ashok Hariharan
 */
public class SettingsQueryFactory {

    /** Creates a new instance of SettingsQueryFactory */
    public SettingsQueryFactory() {}

    public static String Q_FETCH_CHILD_TOOLBAR_ACTIONS(String parent_name) {
        String query = new String("" + "Select doc_type,action_name,action_order,action_state,action_class, "
                                  + "action_type,action_naming_convention,action_numbering_convention,"
                                  + "action_icon,action_display_text,action_dimension, action_dialog_class "
                                  + "from action_settings " + "where action_parent='" + parent_name
                                  + "' and action_state=1 " + "order by action_order");

        return query;
    }

    private static String Common_ToolbarAction_Selection() {
        String query =
            "" + "SELECT distinct act.doc_type, act.action_name, act.action_order, "
            + "act.action_state, act.action_class, act.action_type, act.action_naming_convention, "
            + "act.action_numbering_convention, "
            + "act.action_icon, act.action_display_text, act.action_dimension, act.action_section_type, act.action_dialog_class, act.action_edit_dlg_allowed ";

        return query;
    }

    public static String Q_FETCH_PARENT_ACTIONS() {
        String query =
            "" + "SELECT distinct act.doc_type, act.action_name, act.action_order, "
            + "act.action_state, act.action_class, act.action_type, act.action_naming_convention, "
            + "act.action_numbering_convention,  "
            + "act.action_icon, act.action_display_text, act.action_dimension, act.action_section_type, act.action_edit_dlg_allowed, act.action_dialog_class "
            + " FROM action_settings act inner join " + "action_parent p on (act.action_name = p.parent_action)"
            + " where p.parent_action not in (select action_name from action_parent) " + " order by action_order";

        return query;
    }

    public static String Q_FETCH_PARENT_ACTIONS(String byAction) {
        String query =
            "SELECT distinct act.doc_type, act.action_name, act.action_order,"
            + "act.action_state, act.action_class, act.action_type, act.action_naming_convention, "
            + "act.action_numbering_convention, "
            + "act.action_icon, act.action_display_text, act.action_dimension, act.action_section_type, act.action_edit_dlg_allowed, act.action_dialog_class "
            + " FROM " + "action_settings act where act.action_name in "
            + "(select action_name from action_parent where " + "parent_action='" + byAction + "')";

        return query;
    }

    public static String Q_FETCH_ACTION_BY_NAME(String docType, String byActionName) {
        String query = Common_ToolbarAction_Selection() + " from action_settings act " + "where act.doc_type='"
                       + docType + "' and act.action_name = '" + byActionName + "'";

        return query;
    }

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
    }

    public static String Q_CHECK_IF_ACTION_HAS_PARENT(String actionName) {
        String query = "select count(parent_action) as the_count  from action_parent " + " where action_name ='"
                       + actionName + "'";

        return query;
    }

    public static String Q_GET_PARENT_ACTIONS(String byAction) {
        String query =
            "SELECT distinct act.doc_type, act.action_name, act.action_order,"
            + "act.action_state, act.action_class, act.action_type, act.action_naming_convention, "
            + "act.action_numbering_convention,  "
            + "act.action_icon, act.action_display_text, act.action_dimension, act.action_dialog_class  FROM "
            + "action_settings act where act.action_name in " + "(select parent_action from action_parent where "
            + "action_name='" + byAction + "')";

        return query;
    }

    public static String Q_GET_SECTION_PARENT(String actionName) {
        String query = "select action_naming_convention from action_settings  "
                       + "where action_name in (select distinct parent_action from action_parent "
                       + "where action_name  = '" + actionName + "')";

        return query;
    }

    public static String Q_FETCH_EDITOR_PROPERTY(String propertyName) {
        String query = new String("" + "Select property_name, property_value from general_editor_properties "
                                  + "where property_name='" + propertyName + "' ");

        return query;
    }

    public static String Q_FETCH_NEIGBOURING_ACTIONS_deprecated(String preceeding, String following) {
        String query = "SELECT * FROM action_settings where action_type  = 'section'"
                       + " and action_name not in (select action_name from action_parent) " + " and action_order in  ("
                       + preceeding + "," + following + ") order by action_order asc";

        return query;
    }

    public static String Q_FETCH_ALL_SELECTION_ACTIONS(String docType) {
        String query =
            "select doc_type, parent_action_name, sub_action_name, sub_action_order, sub_action_state, action_type, "
            + "action_display_text, action_fields, action_class, system_container, validator_class, router_class, dialog_class, command_chain, profiles from sub_action_settings "
            + "where doc_type ='" + docType + "'";

        return query;
    }

    public static String Q_FETCH_ZERO_LEVEL_SELECTION_ACTIONS(String docType) {
        String query =
            "select doc_type, parent_action_name, sub_action_name, sub_action_order, sub_action_state, action_type, "
            + "action_display_text, action_fields, action_class, system_container, validator_class, router_class, dialog_class, command_chain, profiles from sub_action_settings "
            + "where doc_type = '" + docType + "' and sub_action_order <= 0" + " and sub_action_state = 1 "
            + "order by sub_action_order";

        return query;
    }

    public static String Q_FETCH_CHILDREN_SELECTION_ACTIONS(String docType, String parentAction) {
        String query =
            "select doc_type, parent_action_name, sub_action_name, sub_action_order, sub_action_state, action_type, "
            + "action_display_text, action_fields, action_class, system_container, validator_class, router_class, dialog_class, command_chain, profiles from sub_action_settings "
            + "where doc_type = '" + docType + "' and parent_action_name = '" + parentAction
            + "'  and sub_action_order > 0" + " and sub_action_state = 1" + " order by sub_action_order";

        return query;
    }

    public static String Q_FETCH_SUB_ACTIONS(String docType, String parentAction, String subActionName) {
        String query =
            "select doc_type, parent_action_name, sub_action_name, sub_action_order, sub_action_state, action_type, "
            + "action_display_text, action_fields, action_class, system_container, validator_class, router_class, dialog_class, command_chain, profiles from sub_action_settings "
            + "where doc_type = '" + docType + "' and parent_action_name = '" + parentAction
            + "'  and sub_action_name ='" + subActionName + "' " + " and sub_action_state = 1"
            + " order by sub_action_order";

        return query;
    }

    public static String Q_FETCH_COMMANDS_BY_FORM(String formName) {
        String query =
            "SELECT fcs.FORM_NAME, fcc.FORM_MODE, fcc.COMMAND_CATALOG, fcc.COMMAND_CHAIN,  fcs.CATALOG_SOURCE "
            + "FROM FORM_COMMAND_CHAIN fcc INNER JOIN FORM_CATALOG_SOURCE fcs ON ( fcc.FORM_NAME = fcs.FORM_NAME ) "
            + "WHERE FCC.FORM_NAME = '" + formName + "'";

        return query;
    }

    public static String Q_FETCH_CATALOG_SOURCE(String formName) {
        String query = "SELECT fcs.CATALOG_SOURCE FROM FORM_CATALOG_SOURCE fcs WHERE fcs.FORM_NAME='" + formName + "'";

        return query;
    }

    public static String Q_FETCH_CONDITIONAL_OPERATORS() {
        String query = "SELECT condition_name, condition_syntax, condition_class FROM CONDITIONAL_OPERATORS";

        return query;
    }

    public static String Q_FETCH_CONDITION_CLASS_BY_NAME(String conditionName, String doctype) {
        String query = "Select condition_class from toolbar_conditions \n" + "where condition_name = '" + conditionName
                       + "' and doctype = '" + doctype + "'";

        return query;
    }

    public static String Q_HIDDEN_FIELDS_FOR_ACTION_MODE(String actionName, String currentMode) {
        return "Select mode_hidden_field, control_mode from action_modes " + "Where action_name='" + actionName + "' "
               + "and action_mode='" + currentMode + "'";
    }

    public static String Q_HIDDEN_FIELDS_FOR_ACTION_MODE(String actionName, String subActionName, String currentMode) {
        return "Select mode_hidden_field, control_mode from action_modes " + "Where action_name='" + actionName + "' "
               + "and action_mode='" + currentMode + "' " + "and sub_action_name = '" + subActionName + "'";
    }

    public static String Q_FETCH_PANEL_BY_TYPE(String panelName, String panelType) {
        return "SELECT panel_type, panel_name, panel_desc, panel_class, panel_width, panel_height, panel_x, panel_y "
               + " from PLUGIN_DIALOGS where PANEL_TYPE = '" + panelType + "' and panel_name = '" + panelName + "'";
    }

    public static String Q_FETCH_TOOLBAR_CONFIG_FILE(String documentType) {
        String query = "SELECT doc_type, toolbar_xml  FROM TOOLBAR_XML_CONFIG " + "where DOC_TYPE = '" + documentType
                       + "'";

        return query;
    }

    public static String Q_FETCH_ALL_DOCUMENT_TYPES() {
        String query = "Select doc_type, description, template_path from DOCUMENT_TYPES ";

        return query;
    }

    public static String Q_FETCH_DOCUMENT_TYPE_BY_NAME(String docType) {
        String query =
            "Select doc_type, description, template_path, metadata_model_editor, metadata_editor_title, work_uri, exp_uri, file_name_scheme from DOCUMENT_TYPES ";

        query += " where doc_type='" + docType + "'";

        return query;
    }

    public static String Q_SET_EDITOR_PROPERTY(String propertyName, String propertyValue) {
        String query = "update general_editor_properties set property_value ='" + propertyValue
                       + "' where property_name ='" + propertyName + "'";

        return query;
    }

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

    public static String Q_FETCH_DOCUMENT_SECTION_TYPES(String docType) {
        String query =
            "select doc_type, section_type_name, section_name_prefix, section_numbering_style, "
            + "section_background, section_indent_left, section_indent_right, section_visibility, numbering_scheme, number_decorator from "
            + "document_section_types where doc_type ='" + docType + "'";

        return query;
    }

    public static String Q_FETCH_NUMBER_DECORATORS() {
        String query = "select decorator_name, decorator_desc, decorator_class from " + "number_decorators";

        return query;
    }

    public static String Q_FETCH_TRANSFORM_CONFIG(String docType) {
        String query = "select DOC_TYPE, CONFIG_NAME, CONFIG_FILE from TRANSFORM_CONFIGURATIONS where "
                       + "DOC_TYPE = '" + docType + "' ";

        return query;
    }

    public static String Q_FETCH_MESSAGE_BUNDLES() {
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        String query   = "select BUNDLE_NAME from RESOURCE_MESSAGE_BUNDLES where doc_type='" + docType + "'";

        return query;
    }

    public static String Q_FETCH_TRANSFORM_TARGETS(String targetName) {
        String query =
            "SELECT target_name, target_desc, target_ext, target_class FROM TRANSFORM_TARGETS where target_name='"
            + targetName + "'";

        return query;
    }

    public static String Q_FETCH_EXTERNAL_PLUGINS() {
        String query =
            "SELECT plugin_name, plugin_loader, plugin_desc, plugin_enabled, plugin_jar FROM EXTERNAL_PLUGINS ";

        return query;
    }

    public static String Q_FETCH_METADATA_MODEL_EDITORS(String docType) {
        String query = "SELECT DOC_TYPE, METADATA_MODEL_EDITOR, METADATA_EDITOR_TITLE  FROM METADATA_MODEL_EDITORS "
                       + "WHERE DOC_TYPE = '" + docType + "'  ORDER BY ORDER_OF_LOADING";

        return query;
    }
}
