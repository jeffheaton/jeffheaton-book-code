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

namespace Encog.Neural.Activation
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
