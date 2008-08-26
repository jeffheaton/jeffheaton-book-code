using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter09Predict
{
    class ActualData
    {
        public static double sinDEG(double deg)
        {
            double rad = deg * (Math.PI / 180);
            double result = Math.Sin(rad);
            return ((int)(result * 100000.0)) / 100000.0;
        }

        private double[] actual;
        private int inputSize;

        private int outputSize;

        public ActualData(int size, int inputSize, int outputSize)
        {
            this.actual = new double[size];
            this.inputSize = inputSize;
            this.outputSize = outputSize;

            int angle = 0;
            for (int i = 0; i < this.actual.Length; i++)
            {
                this.actual[i] = sinDEG(angle);
                angle += 10;
            }
        }

        public void getInputData(int offset, double[] target)
        {
            for (int i = 0; i < this.inputSize; i++)
            {
                target[i] = this.actual[offset + i];
            }
        }

        public void getOutputData(int offset, double[] target)
        {
            for (int i = 0; i < this.outputSize; i++)
            {
                target[i] = this.actual[offset + this.inputSize + i];
            }
        }

    }
}
