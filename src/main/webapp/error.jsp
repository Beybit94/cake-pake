<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
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
    <jsp:include page="css.jsp"/>
</head>
<body>

<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value=""/>
    </jsp:include>
    <div class="row">
        <div class="col-12">
            <c:if test="${requestScope.errors != null}">
                <c:forEach var="error" items="${errors}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>${error.text}</strong>
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </c:forEach>
            </c:if>
            <h4 class="text-center"><fmt:message key="error.undefinedError"/></h4>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
