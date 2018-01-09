package com.heaton.bot;
import java.util.*;
import com.heaton.bot.*;
import java.net.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

/**
 * The HTMLPage class is used to parse an HTML page and store
 * that page, in a parsed form, in memory.
 * are exchanged with a webserver.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class HTMLPage {

  /**
   * A list of images on this page.
   */
  protected Vector _images = new Vector();

  /**
   * A list of links on this page.
   */
  protected Vector _links = new Vector();

  /**
   * A list of forms on this page.
   */
  protected Vector _forms = new Vector();

  /**
   * The underlying HTTP object for this page.
   */
  protected HTTP _http;

  /**
   * The base URL to resolve relative URL's.
   */
  protected String _base;

  /**
   * Construct an HTMLPage object.
   *
   * @param http The HTTP object(or subclass) to use to
   * download pages.
   */
  public HTMLPage(HTTP http)
  {
    _http = http;
  }


  /**
   * Called to open a page and read it in. If null
   * is specified for the callback, then the other
   * methods in this class may be used to look at
   * images, links and forms.
   *
   * @param url The URL to read.
   * @param callback A callback class to handle the parse, or null
   * to use the built in one.
   * @exception java.io.IOException
   * @exception javax.swing.text.BadLocationException
   */
  public void open(String url,
                   HTMLEditorKit.ParserCallback callback)
  throws IOException,BadLocationException
  {
    _http.send(url,null);
    _base = url;
    processPage(callback);
  }

  /**
   * Internal function called to start the parse.
   *
   * @param callback The callback object to use.
   * @exception java.io.IOException
   */
  protected void processPage(HTMLEditorKit.ParserCallback callback)
  throws IOException
  {
    StringReader r = new StringReader(_http.getBody());
    HTMLEditorKit.Parser parse = new HTMLParse().getParser();

    if ( callback==null ) {
      HTMLPage.Parser p=new HTMLPage.Parser();
      parse.parse(r,p,true);
    } else
      parse.parse(r,callback,false);

  }

  /**
   * Get the underlying HTTP object that was
   * sent to the constructor.
   *
   * @return The underlying HTTP object.
   */
  public HTTP getHTTP()
  {
    return _http;
  }

  /**
   * Get a list of all of the links from this page.
   * If this is to be used then null must have been
   * passed as the callback object to the open method.
   *
   * @return All links on this page.
   */
  public Vector getLinks()
  {
    return _links;
  }

  /**
   * Get a list of all of the images from this page.
   * If this is to be used then null must have been
   * passed as the callback object to the open method.
   *
   * @return A list of all of the images on this page.
   */
  public Vector getImages()
  {
    return _images;
  }

  /**
   * Get a list of all of the forms from this page.
   * If this is to be used then null must have been
   * passed as the callback object to the open method.
   *
   * @return A list of forms.
   */
  public Vector getForms()
  {
    return _forms;
  }

  /**
   * Called to perform a post for the specified form.
   *
   * @param form The form object to post.
   * @exception java.io.IOException
   */
  public void post(HTMLForm form)
  throws IOException
  {
    _http.getClientHeaders().set("Content-Type",
                                 "application/x-www-form-urlencoded");
    _http.send(form.getAction(),form.toString());
    processPage(null);
  }

  /**
   * Get the URL that is represented by this page.
   *
   * @return The URL that is represented by this page.
   */
  public String getURL()
  {
    return _http.getURL();
  }

  /**
   * Called internally to add an image to the list.
   *
   * @param img The image to add.
   */
  protected void addImage(String img)
  {
    img = URLUtility.resolveBase(_base,img);
    for ( int i=0;i<_images.size();i++ ) {
      String s = (String)_images.elementAt(i);
      if ( s.equalsIgnoreCase(img) )
        return;
    }
    _images.addElement(img);
  }

  /**
   * A HTML parser callback used by this class to
   * detect links, images and forms.
   *
   * @author Jeff Heaton
   * @version 1.0
   */
  protected class Parser
    extends HTMLEditorKit.ParserCallback {

    /**
     * Used to build up data for an HTML form.
     */
    protected HTMLForm _tempForm;

    /**
     * Used to build up options for an HTML form.
     */
    protected AttributeList _tempOptions;

    /**
     * Used to build up options for an HTML form.
     */
    protected Attribute _tempElement = new Attribute();

    /**
     * Holds the prompt text(just before or after a control.
     */
    protected String _tempPrompt = "";

    /**
     * Holds the link till the end link is found
     */
    protected Link _tempLink;





    /**
     * Called to handle comments.
     *
     * @param data The comment.
     * @param pos The position.
     */
    public void handleComment(char[] data,int pos)
    {
    }

    /**
     * Called to handle an ending tag.
     *
     * @param t The ending tag.
     * @param pos The position.
     */
    public void handleEndTag(HTML.Tag t,int pos)
    {
      if ( t==HTML.Tag.OPTION ) {
        if ( _tempElement!=null ) {
          _tempElement.setName(_tempPrompt);
          _tempOptions.add(_tempElement);
          _tempPrompt = "";
        }
        _tempElement = null;
      } else if ( t==HTML.Tag.FORM ) {
        if ( _tempForm!=null )
          _forms.addElement(_tempForm);
        _tempPrompt = "";
      } else if ( t==HTML.Tag.A ) {
        if ( _tempLink!=null )
          _tempLink.setPrompt(_tempPrompt);
        _tempPrompt = "";
      }

    }

    /**
     * Called to handle an error. Not used.
     *
     * @param errorMsg The error.
     * @param pos The position.
     */
    public void handleError(String errorMsg,int pos)
    {
    }

    /**
     * Called to handle a simple tag.
     *
     * @param t The simple tag.
     * @param a The attribute list.
     * @param pos The position.
     */
    public void handleSimpleTag(HTML.Tag t,
                                MutableAttributeSet a,int pos)
    {
      handleStartTag(t,a,pos);
    }

    /**
     * Called to handle a starting tag.
     *
     * @param t The starting tag.
     * @param a The attribute list.
     * @param pos The position.
     */
    public void handleStartTag(HTML.Tag t,
                               MutableAttributeSet a,int pos)
    {
      String type = "";

      // is it some sort of a link
      String href = (String)a.getAttribute(HTML.Attribute.HREF);

      if ( (href!=null) && (t!=HTML.Tag.BASE) ) {
        String alt = (String)a.getAttribute(HTML.Attribute.ALT);
        Link link = new Link(
                            alt,
                            URLUtility.resolveBase(_base,href),
                            null);
        _links.addElement(_tempLink=link);
      } else if ( t==HTML.Tag.OPTION ) {
        _tempElement = new Attribute();
        _tempElement.setName("");
        _tempElement.setValue((String)a.getAttribute(HTML.Attribute.VALUE));
      } else if ( t==HTML.Tag.SELECT ) {
        if ( _tempForm==null )
          return;

        _tempOptions = new AttributeList();
        _tempForm.addInput(
                          (String)a.getAttribute(HTML.Attribute.NAME),
                          null,
                          "select",
                          _tempPrompt,
                          _tempOptions);
        _tempPrompt = "";
      } else if ( t==HTML.Tag.TEXTAREA ) {
        if ( _tempForm==null )
          return;

        _tempForm.addInput(
                          (String)a.getAttribute(HTML.Attribute.NAME),
                          null,
                          "textarea",
                          _tempPrompt,
                          null);
        _tempPrompt = "";
      }

      else if ( t==HTML.Tag.FORM ) {
        if ( _tempForm!=null )
          _forms.addElement(_tempForm);

        String action =
        (String)a.getAttribute(HTML.Attribute.ACTION);
        if ( action!=null ) {
          try {
            URL aurl = new URL(new URL(_http.getURL()),action);
            action = aurl.toString();
          } catch ( MalformedURLException e ) {
            action = null;
          }
        }

        _tempForm = new HTMLForm(
                                (String)a.getAttribute(HTML.Attribute.METHOD),
                                action );
        _tempPrompt = "";
      } else if ( t==HTML.Tag.INPUT ) {
        if ( _tempForm==null )
          return;
        if ( t!=HTML.Tag.INPUT ) {
          type = (String)a.getAttribute(HTML.Attribute.TYPE);
          if ( type==null )
            return;
        } else
          type = "select";

        if ( type.equalsIgnoreCase("text") ||
             type.equalsIgnoreCase("edit") ||
             type.equalsIgnoreCase("password") ||
             type.equalsIgnoreCase("select") ||
             type.equalsIgnoreCase("hidden") ) {
          _tempForm.addInput(
                            (String)a.getAttribute(HTML.Attribute.NAME),
                            (String)a.getAttribute(HTML.Attribute.VALUE),
                            type,
                            _tempPrompt,
                            null);
          _tempOptions = new AttributeList();
        }
      } else if ( t==HTML.Tag.BASE ) {
        href = (String)a.getAttribute(HTML.Attribute.HREF);
        if ( href!=null )
          _base = href;
      } else if ( t==HTML.Tag.IMG ) {
        String src = (String)a.getAttribute(HTML.Attribute.SRC);
        if ( src!=null )
          addImage(src);
      }

    }

    /**
     * Called to handle text.
     *
     * @param data The text.
     * @param pos The position.
     */
    public void handleText(char[] data,int pos)
    {
      _tempPrompt += new String(data) + " ";
    }

  }
}
