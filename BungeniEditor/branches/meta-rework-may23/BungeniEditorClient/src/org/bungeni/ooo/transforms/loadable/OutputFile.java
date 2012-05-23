package org.bungeni.ooo.transforms.loadable;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Ashok Hariharan
 */
public class OutputFile {
    String         fileName;
    String         fullPathToFile;
    OutputFileType outputFileType;

    public OutputFile(String fullPath, String type) {
        outputFileType = new OutputFileType(type);
        fullPathToFile = fullPath;
        fileName       = (new File(fullPathToFile)).getName();
    }

    public String getFullFileName() {
        return outputFileType.generateFileName(fullPathToFile);
    }

    public File getFullFile() {
        return new File(getFullFileName());
    }
}
