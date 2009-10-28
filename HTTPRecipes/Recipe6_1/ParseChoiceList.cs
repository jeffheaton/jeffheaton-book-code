using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Net;
using HeatonResearch.Spider.HTML;

namespace Recipe6_1
{
    /// <summary>
    /// Recipe #6.1: Parse Choice List
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse data from a choice list.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class ParseChoiceList
    {
        /// <summary>
        /// Called for each option item that is found. 
        /// </summary>
        /// <param name="name">The name of the option item.</param>
        /// <param name="value">The value of the option item.</param>
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

        /// <summary>
        /// Process the specified URL and extract the option list there.
        /// </summary>
        /// <param name="url">The URL to process.</param>
        /// <param name="optionList">Which option list to process, zero for first.</param>
        public void Process(Uri url, int optionList)
        {
            String value = "";
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            StringBuilder buffer = new StringBuilder();

            Advance(parse, "select", optionList);

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "option") == 0)
                    {
                        value = tag["value"];
                        buffer.Length = 0;
                    }
                    else if (String.Compare(tag.Name, "/option") == 0)
                    {
                        ProcessOption(buffer.ToString(), value);
                    }
                    else if (String.Compare(tag.Name, "/choice") == 0)
                    {
                        break;
                    }
                }
                else
                {
                    buffer.Append((char)ch);
                }
            }
        }

        /// <summary>
        /// The main method.
        /// </summary>
        /// <param name="args">Not used.</param>
        static void Main(string[] args)
        {
            Uri u = new Uri("http://www.httprecipes.com/1/6/form.php");
            ParseChoiceList parse = new ParseChoiceList();
            parse.Process(u, 1);
        }
    }
}
