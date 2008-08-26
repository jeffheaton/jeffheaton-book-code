using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Feedforward.Train
{
    public interface Train
    {
        /// <summary>
        /// Get the current best network from the training.
        /// </summary>
        FeedforwardNetwork Network
        {
            get;
        }

        /// <summary>
        /// Get the current error percent from the training.
        /// </summary>
        double Error
        {
            get;
        }

        /// <summary>
        /// Perform one iteration of training.
        /// </summary>
        void Iteration();
    }
}
