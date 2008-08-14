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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.heatonresearch.book.introneuralnet.ch13.Config;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * AnalyzeSentences: Analyze either good or bad sentences.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class AnalyzeSentences {
	private int sampleCount;
	private int currentSample;
	private final WordHistogram histogram;

	public AnalyzeSentences(final WordHistogram histogram, final int size) {
		this.histogram = histogram;
	}

	private void countLines(final String filename) throws IOException {
		this.sampleCount++;
		final BufferedReader br = new BufferedReader(new FileReader(filename));
		while ((br.readLine()) != null) {
			this.sampleCount++;
		}
		br.close();
	}

	public void process(final TrainingSet set, final double value,
			final String filename) throws IOException {
		countLines(filename);
		processLines(filename, set, value);
	}

	private void processLine(final String line, final TrainingSet set,
			final double value) {
		if (this.histogram.count(line) >= Config.MINIMUM_WORDS_PRESENT) {
			final double[] result = this.histogram.compact(line);
			set.addTrainingSet(result, value);
			this.currentSample++;
		}
	}

	private void processLines(final String filename, final TrainingSet set,
			final double value) throws IOException {
		this.currentSample = 0;
		String line = null;
		final BufferedReader br = new BufferedReader(new FileReader(filename));
		while ((line = br.readLine()) != null) {
			processLine(line, set, value);
		}
		br.close();
	}

}
