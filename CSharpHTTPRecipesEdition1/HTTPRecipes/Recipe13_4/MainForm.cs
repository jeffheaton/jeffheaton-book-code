using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data.Common;
using System.Data;
using System.Drawing;
using System.Drawing.Text;
using System.Text;
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
    /// Main form for Spider Stats.
    ///
    /// This software is copyrighted. You may use it in programs
    /// of your own, without restriction, but you may not
    /// publish the source code without the author's permission.
    /// For more information on distributing this code, please
    /// visit:
    ///    http://www.heatonresearch.com/hr_legal.php
    /// </summary>
    public partial class MainForm : Form
    {
        /// <summary>
        /// The SpiderOptions to load the database from.
        /// </summary>
        public SpiderOptions Options
        {
            get
            {
                return options;
            }
            set
            {
                this.options = value;
            }
        }


        /// <summary>
        /// Contains information about how to connect to the
        /// database.
        /// </summary>
        private SpiderOptions options;

        /// <summary>
        /// An ADO connection.
        /// </summary>
        private DbConnection connection;

        /// <summary>
        /// Get a count by status.
        /// </summary>
        private const String sqlStatus = "select status,count(*) from spider_workload group by status;";

        /// <summary>
        /// Get the maximum depth
        /// </summary>
        private const String sqlDepth = "SELECT MAX(depth) from spider_workload";

        /// <summary>
        /// Prepared statement for status.
        /// </summary>
        private DbCommand stmtStatus;

        /// <summary>
        /// Prepared statement for depth.
        /// </summary>
        private DbCommand stmtDepth;

        /// <summary>
        /// How many URL's are waiting.
        /// </summary>
        private int waiting;

        /// <summary>
        /// How many URL's are done.
        /// </summary>
        private int done;

        /// <summary>
        /// How many URL's are being processed.
        /// </summary>
        private int processing;

        /// <summary>
        /// How many URL's resulted in an error.
        /// </summary>
        private int error;

        /// <summary>
        /// What is the maximum depth encountered so far.
        /// </summary>
        private int depth;

        /// <summary>
        /// Percent done.
        /// </summary>
        private double donePercent;

        /// <summary>
        /// Percent error.
        /// </summary>
        private double errorPercent;

        /// <summary>
        /// If there was an error loading the database.
        /// </summary>
        private String errorMessage;

        /// <summary>
        /// Load the stats from the database.
        /// </summary>
        private void GetStats()
        {
            if (options != null)
            {
                try
                {
                    this.waiting = this.processing = this.error = this.done = 0;
                    DbDataReader rs = this.stmtStatus.ExecuteReader();
                    bool b = rs.HasRows;

                    while (rs.Read())
                    {
                        String status = (string)rs[0];
                        int count = (int)rs[1];
                        if (String.Compare(status, "W", true) == 0)
                        {
                            this.waiting = count;
                        }
                        else if (String.Compare(status, "P", true) == 0)
                        {
                            this.processing = count;
                        }
                        else if (String.Compare(status, "E", true) == 0)
                        {
                            this.error = count;
                        }
                        else if (String.Compare(status, "D", true) == 0)
                        {
                            this.done = count;
                        }
                    }
                    rs.Close();

                    this.depth = 0;
                    rs = this.stmtDepth.ExecuteReader();
                    if (rs.Read())
                    {
                        this.depth = (int)rs[0];
                    }
                    rs.Close();
                }
                catch (Exception e)
                {
                    errorMessage = e.Message;
                }
            }
        }


        /// <summary>
        /// Draw a progress bar.
        /// </summary>
        /// <param name="g">The graphics object.</param>
        /// <param name="y">The y position to draw the progress bar.</param>
        private void DisplayProgressBar(Graphics g, int y)
        {
            int width = this.Width-32;
            int progressWidth = width - 20;
            g.FillRectangle(Brushes.White, new Rectangle(10, y, progressWidth, 16));
            g.FillRectangle(Brushes.Green, new Rectangle(10, y, (int)(progressWidth * this.donePercent), 16));
            g.DrawRectangle(Pens.Black, new Rectangle(10, y, progressWidth, 16));
        }

        /// <summary>
        /// Display the stats.
        /// </summary>
        /// <param name="g">The graphics object.</param>
        public void DisplayStats(Graphics g)
        {
            const String stat1 = "Total URL\'s Encountered:";
            const String stat2 = "Completed URL\'s:";
            const String stat3 = "Waiting URL\'s:";
            const String stat4 = "URL\'s Currently Processing:";
            const String stat5 = "URL\'s with Errors:";
            const String stat6 = "Deepest URL\'s found:";

            g.FillRectangle(new SolidBrush(Color.White), new Rectangle(0,0,this.Width,this.Height));
            
            Font drawFont = new Font(FontFamily.GenericSansSerif, 10.0F, FontStyle.Regular);
            Font boldFont = new Font(FontFamily.GenericSansSerif, 10.0F, FontStyle.Bold);

            int y = boldFont.Height;


            if (errorMessage == null)
            {
                int total = this.processing + this.error + this.done + this.waiting;

                if ((this.waiting + this.processing + this.done) == 0)
                {
                    this.donePercent = 0;
                }
                else
                {
                    this.donePercent = (double)this.done / (double)(this.waiting + this.processing + this.done);
                }

                if (total == 0)
                {
                    this.errorPercent = 0;
                }
                else
                {
                    this.errorPercent = (double)this.error / (double)total;
                }

                g.DrawString(stat1, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString("" + total, drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                g.DrawString(stat2, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString(this.done.ToString("G") + "("
                    + this.donePercent.ToString("P") + ")", drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                g.DrawString(stat3, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString(this.waiting.ToString("G"), drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                g.DrawString(stat4, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString(this.processing.ToString("G"), drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                g.DrawString(stat5, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString(this.error.ToString("G") + "("
                    + this.errorPercent.ToString("P") + ")", drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                g.DrawString(stat6, boldFont, Brushes.Black, new PointF(10, y));
                g.DrawString(this.depth.ToString("G"), drawFont, Brushes.Black, new PointF(200, y));
                y += boldFont.Height;

                DisplayProgressBar(g, y);
            }
            else
            {
                g.DrawString(errorMessage, boldFont, Brushes.Black, new PointF(10, 10));
            }

        }
        
        /// <summary>
        /// Open a connection to the database.
        /// </summary>
        private void Open()
        {
            try
            {
                if (options != null)
                {
                    connection = new OleDbConnection(this.options.DbConnectionString);
                    connection.Open();


                    stmtStatus = this.connection.CreateCommand();
                    stmtStatus.CommandText = MainForm.sqlStatus;
                    stmtStatus.Prepare();

                    stmtDepth = this.connection.CreateCommand();
                    stmtDepth.CommandText = MainForm.sqlDepth;
                    stmtDepth.Prepare();
                }
                else
                {
                    errorMessage = "Data base options were not loaded";
                }
            }
            catch (Exception e)
            {
                errorMessage = e.Message;
            }
        }


        /// <summary>
        /// The constructor.
        /// </summary>
        public MainForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Called when the form loads; start the timer.
        /// </summary>
        /// <param name="sender">Not used.</param>
        /// <param name="e">Not used.</param>
        private void MainForm_Load(object sender, EventArgs e)
        {
            timer.Start();
        }

        /// <summary>
        /// The timer updates the stats display.
        /// </summary>
        /// <param name="sender">Not used.</param>
        /// <param name="e">Not used.</param>
        private void timer_Tick(object sender, EventArgs e)
        {
            Graphics g = this.CreateGraphics();

            if (this.connection == null)
            {
                Open();
            }

            if (g != null)
            {
                GetStats();
                DisplayStats(g);
            }

            g.Dispose();
        }
    }
}