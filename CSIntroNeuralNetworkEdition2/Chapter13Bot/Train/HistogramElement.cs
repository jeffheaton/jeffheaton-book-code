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

namespace Chapter13Bot.Train
{
    /// <summary>
    /// One element in the word histogram.
    /// </summary>
    [Serializable]
    public class HistogramElement: IComparable
    {

        public String Word { get; set; }
        public int Count { get; set; }

        public HistogramElement(String word, int count)
        {
            this.Word = word.ToLower();
            this.Count = count;
        }

        public void Increase()
        {
            this.Count++;
        }

        public int CompareTo(object obj)
        {
            HistogramElement o = (HistogramElement)obj;
            int result = o.Count - this.Count;
            if (result == 0)
            {
                return this.Word.CompareTo(o.Word);
            }
            else
            {
                return result;
            }
        }
    }
}
