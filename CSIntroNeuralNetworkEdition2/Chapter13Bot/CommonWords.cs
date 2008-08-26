using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Chapter13Bot
{
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
                this.words.Add(line.Trim().ToLower(), index++);
            }

            reader.Close();
        }

        public int getWordIndex(String word)
        {
            return this.words[word.ToLower()];
        }

        public bool isCommonWord(String word)
        {
            return (this.words.ContainsKey(word.ToLower()));
        }
    }
}
