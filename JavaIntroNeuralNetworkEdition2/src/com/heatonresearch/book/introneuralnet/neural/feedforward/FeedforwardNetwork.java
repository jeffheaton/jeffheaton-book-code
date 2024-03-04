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
package com.heatonresearch.book.introneuralnet.neural.feedforward;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;
import com.heatonresearch.book.introneuralnet.neural.matrix.MatrixCODEC;
import com.heatonresearch.book.introneuralnet.neural.util.ErrorCalculation;

/**
 * FeedforwardNeuralNetwork: This class implements a feed forward
 * neural network.  This class works in conjunction the 
 * FeedforwardLayer class.  Layers are added to the 
 * FeedforwardNeuralNetwork to specify the structure of the neural
 * network.
 * 
 * The first layer added is the input layer, the final layer added
 * is the output layer.  Any layers added between these two layers
 * are the hidden layers.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class FeedforwardNetwork implements Serializable {
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -136440631687066461L;
	
	/**
	 * The input layer.
	 */
	protected FeedforwardLayer inputLayer;
	
	/**
	 * The output layer.
	 */
	protected FeedforwardLayer outputLayer;
	
	/**
	 * All of the layers in the neural network.
	 */
	protected List<FeedforwardLayer> layers = new ArrayList<FeedforwardLayer>();
	
	/**
	 * Construct an empty neural network.
	 */
	public FeedforwardNetwork() {
	}

	/**
	 * Add a layer to the neural network. The first layer added is the input
	 * layer, the last layer added is the output layer.
	 * 
	 * @param layer The layer to be added.
	 */
	public void addLayer(final FeedforwardLayer layer) {
		// setup the forward and back pointer
		if (this.outputLayer != null) {
			layer.setPrevious(this.outputLayer);
			this.outputLayer.setNext(layer);
		}

		// update the inputLayer and outputLayer variables
		if (this.layers.size() == 0) {
			this.inputLayer = this.outputLayer = layer;
		} else {
			this.outputLayer = layer;
		}

		// add the new layer to the list
		this.layers.add(layer);
	}

	/**
	 * Calculate the error for this neural network. The error is calculated
	 * using root-mean-square(RMS).
	 * 
	 * @param input
	 *            Input patterns.
	 * @param ideal
	 *            Ideal patterns.
	 * @return The error percentage.
	 * @throws NeuralNetworkException
	 *             An error happened trying to determine the error.
	 */
	public double calculateError(final double input[][], final double ideal[][])
			throws NeuralNetworkError {
		final ErrorCalculation errorCalculation = new ErrorCalculation();

		for (int i = 0; i < ideal.length; i++) {
			computeOutputs(input[i]);
			errorCalculation.updateError(this.outputLayer.getFire(), 
					ideal[i]);
		}
		return (errorCalculation.calculateRMS());
	}

	/**
	 * Calculate the total number of neurons in the network across all layers.
	 * 
	 * @return The neuron count.
	 */
	public int calculateNeuronCount() {
		int result = 0;
		for (final FeedforwardLayer layer : this.layers) {
			result += layer.getNeuronCount();
		}
		return result;
	}

	/**
	 * Return a clone of this neural network. Including structure, weights and
	 * threshold values.
	 * 
	 * @return A cloned copy of the neural network.
	 */
	@Override
	public Object clone() {
		final FeedforwardNetwork result = cloneStructure();
		final Double copy[] = MatrixCODEC.networkToArray(this);
		MatrixCODEC.arrayToNetwork(copy, result);
		return result;
	}

	/**
	 * Return a clone of the structure of this neural network. 
	 * 
	 * @return A cloned copy of the structure of the neural network.
	 */

	public FeedforwardNetwork cloneStructure() {
		final FeedforwardNetwork result = new FeedforwardNetwork();

		for (final FeedforwardLayer layer : this.layers) {
			final FeedforwardLayer clonedLayer = new FeedforwardLayer(layer.getActivationFunction(), layer
					.getNeuronCount());
			result.addLayer(clonedLayer);
		}

		return result;
	}

	/**
	 * Compute the output for a given input to the neural network.
	 * 
	 * @param input
	 *            The input provide to the neural network.
	 * @return The results from the output neurons.
	 * @throws MatrixException A matrix error occurred.
	 * @throws NeuralNetworkException A neural network error occurred.
	 */
	public double[] computeOutputs(final double input[]) {

		if (input.length != this.inputLayer.getNeuronCount()) {
			throw new NeuralNetworkError(
					"Size mismatch: Can't compute outputs for input size="
							+ input.length + " for input layer size="
							+ this.inputLayer.getNeuronCount());
		}

		for (final FeedforwardLayer layer : this.layers) {
			if (layer.isInput()) {
				layer.computeOutputs(input);
			} else if (layer.isHidden()) {
				layer.computeOutputs(null);
			}
		}

		return this.outputLayer.getFire();
	}

	/**
	 * Compare the two neural networks. For them to be equal they must be of the
	 * same structure, and have the same matrix values.
	 * 
	 * @param other
	 *            The other neural network.
	 * @return True if the two networks are equal.
	 */
	public boolean equals(final FeedforwardNetwork other) {
		final Iterator<FeedforwardLayer> otherLayers = other.getLayers()
				.iterator();

		for (final FeedforwardLayer layer : this.getLayers()) {
			final FeedforwardLayer otherLayer = otherLayers.next();

			if (layer.getNeuronCount() != otherLayer.getNeuronCount()) {
				return false;
			}

			// make sure they either both have or do not have
			// a weight matrix.
			if ((layer.getMatrix() == null) && (otherLayer.getMatrix() != null)) {
				return false;
			}

			if ((layer.getMatrix() != null) && (otherLayer.getMatrix() == null)) {
				return false;
			}

			// if they both have a matrix, then compare the matrices
			if ((layer.getMatrix() != null) && (otherLayer.getMatrix() != null)) {
				if (!layer.getMatrix().equals(otherLayer.getMatrix())) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Get the count for how many hidden layers are present.
	 * @return The hidden layer count.
	 */
	public int getHiddenLayerCount() {
		return this.layers.size() - 2;
	}

	/**
	 * Get a collection of the hidden layers in the network.
	 * @return The hidden layers.
	 */
	public Collection<FeedforwardLayer> getHiddenLayers() {
		final Collection<FeedforwardLayer> result = new ArrayList<FeedforwardLayer>();
		for (final FeedforwardLayer layer : this.layers) {
			if (layer.isHidden()) {
				result.add(layer);
			}
		}
		return result;
	}

	/**
	 * Get the input layer.
	 * @return The input layer.
	 */
	public FeedforwardLayer getInputLayer() {
		return this.inputLayer;
	}

	/**
	 * Get all layers.
	 * @return All layers.
	 */
	public List<FeedforwardLayer> getLayers() {
		return this.layers;
	}

	/**
	 * Get the output layer.
	 * @return The output layer.
	 */
	public FeedforwardLayer getOutputLayer() {
		return this.outputLayer;
	}

	/**
	 * Get the size of the weight and threshold matrix.
	 * @return The size of the matrix.
	 */
	public int getWeightMatrixSize() {
		int result = 0;
		for (final FeedforwardLayer layer : this.layers) {
			result += layer.getMatrixSize();
		}
		return result;
	}

	/**
	 * Reset the weight matrix and the thresholds.
	 * 
	 * @throws MatrixException
	 */
	public void reset() {
		for (final FeedforwardLayer layer : this.layers) {
			layer.reset();
		}
	}
}
