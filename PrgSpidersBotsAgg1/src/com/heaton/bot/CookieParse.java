package com.heaton.bot;

/**
 * This class is used to parse cookies that are transmitted
 * with the HTTP headers.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class CookieParse extends Parse {

  /**
   * Special version of the parseAttribute method.
   */
  public void parseAttributeValue()
  {
    eatWhiteSpace();
    if ( _source.charAt(_idx)=='=' ) {
      _idx++;
      eatWhiteSpace();
      if ( (_source.charAt(_idx)=='\'') ||
           (_source.charAt(_idx)=='\"') ) {
        _parseDelim = _source.charAt(_idx);
        _idx++;
        while ( _source.charAt(_idx)!=_parseDelim ) {
          _parseValue+=_source.charAt(_idx);
          _idx++;
        }
        _idx++;
      } else {
        while ( !eof() && (_source.charAt(_idx)!=';') ) {
          _parseValue+=_source.charAt(_idx);
          _idx++;
        }
      }
      eatWhiteSpace();
    }
  }
  /**
   * Called to parse this cookie.
   *
   * @return The return value is unused.
   */

  public boolean get()
  {
    // get the attributes
    while ( !eof() ) {
      _parseName="";
      _parseValue="";

      parseAttributeName();

      if ( _source.charAt(_idx)==';' ) {
        addAttribute();
        break;
      }

      // get the value(if any)
      parseAttributeValue();
      addAttribute();

      // move forward to the ; if there is one
      eatWhiteSpace();
      while ( !eof() ) {
        if ( _source.charAt(_idx++)==';' )
          break;
      }
    }
    _idx++;
    return false;
  }

  /**
   * Convert this cookie to a string to be sent as
   * an HTTP header.
   *
   * @return This cookie as a string.
   */
  public String toString()
  {
    String str;
    str = get(0).getName();
    str+= "=";
    str+= get(0).getValue();
    return str;

  }
}
