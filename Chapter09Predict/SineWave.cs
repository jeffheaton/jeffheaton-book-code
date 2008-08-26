using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train;
using HeatonResearchNeural.Feedforward.Train.Backpropagation;
using HeatonResearchNeural.Feedforward.Train.Anneal;
using HeatonResearchNeural.Activation;
using HeatonResearchNeural.Util;

namespace Chapter09Predict
{
    class SineWave
    {
        public const int ACTUAL_SIZE = 500;
        public const int TRAINING_SIZE = 250;
        public const int INPUT_SIZE = 5;
        public const int OUTPUT_SIZE = 1;
        public const int NEURONS_HIDDEN_1 = 7;
        public const int NEURONS_HIDDEN_2 = 0;
        public const bool USE_BACKPROP = true;


        public static void Main(string[] args)
        {
            SineWave wave = new SineWave();
            wave.run();
        }

        private ActualData actual;
        private double[][] input;

        private double[][] ideal;

        private FeedforwardNetwork network;

        public void createNetwork()
        {
            ActivationFunction threshold = new ActivationTANH();
            this.network = new FeedforwardNetwork();
            this.network.AddLayer(new FeedforwardLayer(threshold, INPUT_SIZE));
            this.network.AddLayer(new FeedforwardLayer(threshold,
                    SineWave.NEURONS_HIDDEN_1));
            if (SineWave.NEURONS_HIDDEN_2 > 0)
            {
                this.network.AddLayer(new FeedforwardLayer(threshold,
                        SineWave.NEURONS_HIDDEN_2));
            }
            this.network.AddLayer(new FeedforwardLayer(threshold, OUTPUT_SIZE));

            this.network.Reset();
        }

        private void display()
        {
            double[] input = new double[SineWave.INPUT_SIZE];
            double[] output = new double[SineWave.OUTPUT_SIZE];

            for (int i = SineWave.INPUT_SIZE; i < SineWave.ACTUAL_SIZE; i++)
            {
                this.actual.getInputData(i - SineWave.INPUT_SIZE, input);
                this.actual.getOutputData(i - SineWave.INPUT_SIZE, output);

                StringBuilder str = new StringBuilder();
                str.Append(i);
                str.Append(":Actual=");
                for (int j = 0; j < output.Length; j++)
                {
                    if (j > 0)
                    {
                        str.Append(',');
                    }
                    str.Append(output[j]);
                }

                double[] predict = this.network.ComputeOutputs(input);

                str.Append(":Predicted=");
                for (int j = 0; j < output.Length; j++)
                {
                    if (j > 0)
                    {
                        str.Append(',');
                    }
                    str.Append(predict[j]);
                }

                str.Append(":Difference=");

                ErrorCalculation error = new ErrorCalculation();
                error.UpdateError(predict, output);
                str.Append(error.CalculateRMS().ToString("N2"));

                Console.WriteLine(str.ToString());
            }
        }

        public void displayTraining()
        {
            for (int i = 0; i < this.input.Length; i++)
            {
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < this.input[0].Length; j++)
                {
                    if (j > 0)
                    {
                        str.Append(',');
                    }
                    str.Append(this.input[i][j]);
                }
                str.Append("=>");
                for (int j = 0; j < this.ideal[0].Length; j++)
                {
                    if (j > 0)
                    {
                        str.Append(',');
                    }
                    str.Append(this.ideal[i][j]);
                }
                Console.WriteLine(str.ToString());
            }

        }

        private void generateActual()
        {
            this.actual = new ActualData(SineWave.ACTUAL_SIZE, SineWave.INPUT_SIZE,
                    SineWave.OUTPUT_SIZE);
        }

        private void generateTrainingSets()
        {
            this.input = new double[TRAINING_SIZE][];
            this.ideal = new double[TRAINING_SIZE][];

            for (int i = 0; i < TRAINING_SIZE; i++)
            {
                this.input[i] = new double[INPUT_SIZE];
                this.ideal[i] = new double[OUTPUT_SIZE];
                this.actual.getInputData(i, this.input[i]);
                this.actual.getOutputData(i, this.ideal[i]);
            }
        }

        public void run()
        {
            generateActual();
            createNetwork();
            generateTrainingSets();

            if (SineWave.USE_BACKPROP)
            {
                trainNetworkBackprop();
            }
            else
            {
                trainNetworkAnneal();
            }
            display();

        }

        private void trainNetworkAnneal()
        {
            // train the neural network
            NeuralSimulatedAnnealing train = new NeuralSimulatedAnnealing(
                   this.network, this.input, this.ideal, 10, 2, 100);

            int epoch = 1;

            do
            {
                train.Iteration();
                Console.WriteLine("Iteration #" + epoch + " Error:"
                        + train.Error);
                epoch++;
            } while ((train.Error > 0.01));
        }

        private void trainNetworkBackprop()
        {
            Train train = new Backpropagation(this.network, this.input,
                   this.ideal, 0.001, 0.1);

            int epoch = 1;

            do
            {
                train.Iteration();
                Console.WriteLine("Iteration #" + epoch + " Error:"
                        + train.Error);
                epoch++;
            } while ((epoch < 5000) && (train.Error > 0.01));
        }
    }
}
