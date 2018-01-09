import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

import com.heaton.bot.*;

/**
 * Example program from Chapter 4
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This program accepts a URL as input and then scans all images
 * from that one page. These images are saved to the specified
 * directory.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class GetImage extends javax.swing.JFrame {

  /**
   * The constructor.
   */
  public GetImage()
  {
    //{{INIT_CONTROLS
    setTitle("Download Images");
    getContentPane().setLayout(null);
    setSize(405,305);
    setVisible(false);
    JLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    JLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
    JLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    JLabel1.setText("Download images from(one page only):");
    getContentPane().add(JLabel1);
    JLabel1.setBounds(12,12,384,24);
    JLabel2.setText("URL:");
    getContentPane().add(JLabel2);
    JLabel2.setBounds(12,36,36,24);
    getContentPane().add(_url);
    _url.setBounds(48,36,348,24);
    JLabel3.setText("Select local path to download images to:");
    getContentPane().add(JLabel3);
    JLabel3.setBounds(12,72,384,24);
    getContentPane().add(_save);
    _save.setBounds(12,96,384,24);
    JScrollPane1.setOpaque(true);
    getContentPane().add(JScrollPane1);
    JScrollPane1.setBounds(12,168,384,132);
    JScrollPane1.getViewport().add(_log);
    _log.setBounds(0,0,381,129);
    _go.setText("GO!");
    getContentPane().add(_go);
    _go.setBounds(84,132,216,24);
    _go.setActionCommand("jbutton");
    //}}

    //{{INIT_MENUS
    //}}

    //{{REGISTER_LISTENERS
    SymAction lSymAction = new SymAction();
    _go.addActionListener(lSymAction);
    //}}
    setLocation(32,32);
  }

  /**
   * Added by Visual Cafe.
   *
   * @param b
   */
  public void setVisible(boolean b)
  {
    if ( b )
      setLocation(50, 50);
    super.setVisible(b);
  }

  /**
   * Program entry point.
   *
   * @param args
   */
  static public void main(String args[])
  {
    (new GetImage()).setVisible(true);
  }

  /**
   * Added by Visual Cafe.
   */
  public void addNotify()
  {
    // Record the size of the window prior to calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if ( frameSizeAdjusted )
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the insets and menu bar
    Insets insets = getInsets();
    javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
    int menuBarHeight = 0;
    if ( menuBar != null )
      menuBarHeight = menuBar.getPreferredSize().height;
    setSize(insets.left +
            insets.right +
            size.width,
            insets.top +
            insets.bottom +
            size.height + menuBarHeight);
  }

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS
  javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
  javax.swing.JLabel JLabel2 = new javax.swing.JLabel();

  /**
   * The URL to be scanned for images.
   */
  javax.swing.JTextField _url = new javax.swing.JTextField();
  javax.swing.JLabel JLabel3 = new javax.swing.JLabel();

  /**
   * The directory that these images are to be saved in.
   */
  javax.swing.JTextField _save = new javax.swing.JTextField();
  javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();

  /**
   * Progress display.
   */
  javax.swing.JList _log = new javax.swing.JList();

  /**
   * Button to be pressed to start the scan.
   */
  javax.swing.JButton _go = new javax.swing.JButton();
  //}}

  //{{DECLARE_MENUS
  //}}


  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( object == _go )
        Go_actionPerformed(event);
    }
  }

  /**
   * Called for each image. The name(URL) of each image
   * is passed to this method so it can be retrieved and
   * saved.
   *
   * @param name The complete URL to an image to save.
   */
  protected void processImage(String name)
  {
    try {
      if ( _save.getText().length()>0 ) {
        int i = name.lastIndexOf('/');
        if ( i!=-1 ) {
          FileOutputStream fso
          = new FileOutputStream(
                                new File(_save.getText(),name.substring(i)) );
          HTTPSocket http = new HTTPSocket();
          http.send(name,null);
          fso.write( http.getBody().getBytes("8859_1") );
          fso.close();
        }
      }
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(this,
                                    e,
                                    "Error",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    null );
    }
  }

  /**
   * This is where most of the action takes place. This
   * method is called when the GO! button is pressed.
   *
   * @param event The event
   */
  void Go_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {
      // open the connection and get the page.
      HTTPSocket http = new HTTPSocket();
      HTMLPage page = new HTMLPage(http);
      page.open(_url.getText(),null);

      // look at the images.
      Vector vec = page.getImages();
      if ( vec.size()>0 ) {
        // copy the images to an array for display
        String array[] = new String[vec.size()];
        vec.copyInto(array);
        _log.setListData( array );
        // loop through and process each image
        for ( int i=0;i<vec.size();i++ )
          processImage((String)vec.elementAt(i));
      }
    } catch ( Exception e ) {
      String s[] = new String[1];
      s[0] = "Error: " + e;
      _log.setListData( s );
    }
  }
}