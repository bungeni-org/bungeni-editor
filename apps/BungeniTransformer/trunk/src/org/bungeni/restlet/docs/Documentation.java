package org.bungeni.restlet.docs;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.restlet.resources.OdtResource;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class Documentation {
    private static org.apache.log4j.Logger log = Logger.getLogger(Documentation.class.getName());

    public Documentation() {}

    // ----------------------------------------------------------------
    // Reader -> Writer
    // ----------------------------------------------------------------

    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     * @param input the <code>Reader</code> to read from
     * @param output the <code>Writer</code> to write to
     * @return the number of characters copied
     * @throws IOException In case of an I/O problem
     */
    public static int copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1024];
        int    count  = 0;
        int    n      = 0;

        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    // ----------------------------------------------------------------
    // InputStream -> Writer
    // ----------------------------------------------------------------

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>.
     * The platform's default encoding is used for the byte-to-char conversion.
     * @param input the <code>InputStream</code> to read from
     * @param output the <code>Writer</code> to write to
     * @throws IOException In case of an I/O problem
     */
    public static void copy(InputStream input, Writer output) throws IOException {
        InputStreamReader in = new InputStreamReader(input);

        copy(in, output);
    }

    private static String getDocumentationFile(String docPath) {
        InputStream  docStream = OdtResource.class.getResourceAsStream(docPath);
        StringWriter swDocFile = new StringWriter();

        try {
            copy(docStream, swDocFile);
        } catch (IOException e) {

            // TODO Auto-generated catch block
            log.error("getDocumentationFile:", e);
            e.printStackTrace();
        }

        return swDocFile.toString();
    }

    public static String getDocumentation(String docUrl) {
        String docHtml = getDocumentationFile(docUrl);

        return docHtml;
    }
}
