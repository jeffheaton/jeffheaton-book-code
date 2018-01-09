package com.heaton.bot.ship;
import com.heaton.bot.*;
import com.heaton.bot.catbot.*;
import java.net.*;
import java.io.*;
import javax.swing.text.*;

import com.heaton.bot.*;

public class ShipBot extends CatBot {
  protected String _packageID;
  public ShipBot(HTTP http)
  {
    super(http);
  }

  String getPackageID()
  {
    return _packageID;
  }

  public String lookup(String packageID)
  throws IOException, BadLocationException
  {
    _packageID = packageID;

    RecognizeCountry rcountry = new RecognizeCountry(this);
    RecognizeLink rlink = new RecognizeLink(this);
    rlink.setSearch("track");      
    RecognizePackagePage rship = new RecognizePackagePage(this);
    _recognizers.addElement(_primeRecognizer=rship);
    _recognizers.addElement(rlink);
    _recognizers.addElement(rcountry);
    HTMLPage page = standardRecognition();
    if ( page==null )
      return "Failed to understand site: " + _url;
    return page.getHTTP().getBody();

  }
}