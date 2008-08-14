package com.heatonresearch.book.introneuralnet.feedforward;

import com.heatonresearch.book.introneuralnet.XOR;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;


import junit.framework.TestCase;



public class TestClone extends TestCase {

	public void testClone() throws Throwable
	{
		FeedforwardNetwork source = XOR.createThreeLayerNet();
		source.reset();
		
		FeedforwardNetwork target = (FeedforwardNetwork)source.clone();
		
		TestCase.assertTrue(target.equals(source));
	}
	
}
