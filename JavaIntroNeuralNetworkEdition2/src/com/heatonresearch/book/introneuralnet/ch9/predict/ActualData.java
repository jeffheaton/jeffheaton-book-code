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
package com.heatonresearch.book.introneuralnet.ch9.predict;

/**
 * Chapter 9: Predictive Neural Networks
 * 
 * ActualData: Holds values from the sine wave.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ActualData {
	public static double sinDEG(final double deg) {
		final double rad = deg * (Math.PI / 180);
		final double result = Math.sin(rad);
		return ((int) (result * 100000.0)) / 100000.0;
	}

	private final double actual[];
	private final int inputSize;

	private final int outputSize;

	public ActualData(final int size, final int inputSize, final int outputSize) {
		this.actual = new double[size];
		this.inputSize = inputSize;
		this.outputSize = outputSize;

		int angle = 0;
		for (int i = 0; i < this.actual.length; i++) {
			this.actual[i] = sinDEG(angle);
			angle += 10;
		}
	}

	public void getInputData(final int offset, final double target[]) {
		for (int i = 0; i < this.inputSize; i++) {
			target[i] = this.actual[offset + i];
		}
	}

	public void getOutputData(final int offset, final double target[]) {
		for (int i = 0; i < this.outputSize; i++) {
			target[i] = this.actual[offset + this.inputSize + i];
		}
	}
	

}
