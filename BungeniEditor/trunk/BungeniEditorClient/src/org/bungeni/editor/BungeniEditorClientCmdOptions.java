package org.bungeni.editor;

import java.util.ArrayList;
import java.util.List;
import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Class that describes command line options for bungnei
 * @author Ashok Hariharan
 */
public class BungeniEditorClientCmdOptions {

    @Option(name="-dt",required=true, usage="specify one of the available document types debaterecord / bill / judgement / gui")
    private String documentType ;

    @Option(name="-om",required=true,  usage="specify one of the launch modes -- new / edit")
    private String openMode;

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static final int LAUNCH_FAIL = 0;
    public static final int LAUNCH_DOCTYPE = 1;
    public static final int LAUNCH_WITH_LAUNCHER = 2;

    public int doMain(String[] args) {
            for (String string : args) {
            System.out.println(string);
        }
            CmdLineParser cmdParser = new CmdLineParser(this);
            int returnState = LAUNCH_DOCTYPE;
            try {
                cmdParser.parseArgument(args);

            } catch (CmdLineException e) {
                        System.err.println(e.getMessage());
                        System.err.println("java -jar BungeniEditorClient.jar [options...] arguments...");
                        // print the list of available options
                        cmdParser.printUsage(System.err);
                        System.err.println();
                        // print option sample. This is useful some time
                        System.err.println("  Example: java -jar BungeniEditorClient.jar "+cmdParser.printExample(ALL));
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

    public String getDocType(){
        return this.documentType;
    }

    public String getLaunchMode(){
        return this.openMode;
    }
}
