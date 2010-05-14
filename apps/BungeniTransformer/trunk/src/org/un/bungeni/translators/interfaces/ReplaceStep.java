package org.un.bungeni.translators.interfaces;

/**
 * This is the interface for the ReplaceStep Object.
 * A replace step is a simple String replacement, having a pattern to search and the replacement string.
 */
public interface ReplaceStep {

    /**
     * Return the name of this Replace Step
     * @return a String containing the name of this replace object
     */
    public String getName();

    /**
     * Return the replacement of this Replace Step
     * @return a String containing the replacement of this replace object
     */
    public String getReplacement();

    /**
     * Return the pattern of this Replace Step
     * @return a String containing the pattern of this Replace Step
     */
    public String getPattern();
}
