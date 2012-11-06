package org.bungeni.editor.metadata;

import java.util.ArrayList;
import java.util.List;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Ashok Hariharan
 */
public class EditorDocMetadataDialogFactory {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditorDocMetadataDialogFactory.class.getName());
    public static String WORK_URI = "";
    public static String EXP_URI = "";
    public static String MANIFESTATION_FORMAT = "";

    public static ArrayList<IEditorDocMetadataDialog> getInstances(String docType) {
        ArrayList<IEditorDocMetadataDialog> dlgLists = new ArrayList<IEditorDocMetadataDialog>(0);
        log.info("getInstance : getting db handle");
        Element doctypeElem = null;
        try {
            doctypeElem = DocTypesReader.getInstance().getDocTypeByName(docType);
        } catch (JDOMException ex) {
            log.error("Errro while getting doctype by name", ex);
        }
        if (null != doctypeElem) {
        List<Element> metadataModelEditors = DocTypesReader.getInstance().getMetadataModelEditorsForDocType(doctypeElem);
            if (null != metadataModelEditors) {
                for (Element editorElem : metadataModelEditors) {
                    String metadataModelClass = editorElem.getAttributeValue("class");
                    String metadataModelTitle = CommonEditorXmlUtils.getLocalizedChildElementValue(
                            BungeniEditorPropertiesHelper.getLangAlpha3Part2(),
                            editorElem,
                            "title");
                    if (metadataModelClass.length() > 0) {
                        log.info("iterateRow : creating instance for class = " + metadataModelClass);
                        IEditorDocMetadataDialog iInstance = newInstance(metadataModelClass, metadataModelTitle);
                        dlgLists.add(iInstance);
                    }
                }
            }
        }
        return dlgLists;
    }

    public static IEditorDocMetadataDialog newInstance(String metaClassName, String metaTabTitle) {
        IEditorDocMetadataDialog iInstance = null;
        try {
            log.info("newInstance for class : " + metaClassName);
            Class modelClass;
            modelClass = Class.forName(metaClassName);
            log.info("newInstance created class");
            iInstance = (IEditorDocMetadataDialog) modelClass.newInstance();
            log.info("newInstance created instance from class");
            iInstance.setTabTitle(metaTabTitle);
            log.info("newInstance set title = " + metaTabTitle);
        } catch (Exception ex) {
            log.error("newInstance() : " + ex.getMessage());
        }
        return iInstance;
        
    }
}
