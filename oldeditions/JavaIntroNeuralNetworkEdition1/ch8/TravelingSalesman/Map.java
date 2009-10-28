import javax.swing.*;
import java.awt.*;

/**
 * Map
 * Copyright 2005 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 8
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

public class Map extends JPanel {

  /**
   * The TravelingSalesman object that owns this object.
   */
  protected TravelingSalesman owner;

  /**
   * Constructor.
   *
   * @param owner The TravelingSalesman object that owns
   * this object.
   */
  Map(TravelingSalesman owner)
  {
    this.owner = owner;
  }

  /**
   * Update the graphical display of the map.
   *
   * @param g The graphics object to use.
   */
  public void paint(Graphics g) {
    update(g);
  }

  /**
   * Update the graphical display of the map.
   *
   * @param g The graphics object to use.
   */
  public void update(Graphics g) {
    int width = getBounds().width;
    int height = getBounds().height;

    g.setColor(Color.black);
    g.fillRect(0,0,width,height);

    if ( !owner.started ) return;

    g.setColor(Color.green);
    for ( int i=0;i<TravelingSalesman.CITY_COUNT;i++ ) {
      int xpos = owner.cities[i].getx();
      int ypos = owner.cities[i].gety();
      g.fillOval(xpos-5,ypos-5,10,10);
    }

    g.setColor(Color.white);
    for ( int i=0;i<TravelingSalesman.CITY_COUNT;i++ ) {
      int icity = owner.chromosomes[0].getCity(i);
      if ( i!=0 ) {
        int last = owner.chromosomes[0].getCity(i-1);
        g.drawLine(
                  owner.cities[icity].getx(),
                  owner.cities[icity].gety(),
                  owner.cities[last].getx(),
                  owner.cities[last].gety());
      }
    }
  }

}




