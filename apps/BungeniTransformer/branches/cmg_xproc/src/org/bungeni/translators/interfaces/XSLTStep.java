package org.bungeni.translators.interfaces;

/**
 * This is the interface for the step objects of the ODTtoAKN translator.
 * A step contains all the informations related to a particular step of the translation.
 */
public interface XSLTStep {

    /**
     * Used to get the name of the Step
     * @return the name of the Step
     */
    public String getName();

    /**
     * Used to get the href of the Step
     * @return the href of the Step
     */
    public String getHref();

    /**
     * Used to get the position of the Step. The step of the configurations
     * are resolved performing all the Steps of the configurations. The step
     * with the lowest position number is performed firstly.
     * @return the position of the Step
     */
    public Integer getPosition();
}
