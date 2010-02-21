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
using System.Threading;
using System.IO;

using HeatonResearchNeural.Util;

namespace Chapter13Bot.Gather
{
    /// <summary>
    /// Gather training data for the neural network.
    /// </summary>
    public class GatherForTrain : ScanReportable
    {
        public const bool LOG = true;
        public const int THREAD_POOL_SIZE = 20;

        private ICollection<String> trainingDataGood = new List<String>();
        private ICollection<String> trainingDataBad = new List<String>();
        private int currentTask;

        private int totalTasks;

        /// <summary>
        /// This method is called to determine the birth year for a person. It
        /// obtains 100 web pages that Yahoo returns for that person. Each of these
        /// pages is then searched for the birth year of that person. Which ever year
        /// is selected the largest number of times is selected as the birth year.
        /// </summary>
        public void Process()
        {
            ReadCSV famous = new ReadCSV("famous.csv");
            Report("Building training data from list of famous people.");
            DateTime started = new DateTime();

          
            this.totalTasks = 0;
            while (famous.Next())
            {
                String name = famous.Get("Person");
                int year = famous.GetInt("Year");

                CollectionWorker worker = new CollectionWorker(this, name,
                       year);
                worker.Call();

                this.totalTasks++;
            }


            long length = (DateTime.Now - started).Ticks;
            length /= 1000L;
            length /= 60;
            Console.WriteLine("Took " + length
                    + " minutes to collect training data from the Internet.");
            Console.WriteLine("Writing training file");
            WriteTrainingFile();

        }

        public void ReceiveBadSentence(String sentence)
        {
            this.trainingDataBad.Add(sentence);

        }

        public void ReceiveGoodSentence(String sentence)
        {
            this.trainingDataGood.Add(sentence);

        }

        public void Report(String str)
        {
            Console.WriteLine(str);
        }

        public void ReportDone(String str)
        {
            Report(this.currentTask + "/" + this.totalTasks + ":" + str);
            this.currentTask++;
        }

        private void WriteTrainingFile()
        {
            TextWriter o = new StreamWriter(Config.FILENAME_GOOD_TRAINING_TEXT);

            foreach (String str in this.trainingDataGood)
            {
                o.WriteLine(str.Trim());
            }

            o.Close();

            o = new StreamWriter(Config.FILENAME_BAD_TRAINING_TEXT);

            foreach (String str in this.trainingDataBad)
            {
                o.WriteLine(str.Trim());
            }

            o.Close();
        }
    }
}
