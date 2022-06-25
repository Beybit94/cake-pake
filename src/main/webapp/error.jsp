<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <<fmt:message key="label.error"/></title>
    <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet">
</head>
<body>

<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value=""/>
    </jsp:include>
    <h4 class="text-center"><fmt:message key="error.undefinedError"/></h4>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
