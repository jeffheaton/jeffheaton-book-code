package com.heaton.bot;

/**
 * The Attribute class stores a list of named value pairs.
 * Optionally the value can have delimiters such as ' or ".
 *
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 * @see AttributeList
 */
public class Attribute implements Cloneable {

  /**
   * The name of this attribute.
   */
  private String _name;

  /**
   * The value of this attribute.
   */
  private String _value;

  /**
   * The delimiter for the value of this
   * attribute(i.e. " or ').
   */
  private char _delim;

  /**
   * Clone this object using the cloneable interface.
   *
   * @return A new object which is a clone of the
   * the specified object.
   */
  public Object clone()
  {
    return new Attribute(_name,_value,_delim);
  }

  /**
   * Construct a new Attribute.  The name, delim and value
   * properties can be specified here.
   *
   * @param name The name of this attribute.
   * @param value The value of this attribute.
   * @param delim The delimiter character for the value.
   * For example a " or '.
   */
  public Attribute(String name,String value,char delim)
  {
    _name = name;
    _value = value;
    _delim = delim;
  }

  /**
   * The default constructor.  Construct a blank attribute.
   */
  public Attribute()
  {
    this("","",(char)0);
  }

  /**
   * Construct an attribute without a delimiter.
   *
   * @param name The name of this attribute.
   * @param value The value of this attribute.
   */
  public Attribute(String name,String value)
  {
    this(name,value,(char)0);
  }

  /**
   * Get the name of this attribute.
   *
   * @return The name of this attribute.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the value of this attribute.
   *
   * @return The value of this attribute.
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Returns the delimiter used for the value of this attribute.
   *
   * @return The delimiter used for the value of this attribute.
   */
  public char getDelim()
  {
    return _delim;
  }

  /**
   * Set the name of this attribute.
   *
   * @param name The name of this attribute.
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Set the value of this attribute.
   *
   * @param value The value of this attribute.
   */
  public void setValue(String value)
  {
    _value = value;
  }

  /**
   * Set the delimiter of the value of this attribute.
   *
   * @param ch The delimiter of the value of this attribute.
   */
  public void setDelim(char ch)
  {
    _delim = ch;
  }
}
