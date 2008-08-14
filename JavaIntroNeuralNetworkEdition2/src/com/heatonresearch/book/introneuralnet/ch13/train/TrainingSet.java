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

import java.util.ArrayList;
import java.util.List;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * TrainingSet: Used to build a training set based on both
 * good and bad sentences.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class TrainingSet {
	private List<double[]> input = new ArrayList<double[]>();
	private List<double[]> ideal = new ArrayList<double[]>();

	public void addTrainingSet(final double[] addInput, final double addIdeal) {
		// does the training set already exist
		for (final double[] element : this.input) {
			if (compare(addInput, element)) {
				return;
			}
		}

		// add it
		final double array[] = new double[1];
		array[0] = addIdeal;
		this.input.add(addInput);
		this.ideal.add(array);

	}

	private boolean compare(final double[] d1, final double[] d2) {
		boolean result = true;

		for (int i = 0; i < d1.length; i++) {
			if (Math.abs(d1[i] - d2[i]) > 0.000001) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * @return the ideal
	 */
	public List<double[]> getIdeal() {
		return this.ideal;
	}

	/**
	 * @return the input
	 */
	public List<double[]> getInput() {
		return this.input;
	}

	/**
	 * @param ideal
	 *            the ideal to set
	 */
	public void setIdeal(final List<double[]> ideal) {
		this.ideal = ideal;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(final List<double[]> input) {
		this.input = input;
	}

}
