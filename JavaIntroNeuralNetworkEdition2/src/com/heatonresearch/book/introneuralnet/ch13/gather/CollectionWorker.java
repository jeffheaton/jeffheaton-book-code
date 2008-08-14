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
package com.heatonresearch.book.introneuralnet.ch13.gather;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.heatonresearch.book.introneuralnet.ch13.Text;
import com.heatonresearch.book.introneuralnet.common.YahooSearch;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * CollectionWorker: Worker class in a thread pool used to 
 * gather sentences from the web.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class CollectionWorker implements Callable<Integer> {

	private final GatherForTrain bot;

	/*
	 * The search object to use.
	 */
	private final YahooSearch search;

	private final String name;
	private final int year;

	public CollectionWorker(final GatherForTrain bot, final String name,
			final int year) {
		this.bot = bot;
		this.name = name;
		this.year = year;
		this.search = new YahooSearch();
	}

	public Integer call() throws Exception {
		try {
			scanPerson(this.name, this.year);
			this.bot.reportDone(this.name + ", done scanning.");
		} catch (final Exception e) {
			this.bot.reportDone(this.name + ", error encountered.");
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	private void scanPerson(final String name, final int year)
			throws IOException {
		final Collection<URL> c = this.search.search(name);
		int i = 0;

		for (final URL u : c) {
			try {
				i++;
				Text.checkURL(this.bot, u, year);
			} catch (final IOException e) {

			}
		}
	}

}
