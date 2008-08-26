using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Chapter13Bot
{
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
