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
package com.heatonresearch.book.introneuralnet.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * ReadCSV: Read and parse CSV format files.
 *  
 * @author Jeff Heaton
 * @version 2.1
 */
public class ReadCSV {

	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static String displayDate(final Date date) {
		return sdf.format(date);
	}

	public static Date parseDate(final String when) {
		try {
			return sdf.parse(when);
		} catch (final ParseException e) {
			return null;
		}
	}

	private final BufferedReader reader;

	private final Map<String, Integer> columns = new HashMap<String, Integer>();

	private final String data[];

	public ReadCSV(final String filename) throws IOException {
		this.reader = new BufferedReader(new FileReader(filename));

		// read the column heads
		final String line = this.reader.readLine();
		final StringTokenizer tok = new StringTokenizer(line, ",");
		int i = 0;
		while (tok.hasMoreTokens()) {
			final String header = tok.nextToken();
			this.columns.put(header.toLowerCase(), i++);
		}

		this.data = new String[i];
	}

	public void close() throws IOException {
		this.reader.close();
	}

	public String get(final int i) {
		return this.data[i];
	}

	public String get(final String column) {
		final Integer i = this.columns.get(column.toLowerCase());
		if (i == null) {
			return null;
		}
		return this.data[i.intValue()];
	}

	public Date getDate(final String column) throws ParseException {
		final String str = get(column);
		return sdf.parse(str);
	}

	public double getDouble(final String column) {
		final String str = get(column);
		return Double.parseDouble(str);
	}

	public int getInt(final String col) {
		final String str = get(col);
		try {
			return Integer.parseInt(str);
		} catch (final NumberFormatException e) {
			return 0;
		}
	}

	public boolean next() throws IOException {
		final String line = this.reader.readLine();
		if (line == null) {
			return false;
		}

		final StringTokenizer tok = new StringTokenizer(line, ",");

		int i = 0;
		while (tok.hasMoreTokens()) {
			final String str = tok.nextToken();
			if (i < this.data.length) {
				this.data[i++] = str;
			}
		}

		return true;
	}

}
