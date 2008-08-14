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

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.matrix.Matrix;
import com.heatonresearch.book.introneuralnet.neural.matrix.MatrixMath;
import com.heatonresearch.book.introneuralnet.neural.util.BoundNumbers;

/**
 * BackpropagationLayer: The back propagation training algorithm
 * requires training data to be stored for each of the layers.
 * The Backpropagation class creates a BackpropagationLayer object
 * for each of the layers in the neural network that it is training.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */

public class BackpropagationLayer {
	private final double error[];

	private final double errorDelta[];

	/**
	 * Accumulate the error deltas for each weight matrix and bias value.
	 */
	private Matrix accMatrixDelta;

	/**
	 * The bias values are stored in a "virtual row" just beyond the regular
	 * weight rows. This variable holds the index to that location.
	 */
	private int biasRow;

	/**
	 * Hold the previous matrix deltas so that "momentum" can be implemented.
	 * This handles both weights and thresholds.
	 */
	private Matrix matrixDelta;

	/**
	 * The parent object.
	 */
	private final Backpropagation backpropagation;

	/**
	 * The actual layer that this training layer corresponds to.
	 */
	private final FeedforwardLayer layer;

	/**
	 * Construct a BackpropagationLayer object that corresponds to a specific neuron layer.
	 * @param backpropagation The back propagation training object.
	 * @param layer The layer that this object corresponds to.
	 */
	public BackpropagationLayer(final Backpropagation backpropagation,
			final FeedforwardLayer layer) {
		this.backpropagation = backpropagation;
		this.layer = layer;

		final int neuronCount = layer.getNeuronCount();

		this.error = new double[neuronCount];
		this.errorDelta = new double[neuronCount];

		if (layer.getNext() != null) {
			this.accMatrixDelta = new Matrix(layer.getNeuronCount() + 1, layer
					.getNext().getNeuronCount());
			this.matrixDelta = new Matrix(layer.getNeuronCount() + 1, layer
					.getNext().getNeuronCount());
			this.biasRow = layer.getNeuronCount();
		}
	}

	/**
	 * Accumulate a matrix delta.
	 * @param i1 The matrix row.
	 * @param i2 The matrix column.
	 * @param value The delta value.
	 */
	public void accumulateMatrixDelta(final int i1, final int i2,
			final double value) {
		this.accMatrixDelta.add(i1, i2, value);
	}

	/**
	 * Accumulate a threshold delta.
	 * @param index The threshold index.
	 * @param value The threshold value.
	 */
	public void accumulateThresholdDelta(final int index, final double value) {
		this.accMatrixDelta.add(this.biasRow, index, value);
	}

	/**
	 * Calculate the current error.
	 */
	public void calcError() {

		final BackpropagationLayer next = this.backpropagation
				.getBackpropagationLayer(this.layer.getNext());

		for (int i = 0; i < this.layer.getNext().getNeuronCount(); i++) {
			for (int j = 0; j < this.layer.getNeuronCount(); j++) {
				accumulateMatrixDelta(j, i, next.getErrorDelta(i)
						* this.layer.getFire(j));
				setError(j, getError(j) + this.layer.getMatrix().get(j, i)
						* next.getErrorDelta(i));
			}
			accumulateThresholdDelta(i, next.getErrorDelta(i));
		}

		if (this.layer.isHidden()) {
			// hidden layer deltas
			for (int i = 0; i < this.layer.getNeuronCount(); i++) {
				setErrorDelta(i, BoundNumbers.bound(calculateDelta(i)));
			}
		}

	}

	/**
	 * Calculate the error for the given ideal values.
	 * @param ideal Ideal output values.
	 */
	public void calcError(final double ideal[]) {

		// layer errors and deltas for output layer
		for (int i = 0; i < this.layer.getNeuronCount(); i++) {
			setError(i, ideal[i] - this.layer.getFire(i));
			setErrorDelta(i, BoundNumbers.bound(calculateDelta(i)));
		}
	}

	/**
	 * Calculate the delta for actual vs ideal. This is the amount that will be
	 * applied during learning.
	 * 
	 * @param i
	 *            The neuron being calculated for.
	 * @return The delta to be used to learn.
	 */
	private double calculateDelta(final int i) {
		return getError(i) * 
		this.layer.getActivationFunction().derivativeFunction(this.layer.getFire(i));
	}

	/**
	 * Clear any error values.
	 */
	public void clearError() {
		for (int i = 0; i < this.layer.getNeuronCount(); i++) {
			this.error[i] = 0;
		}
	}

	/**
	 * Get the error for the specified neuron.
	 * @param index The index for the specified neuron.
	 * @return The error for the specified neuron.
	 */
	public double getError(final int index) {
		return this.error[index];
	}

	/**
	 * Get the error delta for the specified neuron.
	 * @param index The specified neuron.
	 * @return The error delta.
	 */
	public double getErrorDelta(final int index) {
		return this.errorDelta[index];
	}

	/**
	 * Learn from the last error calculation.
	 * @param learnRate The learning rate.
	 * @param momentum The momentum.
	 */
	public void learn(final double learnRate, final double momentum) {
		// process the matrix
		if (this.layer.hasMatrix()) {

			final Matrix m1 = MatrixMath.multiply(this.accMatrixDelta,
					learnRate);
			final Matrix m2 = MatrixMath.multiply(this.matrixDelta, momentum);
			this.matrixDelta = MatrixMath.add(m1, m2);
			this.layer.setMatrix(MatrixMath.add(this.layer.getMatrix(),
					this.matrixDelta));
			this.accMatrixDelta.clear();
		}
	}

	/**
	 * Set the error for the specified neuron.
	 * @param index The specified neuron.
	 * @param e The error value.
	 */
	public void setError(final int index, final double e) {
		this.error[index] = BoundNumbers.bound(e);
	}

	/**
	 * Set the error delta for the specified neuron.
	 * @param index The specified neuron.
	 * @param d The error delta.
	 */
	public void setErrorDelta(final int index, final double d) {
		this.errorDelta[index] = d;
	}
}
