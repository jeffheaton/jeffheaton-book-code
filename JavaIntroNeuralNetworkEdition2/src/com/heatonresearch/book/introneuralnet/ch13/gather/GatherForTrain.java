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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.heatonresearch.book.introneuralnet.ch13.Config;
import com.heatonresearch.book.introneuralnet.ch13.ScanReportable;
import com.heatonresearch.book.introneuralnet.common.ReadCSV;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * GatherForTrain: Gather training data for the neural network.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class GatherForTrain implements ScanReportable {
	public static final boolean LOG = true;
	public static final int THREAD_POOL_SIZE = 20;

	/**
	 * The main method processes the command line arguments and then calls
	 * process method to determine the birth year.
	 * 
	 * @param args
	 *            Holds the Yahoo key and the site to search.
	 */
	public static void main(final String args[]) {
		try {
			final GatherForTrain when = new GatherForTrain();
			when.process();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private ExecutorService pool;
	private final Collection<String> trainingDataGood = new Vector<String>();
	private final Collection<String> trainingDataBad = new Vector<String>();
	private int currentTask;

	private int totalTasks;

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
	public void process() throws IOException {
		this.pool = Executors
				.newFixedThreadPool(GatherForTrain.THREAD_POOL_SIZE);
		final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		final ReadCSV famous = new ReadCSV("famous.csv");
		report("Building training data from list of famous people.");
		final Date started = new Date();

		while (famous.next()) {
			final String name = famous.get("Person");
			final int year = famous.getInt("Year");

			final CollectionWorker worker = new CollectionWorker(this, name,
					year);
			tasks.add(worker);
		}

		try {
			this.totalTasks = tasks.size();
			this.currentTask = 1;
			this.pool.invokeAll(tasks);
			this.pool.shutdownNow();
			report("Done collecting Internet data.");
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long length = (new Date()).getTime() - started.getTime();
		length /= 1000L;
		length /= 60;
		System.out.println("Took " + length
				+ " minutes to collect training data from the Internet.");
		System.out.println("Writing training file");
		writeTrainingFile();

	}

	public void receiveBadSentence(final String sentence) {
		this.trainingDataBad.add(sentence);

	}

	public void receiveGoodSentence(final String sentence) {
		this.trainingDataGood.add(sentence);

	}

	public void report(final String str) {
		synchronized (this) {
			System.out.println(str);
		}
	}

	public void reportDone(final String string) {
		report(this.currentTask + "/" + this.totalTasks + ":" + string);
		this.currentTask++;
	}

	private void writeTrainingFile() throws IOException {
		OutputStream fs = new FileOutputStream(
				Config.FILENAME_GOOD_TRAINING_TEXT);
		PrintStream ps = new PrintStream(fs);

		for (final String str : this.trainingDataGood) {
			ps.println(str.trim());
		}

		ps.close();
		fs.close();

		fs = new FileOutputStream(Config.FILENAME_BAD_TRAINING_TEXT);
		ps = new PrintStream(fs);

		for (final String str : this.trainingDataBad) {
			ps.println(str.trim());
		}

		ps.close();
		fs.close();
	}

}