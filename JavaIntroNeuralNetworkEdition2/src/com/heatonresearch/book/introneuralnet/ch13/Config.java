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
package com.heatonresearch.book.introneuralnet.ch13;
/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * Config: Holds configuration data for the bot.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class Config {

	public final static int INPUT_SIZE = 10;
	public final static int OUTPUT_SIZE = 1;
	public final static int NEURONS_HIDDEN_1 = 20;
	public final static int NEURONS_HIDDEN_2 = 0;
	public final static double ACCEPTABLE_ERROR = 0.01;
	public final static int MINIMUM_WORDS_PRESENT = 3;

	public static final String FILENAME_GOOD_TRAINING_TEXT = "bornTrainingGood.txt";
	public static final String FILENAME_BAD_TRAINING_TEXT = "bornTrainingBad.txt";
	public static final String FILENAME_COMMON_WORDS = "common.csv";
	public static final String FILENAME_WHENBORN_NET = "whenborn.net";
	public static final String FILENAME_HISTOGRAM = "whenborn.hst";
}
