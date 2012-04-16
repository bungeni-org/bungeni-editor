package org.bungeni.translators.configurations.steps;

//~--- non-JDK imports --------------------------------------------------------
import org.bungeni.translators.interfaces.MapStep;

/**
 * This is the map step object
 * A map step object is an object used to store each element of the mapping files
 */
public class OAMapStep implements MapStep {

    // the bungeniSectionType attribute of the step
    private String stepBungeniSectionType;
    // the id of the step
    private Integer stepId;
    // the result attribute of the step
    private String stepResult;

    /**
     * Create a new step setting its type, name, bungeniSectionType and result to the given values
     * @param aStepBungeniSectionType the bungeniSectionType attribute of the step
     * @param aStepResult the result attribute of the step
     * @param anAttributesList the string that contains all the operation to be done on the attributes of a particular element
     */
    public OAMapStep(Integer aStepId, String aStepBungeniSectionType, String aStepResult) {

        // set the id attribute of the step
        this.stepId = aStepId;

        // set the bungeniSectionType attribute of the step
        this.stepBungeniSectionType = aStepBungeniSectionType;

        // set the result attribute of the step
        this.stepResult = aStepResult;
    }

    /**
     * This method is used to get the id attribute of a map Step
     * @return the id attribute of a map Step
     */
    public Integer getId() {

        // copy the value of the type attribute and return it
        Integer aId = this.stepId;

        return aId;
    }

    /**
     * This method is used to get the bungeniSectionType attribute of a map Step
     * @return the bungeniSectionType attribute of a map Step
     */
    public String getBungeniSectionType() {

        // copy the value of the bungeniSectionType attribute and return it
        String aBungeniSectionType = this.stepBungeniSectionType;

        return aBungeniSectionType;
    }

    /**
     * This method is used to get the result attribute of a map Step
     * @return the result attribute of a map Step
     */
    public String getResult() {

        // copy the value of the result attribute and return it
        String aResult = this.stepResult;

        return aResult;
    }
}
