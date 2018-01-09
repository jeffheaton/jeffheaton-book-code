<%@page import="com.heaton.bot.translate.*" %>
<%
if(request.getParameter("url")==null)
{%>
<html>
<head>
<title>Translate</title>
</head>
<body>
<h1>Translate to Pig Latin</h1>
<form method="get">
Select a URL to translate to Pig Latin:<input type=text name=url 
value="http://www.kimmswick.com/" size="50">
<input type="submit" value="Translate">
</form>
</body>
</html>
<%
}
else
{
  try
  {
    String str = Translate.translate(
      request.getParameter("url"),"translate.jsp?url=");
    out.println( str );
  }
  catch(Exception e)
  {
    out.println("Error:" + e );
  }
}
%>