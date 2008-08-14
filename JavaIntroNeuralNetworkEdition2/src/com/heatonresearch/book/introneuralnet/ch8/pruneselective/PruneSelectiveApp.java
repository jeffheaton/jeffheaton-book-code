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
package com.heatonresearch.book.introneuralnet.ch8.pruneselective;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation.Backpropagation;
import com.heatonresearch.book.introneuralnet.neural.prune.Prune;

/**
 * Chapter 8: Pruning a Neural Network
 * 
 * PruneSelectiveApp: Prune a simple XOR map using the selective method.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class PruneSelectiveApp extends JFrame implements ActionListener,
		Runnable {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 1006747373773049367L;

	/**
	 * The number of input neurons.
	 */
	protected final static int NUM_INPUT = 2;

	/**
	 * The number of output neurons.
	 */
	protected final static int NUM_OUTPUT = 1;

	/**
	 * The number of hidden neurons.
	 */
	protected final static int NUM_HIDDEN = 10;

	/**
	 * The learning rate.
	 */
	protected final static double RATE = 0.5;

	/**
	 * The learning momentum.
	 */
	protected final static double MOMENTUM = 0.7;

	/**
	 * The main function, just display the JFrame.
	 * 
	 * @param args
	 *            No arguments are used.
	 */
	public static void main(final String args[]) {
		(new PruneSelectiveApp()).setVisible(true);
	}

	/**
	 * The train button.
	 */
	JButton btnTrain;

	/**
	 * The prune button.
	 */
	JButton btnPrune;

	/**
	 * The run button.
	 */
	JButton btnRun;

	/**
	 * The quit button.
	 */
	JButton btnQuit;

	/**
	 * The status line.
	 */
	JLabel status;

	/**
	 * The background worker thread.
	 */
	protected Thread worker = null;

	/**
	 * The training data that the user enters. This represents the inputs and
	 * expected outputs for the XOR problem.
	 */
	protected JTextField data[][] = new JTextField[4][4];

	/**
	 * The neural network.
	 */
	protected FeedforwardNetwork network;

	/**
	 * Constructor. Setup the components.
	 */
	public PruneSelectiveApp() {
		setTitle("XOR Solution - Select Prune");
		this.network = new FeedforwardNetwork();
		this.network.addLayer(new FeedforwardLayer(NUM_INPUT));
		this.network.addLayer(new FeedforwardLayer(NUM_HIDDEN));
		this.network.addLayer(new FeedforwardLayer(NUM_OUTPUT));
		this.network.reset();

		final Container content = getContentPane();

		final GridBagLayout gridbag = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		content.setLayout(gridbag);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;

		// Training input label
		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.anchor = GridBagConstraints.NORTHWEST;
		content.add(new JLabel("Enter training data:"), c);

		final JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(5, 4));
		grid.add(new JLabel("IN1"));
		grid.add(new JLabel("IN2"));
		grid.add(new JLabel("Expected OUT   "));
		grid.add(new JLabel("Actual OUT"));

		for (int i = 0; i < 4; i++) {
			final int x = (i & 1);
			final int y = (i & 2) >> 1;
			grid.add(this.data[i][0] = new JTextField("" + y));
			grid.add(this.data[i][1] = new JTextField("" + x));
			grid.add(this.data[i][2] = new JTextField("" + (x ^ y)));
			grid.add(this.data[i][3] = new JTextField("??"));
			this.data[i][0].setEditable(false);
			this.data[i][1].setEditable(false);
			this.data[i][3].setEditable(false);
		}

		content.add(grid, c);

		// the button panel
		final JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(this.btnTrain = new JButton("Train"));
		buttonPanel.add(this.btnPrune = new JButton("Prune"));
		buttonPanel.add(this.btnRun = new JButton("Run"));
		buttonPanel.add(this.btnQuit = new JButton("Quit"));
		this.btnTrain.addActionListener(this);
		this.btnPrune.addActionListener(this);
		this.btnRun.addActionListener(this);
		this.btnQuit.addActionListener(this);

		// Add the button panel
		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.anchor = GridBagConstraints.CENTER;
		content.add(buttonPanel, c);

		// Training input label
		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.anchor = GridBagConstraints.NORTHWEST;
		content
				.add(this.status = new JLabel(
						"Click train to begin training..."), c);

		// adjust size and position
		pack();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension d = toolkit.getScreenSize();
		setLocation((int) (d.width - this.getSize().getWidth()) / 2,
				(int) (d.height - this.getSize().getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		this.btnRun.setEnabled(false);
		this.btnPrune.setEnabled(false);
	}

	/**
	 * Called when the user clicks one of the three buttons.
	 * 
	 * @param e
	 *            The event.
	 */
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.btnQuit) {
			System.exit(0);
		} else if (e.getSource() == this.btnTrain) {
			train();
		} else if (e.getSource() == this.btnRun) {
			evaluate();
		} else if (e.getSource() == this.btnPrune) {
			prune();
		}

	}

	/**
	 * Called when the user clicks the run button.
	 */
	protected void evaluate() {
		final double xorData[][] = getGrid();

		for (int i = 0; i < 4; i++) {
			final NumberFormat nf = NumberFormat.getInstance();
			final double d[] = this.network.computeOutputs(xorData[i]);
			this.data[i][3].setText(nf.format(d[0]));
		}

	}

	/**
	 * Called to generate an array of doubles based on the training data that
	 * the user has entered.
	 * 
	 * @return An array of doubles
	 */
	double[][] getGrid() {
		final double array[][] = new double[4][2];

		for (int i = 0; i < 4; i++) {
			array[i][0] = Float.parseFloat(this.data[i][0].getText());
			array[i][1] = Float.parseFloat(this.data[i][1].getText());
		}

		return array;
	}

	/**
	 * Called to the ideal values that the neural network should return for each
	 * of the grid training values.
	 * 
	 * @return The ideal results.
	 */
	double[][] getIdeal() {
		final double array[][] = new double[4][1];

		for (int i = 0; i < 4; i++) {
			array[i][0] = Float.parseFloat(this.data[i][2].getText());
		}

		return array;
	}

	/**
	 * Called when the user clicks the prune button.
	 */
	public void prune() {
		final double xorData[][] = getGrid();
		final double xorIdeal[][] = getIdeal();

		final Prune prune = new Prune(this.network, xorData, xorIdeal,0.05);
		final int count = prune.pruneSelective();
		this.network = prune.getCurrentNetwork();
		this.status.setText("Prune removed " + count + " neurons.");
		this.btnTrain.setEnabled(false);
	}

	/**
	 * The thread worker, used for training
	 */
	public void run() {
		final double xorData[][] = getGrid();
		final double xorIdeal[][] = getIdeal();
		int update = 0;

		final Train train = new Backpropagation(this.network, xorData,
				xorIdeal, 0.7, 0.9);

		final int max = 10000;
		for (int i = 0; i < max; i++) {
			train.iteration();

			update++;
			if (update == 100) {
				this.status.setText("Cycles Left:" + (max - i) + ",Error:"
						+ train.getError());
				update = 0;
			}
		}
		this.btnRun.setEnabled(true);
		this.btnPrune.setEnabled(true);
	}

	/**
	 * Called when the user clicks the train button.
	 */
	protected void train() {
		if (this.worker != null) {
			this.worker = null;
		}
		this.worker = new Thread(this);
		this.worker.setPriority(Thread.MIN_PRIORITY);
		this.worker.start();
	}

}