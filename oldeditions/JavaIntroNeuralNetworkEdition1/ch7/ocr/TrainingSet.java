import java.io.*;
import java.util.*;

/**
 * TrainingSet
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

public class TrainingSet {

  protected int inputCount;
  protected int outputCount;
  protected double input[][];
  protected double output[][];
  protected int trainingSetCount;

  /**
   * The constructor.
   *
   * @param inputCount Number of input neurons
   * @param outputCount Number of output neurons
   */
  TrainingSet ( int inputCount , int outputCount )
  {
    this.inputCount = inputCount;
    this.outputCount = outputCount;
    trainingSetCount = 0;
  }

  /**
   * Get the input neuron count
   *
   * @return The input neuron count
   */
  public int getInputCount()
  {
    return inputCount;
  }

  /**
   * Get the output neuron count
   *
   * @return The output neuron count
   */
  public int getOutputCount()
  {
    return outputCount;
  }

  /**
   * Set the number of entries in the training set. This method
   * also allocates space for them.
   *
   * @param trainingSetCount How many entries in the training set.
   */
  public void setTrainingSetCount(int trainingSetCount)
  {
    this.trainingSetCount = trainingSetCount;
    input = new double[trainingSetCount][inputCount];
    output = new double[trainingSetCount][outputCount];
  }

  /**
   * Get the training set data.
   *
   * @return Training set data.
   */
  public int getTrainingSetCount()
  {
    return trainingSetCount;
  }

  /**
   * Set one of the training set's inputs.
   *
   * @param set The entry number
   * @param index The index(which item in that set)
   * @param value The value
   * @exception java.lang.RuntimeException
   */
  void setInput(int set,int index,double value)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (index>=inputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    input[set][index] = value;
  }


  /**
   * Set one of the training set's outputs.
   *
   * @param set The entry number
   * @param index The index(which item in that set)
   * @param value The value
   * @exception java.lang.RuntimeException
   */
  void setOutput(int set,int index,double value)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (set>=outputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    output[set][index] = value;
  }


  /**
   * Get a specified input value.
   *
   * @param set The input entry.
   * @param index The index
   * @return An individual input
   * @exception java.lang.RuntimeException
   */
  double getInput(int set,int index)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (index>=inputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    return input[set][index];
  }

  /**
   * Get one of the output values.
   *
   * @param set The entry
   * @param index Which value in the entry
   * @return The output value.
   * @exception java.lang.RuntimeException
   */
  double getOutput(int set,int index)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    if ( (index<0) || (set>=outputCount) )
      throw(new RuntimeException("Training input index out of range:" + index ));
    return output[set][index];
  }

  /**
   * Get an output set.
   *
   * @param set The entry requested.
   * @return The complete output set as an array.
   * @exception java.lang.RuntimeException
   */

  double []getOutputSet(int set)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    return output[set];
  }

  /**
   * Get an input set.
   *
   * @param set The entry requested.
   * @return The complete input set as an array.
   * @exception java.lang.RuntimeException
   */

  double []getInputSet(int set)
  throws RuntimeException
  {
    if ( (set<0) || (set>=trainingSetCount) )
      throw(new RuntimeException("Training set out of range:" + set ));
    return input[set];
  }


}
