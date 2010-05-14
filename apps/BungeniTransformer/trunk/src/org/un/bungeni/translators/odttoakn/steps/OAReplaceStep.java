package org.un.bungeni.translators.odttoakn.steps;

//~--- non-JDK imports --------------------------------------------------------

import org.un.bungeni.translators.interfaces.ReplaceStep;

/**
 * This is the ReplaceStep Object.
 * A ReplaceStep is a simple String replacement, having a pattern to search and the replacement string.
 */
public class OAReplaceStep implements ReplaceStep {

    // the name of this replace step
    private String name;

    // the pattern of this ReplaceStep
    private String pattern;

    // the replacement of this ReplaceStep
    private String replacement;

    /**
     * Create a new ReplaceStep having the given replacement and the given pattern
     * @param aReplacement the replacement String for the new ReplaceStep
     * @param aPattern the pattern String for the new ReplaceStep
     */
    public OAReplaceStep(String aName, String aReplacement, String aPattern) {

        // set the name
        this.name = aName;

        // set the replacement
        this.replacement = aReplacement;

        // set the pattern
        this.pattern = aPattern;
    }

    /**
     * Return the name of this Replace Step
     * @return a String containing the name of this replace object
     */
    public String getName() {

        // copy the replacement
        String aName = this.name;

        // return the copy of the replacement
        return aName;
    }

    /**
     * Return the replacement of this Replace Step
     * @return a String containing the replacement of this replace object
     */
    public String getReplacement() {

        // copy the replacement
        String aReplacement = this.replacement;

        // return the copy of the replacement
        return aReplacement;
    }

    /**
     * Return the pattern of this Replace Step
     * @return a String containing the pattern of this Replace Step
     */
    public String getPattern() {

        // copy the pattern
        String aPattern = this.pattern;

        // return the copy of the pattern
        return aPattern;
    }
}
