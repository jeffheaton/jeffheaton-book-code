package com.heatonresearch.httprecipes.ch13.recipe4;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.text.*;

import javax.swing.*;

import com.heatonresearch.httprecipes.spider.*;

/**
 * Recipe #13.4: Spider Stats
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * The recipe displays updating statistics for a database
 * based spider.
 * 
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not publish
 * the source code without the author's permission. For more
 * information on distributing this code, please visit:
 * http://www.heatonresearch.com/hr_legal.php
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderStats extends JFrame implements Runnable {
  /**
   * Serial id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Start the program.
   * 
   * @param args
   *          The first argument contains a path to a spider
   *          configuration file.
   */
  public static void main(String args[]) {
    try {
      if (args.length < 1) {
        System.out.println("Usage: SpiderStats [path to config file]");
      } else {
        JFrame frame = new SpiderStats(args[0]);
        frame.setSize(new Dimension(300, 200));
        frame.setVisible(true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Contains information about how to connect to the
   * database.
   */
  private SpiderOptions options;

  /**
   * A JDBC connection.
   */
  private Connection connection;

  /**
   * Get a count by status.
   */
  private final String sqlStatus = "select status,count(*) from spider_workload group by status;";

  /**
   * Get the maximum depth
   */
  private final String sqlDepth = "SELECT MAX(depth) from spider_workload";

  /**
   * Prepared statement for status.
   */
  private PreparedStatement stmtStatus;

  /**
   * Prepared statement for depth.
   */
  private PreparedStatement stmtDepth;

  /**
   * How many URL's are waiting.
   */
  private int waiting;

  /**
   * How many URL's are done.
   */
  private int done;

  /**
   * How many URL's are being processed.
   */
  private int processing;

  /**
   * How many URL's resulted in an error.
   */
  private int error;

  /**
   * What is the maximum depth encountered so far.
   */
  private int depth;

  /**
   * The background thread.
   */
  private Thread thread;

  /**
   * Percent done.
   */
  private double donePercent;

  /**
   * Percent error.
   */
  private double errorPercent;

  /**
   * Construct a SpiderStats object.
   * 
   * @param config
   *          A path to a spider configuration file.
   * @throws IOException
   *           Thrown if an I/O error occurs.
   * @throws SpiderException
   *           Thrown if the options can not be loaded.
   */
  public SpiderStats(String config) throws IOException, SpiderException {
    this.options = new SpiderOptions();
    this.options.load(config);
    this.thread = new Thread(this);
    this.thread.start();
  }

  /**
   * Display the stats.
   * 
   * @param g
   *          The graphics object.
   */
  public void displayStats(Graphics g) {
    final String stat1 = "Total URL\'s Encountered:";
    final String stat2 = "Completed URL\'s:";
    final String stat3 = "Waiting URL\'s:";
    final String stat4 = "URL\'s Currently Processing:";
    final String stat5 = "URL\'s with Errors:";
    final String stat6 = "Deepest URL\'s found:";

    FontMetrics fm = g.getFontMetrics();
    int y = fm.getHeight();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.BLACK);
    int total = this.processing + this.error + this.done + this.waiting;

    NumberFormat numFormat = NumberFormat.getInstance();
    NumberFormat percentFormat = NumberFormat.getPercentInstance();

    if ((this.waiting + this.processing + this.done) == 0) {
      this.donePercent = 0;
    } else {
      this.donePercent = (double) this.done / (double) (this.waiting + this.processing + this.done);
    }

    if (total == 0) {
      this.errorPercent = 0;
    } else {
      this.errorPercent = (double) this.error / (double) total;
    }

    g.drawString(stat1, 10, y);
    g.drawString("" + numFormat.format(total), 200, y);
    y += fm.getHeight();

    g.drawString(stat2, 10, y);
    g.drawString("" + numFormat.format(this.done) + "("
        + percentFormat.format(this.donePercent) + ")", 200, y);
    y += fm.getHeight();

    g.drawString(stat3, 10, y);
    g.drawString("" + numFormat.format(this.waiting), 200, y);
    y += fm.getHeight();

    g.drawString(stat4, 10, y);
    g.drawString("" + numFormat.format(this.processing), 200, y);
    y += fm.getHeight();

    g.drawString(stat5, 10, y);
    g.drawString("" + numFormat.format(this.error) + "("
        + percentFormat.format(this.errorPercent) + ")", 200, y);
    y += fm.getHeight();

    g.drawString(stat6, 10, y);
    g.drawString("" + numFormat.format(this.depth), 200, y);
    y += fm.getHeight();

    displayProgressBar(g, y);

  }

  /**
   * Run the background thread.
   */
  public void run() {
    Graphics g = null;
    for (;;) {
      if (this.connection == null) {
        open();
      }

      if (g == null) {
        g = getContentPane().getGraphics();
      }

      if (g != null) {
        getStats();
        displayStats(g);
      }

      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        return;
      }
    }

  }

  /**
   * Draw a progress bar.
   * 
   * @param g
   *          The graphics object.
   * @param y
   *          The y position to draw the progress bar.
   */
  private void displayProgressBar(Graphics g, int y) {
    int width = getWidth();
    int progressWidth = width - 40;
    g.setColor(Color.GREEN);
    g.fillRect(10, y, (int) (progressWidth * this.donePercent), 16);
    g.setColor(Color.BLACK);
    g.drawRect(10, y, progressWidth, 16);

  }

  /**
   * Load the stats from the database.
   */
  private void getStats() {
    try {
      this.waiting = this.processing = this.error = this.done = 0;
      ResultSet rs = this.stmtStatus.executeQuery();
      while (rs.next()) {
        String status = rs.getString(1);
        int count = rs.getInt(2);
        if (status.equalsIgnoreCase("W")) {
          this.waiting = count;
        } else if (status.equalsIgnoreCase("P")) {
          this.processing = count;
        } else if (status.equals("E")) {
          this.error = count;
        } else if (status.equals("D")) {
          this.done = count;
        }
      }
      rs.close();

      this.depth = 0;
      rs = this.stmtDepth.executeQuery();
      if (rs.next()) {
        this.depth = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Open a connection to the database.
   */
  private void open() {
    try {
      setTitle("Heaton Research Spider");
      Class.forName(this.options.dbClass).newInstance();
      this.connection = DriverManager.getConnection(this.options.dbURL, this.options.dbUID,
          this.options.dbPWD);
      this.stmtStatus = this.connection.prepareStatement(this.sqlStatus);
      this.stmtDepth = this.connection.prepareStatement(this.sqlDepth);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
