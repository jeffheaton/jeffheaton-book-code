using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Activation
{
    /// <summary>
    /// ActivationFunction: This interface allows various 
    /// activation functions to be used with the feedforward
    /// neural network.  Activation functions are applied
    /// to the output from each layer of a neural network.
    /// Activation functions scale the output into the
    /// desired range. 
    /// </summary>
    public interface ActivationFunction
    {
        /// <summary>
        /// A activation function for a neural network.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        double ActivationFunction(double d);


        /// <summary>
        /// Performs the derivative function of the activation function function on the input.
        /// </summary>
        /// <param name="d">The input to the function.</param>
        /// <returns>The ouput from the function.</returns>
        double DerivativeFunction(double d);
    }
}
