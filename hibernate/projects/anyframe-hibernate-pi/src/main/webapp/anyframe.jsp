<%@ page language="java" errorPage="/sample/common/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/sample/common/taglibs.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Anyframe Plugins</title>
	<link rel="stylesheet" href="<c:url value='/sample/css/admin.css'/>" type="text/css">
	<link rel="stylesheet" href="<c:url value='/sample/css/left.css'/>" type="text/css">
	<script type="text/javascript" src="<c:url value='/sample/javascript/CommonScript.js'/>"></script>
</head>
<body>
<table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
<!-- Top -->
  <tr>
    <td height="100%" align="left" valign="middle">
		<font size="5">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Anyframe Java 5.0.0.RC1</font>
    </td>
  </tr>
</table>
<!-- Body -->
<center>
<table class="scrollTable" width="90%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<thead>
		<tr>
			<th scope="col" style="border-right: 1px #CCCCCC solid"><font size="2">Installed Plugins</font></th>
			<th scope="col" style="border-right: 1px #CCCCCC solid"><font size="2">Generated CRUD Codes</font></th>
		</tr>
	</thead>
	<tbody>
		<tr class="board">
				<td class="underline">
					<!--hibernate-menu-START-->
					<a href="${ctx}/hibernateMovieFinder.do?method=list"><font size="2">- Hibernate 1.0.0.RC1</font></a><br/>
					<!--hibernate-menu-END-->
					<!--Add new menu here-->
				</td>
				<td align="left"></td>
		</tr>
	</tbody>
</table>
</center>
</body>
</html>