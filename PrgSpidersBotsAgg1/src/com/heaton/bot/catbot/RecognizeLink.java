package com.heaton.bot.catbot;

import java.util.*;
import com.heaton.bot.*;

/**
 * The link recognizer is used to search for a specific
 * text or graphic link on the page. To locate this link
 * the recognizer uses ALT tags, the text linked, and
 * even the HREF itself to establish the identity of
 * the link.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class RecognizeLink extends Recognize
{

  /**
   * The string that is being searched for.
   */
  String _search;

  /**
   * The HREF found in the target link.
   */
  String _targetHREF;

  /**
   * The constructor. Pass the controller on
   * to the parent.
   *
   * @param controller
   */
  public RecognizeLink(CatBot controller)
  {
    super(controller);
  }

  /**
   * Returns true if the link has already been
   * recognized.
   *
   * @return True if the link has already been recognized.
   */
  public boolean isRecognized()
  {
    return(_targetHREF!=null);
  }


  /**
   * Returns the search string being used to find
   * the link.
   *
   * @return The search string being used to find the
   * link.
   */
  public String getSearch()
  {
    return _search;
  }

  /**
   * Sets the search string being used to find the link.
   *
   * @param s The search string being used to find the link.
   */
  public void setSearch(String s)
  {
    _search = s.toUpperCase();
  }


  /**
   * Returns true if this page can be recognized.
   *
   * @param page The page to look at.
   * @return True if this page can be recognized.
   */
  public boolean isRecognizable(HTMLPage page)
  {
    for(Enumeration e = page.getLinks().elements() ; e.hasMoreElements() ;)
    {
      Link link = (Link)e.nextElement();
      String alt = link.getALT();
      String href = link.getHREF();
      String prompt = link.getPrompt();
      if(prompt==null)
        prompt="";
      if(alt==null)
        alt="";
      if(href==null)
        continue;

      if( ( alt.toUpperCase().indexOf(_search)!=-1) ||
          ( prompt.toUpperCase().indexOf(_search)!=-1) ||
          ( href.toUpperCase().indexOf(_search)!=-1) )
      {
        _targetHREF = link.getHREF();
        Log.log(Log.LOG_LEVEL_NORMAL,"Recognized a link:" + _search);
        return true;
      }
    }
    return false;
  }

  /**
   * The internal perform of this class will scan
   * all forms of links on the page searching for
   * the text specified by the setSearch method.
   *
   * @param page The page to look at.
   * @return True if successful.
   * @exception java.io.IOException
   * @exception javax.swing.text.BadLocationException
   */
  protected boolean internalPerform(HTMLPage page)
    throws java.io.IOException,
      javax.swing.text.BadLocationException
  {
    if(_targetHREF!=null)
    {
      page.open(_targetHREF,null);
      return true;
    }
    else return false;
  }

}
