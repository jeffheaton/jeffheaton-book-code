using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using HeatonResearchNeural.Activation;
using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train;
using HeatonResearchNeural.Feedforward.Train.Backpropagation;
using HeatonResearchNeural.Feedforward.Train.Anneal;
using HeatonResearchNeural.Util;

namespace Chapter10SP500
{
    class PredictSP500
    {
  
	public const int TRAINING_SIZE = 500;
	public const int INPUT_SIZE = 10;
	public const int OUTPUT_SIZE = 1;
	public const int NEURONS_HIDDEN_1 = 20;
	public const int NEURONS_HIDDEN_2 = 0;
	public const double MAX_ERROR = 0.02;
	public  DateTime PREDICT_FROM = ReadCSV.ParseDate("2007-01-01");
	public  DateTime LEARN_FROM = ReadCSV.ParseDate("1980-01-01");

        static void Main(string[] args){
		 PredictSP500 predict = new PredictSP500();
		if (args.Length > 0 && args[0].Equals("full",StringComparison.CurrentCultureIgnoreCase))
			predict.run(true);
		else
			predict.run(false);
	}

	private double [][]input;

	private double [][]ideal;
	private FeedforwardNetwork network;

	private SP500Actual actual;

	public void createNetwork() {
		 ActivationFunction threshold = new ActivationTANH();
		this.network = new FeedforwardNetwork();
		this.network.AddLayer(new FeedforwardLayer(threshold,
				PredictSP500.INPUT_SIZE * 2));
		this.network.AddLayer(new FeedforwardLayer(threshold,
				PredictSP500.NEURONS_HIDDEN_1));
		if (PredictSP500.NEURONS_HIDDEN_2 > 0) {
			this.network.AddLayer(new FeedforwardLayer(threshold,
					PredictSP500.NEURONS_HIDDEN_2));
		}
		this.network.AddLayer(new FeedforwardLayer(threshold,
				PredictSP500.OUTPUT_SIZE));
		this.network.Reset();
	}

	public void display() {


		 double[] present = new double[INPUT_SIZE * 2];
		double[] predict = new double[OUTPUT_SIZE];
		 double[] actualOutput = new double[OUTPUT_SIZE];

		int index = 0;
		foreach ( FinancialSample sample in this.actual.getSamples()) {
			if (sample.getDate().CompareTo(this.PREDICT_FROM)>0) {
				 StringBuilder str = new StringBuilder();
				str.Append(ReadCSV.DisplayDate(sample.getDate()));
				str.Append(":Start=");
				str.Append(sample.getAmount());

				this.actual.getInputData(index - INPUT_SIZE, present);
				this.actual.getOutputData(index - INPUT_SIZE, actualOutput);

				predict = this.network.ComputeOutputs(present);
				str.Append(",Actual % Change=");
				str.Append(actualOutput[0].ToString("N2"));
				str.Append(",Predicted % Change= ");
				str.Append(predict[0].ToString("N2"));

				str.Append(":Difference=");

				 ErrorCalculation error = new ErrorCalculation();
				error.UpdateError(predict, actualOutput);
				str.Append(error.CalculateRMS().ToString("N2"));

				// 

				Console.WriteLine(str.ToString());
			}

			index++;
		}
	}

	private void generateTrainingSets() {
		this.input = new double[TRAINING_SIZE][];//[INPUT_SIZE * 2];
		this.ideal = new double[TRAINING_SIZE][];//[OUTPUT_SIZE];

		// find where we are starting from
		int startIndex = 0;
		foreach ( FinancialSample sample in this.actual.getSamples()) {
			if (sample.getDate().CompareTo(this.LEARN_FROM)>0) {
				break;
			}
			startIndex++;
		}

		// create a sample factor across the training area
		 int eligibleSamples = TRAINING_SIZE - startIndex;
		if (eligibleSamples == 0) {
			Console.WriteLine("Need an earlier date for LEARN_FROM or a smaller number for TRAINING_SIZE.");
            return;
		}
		 int factor = eligibleSamples / TRAINING_SIZE;

		// grab the actual training data from that point
		for (int i = 0; i < TRAINING_SIZE; i++) {
			this.actual.getInputData(startIndex + (i * factor), this.input[i]);
			this.actual.getOutputData(startIndex + (i * factor), this.ideal[i]);
		}
	}

	public void loadNeuralNetwork()  {
		this.network = (FeedforwardNetwork) SerializeObject.Load("sp500.net");
	}

	public void run(bool full) {
		try {
			this.actual = new SP500Actual(INPUT_SIZE, OUTPUT_SIZE);
			this.actual.load("sp500.csv", "prime.csv");

			Console.WriteLine("Samples read: " + this.actual.size());
			
			if (full) {
				createNetwork();
				generateTrainingSets();
				
				trainNetworkBackprop();
				
				saveNeuralNetwork();
			} else {
				loadNeuralNetwork();
			}
			
			display();

		} catch ( Exception e) {
            Console.WriteLine(e);
            Console.WriteLine(e.StackTrace);
		}
	}

	public void saveNeuralNetwork()  {
		SerializeObject.Save("sp500.net", this.network);
	}

	private void trainNetworkBackprop() {
		 Train train = new Backpropagation(this.network, this.input,
				this.ideal, 0.00001, 0.1);
		double lastError = Double.MaxValue;
		int epoch = 1;
		int lastAnneal = 0;

		do {
			train.Iteration();
			double error = train.Error;
			
			Console.WriteLine("Iteration(Backprop) #" + epoch + " Error:"
					+ error);
			
			if( error>0.05 )
			{
				if( (lastAnneal>100) && (error>lastError || Math.Abs(error-lastError)<0.0001) )
				{
					trainNetworkAnneal();
					lastAnneal = 0;
				}
			}
			
			lastError = train.Error;
			epoch++;
			lastAnneal++;
		} while (train.Error > MAX_ERROR);
	}
	
	private void trainNetworkAnneal() {
		Console.WriteLine("Training with simulated annealing for 5 iterations");
		// train the neural network
		 NeuralSimulatedAnnealing train = new NeuralSimulatedAnnealing(
				this.network, this.input, this.ideal, 10, 2, 100);

		int epoch = 1;

		for(int i=1;i<=5;i++) {
			train.Iteration();
			Console.WriteLine("Iteration(Anneal) #" + epoch + " Error:"
					+ train.Error);
			epoch++;
		} 
	}      

        

    }
}
