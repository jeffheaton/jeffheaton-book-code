<%@page import="com.heaton.bot.weather.*" %>

<html><head><title>Current Weather</title></head>
<body>
<h1>Current Weather</h1>
<table border=0>
<%
    Weather a[] = Weather.getList();
    for(int i=0;i<a.length;i++)
    {%>
<tr><td><%=a[i].city%>&nbsp;&nbsp;</td><td><%=a[i].deg%> F</td></tr>
<%  }
    
    Weather.fileAggregate("wxtest.html");
%>
</table>
</body>
</html>