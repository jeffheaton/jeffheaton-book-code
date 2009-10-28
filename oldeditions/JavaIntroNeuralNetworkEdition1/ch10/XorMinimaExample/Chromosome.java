/**
 * Chromosome
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
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
 * The size of a gene.
 */
  protected static final int GENE_SIZE = 64;

  /**
   * The genes of this chromosome.
   */
  protected int [] gene;

  /**
   * The cost of following the gene order of this
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
   * The weight matrix associated with this chromosome.
   */
  double matrix[];

  /**
   * The constructor, takes a list of cities to
   * set the initial "genes" to.
   *
   * @param cities The order that this chromosome would
   * visit the
   * cities. These cities can be thought of as the
   * genes of this chromosome.
   */
  Chromosome(Network network) {
    matrix = network.toArray();
    gene = new int[matrix.length*GENE_SIZE];
    cost = 0.0;
    fromMatrix();
    cutLength = gene.length/2;
  }

  /**
   * Copy the genes to a weight matrix.
   */
  public void toMatrix()
  {
    int idx = 0;

    for (int i=0;i<matrix.length;i++) {
      long l = 0;
      long or = 1;
      for (int j=0;j<GENE_SIZE;j++) {
        if ( gene[idx++]!=0 )
          l = l | or;
        or+=or;
      }
      matrix[i] = Double.longBitsToDouble(l);
    }

  }

  /**
   * Copy the weight matrix to the genes.
   */
  public void fromMatrix()
  {
    int idx = 0;
    for (int i=0;i<matrix.length;i++) {
      long l = Double.doubleToLongBits(matrix[i]);
      long and = 1;
      for (int j=0;j<GENE_SIZE;j++) {
        gene[idx++] = (l & and)!=0?1:0;
        and+=and;
      }
    }
  }

  /**
   * Calculate the cost of this solution. This is
   * the error of this weight matrix against the
   * sample data.
   *
   * @param network The neural network to use to calculate the cost.
   * @param trial
   * @param ideal
   */
  public void calculateCost(Network network,double trial[][],double ideal[][])
  {
    toMatrix();
    network.fromArray(matrix);
    for (int i=0;i<trial.length;i++) {
      network.computeOutputs(trial[i]);
      network.calcError(ideal[i]);
    }
    cost = network.getError(trial.length);
  }

  /**
   * Get the cost for this chromosome. Note that
   * the cost must be calculated first.
   *
   * @return The cost of this chromosome's solution.
   */
  double getCost() {
    return cost;
  }

  /**
   * Get the ith gene in this chromosome.
   *
   * @param i The city you want.
   * @return The ith city.
   */
  int getGene(int i) {
    return gene[i];
  }

  /**
   * Set all genes.
   *
   * @param list A list of genes.
   */
  void setGenes(int [] list) {
    for ( int i=0;i<gene.length;i++ ) {
      gene[i] = list[i];
    }
  }

  /**
   * Set the cut value.
   *
   * @param cut The new cut value.
   */
  void setCut(int cut) {
    cutLength = cut;
  }

  /**
   * Set the percent of new births that will be mutated.
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
    int cutpoint = (int) (0.999999*Math.random()*(double)(gene.length-cutLength));

    int off1 [] = new int[gene.length];
    int off2 [] = new int[gene.length];
// mate
for(int i=0;i<gene.length;i++)
{
    if(Math.random()>0.5)
    {
        off1[i] = this.getGene(i);
        off2[i] = father.getGene(i);
    }
    else
    {
        off2[i] = this.getGene(i);
        off1[i] = father.getGene(i);
    }
}

/*    int n1 = gene.length/2;
    int n2 = gene.length-n1;
    int c = cutpoint;

    while ((n1--)>0) {
      c = (c+1)%gene.length;
      off1[c] = this.getGene(c);
      off2[c] = father.getGene(c);
    }

    while ((n2--)>0) {
      c = (c+1)%gene.length;
      off2[c] = this.getGene(c);
      off1[c] = father.getGene(c);
    }*/

    int mutate = 0;
    if ( Math.random() < mutationPercent ) {
      int iswap1 = (int) (0.999999*Math.random()*(double)(gene.length));
      int iswap2 = (int) (0.999999*Math.random()*(double)gene.length);
      int i = off1[iswap1];
      off1[iswap1] = off1[iswap2];
      off1[iswap2] = i;
      mutate++;
    }
    if ( Math.random() < mutationPercent ) {
      int iswap1 = (int) (0.999999*Math.random()*(double)(gene.length));
      int iswap2 = (int) (0.999999*Math.random()*(double)gene.length);
      int i = off2[iswap1];
      off2[iswap1] = off2[iswap2];
      off2[iswap2] = i;
      mutate++;
    }

    // copy results
    offspring1.setGenes(off1);
    offspring1.toMatrix();
    offspring2.setGenes(off2);
    offspring2.toMatrix();

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


  /**
   * Get the weight matrix associated with this chromosome.
   *
   * @return The weight matrix associated with this chromosome.
   */
  public double[] getMatrix()
  {
    return matrix;
  }

}
