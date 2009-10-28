/**
 * Chromosome
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
 * Programming Neural Networks in Java
 * http://www.heatonresearch.com/articles/series/1/
 *
 * This class implements a single chromosome. This
 * will generally be the weight matrix to a neural network.
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

class Chromosome {

  /**
   * The list of cities, which are the genes of this
   * chromosome.
   */
  protected int [] cityList;

  /**
   * The cost of following the cityList order of this
   * chromosome.
   */
  protected double cost;

  /**
   * The mutation percent.
   */
  protected double mutationPercent;

  /**
   * How much genetic material to take.
   */
  protected int cutLength;

  /**
   * The constructor, takes a list of cities to
   * set the initial "genes" to.
   *
   * @param cities The order that this chromosome would
   * visit the
   * cities. These cities can be thought of as the
   * genes of this chromosome.
   */
  Chromosome(City [] cities) {
    boolean taken[] = new boolean[cities.length];
    cityList = new int[cities.length];
    cost = 0.0;
    for ( int i=0;i<cityList.length;i++ ) taken[i] = false;
    for ( int i=0;i<cityList.length-1;i++ ) {
      int icandidate;
      do {
        icandidate = (int) ( 0.999999* Math.random() *
                             (double) cityList.length );
      } while ( taken[icandidate] );
      cityList[i] = icandidate;
      taken[icandidate] = true;
      if ( i == cityList.length-2 ) {
        icandidate = 0;
        while ( taken[icandidate] ) icandidate++;
        cityList[i+1] = icandidate;
      }
    }
    calculateCost(cities);
    cutLength = 1;
  }

  /**
   * Calculate the cost of of the specified list of
   * cities.
   *
   * @param cities A list of cities.
   */
  void calculateCost(City [] cities) {
    cost=0;
    for ( int i=0;i<cityList.length-1;i++ ) {
      double dist = cities[cityList[i]].proximity(cities[cityList[i+1]]);
      cost += dist;
    }
  }


  /**
   * Get the cost for this chromosome. This is the
   * amount of distance that must be traveled
   * for this chromosome's plan for moving through
   * the cities. The cost of a chromosome determines
   * its rank in terms of being able to mate and not
   * being killed off.
   */
  double getCost() {
    return cost;
  }

  /**
   * Get the ith city in this chromosome. For example the
   * value 3 would return the fourth city this chromosome
   * would visit. Similarly 0 would return the first city
   * that this chromosome would visit.
   *
   * @param i The city you want.
   * @return The ith city.
   */
  int getCity(int i) {
    return cityList[i];
  }

  /**
   * Set the order of cities that this chromosome
   * would visit.
   *
   * @param list A list of cities.
   */
  void setCities(int [] list) {
    for ( int i=0;i<cityList.length;i++ ) {
      cityList[i] = list[i];
    }
  }

  /**
   * Set the index'th city in the city list.
   *
   * @param index The city index to change
   * @param value The city number to place into the index.
   */
  void setCity(int index, int value) {
    cityList[index] = value;
  }

  /**
   * Set the cut. The cut determines how much
   * genetic material to take from each "partner"
   * when mating occurs.
   *
   * @param cut The new cut value.
   */
  void setCut(int cut) {
    cutLength = cut;
  }

  /**
   * Set the mutation percent. This is what percentage
   * of birth's will be mutated.
   *
   * @param prob The probability that a mutation will occur.
   */
  void setMutation(double prob) {
    mutationPercent = prob;
  }
  /**
   * Assuming this chromosome is the "mother" mate with
   * the passed in "father".
   *
   * @param father The father.
   * @param offspring1 Returns the first offspring
   * @param offspring2 Returns the second offspring.
   * @return The amount of mutation that was applied.
   */


  int mate(Chromosome father, Chromosome offspring1, Chromosome offspring2) {
    int cutpoint1 = (int) (0.999999*Math.random()*(double)(cityList.length-cutLength));
    int cutpoint2 = cutpoint1 + cutLength;

    boolean taken1 [] = new boolean[cityList.length];
    boolean taken2 [] = new boolean[cityList.length];
    int off1 [] = new int[cityList.length];
    int off2 [] = new int[cityList.length];

    for ( int i=0;i<cityList.length;i++ ) {
      taken1[i] = false;
      taken2[i] = false;
    }

    for ( int i=0;i<cityList.length;i++ ) {
      if ( i<cutpoint1 || i>= cutpoint2 ) {
        off1[i] = -1;
        off2[i] = -1;
      } else {
        int imother = cityList[i];
        int ifather = father.getCity(i);
        off1[i] = ifather;
        off2[i] = imother;
        taken1[ifather] = true;
        taken2[imother] = true;
      }
    }

    for ( int i=0;i<cutpoint1;i++ ) {
      if ( off1[i] == -1 ) {
        for ( int j=0;j<cityList.length;j++ ) {
          int imother = cityList[j];
          if ( !taken1[imother] ) {
            off1[i] = imother;
            taken1[imother] = true;
            break;
          }
        }
      }
      if ( off2[i] == -1 ) {
        for ( int j=0;j<cityList.length;j++ ) {
          int ifather = father.getCity(j);
          if ( !taken2[ifather] ) {
            off2[i] = ifather;
            taken2[ifather] = true;
            break;
          }
        }
      }
    }
    for ( int i=cityList.length-1;i>=cutpoint2;i-- ) {
      if ( off1[i] == -1 ) {
        for ( int j=cityList.length-1;j>=0;j-- ) {
          int imother = cityList[j];
          if ( !taken1[imother] ) {
            off1[i] = imother;
            taken1[imother] = true;
            break;
          }
        }
      }
      if ( off2[i] == -1 ) {
        for ( int j=cityList.length-1;j>=0;j-- ) {
          int ifather = father.getCity(j);
          if ( !taken2[ifather] ) {
            off2[i] = ifather;
            taken2[ifather] = true;
            break;
          }
        }
      }
    }

    offspring1.setCities(off1);
    offspring2.setCities(off2);

    int mutate = 0;
    if ( Math.random() < mutationPercent ) {
      int iswap1 = (int) (0.999999*Math.random()*(double)(cityList.length));
      int iswap2 = (int) (0.999999*Math.random()*(double)cityList.length);
      int i = off1[iswap1];
      off1[iswap1] = off1[iswap2];
      off1[iswap2] = i;
      mutate++;
    }
    if ( Math.random() < mutationPercent ) {
      int iswap1 = (int) (0.999999*Math.random()*(double)(cityList.length));
      int iswap2 = (int) (0.999999*Math.random()*(double)cityList.length);
      int i = off2[iswap1];
      off2[iswap1] = off2[iswap2];
      off2[iswap2] = i;
      mutate++;
    }
    return mutate;
  }

  /**
   * Sort the chromosomes by their cost.
   *
   * @param chromosomes An array of chromosomes to sort.
   * @param num How much of the chromosome list to sort.
   */
  public static void sortChromosomes(Chromosome chromosomes[],int num) {
    Chromosome ctemp;
    boolean swapped = true;
    while ( swapped ) {
      swapped = false;
      for ( int i=0;i<num-1;i++ ) {
        if ( chromosomes[i].getCost() > chromosomes[i+1].getCost() ) {
          ctemp = chromosomes[i];
          chromosomes[i] = chromosomes[i+1];
          chromosomes[i+1] = ctemp;
          swapped = true;
        }
      }
    }
  }

}
