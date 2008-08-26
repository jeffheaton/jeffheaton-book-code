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

namespace HeatonResearch.Spider.RSS
{
    /// <summary>
    /// RSSItem: This is the class that holds individual RSS items,
    /// or stories, for the RSS class.
    /// </summary>
    public class RSSItem
    {
        /// <summary>
        /// The title of this item.
        /// </summary>
        public String Title
        {
            get
            {
                return title;
            }
            set
            {
                title = value;
            }
        }

        /// <summary>
        /// The hyperlink to this item.
        /// </summary>
        public String Link
        {
            get
            {
                return link;
            }
            set
            {
                link = value;
            }
        }


        /// <summary>
        /// The description of this item.
        /// </summary>
        public String Description
        {
            get
            {
                return description;
            }
            set
            {
                description = value;
            }
        }

        /// <summary>
        /// The date this item was published.
        /// </summary>
        public DateTime Date
        {
            get
            {
                return date;
            }
            set
            {
                date = value;
            }
        }

        /// <summary>
        /// The title of this item.
        /// </summary>
        private String title;

        /// <summary>
        /// The hyperlink to this item.
        /// </summary>
        private String link;

        /// <summary>
        /// The description of this item.
        /// </summary>
        private String description;

        /// <summary>
        /// The date this item was published.
        /// </summary>
        private DateTime date;


        /// <summary>
        /// Load an item from the specified node.
        /// </summary>
        /// <param name="node">The Node to load the item from.</param>
        public void Load(XmlNode node)
        {
            
            foreach(XmlNode n in node.ChildNodes )
            {
                String name = n.Name;

                if (String.Compare(name, "title", true) == 0)
                    title = n.InnerText;
                else if (String.Compare(name, "link", true) == 0)
                    link = n.InnerText;
                else if (String.Compare(name, "description", true) == 0)
                    description = n.InnerText;
                else if (String.Compare(name, "pubDate", true) == 0)
                {
                    String str = n.InnerText;
                    if (str != null)
                        date = RSS.ParseDate(str);
                }

            }
        }


        /// <summary>
        /// Convert the object to a String.
        /// </summary>
        /// <returns>The object as a String.</returns>
        public override String ToString()
        {
            StringBuilder builder = new StringBuilder();
            builder.Append('[');
            builder.Append("title=\"");
            builder.Append(title);
            builder.Append("\",link=\"");
            builder.Append(link);
            builder.Append("\",date=\"");
            builder.Append(date);
            builder.Append("\"]");
            return builder.ToString();
        }
    }
}
