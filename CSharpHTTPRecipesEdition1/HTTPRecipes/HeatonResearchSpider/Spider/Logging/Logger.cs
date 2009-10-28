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
using System.IO;
using System.Threading;

namespace HeatonResearch.Spider.Logging
{
    /// <summary>
    /// Logger: Provides basic logging for the spider.
    /// </summary>
    public class Logger
    {
        /// <summary>
        /// The logging levels.
        /// </summary>
        public enum Level
        {
            /// <summary>
            /// Debugging information.
            /// </summary>
            DEBUG = 0,
            /// <summary>
            /// General information about the spider's progress.
            /// </summary>
            INFO = 1, 
            /// <summary>
            /// Errors that were encountered, but will not halt the
            /// spider.
            /// </summary>
            ERROR = 2, 
            /// <summary>
            /// Critical errors.
            /// </summary>
            CRITICAL = 3
        };

        /// <summary>
        /// If the filename is null, then the log is not recorded to a file.
        /// If a filename is specified then the log will be recorded to that file.
        /// </summary>
        public String Filename
        {
            get
            {
                return filename;
            }
            set
            {
                filename = value;
            }
        }

        /// <summary>
        /// Give a value of true to record logging to the console.
        /// </summary>
        public bool Console
        {
            get
            {
                return console;
            }
            set
            {
                console = value;
            }
        }

        /// <summary>
        /// The level at which logging should be recorded, anything below this level will not be logged.
        /// </summary>
        public Level LogLevel
        {
            get
            {
                return level;
            }
            set
            {
                level = value;
            }
        }

        private String filename;
        private bool console = false;
        private Level level = Level.INFO;

        /// <summary>
        /// Delete the log file.
        /// </summary>
        public void Clear()
        {
            if (filename != null)
                File.Delete(filename);
        }

        /// <summary>
        /// Record a line of text into the log.
        /// </summary>
        /// <param name="level">The level for this entry.</param>
        /// <param name="str">The string to record.</param>
        public void Log(Level level, String str)
        {
            Monitor.Enter(this);
            try
            {
                if (level < this.level)
                    return;

                StringBuilder builder = new StringBuilder();
                DateTime now = DateTime.Now;
                builder.Append(now.ToString());
                builder.Append(':');
                if (level == Level.CRITICAL)
                    builder.Append("CRITICAL");
                else if (level == Level.DEBUG)
                    builder.Append("DEBUG");
                else if (level == Level.ERROR)
                    builder.Append("ERROR");
                else if (level == Level.INFO)
                    builder.Append("INFO");
                builder.Append(':');
                builder.Append(Thread.CurrentThread.ManagedThreadId);
                builder.Append(':');
                builder.Append(str);

                if (filename != null)
                {
                    FileStream fs = new FileStream(filename, FileMode.OpenOrCreate | FileMode.Append);
                    StreamWriter writer = new StreamWriter(fs);

                    writer.WriteLine(builder.ToString());
                    writer.Close();
                    fs.Close();

                }
                if (console)
                    System.Console.WriteLine(builder.ToString());
            }
            finally
            {
                Monitor.Exit(this);
            }


        }

        /// <summary>
        /// Record an exception into the log.
        /// </summary>
        /// <param name="level">The level for this entry.</param>
        /// <param name="str">A string to describe the entry.</param>
        /// <param name="e">The exception.</param>
        public void Log(Level level, String str, Exception e)
        {
            StringBuilder builder = new StringBuilder();
 
            builder.Append(str);
            builder.Append(Environment.NewLine);
            builder.Append(e.ToString());
            builder.Append(Environment.NewLine);
            builder.Append(e.StackTrace);
            Log(level, builder.ToString());
        }
    }
}
