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
package com.heatonresearch.book.introneuralnet.ch10.sp500;

import java.util.Date;

/**
 * Chapter 10: Application to the Financial Markets
 * 
 * InterestRate: Hold the prime interest rate for the specified
 * date.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class InterestRate implements Comparable<InterestRate> {
	private Date effectiveDate;
	private double rate;

	public InterestRate(final Date effectiveDate, final double rate) {
		this.effectiveDate = effectiveDate;
		this.rate = rate;
	}

	public int compareTo(final InterestRate other) {
		return getEffectiveDate().compareTo(other.getEffectiveDate());
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return this.rate;
	}

	/**
	 * @param effectiveDate
	 *            the effectiveDate to set
	 */
	public void setEffectiveDate(final Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(final double rate) {
		this.rate = rate;
	}

}
