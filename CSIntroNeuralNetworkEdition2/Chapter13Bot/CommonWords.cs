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

namespace Chapter13Bot
{
    /// <summary>
    /// Holds a list of common words in the English
    /// language.
    /// </summary>
    [Serializable]
    public class CommonWords
    {
        private IDictionary<String, int> words;

        public CommonWords(String filename)
        {
            this.words = new Dictionary<String, int>();

            TextReader reader = new StreamReader(filename);
            String line;

            int index = 0;
            while ((line = reader.ReadLine()) != null)
            {
                this.words[line.Trim().ToLower()] =  index++;
            }

            reader.Close();
        }

        public int GetWordIndex(String word)
        {
            return this.words[word.ToLower()];
        }

        public bool IsCommonWord(String word)
        {
            return (this.words.ContainsKey(word.ToLower()));
        }
    }
}
