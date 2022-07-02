<%@ page import="kz.cake.web.helpers.constants.ActionNames" %>

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
    <title>Cake - Home</title>
    <jsp:include page="css.jsp"/>
</head>
<body>
<div class="container">
    <jsp:include page="header.jsp">
        <jsp:param name="redirect" value=""/>
    </jsp:include>
    <div class="row">
        <div class="col-12">
            <div class="pricing-header mx-auto pb-4 border-bottom">
                <form method="POST" action="${ActionNames.ProductList.name}">
                    <div class="row">
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.city"/>
                            </label>
                            <select name="city" class="form-control local" style="width: 100%">
                                <option value=""></option>
                                <c:forEach items="${cities}" var="city">
                                    <option value="${city.id}" <c:if
                                            test="${city.id == filter.cityId}"> selected </c:if>>
                                            ${city.text}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.productSize"/>
                            </label>
                            <select name="productSize" class="form-control local" style="width: 100%">
                                <option value=""></option>
                                <c:forEach items="${productSizes}" var="productSize">
                                    <option value="${productSize.id}" <c:if
                                            test="${productSize.id == filter.sizeId}"> selected </c:if>>
                                            ${productSize.text}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-4">
                            <label>
                                <fmt:message key="label.productCategory"/>
                            </label>
                            <select name="productCategory" class="form-control local" style="width: 100%">
                                <option value=""></option>
                                <c:forEach items="${productCategories}" var="productCategory">
                                    <optgroup label="${productCategory.text}">
                                        <c:forEach items="${productCategory.children}" var="children">
                                            <option value="${children.id}" <c:if
                                                    test="${children.id == filter.categoryId}"> selected </c:if>>
                                                    ${children.text}
                                            </option>
                                        </c:forEach>
                                    </optgroup>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-4">
                            <label><fmt:message key="label.from"/></label>
                            <input type="number" class="form-control" name="fromPrice" value="${filter.fromPrice}">
                        </div>
                        <div class="col-4">
                            <label><fmt:message key="label.to"/></label>
                            <input type="number" class="form-control" name="toPrice" value="${filter.toPrice}">
                        </div>
                        <div class="col-4 text-left d-flex align-items-end">
                            <button type="submit" class="btn btn-primary"><fmt:message
                                    key="button.search"/></button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-deck pt-4">
                <c:forEach var="item" items="${products}">
                    <div class="col-md-4">
                        <div class="card box-shadow">
                            <div class="card-header">
                                <h4 class="my-0 font-weight-normal">${item.name}</h4>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${item.photos[0] eq null}">
                                        <img class="card-img-top " src="${contextPath}/static/img/no_image.png"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img class="card-img-top" src="${contextPath}/files${item.photos[0].path}"
                                             width="100%" height="100%"/>
                                    </c:otherwise>
                                </c:choose>
                                <h1 class="card-title">${item.priceText}</h1>
                                <ul class="list-unstyled mt-3 mb-4">
                                    <li><fmt:message key="label.city"/> : ${item.city.text}</li>
                                    <li><fmt:message key="label.productSize"/> : ${item.productSize.text}</li>
                                    <li><fmt:message key="label.productCategory"/> : ${item.productCategory.text}</li>
                                </ul>
                                <form method="post" action="${ActionNames.ProductDetail.name}">
                                    <input type="hidden" name="id" value="${item.id}"/>
                                    <button type="submit" class="btn btn-lg btn-block btn-outline-primary"><fmt:message key="button.detail"/>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
