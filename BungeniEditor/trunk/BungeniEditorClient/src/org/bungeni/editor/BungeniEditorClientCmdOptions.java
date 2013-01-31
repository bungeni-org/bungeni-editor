package org.bungeni.editor;

//~--- non-JDK imports --------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Option;

/**
 * Class that describes and parses command line options for bungeni editor
 * Uses args4j
 * @author Ashok Hariharan
 */
public class BungeniEditorClientCmdOptions {
    public static final int LAUNCH_DOCTYPE       = 1;
    public static final int LAUNCH_FAIL          = 0;
    public static final int LAUNCH_WITH_LAUNCHER = 2;
    @Argument
    private List<String>    arguments            = new ArrayList<String>();
    /**
     * Args4j uses a decorator to map commandline parameter to a class variable.
     * declared variable after decorator stores the input parameter
     */

    @Option(
        name                                     = "-dt",
        required                                 = true,
        usage                                    = "specify one of the available document types debaterecord / bill / judgement / gui"
    )
    private String          documentType;

    @Option(
        name     = "-om",
        required = true,
        usage    = "specify one of the launch modes -- new / edit"
    )
    private String          openMode;

   /**
    * Set the launch language for the application
    */
    @Option(
        name     = "-lang",
        required = true,
        usage    = "specifies user language e.g. en,fr,es"
    )
    private String          userLanguage;

    /**
     * Set the launch region for the application
     * language + region = Locale
     */
    @Option(
        name     = "-region",
        required = true,
        usage    = "specifies user region e.g. US, UK"
    )
    private String          userRegion;


   

    public int doMain(String[] args) {
        for (String string : args) {
            System.out.println(string);
        }

        CmdLineParser cmdParser   = new CmdLineParser(this);
        int           returnState = LAUNCH_DOCTYPE;

        try {
            cmdParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar BungeniEditorClient.jar [options...] arguments...");

            // print the list of available options
            cmdParser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("  Example: java -jar BungeniEditorClient.jar " + cmdParser.printExample(ALL));
            returnState = LAUNCH_FAIL;

            // }
        }

        if (returnState != LAUNCH_FAIL) {
            System.out.println(" docType = " + this.documentType);
            System.out.println(" launchMode = " + this.openMode);

            return LAUNCH_DOCTYPE;
        }

        return LAUNCH_FAIL;
    }

    public String getDocType() {
        return this.documentType;
    }

    public String getLaunchMode() {
        return this.openMode;
    }

    public String getLang(){
        return this.userLanguage;
    }

    public String getRegion(){
        return this.userRegion;
    }

}

