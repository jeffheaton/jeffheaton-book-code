import java.awt.*;
import javax.swing.*;
import java.text.*;

/**
 * TestKohonen
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 6
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

public class TestKohonen
  extends JFrame
  implements NeuralReportable,Runnable {

  /**
   * How many input neurons to use.
   */
  public static final int INPUT_COUNT=2;

  /**
   * How many output neurons to use.
   */
  public static final int OUTPUT_COUNT=7;

  /**
   * How many random samples to generate.
   */
  public static final int SAMPLE_COUNT=100;

  /**
   * The unit length in pixels, which is the max of the
   * height and width of the window.
   */
  protected int unitLength;

  /**
   * How many retries so far.
   */
  protected int retry=1;

  /**
   * The current error percent.
   */
  protected double totalError=0;

  /**
   * The best error percent.
   */
  protected double bestError = 0;
  /**
   * The neural network.
   */

  protected KohonenNetwork net;

  /**
   * The training set.
   */
  protected TrainingSet ts;

  /**
   * The offscreen image. Used to precent flicker.
   */
  protected Image offScreen;

  /**
   * The constructor sets up the position and size of
   * the window.
   */
  TestKohonen()
  {
    setTitle("Training a Kohonen Neural Network");
    setSize(400,450);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension d = toolkit.getScreenSize();
    setLocation(
               (int)(d.width-this.getSize().getWidth())/2,
               (int)(d.height-this.getSize().getHeight())/2 );
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setResizable(false);
  }


  /**
   * Update is called by the neural network as the
   * network is trained.
   *
   * @param retry What retry number this is.
   * @param totalError The error for this retry.
   * @param bestError The best error so far.
   */
  public void update(int retry,double totalError,double bestError)
  {
    this.retry = retry;
    this.totalError = totalError;
    this.bestError = bestError;
    this.paint(null);


  }

  /**
   * Called to run the background thread. The background thread
   * sets up the neural network and training data and begins
   * training the network.
   */
  public void run()
  {
    // build the training set
    ts = new TrainingSet(INPUT_COUNT,OUTPUT_COUNT);
    ts.setTrainingSetCount(SAMPLE_COUNT);

    for ( int i=0;i<SAMPLE_COUNT;i++ ) {
      for ( int j=0;j<INPUT_COUNT;j++ ) {
        ts.setInput(i,j,Math.random());
      }
    }

    // build and train the neural network
    net = new KohonenNetwork(INPUT_COUNT,OUTPUT_COUNT,this);
    net.setTrainingSet(ts);
    net.learn();
  }

  /**
   * Display the progress of the neural network.
   *
   * @param g A graphics object.
   */
  public void paint(Graphics g)
  {
    if ( net==null )
      return;
    if ( offScreen==null ) {
      offScreen = this.createImage(
        (int)getBounds().getWidth(),
        (int)getBounds().getHeight());
    }
    g = offScreen.getGraphics();
    int width = (int)getContentPane().bounds().getWidth();
    int height = (int)getContentPane().bounds().getHeight();
    unitLength = Math.min(width,height);
    g.setColor(Color.black);
    g.fillRect(0,0,width,height);

    // plot the weights of the output neurons
    g.setColor(Color.white);
    for ( int y=0;y<net.outputWeights.length;y++ ) {

      g.fillRect((int)(net.outputWeights[y][0]*unitLength),
                 (int)(net.outputWeights[y][1]*unitLength),10,10);

    }

    // plot a grid of samples to test the net with
    g.setColor(Color.green);
    for ( int y=0;y<unitLength;y+=50 ) {
      for ( int x=0;x<unitLength;x+=50 ) {
        g.fillOval(x,y,5,5);
        double d[] = new double[2];
        d[0]=x;
        d[1]=y;
        double normfac[] = new double[1];
        double synth[] = new double[1];
        int c = net.winner(d,normfac,synth);


        int x2=(int)(net.outputWeights[c][0]*unitLength);
        int y2=(int)(net.outputWeights[c][1]*unitLength);

        g.drawLine(x,y,x2,y2);
      }

    }

    // display the status info
    g.setColor(Color.white);
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);
    g.drawString(
                "retry = "
                + retry
                + ",current error = "
                + nf.format(totalError*100)
                +  "%, best error = "
                + nf.format(bestError*100)
                +"%", 0,
                (int)getContentPane().getBounds().getHeight());
    getContentPane().getGraphics().drawImage(offScreen,0,0,this);

  }


  /**
   * Startup the program.
   *
   * @param args Not used.
   */
  public static void main(String args[])
  {
    TestKohonen app = new TestKohonen();
    app.show();
    Thread t = new Thread(app);
    t.setPriority(Thread.MIN_PRIORITY);
    t.start();


  }

}