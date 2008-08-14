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
 * ScanReportable: Receives the results from a URL scan.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public interface ScanReportable {
	public void receiveBadSentence(String sentence);

	public void receiveGoodSentence(String sentence);
}
