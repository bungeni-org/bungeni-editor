/*
 * NumberRange.java
 *
 * Created on March 18, 2008, 12:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

/**
 * NumberRange class, stores range information, starting ,ending.
 * @author Ashok
 */
public class NumberRange {
    /**
     * starting range , a property that can be set directly
     */
    public long start;
    /**
     * ending range - a property that can be set directly
     */
    public long end;
    /** Creates a new instance of NumberRange */
    public NumberRange() {
        start= (long) 1;
        end = (long) 10;
    }
    /**
     * Constructor to initialize a NumberRange object. 
     * Requires a starting and ending number to describe the range
     * @param s starting range of number
     * @param e ending range of number
     */
    public NumberRange(long s, long e) {
       setRange(s, e);
    }
    /**
     * Function to explicitly set the range. Used when the default constructor is called to initialize a blank NumberRange object.
     * @param s starting number of range
     * @param e ending number of range.
     */
    public void setRange (long s, long e) {
        start = s;
        end = e;
    }
}
