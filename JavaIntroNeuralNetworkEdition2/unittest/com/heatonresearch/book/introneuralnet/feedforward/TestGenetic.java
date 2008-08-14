package com.heatonresearch.book.introneuralnet.feedforward;


import com.heatonresearch.book.introneuralnet.XOR;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.feedforward.train.genetic.TrainingSetNeuralGeneticAlgorithm;

import junit.framework.TestCase;

public class TestGenetic extends TestCase {
	public void testGenetic() throws Throwable
	{
		FeedforwardNetwork network = XOR.createThreeLayerNet();
				
		TrainingSetNeuralGeneticAlgorithm train = new TrainingSetNeuralGeneticAlgorithm(network, true, XOR.XOR_INPUT, XOR.XOR_IDEAL,5000,0.1,0.25);	

		for (int i = 0; i < 100; i++) 
		{
			train.iteration();
			network = train.getNetwork();
		}
		
		TestCase.assertTrue("Error too high for genetic algorithm",train.getError()<0.1);
		TestCase.assertTrue("XOR outputs not correct",XOR.verifyXOR(network, 0.1));

	}
}
