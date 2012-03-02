/*
 * schemeRomanTest.java
 * JUnit based test
 *
 * Created on May 7, 2008, 12:12 PM
 */

package org.bungeni.numbering.schemes;

import junit.framework.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class schemeRomanTest extends TestCase {
    
    public schemeRomanTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of generateSequence method, of class org.bungeni.numbering.schemes.schemeRoman.
     */
    public void testGenerateSequence() {
        System.out.println("generateSequence");
        
        schemeRoman numObj = new schemeRoman((long)12, (long) 33);
        numObj.setParentPrefix("1");
        numObj.generateSequence();
        ArrayList<String> seq = numObj.getGeneratedSequence();
        Iterator<String> iter = seq.iterator();
        while (iter.hasNext()) {
            String elem = iter.next();
            System.out.println(elem);
        }
        this.assertTrue((seq != null));
        this.assertTrue((seq.size() > 0));
    }

    
}
