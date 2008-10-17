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

using Encog.Neural.Activation;
using Encog.Matrix;

namespace Encog.Neural.Feedforward
{
    [Serializable]
    public class FeedforwardLayer
    {
        /// <summary>
        /// The weight and threshold matrix for this layer.
        /// </summary>
        public Matrix.Matrix LayerMatrix
        {
            get
            {
                return this.matrix;
            }
            set
            {
                if (matrix.Rows < 2)
                {
                    throw new NeuralNetworkError(
                            "Weight matrix includes threshold values, and must have at least 2 rows.");
                }
                if (matrix != null)
                {
                    this.fire = new double[matrix.Rows - 1];
                }
                this.matrix = value;
            }
        }

        /// <summary>
        /// The number of neurons in this layer.
        /// </summary>
        public int NeuronCount
        {
            get
            {
                return fire.Length;
            }
        }

        /// <summary>
        /// The outputs from the layer.
        /// </summary>
        public double[] Fire
        {
            get
            {
                return this.fire;
            }
        }

        /// <summary>
        /// The next layer in this neural network.
        /// </summary>
        public FeedforwardLayer Next
        {
            get
            {
                return this.next;
            }
            set
            {
                this.next = value;

                // add one to the neuron count to provide a threshold value in row 0
                this.matrix = new Matrix.Matrix(this.NeuronCount + 1, next.NeuronCount);
            }
        }

        /// <summary>
        /// The previous layer in this neural network.
        /// </summary>
        public FeedforwardLayer Previous
        {
            get
            {
                return this.previous;
            }
            set
            {
                this.previous = value;
            }
        }


        /// <summary>
        /// The activation function for this layer.
        /// </summary>
        public ActivationFunction LayerActivationFunction
        {
            get
            {
                return this.activationFunction;
            }
            set
            {
                this.activationFunction = value;
            }
        }

        public int MatrixSize
        {
            get
            {
                if (this.matrix == null)
                {
                    return 0;
                }
                else
                {
                    return this.matrix.Size;
                }
            }
        }

        /// <summary>
        /// Results from the last time that the outputs were calculated for this
        /// layer.
        /// </summary>
        private double[] fire;

        /// <summary>
        /// The weight and threshold matrix.
        /// </summary>
        private Matrix.Matrix matrix;

        /// <summary>
        /// The next layer in the neural network.
        /// </summary>
        private FeedforwardLayer next;

        /// <summary>
        /// The previous layer in the neural network.
        /// </summary>
        private FeedforwardLayer previous;

        /// <summary>
        /// Which activation function to use for this layer.
        /// </summary>
        private ActivationFunction activationFunction;

        /// <summary>
        /// Construct this layer with a non-default threshold function.
        /// </summary>
        /// <param name="thresholdFunction">The threshold function to use.</param>
        /// <param name="neuronCount">How many neurons in this layer.</param>
        public FeedforwardLayer(ActivationFunction thresholdFunction,
                 int neuronCount)
        {
            this.fire = new double[neuronCount];
            this.activationFunction = thresholdFunction;
        }

        /// <summary>
        /// Construct this layer with a sigmoid threshold function.
        /// </summary>
        /// <param name="neuronCount">How many neurons in this layer.</param>
        public FeedforwardLayer(int neuronCount)
            : this(new ActivationSigmoid(), neuronCount)
        {
        }

        /// <summary>
        /// Clone the structure of this layer, but do not copy any matrix data.
        /// </summary>
        /// <returns>The cloned layer.</returns>
        public FeedforwardLayer CloneStructure()
        {
            return new FeedforwardLayer(this.activationFunction, this.NeuronCount);
        }

        /// <summary>
        /// Compute the outputs for this layer given the input pattern.
        /// The output is also stored in the fire instance variable.
        /// </summary>
        /// <param name="pattern">The input pattern.</param>
        /// <returns>The output from this layer.</returns>
        public double[] ComputeOutputs(double[] pattern)
        {
            int i;
            if (pattern != null)
            {
                for (i = 0; i < this.NeuronCount; i++)
                {
                    SetFire(i, pattern[i]);
                }
            }

            Matrix.Matrix inputMatrix = CreateInputMatrix(this.fire);

            for (i = 0; i < this.next.NeuronCount; i++)
            {
                Matrix.Matrix col = this.matrix.getCol(i);
                double sum = MatrixMath.DotProduct(col, inputMatrix);

                this.next.SetFire(i, this.activationFunction.ActivationFunction(sum));
            }

            return this.fire;
        }

        /// <summary>
        /// Take a simple double array and turn it into a matrix that can be used to
        /// calculate the results of the input array. Also takes into account the
        /// threshold.
        /// </summary>
        /// <param name="pattern"></param>
        /// <returns></returns>
        private Matrix.Matrix CreateInputMatrix(double[] pattern)
        {
            Matrix.Matrix result = new Matrix.Matrix(1, pattern.Length + 1);
            for (int i = 0; i < pattern.Length; i++)
            {
                result[0, i] = pattern[i];
            }

            // add a "fake" first column to the input so that the threshold is
            // always multiplied by one, resulting in it just being added.
            result[0, pattern.Length] = 1;

            return result;
        }

        /// <summary>
        /// Get the output from an individual neuron.
        /// </summary>
        /// <param name="index">The neuron specified.</param>
        /// <returns>The output from the specified neuron.</returns>
        public double GetFire(int index)
        {
            return this.fire[index];
        }

        /// <summary>
        /// Determine if this layer has a matrix.
        /// </summary>
        /// <returns>True if this layer has a matrix.</returns>
        public bool HasMatrix()
        {
            return this.matrix != null;
        }

        /// <summary>
        /// Determine if this is a hidden layer.
        /// </summary>
        /// <returns>True if this is a hidden layer.</returns>
        public bool IsHidden()
        {
            return ((this.next != null) && (this.previous != null));
        }

        /// <summary>
        /// Determine if this is an input layer.
        /// </summary>
        /// <returns>True if this is an input layer.</returns>
        public bool IsInput()
        {
            return (this.previous == null);
        }

        /// <summary>
        /// Determine if this is an output layer.
        /// </summary>
        /// <returns>True if this is an output layer.</returns>
        public bool IsOutput()
        {
            return (this.next == null);
        }

        /// <summary>
        /// Prune one of the neurons from this layer. Remove all entries in this
        /// weight matrix and other layers.
        /// </summary>
        /// <param name="neuron">The neuron to prune. Zero specifies the first neuron.</param>
        public void Prune(int neuron)
        {
            // delete a row on this matrix
            if (this.matrix != null)
            {
                this.LayerMatrix = (MatrixMath.DeleteRow(this.matrix, neuron));
            }

            // delete a column on the previous
            FeedforwardLayer previous = this.Previous;
            if (previous != null)
            {
                if (previous.LayerMatrix != null)
                {
                    previous.LayerMatrix =  (MatrixMath.DeleteCol(previous.LayerMatrix,
                            neuron));
                }
            }
        }

        /// <summary>
        /// Reset the weight matrix and threshold values to random numbers between -1
        /// and 1.
        /// </summary>
        public void Reset()
        {
            if (this.matrix != null)
            {
                this.matrix.Ramdomize(-1, 1);
            }
        }


        /// <summary>
        /// Set the last output value for the specified neuron.
        /// </summary>
        /// <param name="index">The specified neuron.</param>
        /// <param name="f">The fire value for the specified neuron.</param>
        public void SetFire(int index, double f)
        {
            this.fire[index] = f;
        }

        /// <summary>
        /// Produce a string form of the layer.
        /// </summary>
        /// <returns>The string form of the layer.</returns>
        override public String ToString()
        {
            StringBuilder result = new StringBuilder();
            result.Append("[FeedforwardLayer: Neuron Count=");
            result.Append(this.NeuronCount);
            result.Append("]");
            return result.ToString();
        }
    }
}
