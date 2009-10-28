/**
 * Neuron
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


public class Neuron {

  /**
   * The weights between this neuron and the other neurons on
   * the layer.
   */
  public int weightv[];

  /**
   * Activation results for this neuron.
   */
  public int activation;

  /**
   * The constructor. The weights between this neuron and
   * every other neuron(including itself) is passed in as
   * an array. Usually the weight between this neuron and
   * itself is zero.
   *
   * @param in The weight vector.
   */
  public Neuron(int in[])
  {
    weightv = in;
  }

  /**
   * This method is called to determine if the neuron would
   * activate, or fire.
   *
   * @param x Neuron input
   * @return If the neuron would activate, or fire
   */
  public int act(boolean x[] )
  {
    int i;
    int a=0;

    for ( i=0;i<x.length;i++ )
      if ( x[i] )
        a+=weightv[i];
    return a;
  }

}