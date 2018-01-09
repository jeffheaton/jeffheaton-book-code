package com.heaton.bot.catbot;
import java.util.*;
import com.heaton.bot.*;

/**
 * A CatBot is a bot that is designed to be able
 * to get information from a variety of sites in
 * the same category. This class lays the framework
 * for CatBots.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class CatBot
{

  /**
   * A user ID for the catbot to use.
   * May not be used by some implementations
   * of the CatBot.
   */
  protected String _uid = "";

  /**
   * A password for the catbot to use.
   * May not be used by some implementations
   * of the CatBot.
   */
  protected String _pwd = "";

  /**
   * A country for the catbot to use.
   * May not be used by some implementations
   * of the CatBot.
   */
  protected String _country = "";


  /**
   * The URL that the CatBot starts at.
   */
  protected String _url = "";

  /**
   * The HTTP object to be used by this
   * CatBot.
   */
  protected HTTP _http;

  /**
   * A list of recognizers. See the Recognize class
   * for more info on what a Recognizer is.
   */
  protected Vector _recognizers = new Vector();

  /**
   * The prime recognizer that will recognize the
   * ultimate information that the CatBot is
   * seeking.
   */
  protected Recognize _primeRecognizer;

  /**
   * The constructor. Sets up the CatBot.
   *
   * @param http An HTTP object that should be used for
   * communication.
   */
  public CatBot(HTTP http)
  {
    _http = http;
  }

  /**
   * Sets a user id for the CatBot to use. This
   * parameter may or may not be used, depending
   * on the type of CatBot.
   *
   * @param uid A user id for the CatBot to use.
   */
  public void setUID(String uid)
  {
    _uid = uid;
  }

  /**
   * Gets a user ID that the CatBot may have been
   * using.
   *
   * @return A user id that the CatBot was using.
   */
  public String getUID()
  {
    return _uid;
  }


  /**
   * Sets a country for the CatBot to use. This
   * parameter may or may not be used, depending
   * on the type of CatBot.
   *
   * @param uid A user id for the CatBot to use.
   */
  public void setCountry(String country)
  {
    _country = country;
  }

  /**
   * Gets a country that the CatBot may have been
   * using.
   *
   * @return A user id that the CatBot was using.
   */
  public String getCountry()
  {
    return _country;
  }


  /**
   * Sets a password for the CatBot to use. This
   * parameter may or may not be used, depending
   * on the type of CatBot.
   *
   * @param pwd A password for the CatBot to use.
   */
  public void setPWD(String pwd)
  {
    _pwd = pwd;
  }

  /**
   * Gets a password that the CatBot may have been
   * using.
   *
   * @return A user id that the CatBot was using.
   */
  public String getPWD()
  {
    return _pwd;
  }

  /**
   * Set the URL that this CatBot should start on.
   *
   * @param url The URL that this CatBot should start on.
   */
  public void setURL(String url)
  {
    _url = url;
  }

  /**
   * Get the HTTP object to use.
   *
   * @return The HTTP object being used by the
   * CatBot.
   */
  public HTTP getHTTP()
  {
    return _http;
  }

  /**
   * Get the list of recognizers that should
   * be used by this CatBot.
   *
   * @return The list of recoginzers used by this
   * CatBot.
   */
  public Vector getRecognizers()
  {
    return _recognizers;
  }
  /**
   * This method can be called to cause the CatBot
   * to begin moving through the site with a set of
   * recognizers. The CatBot will continue until the
   * prime recognizer recognizes something, or all
   * recognizers have been exhausted.
   *
   * @return True if the recognition was successful.
   * @exception java.io.IOException
   * @exception javax.swing.text.BadLocationException
   */

  protected HTMLPage standardRecognition()
    throws java.io.IOException,
    javax.swing.text.BadLocationException
  {
    boolean recognizedOne;

    HTMLPage page = new HTMLPage(_http);
    page.open(_url,null);

    // loop so long as the prime recogizer is not
    // satisified and all other recognizers have
    // not been exhausted.
    do
    {
      recognizedOne = false;
      // first try the prime recognizer
      if(_primeRecognizer.perform(page))
        return page;
      for (Enumeration e = _recognizers.elements() ;
        e.hasMoreElements() ;)
      {
        Recognize rec = (Recognize)e.nextElement();
        if(!rec.isRecognized() && rec.perform(page))
        {
          // one was found, thats enough
          // the one that was just found moved
          // us to a new page so we must break
          // to restart the process.
          recognizedOne = true;
          break;
        }
      }
    } while( recognizedOne && !_primeRecognizer.isRecognized() );

    // if successful return the page
    if( _primeRecognizer.isRecognized() )
      return page;
    else
      return null;
  }

}
