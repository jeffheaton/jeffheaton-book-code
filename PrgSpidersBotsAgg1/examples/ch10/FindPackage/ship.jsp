<%@page import="com.heaton.bot.ship.*" %>
<h1>Track Packages</h1>
<table border=0>
<form method=post>
<tr><td>Enter the web address of the shipping company:</td>
<td><input name="url" value="<%=request.getParameter("url")%>"></td></tr>
<tr><td>Enter the package tracking number:</td>
<td><input name="track" value="<%=request.getParameter("track")%>"></td></tr>
<tr><td colspan="2">
<input type="submit" value="Submit"></td></tr>
</form>
</table>
<hr>
<%
if(request.getParameter("url")==null)
{%>
...Status will be displayed here...
<%
}
else
{
    String str 
      = FindPackage.findPackage(
        request.getParameter("url"),
        request.getParameter("track"), 
        "U.S.A.");
     out.println(str);
}%>