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
using System.IO;

namespace Chapter13Bot.Train
{
    /// <summary>
    /// Analyze either good or bad sentences.
    /// </summary>
    public class AnalyzeSentences
    {
        private int sampleCount;
        private int currentSample;
        private WordHistogram histogram;

        public AnalyzeSentences(WordHistogram histogram, int size)
        {
            this.histogram = histogram;
        }

        private void CountLines(String filename)
        {
            this.sampleCount++;
            Stream s = File.OpenRead(filename);
            StreamReader r = new StreamReader(s);

            while (r.ReadLine() != null)
                this.sampleCount++;

            r.Close();
        }

        public void Process(TrainingSet set, double value,
                 String filename)
        {
            CountLines(filename);
            ProcessLines(filename, set, value);
        }

        private void ProcessLine(String line, TrainingSet set,
                 double value)
        {
            if (this.histogram.Count(line) >= Config.MINIMUM_WORDS_PRESENT)
            {
                double[] result = this.histogram.Compact(line);
                set.AddTrainingSet(result, value);
                this.currentSample++;
            }
        }

        private void ProcessLines(String filename, TrainingSet set,
                 double value)
        {
            this.currentSample = 0;
            String line = null;

            Stream s = File.OpenRead(filename);
            StreamReader r = new StreamReader(s);

            while ((line = r.ReadLine()) != null)
            {
                ProcessLine(line, set, value);
            }
            s.Close();
        }
    }
}
