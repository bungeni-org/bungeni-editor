package org.bungeni.translators.globalconfigurations;

/**
 * This is the global configuration class. It contains static fields that can be accessed
 * and modified by all the classes of the project
 */
public class GlobalConfigurations {

    // the AKOMA NTOSO namespace
    private static String akomaNtosoNamespace = "http://www.akomantoso.org/1.0";

    // the prefix for all the application path
    private static String applicationPathPrefix = "";

    // the path of the configuration file to be used for the translation
    private static String configurationFilePath = "";

    /**
     * Set the application path prefix to the given one
     * @param anApplicationPathPrefix the new application path prefix
     */
    public static void setApplicationPathPrefix(String anApplicationPathPrefix) {

        // change the application path prefix to the given one
        GlobalConfigurations.applicationPathPrefix = anApplicationPathPrefix;
    }

    /**
     * Return the application path prefix
     * @return the application path prefix
     */
    public static String getApplicationPathPrefix() {

        // return the application path prefix
        return GlobalConfigurations.applicationPathPrefix;
    }

   //!+FIX_THIS_LATER (ah, oct-2011) the API here, get and set configuration file paths
    // have been removed !!!
    /**
     * Set the AKOMA NTOSO namespace to the given one
     * @param aNamespace the namespace of AKOMA NTOSO
     */
    public static void setAkomaNtosoNamespace(String aNamespace) {

        // set the AKOMA NTOSO namespace to the given one
        GlobalConfigurations.akomaNtosoNamespace = aNamespace;
    }

    /**
     * Get the AKOMA NTOSO namespace
     * @return the AKOMA NTOSO namespace
     */
    public static String getAkomaNtosoNamespace() {

        // return the AKOMA NTOSO namespace
        return GlobalConfigurations.akomaNtosoNamespace;
    }
}
