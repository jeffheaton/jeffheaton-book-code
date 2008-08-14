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
package com.heatonresearch.book.introneuralnet.ch13.train;

import java.io.Serializable;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * HistogramElement: An element in the histogram.  This is the
 * word, and the number of times it came up in either the good
 * or bad set.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class HistogramElement implements Comparable<HistogramElement>,
		Serializable {
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -5100257451318427243L;
	private String word;
	private int count;

	public HistogramElement(final String word, final int count) {
		this.word = word.toLowerCase();
		this.count = count;
	}

	public int compareTo(final HistogramElement o) {
		final int result = o.getCount() - getCount();
		if (result == 0) {
			return this.getWord().compareTo(o.getWord());
		} else {
			return result;
		}
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return this.word;
	}

	public void increase() {
		this.count++;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(final int count) {
		this.count = count;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(final String word) {
		this.word = word.toLowerCase();
	}

}
