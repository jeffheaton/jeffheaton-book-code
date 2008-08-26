using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train;

namespace Chapter5XOR
{
    /// <summary>
    /// Chapter 5: The Feedforward Backpropagation Neural Network
    /// 
    /// Solve the classic XOR problem.
    /// </summary>
    class XOR
    {
        /// <summary>
        /// Input for the XOR function.
        /// </summary>
        public static double[][] XOR_INPUT ={
            new double[2] { 0.0, 0.0 },
            new double[2] { 1.0, 0.0 },
			new double[2] { 0.0, 1.0 },
            new double[2] { 1.0, 1.0 } };

        /// <summary>
        /// Ideal output for the XOR function.
        /// </summary>
        public static double[][] XOR_IDEAL = {                                              
            new double[1] { 0.0 }, 
            new double[1] { 1.0 }, 
            new double[1] { 1.0 }, 
            new double[1] { 0.0 } };

        /// <summary>
        /// Create, train and use a neural network for XOR.
        /// </summary>
        /// <param name="args">Not used</param>
        static void Main(string[] args)
        {
            FeedforwardNetwork network = new FeedforwardNetwork();
            network.AddLayer(new FeedforwardLayer(2));
            network.AddLayer(new FeedforwardLayer(3));
            network.AddLayer(new FeedforwardLayer(1));
            network.Reset();

            // train the neural network
            Train train = new HeatonResearchNeural.Feedforward.Train.Backpropagation.Backpropagation(network, XOR_INPUT, XOR_IDEAL,
                    0.7, 0.9);

            int epoch = 1;

            do
            {
                train.Iteration();
                Console.WriteLine("Epoch #" + epoch + " Error:" + train.Error);
                epoch++;
            } while ((epoch < 5000) && (train.Error > 0.001));

            // test the neural network
            Console.WriteLine("Neural Network Results:");
            for (int i = 0; i < XOR_IDEAL.Length; i++)
            {
                double[] actual = network.ComputeOutputs(XOR_INPUT[i]);
                Console.WriteLine(XOR_INPUT[i][0] + "," + XOR_INPUT[i][1]
                        + ", actual=" + actual[0] + ",ideal=" + XOR_IDEAL[i][0]);
            }
        }
    }
}
