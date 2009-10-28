using System;
using System.Collections.Generic;
using System.Windows.Forms;
using HeatonResearch.Spider;
using System.Data.OleDb;

namespace Recipe13_4
{
    /// <summary>
    /// Recipe #13.4: Spider Stats
    /// Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
    ///
    /// HTTP Programming Recipes for C# Bots
    /// ISBN: 0-9773206-7-7
    /// http://www.heatonresearch.com/articles/series/20/
    ///
    /// The recipe displays updating statistics for a database
    /// based spider.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    static class SpiderStats
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(String[] args)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            MainForm form = new MainForm();
            if (args.Length < 1)
            {
                MessageBox.Show("Please pass a path to a spider configuration file as an argument to this program (i.e. Recipe13_4 c:\\spider.conf).", "Heaton Research Spider");
                return;
            }

            SpiderOptions options = new SpiderOptions();
            options.Load(args[0]);
            form.Options = options;
            
            Application.Run(form);
        }
    }
}