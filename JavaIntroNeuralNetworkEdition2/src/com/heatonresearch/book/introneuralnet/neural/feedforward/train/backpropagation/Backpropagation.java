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
package com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation;

import java.util.HashMap;
import java.util.Map;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;

/**
 * Backpropagation: This class implements a backpropagation 
 * training algorithm for feed forward neural networks.
 * It is used in the same manner as any other training class 
 * that implements the Train interface.
 * 
 * Backpropagation is a common neural network training algorithm.
 * It works by analyzing the error of the output of the neural 
 * network.  Each neuron in the output layer's contribution, according
 * to weight, to this error is determined.  These weights are 
 * then adjusted to minimize this error.  This process continues
 * working its way backwards through the layers of the neural
 * network.
 * 
 * This implementation of the backpropagation algorithm uses both
 * momentum and a learning rate.  The learning rate specifies the 
 * degree to which the weight matrixes will be modified through
 * each iteration.  The momentum specifies how much the previous
 * learning iteration affects the current.  To use no momentum
 * at all specify zero.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class Backpropagation implements Train {
	/**
	 * The error from the last iteration.
	 */
	private double error;

	/**
	 * The learning rate. This is the degree to which the deltas will affect the
	 * current network.
	 */
	private final double learnRate;

	/**
	 * The momentum, this is the degree to which the previous training cycle
	 * affects the current one.
	 */
	private final double momentum;

	/**
	 * THe network that is being trained.
	 */
	private final FeedforwardNetwork network;

	/**
	 * A map between neural network layers and the corresponding
	 * BackpropagationLayer.
	 */
	private final Map<FeedforwardLayer, BackpropagationLayer> layerMap = new HashMap<FeedforwardLayer, BackpropagationLayer>();

	/**
	 * Input patterns to train with.
	 */
	private final double input[][];

	/**
	 * The ideal output for each of the input patterns.
	 */
	private final double ideal[][];

	/**
	 * 
	 * @param network
	 * @param input
	 * @param ideal
	 * @param learnRate
	 *            The rate at which the weight matrix will be adjusted based on
	 *            learning.
	 * @param momentum
	 *            The influence that previous iteration's training deltas will
	 *            have on the current iteration.
	 */
	public Backpropagation(final FeedforwardNetwork network,
			final double input[][], final double ideal[][],
			final double learnRate, final double momentum) {
		this.network = network;
		this.learnRate = learnRate;
		this.momentum = momentum;
		this.input = input;
		this.ideal = ideal;

		for (final FeedforwardLayer layer : network.getLayers()) {
			final BackpropagationLayer bpl = new BackpropagationLayer(this,
					layer);
			this.layerMap.put(layer, bpl);
		}
	}

	/**
	 * Calculate the error for the recognition just done.
	 * 
	 * @param ideal
	 *            What the output neurons should have yielded.
	 */
	public void calcError(final double ideal[]) {

		if (ideal.length != this.network.getOutputLayer().getNeuronCount()) {
			throw new NeuralNetworkError(
					"Size mismatch: Can't calcError for ideal input size="
							+ ideal.length + " for output layer size="
							+ this.network.getOutputLayer().getNeuronCount());
		}

		// clear out all previous error data
		for (final FeedforwardLayer layer : this.network.getLayers()) {
			getBackpropagationLayer(layer).clearError();
		}

		for (int i = this.network.getLayers().size() - 1; i >= 0; i--) {
			final FeedforwardLayer layer = this.network.getLayers().get(i);
			if (layer.isOutput()) {

				getBackpropagationLayer(layer).calcError(ideal);
			} else {
				getBackpropagationLayer(layer).calcError();
			}
		}
	}

	/**
	 * Get the BackpropagationLayer that corresponds to the specified layer.
	 * @param layer The specified layer.
	 * @return The BackpropagationLayer that corresponds to the specified layer.
	 */
	public BackpropagationLayer getBackpropagationLayer(
			final FeedforwardLayer layer) {
		final BackpropagationLayer result = this.layerMap.get(layer);

		if (result == null) {
			throw new NeuralNetworkError(
					"Layer unknown to backpropagation trainer, was a layer added after training begain?");
		}

		return result;
	}

	/**
	 * Returns the root mean square error for a complete training set.
	 * 
	 * @param len
	 *            The length of a complete training set.
	 * @return The current error for the neural network.
	 */
	public double getError() {
		return this.error;
	}

	/**
	 * Get the current best neural network.
	 * @return The current best neural network.
	 */
	public FeedforwardNetwork getNetwork() {
		return this.network;
	}

	/**
	 * Perform one iteration of training.
	 */
	public void iteration() {

		for (int j = 0; j < this.input.length; j++) {
			this.network.computeOutputs(this.input[j]);
			calcError(this.ideal[j]);
		}
		learn();
		
		this.error = this.network.calculateError(this.input, this.ideal);
	}

	/**
	 * Modify the weight matrix and thresholds based on the last call to
	 * calcError.
	 */
	public void learn() {

		for (final FeedforwardLayer layer : this.network.getLayers()) {
			getBackpropagationLayer(layer).learn(this.learnRate, this.momentum);
		}

	}
}
