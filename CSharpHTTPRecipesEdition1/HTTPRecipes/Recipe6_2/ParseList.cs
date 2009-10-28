using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe6_2
{

    /// <summary>
    /// Recipe #6.2: Parse List
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse a list.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ParseList
    {
        /// <summary>
        /// Handle each list item, as it is found.
        /// </summary>
        /// <param name="item">The list item that was just found.</param>
        private void ProcessItem(String item)
        {
            Console.WriteLine(item);
        }

        /// <summary>
        /// Advance to the specified HTML tag.
        /// </summary>
        /// <param name="parse">The HTML parse object to use.</param>
        /// <param name="tag">The HTML tag.</param>
        /// <param name="count">How many tags like this to find.</param>
        /// <returns>True if found, false otherwise.</returns>
        private bool Advance(ParseHTML parse, String tag, int count)
        {
            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    if (String.Compare(parse.Tag.Name, tag,true) == 0)
                    {
                        count--;
                        if (count <= 0)
                            return true;
                    }
                }
            }
            return false;
        }

        /**
         * Called to extract a list from the specified URL.
         * @param url The URL to extract the list from.
         * @param listType What type of list, specify its beginning tag (i.e. <UL>)
         * @param optionList Which list to search, zero for first.
         * @throws IOException Thrown if an IO exception occurs.
         */
        public void Process(Uri url, String listType, int optionList)
        {
            String listTypeEnd = listType + "/";
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            StringBuilder buffer = new StringBuilder();
            bool capture = false;

            Advance(parse, listType, optionList);

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "li", true) == 0)
                    {
                        if (buffer.Length > 0)
                            ProcessItem(buffer.ToString());
                        buffer.Length = 0;
                        capture = true;
                    }
                    else if (String.Compare(tag.Name, "/li", true) == 0)
                    {
                        Console.WriteLine(buffer.ToString());
                        ProcessItem(buffer.ToString());
                        buffer.Length = 0;
                        capture = false;
                    }
                    else if (String.Compare(tag.Name, listTypeEnd, true) == 0)
                    {
                        break;
                    }
                }
                else
                {
                    if (capture)
                        buffer.Append((char)ch);
                }
            }
        }

        static void Main(string[] args)
        {
            Uri u = new Uri("http://www.httprecipes.com/1/6/list.php");
            ParseList parse = new ParseList();
            parse.Process(u, "ul", 1);
        }
    }
}
