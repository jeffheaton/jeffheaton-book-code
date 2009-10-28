using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe6_7
{
    /// <summary>
    /// Recipe #6.7: Extract Across Several Linked Pages
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse a list that is broken 
    /// across several pages with a next and previous button.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ExtractPartial
    {
        /// <summary>
        /// This method downloads the specified URL into a C#
        /// String. This is a very simple method, that you can
        /// reused anytime you need to quickly grab all data from
        /// a specific URL.
        /// </summary>
        /// <param name="url">The URL to download.</param>
        /// <returns>The contents of the URL that was downloaded.</returns>
        public String DownloadPage(Uri url)
        {
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            StreamReader stream = new StreamReader(response.GetResponseStream(), System.Text.Encoding.ASCII);

            String result = stream.ReadToEnd();

            response.Close();
            stream.Close();
            return result;
        }

        /// <summary>
        /// This method is very useful for grabbing information from a
        /// HTML page.  It extracts text from between two tokens, the
        /// tokens need not be case sensitive.
        /// </summary>
        /// <param name="str">The string to extract from.</param>
        /// <param name="token1">The text, or tag, that comes before the desired text</param>
        /// <param name="token2">The text, or tag, that comes after the desired text</param>
        /// <param name="count">Which occurrence of token1 to use, 1 for the first</param>
        /// <returns></returns>
        public String ExtractNoCase(String str, String token1, String token2,
            int count)
        {
            int location1, location2;

            // convert everything to lower case
            String searchStr = str.ToLower();
            token1 = token1.ToLower();
            token2 = token2.ToLower();

            // now search
            location1 = location2 = 0;
            do
            {
                location1 = searchStr.IndexOf(token1, location1 + 1);

                if (location1 == -1)
                    return null;

                count--;
            } while (count > 0);

            // return the result from the original string that has mixed
            // case
            location1 += token1.Length;
            location2 = str.IndexOf(token2, location1 + 1);
            if (location2 == -1)
                return null;

            return str.Substring(location1, location2 - location1);
        }


        /// <summary>
        /// Called to process each individual item found.
        /// </summary>
        /// <param name="officialSite">The official site for this state.</param>
        /// <param name="flag">The flag for this state.</param>
        private void ProcessItem(Uri officialSite, Uri flag)
        {
            StringBuilder result = new StringBuilder();
            result.Append("\"");
            result.Append(officialSite.ToString());
            result.Append("\",\"");
            result.Append(flag.ToString());
            result.Append("\"");
            Console.WriteLine(result.ToString());
        }

        
        /// <summary>
        /// Called to process each partial page.
        /// </summary>
        /// <param name="url">The URL of the partial page.</param>
        /// <returns>Returns the next partial page, or null if no more.</returns>
        public Uri Process(Uri url)
        {
            Uri result = null;
            StringBuilder buffer = new StringBuilder();
            String value = "";
            String src = "";

            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            bool first = true;

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "a", true) == 0)
                    {
                        buffer.Length = 0;
                        value = tag["href"];
                        Uri u = new Uri(url, value.ToString());
                        value = u.ToString();
                        src = null;
                    }
                    else if (String.Compare(tag.Name, "img", true) == 0)
                    {
                        src = tag["src"];
                    }
                    else if (String.Compare(tag.Name, "/a", true) == 0)
                    {
                        if (String.Compare(buffer.ToString(), "[Next 5]", true) == 0)
                        {
                            result = new Uri(url, value);
                        }
                        else if (src != null)
                        {
                            if (!first)
                            {
                                Uri urlOfficial = new Uri(url, value);
                                Uri urlFlag = new Uri(url, src);
                                ProcessItem(urlOfficial, urlFlag);
                            }
                            else
                                first = false;
                        }
                    }
                }
                else
                {
                    buffer.Append((char)ch);
                }
            }

            return result;
        }

        /// <summary>
        /// Called to download the state information from several partial pages.
        /// Each page displays only 5 of the 50 states, so it is necessary to link
        /// each partial page together.  THis method calls "process" which will process
        /// each of the partial pages, until there is no more data.
        /// </summary>
        public void Process()
        {
            Uri url = new Uri("http://www.httprecipes.com/1/6/partial.php");
            do
            {
                url = Process(url);
            } while (url != null);

        }

        static void Main(string[] args)
        {
            ExtractPartial parse = new ExtractPartial();
            parse.Process();
        }
    }
}
