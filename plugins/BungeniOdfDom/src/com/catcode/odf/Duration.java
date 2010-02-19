/*
    Duration creates an XML-Schema-style Duration object.
    Copyright (C) 2005  J. David Eisenberg

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

	Author: J. David Eisenberg
	Contact: catcode@catcode.com

*/
package com.catcode.odf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Creates an XML-Schema-style Duration object.
 *	@author		J. David Eisenberg
 *	@version	0.1, 2005-10-24
 */
public class Duration
{
	protected int years;
	protected int months;
	protected int days;
	protected int hours;
	protected int minutes;
	protected double seconds;
	protected boolean negative;

	private static final Pattern durationPattern =
		Pattern.compile("-?P(?:(\\d+)Y)?(?:(\\d+)M)?(?:(\\d+)D)?"
			+ "(?:T(?:(\\d+)H)?(?:(\\d+)M)?(?:([\\d.]+)S)?)?");

	/**
	 * Create a new <code>Duration</code> with all fields
	 * set to zero.
	 */
	public Duration()
	{
		this( 0, 0, 0, 0, 0, 0.0 );
	}

	/**
	 * Create a new <code>Duration</code> with
	 * <code>years</code>, <code>months</code> and <code>days</code>
	 * set as specified.
	 *
	 * @param years the number of years.
	 * @param months the number of months.
	 * @param days the number of days
	 */
	public Duration( int years, int months, int days )
	{
		this( years, months, days, 0, 0, 0.0 );
	}

	/**
	 * Create a new <code>Duration</code> with all
	 * numeric fields set as specified.
	 *
	 * @param years the number of years.
	 * @param months the number of months.
	 * @param days the number of days
	 * @param hours the number of hours.
	 * @param minutes the number of minutes.
	 * @param seconds the number of seconds.
	 */
	public Duration( int years, int months, int days,
		int hours, int minutes, double seconds )
	{
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.negative = false;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public int getYears()
	{
		return this.years;
	}

	/**
	 * Sets the number of years of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param years the number of years.
	 */
	public void setYears(int years)
	{
		this.years = years;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public int getMonths()
	{
		return this.months;
	}

	/**
	 * Sets the number of months of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param months the number of months.
	 */
	public void setMonths(int months)
	{
		this.months = months;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public int getDays()
	{
		return this.days;
	}

	/**
	 * Sets the number of days of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param days the number of days.
	 */
	public void setDays(int days)
	{
		this.days = days;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public int getHours()
	{
		return this.hours;
	}

	/**
	 * Sets the number of hours of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param hours the number of hours.
	 */
	public void setHours(int hours)
	{
		this.hours = hours;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public int getMinutes()
	{
		return this.minutes;
	}

	/**
	 * Sets the number of minutes of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param minutes the number of minutes.
	 */
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}

	/**
	 * Returns the number of years of this <code>Duration</code>.
	 * @return number of years of this <code>Duration</code>.
	 */
	public double getSeconds()
	{
		return this.seconds;
	}

	/**
	 * Sets the number of seconds of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param seconds the number of seconds.
	 */
	public void setSeconds(double seconds)
	{
		this.seconds = seconds;
	}

	/**
	 * Returns the negative status <code>Duration</code>.
	 * @return status of the <code>negative</code> field of
	 * this <code>Duration</code>.
	 */
	public boolean isNegative()
	{
		return this.negative;
	}

	/**
	 * Sets the negative status of this <code>Duration</code> to
	 * the specified value.
	 *
	 * @param negative the negative status.
	 */
	public void setNegative( boolean negative )
	{
		this.negative = negative;
	}

	/**
	 * Parses the string argument as a <code>Duration</code>.
	 * The format of the string should be
	 * <code>PyyYmmMddDThhHmmMssS</code> with an optional leading
	 * minus sign. The seconds may contain a decimal point.
	 *
	 * @param str the string to parse
	 * @return the <code>Duration</code> specified by the string.
	 */
	public static Duration parseDuration( String str )
	{
		String part;
		Duration d = new Duration();
		Matcher m = durationPattern.matcher( str );
		if (m.find())
		{
			d.negative = (str.startsWith("-"));
			try
			{
				part = m.group(1);
				d.years = (part != null ) ? Integer.parseInt( part ) : 0;
				part = m.group(2);
				d.months = (part != null ) ? Integer.parseInt( part ) : 0;
				part = m.group(3);
				d.days = (part != null ) ? Integer.parseInt( part ) : 0;
				part = m.group(4);
				d.hours = (part != null ) ? Integer.parseInt( part ) : 0;
				part = m.group(5);
				d.minutes = (part != null ) ? Integer.parseInt( part ) : 0;
				part = m.group(6);
				d.seconds = (part != null ) ? Double.parseDouble( part ) : 0.0;
			}
			catch (Exception e)
			{
				d = null;
			}
		}
		else
		{
			d = null;
		}
		return d;
	}

	/**
	 * Returns a string representation of this
	 * <code>Duration</code>.
	 *
	 * @return a string representation of
	 * this <code>Duration</code>.
	 */
    @Override
	public String toString( )
	{
		String result = (negative) ? "-P" : "P";

		if (years > 0 || months > 0 || days > 0)
		{
			if (years > 0)
			{
				result += years + "Y";
			}
			if (months > 0)
			{
				result += months + "M";
			}
			if (days > 0)
			{
				result += days + "D";
			}
		}
		else
		{
			result += "0D";
		}
		if (hours > 0 || minutes > 0 || seconds > 0)
		{
			result += "T";
			if (hours > 0)
			{
				result += hours + "H";
			}
			if (minutes > 0)
			{
				result += minutes + "M";
			}
			if (seconds > 0)
			{
				result += seconds + "S";
			}
		}
		return result;
	}
}

