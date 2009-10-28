using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using System.Globalization;
using Recipe11_3.com.google.api;

using HeatonResearch.Spider.HTML;

namespace Recipe11_3
{
    /// <summary>
    /// Recipe #11.3: Using the Google API to Find Someone was Born
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to use the Google API to determine
    /// someone's birth year.  This bot uses both the Google API
    /// and regular HTTP programming. 
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class WhenBorn
    {
        /// <summary>
        /// The key that Google provides anyone who uses their API.
        /// </summary>
        static String key;

        /// <summary>
        /// The search object to use.
        /// </summary>
        static GoogleSearchService search;

        /// <summary>
        /// This map stores a mapping between a year, and how many times that year
        /// has come up as a potential birth year.
        /// </summary>
        private Dictionary<int, int> results = new Dictionary<int, int>();


        /// <summary>
        /// Perform a Google search and return the results. Only return
        /// 100 results.
        /// </summary>
        /// <param name="query">What to search for.</param>
        /// <returns>The URL's that google returned for the search.</returns>
        public static List<ResultElement> GetResults(String query)
        {
            List<ResultElement> result = new List<ResultElement>();
            int resultCount = 0;
            int top = 0;

            do
            {
                GoogleSearchResult r = search.doGoogleSearch(key,
                    query, top, 10, false, "", false, "", "latin1", "latin1");
                resultCount = r.resultElements.GetLength(0);

                if (r != null)
                {
                    foreach (ResultElement element in r.resultElements)
                    {
                        result.Add(element);
                    }
                }
                top = top + 10;
                Console.WriteLine("Searching Google: " + top);
            } while ((resultCount >= 10) && (top < 100));

            return result;
        }

        /// <summary>
        /// Examine a sentence and see if it contains the word born
        /// and a number.
        /// </summary>
        /// <param name="sentence">The sentence to search.</param>
        /// <returns>The number that was found.</returns>
        private int ExtractBirth(String sentence)
        {
            bool foundBorn = false;
            Int32 result = -1;
            char[] sep = { ' ' };
            String[] tok = sentence.Split(sep);
            foreach (String word in tok)
            {

                if (String.Compare(word, "born", true) == 0)
                    foundBorn = true;
                else
                {
                    Int32 temp;
                    int.TryParse(word, NumberStyles.Integer, null, out temp);
                    if (temp != 0)
                        result = temp;
                }
            }


            if (!foundBorn)
                result = -1;

            return result;
        }


        /// <summary>
        /// Increase the count for the specified year.
        /// </summary>
        /// <param name="year">The year.</param>
        private void IncreaseYear(int year)
        {
            int count;
            if (!results.ContainsKey(year))
            {
                count = 0;
                results.Add(year, count);
            }
            else
                results[year]++;

        }


        /// <summary>
        /// Check the specified URL for a birth year.  This will occur if one
        /// sentence is found that has the word born, and a numeric value less
        /// than 3000.
        /// </summary>
        /// <param name="url">The URL to check.</param>
        public void CheckURL(Uri url)
        {
            int ch;
            StringBuilder sentence = new StringBuilder();

            try
            {
                WebRequest http = HttpWebRequest.Create(url);
                HttpWebResponse response = (HttpWebResponse)http.GetResponse();
                Stream istream = response.GetResponseStream();
                ParseHTML html = new ParseHTML(istream);
                do
                {
                    ch = html.Read();
                    if ((ch != -1) && (ch != 0))
                    {
                        if (ch == '.')
                        {
                            String str = sentence.ToString();
                            int year = ExtractBirth(str);
                            if ((year > 1) && (year < 3000))
                            {
                                Console.WriteLine("URL supports year: " + year);
                                IncreaseYear(year);
                            }
                            sentence.Length = 0;
                        }
                        else
                            sentence.Append((char)ch);
                    }
                } while (ch != -1);

            }
            catch (WebException)
            {
            }
            catch (IOException)
            {
            }
        }

        /// <summary>
        /// Get birth year that occurred the largest number of times.
        /// </summary>
        /// <returns>The birth year that occurred the largest number of times.</returns>
        public int GetResult()
        {
            int result = -1;
            int maxCount = 0;

            foreach (int year in results.Keys)
            {
                int count = results[year];
                if (count > maxCount)
                {
                    result = year;
                    maxCount = count;
                }
            }

            return result;
        }

        /// <summary>
        /// This method is called to determine the birth year for a person.  It
        /// obtains 100 web pages that Google returns for that person.  Each
        /// of these pages is then searched for the birth year of that person.
        /// Which ever year is selected the largest number of times is selected
        /// as the birth year.
        /// </summary>
        /// <param name="name">The name of the person you are seeing the birth year for.</param>
        public void Process(String name)
        {
            search = new GoogleSearchService();

            Console.WriteLine("Getting search results form Google.");
            List<ResultElement> c = GetResults(name);
            int i = 0;

            Console.WriteLine("Scanning URL's from Google.");
            foreach (ResultElement element in c)
            {
                try
                {
                    i++;
                    Uri u = new Uri(element.URL);
                    Console.WriteLine("Scanning URL: " + i + "/" + c.Count + ":" + u);
                    CheckURL(u);
                }
                catch (IOException)
                {

                }
            }

            int resultYear = GetResult();
            if (resultYear == -1)
            {
                Console.WriteLine("Could not determine when " + name + " was born.");
            }
            else
            {
                Console.WriteLine(name + " was born in " + resultYear);
            }
        }



        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Recipe13_3 [Google Key] [Site to Scan]");
            }
            else
            {
                key = args[0];
                WhenBorn when = new WhenBorn();
                when.Process(args[1]);
            }
        }
    }
}
