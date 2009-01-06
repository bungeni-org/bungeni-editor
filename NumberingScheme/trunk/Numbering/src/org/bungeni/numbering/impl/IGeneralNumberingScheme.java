/*
 * IGeneralNumberingScheme.java
 *
 * Created on March 18, 2008, 2:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

import java.util.ArrayList;

/**
 * General numbering scheme interface, all numbering scheme classes need to implement this interface (apart form extending BaseNumberingScheme)
 * Example :
 * <PRE>   
 *    IGeneralNumberingScheme inumScheme = null;
 *    inumScheme = NumberingSchemeFactory.getNumberingScheme("ROMAN");
 *    inumScheme.setRange(new NumberRange((long)12, (long)26));
 *    inumScheme.generateSequence();
 *    ArrayList<String> seq = inumScheme.getGeneratedSequence();
 *    Iterator<String> iter = seq.iterator();
 *    while (iter.hasNext()) {
 *            System.out.println(iter.next().toString());
 *        }
 *  </PRE>
 * @author Ashok Hariharan
 */
public interface IGeneralNumberingScheme {
    /**
     * Sets the starting and ending range for the numbering scheme with a NumberRange object
     * @param range NumberRange object specifying starting and ending range
     */
    public void setRange(NumberRange range);
    /**
     * Optional parameter, allows adding a prefix to the number, useful for generating parent-relative prefixed numbering schemes (i.e. 1.a , 1.b...and so on...)
     * @param parentPrefix prefix to be applied before the generated number
     * @param separator separator between parentPrefix and the generated number
     */
    public void setParentPrefix (String parentPrefix, String separator);
    
    /**
     * sets the parent prefix to attach to the generated number.  uses the default separator "."
     * @param parentPrefix prefix to be added to number
     */
    public void setParentPrefix (String parentPrefix ) ;
    /**
     * generates the number sequence
     */
    public void generateSequence();
    /**
     * returns the generated sequence as an ArrayList
     * @return Returns an ArrayList of generated numbers as a string
     */
    public ArrayList<String> getGeneratedSequence();
    
    /**
     * set descriptive text to describe the numbering scheme
     * @param desc Description string to be set 
     */
    public void setSchemeDescription(String desc);
    
    /**
     * Returns the scheme description
     * @return Returns a string description of the scheme
     */
    public String getSchemeDescription();
    
    /**
     * Returns the next number in the generated numbering scheme sequence
     * @param getTheNumberAfterThis if a sequence has one, two, three and four, the input parameter "two" should return "three" as the next number in the sequence.
     * @return The next number in the sequence. If the input parameter is already the last number
     * in the sequence, the function throws ArrayIndexOutOfBoundsException.
     * If the input parameter is not found in the sequence the function throws NoSuchElementException.
     */
    public String getNextInSequence (String getTheNumberAfterThis);
    
    /**
     * Initialize the iterator for the number sequence
     * @return returns an Iterator<String>, but the Iterator is a member variable of the class, and hence should be iterated using the class methods.
     */
    public java.util.Iterator<String> sequence_initIterator();
   
    /**
     * Check if the iterator can iterate further
     * @return returns true if the iterator has more elements
     */
    public boolean sequence_hasNext() ;
   
    /**
     * Retrieves the next number in the number sequence
     * @return returns a string representation of the generated number
     */
    public String sequence_next();

    /**
     * Every generated number has an underlying long integer number (i.e iv --> underlying integer 4 , e --> underlying integer 5
     * @return returns underlying long integer representing the input generated number
     */
    public long sequence_base_index (String item ) ;
}
