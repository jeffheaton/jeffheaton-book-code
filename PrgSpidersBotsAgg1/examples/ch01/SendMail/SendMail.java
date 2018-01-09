import java.awt.*;
import javax.swing.*;

/**
 * Example program from Chapter 1
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 * SendMail is an example of client sockets.  This program
 * presents a simple dialog box that prompts the user for
 * information about how to send a mail.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class SendMail extends javax.swing.JFrame {

  /**
   * The constructor.  Do all basic setup for this
   * application.
   */
  public SendMail()
  {
    //{{INIT_CONTROLS
    setTitle("SendMail Example");
    getContentPane().setLayout(null);
    setSize(736,312);
    setVisible(false);
    JLabel1.setText("From:");
    getContentPane().add(JLabel1);
    JLabel1.setBounds(12,12,36,12);
    JLabel2.setText("To:");
    getContentPane().add(JLabel2);
    JLabel2.setBounds(12,48,36,12);
    JLabel3.setText("Subject:");
    getContentPane().add(JLabel3);
    JLabel3.setBounds(12,84,48,12);
    JLabel4.setText("SMTP Server:");
    getContentPane().add(JLabel4);
    JLabel4.setBounds(12,120,84,12);
    getContentPane().add(_from);
    _from.setBounds(96,12,300,24);
    getContentPane().add(_to);
    _to.setBounds(96,48,300,24);
    getContentPane().add(_subject);
    _subject.setBounds(96,84,300,24);
    getContentPane().add(_smtp);
    _smtp.setBounds(96,120,300,24);
    getContentPane().add(_scrollPane2);
    _scrollPane2.setBounds(12,156,384,108);
    _body.setText("Enter your message here.");
    _scrollPane2.getViewport().add(_body);
    _body.setBounds(0,0,381,105);
    Send.setText("Send");
    Send.setActionCommand("Send");
    getContentPane().add(Send);
    Send.setBounds(60,276,132,24);
    Cancel.setText("Cancel");
    Cancel.setActionCommand("Cancel");
    getContentPane().add(Cancel);
    Cancel.setBounds(216,276,120,24);
    getContentPane().add(_scrollPane);
    _scrollPane.setBounds(408,12,312,288);
    getContentPane().add(_output);
    _output.setBounds(408,12,309,285);
    //}}


    //{{INIT_MENUS
    //}}

    //{{REGISTER_LISTENERS
    SymAction lSymAction = new SymAction();
    Send.addActionListener(lSymAction);
    Cancel.addActionListener(lSymAction);
    //}}

    _output.setModel(_model);
    _model.addElement("Server output displayed here:");
    _scrollPane.getViewport().setView(_output);
    _scrollPane2.getViewport().setView(_body);
  }

  /**
   * Moves the app to the correct position
   * when it is made visible.
   *
   * @param b True to make visible, false to make
   * invisible.
   */
  public void setVisible(boolean b)
  {
    if ( b )
      setLocation(50, 50);
    super.setVisible(b);
  }

  /**
   * The main function basically just creates a new object,
   * then shows it.
   *
   * @param args Command line arguments.
   * Not used in this application.
   */
  static public void main(String args[])
  {
    (new SendMail()).show();
  }

  /**
   * Created by Visual Cafe.  Sets the window size.
   */
  public void addNotify()
  {
    // Record the size of the window prior to
    // calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if ( frameSizeAdjusted )
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the
    // insets and menu bar
    Insets insets = getInsets();
    javax.swing.JMenuBar menuBar =
      getRootPane().getJMenuBar();
    int menuBarHeight = 0;
    if ( menuBar != null )
      menuBarHeight = menuBar.getPreferredSize().height;
    setSize(insets.left
            + insets.right
            + size.width,
            insets.top
            + insets.bottom
            + size.height
            + menuBarHeight);
  }

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS

  /**
   * A label.
   */
  javax.swing.JLabel JLabel1 =
    new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel2 =
    new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel3 =
    new javax.swing.JLabel();

  /**
   * A label.
   */
  javax.swing.JLabel JLabel4 =
    new javax.swing.JLabel();

  /**
   * Who this message is from.
   */
  javax.swing.JTextField _from =
    new javax.swing.JTextField();

  /**
   * Who this message is to.
   */
  javax.swing.JTextField _to =
    new javax.swing.JTextField();

  /**
   * The subject of this message.
   */
  javax.swing.JTextField _subject =
    new javax.swing.JTextField();

  /**
   * The SMTP server to use to send this message.
   */
  javax.swing.JTextField _smtp =
    new javax.swing.JTextField();

  /**
   * A scroll pane.
   */
  javax.swing.JScrollPane _scrollPane2 =
    new javax.swing.JScrollPane();

  /**
   * The body of this email message.
   */
  javax.swing.JTextArea _body =
    new javax.swing.JTextArea();

  /**
   * The send button.
   */
  javax.swing.JButton Send =
    new javax.swing.JButton();

  /**
   * The cancel button.
   */
  javax.swing.JButton Cancel =
    new javax.swing.JButton();

  /**
   * A scroll pain.
   */
  javax.swing.JScrollPane _scrollPane
  = new javax.swing.JScrollPane();

  /**
   * The output area.  Server messages
   * are displayed here.
   */
  javax.swing.JList _output =
    new javax.swing.JList();
  //}}

  /**
   * The list of items added to the output
   * list box.
   */
  javax.swing.DefaultListModel _model
  = new javax.swing.DefaultListModel();

  /**
   * Input from the socket.
   */
  java.io.BufferedReader _in;

  /**
   * Output to the socket.
   */
  java.io.PrintWriter _out;

  //{{DECLARE_MENUS
  //}}


  /**
   * Internal class created by Visual Cafe to
   * route the events to the correct functions.
   *
   * @author Visual Cafe
   * @version 1.0
   */
  class SymAction
    implements java.awt.event.ActionListener {

    /**
     * Route the event to the correction method.
     *
     * @param event The event.
     */
    public void actionPerformed
      (java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( object == Send )
        Send_actionPerformed(event);
      else if ( object == Cancel )
        Cancel_actionPerformed(event);
    }
  }

  /**
   * Called to actually send a string of text to the
   * socket.  This method makes note of the text sent
   * and the response in the JList output box.  Pass a
   * null value to simply wait for a response.
   *
   * @param s A string to be sent to the socket.
   * null to just wait for a response.
   * @exception java.io.IOException
   */
  protected void send(String s) throws java.io.IOException
  {
    // Send the SMTP command
    if ( s!=null ) {
      _model.addElement("C:"+s);
      _out.println(s);
      _out.flush();
    }
    // Wait for the response
    String line = _in.readLine();
    if ( line!=null ) {
      _model.addElement("S:"+line);
    }
  }

  /**
   * Called when the send button is clicked.  Actually
   * sends the mail message.
   *
   * @param event The event.
   */
  void Send_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {

      java.net.Socket s
      = new java.net.Socket( _smtp.getText(),25 );
      _out = new java.io.PrintWriter(s.getOutputStream());
      _in = new java.io.BufferedReader(
        new java.io.InputStreamReader(s.getInputStream()));

      send(null);
      send("HELO " +
           java.net.InetAddress.getLocalHost().getHostName() );
      send("MAIL FROM: " + _from.getText() );
      send("RCPT TO: " + _to.getText() );
      send("DATA");
      _out.println("Subject:" + _subject.getText());
      _out.println( _body.getText() );
      send(".");
      s.close();

    } catch ( Exception e ) {
      _model.addElement("Error: " + e );
    }

  }

  /**
   * Called when cancel is clicked.  End the application.
   *
   * @param event The event.
   */
  void Cancel_actionPerformed(java.awt.event.ActionEvent event)
  {
    System.exit(0);

  }
}