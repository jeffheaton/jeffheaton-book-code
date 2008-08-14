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
package com.heatonresearch.book.introneuralnet.neural.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * SerializeObject: Load or save an object using Java serialization.
 *  
 * @author Jeff Heaton
 * @version 2.1
 */
public class SerializeObject {
	
	/**
	 * Load an object.
	 * 
	 * @param filename
	 *            The filename.
	 * @return The loaded object.
	 * @throws IOException
	 *             An IO error occurred.
	 * @throws ClassNotFoundException
	 *             The specified class can't be found.
	 */
	public static Serializable load(final String filename) throws IOException,
			ClassNotFoundException {
		Serializable object;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		object = (Serializable) in.readObject();
		in.close();
		return object;
	}

	/**
	 * Save the specified object.
	 * @param filename The filename to save.
	 * @param object The object to save.
	 * @throws IOException An IO error occurred.
	 */
	public static void save(final String filename, final Serializable object)
			throws IOException {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		fos = new FileOutputStream(filename);
		out = new ObjectOutputStream(fos);
		out.writeObject(object);
		out.close();
	}

}
