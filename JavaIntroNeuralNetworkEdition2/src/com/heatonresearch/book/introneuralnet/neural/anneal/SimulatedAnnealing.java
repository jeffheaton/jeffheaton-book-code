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
package com.heatonresearch.book.introneuralnet.neural.anneal;

import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;

/**
 * SimulatedAnnealing: Simulated annealing is a common training method.  
 * This class implements a simulated annealing algorithm that can be
 * used both for neural networks, as well as more general cases.  This
 * class is abstract, so a more specialized simulated annealing subclass
 * will need to be created for each intended use.  This book demonstrates
 * how to use the simulated annealing algorithm to train feedforward
 * neural networks, as well as find a solution to the traveling salesman
 * problem.
 * 
 * The name and inspiration come from annealing in metallurgy, a technique 
 * involving heating and controlled cooling of a material to increase the 
 * size of its crystals and reduce their defects. The heat causes the atoms 
 * to become unstuck from their initial positions (a local minimum of the 
 * internal energy) and wander randomly through states of higher energy; 
 * the slow cooling gives them more chances of finding configurations 
 * with lower internal energy than the initial one.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
abstract public class SimulatedAnnealing<UNIT_TYPE> {

	/**
	 * The starting temperature.
	 */
	private double startTemperature;
	/**
	 * The ending temperature.
	 */
	private double stopTemperature;
	/**
	 * The number of cycles that will be used.
	 */
	private int cycles;
	/**
	 * The current error.
	 */
	private double error;

	/**
	 * The current temperature.
	 */
	protected double temperature;

	/**
	 * Subclasses should provide a method that evaluates the error for the
	 * current solution. Those solutions with a lower error are better.
	 * 
	 * @return Return the error, as a percent.
	 * @throws NeuralNetworkError
	 *             Should be thrown if any sort of error occurs.
	 */
	public abstract double determineError() throws NeuralNetworkError;

	/**
	 * Subclasses must provide access to an array that makes up the solution.
	 * 
	 * @return An array that makes up the solution.
	 */
	public abstract UNIT_TYPE[] getArray();

	/**
	 * @return the cycles
	 */
	public int getCycles() {
		return this.cycles;
	}

	/**
	 * @return the globalError
	 */
	public double getError() {
		return this.error;
	}

	/**
	 * @return the startTemperature
	 */
	public double getStartTemperature() {
		return this.startTemperature;
	}

	/**
	 * @return the stopTemperature
	 */
	public double getStopTemperature() {
		return this.stopTemperature;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return this.temperature;
	}

	/**
	 * Called to perform one cycle of the annealing process.
	 */
	public void iteration() throws NeuralNetworkError {
		UNIT_TYPE bestArray[];

		setError(determineError());
		bestArray = this.getArrayCopy();
		
		this.temperature = this.getStartTemperature();

		for (int i = 0; i < this.cycles; i++) {
			double curError;
			randomize();
			curError = determineError();
			if (curError < getError()) {
				bestArray = this.getArrayCopy();
				setError(curError);
			}

			this.putArray(bestArray);
			final double ratio = Math.exp(Math.log(getStopTemperature()
					/ getStartTemperature())
					/ (getCycles() - 1));
			this.temperature *= ratio;
		}
	}

	public abstract UNIT_TYPE[] getArrayCopy();

	public abstract void putArray(UNIT_TYPE[] array);

	public abstract void randomize();

	/**
	 * @param cycles
	 *            the cycles to set
	 */
	public void setCycles(final int cycles) {
		this.cycles = cycles;
	}

	/**
	 * @param globalError
	 *            the globalError to set
	 */
	public void setError(final double error) {
		this.error = error;
	}

	/**
	 * @param startTemperature
	 *            the startTemperature to set
	 */
	public void setStartTemperature(final double startTemperature) {
		this.startTemperature = startTemperature;
	}

	/**
	 * @param stopTemperature
	 *            the stopTemperature to set
	 */
	public void setStopTemperature(final double stopTemperature) {
		this.stopTemperature = stopTemperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(final double temperature) {
		this.temperature = temperature;
	}

}
