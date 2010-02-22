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
using System.Net;

using HeatonResearch.Spider.HTML;

namespace Chapter13Bot
{
    /// <summary>
    /// Text parsing utilities.
    /// </summary>
    public class Text
    {
        /// <summary>
        /// Check the specified URL for a birth year. This will occur if one sentence
        /// is found that has the word born, and a numeric value less than 3000.
        /// </summary>
        /// <param name="report">Object to report to.</param>
        /// <param name="url">The url.</param>
        /// <param name="desiredYear">The desired year.</param>
        public static void CheckURL(ScanReportable report, Uri url,
                 int desiredYear)
        {
            int ch;
            StringBuilder sentence = new StringBuilder();
            String ignoreUntil = null;

            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 10000;
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();            
            Stream istream = response.GetResponseStream();
            ParseHTML html = new ParseHTML(istream);


            do
            {
                ch = html.Read();
                if ((ch != -1) && (ch != 0) && (ignoreUntil == null))
                {
                    if (".?!".IndexOf((char)ch) != -1)
                    {
                        String str = sentence.ToString();
                        int year = Text.ExtractYear(str);

                        if (desiredYear == -1)
                        {
                            // looking for any year
                            if (year != -1)
                            {
                                report.ReceiveGoodSentence(str);
                            }
                        }
                        else
                        {
                            // looking for a specific year
                            if (year == desiredYear)
                            {
                                report.ReceiveGoodSentence(str);
                            }
                            else if (year != -1)
                            {
                                report.ReceiveBadSentence(str);
                            }
                        }
                        sentence.Length = 0;
                    }
                    else if (ch == ' ')
                    {
                        string str = sentence.ToString();
                        if ((sentence.Length > 0)
                                && (str[str.Length - 1] != ' '))
                        {
                            sentence.Append(' ');

                        }
                    }
                    else if ((ch != '\n') && (ch != '\t') && (ch != '\r'))
                    {
                        if ((ch) < 128)
                        {
                            sentence.Append((char)ch);
                        }
                    }
                }
                else if (ch == 0)
                {
                    // clear anything before a body tag
                    if (html.Tag.Name.Equals("body", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("br", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("li", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("p", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("h1", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("h2", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("h3", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("td", StringComparison.CurrentCultureIgnoreCase)
                            || html.Tag.Name.Equals("th", StringComparison.CurrentCultureIgnoreCase))
                    {
                        sentence.Length = 0;
                    }
                    // ignore everything between script and style tags
                    if (ignoreUntil == null)
                    {
                        if (html.Tag.Name.Equals("script", StringComparison.CurrentCultureIgnoreCase))
                        {
                            ignoreUntil = "/script";
                        }
                        else if (html.Tag.Name
                                .Equals("style", StringComparison.CurrentCultureIgnoreCase))
                        {
                            ignoreUntil = "/style";
                        }
                    }
                    else
                    {
                        if (html.Tag.Name.Equals(ignoreUntil, StringComparison.CurrentCultureIgnoreCase))
                        {
                            ignoreUntil = null;
                        }
                    }

                    // add a space after the tag
                    if (sentence.Length > 0)
                    {
                        string str = sentence.ToString();
                        if (str[str.Length - 1] != ' ')
                        {
                            sentence.Append(' ');
                        }
                    }
                }
            } while (ch != -1);

        }

  
        /// <summary>
        /// Examine a sentence and see if it contains the word born and a number.
        /// </summary>
        /// <param name="sentence">The sentence to search.</param>
        /// <returns>The number that was found.</returns>
        public static int ExtractYear(String sentence)
        {
            int result = -1;

            string[] tok = sentence.Split(' ');
            for (int i = 0; i < tok.Length; i++)
            {
                String word = tok[i];

                try
                {
                    result = int.Parse(word);

                    if ((result < 1600) || (result > 2100))
                    {
                        result = -1;
                    }
                }
                catch (FormatException)
                {
                }
            }

            return result;
        }

    }
}
