using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

using HeatonResearchNeural.SOM;
using HeatonResearchNeural.Matrix;

namespace Chapter11TestSOM
{
    public partial class TestSOMForm : Form
    {
        	/**
	 * How many input neurons to use.
	 */
	public const int INPUT_COUNT = 2;

	/**
	 * How many output neurons to use.
	 */
	public const int OUTPUT_COUNT = 7;

	/**
	 * How many random samples to generate.
	 */
	public const int SAMPLE_COUNT = 100;

        /**
	 * The unit length in pixels, which is the max of the height and width of
	 * the window.
	 */
	protected int unitLength;

	/**
	 * How many retries so far.
	 */
	protected int retry = 1;

	/**
	 * The current error percent.
	 */
	protected double totalError = 0;
	/**
	 * The best error percent.
	 */
	protected double bestError = 0;

	/**
	 * The neural network.
	 */

	protected SelfOrganizingMap net;
	protected double [][]input;


        public TestSOMForm()
        {
            InitializeComponent();
            start();
        }

        private void TestSOMForm_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            		if (this.net == null) {
			return;
		}

		 int width = this.Width;
		 int height = this.Height;
		this.unitLength = Math.Min(width, height);
        SolidBrush blackBrush = new SolidBrush(Color.Black);
        SolidBrush whiteBrush = new SolidBrush(Color.White);
        SolidBrush greenBrush = new SolidBrush(Color.Green);
        Pen whitePen = new Pen(Color.White);
		g.FillRectangle(blackBrush, 0, 0, width, height);

		// plot the weights of the output neurons
		 Matrix outputWeights = this.net.OutputWeights;

		for (int y = 0; y < outputWeights.Rows; y++) {

			g.FillRectangle(whiteBrush,(int) (outputWeights[y, 0] * this.unitLength),
					(int) (outputWeights[y, 1] * this.unitLength), 10, 10);

		}

		// plot a grid of samples to test the net with
		for (int y = 0; y < this.unitLength; y += 50) {
			for (int x = 0; x < this.unitLength; x += 50) {
				g.FillEllipse(greenBrush,x, y, 5, 5);
				 double []d = new double[2];
				d[0] = x;
				d[1] = y;

				 int c = this.net.Winner(d);

				 int x2 = (int) (outputWeights[c, 0] * this.unitLength);
				 int y2 = (int) (outputWeights[c, 1] * this.unitLength);

				g.DrawLine(whitePen,x, y, x2, y2);
			}

		}

		// display the status info
            string status = "retry = " + this.retry + ",current error = "
				+ (this.totalError * 100).ToString("N2") + "%, best error = "
                + (this.bestError * 100).ToString("N2") + "%";

        Font drawFont = new Font("Arial", 10);
        SolidBrush drawBrush = new SolidBrush(Color.White);
        StringFormat drawFormat = new StringFormat();
        e.Graphics.DrawString(status, drawFont, drawBrush, 0, this.Height - drawFont.Height - 35, drawFormat);


        }

        public void start()
        {
            ThreadStart ts = new ThreadStart(ThreadProc);
            Thread thread = new Thread(ts);
            thread.Start();
        }

        void ThreadProc()
        {
            Random rand = new Random();
            // build the training set
		this.input = new double[SAMPLE_COUNT][];

		for (int i = 0; i < SAMPLE_COUNT; i++) {
            this.input[i] = new double[INPUT_COUNT];
			for (int j = 0; j < INPUT_COUNT; j++) {
				this.input[i][j] = rand.NextDouble();
			}
		}

		// build and train the neural network
		this.net = new SelfOrganizingMap(INPUT_COUNT, OUTPUT_COUNT,
				NormalizationType.MULTIPLICATIVE);
		 TrainSelfOrganizingMap train = new TrainSelfOrganizingMap(
                this.net, this.input, TrainSelfOrganizingMap.LearningMethod.SUBTRACTIVE, 0.5);
		train.Initialize();
		double lastError = Double.MaxValue;
		int errorCount = 0;

		while (errorCount < 10) {
			train.Iteration();
			this.retry++;
			this.totalError = train.TotalError;
			this.bestError = train.BestError;
            this.Invalidate();

			if (this.bestError < lastError) {
				lastError = this.bestError;
				errorCount = 0;
			} else {
				errorCount++;
			}
		}
        }



    }
}
