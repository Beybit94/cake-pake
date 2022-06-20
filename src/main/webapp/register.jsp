<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <fmt:message key="page.register"/></title>
    <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="col-6 mx-auto">
        <c:if test="${requestScope.errors != null}">
            <c:forEach var="error" items="${errors}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong><fmt:message key="${error.text}"/></strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:forEach>
        </c:if>

        <form action="UserSave" method="post">
            <input type="hidden" class="form-control" name="role" id="role" value="user" />

            <div class="form-group">
                <label for="username"><b><fmt:message key="label.login"/></b></label>
                <input type="text" class="form-control" name="username" id="username" required>
            </div>
            <div class="form-group">
                <label for="password"><b><fmt:message key="label.password"/></b></label>
                <input type="password" class="form-control" name="password" id="password" required>
            </div>
            <div class="form-group">
                <label for="confirm"><b><fmt:message key="label.confirm"/></b></label>
                <input type="password" class="form-control" name="confirm" id="confirm" required>
            </div>

            <button type="submit" class="btn btn-primary"><fmt:message key="button.register"/></button>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>

<script src="${contextPath}/js/jquery-3.6.0.min.js"></script>
<script src="${contextPath}/js/bootstrap.min.js"></script>
</body>
</html>