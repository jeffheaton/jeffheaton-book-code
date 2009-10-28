import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

/**
 * TravelingSalesman2
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 9
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

public class TravelingSalesman
  extends JFrame {

  /**
   * How many cities to use.
   */
  public static final int CITY_COUNT = 50;


  /**
   * Starting temperature for simulated annealing
   */
    public static final double START_TEMPERATURE = 10;


      /**
   * The temperature delta for simulated annealing
   */
    public static final double TEMPERATURE_DELTA = 0.99;

  /**
   * A Map object that will display the city map.
   */
  protected Map map = null;

  /**
   * The current status. Used to display the current status to the
   * user.
   */
  protected JLabel status;

  /**
   * The background worker thread.
   */
  public SimulateAnnealing worker = null;

  /**
   * Is the thread started.
   */
  protected boolean started = false;

  /**
   * The list of cities.
   */
  public City [] cities;

  /**
   * The constructor
   */
  public TravelingSalesman() {
    setSize(300,300);
    setTitle("Traveling Salesman Problem2");

    getContentPane().setLayout(new BorderLayout());

    if ( map == null ) {
      map = new Map(this);
      getContentPane().add(map,"Center");
      status = new JLabel("Starting up");
      getContentPane().add(status,"South");
    }
    start();

  }

  /**
   * Start the background thread.
   */
  public void start() {
// create a random list of cities

    cities = new City[TravelingSalesman.CITY_COUNT];
    for ( int i=0;i<TravelingSalesman.CITY_COUNT;i++ ) {
      cities[i] = new City(
        (int)(Math.random()*(getBounds().width-10)),
        (int)(Math.random()*(getBounds().height-60)));
    }

// start up the background thread
    started = true;
    map.update(map.getGraphics());

    if ( worker != null )
        worker = null;
    worker = new SimulateAnnealing(this);
    worker.setPriority(Thread.MIN_PRIORITY);
    worker.start();
  }

  public void paint()
  {
    map.update(getGraphics());
  }

  /**
   * Display the current status
   *
   * @param status The current status.
   */
  public void setStatus(String status)
  {
    this.status.setText(status);
  }

  /**
   * The main method.
   *
   * @param args Not used
   */
  public static void main(String args[])
  {
    (new TravelingSalesman()).show();
  }

}
