import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.heaton.bot.*;

/**
 * Example program from Chapter 2
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This application displays a dialog box that allows the user to specify any URL.
 * This URL is requested, using the Bot package, and displayed in the dialog box.
 * Both the body and HTTP headers are displayed.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class ViewURL extends javax.swing.JFrame {

  /**
   * The HTTP connection used by this application.
   */
  HTTP _http;

  /**
   * The constructor.  This method sets up all the
   * components needed by this class.
   * A new HTTPSocket object is also constructed to manange
   * the connection.
   */
  public ViewURL()
  {
    _http = new HTTPSocket();
    //{{INIT_CONTROLS
    setTitle("View URL");
    getContentPane().setLayout(null);
    setSize(495,341);
    setVisible(false);
    getContentPane().add(_pane2);
    _pane2.setBounds(12,168,456,144);
    _body.setEditable(false);
    _pane2.getViewport().add(_body);
    _body.setBounds(0,0,453,141);
    getContentPane().add(_pane1);
    _pane1.setBounds(12,72,456,72);
    _pane1.getViewport().add(_headers);
    _headers.setBounds(0,0,453,0);
    _label3.setText("Body");
    getContentPane().add(_label3);
    _label3.setBounds(12,144,456,12);
    _label1.setText("URL:");
    getContentPane().add(_label1);
    _label1.setBounds(12,12,36,24);
    _url.setText("http://www.jeffheaton.com");
    getContentPane().add(_url);
    _url.setBounds(48,12,348,24);
    _go.setText("Go");
    _go.setActionCommand("Go");
    getContentPane().add(_go);
    _go.setBounds(408,12,60,24);
    _label2.setText("HTTP Headers");
    getContentPane().add(_label2);
    _label2.setBounds(12,48,384,12);
    //}}

    //{{REGISTER_LISTENERS
    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    SymAction lSymAction = new SymAction();
    _go.addActionListener(lSymAction);
    //}}
    setLocation(32,32);
  }

  /**
   * Set the visibility of this window.
   *
   * @param b true for visible, false for invisible
   */
  public void setVisible(boolean b)
  {
    if ( b )
      setLocation(50, 50);
    super.setVisible(b);
  }

  /**
   * The entry point for this application.
   *
   * @param args Arguments are not used by this program.
   * Required for proper main signature.
   */
  static public void main(String args[])
  {
    (new ViewURL()).setVisible(true);
  }

  /**
   * Called to add notification handlers.
   */
  public void addNotify()
  {
    // Record the size of the window prior to calling parents addNotify.
    Dimension size = getSize();

    super.addNotify();

    if ( frameSizeAdjusted )
      return;
    frameSizeAdjusted = true;

    // Adjust size of frame according to the insets
    Insets insets = getInsets();
    setSize(insets.left +
            insets.right +
            size.width,
            insets.top +
            insets.bottom +
            size.height);
  }

  /**
   * Put here by Visual Cafe.
   */
  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS

  /**
   * The bottom scroll pane.
   */
  javax.swing.JScrollPane _pane2 = new javax.swing.JScrollPane();

  /**
   * The area that the body of this HTTP request will be displayed.
   */
  javax.swing.JTextArea _body = new javax.swing.JTextArea();

  /**
   * The top scroll pane.
   */
  javax.swing.JScrollPane _pane1 = new javax.swing.JScrollPane();

  /**
   * The area that the HTTP headers will be stored.
   */
  javax.swing.JTable _headers = new javax.swing.JTable();

  /**
   * A static label.
   */
  javax.swing.JLabel _label3 = new javax.swing.JLabel();

  /**
   * A static label.
   */
  javax.swing.JLabel _label1 = new javax.swing.JLabel();

  /**
   * The URL requested by the user.
   */
  javax.swing.JTextField _url = new javax.swing.JTextField();

  /**
   * The GO button.
   */
  javax.swing.JButton _go = new javax.swing.JButton();

  /**
   * A static label.
   */
  javax.swing.JLabel _label2 = new javax.swing.JLabel();
  //}}

  /**
   * Class created by Visual Cafe
   */
  class SymWindow extends java.awt.event.WindowAdapter {
    public void windowClosed(java.awt.event.WindowEvent event)
    {
      Object object = event.getSource();
      if ( object == ViewURL.this )
        ViewURL_windowClosed(event);
    }

    public void windowClosing(java.awt.event.WindowEvent event)
    {
      Object object = event.getSource();
      if ( object == ViewURL.this )
        ViewURL_WindowClosing(event);
    }
  }

  /**
   * Called when the window closes.
   *
   * @param event The event.
   */
  void ViewURL_WindowClosing(java.awt.event.WindowEvent event)
  {
    // Hide the Frame
    setVisible(false);

    // Free the system resources
    dispose();
  }
  //{{DECLARE_MENUS
  //}}

  /**
   * Class created by Visual Cafe
   */
  class SymAction implements java.awt.event.ActionListener {
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( object == _go )
        Go_actionPerformed(event);
    }
  }

  /**
   * Called when the GO button is clicked.
   *
   * @param event The event.
   */
  void Go_actionPerformed(java.awt.event.ActionEvent event)
  {
    try {
      _http.send(_url.getText(),null);
      _body.setText(_http.getBody());
      _url.setText(_http.getURL());

      TableModel dataModel = new AbstractTableModel()
      {
        public int getColumnCount() { return 2;}
        public int getRowCount() { return _http.getServerHeaders().length();}
        public String getColumnName(int columnIndex)
        {
          switch ( columnIndex ) {
          case 0:return "HTTP Header";
          case 1:return "Value";
          }
          return "";
        }
        public Object getValueAt(int row, int col)
        {
          if ( col==0 )
            return _http.getServerHeaders().get(row).getName();
          else
            return _http.getServerHeaders().get(row).getValue();
        }
      };
      _headers.setModel(dataModel);
      _headers.sizeColumnsToFit(0);
    } catch ( Exception e ) {
      _body.setText(e.toString());
    }
  }

  /**
   * Called once the window closes.
   *
   * @param event The event.
   */
  void ViewURL_windowClosed(java.awt.event.WindowEvent event)
  {
    System.exit(0);

  }
}