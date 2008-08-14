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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import com.heatonresearch.httprecipes.html.ParseHTML;

/**
 * Chapter 13: Bot Programming and Neural Networks
 * 
 * Text: Text parsing utilities.
 *  
 * @version 2.1
 */
public class Text {

	/**
	 * Check the specified URL for a birth year. This will occur if one sentence
	 * is found that has the word born, and a numeric value less than 3000.
	 * 
	 * @param url
	 *            The URL to check.
	 * @throws IOException
	 *             Thrown if a communication error occurs.
	 */
	public static void checkURL(final ScanReportable report, final URL url,
			final Integer desiredYear) throws IOException {
		int ch;
		final StringBuilder sentence = new StringBuilder();
		String ignoreUntil = null;

		final URLConnection http = url.openConnection();
		http.setConnectTimeout(1000);
		http.setReadTimeout(1000);
		final InputStream is = http.getInputStream();
		final ParseHTML html = new ParseHTML(is);
		do {
			ch = html.read();
			if ((ch != -1) && (ch != 0) && (ignoreUntil == null)) {
				if (".?!".indexOf(ch) != -1) {
					final String str = sentence.toString();
					final int year = Text.extractYear(str);

					if (desiredYear == null) {
						// looking for any year
						if (year != -1) {
							report.receiveGoodSentence(str);
						}
					} else {
						// looking for a specific year
						if (year == desiredYear) {
							report.receiveGoodSentence(str);
						} else if (year != -1) {
							report.receiveBadSentence(str);
						}
					}
					sentence.setLength(0);
				} else if (ch == ' ') {
					if ((sentence.length() > 0)
							&& (sentence.charAt(sentence.length() - 1) != ' ')) {
						sentence.append(' ');
					}
				} else if ((ch != '\n') && (ch != '\t') && (ch != '\r')) {
					if ((ch) < 128) {
						sentence.append((char) ch);
					}
				}
			} else if (ch == 0) {
				// clear anything before a body tag
				if (html.getTag().getName().equalsIgnoreCase("body")
						|| html.getTag().getName().equalsIgnoreCase("br")
						|| html.getTag().getName().equalsIgnoreCase("li")
						|| html.getTag().getName().equalsIgnoreCase("p")
						|| html.getTag().getName().equalsIgnoreCase("h1")
						|| html.getTag().getName().equalsIgnoreCase("h2")
						|| html.getTag().getName().equalsIgnoreCase("h3")
						|| html.getTag().getName().equalsIgnoreCase("td")
						|| html.getTag().getName().equalsIgnoreCase("th")) {
					sentence.setLength(0);
				}
				// ignore everything between script and style tags
				if (ignoreUntil == null) {
					if (html.getTag().getName().equalsIgnoreCase("script")) {
						ignoreUntil = "/script";
					} else if (html.getTag().getName()
							.equalsIgnoreCase("style")) {
						ignoreUntil = "/style";
					}
				} else {
					if (html.getTag().getName().equalsIgnoreCase(ignoreUntil)) {
						ignoreUntil = null;
					}
				}

				// add a space after the tag
				if (sentence.length() > 0) {
					if (sentence.charAt(sentence.length() - 1) != ' ') {
						sentence.append(' ');
					}
				}
			}
		} while (ch != -1);

	}

	/**
	 * Examine a sentence and see if it contains the word born and a number.
	 * 
	 * @param sentence
	 *            The sentence to search.
	 * @return The number that was found.
	 */
	public static int extractYear(final String sentence) {
		int result = -1;

		final StringTokenizer tok = new StringTokenizer(sentence);
		while (tok.hasMoreTokens()) {
			final String word = tok.nextToken();

			try {
				result = Integer.parseInt(word);

				if ((result < 1600) || (result > 2100)) {
					result = -1;
				}
			} catch (final NumberFormatException e) {
			}
		}

		return result;
	}

}
