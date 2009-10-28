using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using System.Xml;

namespace Recipe10_2
{
    /// <summary>
    /// Recipe #10.2: XML AJAX
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to access data from an AJAX website.
    /// The data that will be extracted is XML.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class AjaxXML
    {
        /// <summary>
        /// Obtains a XML node.  Specify a name such as "state.name"
        /// to nest several layers of nodes.
        /// </summary>
        /// <param name="e">The parent node.</param>
        /// <param name="name">The child node to search for. Specify levels with .'s.</param>
        /// <returns>Returns the node found.</returns>
        private XmlNode GetXMLNode(XmlNode e, String name)
        {
            char[] split = { '.' };
            String[] tok = name.Split(split);
            XmlNode node = e;
            foreach (String currentName in tok)
            {
                XmlNodeList list = node.ChildNodes;
                int len = list.Count;
                for (int i = 0; i < len; i++)
                {
                    XmlNode n = list[i];
                    if (n.Name.Equals(currentName))
                    {
                        node = n;
                        break;
                    }
                }
            }
            return node;
        }


        /// <summary>
        /// Obtain the specified XML attribute from the specified node.
        /// </summary>
        /// <param name="e">The XML node to obtain an attribute from.</param>
        /// <param name="name">The name of the attribute.</param>
        /// <returns>Returns the value of the attribute.</returns>
        private String GetXMLAttribute(XmlNode e, String name)
        {
            XmlNamedNodeMap map = e.Attributes;
            XmlNode attr = map.GetNamedItem(name);
            return attr.Value;
        }


        /// <summary>
        /// Download the information for the specified state.  This bot uses
        /// a AJAX web site to obtain the XML message.
        /// </summary>
        /// <param name="state">The state code to look for(i.e. MO).</param>
        public void Process(String state)
        {
            Uri url = new Uri("http://www.httprecipes.com/1/10/request.php");

            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            http.Method = "POST";

            String request = "<request type=\"state\"><code>" + state + "</code></request>";

            http.Timeout = 30000;
            http.ContentType = "application/x-www-form-urlencoded";
            http.Method = "POST";
            Stream ostream = http.GetRequestStream();

            System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
            byte[] b = enc.GetBytes(request);
            ostream.Write(b, 0, b.Length);
            ostream.Close();
            WebResponse response = http.GetResponse();
            Stream istream = response.GetResponseStream();

            XmlDocument d = new XmlDocument();
            d.Load(istream);

            XmlElement e = d.DocumentElement;
            XmlNode stateNode = GetXMLNode(e, "state");
            String id = GetXMLAttribute(stateNode, "id");
            Console.WriteLine("State Name:" + GetXMLNode(e, "state.name").InnerText);
            Console.WriteLine("Code:" + GetXMLNode(e, "state.code").InnerText);
            Console.WriteLine("Capital:" + GetXMLNode(e, "state.capital").InnerText);
            Console.WriteLine("URL:" + GetXMLNode(e, "state.url").InnerText);
            Console.WriteLine("ID:" + id);
        }


        static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Console.WriteLine("Usage: Recipe10_2 [state code, i.e. MO]");
            }
            else
            {
                AjaxXML d = new AjaxXML();
                d.Process(args[0]);
            }
        }
    }
}
