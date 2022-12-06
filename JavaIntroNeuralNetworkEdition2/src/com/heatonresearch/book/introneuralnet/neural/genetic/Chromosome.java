/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc. 
 * http://www.heatonresearch.com/books/java-neural-2/
 * 
 * ISBN13: 978-1-60439-008-7  	 
 * ISBN:   1-60439-008-5
 *   
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */
package com.heatonresearch.book.introneuralnet.neural.genetic;

import java.util.HashSet;
import java.util.Set;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;

/**
 * Chromosome: Implements a chromosome to genetic algorithm.
 * This is an abstract class.  Other classes are provided in this
 * book that use this base class to train neural networks or
 * provide an answer to the traveling salesman problem.
 * 
 * Lifeforms in this genetic algorithm consist of one single 
 * chromosome each.  Therefore, this class represents a virtual
 * lifeform.  The chromosome is a string of objects that represent
 * one solution.  For a neural network, this string of objects
 * usually represents the weight and threshold matrix.
 * 
 * Chromosomes are made up of genes.  These are of the generic type
 * GENE_TYPE.  For a neural network this type would most likely
 * be double values.  
 * 
 * @author Jeff Heaton
 * @version 2.1
 */

abstract public class Chromosome<GENE_TYPE, GA_TYPE extends GeneticAlgorithm<?>>
		implements Comparable<Chromosome<GENE_TYPE, GA_TYPE>> {

	/**
	 * The cost for this chromosome.  The lower the better.
	 */
	private double cost;

	/**
	 * The individual elements of this chromosome.
	 */
	private GENE_TYPE[] genes;

	/**
	 * The genetic algorithm that this chromosome is associated with.
	 */
	private GA_TYPE geneticAlgorithm;

	/**
	 * Called to calculate the cost for this chromosome.
	 * @throws NeuralNetworkError
	 */
	abstract public void calculateCost() throws NeuralNetworkError;

	/**
	 * Used to compare two chromosomes. Used to sort by cost.
	 * 
	 * @param other
	 *            The other chromosome to compare.
	 * @return The value 0 if the argument is a chromosome that has an equal
	 *         cost to this chromosome; a value less than 0 if the argument is a
	 *         chromosome with a cost greater than this chromosome; and a value
	 *         greater than 0 if the argument is a chromosome what a cost less
	 *         than this chromosome.
	 */
	public int compareTo(final Chromosome<GENE_TYPE, GA_TYPE> other) {
		if (getCost() > other.getCost()) {
			return 1;
		} else if ( getCost() == other.getCost() ){
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return this.cost;
	}

	/**
	 * Get the specified gene.
	 * @param gene The specified gene.
	 * @return The gene specified.
	 */
	public GENE_TYPE getGene(final int gene) {
		return this.genes[gene];
	}

	/**
	 * Used the get the entire gene array.
	 * @return the genes
	 */
	public GENE_TYPE[] getGenes() {
		return this.genes;
	}

	/**
	 * @return the geneticAlgorithm
	 */
	public GA_TYPE getGeneticAlgorithm() {
		return this.geneticAlgorithm;
	}

	/**
	 * Get a list of the genes that have not been taken before. This is useful
	 * if you do not wish the same gene to appear more than once in a
	 * chromosome.
	 * 
	 * @param source
	 *            The pool of genes to select from.
	 * @param taken
	 *            An array of the taken genes.
	 * @return Those genes in source that are not taken.
	 */
	private GENE_TYPE getNotTaken(final Chromosome<GENE_TYPE, GA_TYPE> source,
			final Set<GENE_TYPE> taken) {
		final int geneLength = source.size();

		for (int i = 0; i < geneLength; i++) {
			final GENE_TYPE trial = source.getGene(i);
			if (!taken.contains(trial)) {
				taken.add(trial);
				return trial;
			}
		}

		return null;
	}

	/**
	 * Assuming this chromosome is the "mother" mate with the passed in
	 * "father".
	 * 
	 * @param father
	 *            The father.
	 * @param offspring1
	 *            Returns the first offspring
	 * @param offspring2
	 *            Returns the second offspring.
	 * @return The amount of mutation that was applied.
	 * @throws NeuralNetworkException
	 */

	public void mate(final Chromosome<GENE_TYPE, GA_TYPE> father,
			final Chromosome<GENE_TYPE, GA_TYPE> offspring1,
			final Chromosome<GENE_TYPE, GA_TYPE> offspring2)
			throws NeuralNetworkError {
		final int geneLength = getGenes().length;

		// the chromosome must be cut at two positions, determine them
		final int cutpoint1 = (int) (Math.random() * (geneLength - getGeneticAlgorithm()
				.getCutLength()));
		final int cutpoint2 = cutpoint1 + getGeneticAlgorithm().getCutLength();

		// keep track of which cities have been taken in each of the two
		// offspring, defaults to false.
		final Set<GENE_TYPE> taken1 = new HashSet<GENE_TYPE>();
		final Set<GENE_TYPE> taken2 = new HashSet<GENE_TYPE>();

		// handle cut section
		for (int i = 0; i < geneLength; i++) {
			if ((i < cutpoint1) || (i > cutpoint2)) {
			} else {
				offspring1.setGene(i, father.getGene(i));
				offspring2.setGene(i, this.getGene(i));
				taken1.add(offspring1.getGene(i));
				taken2.add(offspring2.getGene(i));
			}
		}

		// handle outer sections
		for (int i = 0; i < geneLength; i++) {
			if ((i < cutpoint1) || (i > cutpoint2)) {
				if (getGeneticAlgorithm().isPreventRepeat()) {
					offspring1.setGene(i, getNotTaken(this, taken1));
					offspring2.setGene(i, getNotTaken(father, taken2));
				} else {
					offspring1.setGene(i, this.getGene(i));
					offspring2.setGene(i, father.getGene(i));
				}
			}
		}

		// mutate
		if (Math.random() < this.geneticAlgorithm.getMutationPercent()) {
			offspring1.mutate();
		}
		if (Math.random() < this.geneticAlgorithm.getMutationPercent()) {
			offspring2.mutate();
		}
		
		// calculate cost
		offspring1.calculateCost();
		offspring2.calculateCost();

	}

	/**
	 * Called to mutate this chromosome.
	 */
	abstract public void mutate();

	/**
	 * Set the cost for this chromosome.
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(final double cost) {
		this.cost = cost;
	}

	/**
	 * Set the specified gene's value.
	 * @param gene The specified gene.
	 * @param value The value to set the specified gene to.
	 */
	public void setGene(final int gene, final GENE_TYPE value) {
		this.genes[gene] = value;
	}

	/**
	 * Set the entire gene array.
	 * @param genes
	 *            the genes to set
	 */
	public void setGenes(final GENE_TYPE[] genes) throws NeuralNetworkError {
		this.genes = genes;
	}

	/**
	 * Set the genes directly, not allowed to be overridden.
	 * @param genes
	 *            the genes to set
	 */
	public final void setGenesDirect(final GENE_TYPE[] genes)
			throws NeuralNetworkError {
		this.genes = genes;
	}

	/**
	 * Set the genetic algorithm.
	 * @param geneticAlgorithm
	 *            the geneticAlgorithm to set
	 */
	public void setGeneticAlgorithm(final GA_TYPE geneticAlgorithm) {
		this.geneticAlgorithm = geneticAlgorithm;
	}

	/**
	 * Get the size of the gene array.
	 * @return The size of the gene array.
	 */
	private int size() {
		return this.genes.length;
	}

	/**
	 * Convert the chromosome to a string.
	 * @return The chromosome as a string.
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[Chromosome: cost=");
		builder.append(getCost());
		return builder.toString();
	}

}
