// Encog Neural Network and Bot Library for DotNet v0.5
// http://www.heatonresearch.com/encog/
// http://code.google.com/p/encog-cs/
// 
// Copyright 2008, Heaton Research Inc., and individual contributors.
// See the copyright.txt in the distribution for a full listing of 
// individual contributors.
//
// This is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation; either version 2.1 of
// the License, or (at your option) any later version.
//
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this software; if not, write to the Free
// Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
// 02110-1301 USA, or see the FSF site: http://www.fsf.org.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Encog.Util;
using Encog.Matrix;

namespace Encog.Neural.Feedforward
{
    [Serializable]
    public class FeedforwardNetwork
    {
        /// <summary>
        /// The input layer.
        /// </summary>
        public FeedforwardLayer InputLayer
        {
            get
            {
                return this.inputLayer;
            }
        }

        /// <summary>
        /// The output layer.
        /// </summary>
        public FeedforwardLayer OutputLayer
        {
            get
            {
                return this.outputLayer;
            }
        }

        /// <summary>
        /// All of the layers in the neural network.
        /// </summary>
        public IList<FeedforwardLayer> Layers
        {
            get
            {
                return this.layers;
            }
        }

        /// <summary>
        /// Get the count for how many hidden layers are present.
        /// </summary>
        public int HiddenLayerCount
        {
            get
            {
                return this.layers.Count - 2;
            }
        }

        /// <summary>
        /// Get the size of the weight and threshold matrix.
        /// </summary>
        public int MatrixSize
        {
            get
            {
                int result = 0;
                foreach (FeedforwardLayer layer in this.layers)
                {
                    result += layer.MatrixSize;
                }
                return result;
            }
        }

        /// <summary>
        /// Return a list of the hidden layers. 
        /// </summary>
        public ICollection<FeedforwardLayer> HiddenLayers
        {
            get
            {
                ICollection<FeedforwardLayer> result = new List<FeedforwardLayer>();
                foreach (FeedforwardLayer layer in this.layers)
                {
                    if (layer.IsHidden())
                    {
                        result.Add(layer);
                    }
                }
                return result;
            }
        }

        /// <summary>
        /// The input layer.
        /// </summary>
        protected FeedforwardLayer inputLayer;

        /// <summary>
        /// The output layer.
        /// </summary>
        protected FeedforwardLayer outputLayer;

        /// <summary>
        /// All of the layers in the neural network.
        /// </summary>
        protected IList<FeedforwardLayer> layers = new List<FeedforwardLayer>();

        /// <summary>
        /// Construct an empty neural network.
        /// </summary>
        public FeedforwardNetwork()
        {
        }

        /// <summary>
        /// Add a layer to the neural network. The first layer added is the input
        /// layer, the last layer added is the output layer.
        /// </summary>
        /// <param name="layer">The layer to be added.</param>
        public void AddLayer(FeedforwardLayer layer)
        {
            // setup the forward and back pointer
            if (this.outputLayer != null)
            {
                layer.Previous = this.outputLayer;
                this.outputLayer.Next = layer;
            }

            // update the inputLayer and outputLayer variables
            if (this.layers.Count == 0)
            {
                this.inputLayer = this.outputLayer = layer;
            }
            else
            {
                this.outputLayer = layer;
            }

            // add the new layer to the list
            this.layers.Add(layer);
        }




        /// <summary>
        /// Calculate the error for this neural network. The error is calculated
        /// using root-mean-square(RMS).
        /// </summary>
        /// <param name="input">Input patterns.</param>
        /// <param name="ideal">Ideal patterns.</param>
        /// <returns>The error percentage.</returns>
        public double CalculateError(double[][] input, double[][] ideal)
        {
            ErrorCalculation errorCalculation = new ErrorCalculation();

            for (int i = 0; i < ideal.Length; i++)
            {
                ComputeOutputs(input[i]);
                errorCalculation.UpdateError(this.outputLayer.Fire,
                        ideal[i]);
            }
            return (errorCalculation.CalculateRMS());
        }


        /// <summary>
        /// Calculate the total number of neurons in the network across all layers.
        /// </summary>
        /// <returns>The neuron count.</returns>
        public int CalculateNeuronCount()
        {
            int result = 0;
            foreach (FeedforwardLayer layer in this.layers)
            {
                result += layer.NeuronCount;
            }
            return result;
        }


        /// <summary>
        /// Return a clone of this neural network. Including structure, weights and
        /// threshold values.
        /// </summary>
        /// <returns>A cloned copy of the neural network.</returns>
        public Object Clone()
        {
            FeedforwardNetwork result = CloneStructure();
            Double[] copy = MatrixCODEC.NetworkToArray(this);
            MatrixCODEC.ArrayToNetwork(copy, result);
            return result;
        }


        /// <summary>
        /// Return a clone of the structure of this neural network. 
        /// </summary>
        /// <returns>A cloned copy of the structure of the neural network.</returns>

        public FeedforwardNetwork CloneStructure()
        {
            FeedforwardNetwork result = new FeedforwardNetwork();

            foreach (FeedforwardLayer layer in this.layers)
            {
                FeedforwardLayer clonedLayer = new FeedforwardLayer(layer.NeuronCount);
                result.AddLayer(clonedLayer);
            }

            return result;
        }



        /// <summary>
        /// Compute the output for a given input to the neural network.
        /// </summary>
        /// <param name="input">The input provide to the neural network.</param>
        /// <returns>The results from the output neurons.</returns>
        public double[] ComputeOutputs(double[] input)
        {

            if (input.Length != this.inputLayer.NeuronCount)
            {
                throw new NeuralNetworkError(
                        "Size mismatch: Can't compute outputs for input size="
                                + input.Length + " for input layer size="
                                + this.inputLayer.NeuronCount);
            }

            foreach (FeedforwardLayer layer in this.layers)
            {
                if (layer.IsInput())
                {
                    layer.ComputeOutputs(input);
                }
                else if (layer.IsHidden())
                {
                    layer.ComputeOutputs(null);
                }
            }

            return this.outputLayer.Fire;
        }

        /// <summary>
        /// Compare the two neural networks. For them to be equal they must be of the
        /// same structure, and have the same matrix values.
        /// </summary>
        /// <param name="other">The other neural network.</param>
        /// <returns>True if the two networks are equal.</returns>
        public bool Equals(FeedforwardNetwork other)
        {
            int i = 0;

            foreach (FeedforwardLayer layer in this.Layers)
            {
                FeedforwardLayer otherLayer = other.Layers[i++];

                if (layer.NeuronCount != otherLayer.NeuronCount)
                {
                    return false;
                }

                // make sure they either both have or do not have
                // a weight matrix.
                if ((layer.LayerMatrix == null) && (otherLayer.LayerMatrix != null))
                {
                    return false;
                }

                if ((layer.LayerMatrix != null) && (otherLayer.LayerMatrix == null))
                {
                    return false;
                }

                // if they both have a matrix, then compare the matrices
                if ((layer.LayerMatrix != null) && (otherLayer.LayerMatrix != null))
                {
                    if (!layer.LayerMatrix.Equals(otherLayer.LayerMatrix))
                    {
                        return false;
                    }
                }
            }

            return true;
        }

        /// <summary>
        /// Reset the weight matrix and the thresholds.
        /// </summary>
        public void Reset()
        {
            foreach (FeedforwardLayer layer in this.layers)
            {
                layer.Reset();
            }
        }
    }
}
