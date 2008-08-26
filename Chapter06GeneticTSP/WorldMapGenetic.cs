using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace Chapter06GeneticTSP
{
    public partial class WorldMapGenetic : Form
    {
        /**
* How many cities to use.
*/
        public const int CITY_COUNT = 50;

        /**
         * How many chromosomes to use.
         */
        public const int POPULATION_SIZE = 1000;

        /**
         * What percent of new-borns to mutate.
         */
        public const double MUTATION_PERCENT = 0.10;

        /**
    * The part of the population eligable for mateing.
    */
        protected int matingPopulationSize = POPULATION_SIZE / 2;

        /**
         * The part of the population favored for mating.
         */
        protected int favoredPopulationSize = (POPULATION_SIZE / 2) / 2;


        /**
         * How much genetic material to take during a mating.
         */
        protected int cutLength = CITY_COUNT / 5;

        /**
         * The current generation, or epoc.
         */
        protected int generation;

        protected string status;

        /**
         * The list of cities.
         */
        protected City[] cities;

        protected TSPGeneticAlgorithm genetic;



        public WorldMapGenetic()
        {
            InitializeComponent();
            Random rand = new Random();
            // place the cities at random locations
            int height = this.Height;
            int width = this.Width;
            this.cities = new City[WorldMapGenetic.CITY_COUNT];
            for (int i = 0; i < WorldMapGenetic.CITY_COUNT; i++)
            {
                this.cities[i] = new City((int)(rand.NextDouble() * (width - 10)),
                        (int)(rand.NextDouble() * (height - 64)));
            }

            this.genetic = new TSPGeneticAlgorithm(this.cities,
                    WorldMapGenetic.POPULATION_SIZE,
                    WorldMapGenetic.MUTATION_PERCENT, 0.25, 0.5,
                    WorldMapGenetic.CITY_COUNT / 5);

            start();
        }

        private void WorldMapGenetic_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            int width = this.Width;
            int height = this.Height;

            SolidBrush blackBrush = new SolidBrush(Color.Black);
            SolidBrush greenBrush = new SolidBrush(Color.Green);
            Pen whitePen = new Pen(Color.White);

            g.FillRectangle(blackBrush, 0, 0, width, height);

            for (int i = 0; i < WorldMapGenetic.CITY_COUNT; i++)
            {
                int xpos = this.cities[i].getx();
                int ypos = this.cities[i].gety();
                g.FillEllipse(greenBrush, xpos - 5, ypos - 5, 10, 10);
            }

            TSPChromosome top = this.getTopChromosome();

            for (int i = 0; i < WorldMapGenetic.CITY_COUNT - 1; i++)
            {
                int icity = top.GetGene(i);
                int icity2 = top.GetGene(i + 1);

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


        public TSPChromosome getTopChromosome()
        {
            return (TSPChromosome)this.genetic.GetChromosome(0);
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

            while (countSame < 25)
            {

                this.generation++;

                this.status = "Generation " + this.generation + " Cost "
                        + (int)thisCost;

                this.genetic.Iteration();
                thisCost = this.getTopChromosome().Cost;

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
            status = "Solution found after " + this.generation
                    + " generations.";
            this.Invalidate();
        }


    }
}
