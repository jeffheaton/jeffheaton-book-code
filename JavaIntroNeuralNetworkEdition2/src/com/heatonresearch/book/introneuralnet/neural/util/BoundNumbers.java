/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package com.heatonresearch.book.introneuralnet.neural.util;

/**
 * BoundNumbers: A simple class that prevents numbers from
 * getting either too big or too small.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class BoundNumbers {
	
	/**
	 * Too small of a number.
	 */
	public static final double TOO_SMALL = -1.0E20;
	
	/**
	 * Too big of a number.
	 */
	public static final double TOO_BIG = 1.0E20;

	/**
	 * Bound the number so that it does not become too big or too small.
	 * 
	 * @param d
	 *            The number to check.
	 * @return The new number. Only changed if it was too big or too small.
	 */
	public static double bound(final double d) {
		if (d < TOO_SMALL) {
			return TOO_SMALL;
		} else if (d > TOO_BIG) {
			return TOO_BIG;
		} else {
			return d;
		}
	}
	
	/**
	 * A bounded version of Math.exp.
	 * @param d What to calculate.
	 * @return The result.
	 */
	public static double exp(final double d) {
		return bound(Math.exp(d));
	}
}
