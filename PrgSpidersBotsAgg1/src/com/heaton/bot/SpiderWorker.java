package com.heaton.bot;
import com.heaton.bot.*;
import java.net.*;

/**
 * The SpiderWorker class performs the actual work of
 * spidering pages.  It is implemented as a thread
 * that is created by the spider class.
 *
 * Copyright 2001 by Jeff Heaton
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class SpiderWorker extends Thread {

  /**
   * The URL that this spider worker
   * should be downloading.
   */
  protected String _target;

  /**
   * The owner of this spider worker class,
   * should always be a Spider object.
   * This is the class that this spider
   * worker will send its data to.
   */
  protected Spider _owner;

  /**
   * Indicates if the spider is busy or not.
   * true = busy
   * false = idle
   */
  protected boolean _busy;

  /**
   * A descendant of the HTTP object that
   * this class should be using for HTTP
   * communication. This is usually the
   * HTTPSocket class.
   */
  protected HTTP _http;

  /**
   * Constructs a spider worker object.
   *
   * @param owner The owner of this object, usually
   * a Spider object.
   * @param http
   */
  public SpiderWorker(Spider owner,HTTP http)
  {
    _http = http;
    _owner = owner;
  }

  /**
   * Returns true of false to indicate if
   * the spider is busy or idle.
   *
   * @return true = busy
   * false = idle
   */
  public boolean isBusy()
  {
    return _busy;
  }

  /**
   * The run method causes this thread to go idle
   * and wait for a workload. Once a workload is
   * received, the processWorkload method is called
   * to handle the workload.
   */
  public void run()
  {
    for ( ;; ) {
      _target = _owner.getWorkload();
      if ( _target==null )
        return;
      _owner.getSpiderDone().workerBegin();
      processWorkload();
      _owner.getSpiderDone().workerEnd();
    }
  }

  protected void processWorkload()
  {
    try {
      _busy = true;
      Log.log(Log  /**
   * The run method actually performs the
   * the workload assigned to this object.
   */
              .LOG_LEVEL_NORMAL,"Spidering " + _target );
      _http.send(_target,null);

      HTMLParser parse = new HTMLParser();
      parse._source = new StringBuffer(_http.getBody());
      _owner.processPage(_http);

      // find all the links
      while ( !parse.eof() ) {
        char ch = parse.get();
        if ( ch==0 ) {
          HTMLTag tag = parse.getTag();
          Attribute href = tag.get("HREF");
          if ( href==null )
            continue;

          URL target=null;
          try {
            target = new URL(new URL(_target),href.getValue());
          } catch ( MalformedURLException e ) {
            Log.log(Log.LOG_LEVEL_TRACE,
                    "Spider found other link: " + href );
            _owner.foundOtherLink(href.getValue());
            continue;
          }

          if ( _owner.getRemoveQuery() )
            target = URLUtility.stripQuery(target);
          target = URLUtility.stripAnhcor(target);


          if ( target.getHost().equalsIgnoreCase(
                                                new URL(_target).getHost()) ) {
            Log.log(Log.LOG_LEVEL_NORMAL,
                    "Spider found internal link: " + target.toString() );
            _owner.foundInternalLink(target.toString());
          } else {
            Log.log(Log.LOG_LEVEL_NORMAL,
                    "Spider found external link: " + target.toString() );
            _owner.foundExternalLink(target.toString());
          }

          _owner.completePage(_http,false);
        }
      }
    } catch ( java.io.IOException e ) {
      Log.log(Log.LOG_LEVEL_ERROR,
              "Error loading file("+ _target +"): " + e );
    } catch ( Exception e ) {
      Log.logException(
                      "Exception while processing file("+ _target +"): ", e );
    } finally {
      _owner.completePage(_http,true);
      _busy = false;

    }
  }

  /**
   * Returns the HTTP descendant that this
   * object should use for all HTTP communication.
   *
   * @return An HTTP descendant object.
   */
  public HTTP getHTTP()
  {
    return _http;
  }
}

