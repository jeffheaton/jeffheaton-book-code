/**
 * Prune
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

public class Prune {

  /**
   * The neural network that is currently being processed.
   */
  protected Network currentNetwork;

  /**
   * The training set.
   */
  protected double train[][];

  /**
   * The ideal results from the training set.
   */
  protected double ideal[][];

  /**
   * The desired learning rate.
   */
  protected double rate;

  /**
   * The desired momentum.
   */
  protected double momentum;;
  protected double minError;

  /**
   * The current error.
   */
  protected double error;

  /**
   * Used to determine if training is still effectve. Holds the error level
   * the last time the error level was tracked. This is 1000 cycles ago. If
   * no significant drop in error occurs for 1000 cycles, training ends.
   */
  protected double markErrorRate;

  /**
   * Used with markErrorRate. This is the number of cycles since the error
   * was last marked.
   */
  protected int sinceMark;

  /**
   * The number of cycles used.
   */
  protected int cycles;

  /**
   * The number of hidden neurons.
   */
  protected int hiddenNeuronCount;

  /**
   * Flag to indicate if the incramental prune process
   * is done or not.
   */
  protected boolean done;

  /**
   * Constructor used to setup the prune object for an incramental prune.
   *
   * @param rate The desired learning rate.
   * @param momentum The desired momentum.
   * @param train The training data.
   * @param ideal The ideal results for the training data.
   * @param minError The minimum error that is acceptable.
   */
  public Prune(double rate,double momentum,double train[][],double ideal[][],double minError)
  {
    this.rate = rate;
    this.momentum = momentum;
    this.train = train;
    this.ideal = ideal;
    this.minError = minError;
  }

  /**
   * Constructor that is designed to setup for a selective
   * prune.
   *
   * @param network The neural network that we wish to prune.
   * @param train
   * @param ideal
   */
  public Prune(Network network,double train[][],double ideal[][])
  {
    this.currentNetwork = network;
    this.train = train;
    this.ideal = ideal;
  }

  /**
   * Method that is called to start the incramental prune
   * process.
   */
  public void startIncramental()
  {
    hiddenNeuronCount = 1;
    cycles = 0;
    done = false;
    currentNetwork = new Network(
                                train[0].length,
                                hiddenNeuronCount,
                                ideal[0].length,
                                rate,
                                momentum);
  }

  /**
   * Internal method that is called at the end of each
   * incramental cycle.
   */
  protected void incrament()
  {
    boolean doit = false;

    if (markErrorRate==0) {
      markErrorRate = error;
      sinceMark = 0;
    } else {
      sinceMark++;
      if (sinceMark>10000) {
        if ( (markErrorRate-error)<0.01 )
          doit = true;
        markErrorRate = error;
        sinceMark = 0;
      }
    }


    if (error<minError)
      done = true;

    if (doit) {
      cycles = 0;
      hiddenNeuronCount++;
      currentNetwork = new Network(
                                  train[0].length,
                                  hiddenNeuronCount,
                                  ideal[0].length,
                                  rate,
                                  momentum);

    }
  }

  /**
   * Method that is called to prune the neural network incramentaly.
   */
  public void pruneIncramental()
  {
    if (done)
      return;
    for (int i=0;i<train.length;i++) {
      currentNetwork.computeOutputs(train[i]);
      currentNetwork.calcError(ideal[i]);
      currentNetwork.learn();
    }
    error = currentNetwork.getError(train.length);
    cycles++;
    incrament();
  }

  /**
   * Called to get the current error.
   *
   * @return The current error.
   */
  public double getError()
  {
    return error;
  }

  /**
   * Get the number of hidden neurons.
   *
   * @return The number of hidden neurons.
   */
  public double getHiddenNeuronCount()
  {
    return hiddenNeuronCount;
  }

  /**
   * Called to get the current number of cycles.
   *
   * @return The current number of cycles.
   */
  public int getCycles()
  {
    return cycles;
  }

  /**
   * Called to determine if we are done in an incremental prune.
   *
   * @return Returns true if we are done, false otherwise.
   */
  public boolean getDone()
  {
    return done;
  }

  /**
   * Get the current neural network.
   *
   * @return The neural network.
   */
  public Network getCurrentNetwork()
  {
    return currentNetwork;
  }

  /**
   * Internal method used to clip the hidden neurons.
   *
   * @param neuron The neuron to clip.
   * @return Returns the new neural network.
   */
  protected Network clipHiddenNeuron(int neuron)
  {
    Network result = new Network(
                                currentNetwork.getInputCount(),
                                currentNetwork.getHiddenCount()-1,
                                currentNetwork.getOutputCount(),
                                currentNetwork.getLearnRate(),
                                currentNetwork.getMomentum());

    double array1[] = currentNetwork.toArray();
    double array2[] = new double[array1.length];
    int index1 = 0;
    int index2 = 0;

    // weight matrix

    for (int i=0;i<currentNetwork.getInputCount();i++) {
      for (int j=0;j<currentNetwork.getHiddenCount();j++) {
        if (j!=neuron) {
          array2[index2] = array1[index1];
          index2++;
        }
        index1++;
      }
    }

    for (int i=0;i<currentNetwork.getHiddenCount();i++) {
      for (int j=0;j<currentNetwork.getOutputCount();j++) {
        if (i!=neuron) {
          array2[index2] = array1[index1];
          index2++;
        }
        index1++;
      }
    }


    // threshold

    int neuronCount =
    currentNetwork.getInputCount()+
    currentNetwork.getHiddenCount()+
    currentNetwork.getOutputCount();
    for (int i=0;i<neuronCount;i++) {
      if (i!=currentNetwork.getInputCount()+neuron) {
        array2[index2] = array1[index1];
        index2++;
      }
      index1++;
    }

    result.fromArray(array2);
    return result;
  }

  /**
   * Internal method to determine the error for a neural network.
   *
   * @param network The neural network that we are seeking a error rate for.
   * @return The error for the specified neural network.
   */
  protected double determineError(Network network)
  {
    for (int i=0;i<train.length;i++) {
      network.computeOutputs(train[i]);
      network.calcError(ideal[i]);
      network.learn();
    }
    return network.getError(train.length);

  }

  /**
   * Internal method that will loop through all hidden neurons and
   * prune them if pruning the neuron does not cause too great of an
   * increase in error.
   *
   * @return True if a prune was made, false otherwise.
   */
  protected boolean findNeuron()
  {
    double e1 = determineError(currentNetwork);

    for (int i=0;i<currentNetwork.getHiddenCount();i++) {
      Network trial = this.clipHiddenNeuron(i);
      double e2 = determineError(trial);
      if ( Math.abs(e1-e2)<0.2 ) {
        currentNetwork = trial;
        return true;
      }
    }
    return false;
  }


  /**
   * Called to complete the selective pruning process.
   *
   * @return The number of neurons that were pruned.
   */
  public int pruneSelective()
  {
    int i = currentNetwork.getHiddenCount();
    while (findNeuron());
    return(i-currentNetwork.getHiddenCount());
  }

}

