
package org.bungeni.extutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;


/**
 *
 * @author Ashok
 */
public class CommonShellFunctions {

       private static Logger log = Logger.getLogger(CommonShellFunctions.class.getName());

       /**
        * Runs a command line command
        * @param cmd - the command to be run
        * @return - String - returns the output of the command
        */
    public static boolean runCommand(ArrayList<String> cmd, String workingDirectory, boolean backgroundFlag) {
        boolean bState = false;
        try {
            if (backgroundFlag) {
                //run it in the background without waiting for feedback
                cmd.add(0,"nohup");
            }
            System.out.println("running this command : in directory = " + workingDirectory);
            for (String string : cmd) {
                System.out.print(string + " ");
            }
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.directory(new File(workingDirectory));
            builder.redirectErrorStream(true);
            System.out.println("current directory = " + builder.directory());
            final Process process = builder.start();
            // wait for feedback in a background thread so runCommand can return immediately
            (new Thread(){
                @Override
                @SuppressWarnings("empty-statement")
                public void run(){
                    try {
                        while (process.getInputStream().read() != -1){};
                    } catch (IOException ex) {
                      log.error("run:process.inputStream", ex);
                    }
                }

            }).start();
            bState = true;
            /*
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line ="" ;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }*/
        } catch (IOException ex) {
           log.error("runCommand : ", ex);
           ex.printStackTrace(System.out);
        }
        return bState;
    }





}
