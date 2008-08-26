using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using HeatonResearchNeural.Hopfield;

namespace Chapter03App
{
    /// <summary>
    /// Chapter 3: Hopfield Form
    /// 
    /// Display a Hopfield neural network's weight matrix and allow basic training.
    /// </summary>
    public partial class HopfieldForm : Form
    {
        private HopfieldNetwork network = new HopfieldNetwork(4);
        private Color defaultColor;

        public HopfieldForm()
        {
            InitializeComponent();
            SetMatrixValues();
            this.input1.SelectedIndex = 0;
            this.input2.SelectedIndex = 0;
            this.input3.SelectedIndex = 0;
            this.input4.SelectedIndex = 0;
            this.defaultColor = this.output1.BackColor;
        }


        /// <summary>
        /// Collect the matrix values from the applet and place inside the weight
        /// matrix for the neural network.
        /// </summary>
        private void CollectMatrixValues()
        {
            this.network.LayerMatrix[0, 0] = int.Parse(this.r1c1.Text);
            this.network.LayerMatrix[1, 0] = int.Parse(this.r1c2.Text);
            this.network.LayerMatrix[2, 0] = int.Parse(this.r1c3.Text);
            this.network.LayerMatrix[3, 0] = int.Parse(this.r1c4.Text);

            this.network.LayerMatrix[0, 1] = int.Parse(this.r2c1.Text);
            this.network.LayerMatrix[1, 1] = int.Parse(this.r2c2.Text);
            this.network.LayerMatrix[2, 1] = int.Parse(this.r2c3.Text);
            this.network.LayerMatrix[3, 1] = int.Parse(this.r2c4.Text);

            this.network.LayerMatrix[0, 2] = int.Parse(this.r3c1.Text);
            this.network.LayerMatrix[1, 2] = int.Parse(this.r3c2.Text);
            this.network.LayerMatrix[2, 2] = int.Parse(this.r3c3.Text);
            this.network.LayerMatrix[3, 2] = int.Parse(this.r3c4.Text);

            this.network.LayerMatrix[0, 3] = int.Parse(this.r4c1.Text);
            this.network.LayerMatrix[1, 3] = int.Parse(this.r4c2.Text);
            this.network.LayerMatrix[2, 3] = int.Parse(this.r4c3.Text);
            this.network.LayerMatrix[3, 3] = int.Parse(this.r4c4.Text);
        }

        /// <summary>
        /// Set the matrix values from the form.
        /// </summary>
        private void SetMatrixValues()
        {
            this.r1c1.Text = "" + this.network.LayerMatrix[0, 0];
            this.r1c2.Text = "" + this.network.LayerMatrix[0, 1];
            this.r1c3.Text = "" + this.network.LayerMatrix[0, 2];
            this.r1c4.Text = "" + this.network.LayerMatrix[0, 3];

            this.r2c1.Text = "" + this.network.LayerMatrix[1, 0];
            this.r2c2.Text = "" + this.network.LayerMatrix[1, 1];
            this.r2c3.Text = "" + this.network.LayerMatrix[1, 2];
            this.r2c4.Text = "" + this.network.LayerMatrix[1, 3];

            this.r3c1.Text = "" + this.network.LayerMatrix[2, 0];
            this.r3c2.Text = "" + this.network.LayerMatrix[2, 1];
            this.r3c3.Text = "" + this.network.LayerMatrix[2, 2];
            this.r3c4.Text = "" + this.network.LayerMatrix[2, 3];

            this.r4c1.Text = "" + this.network.LayerMatrix[3, 0];
            this.r4c2.Text = "" + this.network.LayerMatrix[3, 1];
            this.r4c3.Text = "" + this.network.LayerMatrix[3, 2];
            this.r4c4.Text = "" + this.network.LayerMatrix[3, 3];
        }

        private void btnTrain_Click(object sender, EventArgs e)
        {
            bool[] booleanInput = new bool[4];

            // Collect the input pattern.
            booleanInput[0] = this.input1.SelectedIndex == 1;
            booleanInput[1] = this.input2.SelectedIndex == 1;
            booleanInput[2] = this.input3.SelectedIndex == 1;
            booleanInput[3] = this.input4.SelectedIndex == 1;

            // Train the input pattern.
            this.network.Train(booleanInput);
            this.SetMatrixValues();
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            this.network.LayerMatrix.Clear();
            this.SetMatrixValues();
        }

        private void btnRun_Click(object sender, EventArgs e)
        {
            bool[] booleanInput = new bool[4];

            this.CollectMatrixValues();

            booleanInput[0] = this.input1.SelectedIndex == 1;
            booleanInput[1] = this.input2.SelectedIndex == 1;
            booleanInput[2] = this.input3.SelectedIndex == 1;
            booleanInput[3] = this.input4.SelectedIndex == 1;

            bool[] output = this.network.Present(booleanInput);

            this.output1.Text = output[0] ? "1" : "0";
            this.output2.Text = output[1] ? "1" : "0";
            this.output3.Text = output[2] ? "1" : "0";
            this.output4.Text = output[3] ? "1" : "0";

            if (booleanInput[0] != output[0])
            {
                this.output1.BackColor = Color.Yellow;
            }
            else
            {
                this.output1.BackColor = this.defaultColor;
            }

            if (booleanInput[1] != output[1])
            {
                this.output2.BackColor = Color.Yellow;
            }
            else
            {
                this.output2.BackColor = this.defaultColor;
            }

            if (booleanInput[2] != output[2])
            {
                this.output3.BackColor = Color.Yellow;
            }
            else
            {
                this.output3.BackColor = this.defaultColor;
            }

            if (booleanInput[3] != output[3])
            {
                this.output4.BackColor = Color.Yellow;
            }
            else
            {
                this.output4.BackColor = this.defaultColor;
            }
        }
    }
}
