
/**
 * The HTMLTag class is used to store an HTML tag.  This
 * includes the tag name and any attributes.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
package com.heaton.bot;
import com.heaton.bot.*;

public class HTMLTag extends AttributeList implements Cloneable {
  protected String _name;

  public Object clone()
  {
    int i;
    AttributeList rtn = new AttributeList();
    for ( i=0;i<_vec.size();i++ )
      rtn.add( (Attribute)get(i).clone() );
    rtn.setName(_name);

    return rtn;
  }

  public void setName(String s)
  {
    _name = s;
  }

  public String getName()
  {
    return _name;
  }

  public String getAttributeValue(String name)
  {
    Attribute a = get(name);
    if ( a==null )
      return null;
    return a.getValue();
  }
}
