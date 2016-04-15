<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
  <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
  <title>Uploaded Image</title>
</head>
<body>
<h4><a href="<core:url value="/logout"/>">Logout</a></h4>
<h4><a href="uploadFile">Upload Another Image</a></h4>
<%
  if (session.getAttribute("uploadFile") != null
    && !(session.getAttribute("uploadFile")).equals("")) {
%>
<br>
<img src="<%=session.getAttribute("uploadFile")%>" alt="Uploaded Image" />
<%
  }
%>
<form:form modelAttribute="uploadItem" action="/recognize"
           name="recognize" method="get">
<table>
  <tr>
    <td><input name="recognizeFaces" type="submit" value="Recognize Faces" /></td>
    <td><input name="recognizeLabels" type="submit" value="Recognize Labels" /></td>
  </tr>
</table>
</form:form>
<%
  if (session.getAttribute("labels") != null) {
%>
<core:forEach var="label" items="${labels}">
<h3><core:out value="${label.getDescription()}"/>
<core:out value="${label.getScore()}"/></h3><p>
  </core:forEach>
      <%
    session.removeAttribute("labels");
  }
%>
</body>
</html>