using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter04Hebb
{


    /// <summary>
    /// Chapter 4: Machine Learning
    ///
    /// Hebb: Learn, using Hebb's rule.
    /// </summary>
    public class Hebb
    {

        /// <summary>
        /// Main method just instanciates a delta object and calls run.
        /// </summary>
        /// <param name="args">Not used</param>
        static void Main(string[] args)
        {
            Hebb hebb = new Hebb();
            hebb.run();

        }

        /// <summary>
        /// Weight for neuron 1
        /// </summary>
        double w1;

        /// <summary>
        /// Weight for neuron 2
        /// </summary>
        double w2;

        /// <summary>
        /// Learning rate
        /// </summary>
        double rate = 1.0;

        /// <summary>
        /// Current epoch #
        /// </summary>
        int epoch = 1;

        /// <summary>
        /// Simple constructor.
        /// </summary>
        public Hebb()
        {
            this.w1 = 1;
            this.w2 = -1;
        }


        /// <summary>
        /// Process one epoch. Here we learn from all three training samples and then
        /// update the weights based on error.
        /// </summary>
        protected void Epoch()
        {
            Console.WriteLine("***Beginning Epoch #" + this.epoch + "***");
            PresentPattern(-1, -1);
            PresentPattern(-1, 1);
            PresentPattern(1, -1);
            PresentPattern(1, 1);
            this.epoch++;
        }

        /// <summary>
        /// Present a pattern and learn from it.
        /// </summary>
        /// <param name="i1">Input to neuron 1</param>
        /// <param name="i2">Input to neuron 2</param>
        protected void PresentPattern(double i1, double i2)
        {
            double result;
            double delta;

            // run the net as is on training data
            // and get the error
            Console.Write("Presented [" + i1 + "," + i2 + "]");
            result = Recognize(i1, i2);
            Console.Write(" result=" + result);

            // adjust weight 1
            delta = TrainingFunction(this.rate, i1, result);
            this.w1 += delta;
            Console.Write(",delta w1=" + delta);

            // adjust weight 2
            delta = TrainingFunction(this.rate, i2, result);
            this.w2 += delta;
            Console.WriteLine(",delta w2=" + delta);

        }


        /// <summary>
        /// Attempt to recognize a pattern.
        /// </summary>
        /// <param name="i1">Input to neuron 1</param>
        /// <param name="i2">Input to neuron 2</param>
        /// <returns>The output from the neural network</returns>
        protected double Recognize(double i1, double i2)
        {
            double a = (this.w1 * i1) + (this.w2 * i2);
            return (a * .5);
        }

        /// <summary>
        /// This method loops through 10 epochs.
        /// </summary>
        public void run()
        {
            for (int i = 0; i < 5; i++)
            {
                Epoch();
            }
        }



        /// <summary>
        /// The learningFunction implements the delta rule. This method will return
        /// the weight adjustment for the specified input neuron.
        /// </summary>
        /// <param name="rate">The learning rate</param>
        /// <param name="input">The input neuron we're processing</param>
        /// <param name="output">The output from the neural network.</param>
        /// <returns>The amount to adjust the weight by.</returns>
        protected double TrainingFunction(double rate, double input,
                 double output)
        {
            return rate * input * output;
        }
    }
}
