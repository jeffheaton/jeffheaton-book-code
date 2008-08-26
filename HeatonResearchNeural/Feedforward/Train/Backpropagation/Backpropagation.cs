using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Exception;

namespace HeatonResearchNeural.Feedforward.Train.Backpropagation
{
    public class Backpropagation : Train
    {
        /// <summary>
        /// Get the current best neural network.
        /// </summary>
        public FeedforwardNetwork Network
        {
            get
            {
                return this.network;
            }
        }


        /// <summary>
        /// Returns the root mean square error for a complete training set.
        /// </summary>

        public double Error
        {
            get
            {
                return this.error;
            }
        }



        /// <summary>
        /// The error from the last iteration.
        /// </summary>
        private double error;


        /// <summary>
        /// The learning rate. This is the degree to which the deltas will affect the
        /// current network.
        /// </summary>
        private double learnRate;


        /// <summary>
        /// The momentum, this is the degree to which the previous training cycle
        /// affects the current one.
        /// </summary>
        private double momentum;

        /// <summary>
        /// The network that is being trained.
        /// </summary>
        private FeedforwardNetwork network;

        /// <summary>
        /// A map between neural network layers and the corresponding
        /// BackpropagationLayer.
        /// </summary>
        private IDictionary<FeedforwardLayer, BackpropagationLayer> layerMap = new Dictionary<FeedforwardLayer, BackpropagationLayer>();

        /// <summary>
        /// Input patterns to train with.
        /// </summary>
        private double[][] input;

        /// <summary>
        /// The ideal output for each of the input patterns.
        /// </summary>
        private double[][] ideal;

        /// <summary>
        /// The rate at which the weight matrix will be adjusted based on
        /// learning.
        /// </summary>
        /// <param name="network">The neural network to be trained.</param>
        /// <param name="input">The input values to the neural network.</param>
        /// <param name="ideal">The ideal values expected from the neural network.</param>
        /// <param name="learnRate">The learning rate, how fast to modify neural network values.</param>
        /// <param name="momentum">The momentum, how much to use the previous training iteration for the current.</param>
        public Backpropagation(FeedforwardNetwork network,
                 double[][] input, double[][] ideal,
                 double learnRate, double momentum)
        {
            this.network = network;
            this.learnRate = learnRate;
            this.momentum = momentum;
            this.input = input;
            this.ideal = ideal;

            foreach (FeedforwardLayer layer in network.Layers)
            {
                BackpropagationLayer bpl = new BackpropagationLayer(this,
                       layer);
                this.layerMap.Add(layer, bpl);
            }
        }

        /// <summary>
        /// Calculate the error for the recognition just done.
        /// </summary>
        /// <param name="ideal">What the output neurons should have yielded.</param>
        public void CalcError(double[] ideal)
        {

            if (ideal.Length != this.network.OutputLayer.NeuronCount)
            {
                throw new NeuralNetworkError(
                        "Size mismatch: Can't calcError for ideal input size="
                                + ideal.Length + " for output layer size="
                                + this.network.OutputLayer.NeuronCount);
            }

            // clear out all previous error data
            foreach (FeedforwardLayer layer in this.network.Layers)
            {
                GetBackpropagationLayer(layer).ClearError();
            }

            for (int i = this.network.Layers.Count - 1; i >= 0; i--)
            {
                FeedforwardLayer layer = this.network.Layers[i];
                if (layer.IsOutput())
                {

                    GetBackpropagationLayer(layer).CalcError(ideal);
                }
                else
                {
                    GetBackpropagationLayer(layer).CalcError();
                }
            }
        }

        /// <summary>
        /// Get the BackpropagationLayer that corresponds to the specified layer.
        /// </summary>
        /// <param name="layer">The specified layer.</param>
        /// <returns>The BackpropagationLayer that corresponds to the specified layer.</returns>
        public BackpropagationLayer GetBackpropagationLayer(
                 FeedforwardLayer layer)
        {
            BackpropagationLayer result = this.layerMap[layer];

            if (result == null)
            {
                throw new NeuralNetworkError(
                        "Layer unknown to backpropagation trainer, was a layer added after training begain?");
            }

            return result;
        }





        /// <summary>
        /// Perform one iteration of training.
        /// </summary>
        public void Iteration()
        {

            for (int j = 0; j < this.input.Length; j++)
            {
                this.network.ComputeOutputs(this.input[j]);
                CalcError(this.ideal[j]);
            }
            Learn();

            this.error = this.network.CalculateError(this.input, this.ideal);
        }

        /// <summary>
        /// Modify the weight matrix and thresholds based on the last call to
        /// calcError.
        /// </summary>
        public void Learn()
        {

            foreach (FeedforwardLayer layer in this.network.Layers)
            {
                GetBackpropagationLayer(layer).Learn(this.learnRate, this.momentum);
            }

        }

    }
}
