using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

using HeatonResearchNeural.Feedforward;
using HeatonResearchNeural.Feedforward.Train;
using HeatonResearchNeural.Feedforward.Train.Backpropagation;
using HeatonResearchNeural.Prune;

namespace Chapter08PruneSelective
{
    public partial class PruneSelectiveForm : Form
    {
        public static double[][] XOR_INPUT ={
            new double[2] { 0.0, 0.0 },
            new double[2] { 1.0, 0.0 },
			new double[2] { 0.0, 1.0 },
            new double[2] { 1.0, 1.0 } };

        /**
 * The number of input neurons.
 */
        protected const int NUM_INPUT = 2;

        /**
         * The number of output neurons.
         */
        protected const int NUM_OUTPUT = 1;

        /**
         * The number of hidden neurons.
         */
        protected const int NUM_HIDDEN = 10;

        /**
         * The learning rate.
         */
        protected const double RATE = 0.5;

        /**
         * The learning momentum.
         */
        protected const double MOMENTUM = 0.7;

        /**
    * The neural network.
    */
        protected FeedforwardNetwork network;

        // This delegate enables asynchronous calls for setting
        // the text property on a TextBox control.
        delegate void SetTextCallback(string text);


        public PruneSelectiveForm()
        {
            InitializeComponent();
            this.network = new FeedforwardNetwork();
            this.network.AddLayer(new FeedforwardLayer(NUM_INPUT));
            this.network.AddLayer(new FeedforwardLayer(NUM_HIDDEN));
            this.network.AddLayer(new FeedforwardLayer(NUM_OUTPUT));
            this.network.Reset();

        }

        private void btnQuit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void btnTrain_Click(object sender, EventArgs e)
        {
            this.status.Text = "Training...";
            ThreadStart ts = new ThreadStart(ThreadProc);
            Thread thread = new Thread(ts);
            thread.Start();
        }

        private void btnRun_Click(object sender, EventArgs e)
        {
            double[] output = this.network.ComputeOutputs(PruneSelectiveForm.XOR_INPUT[0]);
            this.actual1.Text = "" + output[0];

            output = this.network.ComputeOutputs(PruneSelectiveForm.XOR_INPUT[1]);
            this.actual2.Text = "" + output[0];

            output = this.network.ComputeOutputs(PruneSelectiveForm.XOR_INPUT[2]);
            this.actual3.Text = "" + output[0];

            output = this.network.ComputeOutputs(PruneSelectiveForm.XOR_INPUT[3]);
            this.actual4.Text = "" + output[0];
        }

        public void ThreadProc()
        {

            int update = 0;

            Train train = new Backpropagation(this.network, PruneSelectiveForm.XOR_INPUT,
                   this.obtainIdeal(), 0.7, 0.9);

            int max = 10000;
            for (int i = 0; i < max; i++)
            {
                train.Iteration();

                update++;
                if (update == 100)
                {
                    SetText("Cycles Left:" + (max - i) + ",Error:"
                            + train.Error);
                    update = 0;
                }
            }

        }

        private void SetText(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.textBox1.InvokeRequired)
            {
                SetTextCallback d = new SetTextCallback(SetText);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.status.Text = text;
            }
        }

        private void btnPrune_Click(object sender, EventArgs e)
        {

            Prune prune = new Prune(this.network, PruneSelectiveForm.XOR_INPUT, this.obtainIdeal(), 0.05);
            int count = prune.PruneSelective();
            this.network = prune.CurrentNetwork;
            this.SetText("Prune removed " + count + " neurons.");

        }

        private double[][] obtainIdeal()
        {
            double[][] ideal = new double[4][];
            ideal[0] = new double[1];
            ideal[1] = new double[1];
            ideal[2] = new double[1];
            ideal[3] = new double[1];
            ideal[0][0] = double.Parse(this.expected1.Text);
            ideal[1][0] = double.Parse(this.expected2.Text);
            ideal[2][0] = double.Parse(this.expected3.Text);
            ideal[3][0] = double.Parse(this.expected4.Text);
            return ideal;
        }
    }
}
