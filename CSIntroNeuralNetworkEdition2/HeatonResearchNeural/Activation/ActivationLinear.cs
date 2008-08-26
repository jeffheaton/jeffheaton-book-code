using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Exception;

namespace HeatonResearchNeural.Activation
{
    /// <summary>
    /// ActivationLinear: The Linear layer is really not an activation function 
    /// at all.  The input is simply passed on, unmodified, to the output.
    /// This activation function is primarily theoretical and of little actual
    /// use.  Usually an activation function that scales between 0 and 1 or
    /// -1 and 1 should be used.
    /// </summary>
    [Serializable]
    public class ActivationLinear : ActivationFunction
    {
        /// <summary>
        /// A activation function for a neural network.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double ActivationFunction(double d)
        {
            return d;
        }

        /// <summary>
        /// Performs the derivative function of the activation function function on the input.  There is 
        /// no derivative function for the linear activation function, so this method throws an error.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        public double DerivativeFunction(double d)
        {
            throw new NeuralNetworkError("Can't use the linear activation function where a derivative is required.");
        }
    }
}
