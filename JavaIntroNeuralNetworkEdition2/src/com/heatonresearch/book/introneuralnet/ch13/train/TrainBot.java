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
package com.heatonresearch.book.introneuralnet.ch13.train;

import java.io.IOException;

import com.heatonresearch.book.introneuralnet.ch13.CommonWords;
import com.heatonresearch.book.introneuralnet.ch13.Config;
import com.heatonresearch.book.introneuralnet.ch13.NetworkUtil;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.Train;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.backpropagation.Backpropagation;
import com.heatonresearch.book.introneuralnet.neural.util.SerializeObject;


/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * TrainBot:Train the bot with the data gathered.
 */
public class TrainBot {

	public static void main(final String args[]) {
		try {
			final TrainBot trainBot = new TrainBot();
			trainBot.process();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private int sampleCount;
	private CommonWords common;
	private double input[][];
	private double ideal[][];
	private FeedforwardNetwork network;
	private AnalyzeSentences goodAnalysis;
	private AnalyzeSentences badAnalysis;
	WordHistogram histogramGood;
	WordHistogram histogramBad;

	private final TrainingSet trainingSet;

	public TrainBot() throws IOException {
		this.common = new CommonWords(Config.FILENAME_COMMON_WORDS);
		this.trainingSet = new TrainingSet();
	}

	private void allocateTrainingSets() {
		this.input = new double[this.sampleCount][Config.INPUT_SIZE];
		this.ideal = new double[this.sampleCount][Config.OUTPUT_SIZE];
	}

	private void copyTrainingSets() {
		int index = 0;
		// first the input
		for (final double[] array : this.trainingSet.getInput()) {
			System.arraycopy(array, 0, this.input[index], 0, array.length);
			index++;
		}
		index = 0;
		// second the ideal
		for (final double[] array : this.trainingSet.getIdeal()) {
			System.arraycopy(array, 0, this.ideal[index], 0, array.length);
			index++;
		}

	}

	public void process() throws IOException {
		this.network = NetworkUtil.createNetwork();
		System.out.println("Preparing training sets...");
		this.common = new CommonWords(Config.FILENAME_COMMON_WORDS);
		this.histogramGood = new WordHistogram(this.common);
		this.histogramBad = new WordHistogram(this.common);

		// load the good words
		this.histogramGood.buildFromFile(Config.FILENAME_GOOD_TRAINING_TEXT);
		this.histogramGood.buildComplete();

		// load the bad words
		this.histogramBad.buildFromFile(Config.FILENAME_BAD_TRAINING_TEXT);
		this.histogramBad.buildComplete();

		// remove low scoring words
		this.histogramGood
				.removeBelow((int) this.histogramGood.calculateMean());
		this.histogramBad.removePercent(0.99);

		// remove common words
		this.histogramGood.removeCommon(this.histogramBad);

		this.histogramGood.trim(Config.INPUT_SIZE);

		this.goodAnalysis = new AnalyzeSentences(this.histogramGood,
				Config.INPUT_SIZE);
		this.badAnalysis = new AnalyzeSentences(this.histogramGood,
				Config.INPUT_SIZE);

		this.goodAnalysis.process(this.trainingSet, 0.9,
				Config.FILENAME_GOOD_TRAINING_TEXT);
		this.badAnalysis.process(this.trainingSet, 0.1,
				Config.FILENAME_BAD_TRAINING_TEXT);

		this.sampleCount = this.trainingSet.getIdeal().size();
		System.out
				.println("Processing " + this.sampleCount + " training sets.");

		allocateTrainingSets();

		copyTrainingSets();

		trainNetworkBackpropBackprop();
		SerializeObject.save(Config.FILENAME_WHENBORN_NET, this.network);
		SerializeObject.save(Config.FILENAME_HISTOGRAM, this.histogramGood);
		System.out.println("Training complete.");

	}

	private void trainNetworkBackpropBackprop() {
		final Train train = new Backpropagation(this.network, this.input,
				this.ideal, 0.7, 0.7);

		int epoch = 1;

		do {
			train.iteration();
			System.out.println("Backprop:Iteration #" + epoch + " Error:"
					+ train.getError());
			epoch++;
		} while ((train.getError() > Config.ACCEPTABLE_ERROR));
	}
}
