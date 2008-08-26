using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Anneal;

namespace Chapter07AnnealTSP
{
    public class TSPSimulatedAnnealing : SimulatedAnnealing<int>
    {
        protected City[] cities;
        protected int[] path;

        /**
         * The constructor.
         * 
         * @param network
         *            The neural network that is to be trained.
         */
        public TSPSimulatedAnnealing(City[] cities, double startTemp,
                 double stopTemp, int cycles)
        {

            this.temperature = startTemp;
            this.StartTemperature = startTemp;
            this.StopTemperature = stopTemp;
            this.Cycles = cycles;

            this.cities = cities;
            this.path = new int[this.cities.Length];
        }


        override public double DetermineError()
        {
            double cost = 0.0;
            for (int i = 0; i < this.cities.Length - 1; i++)
            {
                double dist = this.cities[this.path[i]]
                       .proximity(this.cities[this.path[i + 1]]);
                cost += dist;
            }
            return cost;
        }

        /**
         * Called to get the distance between two cities.
         * 
         * @param i
         *            The first city
         * @param j
         *            The second city
         * @return The distance between the two cities.
         */
        public double distance(int i, int j)
        {
            int c1 = this.path[i % this.path.Length];
            int c2 = this.path[j % this.path.Length];
            return this.cities[c1].proximity(this.cities[c2]);
        }

        override public int[] GetArray()
        {
            return this.path;
        }

        override public void PutArray(int[] array)
        {
            this.path = array;
        }

        override public void Randomize()
        {

            int length = this.path.Length;

            Random rand = new Random();

            // make adjustments to city order(annealing)
            for (int i = 0; i < this.temperature; i++)
            {
                int index1 = (int)Math.Floor(length * rand.NextDouble());
                int index2 = (int)Math.Floor(length * rand.NextDouble());
                double d = distance(index1, index1 + 1) + distance(index2, index2 + 1)
                       - distance(index1, index2) - distance(index1 + 1, index2 + 1);
                if (d > 0)
                {

                    // sort index1 and index2 if needed
                    if (index2 < index1)
                    {
                        int temp = index1;
                        index1 = index2;
                        index2 = temp;
                    }
                    for (; index2 > index1; index2--)
                    {
                        int temp = this.path[index1 + 1];
                        this.path[index1 + 1] = this.path[index2];
                        this.path[index2] = temp;
                        index1++;
                    }
                }
            }

        }

        override public int[] GetArrayCopy()
        {
            int[] result = new int[this.path.Length];
            Array.Copy(path, result, this.path.Length);
            return result;
        }

    }
}
