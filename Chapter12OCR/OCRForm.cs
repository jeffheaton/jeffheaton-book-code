using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.IO;

using HeatonResearchNeural.SOM;

namespace Chapter12OCR
{
    public partial class OCRForm : Form
    {
        /**
 * The downsample width for the application.
 */
        const int DOWNSAMPLE_WIDTH = 5;

        /**
         * The down sample height for the application.
         */
        const int DOWNSAMPLE_HEIGHT = 7;

        private Bitmap entryImage;
        private Graphics entryGraphics;
        private int entryLastX;
        private int entryLastY;
        private Pen blackPen;
        private bool[] downsampled;
        private Dictionary<char, bool[]> letterData = new Dictionary<char, bool[]>();
        private double[][] trainingSet;
        private SelfOrganizingMap network;

        public OCRForm()
        {
            InitializeComponent();
            blackPen = new Pen(Color.Black);
            entryImage = new Bitmap(entry.Width, entry.Height);
            entryGraphics = Graphics.FromImage(entryImage);
            downsampled = new bool[OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH];
            ClearEntry();
        }

        private void entry_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;
            g.DrawImage(entryImage, 0, 0);
            Pen blackPen = new Pen(Color.Black);
            g.DrawRectangle(blackPen, 0, 0, entry.Width - 1, entry.Height - 1);

        }

        private void btnDelete_Click(object sender, EventArgs e)
        {
            string str = (string)this.letters.Items[this.letters.SelectedIndex];
            char ch = str[0];
            this.letterData.Remove(ch);
            this.letters.Items.Remove(str);
            ClearEntry();
        }

        private void btnLoad_Click(object sender, EventArgs e)
        {
            try
            {
                TextReader f = new StreamReader("..\\..\\..\\Data\\Sample.dat");

                String line;

                this.letterData.Clear();
                this.letters.Items.Clear();

                while ((line = f.ReadLine()) != null)
                {
                    int sampleSize = OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH;
                    char ch = char.ToUpper(line[0]);
                    bool[] sample = new bool[sampleSize];

                    int idx = 2;
                    for (int i = 0; i < sampleSize; i++)
                    {
                        if (line[idx++] == '1')
                            sample[i] = true;
                        else
                            sample[i] = false;
                    }

                    this.letterData.Add(ch, sample);
                    this.letters.Items.Add("" + ch);
                }

                f.Close();

                MessageBox.Show(this, "Loaded from 'sample.dat'.");

            }
            catch (Exception ex)
            {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            try
            {
                TextWriter f = new StreamWriter("..\\..\\..\\Data\\Sample.dat");
                int size = OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH;

                for (int i = 0; i < this.letters.Items.Count; i++)
                {
                    char ch = ((string)this.letters.Items[i])[0];
                    bool[] data = this.letterData[ch];

                    f.Write(ch + ":");
                    for (int j = 0; j < size; j++)
                    {
                        f.Write(data[j] ? "1" : "0");

                    }
                    f.WriteLine("");


                }
                f.Close();
                MessageBox.Show("Saved to 'sample.dat'.");


            }
            catch (Exception e2)
            {
                MessageBox.Show("Error: " + e2.Message, "Training");
            }
        }

        private void btnBeginTraining_Click(object sender, EventArgs e)
        {
            int inputCount = OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH;
            int letterCount = this.letters.Items.Count;
            this.trainingSet = new double[letterCount][];
            int index = 0;
            foreach (char ch in this.letterData.Keys)
            {
                this.trainingSet[index] = new double[inputCount];
                bool[] data = this.letterData[ch];
                for (int i = 0; i < inputCount; i++)
                {
                    this.trainingSet[index][i] = data[i] ? 0.5 : -0.5;
                }
                index++;
            }

            network = new SelfOrganizingMap(inputCount, letterCount, NormalizationType.Z_AXIS);

            this.ThreadProc();

            //ThreadStart ts = new ThreadStart(ThreadProc);
            //Thread thread = new Thread(ts);
            //thread.Start();
        }

        private void btnAdd_Click(object sender, EventArgs e)
        {
            DownSample ds = new DownSample(this.entryImage);
            this.downsampled = ds.downSample(OCRForm.DOWNSAMPLE_WIDTH, OCRForm.DOWNSAMPLE_HEIGHT);
            this.sample.Invalidate();
            String Prompt = "What letter did you just draw.";
            String Title = "Input Required";
            String Default = "";
            Int32 XPos = ((SystemInformation.WorkingArea.Width / 2) - 200);
            Int32 YPos = ((SystemInformation.WorkingArea.Height / 2) - 100);

            bool valid = false;
            for (int i = 0; i < this.downsampled.Length; i++)
            {
                if (this.downsampled[i])
                {
                    valid = true;
                }
            }

            if (!valid)
            {
                MessageBox.Show("Please draw a letter before adding it.");
                return;
            }

            String Result = Microsoft.VisualBasic.Interaction.InputBox(Prompt, Title, Default, XPos, YPos);
            if (Result != null)
            {
                Result = Result.ToUpper();
                if (Result.Length == 0)
                {
                    MessageBox.Show("Please enter a character.");
                }
                else if (Result.Length > 1)
                {
                    MessageBox.Show("Please enter only a single character.");
                }
                else if (this.letterData.ContainsKey(Result[0]))
                {
                    MessageBox.Show("That letter is already defined, please delete first.");
                }
                else
                {
                    this.letters.Items.Add(Result);
                    this.letterData.Add(Result[0], this.downsampled);
                    this.ClearEntry();
                }
            }


        }

        private void btnRecognize_Click(object sender, EventArgs e)
        {
            DownSample ds = new DownSample(this.entryImage);
            this.downsampled = ds.downSample(OCRForm.DOWNSAMPLE_WIDTH, OCRForm.DOWNSAMPLE_HEIGHT);
            this.sample.Invalidate();

            if (this.network == null)
            {
                MessageBox.Show("I need to be trained first!");
                return;
            }

            int sampleSize = OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH;
            double[] input = new double[sampleSize];

            for (int i = 0; i < sampleSize; i++)
            {
                input[i] = this.downsampled[i] ? 0.5 : -0.5;
            }

            int best = this.network.Winner(input);
            char[] map = mapNeurons();
            MessageBox.Show("  " + map[best] + "   (Neuron #"
                            + best + " fired)", "That Letter Is");
            ClearEntry();
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            ClearEntry();
        }

        private void btnSample_Click(object sender, EventArgs e)
        {
            DownSample ds = new DownSample(this.entryImage);
            this.downsampled = ds.downSample(OCRForm.DOWNSAMPLE_WIDTH, OCRForm.DOWNSAMPLE_HEIGHT);
            this.sample.Invalidate();
        }
        public void ClearEntry()
        {
            Brush whiteBrush = new SolidBrush(Color.White);
            entryGraphics.FillRectangle(whiteBrush, 0, 0, entry.Width, entry.Height);
            entry.Invalidate();
            DownSample ds = new DownSample(this.entryImage);
            this.downsampled = ds.downSample(OCRForm.DOWNSAMPLE_WIDTH, OCRForm.DOWNSAMPLE_HEIGHT);
            this.sample.Invalidate();
        }

        private void entry_MouseDown(object sender, MouseEventArgs e)
        {
            entry.Capture = true;
            entryLastX = e.X;
            entryLastY = e.Y;
        }

        private void entry_MouseUp(object sender, MouseEventArgs e)
        {
            entryGraphics.DrawLine(blackPen, entryLastX, entryLastY, e.X, e.Y);
            entry.Invalidate();
            entry.Capture = false;
        }

        private void entry_MouseMove(object sender, MouseEventArgs e)
        {
            if (entry.Capture == true)
            {
                entryGraphics.DrawLine(blackPen, entryLastX, entryLastY, e.X, e.Y);
                entry.Invalidate();
                entryLastX = e.X;
                entryLastY = e.Y;
            }
        }

        private void sample_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            int x, y;
            int vcell = sample.Height / OCRForm.DOWNSAMPLE_HEIGHT;
            int hcell = sample.Width / OCRForm.DOWNSAMPLE_WIDTH;
            Brush whiteBrush = new SolidBrush(Color.White);
            Brush blackBrush = new SolidBrush(Color.Black);
            Pen blackPen = new Pen(Color.Black);

            g.FillRectangle(whiteBrush, 0, 0, sample.Width, sample.Height);



            for (y = 0; y < OCRForm.DOWNSAMPLE_HEIGHT; y++)
            {
                g.DrawLine(blackPen, 0, y * vcell, sample.Width, y * vcell);
            }
            for (x = 0; x < OCRForm.DOWNSAMPLE_WIDTH; x++)
            {
                g.DrawLine(blackPen, x * hcell, 0, x * hcell, sample.Height);
            }

            int index = 0;
            for (y = 0; y < OCRForm.DOWNSAMPLE_HEIGHT; y++)
            {
                for (x = 0; x < OCRForm.DOWNSAMPLE_WIDTH; x++)
                {
                    if (this.downsampled[index++])
                    {
                        g.FillRectangle(blackBrush, x * hcell, y * vcell, hcell, vcell);
                    }
                }
            }

            g.DrawRectangle(blackPen, 0, 0, sample.Width - 1, sample.Height - 1);
        }

        private void letters_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (this.letters.SelectedIndex >= 0)
            {
                string str = (string)this.letters.Items[this.letters.SelectedIndex];
                char ch = str[0];
                this.downsampled = this.letterData[ch];
                this.sample.Invalidate();
            }
        }

        public void ThreadProc()
        {
            TrainSelfOrganizingMap train = new TrainSelfOrganizingMap(
                this.network, this.trainingSet, TrainSelfOrganizingMap.LearningMethod.SUBTRACTIVE, 0.5);

            int tries = 1;

            do
            {
                train.Iteration();
                this.txtTries.Text = "" + tries;
                this.txtBestError.Text = "" + train.BestError;
                this.txtLastError.Text = "" + train.TotalError;
                tries++;
                Application.DoEvents();
            } while (train.TotalError > 0.01 && (tries <= 100));
            MessageBox.Show("Training complete.");
        }

        /**
 * Used to map neurons to actual letters.
 * 
 * @return The current mapping between neurons and letters as an array.
 */
        public char[] mapNeurons()
        {
            char[] map = new char[this.letters.Items.Count];

            for (int i = 0; i < map.Length; i++)
            {
                map[i] = '?';
            }
            for (int i = 0; i < this.letters.Items.Count; i++)
            {
                double[] input = new double[OCRForm.DOWNSAMPLE_HEIGHT * OCRForm.DOWNSAMPLE_WIDTH];
                char ch = ((string)(this.letters.Items[i]))[0];
                bool[] data = this.letterData[ch];
                for (int j = 0; j < input.Length; j++)
                {
                    input[j] = data[j] ? 0.5 : -0.5;
                }

                int best = this.network.Winner(input);
                map[best] = ch;
            }
            return map;
        }

    }
}
