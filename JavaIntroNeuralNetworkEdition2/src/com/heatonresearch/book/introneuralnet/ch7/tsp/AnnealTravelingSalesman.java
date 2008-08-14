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
package com.heatonresearch.book.introneuralnet.ch7.tsp;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Chapter 7:Training using Simulated Annealing
 * 
 * AnnealTravelingSalesman: Try to solve the traveling salesman problem
 * using simulated annealing.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class AnnealTravelingSalesman extends JFrame implements Runnable,
		ComponentListener {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -8345055872273623611L;

	/**
	 * How many cities to use.
	 */
	public static final int CITY_COUNT = 50;

	/**
	 * Starting temperature for simulated annealing
	 */
	public static final double START_TEMPERATURE = 10;

	/**
	 * The temperature delta for simulated annealing
	 */
	public static final double STOP_TEMPERATURE = 2;

	/**
	 * Cycles per epoc
	 */
	public static final int CYCLES = 10;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(final String args[]) {
		(new AnnealTravelingSalesman()).setVisible(true);
	}

	/**
	 * A Map object that will display the city map.
	 */
	protected WorldMap map = null;

	/**
	 * The current status. Used to display the current status to the user.
	 */
	protected JLabel status;

	/**
	 * The current generation, or epoc.
	 */
	protected int epoc;

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
	protected City[] cities;

	protected TSPSimulatedAnnealing anneal;

	/**
	 * The constructor
	 */
	public AnnealTravelingSalesman() {
		addComponentListener(this);
		setSize(300, 300);
		setTitle("Traveling Salesman Problem");
	}

	/**
	 * Not used, but required by Java.
	 * 
	 * @param e
	 *            The event.
	 */
	public void componentHidden(final ComponentEvent e) {
	}

	/**
	 * Not used, but required by Java.
	 * 
	 * @param e
	 *            The event.
	 */
	public void componentMoved(final ComponentEvent e) {
	}

	/**
	 * Not used, but required by Java.
	 * 
	 * @param e
	 *            The event.
	 */
	public void componentResized(final ComponentEvent e) {
	}

	/**
	 * Used to add necessary components.
	 * 
	 * @param e
	 *            The event.
	 */
	public void componentShown(final ComponentEvent e) {
		getContentPane().setLayout(new BorderLayout());

		if (this.map == null) {
			this.map = new WorldMap(this);
			getContentPane().add(this.map, "Center");
			this.status = new JLabel("Starting up");
			getContentPane().add(this.status, "South");
		}

		// place the cities at random locations
		final int height = getBounds().height - 50;
		final int width = getBounds().width - 10;
		this.cities = new City[AnnealTravelingSalesman.CITY_COUNT];
		for (int i = 0; i < AnnealTravelingSalesman.CITY_COUNT; i++) {
			this.cities[i] = new City((int) (Math.random() * width),
					(int) (Math.random() * height));
		}

		this.anneal = new TSPSimulatedAnnealing(this.cities, START_TEMPERATURE,
				STOP_TEMPERATURE, CYCLES);

		final boolean taken[] = new boolean[this.cities.length];
		final Integer path[] = new Integer[this.cities.length];

		for (int i = 0; i < path.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < path.length - 1; i++) {
			int icandidate;
			do {
				icandidate = (int) (Math.random() * path.length);
			} while (taken[icandidate]);
			path[i] = icandidate;
			taken[icandidate] = true;
			if (i == path.length - 2) {
				icandidate = 0;
				while (taken[icandidate]) {
					icandidate++;
				}
				path[i + 1] = icandidate;
			}
		}

		this.anneal.putArray(path);

		start();

	}

	public TSPSimulatedAnnealing getAnneal() {
		return this.anneal;
	}

	/**
	 * The main loop for the background thread. It is here that most of the work
	 * os orchestrated.
	 */
	public void run() {

		double thisCost = 500.0;
		double oldCost = 0.0;
		int countSame = 0;

		this.map.update(this.map.getGraphics());

		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		while (countSame < 10) {

			this.epoc++;

			this.status.setText("Epoc " + this.epoc);

			this.anneal.iteration();
			thisCost = this.anneal.getError();
System.out.println(thisCost);
			if ((int) thisCost == (int) oldCost) {
				countSame++;
			} else {
				countSame = 0;
				oldCost = thisCost;
			}
			this.map.update(this.map.getGraphics());

		}
		this.status.setText("Solution found after " + this.epoc + " epocs.");
	}

	/**
	 * Start the background thread.
	 */
	public void start() {

		// create the initial chromosomes

		// start up the background thread
		this.started = true;
		this.map.update(this.map.getGraphics());

		this.epoc = 0;

		if (this.worker != null) {
			this.worker = null;
		}
		this.worker = new Thread(this);
		// worker.setPriority(Thread.MIN_PRIORITY);
		this.worker.start();
	}

}
