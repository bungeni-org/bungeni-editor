package org.bungeni.editor.panels.loadable;

/**
 *
 * @author Ashok Hariharan
 */
 class exportDestination extends Object {

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