using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe9_3
{
    /// <summary>
    /// Recipe #9.3: JavaScript Forms
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to access JavaScript forms.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class JavaScriptForms
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
        /// This method is called once for each table row located, it 
        /// contains a list of all columns in that row.  The method provided
        /// simply prints the columns to the console.
        /// </summary>
        /// <param name="list">Columns that were found on this row.</param>
        private void ProcessTableRow(List<String> list)
        {
            StringBuilder result = new StringBuilder();
            foreach (String item in list)
            {
                if (result.Length > 0)
                    result.Append(",");
                result.Append('\"');
                result.Append(item);
                result.Append('\"');

            }
            Console.WriteLine(result.ToString());
        }

        /// <summary>
        /// This method will download an amortization table for the 
        /// specified parameters.
        /// </summary>
        /// <param name="interest">The interest rate for the loan.</param>
        /// <param name="term">The term(in months) of the loan.</param>
        /// <param name="principle">The principle amount of the loan.</param>
        public void process(double interest, int term, int principle)
        {


            Uri url = new Uri("http://www.httprecipes.com/1/9/loan.php");
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            http.ContentType = "application/x-www-form-urlencoded";
            http.Method = "POST";
            Stream ostream = http.GetRequestStream();



            FormUtility form = new FormUtility(ostream, null);
            form.Add("interest", "" + interest);
            form.Add("term", "" + term);
            form.Add("principle", "" + principle);
            form.Complete();
            ostream.Close();
            WebResponse response = http.GetResponse();

            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            StringBuilder buffer = new StringBuilder();
            List<String> list = new List<String>();
            bool capture = false;

            Advance(parse, "table", 3);

            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (String.Compare(tag.Name, "tr", true) == 0)
                    {
                        list.Clear();
                        capture = false;
                        buffer.Length = 0;
                    }
                    else if (String.Compare(tag.Name, "/tr", true) == 0)
                    {
                        if (list.Count > 0)
                        {
                            ProcessTableRow(list);
                            list.Clear();
                        }
                    }
                    else if (String.Compare(tag.Name, "td", true) == 0)
                    {
                        if (buffer.Length > 0)
                            list.Add(buffer.ToString());
                        buffer.Length = 0;
                        capture = true;
                    }
                    else if (String.Compare(tag.Name, "/td", true) == 0)
                    {
                        list.Add(buffer.ToString());
                        buffer.Length = 0;
                        capture = false;
                    }
                    else if (String.Compare(tag.Name, "/table", true) == 0)
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
            JavaScriptForms parse = new JavaScriptForms();
            parse.process(7.5, 12, 10000);
        }
    }
}
