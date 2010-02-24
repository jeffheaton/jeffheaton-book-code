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
using HeatonResearchNeural.Util;
using HeatonResearchNeural.Feedforward.Train.Backpropagation;

namespace Chapter13Bot.Train
{
    /// <summary>
    /// Train the bot with the data gathered.
    /// </summary>
    public class TrainBot
    {
        private int sampleCount;
        private CommonWords common;
        private double[][] input;
        private double[][] ideal;
        private FeedforwardNetwork network;
        private AnalyzeSentences goodAnalysis;
        private AnalyzeSentences badAnalysis;
        WordHistogram histogramGood;
        WordHistogram histogramBad;

        private TrainingSet trainingSet;

        public TrainBot()
        {
            this.common = new CommonWords(Config.FILENAME_COMMON_WORDS);
            this.trainingSet = new TrainingSet();
        }

        private void AllocateTrainingSets()
        {
            this.input = new double[this.sampleCount][];
            this.ideal = new double[this.sampleCount][];

            for (int i = 0; i < this.sampleCount; i++)
            {
                this.input[i] = new double[Config.INPUT_SIZE];
                this.ideal[i] = new double[Config.OUTPUT_SIZE];
            }
        }

        private void CopyTrainingSets()
        {
            int index = 0;
            // first the input
            foreach (double[] array in this.trainingSet.Input)
            {
                Array.Copy(array, this.input[index], array.Length);
                index++;
            }
            index = 0;
            // second the ideal
            foreach (double[] array in this.trainingSet.Ideal)
            {
                Array.Copy(array, this.ideal[index], array.Length);
                index++;
            }

        }

        public void Process()
        {
            this.network = NetworkUtil.CreateNetwork();
            Console.WriteLine("Preparing training sets...");
            this.common = new CommonWords(Config.FILENAME_COMMON_WORDS);
            this.histogramGood = new WordHistogram(this.common);
            this.histogramBad = new WordHistogram(this.common);

            // load the good words
            this.histogramGood.BuildFromFile(Config.FILENAME_GOOD_TRAINING_TEXT);
            this.histogramGood.BuildComplete();

            // load the bad words
            this.histogramBad.BuildFromFile(Config.FILENAME_BAD_TRAINING_TEXT);
            this.histogramBad.BuildComplete();

            // remove low scoring words
            this.histogramGood
                    .RemoveBelow((int)this.histogramGood.CalculateMean());
            this.histogramBad.RemovePercent(0.99);

            // remove common words
            this.histogramGood.RemoveCommon(this.histogramBad);

            this.histogramGood.Trim(Config.INPUT_SIZE);

            this.goodAnalysis = new AnalyzeSentences(this.histogramGood,
                    Config.INPUT_SIZE);
            this.badAnalysis = new AnalyzeSentences(this.histogramGood,
                    Config.INPUT_SIZE);

            this.goodAnalysis.Process(this.trainingSet, 0.9,
                    Config.FILENAME_GOOD_TRAINING_TEXT);
            this.badAnalysis.Process(this.trainingSet, 0.1,
                    Config.FILENAME_BAD_TRAINING_TEXT);

            this.sampleCount = this.trainingSet.Ideal.Count;
            Console.WriteLine("Processing " + this.sampleCount + " training sets.");

            AllocateTrainingSets();

            CopyTrainingSets();

            TrainNetworkBackpropBackprop();
            SerializeObject.Save(Config.FILENAME_WHENBORN_NET, this.network);
            SerializeObject.Save(Config.FILENAME_HISTOGRAM, this.histogramGood);
            Console.WriteLine("Training complete.");

        }

        private void TrainNetworkBackpropBackprop()
        {
            HeatonResearchNeural.Feedforward.Train.Train train = new Backpropagation(this.network, this.input,
                   this.ideal, 0.7, 0.5);

            int epoch = 1;

            do
            {
                train.Iteration();
                Console.WriteLine("Backprop:Iteration #" + epoch + " Error:"
                        + train.Error);
                epoch++;
            } while ((train.Error > Config.ACCEPTABLE_ERROR));
        }
    }
}
