using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Activation;

namespace Chapter13Bot
{
    public class NetworkUtil
    {
	public static FeedforwardNetwork createNetwork() {
		 ActivationFunction threshold = new ActivationSigmoid();
		 FeedforwardNetwork network = new FeedforwardNetwork();
		network.AddLayer(new FeedforwardLayer(threshold, Config.INPUT_SIZE));
		network.AddLayer(new FeedforwardLayer(threshold,
				Config.NEURONS_HIDDEN_1));
		if (Config.NEURONS_HIDDEN_2 > 0) {
			network.AddLayer(new FeedforwardLayer(threshold,
					Config.NEURONS_HIDDEN_2));
		}
		network.AddLayer(new FeedforwardLayer(threshold, Config.OUTPUT_SIZE));
		network.Reset();
		return network;
	}
    }
}
