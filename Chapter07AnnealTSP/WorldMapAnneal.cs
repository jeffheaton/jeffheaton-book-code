using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace Chapter07AnnealTSP
{
    public partial class WorldMapAnneal : Form
    {

        /**
         * How many cities to use.
         */
        public const int CITY_COUNT = 50;

        /**
         * Starting temperature for simulated annealing
         */
        public const double START_TEMPERATURE = 10;

        /**
         * The temperature delta for simulated annealing
         */
        public const double STOP_TEMPERATURE = 2;

        /**
         * Cycles per epoc
         */
        public const int CYCLES = 100;


        /**
         * The current generation, or epoc.
         */
        protected int epoc;

        /**
         * The list of cities.
         */
        protected City[] cities;

        protected string status;

        protected TSPSimulatedAnnealing anneal;

        public WorldMapAnneal()
        {
            InitializeComponent();
            Random rand = new Random();
            // place the cities at random locations
            int height = this.Height - 50;
            int width = this.Width - 10;
            this.cities = new City[WorldMapAnneal.CITY_COUNT];
            for (int i = 0; i < WorldMapAnneal.CITY_COUNT; i++)
            {
                this.cities[i] = new City((int)(rand.NextDouble() * width),
                        (int)(rand.NextDouble() * height));
            }

            this.anneal = new TSPSimulatedAnnealing(this.cities, START_TEMPERATURE,
                    STOP_TEMPERATURE, CYCLES);

            bool[] taken = new bool[this.cities.Length];
            int[] path = new int[this.cities.Length];

            for (int i = 0; i < path.Length; i++)
            {
                taken[i] = false;
            }
            for (int i = 0; i < path.Length - 1; i++)
            {
                int icandidate;
                do
                {
                    icandidate = (int)(rand.NextDouble() * path.Length);
                } while (taken[icandidate]);
                path[i] = icandidate;
                taken[icandidate] = true;
                if (i == path.Length - 2)
                {
                    icandidate = 0;
                    while (taken[icandidate])
                    {
                        icandidate++;
                    }
                    path[i + 1] = icandidate;
                }
            }

            this.anneal.PutArray(path);

            start();
        }



        public void start()
        {
            ThreadStart ts = new ThreadStart(ThreadProc);
            Thread thread = new Thread(ts);
            thread.Start();
        }

        public void ThreadProc()
        {
            double thisCost = 500.0;
            double oldCost = 0.0;
            int countSame = 0;

            this.Invalidate();

            while (countSame < 50)
            {

                this.epoc++;

                this.status = "Epoc " + this.epoc;

                this.anneal.Iteration();
                thisCost = this.anneal.Error;

                if ((int)thisCost == (int)oldCost)
                {
                    countSame++;
                }
                else
                {
                    countSame = 0;
                    oldCost = thisCost;
                }
                this.Invalidate();

            }
            this.status = "Solution found after " + this.epoc + " epocs.";
            this.Invalidate();

        }

        private void WorldMapAnneal_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            int width = this.Width;
            int height = this.Height;

            SolidBrush blackBrush = new SolidBrush(Color.Black);
            SolidBrush greenBrush = new SolidBrush(Color.Green);
            Pen whitePen = new Pen(Color.White);

            g.FillRectangle(blackBrush, 0, 0, width, height);

            for (int i = 0; i < WorldMapAnneal.CITY_COUNT; i++)
            {
                int xpos = this.cities[i].getx();
                int ypos = this.cities[i].gety();
                g.FillEllipse(greenBrush, xpos - 5, ypos - 5, 10, 10);
            }

            int[] path = this.anneal.GetArray();

            for (int i = 0; i < WorldMapAnneal.CITY_COUNT - 1; i++)
            {
                int icity = path[i];
                int icity2 = path[i + 1];

                g.DrawLine(whitePen, this.cities[icity].getx(),
                        this.cities[icity].gety(), this.cities[icity2]
                                .getx(), this.cities[icity2].gety());

            }

            // display status

            Font drawFont = new Font("Arial", 10);
            SolidBrush drawBrush = new SolidBrush(Color.White);
            float x = 0.0F;
            float y = this.Height - drawFont.Height - 35;
            StringFormat drawFormat = new StringFormat();
            e.Graphics.DrawString(status, drawFont, drawBrush, x, y, drawFormat);
        }
    }
}
