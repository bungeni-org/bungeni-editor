package org.bungeni.ooo.transforms.impl;

/**
 *
 * @author Ashok Hariharan
 */
public class exportDestination extends Object {

    String exportDestName;
    String exportDestDesc;

    public exportDestination(String name, String desc) {
        this.exportDestDesc = desc;
        this.exportDestName = name;
    }

    @Override
    public String toString() {
        return this.exportDestDesc;
    }
}