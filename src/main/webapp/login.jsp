<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>
<%@ page import="kz.cake.web.helpers.constants.RequestParameters" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Cake - <fmt:message key="page.login"/></title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value="${ActionNames.AuthLogin.name}"/>
    </jsp:include>
    <div class="row">
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

            <form action="${ActionNames.AuthLogin.name}" method="post">
                <div class="form-group">
                    <label for="username"><b><fmt:message key="label.login"/></b></label>
                    <input type="text" class="form-control" name="${RequestParameters.username.name}" id="username" required>
                </div>
                <div class="form-group">
                    <label for="password"><b><fmt:message key="label.password"/></b></label>
                    <input type="password" class="form-control" name="${RequestParameters.password.name}" id="password" required>
                </div>

                <button type="submit" class="btn btn-primary"><fmt:message key="button.login"/></button>
            </form>
        </div>
    </div>

</div>

<jsp:include page="footer.jsp"/>
</body>
</html>