package com.heatonresearch.httprecipes.ch11.recipe2;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.httprecipes.www._1._11.soap.*;

/**
 * Recipe #11.2: Using SOAP
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe uses a SOAP web service to translate a sentence
 * into Pig Latin.  This recipe uses the AXIS framework.
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class PigLatin
{
  /***
   * The main method.
   * @param args Not used.
   * @throws RemoteException Thrown if there is an error connecting 
   * the remote system.
   */
  public static void main(String args[]) throws RemoteException
  {
    
    try
    {
      PigLatinTranslatorLocator locator = new PigLatinTranslatorLocator();
      PigLatinTranslatorBindingStub trans = (PigLatinTranslatorBindingStub) locator.getPigLatinTranslatorPort();
      System.out.println( trans.translate("Hello world!") );
      
    } catch (ServiceException e)
    {
      e.printStackTrace();
    }
    
  }
}
