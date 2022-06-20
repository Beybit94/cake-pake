<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake-Pake web</title>
    <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet">
</head>
<body>
<jsp:invoke fragment="header"/>
<div class="container">
    <jsp:doBody/>
</div>
<jsp:invoke fragment="footer"/>

<script src="${contextPath}/js/jquery-3.6.0.min.js"></script>
<script src="${contextPath}/js/bootstrap.min.js"></script>
</body>
</html>