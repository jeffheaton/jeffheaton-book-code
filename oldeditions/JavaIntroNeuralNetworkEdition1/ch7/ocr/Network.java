import java.util.*;

/**
 * Network
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 7
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

abstract public class Network {

  /**
   * The value to consider a neuron on
   */
  public final static double NEURON_ON=0.9;

  /**
   * The value to consider a neuron off
   */
  public final static double NEURON_OFF=0.1;

  /**
   * Output neuron activations
   */
  protected double output[];

  /**
   * Mean square error of the network
   */
  protected double totalError;

  /**
   * Number of input neurons
   */
  protected int inputNeuronCount;

  /**
   * Number of output neurons
   */
  protected int outputNeuronCount;

  /**
   * Random number generator
   */
  protected Random random = new Random(System.currentTimeMillis());


  /**
   * Called to learn from training sets.
   *
   * @exception java.lang.RuntimeException
   */
  abstract public void learn ()
  throws RuntimeException;

  /**
   * Called to present an input pattern.
   *
   * @param input The input pattern
   */
  abstract void trial(double []input);



  /**
   * Called to get the output from a trial.
   */
  double []getOutput()
  {
    return output;
  }


  /**
   * Calculate the length of a vector.
   *
   * @param v vector
   * @return Vector length.
   */
  static double vectorLength( double v[] )
  {
    double rtn = 0.0 ;
    for ( int i=0;i<v.length;i++ )
      rtn += v[i] * v[i];
    return rtn;
  }
  /**
   * Called to calculate a dot product.
   *
   * @param vec1 one vector
   * @param vec2 another vector
   * @return The dot product.
   */

double dotProduct(double vec1[] , double vec2[] )
  {
    int k,v;
    double rtn;

    rtn = 0.0;
    k = vec1.length;

    v = 0;
    while ( (k--)>0 ) {
      rtn += vec1[v] * vec2[v];
      v++;
    }

    return rtn;
  }
  /**
   * Called to randomize weights.
   *
   * @param weight A weight matrix.
   */
  void randomizeWeights( double weight[][] )
  {
    double r ;


    int temp = (int)(3.464101615 / (2. * Math.random() )) ; // SQRT(12)=3.464...

    for ( int y=0;y<weight.length;y++ ) {
      for ( int x=0;x<weight[0].length;x++ ) {
        r = (double) random.nextInt(Integer.MAX_VALUE) + (double) random.nextInt(Integer.MAX_VALUE) -
            (double) random.nextInt(Integer.MAX_VALUE) - (double) random.nextInt(Integer.MAX_VALUE) ;
        weight[y][x] = temp * r ;
      }
    }
  }

}