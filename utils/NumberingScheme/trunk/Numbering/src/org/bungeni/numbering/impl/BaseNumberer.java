/*
 * BaseNumberer.java
 *
 * Created on March 18, 2008, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.numbering.impl;

import net.sf.saxon.expr.number.AbstractNumberer;

/**
 * Abstract base class that extends AbstractNumberer
 * @author Ashok
 */
  abstract class BaseNumberer extends AbstractNumberer {
        abstract public String toWords(long l);

        abstract public String toOrdinalWords(String string, long l, int i);

        abstract public String monthName(int i, int i0, int i1);

        abstract public String dayName(int i, int i0, int i1) ;
  }
    
