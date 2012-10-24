/*
 *  Copyright (C) 2012 Africa i-Parliaments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class DocumentActionsReader extends BaseConfigReader {

    private static DocumentActionsReader thisInstance = null;



    //private final static String SETTINGS_FOLDER = CONFIGS_FOLDER + File.separator + "actions" + File.separator  + "doc_actions";

    private final static String DOC_ACTIONS_FOLDER =  CONFIGS_FOLDER + File.separator + "actions" + File.separator + "doc_actions";

    private final static String SELECTOR_DIALOGS_FILE = CONFIGS_FOLDER + File.separator + "actions" + File.separator + "selector_dialogs.xml";

    private HashMap<String,Document> cachedActions = new HashMap<String,Document>();

    /**
    private Document routerDocument = null;
    **/
    
    private Document selectorDialogsDocument = null;

    private DocumentActionsReader() {
   

    }

    public static DocumentActionsReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new DocumentActionsReader();
        }
        return thisInstance ;
    }

    public Element getDocumentActionByName(String docType, String actionName) throws JDOMException, IOException {
       if (!this.cachedActions.containsKey(docType)) {
            String docActionsFile = DOC_ACTIONS_FOLDER + File.separator + docType + ".xml";
            this.cachedActions.put(docType, CommonEditorXmlUtils.loadFile(docActionsFile));
        }
        XPath xPath = XPath.newInstance("//actions[@for='" + docType + "']/action[@name='"+actionName+"']");
        return (Element) xPath.selectSingleNode(this.cachedActions.get(docType));

    }


    public  Element getRouter(String docType, String actionName) throws JDOMException, IOException {
        Element actionElement = getDocumentActionByName(docType, actionName);
        Element actionRouter = actionElement.getChild("router");
        return actionRouter;
    }



    public Document getSelectorDialogs() throws JDOMException, IOException {
        if (this.selectorDialogsDocument == null) {
            String dialogFile = SELECTOR_DIALOGS_FILE;
            this.selectorDialogsDocument = CommonEditorXmlUtils.loadFile(dialogFile);
        }
        return this.selectorDialogsDocument;
    }

    public Element getSelectorDialog (String parentDialog) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//dialog[@id='"+ parentDialog + "']");
        Element dialogNode =  (Element) xPath.selectSingleNode(getSelectorDialogs());

        return dialogNode;
    }

    // (rm, jan 2012) - this method obtains the child 
    // dialogs of the main dialog
    public ArrayList getChildDialogs(String parentDialog) throws JDOMException, IOException {
        XPath xPath = XPath.newInstance("//dialog[@id='"+ parentDialog + "']/dialog");
        ArrayList selectorDialogs = (ArrayList) xPath.selectNodes(getSelectorDialogs());

        return selectorDialogs ;
    }
  
}
