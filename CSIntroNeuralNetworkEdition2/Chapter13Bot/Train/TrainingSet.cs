/// Introduction to Neural Networks with C#, 2nd Edition
/// Copyright 2008 by Heaton Research, Inc. 
/// http://www.heatonresearch.com/book/programming-neural-networks-cs-2.html
/// 
/// ISBN-10: 1604390093
/// ISBN-13: 978-1604390094
/// 
/// This class is released under the:
/// GNU Lesser General Public License (LGPL)
/// http://www.gnu.org/copyleft/lesser.html
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter13Bot.Train
{
    /// <summary>
    /// Used to build a training set based on both
    /// good and bad sentences.
    /// </summary>
    public class TrainingSet
    {
        private IList<double[]> input = new List<double[]>();
        private IList<double[]> ideal = new List<double[]>();

        public void AddTrainingSet(double[] addInput, double addIdeal)
        {
            // does the training set already exist
            foreach (double[] element in this.input)
            {
                if (Compare(addInput, element))
                {
                    return;
                }
            }

            // add it
            double[] array = new double[1];
            array[0] = addIdeal;
            this.input.Add(addInput);
            this.ideal.Add(array);

        }

        private bool Compare(double[] d1, double[] d2)
        {
            bool result = true;

            for (int i = 0; i < d1.Length; i++)
            {
                if (Math.Abs(d1[i] - d2[i]) > 0.000001)
                {
                    result = false;
                }
            }

            return result;
        }

        public IList<double[]> Ideal
        {
            get
            {
                return this.ideal;
            }
            set
            {
                this.ideal = value;
            }
        }

        public IList<double[]> Input
        {
            get
            {
                return this.input;
            }
            set
            {
                this.input = value;
            }
        }
    }
}
