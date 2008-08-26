namespace Chapter08PruneSelective
{
    partial class PruneSelectiveForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.expected1 = new System.Windows.Forms.TextBox();
            this.actual1 = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.textBox5 = new System.Windows.Forms.TextBox();
            this.textBox6 = new System.Windows.Forms.TextBox();
            this.expected2 = new System.Windows.Forms.TextBox();
            this.actual2 = new System.Windows.Forms.TextBox();
            this.textBox9 = new System.Windows.Forms.TextBox();
            this.textBox10 = new System.Windows.Forms.TextBox();
            this.expected3 = new System.Windows.Forms.TextBox();
            this.actual3 = new System.Windows.Forms.TextBox();
            this.textBox13 = new System.Windows.Forms.TextBox();
            this.textBox14 = new System.Windows.Forms.TextBox();
            this.expected4 = new System.Windows.Forms.TextBox();
            this.actual4 = new System.Windows.Forms.TextBox();
            this.btnTrain = new System.Windows.Forms.Button();
            this.btnRun = new System.Windows.Forms.Button();
            this.btnQuit = new System.Windows.Forms.Button();
            this.status = new System.Windows.Forms.Label();
            this.btnPrune = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(0, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(96, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Enter training data:";
            // 
            // textBox1
            // 
            this.textBox1.Enabled = false;
            this.textBox1.Location = new System.Drawing.Point(3, 52);
            this.textBox1.Name = "textBox1";
            this.textBox1.ReadOnly = true;
            this.textBox1.Size = new System.Drawing.Size(100, 20);
            this.textBox1.TabIndex = 1;
            this.textBox1.Text = "0";
            // 
            // textBox2
            // 
            this.textBox2.Enabled = false;
            this.textBox2.Location = new System.Drawing.Point(109, 52);
            this.textBox2.Name = "textBox2";
            this.textBox2.ReadOnly = true;
            this.textBox2.Size = new System.Drawing.Size(100, 20);
            this.textBox2.TabIndex = 2;
            this.textBox2.Text = "0";
            // 
            // expected1
            // 
            this.expected1.Location = new System.Drawing.Point(215, 52);
            this.expected1.Name = "expected1";
            this.expected1.Size = new System.Drawing.Size(100, 20);
            this.expected1.TabIndex = 3;
            this.expected1.Text = "0";
            // 
            // actual1
            // 
            this.actual1.Enabled = false;
            this.actual1.Location = new System.Drawing.Point(321, 52);
            this.actual1.Name = "actual1";
            this.actual1.ReadOnly = true;
            this.actual1.Size = new System.Drawing.Size(100, 20);
            this.actual1.TabIndex = 4;
            this.actual1.Text = "???";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(0, 36);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(27, 13);
            this.label2.TabIndex = 5;
            this.label2.Text = "IN1";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(106, 36);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(27, 13);
            this.label3.TabIndex = 6;
            this.label3.Text = "IN2";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(212, 36);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(84, 13);
            this.label4.TabIndex = 7;
            this.label4.Text = "Expected Out";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label5.Location = new System.Drawing.Point(318, 36);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(67, 13);
            this.label5.TabIndex = 8;
            this.label5.Text = "Actual Out";
            // 
            // textBox5
            // 
            this.textBox5.Enabled = false;
            this.textBox5.Location = new System.Drawing.Point(3, 78);
            this.textBox5.Name = "textBox5";
            this.textBox5.ReadOnly = true;
            this.textBox5.Size = new System.Drawing.Size(100, 20);
            this.textBox5.TabIndex = 9;
            this.textBox5.Text = "0";
            // 
            // textBox6
            // 
            this.textBox6.Enabled = false;
            this.textBox6.Location = new System.Drawing.Point(109, 78);
            this.textBox6.Name = "textBox6";
            this.textBox6.ReadOnly = true;
            this.textBox6.Size = new System.Drawing.Size(100, 20);
            this.textBox6.TabIndex = 10;
            this.textBox6.Text = "1";
            // 
            // expected2
            // 
            this.expected2.Location = new System.Drawing.Point(215, 78);
            this.expected2.Name = "expected2";
            this.expected2.Size = new System.Drawing.Size(100, 20);
            this.expected2.TabIndex = 11;
            this.expected2.Text = "1";
            // 
            // actual2
            // 
            this.actual2.Enabled = false;
            this.actual2.Location = new System.Drawing.Point(321, 78);
            this.actual2.Name = "actual2";
            this.actual2.ReadOnly = true;
            this.actual2.Size = new System.Drawing.Size(100, 20);
            this.actual2.TabIndex = 12;
            this.actual2.Text = "???";
            // 
            // textBox9
            // 
            this.textBox9.Enabled = false;
            this.textBox9.Location = new System.Drawing.Point(3, 104);
            this.textBox9.Name = "textBox9";
            this.textBox9.ReadOnly = true;
            this.textBox9.Size = new System.Drawing.Size(100, 20);
            this.textBox9.TabIndex = 13;
            this.textBox9.Text = "1";
            // 
            // textBox10
            // 
            this.textBox10.Enabled = false;
            this.textBox10.Location = new System.Drawing.Point(109, 104);
            this.textBox10.Name = "textBox10";
            this.textBox10.ReadOnly = true;
            this.textBox10.Size = new System.Drawing.Size(100, 20);
            this.textBox10.TabIndex = 14;
            this.textBox10.Text = "0";
            // 
            // expected3
            // 
            this.expected3.Location = new System.Drawing.Point(215, 104);
            this.expected3.Name = "expected3";
            this.expected3.Size = new System.Drawing.Size(100, 20);
            this.expected3.TabIndex = 15;
            this.expected3.Text = "1";
            // 
            // actual3
            // 
            this.actual3.Enabled = false;
            this.actual3.Location = new System.Drawing.Point(321, 104);
            this.actual3.Name = "actual3";
            this.actual3.ReadOnly = true;
            this.actual3.Size = new System.Drawing.Size(100, 20);
            this.actual3.TabIndex = 16;
            this.actual3.Text = "???";
            // 
            // textBox13
            // 
            this.textBox13.Enabled = false;
            this.textBox13.Location = new System.Drawing.Point(3, 130);
            this.textBox13.Name = "textBox13";
            this.textBox13.ReadOnly = true;
            this.textBox13.Size = new System.Drawing.Size(100, 20);
            this.textBox13.TabIndex = 17;
            this.textBox13.Text = "1";
            // 
            // textBox14
            // 
            this.textBox14.Enabled = false;
            this.textBox14.Location = new System.Drawing.Point(109, 130);
            this.textBox14.Name = "textBox14";
            this.textBox14.ReadOnly = true;
            this.textBox14.Size = new System.Drawing.Size(100, 20);
            this.textBox14.TabIndex = 18;
            this.textBox14.Text = "1";
            // 
            // expected4
            // 
            this.expected4.Location = new System.Drawing.Point(215, 130);
            this.expected4.Name = "expected4";
            this.expected4.Size = new System.Drawing.Size(100, 20);
            this.expected4.TabIndex = 19;
            this.expected4.Text = "0";
            // 
            // actual4
            // 
            this.actual4.Enabled = false;
            this.actual4.Location = new System.Drawing.Point(321, 130);
            this.actual4.Name = "actual4";
            this.actual4.ReadOnly = true;
            this.actual4.Size = new System.Drawing.Size(100, 20);
            this.actual4.TabIndex = 20;
            this.actual4.Text = "???";
            // 
            // btnTrain
            // 
            this.btnTrain.Location = new System.Drawing.Point(3, 174);
            this.btnTrain.Name = "btnTrain";
            this.btnTrain.Size = new System.Drawing.Size(75, 23);
            this.btnTrain.TabIndex = 21;
            this.btnTrain.Text = "Train";
            this.btnTrain.UseVisualStyleBackColor = true;
            this.btnTrain.Click += new System.EventHandler(this.btnTrain_Click);
            // 
            // btnRun
            // 
            this.btnRun.Location = new System.Drawing.Point(240, 174);
            this.btnRun.Name = "btnRun";
            this.btnRun.Size = new System.Drawing.Size(75, 23);
            this.btnRun.TabIndex = 23;
            this.btnRun.Text = "Run";
            this.btnRun.UseVisualStyleBackColor = true;
            this.btnRun.Click += new System.EventHandler(this.btnRun_Click);
            // 
            // btnQuit
            // 
            this.btnQuit.Location = new System.Drawing.Point(346, 174);
            this.btnQuit.Name = "btnQuit";
            this.btnQuit.Size = new System.Drawing.Size(75, 23);
            this.btnQuit.TabIndex = 24;
            this.btnQuit.Text = "Quit";
            this.btnQuit.UseVisualStyleBackColor = true;
            this.btnQuit.Click += new System.EventHandler(this.btnQuit_Click);
            // 
            // status
            // 
            this.status.AutoSize = true;
            this.status.Location = new System.Drawing.Point(0, 206);
            this.status.Name = "status";
            this.status.Size = new System.Drawing.Size(38, 13);
            this.status.TabIndex = 24;
            this.status.Text = "Ready";
            // 
            // btnPrune
            // 
            this.btnPrune.Location = new System.Drawing.Point(125, 174);
            this.btnPrune.Name = "btnPrune";
            this.btnPrune.Size = new System.Drawing.Size(75, 23);
            this.btnPrune.TabIndex = 22;
            this.btnPrune.Text = "Prune";
            this.btnPrune.UseVisualStyleBackColor = true;
            this.btnPrune.Click += new System.EventHandler(this.btnPrune_Click);
            // 
            // PruneSelectiveForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(433, 228);
            this.Controls.Add(this.btnPrune);
            this.Controls.Add(this.status);
            this.Controls.Add(this.btnQuit);
            this.Controls.Add(this.btnRun);
            this.Controls.Add(this.btnTrain);
            this.Controls.Add(this.actual4);
            this.Controls.Add(this.expected4);
            this.Controls.Add(this.textBox14);
            this.Controls.Add(this.textBox13);
            this.Controls.Add(this.actual3);
            this.Controls.Add(this.expected3);
            this.Controls.Add(this.textBox10);
            this.Controls.Add(this.textBox9);
            this.Controls.Add(this.actual2);
            this.Controls.Add(this.expected2);
            this.Controls.Add(this.textBox6);
            this.Controls.Add(this.textBox5);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.actual1);
            this.Controls.Add(this.expected1);
            this.Controls.Add(this.textBox2);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.label1);
            this.Name = "PruneSelectiveForm";
            this.Text = "XOR Problem - Selective Pruning";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.TextBox expected1;
        private System.Windows.Forms.TextBox actual1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.TextBox textBox5;
        private System.Windows.Forms.TextBox textBox6;
        private System.Windows.Forms.TextBox expected2;
        private System.Windows.Forms.TextBox actual2;
        private System.Windows.Forms.TextBox textBox9;
        private System.Windows.Forms.TextBox textBox10;
        private System.Windows.Forms.TextBox expected3;
        private System.Windows.Forms.TextBox actual3;
        private System.Windows.Forms.TextBox textBox13;
        private System.Windows.Forms.TextBox textBox14;
        private System.Windows.Forms.TextBox expected4;
        private System.Windows.Forms.TextBox actual4;
        private System.Windows.Forms.Button btnTrain;
        private System.Windows.Forms.Button btnRun;
        private System.Windows.Forms.Button btnQuit;
        private System.Windows.Forms.Label status;
        private System.Windows.Forms.Button btnPrune;
    }
}

