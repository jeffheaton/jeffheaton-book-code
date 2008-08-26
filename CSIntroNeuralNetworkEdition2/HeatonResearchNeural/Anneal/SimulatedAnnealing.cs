using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HeatonResearchNeural.Anneal
{
    abstract public class SimulatedAnnealing<UNIT_TYPE>
    {
        /// <summary>
        /// The starting temperature.
        /// </summary>
        public double StartTemperature
        {
            get
            {
                return startTemperature;
            }
            set
            {
                this.startTemperature = value;
            }
        }

        /// <summary>
        /// The ending temperature.
        /// </summary>
        public double StopTemperature
        {
            get
            {
                return stopTemperature;
            }
            set
            {
                this.stopTemperature = value;
            }
        }

        /// <summary>
        /// The number of cycles that will be used, per iteration.
        /// </summary>
        public int Cycles
        {
            get
            {
                return cycles;
            }
            set
            {
                this.cycles = value;
            }
        }


        /// <summary>
        /// The current error.
        /// </summary>
        public double Error
        {
            get
            {
                return error;
            }
            set
            {
                error = value;
            }
        }

        /// <summary>
        /// The current temperature.
        /// </summary>
        public double Temperature
        {
            get
            {
                return temperature;
            }
        }


        /// <summary>
        /// The starting temperature.
        /// </summary>
        private double startTemperature;

        /// <summary>
        /// The ending temperature.
        /// </summary>
        private double stopTemperature;

        /// <summary>
        /// The number of cycles that will be used, per iteration.
        /// </summary>
        private int cycles;

        /// <summary>
        /// The current error.
        /// </summary>
        private double error;

        /// <summary>
        /// The current temperature.
        /// </summary>
        protected double temperature;

        /**
         * Subclasses should provide a method that evaluates the error for the
         * current solution. Those solutions with a lower error are better.
         * 
         * @return Return the error, as a percent.
         * @throws NeuralNetworkError
         *             Should be thrown if any sort of error occurs.
         */
        public abstract double DetermineError();

        /**
         * Subclasses must provide access to an array that makes up the solution.
         * 
         * @return An array that makes up the solution.
         */
        public abstract UNIT_TYPE[] GetArray();

        /**
         * Called to perform one cycle of the annealing process.
         */
        public void Iteration()
        {
            UNIT_TYPE[] bestArray;

            this.Error = DetermineError();
            bestArray = this.GetArrayCopy();

            this.temperature = this.StartTemperature;

            for (int i = 0; i < this.cycles; i++)
            {
                double curError;
                Randomize();
                curError = DetermineError();
                if (curError < this.Error)
                {
                    bestArray = this.GetArrayCopy();
                    this.Error = curError;
                }

                this.PutArray(bestArray);
                double ratio = Math.Exp(Math.Log(this.StopTemperature
                        / this.StartTemperature)
                        / (this.Cycles - 1));
                this.temperature *= ratio;
            }
        }

        public abstract UNIT_TYPE[] GetArrayCopy();

        public abstract void PutArray(UNIT_TYPE[] array);

        public abstract void Randomize();
    }
}
