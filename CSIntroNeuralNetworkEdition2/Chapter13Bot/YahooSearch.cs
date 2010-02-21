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
using System.Net;
using System.IO;
using System.Threading;

using HeatonResearch.Spider.HTML;

namespace Chapter13Bot
{
    public class YahooSearch
    {

        private ICollection<Uri> DoSearch(Uri url)
        {

            ICollection<Uri> result = new List<Uri>();
            // submit the search
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();
            ParseHTML parse = new ParseHTML(istream);
            StringBuilder buffer = new StringBuilder();
            bool capture = false;

            // parse the results
            int ch;
            while ((ch = parse.Read()) != -1)
            {
                if (ch == 0)
                {
                    HTMLTag tag = parse.Tag;
                    if (tag.Name.Equals("Uri", StringComparison.CurrentCultureIgnoreCase))
                    {
                        buffer.Length = 0;
                        capture = true;
                    }
                    else if (tag.Name.Equals("/Uri", StringComparison.CurrentCultureIgnoreCase))
                    {
                        result.Add(new Uri(buffer.ToString()));
                        buffer.Length = 0;
                        capture = false;
                    }
                }
                else
                {
                    if (capture)
                    {
                        buffer.Append((char)ch);
                    }
                }
            }
            return result;
        }

        /// <summary>
        /// Perform a Yahoo search, return a list of url's.
        /// </summary>
        /// <param name="searchFor">What to search for.</param>
        /// <returns>The urls found.</returns>
        public ICollection<Uri> Search(String searchFor)
        {
            ICollection<Uri> result = null;

            // build the Uri
            MemoryStream mstream = new MemoryStream();
            FormUtility form = new FormUtility(mstream, null);
            form.Add("appid", "YahooDemo");
            form.Add("results", "100");
            form.Add("query", searchFor);
            form.Complete();

            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();

            String str = enc.GetString(mstream.GetBuffer());

            Uri Uri = new Uri(
                   "http://search.yahooapis.com/WebSearchService/V1/webSearch?"
                           + str);

            int tries = 0;
            bool done = false;
            while (!done)
            {
                try
                {
                    result = DoSearch(Uri);
                    done = true;
                }
                catch (IOException e)
                {
                    if (tries == 5)
                    {
                        throw (e);
                    }
                    Thread.Sleep(5000);

                }
                tries++;
            }

            return result;

        }
    }
}
