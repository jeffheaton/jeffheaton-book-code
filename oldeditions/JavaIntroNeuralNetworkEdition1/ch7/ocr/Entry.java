import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * Entry
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 7
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
public class Entry extends JPanel {

  /**
   * The image that the user is drawing into.
   */
  protected Image entryImage;

  /**
   * A graphics handle to the image that the
   * user is drawing into.
   */
  protected Graphics entryGraphics;

  /**
   * The last x that the user was drawing at.
   */
  protected int lastX = -1;

  /**
   * The last y that the user was drawing at.
   */
  protected int lastY = -1;

  /**
   * The down sample component used with this
   * component.
   */
  protected Sample sample;

  /**
   * Specifies the left boundary of the cropping
   * rectangle.
   */
  protected int downSampleLeft;

  /**
   * Specifies the right boundary of the cropping
   * rectangle.
   */
  protected int downSampleRight;

  /**
   * Specifies the top boundary of the cropping
   * rectangle.
   */
  protected int downSampleTop;

  /**
   * Specifies the bottom boundary of the cropping
   * rectangle.
   */
  protected int downSampleBottom;

  /**
   * The downsample ratio for x.
   */
  protected double ratioX;

  /**
   * The downsample ratio for y
   */
  protected double ratioY;

  /**
   * The pixel map of what the user has drawn.
   * Used to downsample it.
   */
  protected int pixelMap[];


  /**
   * The constructor.
   */
  Entry()
  {
    enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|
                 AWTEvent.MOUSE_EVENT_MASK|
                 AWTEvent.COMPONENT_EVENT_MASK);
  }


  /**
   * Setup the internal image that the user
   * draws onto.
   */
  protected void initImage()
  {
    entryImage = createImage(getWidth(),getHeight());
    entryGraphics = entryImage.getGraphics();
    entryGraphics.setColor(Color.white);
    entryGraphics.fillRect(0,0,getWidth(),getHeight());
  }

  /**
   * Paint the drawn image and cropping box
   * (if active).
   *
   * @param g The graphics context
   */
  public void paint(Graphics g)
  {
    if ( entryImage==null )
      initImage();
    g.drawImage(entryImage,0,0,this);
    g.setColor(Color.black);
    g.drawRect(0,0,getWidth(),getHeight());
    g.setColor(Color.red);
    g.drawRect(downSampleLeft,
               downSampleTop,
               downSampleRight-downSampleLeft,
               downSampleBottom-downSampleTop);

  }

  /**
   * Process messages.
   *
   * @param e The event.
   */
  protected void processMouseEvent(MouseEvent e)
  {
    if ( e.getID()!=MouseEvent.MOUSE_PRESSED )
      return;
    lastX = e.getX();
    lastY = e.getY();
  }

  /**
   * Process messages.
   *
   * @param e The event.
   */
  protected void processMouseMotionEvent(MouseEvent e)
  {
    if ( e.getID()!=MouseEvent.MOUSE_DRAGGED )
      return;

    entryGraphics.setColor(Color.black);
    entryGraphics.drawLine(lastX,lastY,e.getX(),e.getY());
    getGraphics().drawImage(entryImage,0,0,this);
    lastX = e.getX();
    lastY = e.getY();
  }

  /**
   * Set the sample control to use. The
   * sample control displays a downsampled
   * version of the character.
   *
   * @param s
   */
  public void setSample(Sample s)
  {
    sample = s;
  }

  /**
   * Get the down sample component to be used
   * with this component.
   *
   * @return The down sample component.
   */
  public Sample getSample()
  {
    return sample;
  }

  /**
   * This method is called internally to
   * see if there are any pixels in the given
   * scan line. This method is used to perform
   * autocropping.
   *
   * @param y The horizontal line to scan.
   * @return True if there were any pixels in this
   * horizontal line.
   */
  protected boolean hLineClear(int y)
  {
    int w = entryImage.getWidth(this);
    for ( int i=0;i<w;i++ ) {
      if ( pixelMap[(y*w)+i] !=-1 )
        return false;
    }
    return true;
  }

  /**
   * This method is called to determine ....
   *
   * @param x The vertical line to scan.
   * @return True if there are any pixels in the
   * specified vertical line.
   */
  protected boolean vLineClear(int x)
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);
    for ( int i=0;i<h;i++ ) {
      if ( pixelMap[(i*w)+x] !=-1 )
        return false;
    }
    return true;
  }

  /**
   * This method is called to automatically
   * crop the image so that whitespace is
   * removed.
   *
   * @param w The width of the image.
   * @param h The height of the image
   */
  protected void findBounds(int w,int h)
  {
    // top line
    for ( int y=0;y<h;y++ ) {
      if ( !hLineClear(y) ) {
        downSampleTop=y;
        break;
      }

    }
    // bottom line
    for ( int y=h-1;y>=0;y-- ) {
      if ( !hLineClear(y) ) {
        downSampleBottom=y;
        break;
      }
    }
    // left line
    for ( int x=0;x<w;x++ ) {
      if ( !vLineClear(x) ) {
        downSampleLeft = x;
        break;
      }
    }

    // right line
    for ( int x=w-1;x>=0;x-- ) {
      if ( !vLineClear(x) ) {
        downSampleRight = x;
        break;
      }
    }
  }

  /**
   * Called to downsample a quadrant of the image.
   *
   * @param x The x coordinate of the resulting
   * downsample.
   * @param y The y coordinate of the resulting
   * downsample.
   * @return Returns true if there were ANY pixels
   * in the specified quadrant.
   */
  protected boolean downSampleQuadrant(int x,int y)
  {
    int w = entryImage.getWidth(this);
    int startX = (int)(downSampleLeft+(x*ratioX));
    int startY = (int)(downSampleTop+(y*ratioY));
    int endX = (int)(startX + ratioX);
    int endY = (int)(startY + ratioY);

    for ( int yy=startY;yy<=endY;yy++ ) {
      for ( int xx=startX;xx<=endX;xx++ ) {
        int loc = xx+(yy*w);

        if ( pixelMap[ loc  ]!= -1 )
          return true;
      }
    }

    return false;
  }


  /**
   * Called to downsample the image and store
   * it in the down sample component.
   */
  public void downSample()
  {
    int w = entryImage.getWidth(this);
    int h = entryImage.getHeight(this);

    PixelGrabber grabber = new PixelGrabber(
                                           entryImage,
                                           0,
                                           0,
                                           w,
                                           h,
                                           true);
    try {

      grabber.grabPixels();
      pixelMap = (int[])grabber.getPixels();
      findBounds(w,h);

      // now downsample
      SampleData data = sample.getData();

      ratioX = (double)(downSampleRight-
                        downSampleLeft)/(double)data.getWidth();
      ratioY = (double)(downSampleBottom-
                        downSampleTop)/(double)data.getHeight();

      for ( int y=0;y<data.getHeight();y++ ) {
        for ( int x=0;x<data.getWidth();x++ ) {
          if ( downSampleQuadrant(x,y) )
            data.setData(x,y,true);
          else
            data.setData(x,y,false);
        }
      }

      sample.repaint();
      repaint();
    } catch ( InterruptedException e ) {
    }
  }

  /**
   * Called to clear the image.
   */
  public void clear()
  {
    this.entryGraphics.setColor(Color.white);
    this.entryGraphics.fillRect(0,0,getWidth(),getHeight());
    this.downSampleBottom =
    this.downSampleTop =
    this.downSampleLeft =
    this.downSampleRight = 0;
    repaint();
  }
}