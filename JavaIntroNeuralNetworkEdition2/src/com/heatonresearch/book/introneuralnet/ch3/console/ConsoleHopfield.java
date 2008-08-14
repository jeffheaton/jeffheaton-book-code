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
package com.heatonresearch.book.introneuralnet.ch3.console;

import com.heatonresearch.book.introneuralnet.neural.hopfield.HopfieldNetwork;

/**
 * Chapter 3: Using a Hopfield Neural Network
 * 
 * ConsoleHopfield: Simple console application that shows how to
 * use a Hopfield Neural Network.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class ConsoleHopfield {

	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(final boolean b[]) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.length - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}

	/**
	 * A simple main method to test the Hopfield neural network.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {

		// Create the neural network.
		final HopfieldNetwork network = new HopfieldNetwork(4);
		// This pattern will be trained
		final boolean[] pattern1 = { true, true, false, false };
		// This pattern will be presented
		final boolean[] pattern2 = { true, false, false, false };
		boolean[] result;

		// train the neural network with pattern1
		System.out.println("Training Hopfield network with: "
				+ formatBoolean(pattern1));
		network.train(pattern1);
		// present pattern1 and see it recognized
		result = network.present(pattern1);
		System.out.println("Presenting pattern:" + formatBoolean(pattern1)
				+ ", and got " + formatBoolean(result));
		// Present pattern2, which is similar to pattern 1. Pattern 1
		// should be recalled.
		result = network.present(pattern2);
		System.out.println("Presenting pattern:" + formatBoolean(pattern2)
				+ ", and got " + formatBoolean(result));

	}

}
