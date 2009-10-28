using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe6_4
{
    /// <summary>
    /// Recipe #6.4: Parse Links
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse links from an HTML page.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ExtractLinks
    {
        private void ProcessOption(String name, String value)
        {
            StringBuilder result = new StringBuilder();
            result.Append('\"');
            result.Append(name);
            result.Append("\",\"");
            result.Append(value);
            result.Append('\"');
            Console.WriteLine(result.ToString());
        }

        /// <summary>
        /// Process the specified URL.
        /// </summary>
        /// <param name="url">The URL to process.</param>
        /// <param name="optionList">Whcih option list to process.</param>
        public void Process(Uri url, int optionList)
        {
            String value = "";
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            StringBuilder buffer = new StringBuilder();

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "a", true) == 0)
                    {
                        value = tag["href"];
                        Uri u = new Uri(url, value.ToString());
                        value = u.ToString();
                        buffer.Length = 0;
                    }
                    else if (String.Compare(tag.Name, "/a", true) == 0)
                    {
                        ProcessOption(buffer.ToString(), value);
                    }
                }
                else
                {
                    buffer.Append((char)ch);
                }
            }
        }

        static void Main(string[] args)
        {
            Uri u = new Uri("http://www.httprecipes.com/1/6/link.php");
            ExtractLinks parse = new ExtractLinks();
            parse.Process(u, 1);
        }
    }
}
