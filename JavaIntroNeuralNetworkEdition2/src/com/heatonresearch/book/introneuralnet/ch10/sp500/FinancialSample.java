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

import java.text.NumberFormat;
import java.util.Date;

import com.heatonresearch.book.introneuralnet.common.ReadCSV;

/**
 * Chapter 10: Application to the Financial Markets
 * 
 * FinancialSample: Holds a sample of financial data at the specified
 * date.  This includes the close of the SP500 and the prime interest
 * rate.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class FinancialSample implements Comparable<FinancialSample> {
	private double amount;
	private double rate;
	private Date date;
	private double percent;

	public int compareTo(final FinancialSample other) {
		return getDate().compareTo(other.getDate());
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return this.amount;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the percent
	 */
	public double getPercent() {
		return this.percent;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return this.rate;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(final double amount) {
		this.amount = amount;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * @param percent
	 *            the percent to set
	 */
	public void setPercent(final double percent) {
		this.percent = percent;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(final double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		final NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		final StringBuilder result = new StringBuilder();
		result.append(ReadCSV.displayDate(this.date));
		result.append(", Amount: ");
		result.append(this.amount);
		result.append(", Prime Rate: ");
		result.append(this.rate);
		result.append(", Percent from Previous: ");
		result.append(nf.format(this.percent));
		return result.toString();
	}

}
