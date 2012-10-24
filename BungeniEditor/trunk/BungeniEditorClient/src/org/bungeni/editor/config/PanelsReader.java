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
import java.util.HashMap;
import java.util.List;
import org.bungeni.utils.BungeniEditorPropertiesHelper;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * This class reads the Panel configuratoins from the XML config
 * @author Ashok
 *
 * !+REFACTOR_EXT_TOFIX
 */
public class PanelsReader extends BaseConfigReader {

    private static PanelsReader thisInstance = null;

    private SAXBuilder saxBuilder ;

    private final static String SETTINGS_FOLDER = CONFIGS_FOLDER  + File.separator + "panels";

    private HashMap<String,Document> cachedPanels = new HashMap<String,Document>();


    private PanelsReader() {
        saxBuilder = CommonEditorXmlUtils.getNonValidatingSaxBuilder();
    }

    public static PanelsReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new PanelsReader();
        }
        return thisInstance ;
    }

    private void __cachedPanels(String docType) throws JDOMException, IOException{
       if (!this.cachedPanels.containsKey(docType)){
            String docPanelsFile = SETTINGS_FOLDER + File.separator + docType + ".xml";
            this.cachedPanels.put(docType, CommonEditorXmlUtils.loadFile(docPanelsFile));
       } 
    }

    public Element getPanelByName(String docType, String panelName) throws JDOMException, IOException {
        __cachedPanels(docType);
        XPath xPath = XPath.newInstance("//panels[@for='" + docType + "']/panel[@name='"+panelName+"']");
        return (Element) xPath.selectSingleNode(this.cachedPanels.get(docType));

    }

    public String getLocalizedTitleForPanel(Element panel) {
        return CommonEditorXmlUtils.getLocalizedChildElementValue(
                BungeniEditorPropertiesHelper.getLangAlpha3Part2(),
                panel,
                "title"
                );
    }



    public List<Element> getPanelsByDocType(String docType) throws JDOMException, IOException {
        __cachedPanels(docType);
        XPath xPath = XPath.newInstance("//panels[@for='" + docType + "']/panel");
        return xPath.selectNodes(this.cachedPanels.get(docType));
    }


}
