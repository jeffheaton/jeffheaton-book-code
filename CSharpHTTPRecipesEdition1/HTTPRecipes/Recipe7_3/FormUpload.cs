using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using HeatonResearch.Spider.HTML;

namespace Recipe7_3
{
    /// <summary>
    /// Recipe #7.3: HTTP Upload
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// This recipe shows how to upload a file through a form, using
    /// the HTTP upload.  This is supported using a multipart form.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    class FormUpload
    {
        /// <summary>
        /// Upload a file.
        /// </summary>
        /// <param name="uid">The user id for the form.</param>
        /// <param name="pwd">The password for the form.</param>
        /// <param name="file">The file to upload.</param>
        public void Upload(String uid, String pwd, String file)
        {
            // Get the boundary used for the multipart upload.
            String boundary = FormUtility.getBoundary();

            Uri url = new Uri("http://www.httprecipes.com/1/7/uploader.php");
            WebRequest http = HttpWebRequest.Create(url);
            http.Timeout = 30000;
            // specify that we will use a multipart form
            http.ContentType = "multipart/form-data; boundary=" + boundary;
            http.Method = "POST";
            Stream ostream = http.GetRequestStream();

            // Construct a form.
            FormUtility form = new FormUtility(ostream, boundary);
            form.Add("uid", uid);
            form.Add("pwd", pwd);
            form.AddFile("uploadedfile", file);
            form.Complete();
            ostream.Close();
            
            // Perform the upload.
            WebResponse response = http.GetResponse();
            Stream istream = response.GetResponseStream();
            istream.Close();
        }

        static void Main(string[] args)
        {
            String uid = "";
            String pwd = "";
            String filename = "";

            if (args.Length < 3)
            {
                Console.WriteLine("Usage:");
                Console.WriteLine("Recipe7_3 [User ID] [Password] [File to upload]");
            }
            else
            {
                uid = args[0];
                pwd = args[1];
                filename = args[2];

                FormUpload upload = new FormUpload();
                upload.Upload(uid, pwd, filename);
            }
        }
    }
}
