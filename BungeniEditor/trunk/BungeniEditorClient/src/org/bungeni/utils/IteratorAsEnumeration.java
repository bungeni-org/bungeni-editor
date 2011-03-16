package org.bungeni.utils;

/**
 *
 * @author Ashok Hariharan
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

