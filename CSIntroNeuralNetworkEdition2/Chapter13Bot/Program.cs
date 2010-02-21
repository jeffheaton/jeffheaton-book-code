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
using Chapter13Bot.Gather;
using Chapter13Bot.Train;
using Chapter13Bot.Born;

namespace Chapter13Bot
{
    public class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 1)
            {
                Console.WriteLine("Run with argument gather/train/born");
            }
            else
            {
                if (String.Compare(args[0], "gather", true) == 0)
                {
                    GatherForTrain gather = new GatherForTrain();
                    gather.Process();
                }
                else if (String.Compare(args[0], "train", true) == 0)
                {
                    TrainBot train = new TrainBot();
                    train.Process();
           
                }
                else if (String.Compare(args[0], "born", true) == 0)
                {
                    YearBornBot bot = new YearBornBot();
                    bot.Process("Bill Gates");
                }
            }

        }
    }
}
