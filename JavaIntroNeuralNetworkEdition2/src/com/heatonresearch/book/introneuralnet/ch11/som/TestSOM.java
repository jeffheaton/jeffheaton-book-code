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
package com.heatonresearch.book.introneuralnet.ch11.som;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.heatonresearch.book.introneuralnet.neural.matrix.Matrix;
import com.heatonresearch.book.introneuralnet.neural.som.SelfOrganizingMap;
import com.heatonresearch.book.introneuralnet.neural.som.TrainSelfOrganizingMap;
import com.heatonresearch.book.introneuralnet.neural.som.NormalizeInput.NormalizationType;
import com.heatonresearch.book.introneuralnet.neural.som.TrainSelfOrganizingMap.LearningMethod;

/** 
 * Chapter 11: Using a Self Organizing Map
 * 
 * TestSOM: Very simple example to test the SOM and show how it works.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class TestSOM extends JFrame implements Runnable {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 2772365196327194581L;

	/**
	 * How many input neurons to use.
	 */
	public static final int INPUT_COUNT = 2;

	/**
	 * How many output neurons to use.
	 */
	public static final int OUTPUT_COUNT = 7;

	/**
	 * How many random samples to generate.
	 */
	public static final int SAMPLE_COUNT = 100;

	/**
	 * Startup the program.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(final String args[]) {
		final TestSOM app = new TestSOM();
		app.setVisible(true);
		final Thread t = new Thread(app);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();

	}

	/**
	 * The unit length in pixels, which is the max of the height and width of
	 * the window.
	 */
	protected int unitLength;

	/**
	 * How many retries so far.
	 */
	protected int retry = 1;

	/**
	 * The current error percent.
	 */
	protected double totalError = 0;
	/**
	 * The best error percent.
	 */
	protected double bestError = 0;

	/**
	 * The neural network.
	 */

	protected SelfOrganizingMap net;
	protected double input[][];

	/**
	 * The offscreen image. Used to prevent flicker.
	 */
	protected Image offScreen;

	/**
	 * The constructor sets up the position and size of the window.
	 */
	TestSOM() {
		setTitle("Training a Self Organizing Map Neural Network");
		setSize(400, 450);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension d = toolkit.getScreenSize();
		setLocation((int) (d.width - this.getSize().getWidth()) / 2,
				(int) (d.height - this.getSize().getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}

	/**
	 * Display the progress of the neural network.
	 * 
	 * @param g
	 *            A graphics object.
	 */
	@Override
	public void paint(Graphics g) {
		if (this.net == null) {
			return;
		}
		if (this.offScreen == null) {
			this.offScreen = this.createImage((int) getBounds().getWidth(),
					(int) getBounds().getHeight());
		}
		g = this.offScreen.getGraphics();
		final int width = getContentPane().getWidth();
		final int height = getContentPane().getHeight();
		this.unitLength = Math.min(width, height);
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);

		// plot the weights of the output neurons
		g.setColor(Color.white);
		final Matrix outputWeights = this.net.getOutputWeights();

		for (int y = 0; y < outputWeights.getRows(); y++) {

			g.fillRect((int) (outputWeights.get(y, 0) * this.unitLength),
					(int) (outputWeights.get(y, 1) * this.unitLength), 10, 10);

		}

		// plot a grid of samples to test the net with
		g.setColor(Color.green);
		for (int y = 0; y < this.unitLength; y += 50) {
			for (int x = 0; x < this.unitLength; x += 50) {
				g.fillOval(x, y, 5, 5);
				final double d[] = new double[2];
				d[0] = x;
				d[1] = y;

				final int c = this.net.winner(d);

				final int x2 = (int) (outputWeights.get(c, 0) * this.unitLength);
				final int y2 = (int) (outputWeights.get(c, 1) * this.unitLength);

				g.drawLine(x, y, x2, y2);
			}

		}

		// display the status info
		g.setColor(Color.white);
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		g.drawString("retry = " + this.retry + ",current error = "
				+ nf.format(this.totalError * 100) + "%, best error = "
				+ nf.format(this.bestError * 100) + "%", 0,
				(int) getContentPane().getBounds().getHeight());
		getContentPane().getGraphics().drawImage(this.offScreen, 0, 0, this);

	}

	/**
	 * Called to run the background thread. The background thread sets up the
	 * neural network and training data and begins training the network.
	 */
	public void run() {
		// build the training set
		this.input = new double[SAMPLE_COUNT][INPUT_COUNT];

		for (int i = 0; i < SAMPLE_COUNT; i++) {
			for (int j = 0; j < INPUT_COUNT; j++) {
				this.input[i][j] = Math.random();
			}
		}

		// build and train the neural network
		this.net = new SelfOrganizingMap(INPUT_COUNT, OUTPUT_COUNT,
				NormalizationType.MULTIPLICATIVE);
		final TrainSelfOrganizingMap train = new TrainSelfOrganizingMap(
				this.net, this.input,LearningMethod.SUBTRACTIVE,0.5);
		train.initialize();
		double lastError = Double.MAX_VALUE;
		int errorCount = 0;

		while (errorCount < 10) {
			train.iteration();
			this.retry++;
			this.totalError = train.getTotalError();
			this.bestError = train.getBestError();
			paint(getGraphics());

			if (this.bestError < lastError) {
				lastError = this.bestError;
				errorCount = 0;
			} else {
				errorCount++;
			}
		}
	}

}