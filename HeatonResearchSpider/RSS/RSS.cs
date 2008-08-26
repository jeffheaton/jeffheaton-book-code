// The Heaton Research Spider for .Net 
// Copyright 2007 by Heaton Research, Inc.
// 
// From the book:
// 
// HTTP Recipes for C# Bots, ISBN: 0-9773206-7-7
// http://www.heatonresearch.com/articles/series/20/
// 
// This class is released under the:
// GNU Lesser General Public License (LGPL)
// http://www.gnu.org/copyleft/lesser.html
//
using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Net;
using System.IO;

namespace HeatonResearch.Spider.RSS
{
    /// <summary>
    /// RSS: This is the class that actually parses the 
    /// RSS and builds a collection of RSSItems.  To make use
    /// of this class call the load method with a URL that
    /// points to RSS.
    /// </summary>
    public class RSS
    {
        /// <summary>
        /// All of the attributes for this RSS document.
        /// </summary>
        public Dictionary<String, String> Attributes
        {
            get
            {
                return attributes;
            }
        }

        /// <summary>
        /// All RSS items, or stories, found.
        /// </summary>
        public List<RSSItem> Items
        {
            get
            {
                return items;
            }
        }

        /// <summary>
        /// All of the attributes for this RSS document.
        /// </summary>
        private Dictionary<String, String> attributes = new Dictionary<String, String>();

        /// <summary>
        /// All RSS items, or stories, found.
        /// </summary>
        private List<RSSItem> items = new List<RSSItem>();

        /// <summary>
        /// Simple utility function that converts a RSS formatted date
        /// into a C# date.
        /// </summary>
        /// <param name="datestr">A date</param>
        /// <returns>A C# DateTime object.</returns>
        public static DateTime ParseDate(String datestr)
        {
            DateTime date = DateTime.Parse(datestr);
            return date;
        }

        /// <summary>
        /// Load the specified RSS item, or story.
        /// </summary>
        /// <param name="item">A XML node that contains a RSS item.</param>
        private void LoadItem(XmlNode item)
        {
            RSSItem rssItem = new RSSItem();
            rssItem.Load(item);
            items.Add(rssItem);
        }

        /// <summary>
        /// Load the channle node.
        /// </summary>
        /// <param name="channel">A node that contains a channel.</param>
        private void LoadChannel(XmlNode channel)
        {

            foreach (XmlNode node in channel.ChildNodes)
            {
                String nodename = node.Name;
                if (String.Compare(nodename, "item", true) == 0)
                {
                    LoadItem(node);
                }
                else
                {
                    attributes.Remove(nodename);
                    attributes.Add(nodename, channel.InnerText);
                }
            }
        }

        /// <summary>
        /// Load all RSS data from the specified URL.
        /// </summary>
        /// <param name="url">URL that contains XML data.</param>
        public void Load(Uri url)
        {
            WebRequest http = HttpWebRequest.Create(url);
            HttpWebResponse response = (HttpWebResponse)http.GetResponse();
            Stream istream = response.GetResponseStream();

            XmlDocument d = new XmlDocument();
            d.Load(istream);

            foreach (XmlNode node in d.DocumentElement.ChildNodes)
            {

                String nodename = node.Name;

                // RSS 2.0
                if (String.Compare(nodename, "channel", true) == 0)
                {
                    LoadChannel(node);
                }
                // RSS 1.0
                else if (String.Compare(nodename, "item", true) == 0)
                {
                    LoadItem(node);
                }
            }

        }

        /// <summary>
        /// Convert the object to a String.
        /// </summary>
        /// <returns>The object as a String.</returns>
        public override String ToString()
        {
            StringBuilder str = new StringBuilder();

            foreach (String item in attributes.Keys)
            {
                str.Append(item);
                str.Append('=');
                str.Append(attributes[item]);
                str.Append('\n');
            }
            str.Append("Items:\n");
            foreach (RSSItem item in items)
            {
                str.Append(item.ToString());
                str.Append('\n');
            }
            return str.ToString();
        }
    }
}
