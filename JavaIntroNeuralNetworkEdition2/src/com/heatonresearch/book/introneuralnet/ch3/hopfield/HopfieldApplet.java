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
package com.heatonresearch.book.introneuralnet.ch3.hopfield;

import java.applet.Applet;
import java.awt.Color;
import java.awt.event.FocusEvent;

import com.heatonresearch.book.introneuralnet.neural.hopfield.HopfieldNetwork;

/**
 * Chapter 3: Using a Hopfield Neural Network
 * 
 * HopfieldApplet: Applet that allows you to work with a Hopfield
 * network.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class HopfieldApplet extends Applet implements
		java.awt.event.ActionListener, java.awt.event.FocusListener {
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 6244453822526901486L;
	HopfieldNetwork network = new HopfieldNetwork(4);

	java.awt.Label label1 = new java.awt.Label();
	java.awt.TextField matrix[][] = new java.awt.TextField[4][4];

	java.awt.Choice input[] = new java.awt.Choice[4];
	java.awt.Label label2 = new java.awt.Label();
	java.awt.TextField output[] = new java.awt.TextField[4];
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Button go = new java.awt.Button();
	java.awt.Button train = new java.awt.Button();
	java.awt.Button clear = new java.awt.Button();

	/**
	 * Called when the user clicks one of the buttons.
	 * 
	 * @param event
	 *            The event.
	 */
	public void actionPerformed(final java.awt.event.ActionEvent event) {
		final Object object = event.getSource();
		if (object == this.go) {
			runNetwork();
		} else if (object == this.clear) {
			clear();
		} else if (object == this.train) {
			train();
		}
	}

	/**
	 * Clear the neural network.
	 */
	void clear() {
		this.network.getMatrix().clear();
		this.setMatrixValues();
	}

	/**
	 * Collect the matrix values from the applet and place inside the weight
	 * matrix for the neural network.
	 */
	private void collectMatrixValues() {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				final String str = this.matrix[row][col].getText();
				int value = 0;

				try {
					value = Integer.parseInt(str);
				} catch (final NumberFormatException e) {
					// let the value default to zero,
					// which it already is by this point.
				}

				// do not allow neurons to self-connect
				if (row == col) {
					this.network.getMatrix().set(row, col, 0);
				} else {
					this.network.getMatrix().set(row, col, value);
				}

			}
		}
	}

	public void focusGained(final FocusEvent e) {
		// don't care

	}

	public void focusLost(final FocusEvent e) {
		this.collectMatrixValues();
		this.setMatrixValues();
	}

	/**
	 * Setup the applet.
	 */
	@Override
	public void init() {
		setLayout(null);
		setBackground(Color.lightGray);
		this.label1.setText("Enter the activation weight matrix:");
		add(this.label1);
		this.label1.setBounds(24, 12, 192, 12);
		this.label2.setText("Input pattern to run or train:");
		add(this.label2);
		this.label2.setBounds(24, 180, 192, 12);

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				this.matrix[row][col] = new java.awt.TextField();
				add(this.matrix[row][col]);
				this.matrix[row][col].setBounds(24 + (col * 60),
						36 + (row * 38), 48, 24);
				this.matrix[row][col].setText("0");
				this.matrix[row][col].addFocusListener(this);

				if (row == col) {
					this.matrix[row][col].setEnabled(false);
				}

			}
		}

		for (int i = 0; i < 4; i++) {
			this.output[i] = new java.awt.TextField();
			this.output[i].setEditable(false);
			this.output[i].setText("0");
			this.output[i].setEnabled(true);
			add(this.output[i]);
			this.output[i].setBounds(24 + (i * 60), 300, 48, 24);

			this.input[i] = new java.awt.Choice();
			this.input[i].add("0");
			this.input[i].add("1");
			this.input[i].select(0);
			add(this.input[i]);
			this.input[i].setBounds(24 + (i * 60), 200, 48, 24);
		}

		this.label3.setText("The output is:");
		add(this.label3);
		this.label3.setBounds(24, 276, 192, 12);

		this.go.setLabel("Run");
		add(this.go);
		this.go.setBackground(java.awt.Color.lightGray);
		this.go.setBounds(10, 240, 50, 24);

		this.train.setLabel("Train");
		add(this.train);
		this.train.setBackground(java.awt.Color.lightGray);
		this.train.setBounds(110, 240, 50, 24);

		this.clear.setLabel("Clear");
		add(this.clear);
		this.clear.setBackground(java.awt.Color.lightGray);
		this.clear.setBounds(210, 240, 50, 24);

		this.go.addActionListener(this);
		this.clear.addActionListener(this);
		this.train.addActionListener(this);
	}

	/**
	 * Collect the input, present it to the neural network, then display the
	 * results.
	 */
	void runNetwork() {

		final boolean pattern[] = new boolean[4];

		// Read the input into a boolean array.
		for (int row = 0; row < 4; row++) {
			final int i = this.input[row].getSelectedIndex();
			if (i == 0) {
				pattern[row] = false;
			} else {
				pattern[row] = true;
			}
		}

		// Present the input to the neural network.
		final boolean result[] = this.network.present(pattern);

		// Display the result.
		for (int row = 0; row < 4; row++) {
			if (result[row]) {
				this.output[row].setText("1");
			} else {
				this.output[row].setText("0");
			}

			// If the result is different than the input, show in yellow.
			if (result[row] == pattern[row]) {
				this.output[row].setBackground(java.awt.Color.white);
			} else {
				this.output[row].setBackground(java.awt.Color.yellow);
			}
		}

	}

	/**
	 * Set the matrix values on the applet from the matrix values stored in the
	 * neural network.
	 */
	private void setMatrixValues() {

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				this.matrix[row][col].setText(""
						+ (int) this.network.getMatrix().get(row, col));
			}
		}
	}

	/**
	 * Called when the train button is clicked. Train for the current pattern.
	 */
	void train() {
		final boolean[] booleanInput = new boolean[4];

		// Collect the input pattern.
		for (int x = 0; x < 4; x++) {
			booleanInput[x] = (this.input[x].getSelectedIndex() != 0);
		}

		// Train the input pattern.
		this.network.train(booleanInput);
		this.setMatrixValues();

	}

}
