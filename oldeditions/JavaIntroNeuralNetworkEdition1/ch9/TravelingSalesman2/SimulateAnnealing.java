/**
 * SimulateAnnealing
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
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

public class SimulateAnnealing extends Thread {

  /**
   * The TravelingSalesman object that owns this object.
   */
  protected TravelingSalesman owner;

  /**
   * The current temperature.
   */
  protected double temperature;

  /**
   * The length of the current path.
   */
  protected double pathlength;

  /**
   * The length of the best path.
   */
  protected double minimallength;

  /**
   * The current order of cities.
   */
  protected int order[];

  /**
   * The best order of cities.
   */
  protected int minimalorder[];

  /**
   * Constructor
   *
   * @param owner The TravelingSalesman class that owns this object.
   */
  SimulateAnnealing(TravelingSalesman owner)
  {
    this.owner = owner;
    order = new int[TravelingSalesman.CITY_COUNT];
    minimalorder = new int[TravelingSalesman.CITY_COUNT];
  }

  /**
   * Called to determine if annealing should take place.
   *
   * @param d The distance.
   * @return True if annealing should take place.
   */
  public boolean anneal(double d)
  {
    if (temperature < 1.0E-4) {
      if (d > 0.0)
        return true;
      else
        return false;
    }
    if (Math.random() < Math.exp(d / temperature))
      return true;
    else
      return false;
  }



  /**
   * Used to ensure that the passed in integer is within thr city range.
   *
   * @param i A city index.
   * @return A city index that will be less than CITY_COUNT
   */
  public int mod(int i)
  {
    return i % TravelingSalesman.CITY_COUNT;
  }



  /**
   * Called to get the distance between two cities.
   *
   * @param i The first city
   * @param j The second city
   * @return The distance between the two cities.
   */
  public double distance(int i, int j)
  {
    int c1 = order[i%TravelingSalesman.CITY_COUNT];
    int c2 = order[j%TravelingSalesman.CITY_COUNT];
    return owner.cities[c1].proximity(owner.cities[c2]);
  }



  /**
   * Run as a background thread. This method is called to
   * perform the simulated annealing.
   */
  public void run()
  {
    int cycle=1;
    int sameCount = 0;
    temperature = TravelingSalesman.START_TEMPERATURE;

    initorder(order);
    initorder(minimalorder);

    pathlength = length();
    minimallength = pathlength;


    while (sameCount<50) {
      // update the screen
      owner.paint();
      owner.setStatus("Cycle=" + cycle + ",Length=" + minimallength + ",Temp=" + temperature );

      // make adjustments to city order(annealing)
      for (int j2 = 0; j2 < TravelingSalesman.CITY_COUNT * TravelingSalesman.CITY_COUNT; j2++) {
        int i1 = (int)Math.floor((double)TravelingSalesman.CITY_COUNT * Math.random());
        int j1 = (int)Math.floor((double)TravelingSalesman.CITY_COUNT * Math.random());
        double d = distance(i1, i1 + 1) + distance(j1, j1 + 1) - distance(i1, j1) - distance(i1 + 1, j1 + 1);
        if (anneal(d)) {
          if (j1 < i1) {
            int k1 = i1;
            i1 = j1;
            j1 = k1;
          }
          for (; j1 > i1; j1--) {
            int i2 = order[i1 + 1];
            order[i1 + 1] = order[j1];
            order[j1] = i2;
            i1++;
          }
        }
      }

      // See if this improved anything
      pathlength = length();
      if (pathlength < minimallength) {
        minimallength = pathlength;
        for (int k2 = 0; k2 < TravelingSalesman.CITY_COUNT; k2++)
          minimalorder[k2] = order[k2];
        sameCount=0;
      } else
        sameCount++;
      temperature = TravelingSalesman.TEMPERATURE_DELTA * temperature;
      cycle++;
    }

    // we're done
    owner.setStatus("Solution found after " + cycle + " cycles." );
  }

  /**
   * Return the length of the current path through
   * the cities.
   *
   * @return The length of the current path through the cities.
   */
  public double length()
  {
    double d = 0.0;
    for (int i = 1; i <= TravelingSalesman.CITY_COUNT; i++)
      d += distance(i, i - 1);
    return d;
  }

  /**
   * Set the specified array to have a list of the cities in
   * order.
   *
   * @param an An array to hold the cities.
   */
  public void initorder(int an[])
  {
    for (int i = 0; i < TravelingSalesman.CITY_COUNT; i++)
      an[i] = i;
  }
}