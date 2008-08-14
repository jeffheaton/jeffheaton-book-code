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
package com.heatonresearch.book.introneuralnet.ch5.xor;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation.Backpropagation;

/**
 * Chapter 5: The Feedforward Backpropagation Neural Network
 * 
 * XOR: Learn the XOR pattern with a feedforward neural network that
 * uses backpropagation.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class XOR {

	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(final String args[]) {
		final FeedforwardNetwork network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(2));
		network.addLayer(new FeedforwardLayer(3));
		network.addLayer(new FeedforwardLayer(1));
		network.reset();

		// train the neural network
		final Train train = new Backpropagation(network, XOR_INPUT, XOR_IDEAL,
				0.7, 0.9);

		int epoch = 1;

		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while ((epoch < 5000) && (train.getError() > 0.001));

		// test the neural network
		System.out.println("Neural Network Results:");
		for (int i = 0; i < XOR_IDEAL.length; i++) {
			final double actual[] = network.computeOutputs(XOR_INPUT[i]);
			System.out.println(XOR_INPUT[i][0] + "," + XOR_INPUT[i][1]
					+ ", actual=" + actual[0] + ",ideal=" + XOR_IDEAL[i][0]);
		}
	}
}
