/*
 * BaseNumberingScheme.java
 *
 * Created on March 18, 2008, 12:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Base class for numbering schemes, all numbering schemes extend this class
 * @author Ashok
 */
public class BaseNumberingScheme   {
    
    /**
     * NumberRange object used by base class
     */
    protected NumberRange baseRange = new NumberRange();
    /**
     * baseSequence ArrayList object, (long) never used directly by the derived class, but used to generate the derived class sequence
     */
    protected ArrayList<Long> baseSequence = new ArrayList<Long>();
    /**
     * ArrayList that stores the derived class generated sequence, (Array of strings)
     */
    protected ArrayList<String> generatedSequence=new ArrayList<String>();
    static String DEFAULT_SEPARATOR = ".";
    protected boolean hasPrefix = false;
    protected String parentPrefix ;
    protected String parentPrefixSeparator = DEFAULT_SEPARATOR;
    
    protected String schemeDescription;
    
    /**
     * base class constructor, needs to be explicitly invoked with super() from the derived class
     */
    public BaseNumberingScheme() {
        baseRange = new NumberRange((long)1, (long)10);
        baseSequence = new ArrayList<Long>();
    }
    
    /**
     * constructor that takes in start, end ranges, called with super(long, long) from the deriveed class overriden constructor
     */
    public BaseNumberingScheme(long rStart, long rEnd){
        baseRange = new NumberRange(rStart, rEnd);
        baseSequence = new ArrayList<Long>();
    }
    
    /**
     * returns the NumberRange object
     */
    public NumberRange getRange() {
        return baseRange;
    }

    /**
     * sets the NumberRange object
     */
    public void setRange(NumberRange range) {
        baseRange = range;
    }

    /**
     * Returns the baseSequence object
     */
    public ArrayList<Long> getSequence() {
        return baseSequence;
    }

    /**
     * generates the baseSequence (list of Long objects)
     */
   public void generateSequence() {
      System.out.println("baseRange.start = " + baseRange.start);
      for(long i=baseRange.start;i<=baseRange.end;i++){
            baseSequence.add(i);
        }
    }
    
    /**
     * returns the generatedSequence object (the derived sequence)
     */
   public ArrayList<String> getGeneratedSequence(){
       return generatedSequence;
   }
   
   private Iterator<String> generatedSequenceIterator ;
   
   public Iterator<String> sequence_initIterator(){
       generatedSequenceIterator = generatedSequence.iterator();
       return this.generatedSequenceIterator;
   }
   
   public boolean sequence_hasNext() {
        return this.generatedSequenceIterator.hasNext();
   }
   
   public String sequence_next(){
        return this.generatedSequenceIterator.next();
   }
   
   public long sequence_base_index (String item ) {
        int nIndex = this.generatedSequence.indexOf(item);
        if (nIndex != -1 )
            return this.baseSequence.get(nIndex);
        else
            return -1;
   }
   
   public String getNextInSequence(String sequenceNumber) {
        if (generatedSequence.contains(sequenceNumber)) {
            int indexofSequenceNumber = generatedSequence.indexOf(sequenceNumber);
            java.util.ListIterator<String> iterSequence = generatedSequence.listIterator(indexofSequenceNumber);
            iterSequence.next();
            if (iterSequence.hasNext()) {
                return iterSequence.next();
            } else {
                throw new java.lang.ArrayIndexOutOfBoundsException(sequenceNumber + " is at the end of the sequence ");
            }
            
        } else {
            throw new java.util.NoSuchElementException(sequenceNumber + " is not a member of the generatedSequence ");
        }
   }
   
   public boolean hasParentPrefix() {
       return hasPrefix;
   }
   
   public void setParentPrefix (String pPrefix, String pSeparator) {
       setParentPrefix (pPrefix);
       this.parentPrefixSeparator = pSeparator;
   }
 
   public void setParentPrefix (String pPrefix) {
       this.hasPrefix = true;
       this.parentPrefix = pPrefix;
   }
   
   public void addNumberToSequence (String number) {
       if (hasParentPrefix()) {
           generatedSequence.add(parentPrefix + parentPrefixSeparator + number);
       } else {
            generatedSequence.add(number);
       }
   }
   
   public void setSchemeDescription(String desc) {
       this.schemeDescription = desc;
   }
   
   public String getSchemeDescription(){
       return this.schemeDescription;
   }
   
}
