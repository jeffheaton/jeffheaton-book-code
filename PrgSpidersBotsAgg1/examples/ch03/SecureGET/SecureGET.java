import java.awt.*;
import javax.swing.*;
import com.heaton.bot.*;

/**
 * Example program from Chapter 3
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This example displays a mini-browser, similar to
 * chapter 3. This browser supports HTTPS(through
 * JSSE) and also allows access to HTTP password
 * protected sites.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class SecureGET extends javax.swing.JFrame {

  /**
   * Constructor
   */
  public SecureGET()
  {
    //{{INIT_CONTROLS
    setTitle("Secure GET");
    getContentPane().setLayout(null);
    setSize(405,305);
    setVisible(false);
    _url.setText("http://www.jeffheaton.com/secure/");
    getContentPane().add(_url);
    _url.setBounds(12,12,312,24);
    _go.setText("GO!");
    _go.setActionCommand("GO!");
    getContentPane().add(_go);
    _go.setBounds(336,12,60,24);
    JScrollPane1.setOpaque(true);
    getContentPane().add(JScrollPane1);
    JScrollPane1.setBounds(12,48,384,252);
    _body.setEditable(false);
    JScrollPane1.getViewport().add(_body);
    _body.setBounds(0,0,381,249);
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
   * Constructor with a title.
   *
   * @param sTitle The title.
   */
  public SecureGET(String sTitle)
  {
    this();
    setTitle(sTitle);
  }

  /**
   * Added by Visual Cafe.
   *
   * @param b display or not.
   */
  public void setVisible(boolean b)
  {
    if ( b )
      setLocation(50, 50);
    super.setVisible(b);
  }

  /**
   * The main function.  Program starts
   * here.
   *
   * @param args Command line arguments not used.
   */
  static public void main(String args[])
  {
    (new SecureGET()).setVisible(true);
  }

  /**
   * Added by Visual Cafe.
   */
  public void addNotify()
  {
    // Record the size of the window prior to calling
    // parents addNotify.
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
            size.height +
            menuBarHeight);
  }

  /**
   * Added by Visual Cafe
   */
  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS
  javax.swing.JTextField _url = new javax.swing.JTextField();
  javax.swing.JButton _go = new javax.swing.JButton();
  javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
  javax.swing.JTextArea _body = new javax.swing.JTextArea();
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
   * This method is where most of the action happens.
   * When the user clicks GO, this method is executed.
   *
   * @param event The event.
   */
  void Go_actionPerformed(java.awt.event.ActionEvent event)
  {
    boolean done = false;

    // create a HTTP object outside the loop
    HTTPSocket http = new HTTPSocket();

    // loop until non-security error or success
    while ( !done ) {
      // Attempt to connect to the URL with no id/password
      // the first time, then with an id/password on
      // subsequent trys.
      try {
        http.send(_url.getText(),null);
        _body.setText(http.getBody());
        done = true;
      } catch ( Exception e ) {
        // was it a security error?
        if ( e.getMessage().indexOf("401")!=-1 ) {
          // did we already try an id/password
          // if so display error
          if ( (http.getUser().length()!=0) ||
               (http.getPassword().length()!=0) ) {
            JOptionPane.showMessageDialog(this,
                  "Invalid user id/password.",
                  "Security Error",
                  JOptionPane.OK_CANCEL_OPTION,
                  null );
          }
          // prompt the user for id/password
          SecurePrompt prompt = new SecurePrompt(this);
          prompt.show();
          if ( (prompt._uid.getText().length()!=0) ||
               (prompt._pwd.getText().length()!=0) ) {
            // set the id/password for next try
            http.setUser( prompt._uid.getText() );
            http.setPassword( prompt._pwd.getText() );
          } else
            done = true;
        } else
          // something else bad happened, so give up
          done=true;
        _body.setText(e.getMessage());
      }
    }
  }
}