/**
 * Layer
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 2
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

public class Layer {

  /**
   * An array of neurons.
   */
  protected Neuron neuron[] = new Neuron[4];

  /**
   * The output of the neurons.
   */
  protected boolean output[] = new boolean[4];

  /**
   * The number of neurons in this layer. And because this is a
   * single layer neural network, this is also the number of
   * neurons in the network.
   */
  protected int neurons;

  /**
   * A constant to multiply against the threshold function.
   * This is not used, and is set to 1.
   */
  public static final double lambda = 1.0;

  /**
   * The constructor. The weight matrix for the
   * neurons must be passed in. Because this is
   * a single layer network the weight array should
   * always be perfectly square(i.e. 4x4). These
   * weights are used to initialize the neurons.
   *
   * @param weights A 2d array that contains the weights between each
   * neuron and the other neurons.
   */
  Layer(int weights[][])
  {
    neurons = weights[0].length;

    neuron = new Neuron[neurons];
    output = new boolean[neurons];

    for ( int i=0;i<neurons;i++ )
      neuron[i]=new Neuron(weights[i]);
  }

  /**
   * The threshold method is used to determine if the neural
   * network will fire for a given pattern. This threshold
   * uses the hyperbolic tangent (tanh).
   *
   * @param k The product of the neuron weights and the input
   * pattern.
   * @return Whether to fire or not to fire.
   */
  public boolean threshold(int k)
  {
    double kk = k * lambda;
    double a = Math.exp( kk );
    double b = Math.exp( -kk );
    double tanh = (a-b)/(a+b);
    return(tanh>=0);
  }

  /**
   * This method is called to actually run the neural network.
   *
   * @param pattern The input pattern to present to the neural network.
   */
  void activation(boolean pattern[])
  {
    int i,j;
    for ( i=0;i<4;i++ ) {
      neuron[i].activation = neuron[i].act(pattern);
      output[i] = threshold(neuron[i].activation);
    }
  }
}