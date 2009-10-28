/**
 * XorMinimaExampleGenetic
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

public class Genetic {

  /**
   * The global error.
   */
  public double globalError = 1;

  /**
   * How many chromosomes to use.
   */
  public static final int POPULATION_SIZE = 5000;

  /**
   * What percent of new-borns to mutate.
   */
  public static final double MUTATION_PERCENT = 0.10;

  /**
   * The part of the population eligible for mateing.
   */
  protected int matingPopulationSize = POPULATION_SIZE/2;

  /**
   * The part of the population favored for mating.
   */
  protected int favoredPopulationSize = matingPopulationSize/2;

  /**
   * Training data.
   */
  protected double input[][];

  /**
   * The ideal results for the training data.
   */
  protected double ideal[][];



  /**
   * The list of chromosomes.
   */
  protected Chromosome [] chromosomes;

  /**
   * The neural network to train.
   */
  Network network;

  /**
   * Get the error for the best chromosome.
   *
   * @return The error for the best chromosome.
   */
  public double getGlobalError()
  {
    return globalError;
  }

  /**
   * Construct a new genetic algorithm class.
   *
   * @param network The neural network that is to be optimized.
   */
  public Genetic(Network network)
  {
    this.network = network;
  }

  /**
   * Init the genetic algorithm.
   */
  public void start()
  {
    chromosomes = new Chromosome[POPULATION_SIZE];
    for (int i=0;i<POPULATION_SIZE;i++) {
      network.reset();
      chromosomes[i] = new Chromosome(network);
      chromosomes[i].calculateCost(network,input,ideal);

      Chromosome c = chromosomes[i];
      c.fromMatrix();
      c.toMatrix();

    }
    Chromosome.sortChromosomes(chromosomes,chromosomes.length);
  }


  /**
   * Set the training data that will be used to evaluate the
   * error level of the weight matrixes.
   *
   * @param input The training data.
   */
  public void setInput(double input[][])
  {
    this.input = input;
  }

  /**
   * Set the idea results for the training data.
   *
   * @param ideal The ideal results for the training data.
   */
  public void setIdeal(double ideal[][])
  {
    this.ideal = ideal;
  }


  /**
   * Process one generation.
   */
  public void generation()
  {

    int ioffset = matingPopulationSize;
    int mutated = 0;
    double thisCost = 500.0;
    double oldCost = 0.0;
    double dcost = 500.0;
    int countSame = 0;



    // Mate the chromosomes in the favoured population
    // with all in the mating population
    for ( int i=0;i<favoredPopulationSize-1;i++ ) {
      Chromosome cmother = chromosomes[i];
      // Select partner from the mating population
      int father = (int) ( 0.999999*Math.random()*(double)matingPopulationSize);
      Chromosome cfather = chromosomes[father];

      mutated += cmother.mate(cfather,chromosomes[ioffset],chromosomes[ioffset+1]);
      ioffset += 2;
    }

    // The new generation is in the matingPopulation area
    // move them to the correct area for sort.
    for ( int i=0;i<matingPopulationSize;i++ ) {
      chromosomes[i] = chromosomes[i+matingPopulationSize];
      chromosomes[i].calculateCost(network,input,ideal);
    }


    // Now sort the new mating population
    Chromosome.sortChromosomes(chromosomes,matingPopulationSize);

    double cost = chromosomes[0].getCost();
    dcost = Math.abs(cost-thisCost);
    thisCost = cost;
    double mutationRate = 100.0 * (double) mutated / (double) matingPopulationSize;
    globalError = thisCost;
    chromosomes[0].toMatrix();
    network.fromArray(chromosomes[0].getMatrix());


  }

}