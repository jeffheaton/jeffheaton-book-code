/// Introduction to Neural Networks with C#, 2nd Edition
/// Copyright 2008 by Heaton Research, Inc. 
/// http://www.heatonresearch.com/book/programming-neural-networks-cs-2.html
/// 
/// ISBN-10: 1604390093
/// ISBN-13: 978-1604390094
/// 
/// This class is released under the:
/// GNU Lesser General Public License (LGPL)
/// http://www.gnu.org/copyleft/lesser.html
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Activation;

namespace Chapter13Bot
{
    /// <summary>
    /// Utility class to create neural networks.
    /// </summary>
    public class NetworkUtil
    {
        public static FeedforwardNetwork CreateNetwork()
        {
            ActivationFunction threshold = new ActivationSigmoid();
            FeedforwardNetwork network = new FeedforwardNetwork();
            network.AddLayer(new FeedforwardLayer(threshold, Config.INPUT_SIZE));
            network.AddLayer(new FeedforwardLayer(threshold,
                    Config.NEURONS_HIDDEN_1));
            if (Config.NEURONS_HIDDEN_2 > 0)
            {
                network.AddLayer(new FeedforwardLayer(threshold,
                        Config.NEURONS_HIDDEN_2));
            }
            network.AddLayer(new FeedforwardLayer(threshold, Config.OUTPUT_SIZE));
            network.Reset();
            return network;
        }
    }
}
