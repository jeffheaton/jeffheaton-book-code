package com.heaton.bot;

import java.io.*;

/**
 * This filter is used to 64-bit encode the specified string.
 * This allows a string to be displayed with only ASCII characters.
 * It is also used to provide HTTP authorization.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
class Base64OutputStream extends FilterOutputStream {

  /**
   * The constructor.
   *
   * @param out The stream used to write to.
   */
  public Base64OutputStream(OutputStream out)
  {
    super(out);
  }

  /**
   * Write a byte to be encoded.
   *
   * @param c The character to be written.
   * @exception java.io.IOException
   */
  public void write(int c) throws IOException
  {
    _buffer[_index] = c;
    _index++;
    if ( _index==3 ) {
      super.write(toBase64[(_buffer[0]&0xfc)>>2]);
      super.write(toBase64[((_buffer[0] &0x03)<<4) |
                           ((_buffer[1]&0xf0)>>4)]);
      super.write(toBase64[((_buffer[1]&0x0f)<<2) |
                           ((_buffer[2]&0xc0)>>6)]);
      super.write(toBase64[_buffer[2]&0x3f]);
      _column+=4;
      _index=0;
      if ( _column>=76 ) {
        super.write('\n');
        _column = 0;
      }
    }
  }

  /**
   * Ensure all bytes are written.
   *
   * @exception java.io.IOException
   */
  public void flush()
  throws IOException
  {
    if ( _index==1 ) {
      super.write(toBase64[(_buffer[2]&0x3f) >> 2]);
      super.write(toBase64[(_buffer[0]&0x03) << 4]);
      super.write('=');
      super.write('=');
    } else if ( _index==2 ) {
      super.write(toBase64[(_buffer[0]&0xfc) >> 2]);
      super.write(toBase64[((_buffer[0]&0x03)<<4)|
                           ((_buffer[1]&0xf0)>>4)]);
      super.write(toBase64[(_buffer[1]&0x0f)<<2]);
      super.write('=');
    }
  }

  /**
   * Allowable characters for base-64.
   */
  private static char[] toBase64 =
  { 'A','B','C','D','E','F','G','H',
    'I','J','K','L','M','N','O','P',
    'Q','R','S','T','U','V','W','X',
    'Y','Z','a','b','c','d','e','f',
    'g','h','i','j','k','l','m','n',
    'o','p','q','r','s','t','u','v',
    'w','x','y','z','0','1','2','3',
    '4','5','6','7','8','9','+','/'};

  /**
   * Current column.
   */
  private int _column=0;

  /**
   * Current index.
   */
  private int _index=0;

  /**
   * Outbound buffer.
   */
  private int _buffer[] = new int[3];

}
