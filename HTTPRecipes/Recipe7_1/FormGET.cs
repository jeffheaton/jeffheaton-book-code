using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe7_1
{
    /// <summary>
    /// Recipe #7.1: Parse List from a Form GET
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to parse a list, obtained from a FORM GET.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class FormGET
    {
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
                    if (String.Compare(parse.Tag.Name, tag, true) == 0)
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
        /// Handle each list item, as it is found.
        /// </summary>
        /// <param name="item">The item to be processed.</param>
        private void ProcessItem(String item)
        {
            Console.WriteLine(item.Trim());
        }

        /// <summary>
        /// Access the website and perform a search for either states or capitals.
        /// </summary>
        /// <param name="search">A search string.</param>
        /// <param name="type">What to search for(s=state, c=capital)</param>
        public void Process(String search, String type)
        {
            String listType = "ul";
            String listTypeEnd = "/ul";
            StringBuilder buffer = new StringBuilder();
            bool capture = false;

            // Build the URL.
            MemoryStream mstream = new MemoryStream();
            FormUtility form = new FormUtility(mstream, null);
            form.Add("search", search);
            form.Add("type", type);
            form.Add("action", "Search");
            form.Complete();

            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();

            String str = enc.GetString(mstream.GetBuffer());
            String surl = "http://www.httprecipes.com/1/7/get.php?" + str;
            Uri url = new Uri(surl);
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);

            // Parse from the URL.

            Advance(parse, listType, 0);

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
                        ProcessItem(buffer.ToString());
                        buffer.Length = 0;
                        capture = false;
                    }
                    else if (String.Compare(tag.Name, listTypeEnd, true) == 0)
                    {
                        ProcessItem(buffer.ToString());
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
            FormGET parse = new FormGET();
            parse.Process("Mi", "s");
        }
    }
}
