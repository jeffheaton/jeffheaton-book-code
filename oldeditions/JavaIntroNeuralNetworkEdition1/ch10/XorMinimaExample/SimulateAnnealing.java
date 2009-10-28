/**
 * SimulateAnnealing
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
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


public class SimulateAnnealing {

  /**
   * The neural network that is to be trained.
   */
  protected Network network;

  /**
   * The training data.
   */
  protected double input[][];

  /**
   * The ideal results to the training data.
   */
  protected double ideal[][];

  /**
   * The starting temperature.
   */
  protected double startTemperature;

  /**
   * The ending temperature.
   */
  protected double stopTemperature;

  /**
   * The number of cycles that will be used.
   */
  protected int cycles;

  /**
   * The current error.
   */
  protected double globalError;

  /**
   * The constructor.
   *
   * @param network The neural network that is to be trained.
   */
  public SimulateAnnealing(Network network)
  {
    this.network = network;
  }

  /**
   * Set the training input.
   *
   * @param input The data that is to be used to train the neural network.
   */
  public void setInput(double input[][])
  {
    this.input = input;
  }

  /**
   * Set the ideal results for the training data.
   *
   * @param ideal The ideal results for the training data.
   */
  public void setIdeal(double ideal[][])
  {
    this.ideal = ideal;
  }

  /**
   * The current temperature.
   */
  protected double temperature;

  /**
   * Randomize the weights and thresholds. This function
   * does most of the work of the class. Each call to this
   * class will randomize the data according to the current
   * temperature. The higher the temperature the more randomness.
   */
  protected void randomize()
  {
    double ratio = temperature * Math.sqrt(12);

    double array[] = network.toArray();

    for (int i=0;i<array.length;i++) {
      double add = 0.5 - (Math.random());
      add /= startTemperature;
      add *= temperature;
      array[i] = array[i] + add;
    }

    network.fromArray(array);
  }

  /**
   * Determine the error of the current weights and
   * thresholds.
   */
  protected double determineError()
  {
    for (int i=0;i<input.length;i++) {
      network.computeOutputs(input[i]);
      network.calcError(ideal[i]);
    }
    return network.getError(input.length);
  }

  /**
   * Initialize the simulated annealing class.
   *
   * @param startTemp The starting temperature.
   * @param stopTemp The ending temperature.
   * @param cycles The number of cycles to use.
   */
  public void start(double startTemp,double stopTemp,int cycles)
  {
    temperature = startTemp;
    this.cycles = cycles;
    this.startTemperature = startTemp;
    this.stopTemperature = stopTemp;
  }

  /**
   * Called to perform one cycle of the annealing process.
   */
  public void anneal()
  {
    double bestArray[];

    globalError = determineError();
    bestArray = network.toArray();

    for (int i=0;i<100;i++) {
      double curError;
      randomize();
      curError = determineError();
      if (curError<globalError) {
        bestArray = network.toArray();
        globalError = curError;
      }
    }

    network.fromArray(bestArray);
    double ratio = Math.exp(Math.log(stopTemperature/startTemperature)/(cycles-1));
    temperature*=ratio;
  }

  /**
   * Get the current temperature.
   *
   * @return The current temperature.
   */
  public double getTemperature()
  {
    return temperature;
  }

  /**
   * Get the global error.
   *
   * @return The global error.
   */
  public double getGlobalError()
  {
    return globalError;
  }

}