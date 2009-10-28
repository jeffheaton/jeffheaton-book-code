using System;
using System.Collections.Generic;
using System.Text;
using Recipe11_1.com.google.api;

namespace Recipe11_1
{
    /// <summary>
    /// Recipe #11.1: Using the Google API to Find Links
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to use the Google API to find all
    /// sites that link to a given web site.  This recipe also
    /// scans to see how many links each of them have as well.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class GoogleSearch
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
        /// For the given URL check how many links the URL has.
        /// </summary>
        /// <param name="url">The URL to check.</param>
        /// <returns>The number of links that URL has.</returns>
        public static int getLinkCount(String url)
        {
            int result = 0;

            String query = "link:" + url;
            GoogleSearchResult r = search.doGoogleSearch(key,
                query, 0, 10, false, "", false, "", "latin1", "latin1");
            
            result = r.estimatedTotalResultsCount;
            return result;
        }


        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Recipe11_1 [Google Key] [Site to Scan]");
            }
            else
            {
                key = args[0];
                search = new GoogleSearchService();

                List<ResultElement> c = GetResults("\"" + args[1]
                    + "\"");

                foreach (ResultElement element in c)
                {
                    StringBuilder str = new StringBuilder();
                    str.Append(getLinkCount(element.URL));
                    str.Append(":");
                    str.Append(element.title);
                    str.Append("(");
                    str.Append(element.URL);
                    str.Append(")");
                    Console.WriteLine(str.ToString());
                }
            }
        }
    }
}
