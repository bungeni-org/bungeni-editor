package org.bungeni.ooo.transforms.loadable;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.extutils.CommonANUtils;

public class OutputFileType {
    String fileType;

    public OutputFileType(String fType) {
        this.fileType = fType;
    }

    public String generateFileName(String baseFileName) {
        String filePrefix = CommonANUtils.getFilePrefix(baseFileName);
        String fileExt    = CommonANUtils.getFileExt(baseFileName);

        if (fileExt.equals(fileType)) {
            return baseFileName;
        } else {
            return filePrefix + "_" + fileType + ((fileExt.length() > 0)
                    ? "." + fileExt
                    : "");
        }
    }
}
