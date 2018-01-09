/**
 * The Parse class is the low-level text parsing class
 * that all other parsing classes are based on.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
package com.heaton.bot;
import com.heaton.bot.*;

class Parse extends AttributeList {
  public StringBuffer _source;
  protected int _idx;
  protected char _parseDelim;
  protected String _parseName;
  protected String _parseValue;
  public String _tag;

  public static boolean isWhiteSpace(char ch)
  {
    return( "\t\n\r ".indexOf(ch) != -1 );
  }

  public void eatWhiteSpace()
  {
    while ( !eof() ) {
      if ( !isWhiteSpace(_source.charAt(_idx)) )
        return;
      _idx++;
    }
  }

  public boolean eof()
  {
    return(_idx>=_source.length() );
  }

  public void parseAttributeName()
  {
    eatWhiteSpace();
    // get attribute name
    if ( (_source.charAt(_idx)=='\'')
       || (_source.charAt(_idx)=='\"') ) {
      _parseDelim = _source.charAt(_idx);
      _idx++;
      while ( _source.charAt(_idx)!=_parseDelim ) {
        _parseName+=_source.charAt(_idx);
        _idx++;
      }
      _idx++;
    } else {
      while ( !eof() ) {
        if ( isWhiteSpace(_source.charAt(_idx)) ||
             (_source.charAt(_idx)=='=') ||
             (_source.charAt(_idx)=='>') )
          break;
        _parseName+=_source.charAt(_idx);
        _idx++;
      }
    }
    eatWhiteSpace();
  }

  public void parseAttributeValue()
  {
    if ( _parseDelim!=0 )
      return;

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
        while ( !eof() &&
                !isWhiteSpace(_source.charAt(_idx)) &&
                (_source.charAt(_idx)!='>') ) {
          _parseValue+=_source.charAt(_idx);
          _idx++;
        }
      }
      eatWhiteSpace();
    }
  }


  void addAttribute()
  {
    Attribute a = new Attribute(_parseName,
      _parseValue,_parseDelim);
    add(a);
  }

  String getParseName()
  {
    return _parseName;
  }

  void setParseName(String s)
  {
    _parseName = s;
  }

  String getParseValue()
  {
    return _parseValue;
  }

  void setParseValue(String s)
  {
    _parseValue = s;
  }

  char getParseDelim()
  {
    return _parseDelim;
  }

  void setParseDelim(char s)
  {
    _parseDelim = s;
  }

}
