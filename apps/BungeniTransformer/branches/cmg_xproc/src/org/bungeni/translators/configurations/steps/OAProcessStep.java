package org.bungeni.translators.configurations.steps;

import org.w3c.dom.NamedNodeMap;

/**
 *
 * <process name="unescape"
 *           description="bill body"
 *           param="/bu:ontology/bu:bill/bu:body" />
 *
 *
 * @author Ashok
 */
public class OAProcessStep {

    String processName = "";
    String description = "";
    String param = "";

    public OAProcessStep(NamedNodeMap attrs){
        this.processName = attrs.getNamedItem("name").getNodeValue();
        this.description = attrs.getNamedItem("description").getNodeValue();
        this.param = attrs.getNamedItem("param").getNodeValue();
    }

    public String getName(){
        return this.processName;
    }

    public String getDescription(){
        return this.description;
    }

    public String getParam(){
        return this.param;
    }


}
