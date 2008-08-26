using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter04Delta
{
    /// <summary>
    /// Delta: Learn, using the delta rule.
    /// </summary>
    class Delta
    {
        /// <summary>
        /// Main method just instanciates a delta object and calls run.
        /// </summary>
        /// <param name="args">Not used</param>
        static void Main(string[] args)
        {
            Delta delta = new Delta();
            delta.Run();

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
        /// Weight for neuron 3
        /// </summary>
        double w3;

        /// <summary>
        /// Learning rate
        /// </summary>
        double rate = 0.5;

        /// <summary>
        /// Current epoch #
        /// </summary>
        int epoch = 1;



        /// <summary>
        /// Process one epoch. Here we learn from all three training samples and then
        /// update the weights based on error.
        /// </summary>
        protected void Epoch()
        {
            Console.WriteLine("***Beginning Epoch #" + this.epoch + "***");
            PresentPattern(0, 0, 1, 0);
            PresentPattern(0, 1, 1, 0);
            PresentPattern(1, 0, 1, 0);
            PresentPattern(1, 1, 1, 1);
            this.epoch++;
        }

        /// <summary>
        /// This method will calculate the error between the anticipated output and
        /// the actual output.
        /// </summary>
        /// <param name="actual">The actual output from the neural network.</param>
        /// <param name="anticipated">The anticipated neuron output.</param>
        /// <returns>The error.</returns>
        protected double GetError(double actual, double anticipated)
        {
            return (anticipated - actual);
        }


        /// <summary>
        /// Present a pattern and learn from it.
        /// </summary>
        /// <param name="i1">Input to neuron 1</param>
        /// <param name="i2">Input to neuron 2</param>
        /// <param name="i3">Input to neuron 3</param>
        /// <param name="anticipated">The anticipated output</param>
        protected void PresentPattern(double i1, double i2,
                 double i3, double anticipated)
        {
            double error;
            double actual;
            double delta;

            // run the net as is on training data
            // and get the error
            Console.Write("Presented [" + i1 + "," + i2 + "," + i3 + "]");
            actual = Recognize(i1, i2, i3);
            error = GetError(actual, anticipated);
            Console.Write(" anticipated=" + anticipated);
            Console.Write(" actual=" + actual);
            Console.WriteLine(" error=" + error);

            // adjust weight 1
            delta = TrainingFunction(this.rate, i1, error);
            this.w1 += delta;

            // adjust weight 2
            delta = TrainingFunction(this.rate, i2, error);
            this.w2 += delta;

            // adjust weight 3
            delta = TrainingFunction(this.rate, i3, error);
            this.w3 += delta;
        }

        /// <summary>
        /// Try to recognize a pattern.
        /// </summary>
        /// <param name="i1">Input to neuron 1</param>
        /// <param name="i2">Input to neuron 2</param>
        /// <param name="i3">Input to neuron 3</param>
        /// <returns>The output from the neural network</returns>
        protected double Recognize(double i1, double i2, double i3)
        {
            double a = (this.w1 * i1) + (this.w2 * i2) + (this.w3 * i3);
            return (a * .5);
        }

        /// <summary>
        /// This method loops through 100 epochs.
        /// </summary>
        public void Run()
        {
            for (int i = 0; i < 100; i++)
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
        /// <param name="error">The error between the actual output and anticipated output.</param>
        /// <returns>The amount to adjust the weight by.</returns>
        protected double TrainingFunction(double rate, double input,
                 double error)
        {
            return rate * input * error;
        }
    }
}
