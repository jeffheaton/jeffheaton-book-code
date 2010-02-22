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
    /// Build a histogram of how many occurrences of
    /// each word.
    /// </summary>
    public class WordHistogram
    {
        private CommonWords common;
        private IDictionary<String, HistogramElement> histogram = new Dictionary<String, HistogramElement>();

        private List<HistogramElement> sorted = new List<HistogramElement>();

        public WordHistogram(CommonWords common)
        {
            this.common = common;
        }

        public void BuildComplete()
        {
            this.sorted.Clear();
            foreach (HistogramElement element in histogram.Values)
                this.sorted.Add(element);
            this.sorted.Sort();

        }

        public void BuildFromFile(Stream istream)
        {
            String line;

            StreamReader sr = new StreamReader(istream);

            while ((line = sr.ReadLine()) != null)
            {
                BuildFromLine(line);
            }

            sr.Close();
        }

        public void BuildFromFile(String filename)
        {
            Stream fis = File.OpenRead(filename);
            BuildFromFile(fis);
            fis.Close();
        }

        public void BuildFromLine(String line)
        {
            String[] tok = line.Split(' ');

            for (int i = 0; i < tok.Length; i++)
            {
                BuildFromWord(tok[i]);
            }
        }

        public void BuildFromWord(String word)
        {
            word = word.Trim().ToLower();
            if (this.common.IsCommonWord(word))
            {
                if (this.histogram.ContainsKey(word))
                {
                    HistogramElement element = this.histogram[word];
                    element = new HistogramElement(word, 0);
                    this.histogram[word] = element;
                    element.Increase();
                }

            }
        }

        public double CalculateMean()
        {
            int total = 0;
            foreach (HistogramElement element in this.sorted)
            {
                total += element.Count;
            }
            return (double)total / (double)this.sorted.Count;
        }

        public double[] Compact(String line)
        {
            double[] result = new double[this.sorted.Count];

            String[] tok = line.Split(' ');

            foreach (String word in tok)
            {
                String word2 = word.ToLower();
                int rank = GetRank(word2);
                if (rank != -1)
                {
                    result[rank] = 0.9;
                }
            }
            return result;
        }

        public int Count(String line)
        {
            int result = 0;
            String[] tok = line.Split(' ');

            foreach (String word in tok)
            {
                String word2 = word.ToLower();
                int rank = GetRank(word2);
                if (rank != -1)
                {
                    result++;
                }
            }
            return result;
        }

        public HistogramElement Get(String word)
        {
            return this.histogram[word.ToLower()];
        }

        public CommonWords Common
        {
            get
            {
                return this.common;
            }
        }

        public int GetRank(String word)
        {
            int result = 0;

            foreach (HistogramElement element in this.sorted)
            {
                if (String.Compare(element.Word, word, true) == 0)
                {
                    return result;
                }
                result++;
            }
            return -1;
        }

        public IList<HistogramElement> Sorted
        {
            get
            {
                return this.sorted;
            }
        }

        public void RemoveBelow(int value)
        {
            Object[] elements = this.sorted.ToArray();
            for (int i = 0; i < elements.Length; i++)
            {
                HistogramElement element = (HistogramElement)elements[i];
                if (element.Count < value)
                {
                    this.histogram.Remove(element.Word);
                    this.sorted.Remove(element);
                }
            }
        }

        public void RemoveCommon(WordHistogram other)
        {
            foreach (HistogramElement element in other.Sorted )
            {
                HistogramElement e = this.Get(element.Word);
                if (e == null)
                {
                    continue;
                }
                this.sorted.Remove(e);
                this.histogram.Remove(element.Word);

            }
        }

        public void RemovePercent(double percent)
        {
            int countdown = (int)(this.sorted.Count * (1 - percent));

            Object[] elements = this.sorted.ToArray();
            for (int i = 0; i < elements.Length; i++)
            {
                countdown--;
                if (countdown < 0)
                {
                    HistogramElement element = (HistogramElement)elements[i];
                    this.histogram.Remove(element.Word);
                    this.sorted.Remove(element);
                }
            }
        }

        public void Trim(int size)
        {

            Object[] elements = this.sorted.ToArray();
            for (int i = 0; i < elements.Length; i++)
            {
                if (i >= size)
                {
                    HistogramElement element = (HistogramElement)elements[i];
                    this.histogram.Remove(element.Word);
                    this.sorted.Remove(element);
                }
            }
        }
    }
}
