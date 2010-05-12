/*
 * IteratorAsEnumeration.java
 *
 * Created on February 25, 2008, 1:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

/**
 *
 * @author undesa
 */
import java.util.Enumeration;
import java.util.Iterator;

public class IteratorAsEnumeration implements Enumeration
{
	Iterator iterator;

	public IteratorAsEnumeration(Iterator aIterator)
	{
		iterator = aIterator;
	}

	public boolean hasMoreElements()
	{
		return iterator.hasNext();
	}

	public Object nextElement()
	{
		return iterator.next();
	}
}

