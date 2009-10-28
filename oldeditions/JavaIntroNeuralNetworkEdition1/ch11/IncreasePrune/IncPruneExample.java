import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

/**
 * IncPruneExample
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 11
 * Programming Neural Networks in Java
 * http://www.heatonresearch.com/articles/series/1/
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */

public class IncPruneExample extends JFrame implements
ActionListener,Runnable {

  /**
   * The train button.
   */
  JButton btnTrain;

  /**
   * The run button.
   */
  JButton btnRun;

  /**
   * The quit button.
   */
  JButton btnQuit;

  /**
   * The status line.
   */
  JLabel status;

  /**
   * The background worker thread.
   */
  protected Thread worker = null;

/**
 * The number of input neurons.
 */
  protected final static int NUM_INPUT = 2;

/**
 * The number of output neurons.
 */
  protected final static int NUM_OUTPUT = 1;

/**
 * The number of hidden neurons.
 */
  protected final static int NUM_HIDDEN = 3;

/**
 * The learning rate.
 */
  protected final static double RATE = 0.5;

/**
 * The learning momentum.
 */
  protected final static double MOMENTUM = 0.7;


  /**
   * The training data that the user enters.
   * This represents the inputs and expected
   * outputs for the XOR problem.
   */
  protected JTextField data[][] = new JTextField[4][4];

  /**
   * The neural network.
   */
  protected Network network;



  /**
   * Constructor. Setup the components.
   */
  public IncPruneExample()
  {
    setTitle("XOR Solution - Incremental Prune");
    network = new Network(
                         NUM_INPUT,
                         NUM_HIDDEN,
                         NUM_OUTPUT,
                         RATE,
                         MOMENTUM);

    Container content = getContentPane();

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    content.setLayout(gridbag);

    c.fill = GridBagConstraints.NONE;
    c.weightx = 1.0;

    // Training input label
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.NORTHWEST;
    content.add(
               new JLabel(
                         "Enter training data:"),c);

    JPanel grid = new JPanel();
    grid.setLayout(new GridLayout(5,4));
    grid.add(new JLabel("IN1"));
    grid.add(new JLabel("IN2"));
    grid.add(new JLabel("Expected OUT   "));
    grid.add(new JLabel("Actual OUT"));

    for ( int i=0;i<4;i++ ) {
      int x = (i&1);
      int y = (i&2)>>1;
      grid.add(data[i][0] = new JTextField(""+y));
      grid.add(data[i][1] = new JTextField(""+x));
      grid.add(data[i][2] = new JTextField(""+(x^y)));
      grid.add(data[i][3] = new JTextField("??"));
      data[i][0].setEditable(false);
      data[i][1].setEditable(false);
      data[i][3].setEditable(false);
    }

    content.add(grid,c);

    // the button panel
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(btnTrain = new JButton("Train and Prune"));
    buttonPanel.add(btnRun = new JButton("Run"));
    buttonPanel.add(btnQuit = new JButton("Quit"));
    btnTrain.addActionListener(this);
    btnRun.addActionListener(this);
    btnQuit.addActionListener(this);

    // Add the button panel
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.CENTER;
    content.add(buttonPanel,c);

    // Training input label
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.NORTHWEST;
    content.add(
               status = new JLabel("Click train to begin training..."),c);

    // adjust size and position
    pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension d = toolkit.getScreenSize();
    setLocation(
               (int)(d.width-this.getSize().getWidth())/2,
               (int)(d.height-this.getSize().getHeight())/2 );
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);

    btnRun.setEnabled(false);
  }

  /**
   * The main function, just display the JFrame.
   *
   * @param args No arguments are used.
   */
  public static void main(String args[])
  {
    (new IncPruneExample()).show(true);
  }

  /**
   * Called when the user clicks one of the three
   * buttons.
   *
   * @param e The event.
   */
  public void actionPerformed(ActionEvent e)
  {
    if ( e.getSource()==btnQuit )
      System.exit(0);
    else if ( e.getSource()==btnTrain )
      train();
    else if ( e.getSource()==btnRun )
      evaluate();
  }

  /**
   * Called when the user clicks the run button.
   */
  protected void evaluate()
  {
    double xorData[][] = getGrid();
    int update=0;

    for (int i=0;i<4;i++) {
      NumberFormat nf = NumberFormat.getInstance();
      double d[] = network.computeOutputs(xorData[i]);
      data[i][3].setText(nf.format(d[0]));
    }

  }


  /**
  * Called when the user clicks the train button.
  */
  protected void train()
  {
    if ( worker != null )
      worker = null;
    worker = new Thread(this);
    worker.setPriority(Thread.MIN_PRIORITY);
    worker.start();
  }

  /**
  * The thread worker, used for training
  */
  public void run()
  {
    double xorData[][] = getGrid();
    double xorIdeal[][] = getIdeal();
    int update=0;

    Prune prune = new Prune(0.7,0.5,xorData,xorIdeal,0.05);
    prune.startIncramental();

    while (!prune.getDone()) {
      prune.pruneIncramental();
      update++;
      if (update==10) {
        status.setText("Cycles:" + prune.getCycles() + ",Hidden Neurons:" + prune.getHiddenNeuronCount() + ", Current Error=" + prune.getError() );
        update=0;
      }
    }

    status.setText("Best network found:" + prune.getHiddenNeuronCount() + ",error = " + prune.getError() );
    network = prune.getCurrentNetwork();
    btnRun.setEnabled(true);
  }


  /**
   * Called to generate an array of doubles based on
   * the training data that the user has entered.
   *
   * @return An array of doubles
   */
  double [][]getGrid()
  {
    double array[][] = new double[4][2];

    for ( int i=0;i<4;i++ ) {
      array[i][0] =
      Float.parseFloat(data[i][0].getText());
      array[i][1] =
      Float.parseFloat(data[i][1].getText());
    }

    return array;
  }

  /**
   * Called to the ideal values that the neural network
   * should return for each of the grid training values.
   *
   * @return The ideal results.
   */
  double [][]getIdeal()
  {
    double array[][] = new double[4][1];

    for ( int i=0;i<4;i++ ) {
      array[i][0] =
      Float.parseFloat(data[i][2].getText());
    }

    return array;
  }


}