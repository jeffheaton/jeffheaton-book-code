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
using System.Xml;
using System.Web;

namespace Chapter13Bot
{
    public class YahooSearch
    {
        private ICollection<Uri> result = new List<Uri>();

        private void HandleResult(XmlElement parent)
        {
            foreach (XmlNode childNode in parent.ChildNodes)
            {
                if (childNode is XmlElement)
                {
                    if (childNode.Name.Equals("Url"))
                    {
                        result.Add(new Uri(childNode.InnerText));
                    }
                }
            }
        }

        private void HandleResultSet(XmlElement parent)
        {
            foreach (XmlNode childNode in parent.ChildNodes)
            {
                if (childNode is XmlElement)
                {
                    if (childNode.Name.Equals("Result"))
                    {
                        HandleResult((XmlElement)childNode);
                    }
                }
            }
        }


        private ICollection<Uri> DoSearch(Uri url)
        {
            result.Clear();
            // submit the search
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();

            Stream istream = response.GetResponseStream();

            XmlDocument doc = new XmlDocument();
            doc.Load(istream);

            foreach (XmlNode node in doc.ChildNodes)
            {
                if (node is XmlElement)
                {
                    if (node.Name.Equals("ResultSet"))
                    {
                        HandleResultSet((XmlElement)node);
                    }
                }
            }


            return result;
        }


        public ICollection<Uri> Search(String searchFor)
        {
            ICollection<Uri> result = null;

            StringBuilder url = new StringBuilder();
            url.Append("http://search.yahooapis.com/WebSearchService/V1/webSearch?");
            url.Append("appid=YahooDemo&results=100&query=");
            url.Append(HttpUtility.UrlEncode(searchFor));

            int tries = 0;
            bool done = false;
            while (!done)
            {
                try
                {
                    result = DoSearch(new Uri(url.ToString()));
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
