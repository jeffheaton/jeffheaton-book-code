package com.heaton.bot;
import java.net.*;

/**
 * The HTMLForm class is used to create a response to an HTML form,
 * and then transmit it to a web server.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class HTMLForm extends AttributeList {

  /**
   * The method(i.e. GET or POST)
   */
  protected String _method;

  /**
   * The action, or the site to post the form to.
   */
  protected String _action;

  /**
   * Construct an HTMLForm object.
   *
   * @param method The method(i.e. GET or POST)
   * @param action The action, or the site that the result
   * should be posted to.
   */
  public HTMLForm(String method,String action)
  {
    _method = method;
    _action = action;
  }
  /**
   * Call to get the URL to post to.
   *
   * @return The URL to post to.
   */

  public String getAction()
  {
    return _action;
  }

  /**
   * @return The method(GET or POST)
   */
  public String getMethod()
  {
    return _method;
  }

  /**
   * Add a HTML INPUT item to this form.
   *
   * @param name The name of the input item.
   * @param value The value of the input item.
   * @param type The type of input item.
   */
  public void addInput(String name,String value,
                       String type,String prompt,AttributeList options)
  {
    FormElement e = new FormElement();
    e.setName(name);
    e.setValue(value);
    e.setType(type.toUpperCase());
    e.setOptions(options);
    e.setPrompt(prompt);
    add(e);
  }

  /**
   * Convert this form into the string that would be posted
   * for it.
   */
  public String toString()
  {
    String postdata;

    postdata = "";
    int i=0;
    while ( get(i)!=null ) {
      Attribute a = get(i);
      if ( postdata.length()>0 )
        postdata+="&";
      postdata+=a.getName();
      postdata+="=";
      if ( a.getValue()!=null )
        postdata+=URLEncoder.encode(a.getValue());
      i++;
    }
    return postdata;
  }

  public class FormElement extends Attribute {
    protected String _type;
    protected AttributeList _options;
    protected String _prompt;

    public void setOptions(AttributeList options)
    {
      _options = options;
    }

    public AttributeList getOptions()
    {
      return _options;
    }

    public void setType(String t)
    {
      _type = t;
    }

    public String getType()
    {
      return _type;
    }

    public String getPrompt()
    {
      return _prompt;
    }

    public void setPrompt(String str)
    {
      _prompt = str;
    }
  }
}
