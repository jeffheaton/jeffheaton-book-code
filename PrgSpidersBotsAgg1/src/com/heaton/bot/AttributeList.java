package com.heaton.bot;
/**
 * The AttributeList class is used to store list of
 * Attribute classes.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */

import java.util.*;

public class AttributeList extends Attribute implements Cloneable {

  /**
   * An internally used Vector.  This vector contains
   * the entire list of attributes.
   */
  protected Vector _vec;

  /**
   * Make an exact copy of this object using the cloneable interface.
   *
   * @return A new object that is a clone of the specified object.
   */
  public Object clone()
  {
    int i;

    AttributeList rtn = new AttributeList();

    for ( i=0;i<_vec.size();i++ )
      rtn.add( (Attribute)get(i).clone() );
    return rtn;
  }

  /**
   * Create a new, empty, AttributeList.
   */
  public AttributeList()
  {
    super("","");
    _vec = new Vector();
  }

  /**
   * Locate an attribute by index number.  Index numbers
   * start at zero.
   *
   * @param id A zero based index that specifies the attribute
   * to retrieve.
   * @return The Attribute object that was found.
   */
  synchronized public Attribute get(int id)
  {
    if ( id<_vec.size() )
      return(Attribute)_vec.elementAt(id);
    else
      return null;
  }

  /**
   * Locate an attribute by its name.  The search is
   * case-insensitive.
   *
   * @param id The attribute name to search for.
   * @return The Attribute object that was located.
   */
  synchronized public Attribute get(String id)
  {
    int i=0;

    while ( get(i)!=null ) {
      if ( get(i).getName().equalsIgnoreCase(id) )
        return get(i);
      i++;
    }

    return null;
  }

  /**
   * Add the specified attribute to the list of attributes.
   *
   * @param a An attribute to add to this AttributeList.
   */
  synchronized public void add(Attribute a)
  {
    _vec.addElement(a);
  }

  /**
   * Clear all attributes from this AttributeList and return it
   * to a empty state.
   */
  synchronized public void clear()
  {
    _vec.removeAllElements();
  }

  /**
   * Returns true of this AttributeList is empty, with no attributes.
   *
   * @return True if this AttributeList is empty, false otherwise.
   */
  synchronized public boolean isEmpty()
  {
    return( _vec.size()<=0);
  }

  /**
   * Returns the number of Attributes in this AttributeList.
   *
   * @return The number of Attributes in this AttributeList.
   */
  synchronized public int length()
  {
    return _vec.size();
  }

  /**
   * If there is already an attribute with the specified name,
   * then it will have its value changed to match the specified value.
   * If there is no Attribute with the specified name, then one will
   * be created.  This method is case-insensitive.
   *
   * @param name The name of the Attribute to edit or create.  Case-insensitive.
   * @param value The value to be held in this attribute.
   */
  synchronized public void set(String name,String value)
  {
    if ( name==null )
      return;
    if ( value==null )
      value="";

    Attribute a = get(name);

    if ( a==null ) {
      a = new Attribute(name,value);
      add(a);
    } else
      a.setValue(value);
  }
}
