package com.heatonresearch.httprecipes.ch10.recipe2;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * Recipe #10.2: XML AJAX
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * This recipe shows how to access data from an AJAX website.
 * The data that will be extracted is XML.
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
public class AjaxXML
{
  // the size of a buffer
  public static int BUFFER_SIZE = 8192;

  
  /**
   * Obtains a XML node.  Specify a name such as "state.name"
   * to nest several layers of nodes.
   * @param e The parent node.
   * @param name The child node to search for. Specify levels with .'s.
   * @return Returns the node found.
   */
  private Node getXMLNode(Node e,String name)
  {
    StringTokenizer tok = new StringTokenizer(name,".");
    Node node = e;
    while( tok.hasMoreTokens() )
    {
      String currentName = tok.nextToken();
      
      NodeList list = node.getChildNodes();
      int len = list.getLength();
      for(int i=0;i<len;i++)
      {
        Node n = list.item(i);
        if( n.getNodeName().equals(currentName) )
        {        
          node = n;
          break;
        }
      }       
    }        
    return node;
  }
  
  /**
   * Obtain the specified XML attribute from the specified node.
   * @param e The XML node to obtain an attribute from.
   * @param name The name of the attribute.
   * @return Returns the value of the attribute.
   */
  private String getXMLAttribute(Node e,String name)
  {
    NamedNodeMap map = e.getAttributes();
    Attr attr = (Attr)map.getNamedItem(name);
    return attr.getNodeValue();
  }
  
  /**
   * Get the text for the specified XML node.
   * @param e The parent node.
   * @param name The child node to search for. Specify levels with .'s.
   * @return The text for the specified XML node.
   */
  private String getXMLText(Element e, String name)
  {
    Node node = getXMLNode(e,name);
    NodeList nl = node.getChildNodes();
    for(int i=0;i<nl.getLength();i++)
    {
      Node n = nl.item(i);
      if( n.getNodeType() == Node.TEXT_NODE )
        return n.getNodeValue();
      
    }
    return null;
  }

  /**
   * Download the information for the specified state.  This bot uses
   * a AJAX web site to obtain the XML message.
   * @param state The state code to look for(i.e. MO).
   * @throws IOException Thrown if there is a communication error.
   * @throws SAXException Thrown if there is an error parsing XML.
   * @throws ParserConfigurationException Thrown if there is an error obtaining the parser.
   */
  public void process(String state) throws IOException, SAXException, ParserConfigurationException
  {
    URL url = new URL("http://www.httprecipes.com/1/10/request.php");

    String request = "<request type=\"state\"><code>"+state+"</code></request>";
    
    URLConnection http = url.openConnection();
    http.setDoOutput(true);
    OutputStream os = http.getOutputStream();
    os.write(request.getBytes());
    InputStream is = http.getInputStream();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document d = factory.newDocumentBuilder().parse(is);
    
    Element e = d.getDocumentElement();
    Node stateNode = getXMLNode(e,"state");
    String id = getXMLAttribute(stateNode,"id");
    System.out.println( "State Name:" + getXMLText(e,"state.name") );
    System.out.println( "Code:" + getXMLText(e,"state.code") );
    System.out.println( "Capital:" + getXMLText(e,"state.capital") );
    System.out.println( "URL:" + getXMLText(e,"state.url") );
    System.out.println( "ID:" + id);
  }
  

  /**
   * Typical Java main method, create an object, and then
   * start the object passing arguments. If insufficient 
   * arguments are provided, then display startup 
   * instructions.
   * 
   * @param args Program arguments.
   */
  public static void main(String args[])
  {
    try
    {
      if (args.length != 1)
      {
        System.out.println("Usage:\njava AjaxXML [state code, i.e. MO]");
      } else
      {
        AjaxXML d = new AjaxXML();
        d.process(args[0]);
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }  
}
