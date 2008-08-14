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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * CommonWords: Holds a list of common words in the English
 * language.
 * 
 * @author Jeff Heaton
 * @version 2.1
 */
public class CommonWords implements Serializable {
	/**
	 * Serial id for this class.
	 */
	private static final long serialVersionUID = -8162247588937098073L;
	private final Map<String, Integer> words;

	public CommonWords(final String filename) throws IOException {
		this.words = new HashMap<String, Integer>();

		final BufferedReader reader = new BufferedReader(new FileReader(
				filename));
		String line;

		int index = 0;
		while ((line = reader.readLine()) != null) {
			this.words.put(line.trim().toLowerCase(), index++);
		}

		reader.close();
	}

	public int getWordIndex(final String word) {
		return this.words.get(word.toLowerCase());
	}

	public boolean isCommonWord(final String word) {
		return (this.words.containsKey(word.toLowerCase()));
	}

}
