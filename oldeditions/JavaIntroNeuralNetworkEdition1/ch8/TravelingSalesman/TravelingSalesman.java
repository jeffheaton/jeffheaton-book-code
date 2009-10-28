import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

/**
 * TravelingSalesman
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 8
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

public class TravelingSalesman
  extends JFrame
  implements Runnable,ComponentListener {

  /**
   * How many cities to use.
   */
  public static final int CITY_COUNT = 50;

  /**
   * How many chromosomes to use.
   */
  public static final int POPULATION_SIZE = 1000;

  /**
   * What percent of new-borns to mutate.
   */
  public static final double MUTATION_PERCENT = 0.10;

  /**
   * The part of the population eligable for mateing.
   */
  protected int matingPopulationSize = POPULATION_SIZE/2;

  /**
   * The part of the population favored for mating.
   */
  protected int favoredPopulationSize = matingPopulationSize/2;

  /**
   * A Map object that will display the city map.
   */
  protected Map map = null;

  /**
   * The current status. Used to display the current status to the
   * user.
   */
  protected JLabel status;

  /**
   * How much genetic material to take during a mating.
   */
  protected int cutLength = CITY_COUNT/5;

  /**
   * The current generation, or epoc.
   */
  protected int generation;

  /**
   * The background worker thread.
   */
  protected Thread worker = null;

  /**
   * Is the thread started.
   */
  protected boolean started = false;

  /**
   * The list of cities.
   */
  protected City [] cities;

  /**
   * The list of chromosomes.
   */
  protected Chromosome [] chromosomes;

  /**
   * The constructor
   */
  public TravelingSalesman() {
    addComponentListener(this);
    setSize(300,300);
    setTitle("Traveling Salesman Problem");
  }

  /**
   * Not used, but required by Java.
   *
   * @param e The event.
   */
  public void componentMoved(ComponentEvent e)
  {
  }

  /**
   * Not used, but required by Java.
   *
   * @param e The event.
   */
  public void componentResized(ComponentEvent e)
  {
  }

  /**
   * Not used, but required by Java.
   *
   * @param e The event.
   */
  public void componentHidden(ComponentEvent e)
  {
  }

  /**
   * Used to add necessary components.
   *
   * @param e The event.
   */
  public void componentShown(ComponentEvent e)
  {
    getContentPane().setLayout(new BorderLayout());

    if ( map == null ) {
      map = new Map(this);
      getContentPane().add(map,"Center");
      status = new JLabel("Starting up");
      getContentPane().add(status,"South");
    }
    start();

  }

  /**
   * Start the background thread.
   */
  public void start() {
// create a random list of cities

    cities = new City[TravelingSalesman.CITY_COUNT];
    for ( int i=0;i<TravelingSalesman.CITY_COUNT;i++ ) {
      cities[i] = new City(
        (int)(Math.random()*(getBounds().width-10)),
        (int)(Math.random()*(getBounds().height-60)));
    }


    // create the initial chromosomes

    chromosomes = new Chromosome[TravelingSalesman.POPULATION_SIZE];
    for ( int i=0;i<TravelingSalesman.POPULATION_SIZE;i++ ) {
      chromosomes[i] = new Chromosome(cities);
      chromosomes[i].setCut(cutLength);
      chromosomes[i].setMutation(TravelingSalesman.MUTATION_PERCENT);
    }
    Chromosome.sortChromosomes(chromosomes,TravelingSalesman.POPULATION_SIZE);

// start up the background thread
    started = true;
    map.update(map.getGraphics());

    generation = 0;

    if ( worker != null )
        worker = null;
    worker = new Thread(this);
    //worker.setPriority(Thread.MIN_PRIORITY);
    worker.start();
  }

  /**
   * The main loop for the background thread.
   * It is here that most of the work os orchestrated.
   */
  public void run() {

    double thisCost = 500.0;
    double oldCost = 0.0;
    double dcost = 500.0;
    int countSame = 0;

    map.update(map.getGraphics());

    while(countSame<100) {

      generation++;

      int ioffset = matingPopulationSize;
      int mutated = 0;


      // Mate the chromosomes in the favoured population
      // with all in the mating population
      for ( int i=0;i<favoredPopulationSize;i++ ) {
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
        chromosomes[i].calculateCost(cities);
      }


      // Now sort the new mating population
      Chromosome.sortChromosomes(chromosomes,matingPopulationSize);

      double cost = chromosomes[0].getCost();
      dcost = Math.abs(cost-thisCost);
      thisCost = cost;
      double mutationRate = 100.0 * (double) mutated / (double) matingPopulationSize;

      NumberFormat nf = NumberFormat.getInstance();
      nf.setMinimumFractionDigits(2);
      nf.setMinimumFractionDigits(2);

      status.setText("Generation "+generation+" Cost "+(int)thisCost+" Mutated "+nf.format(mutationRate)+"%");


      if ( (int)thisCost == (int)oldCost ) {
        countSame++;
      } else {
        countSame = 0;
        oldCost = thisCost;
      }
      map.update(map.getGraphics());


    }
    status.setText("Solution found after "+generation+" generations.");
  }

  /**
   * The main method.
   *
   * @param args Not used
   */
  public static void main(String args[])
  {
    (new TravelingSalesman()).show();
  }

}
