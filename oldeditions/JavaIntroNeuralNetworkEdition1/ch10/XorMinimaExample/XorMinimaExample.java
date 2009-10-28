import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

/**
 * XorMinimaExample
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 10
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
public class XorMinimaExample extends JFrame implements
ActionListener,Runnable {


  /**
   * Back Propagation
   */
  public static final int METHOD_BACKPROP = 1;

  /**
   * Simulated Annealing
   */
  public static final int METHOD_ANNEAL = 2;

  /**
   * Genetic Algorithm
   */
  public static final int METHOD_GENETIC = 3;

  /**
   * Training mode
   */
  protected int mode = METHOD_BACKPROP;

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
   * The genetic button.
   */
  JButton btnGenetic;

  /**
   * The anneal button.
   */
  JButton btnAnneal;

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
  public XorMinimaExample()
  {
    setTitle("XOR Solution - Minima");
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
    buttonPanel.add(btnTrain = new JButton("Train"));
    buttonPanel.add(btnGenetic = new JButton("Genetic"));
    buttonPanel.add(btnAnneal = new JButton("Anneal"));
    buttonPanel.add(btnRun = new JButton("Run"));
    buttonPanel.add(btnQuit = new JButton("Quit"));
    btnTrain.addActionListener(this);
    btnRun.addActionListener(this);
    btnQuit.addActionListener(this);
    btnAnneal.addActionListener(this);
    btnGenetic.addActionListener(this);

    // Add the button panel
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.CENTER;
    content.add(buttonPanel,c);

    // Training input label
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.NORTHWEST;
    content.add(
               status = new JLabel("Click train, anneal or genetic to begin..."),c);

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
    (new XorMinimaExample()).show(true);
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
      begin(METHOD_BACKPROP);
    else if ( e.getSource()==btnAnneal )
      begin(METHOD_ANNEAL);
    else if ( e.getSource()==btnGenetic )
      begin(METHOD_GENETIC);
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
   * Called to begin the background thread for either
   * back propagation, simulated annealing or genetic
   * algorithm training.
   *
   * @param mode Which training method to use.
   */
  protected void begin(int mode)
  {
    this.mode = mode;

    if ( worker != null )
      worker = null;
    worker = new Thread(this);
    worker.setPriority(Thread.MIN_PRIORITY);
    worker.start();
  }
  /**
   * The background thread. Will dispatch to anneal, genetic
   * or backprop.
   */

  public void run()
  {
    switch (mode) {
    case METHOD_BACKPROP:
      backprop();
      break;
    case METHOD_ANNEAL:
      anneal();
      break;
    case METHOD_GENETIC:
      genetic();
      break;
    }
  }

  /**
   * Train the neural network with back propagation.
   */
  public void backprop()
  {
    double xorData[][] = getGrid();
    double xorIdeal[][] = getIdeal();
    int update=0;

    int max = 10000;
    for (int i=0;i<max;i++) {
      for (int j=0;j<xorData.length;j++) {
        network.computeOutputs(xorData[j]);
        network.calcError(xorIdeal[j]);
        network.learn();
      }


      update++;
      if (update==100) {
        status.setText( "Cycles Left:" + (max-i) + ",Error:" + network.getError(xorData.length) );
        update=0;
      }
    }
    btnRun.setEnabled(true);
  }

  /**
   * Train the neural network with simulated
   * annealing.
   */
  protected void anneal()
  {
    double xorData[][] = getGrid();
    double xorIdeal[][] = getIdeal();
    int update=0;

    status.setText("Starting...");
    btnRun.setEnabled(false);
    btnGenetic.setEnabled(false);
    btnAnneal.setEnabled(false);

    SimulateAnnealing anneal = new SimulateAnnealing(network);
    anneal.start(10,2,100);
    anneal.setInput(xorData);
    anneal.setIdeal(xorIdeal);

    for (int x=0;x<100;x++) {
      anneal.anneal();
      status.setText("Anneal Cycle = " + x + ",Error = " + anneal.getGlobalError());
    }

    btnRun.setEnabled(true);
    btnGenetic.setEnabled(true);
    btnAnneal.setEnabled(true);
  }

  /**
   * Train the neural network with a genetic algorithm.
   */
  protected void genetic()
  {
    double xorData[][] = getGrid();
    double xorIdeal[][] = getIdeal();
    int update=0;
    status.setText("Starting...");
    Genetic genetic = new Genetic(network);
    genetic.setInput(xorData);
    genetic.setIdeal(xorIdeal);
    genetic.start();

    btnRun.setEnabled(false);
    btnGenetic.setEnabled(false);
    btnAnneal.setEnabled(false);

    int x=0;
    while (genetic.getGlobalError()>.1) {
      x++;
      genetic.generation();
      status.setText("Genetic Cycle = " + x + ",Error = " + genetic.getGlobalError());
    }

    btnRun.setEnabled(true);
    btnGenetic.setEnabled(true);
    btnAnneal.setEnabled(true);
  }



}