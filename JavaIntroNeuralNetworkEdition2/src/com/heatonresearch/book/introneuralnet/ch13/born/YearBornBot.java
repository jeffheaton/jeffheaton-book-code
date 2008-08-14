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
package com.heatonresearch.book.introneuralnet.ch13.born;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.heatonresearch.book.introneuralnet.ch13.Config;
import com.heatonresearch.book.introneuralnet.ch13.ScanReportable;
import com.heatonresearch.book.introneuralnet.ch13.Text;
import com.heatonresearch.book.introneuralnet.ch13.train.WordHistogram;
import com.heatonresearch.book.introneuralnet.common.YahooSearch;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.util.SerializeObject;

/**
 * Bot example.
 * 
 * @author Jeff Heaton
 * @version 1.1
 */

public class YearBornBot implements ScanReportable {
	public static final boolean LOG = true;
	/*
	 * The search object to use.
	 */
	static YahooSearch search;

	/**
	 * The main method processes the command line arguments and then calls
	 * process method to determine the birth year.
	 * 
	 * @param args
	 *            The person to scan.
	 * 
	 */
	public static void main(final String args[]) {
		/*
		 * if (args.length < 1) { System.out.println("YearBornBot [Famous
		 * Person]"); } else
		 */{
			try {
				final YearBornBot when = new YearBornBot();
				// when.process(args[0]);
				when.process("Bill Gates");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final FeedforwardNetwork network;

	private final WordHistogram histogram;

	/*
	 * This map stores a mapping between a year, and how many times that year
	 * has come up as a potential birth year.
	 */
	private final Map<Integer, Integer> results = new HashMap<Integer, Integer>();

	public YearBornBot() throws IOException, ClassNotFoundException {
		this.network = (FeedforwardNetwork) SerializeObject
				.load(Config.FILENAME_WHENBORN_NET);
		this.histogram = (WordHistogram) SerializeObject
				.load(Config.FILENAME_HISTOGRAM);
	}

	/**
	 * Get birth year that occurred the largest number of times.
	 * 
	 * @return The birth year that occurred the largest number of times.
	 */
	public int getResult() {
		int result = -1;
		int maxCount = 0;

		final Set<Integer> set = this.results.keySet();
		for (final int year : set) {
			final int count = this.results.get(year);
			if (count > maxCount) {
				result = year;
				maxCount = count;
			}
		}

		return result;
	}

	/**
	 * @param year
	 */
	private void increaseYear(final int year) {
		Integer count = this.results.get(year);
		if (count == null) {
			count = new Integer(1);
		} else {
			count = new Integer(count.intValue() + 1);
		}
		this.results.put(year, count);
	}

	/**
	 * This method is called to determine the birth year for a person. It
	 * obtains 100 web pages that Yahoo returns for that person. Each of these
	 * pages is then searched for the birth year of that person. Which ever year
	 * is selected the largest number of times is selected as the birth year.
	 * 
	 * @param name
	 *            The name of the person you are seeing the birth year for.
	 * @throws IOException
	 *             Thrown if a communication error occurs.
	 */
	public void process(final String name) throws IOException {
		search = new YahooSearch();

		if (YearBornBot.LOG) {
			System.out.println("Getting search results form Yahoo.");
		}
		final Collection<URL> c = search.search(name);
		int i = 0;

		if (YearBornBot.LOG) {
			System.out.println("Scanning URL's from Yahoo.");
		}
		for (final URL u : c) {
			try {
				i++;
				Text.checkURL(this, u, null);
			} catch (final IOException e) {

			}
		}

		final int resultYear = getResult();
		if (resultYear == -1) {
			System.out.println("Could not determine when " + name
					+ " was born.");
		} else {
			System.out.println(name + " was born in " + resultYear);
		}

	}

	public void receiveBadSentence(final String sentence) {
		// TODO Auto-generated method stub

	}

	public void receiveGoodSentence(final String sentence) {
		if (this.histogram.count(sentence) >= Config.MINIMUM_WORDS_PRESENT) {
			final double compact[] = this.histogram.compact(sentence);
			final int year = Text.extractYear(sentence);

			final double output[] = this.network.computeOutputs(compact);

			if (output[0] > 0.8) {
				increaseYear(year);
				System.out.println(year + "-" + output[0] + ":" + sentence);
			}
		}

	}

}