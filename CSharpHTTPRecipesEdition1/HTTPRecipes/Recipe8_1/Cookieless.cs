using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using System.Net;
using System.IO;
using System.Web;
using HeatonResearch.Spider.HTML;

namespace Recipe8_1
{
    /// <summary>
    /// Recipe #8.1: Cookieless login and parse
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to login to a system and download 
    /// some data.  This site does not use cookies to keep the login
    /// session, but rather passes a session parameter on the URL.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class Cookieless
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
        /// This method is called to log into the system and return 
        /// a session id.  Once you have the session ID you can call
        /// the search function to perform searches.
        /// </summary>
        /// <param name="uid">The user id to use for login.</param>
        /// <param name="pwd">The password to use for login.</param>
        /// <returns>The session id if login was successful, null if it was not.</returns>
        private String Login(String uid, String pwd)
        {
            Uri url = new Uri("http://www.httprecipes.com/1/8/cookieless.php");
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            http.ContentType = "application/x-www-form-urlencoded";
            http.Method = "POST";
            Stream ostream = http.GetRequestStream();

            FormUtility form = new FormUtility(ostream, null);
            form.Add("uid", uid);
            form.Add("pwd", pwd);
            form.Add("action", "Login");
            form.Complete();
            ostream.Close();
            WebResponse response = http.GetResponse();
            response.GetResponseStream();

            String query = response.ResponseUri.Query;
            if (query != null)
            {
                NameValueCollection c = HttpUtility.ParseQueryString(query);

                return c.Get("session");
            }
            else
                return null;
        }

        /// <summary>
        /// Use the session to search for the specified state or capital.  The search
        /// method can be called multiple times per login.
        /// </summary>
        /// <param name="session">The session to use.</param>
        /// <param name="search">The search string to use.</param>
        /// <param name="type">What to search for(s=state,c=capital).</param>
        /// <returns>A list of states or capitals.</returns>
        public List<String> Search(String session, String search, String type)
        {
            String listType = "ul";
            String listTypeEnd = "/ul";
            StringBuilder buffer = new StringBuilder();
            bool capture = false;
            List<String> result = new List<String>();

            // Build the URL.
            MemoryStream mstream = new MemoryStream();
            FormUtility form = new FormUtility(mstream, null);
            form.Add("search", search);
            form.Add("type", type);
            form.Add("action", "Search");
            form.Complete();

            Uri url = new Uri("http://www.httprecipes.com/1/8/menunc.php?session="
                + session);
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            http.ContentType = "application/x-www-form-urlencoded";
            http.Method = "POST";
            Stream ostream = http.GetRequestStream();

            // Perform the post.
            byte[] b = mstream.GetBuffer();
            ostream.Write(b, 0, b.Length);
            ostream.Close();

            // Read the results.
            WebResponse response = http.GetResponse();
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
                            result.Add(buffer.ToString());
                        buffer.Length = 0;
                        capture = true;
                    }
                    else if (String.Compare(tag.Name, "/li", true) == 0)
                    {
                        result.Add(buffer.ToString());
                        buffer.Length = 0;
                        capture = false;
                    }
                    else if (String.Compare(tag.Name, listTypeEnd, true) == 0)
                    {
                        result.Add(buffer.ToString());
                        break;
                    }
                }
                else
                {
                    if (capture)
                        buffer.Append((char)ch);
                }
            }

            return result;
        }

        /// <summary>
        /// Called to login to the site and download a list of states or capitals.
        /// </summary>
        /// <param name="uid">The user id to use for login.</param>
        /// <param name="pwd">The password to use for login.</param>
        /// <param name="search">The search string to use.</param>
        /// <param name="type">What to search for(s=state,c=capital).</param>
        public void Process(String uid, String pwd, String search, String type)
        {
            String session = Login(uid, pwd);
            if (session != null)
            {
                List<String> list = Search(session, search, type);
                foreach (String item in list)
                {
                    Console.WriteLine(item);
                }
            }
            else
            {
                Console.WriteLine("Error logging in.");
            }
        }



        static void Main(string[] args)
        {
            Cookieless cookieless = new Cookieless();
            cookieless.Process("falken", "joshua", "Mi", "s");
        }
    }
}
