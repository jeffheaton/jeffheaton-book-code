using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using HeatonResearchNeural.Hopfield;

namespace Chapter03Pattern
{
    /// <summary>
    /// Chapter 3: Hopfield Pattern
    /// 
    /// Graphically recognize a pattern with a Hopfield neural network.
    /// </summary>
    public partial class HopfieldRecForm : Form
    {
        public const int GRID_X = 8;
        public const int GRID_Y = 8;
        public const int CELL_WIDTH = 20;
        public const int CELL_HEIGHT = 20;
        public HopfieldNetwork hopfield;
        private bool[] grid;
        private int margin;


        public HopfieldRecForm()
        {
            InitializeComponent();
            grid = new bool[HopfieldRecForm.GRID_X * HopfieldRecForm.GRID_Y];
            this.hopfield = new HopfieldNetwork(HopfieldRecForm.GRID_X * HopfieldRecForm.GRID_Y);

        }

        private void HopfieldRecForm_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            this.margin = (this.Width - (HopfieldRecForm.CELL_WIDTH * HopfieldRecForm.GRID_X)) / 2;
            int index = 0;

            SolidBrush brush = new SolidBrush(Color.Black);
            Pen pen = new Pen(Color.Black);

            for (int y = 0; y < HopfieldRecForm.GRID_Y; y++)
            {
                for (int x = 0; x < HopfieldRecForm.GRID_X; x++)
                {
                    if (this.grid[index++])
                    {

                        g.FillRectangle(brush, this.margin + (x * HopfieldRecForm.CELL_WIDTH), y
                                * HopfieldRecForm.CELL_HEIGHT, HopfieldRecForm.CELL_WIDTH,
                                HopfieldRecForm.CELL_HEIGHT);
                    }
                    else
                    {

                        g.DrawRectangle(pen, this.margin + (x * HopfieldRecForm.CELL_WIDTH), y
                                * HopfieldRecForm.CELL_HEIGHT, HopfieldRecForm.CELL_WIDTH,
                                HopfieldRecForm.CELL_HEIGHT);
                    }
                }
            }
        }

        private void btnTrain_Click(object sender, EventArgs e)
        {
            this.hopfield.Train(this.grid);
        }

        private void btnGo_Click(object sender, EventArgs e)
        {
            this.grid = this.hopfield.Present(this.grid);
            this.Invalidate();
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            int index = 0;
            for (int y = 0; y < HopfieldRecForm.GRID_Y; y++)
            {
                for (int x = 0; x < HopfieldRecForm.GRID_X; x++)
                {
                    this.grid[index++] = false;
                }
            }

            this.Invalidate();
        }

        private void HopfieldRecForm_MouseDown(object sender, MouseEventArgs e)
        {
            int x = ((e.X - this.margin) / HopfieldRecForm.CELL_WIDTH);
            int y = e.Y / HopfieldRecForm.CELL_HEIGHT;
            if (((x >= 0) && (x < HopfieldRecForm.GRID_X)) && ((y >= 0) && (y < HopfieldRecForm.GRID_Y)))
            {
                int index = (y * HopfieldRecForm.GRID_X) + x;
                this.grid[index] = !this.grid[index];
            }
            this.Invalidate();
        }

    }
}
