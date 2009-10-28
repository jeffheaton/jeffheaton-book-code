import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Hopfield
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 2
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
public class Hopfield extends JFrame implements ActionListener {

  /**
   * The number of neurons in this neural network.
   */
  public static final int NETWORK_SIZE = 4;

  /**
   * The weight matrix for the four fully connected
   * neurons.
   */
  JTextField weightMatrix[][] =
    new JTextField[NETWORK_SIZE][NETWORK_SIZE];

  /**
   * The input pattern, used to either train
   * or run the neural network. When the network
   * is being trained, this is the training
   * data. When the neural network is to be ran
   * this is the input pattern.
   */
  JComboBox input[] = new JComboBox[NETWORK_SIZE];

  /**
   * The output from each of the four neurons.
   */
  JTextField output[] = new JTextField[NETWORK_SIZE];

  /**
   * The clear button. Used to clear the weight
   * matrix.
   */
  JButton btnClear = new JButton("Clear");

  /**
   * The train button. Used to train the
   * neural network.
   */
  JButton btnTrain = new JButton("Train");

  /**
   * The run button. Used to run the neural
   * network.
   */
  JButton btnRun = new JButton("Run");

  /**
   * Constructor, create all of the components and position
   * the JFrame to the center of the screen.
   */
  public Hopfield()
  {
    setTitle("Hopfield Neural Network");

    // create connections panel
    JPanel connections = new JPanel();
    connections.setLayout(
      new GridLayout(NETWORK_SIZE,NETWORK_SIZE) );
    for ( int row=0;row<NETWORK_SIZE;row++ ) {
      for ( int col=0;col<NETWORK_SIZE;col++ ) {
        weightMatrix[row][col] = new JTextField(3);
        weightMatrix[row][col].setText("0");
        connections.add(weightMatrix[row][col]);
      }
    }

    Container content = getContentPane();

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    content.setLayout(gridbag);

    c.fill = GridBagConstraints.NONE;
    c.weightx = 1.0;

    // Weight matrix label
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.NORTHWEST;
    content.add(
      new JLabel("Weight Matrix for the Hopfield Neural Network:"),c);

    // Weight matrix
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    content.add(connections,c);
    c.gridwidth = 1;

    // Input pattern label
    c.anchor = GridBagConstraints.NORTHWEST;
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    content.add(
      new JLabel("Click \"Train\" to train the following pattern:"),c);

    // Input pattern

    String options[] = { "0","1"};

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new FlowLayout());
    for ( int i=0;i<NETWORK_SIZE;i++ ) {
      input[i] = new JComboBox(options);
      inputPanel.add(input[i]);

    }

    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.CENTER;
    content.add(inputPanel,c);

    // Output pattern label
    c.anchor = GridBagConstraints.NORTHWEST;
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    content.add(
      new JLabel("Click \"Run\" to see output pattern:"),c);

    // Output pattern

    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new FlowLayout());
    for ( int i=0;i<NETWORK_SIZE;i++ ) {
      output[i] = new JTextField(3);
      output[i].setEditable(false);
      outputPanel.add(output[i]);
    }
    c.gridwidth = GridBagConstraints.REMAINDER; //end row
    c.anchor = GridBagConstraints.CENTER;
    content.add(outputPanel,c);

    // Buttons

    JPanel buttonPanel = new JPanel();
    btnClear = new JButton("Clear");
    btnTrain = new JButton("Train");
    btnRun = new JButton("Run");
    btnClear.addActionListener(this);
    btnTrain.addActionListener(this);
    btnRun.addActionListener(this);
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(btnClear);
    buttonPanel.add(btnTrain);
    buttonPanel.add(btnRun);
    content.add(buttonPanel,c);

    // adjust size and position
    pack();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension d = toolkit.getScreenSize();
    setLocation(
               (int)(d.width-this.getSize().getWidth())/2,
               (int)(d.height-this.getSize().getHeight())/2 );
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);


  }

  /**
   * Used to dispatch events from the buttons
   * to the handler methods.
   *
   * @param e The event
   */
  public void actionPerformed(ActionEvent e)
  {
    if ( e.getSource()==btnRun )
      run();
    else if ( e.getSource()==btnClear )
      clear();
    else if ( e.getSource()==btnTrain )
      train();
  }

  /**
   * Called when the neural network is to be ran against
   * the input.
   */
  protected void run()
  {
    boolean pattern[] = new boolean[NETWORK_SIZE];
    int wt[][] = new int[NETWORK_SIZE][NETWORK_SIZE];

    for ( int row=0;row<NETWORK_SIZE;row++ )
      for ( int col=0;col<NETWORK_SIZE;col++ )
        wt[row][col]=Integer.parseInt(weightMatrix[row][col].getText());
    for ( int row=0;row<NETWORK_SIZE;row++ ) {
      int i = input[row].getSelectedIndex();
      if ( i==0 )
        pattern[row] = false;
      else
        pattern[row] = true;
    }

    Layer net = new Layer(wt);
    net.activation(pattern);

    for ( int row=0;row<NETWORK_SIZE;row++ ) {
      if ( net.output[row] )
        output[row].setText("1");
      else
        output[row].setText("0");
      if ( net.output[row]==pattern[row] )
        output[row].setBackground(java.awt.Color.green);
      else
        output[row].setBackground(java.awt.Color.red);
    }

  }

  /**
   * Called to clear the weight matrix.
   */
  protected void clear()
  {
    for ( int row=0;row<NETWORK_SIZE;row++ )
      for ( int col=0;col<NETWORK_SIZE;col++ )
        weightMatrix[row][col].setText("0");
  }

  /**
   * Called to train the weight matrix based on the
   * current input pattern.
   */
  protected void train()
  {
    int work[][] = new int[NETWORK_SIZE][NETWORK_SIZE];
    int bi[] = new int[NETWORK_SIZE];

    for ( int x=0;x<NETWORK_SIZE;x++ ) {
      if ( input[x].getSelectedIndex()==0 )
        bi[x] = -1;
      else
        bi[x] = 1;
    }

    for ( int row=0;row<NETWORK_SIZE;row++ )
      for ( int col=0;col<NETWORK_SIZE;col++ ) {
        work[row][col] = bi[row]*bi[col];
      }

    for ( int x=0;x<NETWORK_SIZE;x++ )
      work[x][x] -=1;

    for ( int row=0;row<NETWORK_SIZE;row++ )
      for ( int col=0;col<NETWORK_SIZE;col++ ) {
        int i = Integer.parseInt(weightMatrix[row][col].getText());
        weightMatrix[row][col].setText( "" + (i+work[row][col]));
      }

  }

  /**
   * Main program entry point, display the
   * frame.
   *
   * @param args Command line arguments are not used
   */
  static public void main(String args[])
  {
    JFrame f = new Hopfield();
    f.show();
  }


}