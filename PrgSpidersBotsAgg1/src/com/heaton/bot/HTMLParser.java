

/**
 * The HTMLParse class is used to parse an HTML page.  It is
 * just a utility class, and does NOT store any values.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
package com.heaton.bot;
import com.heaton.bot.*;

public class HTMLParser extends Parse {
  public HTMLTag getTag()
  {
    int i;

    HTMLTag tag = new HTMLTag();
    tag.setName(_tag);

    for ( i=0;i<_vec.size();i++ )
      tag.add( (Attribute)get(i).clone() );
    return tag;
  }

  public String buildTag()
  {
    String buffer="<";
    buffer+=_tag;
    int i=0;
    while ( get(i)!=null ) {// has attributes
      buffer+=" ";
      if ( get(i).getValue() == null ) {
        if ( get(i).getDelim()!=0 )
          buffer+=get(i).getDelim();
        buffer+=get(i).getName();
        if ( get(i).getDelim()!=0 )
          buffer+=get(i).getDelim();
      } else {
        buffer+=get(i).getName();
        if ( get(i).getValue()!=null ) {
          buffer+="=";
          if ( get(i).getDelim()!=0 )
            buffer+=get(i).getDelim();
          buffer+=get(i).getValue();
          if ( get(i).getDelim()!=0 )
            buffer+=get(i).getDelim();
        }
      }
      i++;
    }
    buffer+=">";
    return buffer;
  }




  protected void parseTag()
  {
    _idx++;
    _tag="";
    clear();

    // Is it a comment?
    if ( (_source.charAt(_idx)=='!') &&
         (_source.charAt(_idx+1)=='-')&&
         (_source.charAt(_idx+2)=='-') ) {
      while ( !eof() ) {
        if ( (_source.charAt(_idx)=='-') &&
             (_source.charAt(_idx+1)=='-')&&
             (_source.charAt(_idx+2)=='>') )
          break;
        if ( _source.charAt(_idx)!='\r' )
          _tag+=_source.charAt(_idx);
        _idx++;
      }
      _tag+="--";
      _idx+=3;
      _parseDelim=0;
      return;
    }

    // Find the tag name
    while ( !eof() ) {
      if ( isWhiteSpace(_source.charAt(_idx)) || (_source.charAt(_idx)=='>') )
        break;
      _tag+=_source.charAt(_idx);
      _idx++;
    }

    eatWhiteSpace();

    // get the attributes
    while ( _source.charAt(_idx)!='>' ) {
      _parseName = "";
      _parseValue = "";
      _parseDelim=0;

      parseAttributeName();

      if ( _source.charAt(_idx)=='>' ) {
        addAttribute();
        break;
      }

      // get the value(if any)
      parseAttributeValue();
      addAttribute();
    }
    _idx++;
  }


  public char get()
  {
    if ( _source.charAt(_idx)=='<' ) {
      char ch=Character.toUpperCase(_source.charAt(_idx+1));
      if ( (ch>='A') && (ch<='Z') || (ch=='!') || (ch=='/') ) {
        parseTag();
        return 0;
      } else return(_source.charAt(_idx++));
    } else return(_source.charAt(_idx++));
  }

}
