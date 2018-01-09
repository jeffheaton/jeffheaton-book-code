package com.heaton.bot.catbot;

import java.io.*;
import java.util.*;
import com.heaton.bot.*;

/**
 * This recognizer is called to handle a page that
 * contains a large list of countries. The country
 * that was specified in the CatBot class will be
 * located and used.
 *
 * @author Jeff Heaton
 */
public class RecognizeCountry extends Recognize
{

  /**
   * The targeted form, if recognized.
   */
  protected HTMLForm _targetForm = null;

  /**
   * The target form component, if recognized.
   */
  protected HTMLForm.FormElement _targetElement = null;

  /**
   * The constructor. Passes the controller to the
   * parent constructor.
   *
   * @param controller The CatBot controller object.
   */
  public RecognizeCountry(CatBot controller)
  {
    super(controller);
  }

  /**
   * Used to indicate if this recognizer has
   * already recognized the country specified.
   *
   * @return Returns true if the country has been recognized.
   */
  public boolean isRecognized()
  {
    return(_targetForm!=null);
  }

  /**
   * Returns true if the specified page can
   * be recognized to be a country page.
   *
   * @param page The page to look at.
   * @return Returns true if the page is recognized.
   */
  public boolean isRecognizable(HTMLPage page)
  {
    if(page.getForms()==null)
      return false;

    Vector forms = page.getForms();
    for (Enumeration e = forms.elements() ; e.hasMoreElements() ;)
    {
      HTMLForm form = (HTMLForm)e.nextElement();
      HTMLForm.FormElement element = has(form,"select");
      if(element!=null)
      {
        // look for a few known countries. USA is a bad example
        // is it USA? United States? America?
        // United States of America. We will use a few common ones
        // that do not have many name combinations:
        // Canada, France, Japan, Egypt.
        if( (findOption(element.getOptions(),"france")!=null ) ||
         (findOption(element.getOptions(),"canada")!=null ) ||
         (findOption(element.getOptions(),"japan")!=null ) ||
         (findOption(element.getOptions(),"egypt")!=null ) )
        {
          _targetForm = form;
          _targetElement = element;
          Log.log(Log.LOG_LEVEL_NORMAL,"Recognized a country page");
          return true;
        }
      }
     }
    return false;
  }

  /**
   * The internalPerform method will transmit
   * our country choice back to the Web server.
   *
   * @param page The page to look at.
   * @return True if successful.
   * @exception java.io.IOException
   */
  protected boolean internalPerform(HTMLPage page)
    throws java.io.IOException
  {
    if(_targetForm==null)
      return false;
    String code = findOption(
      _targetElement.getOptions(),
      _controller.getCountry());
    _targetForm.set(_targetElement.getName(),code);
    page.post(_targetForm);
    return true;
  }

}
