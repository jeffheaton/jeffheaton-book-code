using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;

namespace HeatonResearchNeural.Matrix
{

    /// <summary>
    /// MatrixCODEC: The matrix CODEC can encode or decode a matrix
    /// to/from an array of doubles.  This is very useful when the 
    /// neural network must be looked at as an array of doubles for
    /// genetic algorithms and simulated annealing.
    /// </summary>
    public class MatrixCODEC
    {

        /// <summary>
        /// Convert from an array.  Use an array to populate the memory of the neural network.
        /// </summary>
        /// <param name="array">An array that will hold the memory of the neural network.</param>
        /// <param name="network">A neural network to convert to an array.</param>
        public static void ArrayToNetwork(Double[] array,
                FeedforwardNetwork network)
        {

            // copy data to array
            int index = 0;

            foreach (FeedforwardLayer layer in network.Layers)
            {

                // now the weight matrix(if it exists)
                if (layer.Next != null)
                {
                    index = layer.LayerMatrix.FromPackedArray(array, index);
                }
            }
        }

        /// <summary>
        /// Convert to an array. This is used with some training algorithms that
        /// require that the "memory" of the neuron(the weight and threshold values)
        /// be expressed as a linear array.
        /// </summary>
        /// <param name="network">A neural network.</param>
        /// <returns>The memory of the neural network as an array.</returns>
        public static double[] NetworkToArray(FeedforwardNetwork network)
        {
            int size = 0;

            // first determine size
            foreach (FeedforwardLayer layer in network.Layers)
            {
                // count the size of the weight matrix
                if (layer.HasMatrix())
                {
                    size += layer.MatrixSize;
                }
            }

            // allocate an array to hold
            Double[] result = new Double[size];

            // copy data to array
            int index = 0;

            foreach (FeedforwardLayer layer in network.Layers)
            {

                // now the weight matrix(if it exists)
                if (layer.Next != null)
                {

                    Double[] matrix = layer.LayerMatrix.ToPackedArray();
                    for (int i = 0; i < matrix.Length; i++)
                    {
                        result[index++] = matrix[i];
                    }
                }
            }

            return result;
        }

    }

}
