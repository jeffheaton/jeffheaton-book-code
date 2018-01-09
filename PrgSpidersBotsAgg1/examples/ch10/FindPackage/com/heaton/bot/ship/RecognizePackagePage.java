package com.heaton.bot.ship;
import com.heaton.bot.*;
import com.heaton.bot.catbot.*;
import java.util.*;

public class RecognizePackagePage extends Recognize {
  HTMLForm _targetForm = null;
  String _targetControl;

  public RecognizePackagePage(CatBot controller)
  {
    super(controller);
  }

  public boolean isRecognized()
  {
    return(_targetForm!=null);
  }


  public boolean isRecognizable(HTMLPage page)
  {
    if ( page.getForms()==null )
      return false;

    Vector forms = page.getForms();
    for ( Enumeration e = forms.elements() ; e.hasMoreElements() ; ) {
      HTMLForm form = (HTMLForm)e.nextElement(); 
      String target = findPrompt(form,"track");
      if ( target!=null ) {
        _targetForm = form;
        _targetControl = target;
        Log.log(Log.LOG_LEVEL_NORMAL,"Recognized a package page");
        return true;
      }
      target = findPrompt(form,"number");
      if ( target!=null ) {
        _targetForm = form;
        _targetControl = target;
        Log.log(Log.LOG_LEVEL_NORMAL,"Recognized a package page");
        return true;
      }
    }
    return false;
  }

  protected boolean internalPerform(HTMLPage page)
  throws java.io.IOException
  {
    if ( _targetForm==null )
      return false;
    _targetForm.set(_targetControl,
                    ((ShipBot)_controller).getPackageID() );
    page.post(_targetForm);

    return true;
  }

}