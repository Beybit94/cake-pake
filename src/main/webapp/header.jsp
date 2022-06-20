<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="local"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div class="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom box-shadow">
    <a href="${contextPath}/main.jsp" class="my-0 mr-md-auto font-weight-normal">Home</a>
    <c:if test="${sessionScope.user eq null}">
        <nav class="my-2 my-md-0 mr-md-3">
            <a class="p-2 text-dark" href="${contextPath}/register.jsp"><fmt:message key="button.register"/></a>
            <a class="p-2 text-dark" href="${contextPath}/login.jsp"><fmt:message key="button.login"/></a>
        </nav>
    </c:if>
    <c:if test="${sessionScope.user ne null}">
        <nav class="my-2 my-md-0 mr-md-3">
            <a class="p-2 text-dark" href="#">Features</a>
            <a class="p-2 text-dark" href="#">Enterprise</a>
            <a class="p-2 text-dark" href="#">Support</a>
        </nav>
        <a class="btn btn-outline-primary" href="${contextPath}/profile.jsp">${sessionScope.user.userName}</a>
        <form class="form-inline my-2 my-lg-0" action="UserLogout" method="post" style="margin-left: 5px;">
            <button type="submit" class="btn btn-secondary"><fmt:message key="button.logout"/></button>
        </form>
    </c:if>

</div>