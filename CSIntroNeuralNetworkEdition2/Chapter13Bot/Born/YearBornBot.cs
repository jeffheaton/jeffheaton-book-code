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
using Chapter13Bot.Train;
using HeatonResearchNeural.Util;

namespace Chapter13Bot.Born
{
    /// <summary>
    /// The actual bot that will look up when someone was born.
    /// </summary>
    public class YearBornBot : ScanReportable
    {
        public const bool LOG = true;
        
        /// <summary>
        /// The search object to use.
        /// </summary>
        static YahooSearch search;


        private FeedforwardNetwork network;

        private WordHistogram histogram;

        /// <summary>
        /// This map stores a mapping between a year, and how many times that year
        /// has come up as a potential birth year.
        /// </summary>
        private IDictionary<int, int> results = new Dictionary<int, int>();

        public YearBornBot()
        {
            this.network = (FeedforwardNetwork)SerializeObject
                    .Load(Config.FILENAME_WHENBORN_NET);
            this.histogram = (WordHistogram)SerializeObject
                    .Load(Config.FILENAME_HISTOGRAM);
        }


        /// <summary>
        /// Get birth year that occurred the largest number of times.
        /// </summary>
        /// <returns>The birth year that occurred the largest number of times.</returns>
        public int GetResult()
        {
            int result = -1;
            int maxCount = 0;

            ICollection<int> set = this.results.Keys;
            foreach (int year in set)
            {
                int count = this.results[year];
                if (count > maxCount)
                {
                    result = year;
                    maxCount = count;
                }
            }

            return result;
        }


        private void IncreaseYear(int year)
        {
            int count;
            if (!results.ContainsKey(year))
            {
                count = 1;
            }
            else
            {
                count = results[year];
                count++;
            }
            this.results[year] = count;
        }

        /// <summary>
        /// This method is called to determine the birth year for a person. It
        /// obtains 100 web pages that Yahoo returns for that person. Each of these
        /// pages is then searched for the birth year of that person. Which ever year
        /// is selected the largest number of times is selected as the birth year.
        /// </summary>
        /// <param name="name">The name of the person you are seeing the birth year for.</param>
        public void Process(String name)
        {
            search = new YahooSearch();

            if (YearBornBot.LOG)
            {
                Console.WriteLine("Getting search results form Yahoo.");
            }
            ICollection<Uri> c = search.Search(name);
            int i = 0;

            if (YearBornBot.LOG)
            {
                Console.WriteLine("Scanning URL's from Yahoo.");
            }
            foreach (Uri u in c)
            {

                i++;
                Text.CheckURL(this, u, 0);

            }

            int resultYear = GetResult();
            if (resultYear == -1)
            {
                Console.WriteLine("Could not determine when " + name
                        + " was born.");
            }
            else
            {
                Console.WriteLine(name + " was born in " + resultYear);
            }

        }

        public void ReceiveBadSentence(String sentence)
        {
            // don't care at this point, bad sentences only matter for training

        }

        public void ReceiveGoodSentence(String sentence)
        {
            if (this.histogram.Count(sentence) >= Config.MINIMUM_WORDS_PRESENT)
            {
                double[] compact = this.histogram.Compact(sentence);
                int year = Text.ExtractYear(sentence);

                double[] output = this.network.ComputeOutputs(compact);

                if (output[0] > 0.8)
                {
                    IncreaseYear(year);
                    Console.WriteLine(year + "-" + output[0] + ":" + sentence);
                }
            }

        }
    }
}
