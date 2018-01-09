<%@ page language="java" import="agent.*" %>
<html><head><title>JSP HTTP Get Example</title></head>
<h1>JSP HTTP Get Example</h3>
<form method="post">Enter URL:<input type="text" name="url">
<input type="submit"></form>
<%if( request.getMethod().equalsIgnoreCase("POST") )
{%>
<hr>
Listed below are all of the links found at
<b><%=request.getParameter("url")%></b>.<br><br>
<ul>
<%
  try
  {
    HTTPSocket sock = new HTTPSocket();    
    	      
    HTMLPage page1 = new HTMLPage(sock,request.getParameter("url"));
    for(int i=0;i<page1.length();i++)
    {
      HTMLTag tag = page1.getTag(i);
    	        
      if( tag.getName().equalsIgnoreCase("a") )
      {
        Attribute a = tag.getAttribute("HREF");
        if(a!=null)
        {
          URLParse p = new URLParse(request.getParameter("url"));
          p.extendBase(a.getValue());
          out.println("<li>Link: " + p.toString() + "</li>");
        }
      }
    }
  }
  catch(Exception e)
  {
    out.println("Error: " + e.getMessage() );
  }
}
%>
</ul>
</body></html>