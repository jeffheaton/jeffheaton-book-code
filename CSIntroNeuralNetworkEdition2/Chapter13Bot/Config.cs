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

namespace Chapter13Bot
{
    /// <summary>
    /// Holds configuration data for the bot.
    /// </summary>
    public class Config
    {
        public const int INPUT_SIZE = 10;
        public const int OUTPUT_SIZE = 1;
        public const int NEURONS_HIDDEN_1 = 20;
        public const int NEURONS_HIDDEN_2 = 0;
        public const double ACCEPTABLE_ERROR = 0.01;
        public const int MINIMUM_WORDS_PRESENT = 3;

        public const String FILENAME_GOOD_TRAINING_TEXT = "bornTrainingGood.txt";
        public const String FILENAME_BAD_TRAINING_TEXT = "bornTrainingBad.txt";
        public const String FILENAME_COMMON_WORDS = "common.csv";
        public const String FILENAME_WHENBORN_NET = "whenborn.net";
        public const String FILENAME_HISTOGRAM = "whenborn.hst";
    }
}
