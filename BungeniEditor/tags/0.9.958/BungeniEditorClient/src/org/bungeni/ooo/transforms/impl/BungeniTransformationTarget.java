package org.bungeni.ooo.transforms.impl;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniTransformationTarget {
    public String targetClass       = "";
    public String targetDescription = "";
    public String targetExt         = "";
    public String targetName        = "";

    /** Creates a new instance of BungeniTransformationTarget */
    public BungeniTransformationTarget() {}

    public BungeniTransformationTarget(String name, String desc, String ext, String sClass) {
        this.targetName        = name;
        this.targetDescription = desc;
        this.targetExt         = ext;
        this.targetClass       = sClass;
    }

    @Override
    public String toString() {
        return this.targetDescription;
    }
}
