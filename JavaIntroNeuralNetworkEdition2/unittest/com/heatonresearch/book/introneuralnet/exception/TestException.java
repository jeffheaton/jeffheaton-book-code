package com.heatonresearch.book.introneuralnet.exception;

import junit.framework.TestCase;

import com.heatonresearch.book.introneuralnet.neural.exception.MatrixError;
import com.heatonresearch.book.introneuralnet.neural.exception.NeuralNetworkError;

public class TestException extends TestCase {
	public void testMatrixError()
	{
		NullPointerException npe = new NullPointerException();
		new MatrixError(npe);
		new NeuralNetworkError(npe);
	}
}
