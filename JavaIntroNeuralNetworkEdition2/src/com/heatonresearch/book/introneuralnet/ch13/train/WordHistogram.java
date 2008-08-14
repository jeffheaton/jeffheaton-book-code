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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.heatonresearch.book.introneuralnet.ch13.CommonWords;
import com.heatonresearch.book.introneuralnet.ch13.Config;


/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * WordHistogram: Build a histogram of how many occurrences of
 * each word.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class WordHistogram implements Serializable {

	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = 4712929616016273693L;

	public static void main(final String args[]) {
		try {
			final CommonWords common = new CommonWords(
					Config.FILENAME_COMMON_WORDS);
			final WordHistogram histogramGood = new WordHistogram(common);
			final WordHistogram histogramBad = new WordHistogram(common);

			// load the good words
			histogramGood.buildFromFile(Config.FILENAME_GOOD_TRAINING_TEXT);
			histogramGood.buildComplete();

			// load the bad words
			histogramBad.buildFromFile(Config.FILENAME_BAD_TRAINING_TEXT);
			histogramBad.buildComplete();

			// remove low scoring words
			histogramGood.removeBelow((int) histogramGood.calculateMean());
			histogramBad.removePercent(0.99);

			// remove common words
			histogramGood.removeCommon(histogramBad);
			// histogramBad.removeCommon(histogramGood);

			System.out.println("Good Words");
			for (final HistogramElement element : histogramGood.getSorted()) {
				System.out
						.println(element.getWord() + ":" + element.getCount());
			}
			System.out.println("\n\n\n");
			System.out.println("Bad Words");
			for (final HistogramElement element : histogramBad.getSorted()) {
				System.out
						.println(element.getWord() + ":" + element.getCount());
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final CommonWords common;
	private final Map<String, HistogramElement> histogram = new HashMap<String, HistogramElement>();

	private final Set<HistogramElement> sorted = new TreeSet<HistogramElement>();

	public WordHistogram(final CommonWords common) {
		this.common = common;
	}

	public void buildComplete() {
		this.sorted.clear();
		this.sorted.addAll(this.histogram.values());
	}

	public void buildFromFile(final InputStream is) throws IOException {
		String line;

		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			buildFromLine(line);
		}
		br.close();
	}

	public void buildFromFile(final String filename) throws IOException {
		final FileInputStream fis = new FileInputStream(filename);
		buildFromFile(fis);
		fis.close();
	}

	public void buildFromLine(final String line) {
		final StringTokenizer tok = new StringTokenizer(line);
		while (tok.hasMoreTokens()) {
			final String word = tok.nextToken();
			buildFromWord(word);
		}
	}

	public void buildFromWord(String word) {
		word = word.trim().toLowerCase();
		if (this.common.isCommonWord(word)) {
			HistogramElement element = this.histogram.get(word);
			if (element == null) {
				element = new HistogramElement(word, 0);
				this.histogram.put(word, element);
			}
			element.increase();
		}
	}

	public double calculateMean() {
		int total = 0;
		for (final HistogramElement element : this.sorted) {
			total += element.getCount();
		}
		return (double) total / (double) this.sorted.size();
	}

	public double[] compact(final String line) {
		final double[] result = new double[this.sorted.size()];

		final StringTokenizer tok = new StringTokenizer(line);

		while (tok.hasMoreTokens()) {
			final String word = tok.nextToken().toLowerCase();
			final int rank = getRank(word);
			if (rank != -1) {
				result[rank] = 0.9;
			}
		}
		return result;
	}

	public int count(final String line) {
		int result = 0;

		final StringTokenizer tok = new StringTokenizer(line);

		while (tok.hasMoreTokens()) {
			final String word = tok.nextToken().toLowerCase();
			final int rank = getRank(word);
			if (rank != -1) {
				result++;
			}
		}
		return result;
	}

	public HistogramElement get(final String word) {
		return this.histogram.get(word.toLowerCase());
	}

	public CommonWords getCommon() {
		return this.common;
	}

	public int getRank(final String word) {
		int result = 0;

		for (final HistogramElement element : this.sorted) {
			if (element.getWord().equalsIgnoreCase(word)) {
				return result;
			}
			result++;
		}
		return -1;
	}

	public Set<HistogramElement> getSorted() {
		return this.sorted;
	}

	public void removeBelow(final int value) {
		final Object[] elements = this.sorted.toArray();
		for (int i = 0; i < elements.length; i++) {
			final HistogramElement element = (HistogramElement) elements[i];
			if (element.getCount() < value) {
				this.histogram.remove(element.getWord());
				this.sorted.remove(element);
			}
		}
	}

	public void removeCommon(final WordHistogram other) {
		for (final HistogramElement element : other.getSorted()) {
			final HistogramElement e = this.get(element.getWord());
			if (e == null) {
				continue;
			}
			this.sorted.remove(e);
			this.histogram.remove(element.getWord());

		}
	}

	public void removePercent(final double percent) {
		int countdown = (int) (this.sorted.size() * (1 - percent));

		final Object[] elements = this.sorted.toArray();
		for (int i = 0; i < elements.length; i++) {
			countdown--;
			if (countdown < 0) {
				final HistogramElement element = (HistogramElement) elements[i];
				this.histogram.remove(element.getWord());
				this.sorted.remove(element);
			}
		}
	}

	public void trim(final int size) {

		final Object[] elements = this.sorted.toArray();
		for (int i = 0; i < elements.length; i++) {
			if (i >= size) {
				final HistogramElement element = (HistogramElement) elements[i];
				this.histogram.remove(element.getWord());
				this.sorted.remove(element);
			}
		}
	}

}
