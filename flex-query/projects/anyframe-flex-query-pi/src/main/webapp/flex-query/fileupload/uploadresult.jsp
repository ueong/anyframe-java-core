<%@ page contentType="text/html; charset=euc-kr" %><%@ page import="org.anyframe.plugin.flex.query.domain.Attached" %><%Attached attached = (Attached)request.getAttribute("attached");%><%=attached.getId()%>