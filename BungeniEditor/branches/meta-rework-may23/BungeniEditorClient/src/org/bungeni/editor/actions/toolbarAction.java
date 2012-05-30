package org.bungeni.editor.actions;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.editor.config.DocumentActionsReader;
import java.io.IOException;
import org.bungeni.editor.selectors.SelectorDialogModes;

//~--- JDK imports ------------------------------------------------------------

import org.bungeni.editor.document.DocumentSection;
import org.bungeni.editor.document.DocumentSectionsContainer;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Administrator
 */

// !+ACTION_RECONF (rm, jan 2012) - note that toolbarAction class is being
// consolidated into this class. A number of variables and methods
// will be added

public class toolbarAction {
    private static org.apache.log4j.Logger log      =
        org.apache.log4j.Logger.getLogger(toolbarAction.class.getName());
  //  private String[]                       profiles = null;
    private SelectorDialogModes            theMode  = null;
    private String                         action_class;

    private static final String DEFAULT_VALIDATOR = "org.bungeni.editor.actions.validators.defaultValidator";

    // !+ACTION_RECONF (rm, jan 2012) - deprecating var since corresponding
    // field in db is deprecated
    //private String                         action_type;
    
    private String                         action_value;

    private String                         doc_type;
    //!+DEPRECATED(ah, feb-2012)
    //private String                         parent_action_name;
    private String                         actionName;

    // !+ACTION_RECONF (rm, jan 2012) - deprecating variable since the
    // field it maps to in the SUB_ACTIONS_SETTINGS table is dropped
    // private String                         sub_action_order;

    // !+ACTION_RECONF (rm, jan 2012) - deprecating variable since the
    // field it maps to in the SUB_ACTIONS_SETTINGS table is dropped
    // private String                         sub_action_state;
    private DocumentSection                textSection ;
    private String                         validator_class;
    private String                         section_naming_convention;
    private String                         section_numbering_convention;

    //~-----------------------------------------------------------------------
    // !+ACTION_RECONF (rm, jan 2012) - these variables are added from toolBarAction
  
    //~------------------------------------------------------------------------


    private actionRouter                   router;

    /**
    <router name="debaterecord.markup_motion_title.makeProcMotionBlockSection"
    class="org.bungeni.editor.actions.routers.routerApplyStyle"
    dialog= "org.bungeni.editor.selectors.debaterecord.motions.MotionSelect"
    />
     **/
    public class actionRouter {
    
        private String name ;
        private String router_class;
        private String dialog_class;

        public actionRouter(String actionName ) throws JDOMException, IOException, Exception {
            Element routerElement = DocumentActionsReader.getInstance().getRouter(actionName);
            if (routerElement != null) {
                    name = routerElement.getAttributeValue("name");
                    router_class = routerElement.getAttributeValue("class");
                    dialog_class = routerElement.getAttributeValue("dialog");
            } else
                throw new Exception("Router Element not created, getRouter returned null !");
        }

        public String getName() {
            return this.name;
        }
        public String getRouterClass() {
            return this.router_class;
        }
        public String getDialogClass() {
            return this.dialog_class;
        }
    }

    public enum actionSourceOrigin {
        sectionType,
        inlineType,
        notSetType,
        invalidType
    };

    actionSourceOrigin actionSource;

    String actionSourceType;
    
    String actionSourceValue ;


    public toolbarAction (Element actionElement) throws JDOMException, IOException, Exception {

        this.actionName     = actionElement.getAttributeValue("name");


        this.doc_type            =  actionElement.getParentElement().getAttributeValue("for");

        this.action_class = "org.bungeni.editor.actions.EditorSelectionActionHandler" ;

        //if a validator is specified in the action, use it otherwise use the default
        this.validator_class     = actionElement.getAttributeValue("validator");
        if (this.validator_class == null) {
            this.validator_class = DEFAULT_VALIDATOR;
        }
        this.router = new actionRouter(this.actionName);
    
        this.actionSourceType = actionElement.getAttributeValue("source");
        if (this.actionSourceType == null ) {
            this.actionSourceType = "";
            this.actionSource = actionSourceOrigin.notSetType;
        } else {

            this.actionSourceType = this.actionSourceType.trim();
            this.actionSource = actionSourceOrigin.valueOf(this.actionSourceType);
            if (this.actionSource != null) {
                  String metadataValue = actionElement.getAttributeValue("metadata");
                  if (metadataValue != null)
                      this.actionSourceValue = metadataValue.trim();
                  else
                      this.actionSourceValue = "";
                  if (actionSourceValue.length() > 0 ) {
                    if (this.actionSource.equals(actionSourceOrigin.sectionType)) {
                        this.setupSectionType(this.actionSourceValue);
                    } else if (this.actionSource.equals(actionSourceOrigin.inlineType)) {
                        log.debug("This is an inline type - no special setup for now");
                    }
                      
                  }
            } else {
                this.actionSource = actionSourceOrigin.invalidType;
            }
        }
    }


    private void setupSectionType(String sub_section_type) {
          if (sub_section_type.length() > 0 ) {
            DocumentSection associatedSection = DocumentSectionsContainer.getDocumentSectionByType(sub_section_type);
            if (associatedSection != null ) {
                this.textSection = associatedSection;
                this.section_naming_convention = associatedSection.getSectionNamePrefix();    
                this.section_numbering_convention = associatedSection.getSectionNumberingStyle();    
             } else {
                this.section_naming_convention = "" ;
                this.section_numbering_convention = "" ;
             }
        } else {
              this.section_naming_convention = "" ;
              this.section_numbering_convention = "" ;
        }
    }

 
    @Override
    public String toString() {
        return this.actionName;
    }


    public String doc_type() {
        return doc_type;
    }


    public String sub_action_name() {
        return actionName;
    }

    public String action_class() {
        return action_class;
    }


    public String validator_class() {
        return this.validator_class;
    }


    public String action_value() {
        return this.action_value;
    }


    public String section_naming_convention(){
        return this.section_naming_convention;
    }

   public String section_numbering_convention(){
        return this.section_numbering_convention;
    }

    public String dialog_class() {
        return this.router.getDialogClass();
    }

    public String router_class() {
        return this.router.getRouterClass();
    }

    public void setActionValue(String value) {
        this.action_value = value;
    }

    public void setSelectorDialogMode(SelectorDialogModes mode) {
        theMode = mode;
    }

    public SelectorDialogModes getSelectorDialogMode() {
        return theMode;
    }

    public String getSectionType(){
        if (this.actionSource.equals(actionSourceOrigin.sectionType)) {
            return this.actionSourceValue;
        } else {
            log.warn("getSectionType : There is no section Type set !");
            return "";
        }
    }

    public String getInlineType(){
        if (this.actionSource.equals(actionSourceOrigin.inlineType)) {
            return this.actionSourceValue;
        } else {
            log.warn("getInlineType: There is no inline type set !");
            return "";
        }
    }

    // !+ACTION_RECONF (rm, jan 2012) - action_name variable has
    // been changed to sub_action_name
    public boolean isTopLevelAction(){
        if (sub_action_name().equals("editor_root") || sub_action_name().equals("parent")
                || sub_action_name().equals("selection_root")) {
            return true;
        } else {
            return false;
        }
    }
    //~-------------------------------------------------------------------------


}
