package com.heaton.bot.catbot;

import com.heaton.bot.*;

/**
 * This class forms the base class for all
 * recognizers. Recognizers are an important
 * concept for the CatBot.
 * A recoginzer is a class that can recognize
 * and extract data from certian types of page.
 * The recogizer will provide the page with the
 * needed information so that the CatBot can
 * continue.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
abstract public class Recognize
{

  /**
   * The CatBot object that controls this object.
   */
  protected CatBot _controller;

  /**
   * Returns true if this page is recognized.
   *
   * @param page The page to look at.
   * @return Returns true if this page is recognized.
   */
  abstract public boolean isRecognizable(HTMLPage page);

  /**
   * This method is called internally to perform
   * the process of interracting with this page.
   * This is an abstract class that must be implemented
   * by child classes.
   *
   * @param page The page to perform against.
   * @return True if successful.
   * @exception java.io.IOException
   * @exception javax.swing.text.BadLocationException
   */
  abstract protected boolean internalPerform(HTMLPage page)
    throws java.io.IOException,
    javax.swing.text.BadLocationException;

  /**
   * Returns true if this page has already been
   * recognized.
   *
   * @return True if this page has already been recognized.
   */
  abstract public boolean isRecognized();

  /**
   * The constructor. This method stores the CatBot
   * controller for this recognizer.
   *
   * @param controller The CatBot controller for this object.
   */
  public Recognize(CatBot controller)
  {
    _controller = controller;
  }

  /**
   * Perform whatever task is done by this Recognize
   * object.
   *
   * @param page
   */
  public boolean perform(HTMLPage page)
  {
    try
    {
    if(!isRecognizable(page))
      return false;
    return internalPerform(page);
    }
    catch(java.io.IOException e)
    {
      Log.logException("CatBot IO exception during perform:",e);
      return false;
    }
    catch(javax.swing.text.BadLocationException e)
    {
      Log.logException("CatBot HTML Parse exception during perform:",e);
      return false;
    }
  }

  /**
   * Returns true if the specified form has
   * the specified type of component.
   *
   * @param form The form object that is to be searched.
   * @param hasWhat The search string
   * @return The component found.
   */
  public HTMLForm.FormElement has(HTMLForm form,String hasWhat)
  {
    if(form==null)
      return null;
    for(int i=0;i<form.length();i++)
    {
      HTMLForm.FormElement element = (HTMLForm.FormElement)form.get(i);
      if(element.getType().equalsIgnoreCase(hasWhat) )
        return element;
    }
    return null;
  }

  /**
   * Find the specified option in a select list.
   *
   * @param list The component list to search.
   * @param search The search string. Only partial match is needed.
   */
  public String findOption(AttributeList list,String search)
  {
    if(list==null)
      return null;
    search = search.toUpperCase();
    for(int i=0;i<list.length();i++)
    {
      Attribute element = list.get(i);
      if( element.getName().toUpperCase().indexOf(search)!=-1 )
        return element.getValue();
    }
    return null;
  }

  /**
   * Find a form prompt based on a partial
   * search string.
   *
   * @param form The form to search.
   * @param search The form text we are searching for.
   */
  public String findPrompt(HTMLForm form,String search)
  {
    search = search.toUpperCase();
    for(int i=0;i<form.length();i++)
    {
      HTMLForm.FormElement element =
        (HTMLForm.FormElement)form.get(i);
      String name = element.getName();
      if(name==null)
        continue;
      if( name.toUpperCase().indexOf(search)!=-1 )
      {
        return element.getName();
      }
    }
    return null;
  }
}
