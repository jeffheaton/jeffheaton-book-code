import java.net.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;
import com.heaton.bot.*;
import java.io.*;

/**
 * Example program from Chapter 6
 * Programming Spiders, Bots and Aggregators in Java
 * Copyright 2001 by Jeff Heaton
 *
 *
 * This example program parses a XML file. This file
 * may come from either the Internet or a local drive.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class ParseXML {
  /** Current indentention */
  static private int _indent = 0;

  /**
   * Display common data for every type of element.
   *
   * @param n The node.
   */
  static private void printlnCommon(Node n)
  {
    System.out.print(" nodeName=\"" + n.getNodeName() + "\"");

    String val = n.getNamespaceURI();
    if ( val != null )
      System.out.print(" uri=\"" + val + "\"");

    val = n.getPrefix();
    if ( val != null )
      System.out.print(" pre=\"" + val + "\"");

    val = n.getLocalName();
    if ( val != null )
      System.out.print(" local=\"" + val + "\"");

    val = n.getNodeValue();
    if ( val != null ) {
      System.out.print(" nodeValue=");
      if ( val.trim().equals("") ) {
        // Whitespace
        System.out.print("[WS]");
      } else {
        System.out.print("\"" + n.getNodeValue() + "\"");
      }
    }
    System.out.println();
  }

  /**
   * Indent the proper number of spaces for the display.
   */
  static private void doIndentation()
  {
    for ( int i = 0; i < _indent; i++ )
      System.out.print("  ");
  }

  /**
   * Display an idividual node. Called recursivly.
   *
   * @param n
   */
  static private void display(Node n)
  {
    // Indent to the current level before printing anything
    doIndentation();

    int type = n.getNodeType();
    switch ( type ) {
    case Node.ATTRIBUTE_NODE:
      System.out.print("ATTR:");
      printlnCommon(n);
      break;
    case Node.CDATA_SECTION_NODE:
      System.out.print("CDATA:");
      printlnCommon(n);
      break;
    case Node.COMMENT_NODE:
      System.out.print("COMM:");
      printlnCommon(n);
      break;
    case Node.DOCUMENT_FRAGMENT_NODE:
      System.out.print("DOC_FRAG:");
      printlnCommon(n);
      break;
    case Node.DOCUMENT_NODE:
      System.out.print("DOC:");
      printlnCommon(n);
      break;
    case Node.DOCUMENT_TYPE_NODE:
      System.out.print("DOC_TYPE:");
      printlnCommon(n);

      // Print entities if any
      NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
      _indent += 2;
      for ( int i = 0; i < nodeMap.getLength(); i++ )
        display((Entity)nodeMap.item(i));

      _indent -= 2;
      break;
    case Node.ELEMENT_NODE:
      System.out.print("ELEM:");
      printlnCommon(n);
      NamedNodeMap atts = n.getAttributes();
      _indent += 2;
      for ( int i = 0; i < atts.getLength(); i++ )
        display( (Node) atts.item(i) );
      _indent -= 2;
      break;
    case Node.ENTITY_NODE:
      System.out.print("ENT:");
      printlnCommon(n);
      break;
    case Node.ENTITY_REFERENCE_NODE:
      System.out.print("ENT_REF:");
      printlnCommon(n);
      break;
    case Node.NOTATION_NODE:
      System.out.print("NOTATION:");
      printlnCommon(n);
      break;
    case Node.PROCESSING_INSTRUCTION_NODE:
      System.out.print("PROC_INST:");
      printlnCommon(n);
      break;
    case Node.TEXT_NODE:
      System.out.print("TEXT:");
      printlnCommon(n);
      break;
    default:
      System.out.print("UNSUPPORTED NODE: " + type);
      printlnCommon(n);
      break;
    }

    // Print children if any
    _indent++;
    for ( Node child = n.getFirstChild(); child != null;
        child = child.getNextSibling() )
      display(child);

    _indent--;
  }


  /**
   * Main entry point.
   *
   * @param args The first command line parameter specifies the URL or filename.
   */
  public static void main(String args[])
  {
    InputStream f=null;

    if ( args.length!=1 ) {
      // display usage
      System.out.println("Usage:");
      System.out.println("javac ParseXML [qif file or URL to parse]");
      System.exit(0);
    }
    try {
      // first try and open it as a file
      f = new FileInputStream( new File(args[0]) );
    } catch ( FileNotFoundException e ) {
      // if it fails as a file, try as a URL
      try {
        HTTPSocket http = new HTTPSocket();
        http.send(args[0],null);
        f = new StringBufferInputStream(http.getBody());
      } catch ( UnknownHostException ee ) {
        // if it fails as a URL too, give up
        System.out.println("Can't open file or URL named: "
                           + args[0] + "(" + ee + ")");
        System.exit(0);
      } catch ( IOException ee ) {
        System.out.println( e );
        System.exit(0);
      }
    }

    // parse the XML
    try {
      DocumentBuilderFactory dbf =
      DocumentBuilderFactory.newInstance();

      DocumentBuilder db = null;
      db = dbf.newDocumentBuilder();

      Document doc = null;
      doc = db.parse(f);

      // Parse the document
      display(doc);
    } catch ( Exception e ) {
      System.out.println("Error: " + e );
    }

    // close out the file
    try {
      if ( f!=null )
        f.close();
    } catch ( IOException e ) {
      System.out.println("Error:" + e );
      System.exit(0);
    }
  }

}
