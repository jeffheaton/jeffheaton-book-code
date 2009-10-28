/**
 * Network
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 11
 * Programming Neural Networks in Java
 * http://www.heatonresearch.com/articles/series/1/
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */

public class Network {

  /**
   * The global error for the training.
   */
  protected double globalError;


  /**
   * The number of input neurons.
   */
  protected int inputCount;

  /**
   * The number of hidden neurons.
   */
  protected int hiddenCount;

  /**
   * The number of output neurons
   */
  protected int outputCount;

  /**
   * The total number of neurons in the network.
   */
  protected int neuronCount;

  /**
   * The number of weights in the network.
   */
  protected int weightCount;

  /**
   * The learning rate.
   */
  protected double learnRate;

  /**
   * The outputs from the various levels.
   */
  protected double fire[];

  /**
   * The weight matrix this, along with the thresholds can be
   * thought of as the "memory" of the neural network.
   */
  protected double matrix[];

  /**
   * The errors from the last calculation.
   */
  protected double error[];

  /**
   * Accumulates matrix delta's for training.
   */
  protected double accMatrixDelta[];

  /**
   * The thresholds, this value, along with the weight matrix
   * can be thought of as the memory of the neural network.
   */
  protected double thresholds[];

  /**
   * The changes that should be applied to the weight
   * matrix.
   */
  protected double matrixDelta[];

  /**
   * The accumulation of the threshold deltas.
   */
  protected double accThresholdDelta[];

  /**
   * The threshold deltas.
   */
  protected double thresholdDelta[];

  /**
   * The momentum for training.
   */
  protected double momentum;

  /**
   * The changes in the errors.
   */
  protected double errorDelta[];


  /**
   * Construct the neural network.
   *
   * @param inputCount The number of input neurons.
   * @param hiddenCount The number of hidden neurons
   * @param outputCount The number of output neurons
   * @param learnRate The learning rate to be used when training.
   * @param momentum The momentum to be used when training.
   */
  public Network(int inputCount,
                 int hiddenCount,
                 int outputCount,
                 double learnRate,
                 double momentum) {

    this.learnRate = learnRate;
    this.momentum = momentum;

    this.inputCount = inputCount;
    this.hiddenCount = hiddenCount;
    this.outputCount = outputCount;
    neuronCount = inputCount + hiddenCount + outputCount;
    weightCount = (inputCount * hiddenCount) + (hiddenCount * outputCount);

    fire        = new double[neuronCount];
    matrix      = new double[weightCount];
    matrixDelta = new double[weightCount];
    thresholds  = new double[neuronCount];
    errorDelta  = new double[neuronCount];
    error       = new double[neuronCount];
    accThresholdDelta = new double[neuronCount];
    accMatrixDelta = new double[weightCount];
    thresholdDelta = new double[neuronCount];

    reset();
  }



  /**
   * Returns the root mean square error for a complete training set.
   *
   * @param len The length of a complete training set.
   * @return The current error for the neural network.
   */
  public double getError(int len) {
    double err = Math.sqrt(globalError / (len * outputCount));
    globalError = 0;  // clear the accumulator
    return err;

  }

  /**
   * The threshold method. You may wish to override this class to provide other
   * threshold methods.
   *
   * @param sum The activation from the neuron.
   * @return The activation applied to the threshold method.
   */
  public double threshold(double sum) {
    return 1.0 / (1 + Math.exp(-1.0 * sum));
  }

  /**
   * Compute the output for a given input to the neural network.
   *
   * @param input The input provide to the neural network.
   * @return The results from the output neurons.
   */
  public double []computeOutputs(double input[]) {
    int i, j;
    final int hiddenIndex = inputCount;
    final int outIndex = inputCount + hiddenCount;

    for (i = 0; i < inputCount; i++) {
      fire[i] = input[i];
    }

    // first layer
    int inx = 0;

    for (i = hiddenIndex; i < outIndex; i++) {
      double sum = thresholds[i];

      for (j = 0; j < inputCount; j++) {
        sum += fire[j] * matrix[inx++];
      }
      fire[i] = threshold(sum);
    }

    // hidden layer

    double result[] = new double[outputCount];

    for (i = outIndex; i < neuronCount; i++) {
      double sum = thresholds[i];

      for (j = hiddenIndex; j < outIndex; j++) {
        sum += fire[j] * matrix[inx++];
      }
      fire[i] = threshold(sum);
      result[i-outIndex] = fire[i];
    }

    return result;
  }


  /**
   * Calculate the error for the recognition just done.
   *
   * @param ideal What the output neurons should have yielded.
   */
  public void calcError(double ideal[]) {
    int i, j;
    final int hiddenIndex = inputCount;
    final int outputIndex = inputCount + hiddenCount;

    // clear hidden layer errors
    for (i = inputCount; i < neuronCount; i++) {
      error[i] = 0;
    }

    // layer errors and deltas for output layer
    for (i = outputIndex; i < neuronCount; i++) {
      error[i] = ideal[i - outputIndex] - fire[i];
      globalError += error[i] * error[i];
      errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
    }

    // hidden layer errors
    int winx = inputCount * hiddenCount;

    for (i = outputIndex; i < neuronCount; i++) {
      for (j = hiddenIndex; j < outputIndex; j++) {
        accMatrixDelta[winx] += errorDelta[i] * fire[j];
        error[j] += matrix[winx] * errorDelta[i];
        winx++;
      }
      accThresholdDelta[i] += errorDelta[i];
    }

    // hidden layer deltas
    for (i = hiddenIndex; i < outputIndex; i++) {
      errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
    }

    // input layer errors
    winx = 0;  // offset into weight array
    for (i = hiddenIndex; i < outputIndex; i++) {
      for (j = 0; j < hiddenIndex; j++) {
        accMatrixDelta[winx] += errorDelta[i] * fire[j];
        error[j] += matrix[winx] * errorDelta[i];
        winx++;
      }
      accThresholdDelta[i] += errorDelta[i];
    }
  }

  /**
   * Modify the weight matrix and thresholds based on the last call to
   * calcError.
   */
  public void learn() {
    int i;

    // process the matrix
    for (i = 0; i < matrix.length; i++) {
      matrixDelta[i] = (learnRate * accMatrixDelta[i]) + (momentum * matrixDelta[i]);
      matrix[i] += matrixDelta[i];
      accMatrixDelta[i] = 0;
    }

    // process the thresholds
    for (i = inputCount; i < neuronCount; i++) {
      thresholdDelta[i] = learnRate * accThresholdDelta[i] + (momentum * thresholdDelta[i]);
      thresholds[i] += thresholdDelta[i];
      accThresholdDelta[i] = 0;
    }
  }

  /**
   * Reset the weight matrix and the thresholds.
   */
  public void reset() {
    int i;

    for (i = 0; i < neuronCount; i++) {
      thresholds[i] = 0.5 - (Math.random());
      thresholdDelta[i] = 0;
      accThresholdDelta[i] = 0;
    }
    for (i = 0; i < matrix.length; i++) {
      matrix[i] = 0.5 - (Math.random());
      matrixDelta[i] = 0;
      accMatrixDelta[i] = 0;
    }
  }

  /**
   * Convert to an array. This is used with some training algorithms
   * that require that the "memory" of the neuron(the weight and threshold
   * values) be expressed as a linear array.
   *
   * @return The memory of the neuron.
   */
  public double []toArray()
  {
    double result[] = new double[matrix.length+thresholds.length];
    for (int i=0;i<matrix.length;i++)
      result[i] = matrix[i];
    for (int i=0;i<thresholds.length;i++)
      result[matrix.length+i] = thresholds[i];
    return result;
  }

  /**
   * Use an array to populate the memory of the neural network.
   *
   * @param array An array of doubles.
   */
  public void fromArray(double array[])
  {
    for (int i=0;i<matrix.length;i++)
      matrix[i] = array[i];
    for (int i=0;i<thresholds.length;i++)
      thresholds[i] = array[matrix.length+i];
  }

  /**
   * Get the number of input neurons.
   *
   * @return The number of input neurons.
   */
  public int getInputCount()
  {
    return inputCount;
  }

  /**
   * Get the number of output neurons.
   *
   * @return The number of output neurons.
   */
  public int getOutputCount()
  {
    return outputCount;
  }

  /**
   * Get the number of hidden neurons.
   *
   * @return The number of hidden neurons.
   */
  public int getHiddenCount()
  {
    return hiddenCount;
  }

  /**
   * Get the learning rate.
   *
   * @return The learning rate.
   */
  public double getLearnRate()
  {
    return learnRate;
  }

  /**
   * Get the momentum.
   *
   * @return The momentum.
   */
  public double getMomentum()
  {
    return momentum;
  }
}
