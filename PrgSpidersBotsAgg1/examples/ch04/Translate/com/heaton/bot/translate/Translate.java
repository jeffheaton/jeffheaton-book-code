package com.heaton.bot.translate;
import java.util.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import com.heaton.bot.*;


/**
 * Example program from chapter 4
 * 
 * This class is used to translate websites into Pig Latin.
 * This class does three main things.
 * 
 * 1. Resolve all image URL's.
 * 2. Resolve all links and point them back to the translator.
 * 3. Translate actual text into Pig Latin.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class Translate extends HTMLEditorKit.ParserCallback {

  /**
   * Used to hold the page as it is translated.
   */
  protected String _buffer = "";

  /**
   * The base URL, which is the page being translated.
   * This is used to resolve relative links.
   */
  protected String _base;

  /**
   * This is the name of the URL that pages should be reflected
   * back to for further translation. This allows links to be
   * translated too.
   */
  protected String _reflect;

  /**
   * The constructor.
   * 
   * @param t The URL to translate.
   * @param reflect The page to reflect back to.
   * @see _reflect
   */
  Translate(String t,String reflect)
  {
    _base = t;
    _reflect = reflect;
  }

  /**
   * Get the translated page.
   * 
   * @return The translated page.
   */
  public String getBuffer()
  {
    return _buffer;
  }

  /**
   * Called by the parser whenever an HTML comment is found.
   * 
   * @param data The actual comment.
   * @param pos The position.
   */
  public void handleComment(char[] data,int pos)
  {
    _buffer+="<!-- ";
    _buffer+= new String(data);;
    _buffer+=" -->";
  }

  /**
   * A method used to handle HTML attributes. This is called
   * by most tag types.
   * 
   * @param attributes The attributes.
   */
  protected void attributes(AttributeSet attributes)
  {
    Enumeration e = attributes.getAttributeNames();
    while ( e.hasMoreElements() ) {
      Object name = e.nextElement();
      String value = (String)attributes.getAttribute(name);

      if ( name == HTML.Attribute.HREF )
        value = _reflect + URLUtility.resolveBase(_base,value);
      else
        if ( name == HTML.Attribute.SRC ||
             name == HTML.Attribute.LOWSRC )
        value = URLUtility.resolveBase(_base,value);

      _buffer+= " " + name + "=\"" + value + "\" ";
    }
  }

  /**
   * Handle an HTML start tag.
   * 
   * @param t The tag encountered.
   * @param a The attributes for that tag.
   * @param pos The position.
   */
  public void handleStartTag(HTML.Tag t,MutableAttributeSet a,int pos)
  {
    _buffer+="<" + t;
    attributes(a);
    if ( t == HTML.Tag.APPLET && 
         a.getAttribute(HTML.Attribute.CODEBASE)==null ) {
      String codebase = _base;
      if ( codebase.toUpperCase().endsWith(".HTM") || 
           codebase.toUpperCase().endsWith(".HTML") )
        codebase = codebase.substring(0,codebase.lastIndexOf('/'));

      _buffer+=" codebase=\"" + codebase + "\"";
    }
    _buffer+=">";
  }  

  /**
   * Handle an HTML end tag.
   * 
   * @param t The tag encountered.
   * @param pos The position.
   */
  public void handleEndTag(HTML.Tag t,int pos)
  {
    _buffer+="</" + t + ">";
  }

  /**
   * Handle a simple tag(one without an end tag)
   * 
   * @param t The tag encountered.
   * @param a The attributes for that tag.
   * @param pos The position.
   */
  public void handleSimpleTag(HTML.Tag t,MutableAttributeSet a,int pos)
  {
    if ( (t.toString()).startsWith("__") )
      return;
    _buffer+="<";
    if ( a.getAttribute(HTML.Attribute.ENDTAG)!=null ) {
      _buffer+="/"+t;
    } else {
      _buffer+=t;
      attributes(a);
    }
    _buffer+=">";
  }

  /**
   * Handle text.
   * 
   * @param data
   * @param pos
   */
  public void handleText(char[] data,int pos)
  {
    _buffer+=pigLatin(new String(data));
  }

  /**
   * Translate a string to Pig Latin.
   * 
   * @param english The English string.
   * @return A Pig Latin string.
   */
  protected String pigLatin( String english )
  {
    String pigWord; //an array of 
    String pigLatin;
    String temp;
    int mode;// 0=lower,1=cap,2=upper

    StringTokenizer englishWords = new StringTokenizer( english );
    //StringTokenizer is used to parse a string
    //creates an emptry string
    pigLatin = "";  
    //checks for the next word
    while ( englishWords.hasMoreTokens() ) {
      //puts the next word a temp
      temp = new String (englishWords.nextToken() ); 

      if ( (temp.charAt(0)>='A' && temp.charAt(0)<='Z') &&
           (temp.charAt(1)>='A' && temp.charAt(1)<='Z') )
        mode=2;
      else if ( (temp.charAt(0)>='A' && temp.charAt(0)<='Z') &&
                (temp.charAt(1)>='a' && temp.charAt(1)<='z') )
        mode=1;
      else
        mode=0;

      //if the word is more than one char long
      if ( temp.length() > 1 ) {
        //calls function to rearrange word	
        String start = new String(firstVowel(temp)); 
        pigWord = new String ( start + "ay" ); 
//takes the first letter of the word concatenates it and "ay" to the end
//of the word and puts it in the array
      } else {
        pigWord = temp + "ay"; //add "ay" to a one letter word
      }
      if ( pigWord.trim().length()>0 ) {
        if ( pigLatin.length()>0 )
          pigLatin+=" ";
        switch ( mode ) {
        case 0:
          pigLatin+=pigWord.toLowerCase();
          break;
        case 1:
          pigLatin+=pigWord.substring(0,1).toUpperCase();
          pigLatin+=pigWord.substring(1).toLowerCase();
          break;
        case 2:
          pigLatin+=pigWord.toUpperCase();
          break;            
        }
      }
    }

    return pigLatin;
  }

  /**
   * Find the first vowel in a string, used by the pigLatin
   * method.
   * 
   * @param s A string.
   * @return The location of the first vowel.
   */
  protected String firstVowel(String s)
  {
    String consonants = new String();
    String remain = new String();
    String newWord = "";
    int letterIndex = 0;  //The index of a letter in the word

    //checks each letter in the word for a vowel
    while ( letterIndex < s.length() ) {
      if ( (s.charAt(letterIndex) != 'a') && 
           (s.charAt(letterIndex) != 'A') &&
           (s.charAt(letterIndex) != 'e') && 
           (s.charAt(letterIndex) != 'E') &&
           (s.charAt(letterIndex) != 'i') && 
           (s.charAt(letterIndex) != 'I') &&
           (s.charAt(letterIndex) != 'o') && 
           (s.charAt(letterIndex) != 'O') &&
           (s.charAt(letterIndex) != 'u') && 
           (s.charAt(letterIndex) != 'U') ) {
        consonants += s.substring(letterIndex,(letterIndex + 1));
        letterIndex++;  //concatenats the consonants into a string
      } else {
        //puts the rest of the word into a string
        remain += s.substring(letterIndex,s.length()); 
        letterIndex = s.length();  //ends the loop
      }
    }
    if ( consonants != "" ) {
      //concatenates the consonants onto the end of the word
      newWord = remain + consonants;  
    } else {
      newWord = remain;
    }
    return newWord;
  }     


  /**
   * Static method that should be called to actually translate
   * a URL. This is the main entry point.
   * 
   * @param url The URL to translate.
   * @param callback The URL that all links should be redirected through for 
   * further translation.
   * @return The text of this page translated.
   * @exception java.io.IOException
   * @exception javax.swing.text.BadLocationException
   */
  public static String translate(String url,String callback)
  throws java.io.IOException,javax.swing.text.BadLocationException
  {
    Translate trans = new Translate(url,callback);
    HTMLPage page = new HTMLPage(new HTTPSocket());
    page.open(url,trans);
    return trans.getBuffer();
  }
}