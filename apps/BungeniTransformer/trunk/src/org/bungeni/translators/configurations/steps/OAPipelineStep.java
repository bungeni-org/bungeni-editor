package org.bungeni.translators.configurations.steps;

import org.bungeni.translators.globalconfigurations.GlobalConfigurations;

/**
 * This class reads the pipetoxml entry in the config.xml file
 * @author Ashok
 */
public class OAPipelineStep {

    private String pipelineName;
    private String pipelineHref;

    public OAPipelineStep(String name, String href) {
        this.pipelineName = name;
        this.pipelineHref = href;
    }

    /**
     * @return the pipelineName
     */
    public String getPipelineName() {
        return pipelineName;
    }

    /**
     * @return the pipelineHref
     */
    public String getPipelineHref() {
        return GlobalConfigurations.getApplicationPathPrefix() + pipelineHref;
    }
}
